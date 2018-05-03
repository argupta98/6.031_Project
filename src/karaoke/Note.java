package karaoke;

import karaoke.sound.Instrument;
import karaoke.sound.Pitch;
import karaoke.sound.SequencePlayer;

public class Note implements Music{
    
    private final double duration;
    private final Pitch pitch;
    private final Instrument instrument;

    @Override
    public double duration() {
        return 0;
    }

    @Override
    public void play(SequencePlayer player, double beat) {

        
    }

}
