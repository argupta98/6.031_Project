package karaoke;

import karaoke.sound.SequencePlayer;

public class Rest implements Music {
    
    private final double duration;
    
    public Rest(double duration) {
        this.duration = duration;
    }
    @Override
    public double duration() {
        return this.duration;
    }

    @Override
    public void play(SequencePlayer player, double beat, Voice myVoice) {
        return;
    }

}
