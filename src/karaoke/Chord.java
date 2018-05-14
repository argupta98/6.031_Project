package karaoke;

import java.util.Collections;
import java.util.List;

import karaoke.sound.SequencePlayer;

/**
 * A Chord is a set of notes that are played simultaneously
 */
public class Chord implements Music{
    
    private final List<Music> notes;
    private final int lyricIndex;
    
    // AF(notes, lyricIndex) = a Chord consisting of the notes in <notes>
    //                         associated with a lyric at index <lyricIndex>
    
    // Rep Invariant
    // lyricIndex is non-negative
    
    // Safety from Rep Exposure
    // all variables are private
    // lyricIndex is an immutable reference to an immutable value
    // we do not return references to notes
    
    // Thread safety Argument:
    // - Player Wrapper Class that plays music is a threadsafe datatype
    // - Player Class is the only class that is called upon from multiple threads
    
    /**
     * Creates a new instance of Chord
     * @param notes the notes in the chord
     * @param lyricIndex the index of the syllable corresponding to the chord in the list of syllables
     */
    public Chord(List<Music> notes, int lyricIndex) {
        this.notes = notes;
        this.lyricIndex = lyricIndex;
        checkRep();
    }
    
    private void checkRep() {
        assert notes != null;
        assert lyricIndex >= 0;

    }
    
    @Override
    public double duration() {
        return notes.get(0).duration();
    }
    
    @Override
    public void play(SequencePlayer player, double beat, Voice myVoice) {
        player.addEvent(beat, (newBeat) -> myVoice.notifyAll(lyricIndex));
        for(Music note: notes) {
            //Add a new voice that will do nothing when called (callback defined on the chord)
            note.play(player, beat, new Voice(new Rest(0), Collections.emptyList(), ""));
        }
    }
    
    @Override
    public String toString() {
        String outString = "[";
        for(Music note: notes) {
            outString+=note.toString();
        }
        return outString+"]";
    }

}
