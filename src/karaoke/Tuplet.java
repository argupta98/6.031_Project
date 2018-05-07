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
    
    // Thread Safety
    
    public Tuplet(int tupletNumber, List<Music> notes) {
        this.tupletNumber = tupletNumber;
        this.notes = notes;
    }
    
    @Override
    public double duration() {
        double duration = 0;
        for(Music note: this.notes) {
            duration+=note.duration();
        }
        return duration;
    }
    
    
    @Override
    public void play(SequencePlayer player, double beat, Voice myVoice) {
        double currentBeat = beat;
        for(Music note: notes) {
            note.play(player, currentBeat, myVoice);
            currentBeat+=note.duration();
        }
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
