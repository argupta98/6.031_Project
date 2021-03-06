package karaoke;

import karaoke.sound.SequencePlayer;

public class Concat implements Music{
    
    private final Music left;
    private final Music right;
    
    /*
     * AF(left, right) = A piece of music with left played first and then immediately followed by right
     * 
     * RI(): true
     * 
     * Safety from rep exposure:
     *     - All internal variables are private and final and immutable
     *     - returns any of the internal rep variable
     *     
     * Thread Safety Argument:
     *  - Player Wrapper Class that plays music is a threadsafe datatype
     *  - Player Class is the only class that is called upon from multiple threads 
     */
    
    /**
     * Creates a new Concat instance where the left half is <left> and the right half is <right>
     * @param left the earlier part of the music piece
     * @param right the latter part of the  music piece
     */
    public Concat(Music left, Music right) {
        this.left = left;
        this.right = right;
    }
    
    private void checkRep() {
        assert left != null;
        assert right != null;
    }
    
    @Override
    public double duration() {
        return left.duration()+right.duration();
    }
    
    @Override
    public void play(SequencePlayer player, double beat, Voice voice) {
        left.play(player, beat, voice);
        right.play(player, beat+left.duration(), voice);
    }
    
    @Override
    public boolean equals(Object that) {
    	return that instanceof Concat && ((Concat)that).right.equals(right) && ((Concat)that).left.equals(left);
    }
    
    @Override
    public int hashCode() {
    	return right.hashCode() + left.hashCode();
    }
    
    @Override
    public String toString() {
        return left.toString() + right.toString();
    }

}



