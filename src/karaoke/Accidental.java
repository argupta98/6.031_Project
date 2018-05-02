package karaoke;

import karaoke.sound.SequencePlayer;



public class Accidental implements Music {
    
    public enum ACCIDENTAL{
        SHARP,
        FLAT,
        NATURAL,
        DOUBLESHARP,
        DOUBLEFLAT
    }
    
    
    
     private final ACCIDENTAL accidental;
     private final Note note;
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
