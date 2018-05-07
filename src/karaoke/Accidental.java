package karaoke;

import karaoke.sound.SequencePlayer;


/**
 * An Accidental is a note of a pitch that is not a member of the scale
 * indicated by the key signature. It can be either sharp, flat, or natural.
 */
public class Accidental implements Music {
    
    public enum Acc{
        SHARP,
        FLAT,
        NATURAL,
    }
    
    private final Acc accidental;
    private final Note note;
    
    // AF(accidental, note) = an Accidental where <note> is the note of the pitch
    //                        raised by a semitone if <accidental> is 'SHARP', lowered by
    //                        a semitone if <accidental> is flat, or unchanged if accidental
    //                        is 'NATURAL'
    
    // Rep Invariant
    // true
    
    // Safety from Rep Exposure
    // All fields private and immutable
    
    // Thread Safety
    //
    
    public Accidental(Note note, Acc accidental) {
        this.note = note;
        this.accidental = accidental;
    }
    
    @Override
    public double duration() {
        return this.note.duration();
    }
   @Override
    public void play(SequencePlayer player, double beat, Voice myVoice) {
        SequencePlayer accidentalPlayer
    }

}
