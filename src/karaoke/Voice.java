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
    // - true 
    
    // Safety from Rep Exposure 
    //  - Client has no reference to internal representation
    //  - All fields are private and final
    //  - None of the internal rep variables are returned directly
    /**
     * Constructs a new Voice object with Music <piece>, syllables <syllables>, and name <name>
     * @param piece a playable music for this voice
     * @param syllables the syllables in this voice
     * @param name of the voice
     */
    public Voice(Music piece, List<String> syllables, String name) {
        this.music = piece;
        this.allSyllables = syllables;
        this.listeners = Collections.synchronizedList(new ArrayList<>());
        this.name = name;
        checkRep();
    }
    
    private void checkRep() {
        assert music != null;
        assert allSyllables != null;
        assert listeners != null;
        assert name != null;
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
    public synchronized void play (SequencePlayer player) {
        this.music.play(player, 0, this);
        player.addEvent(music.duration(), (beat) -> this.notifyEnd());
        checkRep();
    }
    
    /**
     * Adds a LyricListener for the voice
     * @param listener that will call for the correct lyric
     */
    public synchronized void addListener(LyricListener listener) {
        this.listeners.add(listener);
        checkRep();
    }

    /**
     * Notifies all the LyricListeners with the current line of lyrics on each new note
     * @param lyricIndex
     */
    public synchronized void notifyAll(int lyricIndex) {
        for(LyricListener listen: this.listeners) {
            listen.notePlayed(constructLine(lyricIndex));
        }
        checkRep();
    }
    
    /**
     * Final notification, to tell that the song has ended
     */
    public synchronized void notifyEnd() {
    	for(LyricListener listen: this.listeners) {
            listen.notePlayed("END");
        }
    	checkRep();
    }
    
    /**
     * @return the name of the voice
     */
    public String name() {
        return this.name;
    }
    
    /**
     * @param voice the voice to join to this one
     * @return a new voice which is the concatenation of the music and lyrics in 
     * <this> and then music and lyrics in <voice>
     */
    public Voice join(Voice voice) {
        Music newMusic = new Concat(this.music, voice.music);
        List<String> combinedSyllables = new ArrayList<>(this.allSyllables);
        combinedSyllables.addAll(voice.allSyllables);
        return new Voice(newMusic, combinedSyllables, name);
    }
    
    /**
     * @return the duration of this voice's music
     */
    public double duration() {
        return music.duration();
    }
    
    /**
     * @param boldedIndex the index of the syllable to be bolded
     * @return the line of lyrics reformatted as a readable string with the syllable at <boldedIndex>
     * bolded
     */
    private String constructLine(int boldedIndex) {
        String fullLine = "";
        if(this.allSyllables.size() == 0) {
        	return "No Lyrics";
        }
        if(boldedIndex >= this.allSyllables.size()) {
            boldedIndex = this.allSyllables.size();
            allSyllables.add("");
        }
        
        if(this.allSyllables.get(boldedIndex).trim().equals("_")) {
            while(this.allSyllables.get(boldedIndex).trim().equals("_") || this.allSyllables.get(boldedIndex).equals("-")) {
                boldedIndex--;
            }
        }
        
        for(int index = 0; index < this.allSyllables.size(); index++) {
            boolean bolded = false;
            if(this.allSyllables.get(index).trim().equals("_")) {
                fullLine+="";
            }
            else if(this.allSyllables.get(index).equals(" ")) {
                fullLine+= " ";
            }
            else if(this.allSyllables.get(index).equals("")) {
            	continue;
            }
            else if(this.allSyllables.get(index).trim().equals("*")) {
                continue;
            }
            else if(index == boldedIndex) {
                String syllable = this.allSyllables.get(index);
                if(syllable.endsWith(" ")) {
                    fullLine+="*"+this.allSyllables.get(index).substring(0, syllable.length()-1)+"* ";
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
    
    /**
     * @return the number of syllables in this voice's lyrics
     */
    public int lyricLength() {
        return this.allSyllables.size();
    }
}
