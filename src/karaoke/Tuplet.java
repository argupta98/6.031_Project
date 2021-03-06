package karaoke;

import java.util.List;

import karaoke.sound.SequencePlayer;

/**
 * A Tuplet is a rhythm that involves dividing the beat into a different number
 * of equal subdivisions from that usually permitted by the time signature.
 */
public class Tuplet implements Music {
    
    private final int tupletNumber;
    private final List<Music> notes;
    
    // AF(tupletNumber, notes) = a tuplet of length <tupletNumber>
    //                           containing notes or chords in the order
    //                           they appear in <notes>
    
    // Rep Invariant
    // tupletNumber is 2, 3, or 4
    // notes has length tupletNumber
    // notes does not contain rests
    
    // Safety from Rep Exposure
    // tupletNumber is private and immutable
    // notes is private and references to the list are not returned to the client
    
    // Thread Safety Argument
    // - Player Wrapper Class that plays music is a threadsafe datatype
    // - Player Class is the only class that is called upon from multiple threads
    
    
    
    /**
     * Creates a new instance of the Tuplet
     * @param tupletNumber the number of notes in the tuplet
     * @param notes the notes in the Tuplet
     */
    public Tuplet(int tupletNumber, List<Music> notes) {
        this.tupletNumber = tupletNumber;
        this.notes = notes;
        checkRep();
    }
    
    private void checkRep() {
        assert tupletNumber >= 2 && tupletNumber <= 4;
        assert notes != null;
        assert notes.size() == tupletNumber: notes;
        for(Music note: notes) {
            assert !(note instanceof Rest);
        }
    }
    
    @Override
    public double duration() {
        double duration = 0;
        // Duration is the sum of durations for all notes 
        for(Music note: this.notes) {
            duration+=note.duration();
        }
        return duration;
    }
    
    
    @Override
    public void play(SequencePlayer player, double beat, Voice myVoice) {
        double currentBeat = beat;
        // Play each note after one another 
        for(Music note: notes) {
            note.play(player, currentBeat, myVoice);
            currentBeat+=note.duration();
        }
    }
    
    @Override
    public boolean equals(Object that) {
    	return that instanceof Tuplet && ((Tuplet)that).tupletNumber == tupletNumber
    			&& ((Tuplet)that).notes.equals(notes);
    }
    
    @Override
    public int hashCode() {
    	return ((Integer)tupletNumber).hashCode() + notes.hashCode();
    }
    
    @Override
    public String toString() {
        String outString =  "("+this.tupletNumber;
        for(Music note: notes) {
            outString+=note.toString();
        }
        return outString;
    }

}
