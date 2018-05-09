package karaoke;

import karaoke.sound.SequencePlayer;

public class Rest implements Music {
    
    private final double duration;
    
    /*
     * AF(duration) = A rest for length <duration>
     * 
     * RI(): duration > 0
     * 
     * Safety from rep exposure:
     *     - Client has no reference to any internal variables
     *     - All internal variables are private and final
     *     - Never returns any of the internal rep variables
     */
    
    /**
     * Creates a new rest with duration <duration>
     * @param duration the duration of the rest
     */
    public Rest(double duration) {
        this.duration = duration;
    }
    
    private void checkRep() {
        assert duration > 0;
    }
    
    @Override
    public double duration() {
        return this.duration;
    }

    @Override
    public void play(SequencePlayer player, double beat, Voice myVoice) {
        return;
    }
    
    @Override
    public String toString() {
        if(duration != 0) {
            return "z";
        }
        else {
            return "";
        }
    }

}
