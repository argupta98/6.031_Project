package karaoke;

import java.util.List;
import java.util.Set;

import karaoke.sound.SequencePlayer;

public class Voice {
    
    
    private final Music piece;
    private final List<Syllable> allSyllables;
    private final Set<LyricListener> listeners;
    
    
    // Abstraction Function
    // AF(piece, allSyllables, listeners) => A Voice that sings the music piece which pronounces the syllables in allSyllables, and
    //                                       has listeners in the set listeners which provide callbacks for the lyric highlight 
    
    // Rep Invaraint
    // - piece cannot be null
    // - allSyllables cannot be null
    // - listeners cannot be null
    
    //Thread Safety Argument:
    // - 
    
   /** Listens for note being played and provides the necessary lyric */
    public interface LyricListener{
       /** Called when note is being played */
       public void notePlayed();
   }
    
    /**
     * Play music with in this particular voice. With each note played
     * a callback is made to the current lyric as well.
     * @param player to add the notes to be played
     */
    public void play (SequencePlayer player) {
        throw new RuntimeException("Not implemented");
    }
    
    /**
     * Adds a LyricListener for the voice
     * @param listener that will call for the correct lyric
     */
    public void addListener(LyricListener listener) {
        throw new RuntimeException("Not implemented");
    }
    

}