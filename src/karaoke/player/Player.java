package karaoke.player;

import karaoke.Composition;
import karaoke.Composition.LyricListener;
import karaoke.parser.MusicParser;

import java.io.File;

import edu.mit.eecs.parserlib.UnableToParseException;

//An immutable wrapper that plays parsed music
public class Player {
    private final Composition music;
    
    //AF(music) = a music player that can play this piece of music 
    //RI: True
    //Safety from rep exposure:
    //  All internal variables are private and final
    //  Client has no reference to internal variables
    //Thread Safety:
    //  Uses monitor pattern
    
    /**
     * Creates a new player that plays the music represented in the given file
     * @param input the file containing the music
     * @throws UnableToParseException if the file cannot be parsed
     */
    public Player(File input) throws UnableToParseException {
        //parse input into a Piece object
        this.music = MusicParser.parseFile(input);
    }
    
    /**
     * Creates a new player that plays the music represented by the given string
     * @param input the string containing the music
     * @throws UnableToParseException if the file cannot be parsed
     */
    public Player(String input) throws UnableToParseException {
        //parse input into a Piece object
        this.music = MusicParser.parse(input);
    }
    
    
    /**
     * Plays the music described by this.music
     */
    public synchronized void play() {
        //play the piece
        music.play();
    }
    
    /**
     * Adds a listener to the lyrics of the voice, voice. This listener is notified 
     * with an updated line of lyrics every time the voice changes notes
     * @param voice the voice whose lyrics are being listened to
     * @param listener the listener being added
     */
    public synchronized void addLyricListener(String voice, LyricListener listener) {
            music.addVoiceListener(voice, listener);
    }
}
