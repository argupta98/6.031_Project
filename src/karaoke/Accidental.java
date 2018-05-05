package karaoke;

import karaoke.sound.SequencePlayer;



public class Accidental implements Music {
    
    public enum Acc{
        SHARP,
        FLAT,
        NATURAL,
    }
    
    private final Acc accidental;
    private final Note note;
    
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
