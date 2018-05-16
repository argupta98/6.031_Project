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
    private final List<Integer> lineBreaks;
    private final String name;
    
    // Abstraction Function
    // AF(piece, allSyllables, listeners, lineBreaks) => A Voice that sings the music piece which pronounces the syllables in allSyllables, and
    //                                       has listeners in the set listeners which provide callbacks for the lyric highlight, the lyrics 
    //										 start a newline at each index in lineBreaks
    
    // Rep Invaraint
    // - true 
    
    // Safety from Rep Exposure 
    //  - Client has no reference to internal representation
    //  - All fields are private and final
    //  - None of the internal rep variables are returned directly
    
    // Thread Safety Argument
    // - Player Wrapper Class that plays music is a threadsafe datatype
    // - Player Class is the only class that is called upon from multiple threads
    // - Multiple threads can have access to listeners at once so it is a threadsafe datatype 
    
    
    /**
     * Constructs a new Voice object with Music <piece>, syllables <syllables>, and name <name>
     * @param piece a playable music for this voice
     * @param syllables the syllables in this voice
     * @param name of the voice
     */
    public Voice(Music piece, List<String> syllables, String name) {
        this.music = piece;
        this.allSyllables = Collections.synchronizedList(new ArrayList<>(syllables));
        this.lineBreaks = Collections.synchronizedList(new ArrayList<>());
        
        //constructLineBreaks();
        this.listeners = Collections.synchronizedList(new ArrayList<>());
        this.name = name;
        checkRep();
    }
    
    private void constructLineBreaks() {
    	//put a zero at the beginning
        this.lineBreaks.add(0);
        for(int syllable = 0; syllable < this.allSyllables.size(); syllable++) {
        	if(allSyllables.get(syllable).endsWith("\n")) {
        		this.lineBreaks.add(syllable+1);
        	}
        }
        this.lineBreaks.add(allSyllables.size());
    }
    
    private void checkRep() {
        assert music != null;
        assert allSyllables != null;
        assert listeners != null;
        assert name != null;
        assert lineBreaks != null;
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
     * @param lyricIndex index to the list of all lyrics for song to notify which lyric to highlight 
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
        // There are no remaining lyrics left 
        if(boldedIndex >= this.allSyllables.size()) {
            boldedIndex = this.allSyllables.size();
            allSyllables.add("");
            this.lineBreaks.set(lineBreaks.size()-1, this.allSyllables.size());
        }
        // Hold the syllable longer so reduce the boldedIndex 
        if(this.allSyllables.get(boldedIndex).trim().equals("_")) {
            while(this.allSyllables.get(boldedIndex).trim().equals("_") || this.allSyllables.get(boldedIndex).equals("-")) {
                boldedIndex--;
            }
        }
        
        /*
        //find the closest set of line breaks
        int lineIndex = 0;
        while(this.lineBreaks.get(lineIndex+1) <= boldedIndex) {
        	lineIndex++;
        }
        */
        for(int index = 0/*this.lineBreaks.get(lineIndex)*/; index < this.allSyllables.size() /*this.lineBreaks.get(lineIndex+1)*/; index++) {
            // Syllable being held so add no extra syllable
            if(this.allSyllables.get(index).trim().equals("_")) {
            	 if(this.allSyllables.get(index).endsWith(" ")) {
                     fullLine+=" ";
                 }
            }
            else if(this.allSyllables.get(index).equals("")) {
                continue;
            }
            else if(this.allSyllables.get(index).trim().equals("")) {
                fullLine+= " ";
            }
            // Skipping a note so add no extra syllable 
            else if(this.allSyllables.get(index).trim().equals("*")) {
                continue;
            }
            // Correctly highlight the syllable to bolded during the song 
            else if(index == boldedIndex) {
                String syllable = this.allSyllables.get(index);
                if(syllable.endsWith(" ")) {
                    fullLine+="*"+this.allSyllables.get(index).trim()+"* ";
                }
                else {
                    fullLine+="*"+this.allSyllables.get(index).trim()+"*";
                }
            }
            // If not the current syllable just display the normal syllable 
            else {
                fullLine+=this.allSyllables.get(index);
            }
        }
        return fullLine.trim();
    }
    
    @Override
    public boolean equals(Object that) {
    	return that instanceof Voice && ((Voice)that).music.equals(music)
    			&& ((Voice)that).allSyllables.equals(allSyllables) &&((Voice)that).listeners.equals(listeners)
    			&& ((Voice)that).lineBreaks.equals(lineBreaks) && ((Voice)that).name.equals(name);
    }
    
    @Override
    public int hashCode() {
    	return music.hashCode() + allSyllables.hashCode() + listeners.hashCode() + lineBreaks.hashCode() + name.hashCode();
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
