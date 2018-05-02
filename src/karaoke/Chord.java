package karaoke;

import java.util.List;

import karaoke.sound.SequencePlayer;

public class Chord implements Music{
    
    private final List<Music> notes;

    @Override
    public double duration() {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public void play(SequencePlayer player, double beat) {
        // TODO Auto-generated method stub
        
    }

}
