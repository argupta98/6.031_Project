package karaoke;

import java.util.ArrayList;
import java.util.List;

import karaoke.sound.SequencePlayer;

public class Repeat implements Music{
    
    private final Music measures;
    private final List<Music> endings;
    
    public Repeat(Music measures, List<Music> endings) {
        this.measures = measures;
        this.endings = new ArrayList<>(endings);
    }
    
    @Override
    public double duration() {
        double totalDuration = 0;
        if(endings.size() ==0 ) {
            totalDuration = 2*measures.duration();
        }
        else {
            for(Music ending: endings) {
                totalDuration += measures.duration()+ending.duration();
            }
        }
        return totalDuration;
    }
    
    @Override
    public void play(SequencePlayer player, double beat, Voice voice) {
        double currentBeat = beat;
        if(endings.size() ==0 ) {
            measures.play(player, currentBeat, voice);
            currentBeat+=measures.duration();
            measures.play(player, currentBeat, voice);
        }
        
        else {
            for(Music ending: endings) {
                measures.play(player, currentBeat, voice);
                currentBeat+=measures.duration();
                ending.play(player, currentBeat, voice);
                currentBeat+=ending.duration();
            }
        }
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
