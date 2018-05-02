package karaoke.player;

import java.io.File;
import java.util.function.Function;


//An immutable wrapper that plays parsed music
public class Player {
    private final Piece music;
    
    // AF
    /**
     * Creates a new player that plays the music represented in the given file
     * @param input the file containing the music
     */
    public Player(File input) {
        //parse input into a Piece object
        this.music = parser.parse(input);
    }
    
    /**
     * Plays the music described by this.music
     */
    public void play() {
        //play the piece
        music.play();
    }
    
    /**
     * Adds a listener to the lyrics of the voice, voice. This listener is notified 
     * with an updated line of lyrics every time the voice changes notes
     * @param voice the voice whose lyrics are being listened to
     * @param listener the listener being added
     */
    public void addLyricListener(String voice, LyricListener listener) {
        music.addVoiceListener(voice, listener);
    }
        
    public interface LyricListener{
        /**
         * Displays the line given
         * @param line the line to be displayed
         */
        public void diplayLine(String line);
    }
}
