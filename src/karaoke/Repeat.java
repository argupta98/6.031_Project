package karaoke;

import java.util.ArrayList;
import java.util.List;

import karaoke.sound.SequencePlayer;

public class Repeat implements Music{
    
    private final Music measures;
    private final List<Music> endings;
    
    // Repeat(Measures, Endings): A repeat with a main body <Measures> which is repeated every time, 
    //                            and has endings <endings> where the 1st ending is at index 0 of <endings>
    //                            and the following ending occur in order. With no endings, the repeat only happens twice
    //                            otherwise it repeats once for every ending
    //
    // RI() = True
    //
    // Safety from Rep Exposure:
    //     - Client has no reference to any internal variables
    //     - All internal variables are private and final
    //     - Never returns any of the internal rep variables
    // Thread Safety Argument
    // - Player Wrapper Class that plays music is a threadsafe datatype
    // - Player Class is the only class that is called upon from multiple threads
    
    /**
     * Creates a new instance of a Repeat
     * @param measures the measures in the main body of the repeat
     * @param endings the different endings of the repeat
     */
    public Repeat(Music measures, List<Music> endings) {
        this.measures = measures;
        this.endings = new ArrayList<>(endings);
        checkRep();
    }
    
    private void checkRep() {
        assert measures != null;
        assert endings != null;
    }
    
    @Override
    public double duration() {
        double totalDuration = 0;
        // Automatically set duration to playing the measure twice 
        if(endings.size() ==0 ) {
            totalDuration = 2*measures.duration();
        }
        else {
            // Set duration to length of measure with all different endings
            for(Music ending: endings) {
                totalDuration += measures.duration()+ending.duration();
            }
        }
        return totalDuration;
    }
    
    @Override
    public void play(SequencePlayer player, double beat, Voice voice) {
        double currentBeat = beat;
        // Play measure twice 
        if(endings.size() ==0 ) {
            measures.play(player, currentBeat, voice);
            currentBeat+=measures.duration();
            measures.play(player, currentBeat, voice);
        }
        
        else {
            // Play meausre with each ending 
            for(Music ending: endings) {
                measures.play(player, currentBeat, voice);
                currentBeat+=measures.duration();
                ending.play(player, currentBeat, voice);
                currentBeat+=ending.duration();
            }
        }
    }
    
    @Override
    public boolean equals(Object that) {
    	return that instanceof Repeat && ((Repeat)that).measures.equals(measures) && ((Repeat)that).endings.equals(endings);
    }
    
    @Override
    public int hashCode() {
    	return measures.hashCode() + endings.hashCode();
    }
    
    @Override 
    public String toString() {
        String outString ="|:";
        outString+=measures.toString();
        if(endings.size() ==0 ) { 
            outString+=":|";
        }
        else {
            for(int i = 0; i < endings.size(); i++) {
                outString+="["+(i+1)+endings.get(i).toString();
                if(i != endings.size() -1) {
                    outString+=":|";
                }
            }
        }
        return outString;
    }
    
}
