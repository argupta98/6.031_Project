package karaoke;

import karaoke.sound.SequencePlayer;

public class Concat implements Music{
    
    private final Music left;
    private final Music right;
    
    public Concat(Music left, Music right) {
        this.left = left;
        this.right = right;
    }
    
    public double duration() {
        return left.duration()+right.duration();
    }

    public void play(SequencePlayer player, double beat, Voice voice) {
        left.play(player, beat, voice);
        right.play(player, beat+left.duration(), voice);
    }
    
    @Override
    public String toString() {
        return left.toString() + right.toString();
    }

}



