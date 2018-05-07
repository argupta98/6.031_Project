package karaoke;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import karaoke.sound.SequencePlayer;

public class Voice {
    
    
    private final Music music;
    private final List<String> allSyllables;
    private final List<LyricListener> listeners;
    private final String name;
    
    // Abstraction Function
    // AF(piece, allSyllables, listeners) => A Voice that sings the music piece which pronounces the syllables in allSyllables, and
    //                                       has listeners in the set listeners which provide callbacks for the lyric highlight 
    
    // Rep Invaraint
    // - piece cannot be null
    // - allSyllables cannot be null
    // - listeners cannot be null
    
    // Safety from Rep Exposure 
    
    
    //Thread Safety Argument:
    // - 
    
    public Voice(Music piece, List<String> syllables, String name) {
        this.music = piece;
        this.allSyllables = syllables;
        this.listeners = Collections.synchronizedList(new ArrayList<>());
        this.name = name;
    }
    
   /** Listens for note being played and provides the necessary lyric */
    public interface LyricListener{
       /** Called when note is being played */
       public void notePlayed(String line);
   }
    
    /**
     * Play music with in this particular voice. With each note played
     * a callback is made to the current lyric as well.
     * @param player to add the notes to be played
     */
    public void play (SequencePlayer player) {
        this.music.play(player, 0, this);
    }
    
    /**
     * Adds a LyricListener for the voice
     * @param listener that will call for the correct lyric
     */
    public void addListener(LyricListener listener) {
        this.listeners.add(listener);
    }

    public void notifyAll(int lyricIndex) {
        for(LyricListener listen: this.listeners) {
            listen.notePlayed(constructLine(lyricIndex));
        }
    }
    
    public String name() {
        return this.name;
    }
    
    public Voice join(Voice voice) {
        Music newMusic = new Concat(this.music, voice.music);
        List<String> combinedSyllables = new ArrayList<>(this.allSyllables);
        combinedSyllables.addAll(voice.allSyllables);
        return new Voice(newMusic, combinedSyllables, name);
    }

    public double duration() {
        return music.duration();
    }
    
    private String constructLine(int boldedIndex) {
        String fullLine = "";
        
        if(this.allSyllables.get(boldedIndex).equals("_")) {
            while(this.allSyllables.get(boldedIndex).equals("_") || this.allSyllables.get(boldedIndex).equals("-")) {
                boldedIndex--;
            }
        }
        
        for(int index = 0; index < this.allSyllables.size(); index++) {
            boolean bolded = false;
            if(this.allSyllables.get(index).equals("_")) {
                fullLine+=" ";
            }
            else if(index == boldedIndex) {
                String syllable = this.allSyllables.get(index);
                if(syllable.endsWith(" ")) {
                    fullLine+="*"+this.allSyllables.get(index).substring(0, syllable.length()-1)+"*";
                }
                else {
                    fullLine+="*"+this.allSyllables.get(index)+"*";
                }
            }
            else {
                fullLine+=this.allSyllables.get(index);
            }
        }
        return fullLine;
    }
    
    @Override 
    public String toString() {
        String outString = "";
        if(this.name != "") {
            outString+= "V: "+this.name+"\n";
        }
        outString+= this.music.toString();
        if(this.allSyllables.size() > 0) {
            outString+= "\nw: "+constructLine(-1);
        }
        return outString;
    }
}
