package karaoke;

import java.util.List;

import karaoke.sound.SequencePlayer;

public class Tuplet implements Music {
    
    private final int tupletNumber;
    private final List<Music> notes;
    
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
        return duration*(this.tupletNumber-1)/this.tupletNumber;
    }
    
    
    @Override
    public void play(SequencePlayer player, double beat, Voice myVoice) {
        //TODO
        for(Music note: notes) {
            note.play(player, beat, myVoice);
        }
    }

}
