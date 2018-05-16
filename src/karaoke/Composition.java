package karaoke;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import karaoke.Voice.LyricListener;
import karaoke.sound.SequencePlayer;

//Mutable class
public class Composition {
    private Map<String, Voice> voices;
    public enum Key{C, Am, G, Em, D, Bm, A, Fsharpm, E, Csharpm, B, Gsharpm, Cflat, Aflatm, Gflat, Eflatm,
    	Fsharp, Dsharpm, Dflat, Bflatm, Csharp, Asharpm, Aflat, Fm, Eflat, Cm, Bflat, Gm, F, Dm};
    
    public enum Accidental{FLAT(-1), SHARP(1), DOUBLE_FLAT(-2), DOUBLE_SHARP(2), NATURAL(0);
        
        private final int semitonesUp;
        /**
         * Creates a new accidental 
         * @param semitonesUp the number of semitones up the accidental is
         */
        Accidental(int semitonesUp) {
            this.semitonesUp = semitonesUp;
        }
        
        /**
         * @return the number of semitones up the Accidental represents
         */
        public int getTranspose() {
            return this.semitonesUp;
        }
    }
    
    //UnModifiable
    private final double tempo;
    private final String title;
    private final String composer;
    private final double length;
    private final double meter;
    private final int trackNumber;
    private final Key key;
    
    //AF(Voices) = A composition music piece which consists of all the voices in voices played
    //             together
    //
    //RI(): True
    //
    //Safety from rep Exposure:
    //  All internal variables are private, final and never returned. 
    //  The client has no access to any of the variables. 
    // Thread Safety Argument
    // - Player Wrapper Class that plays music is a threadsafe datatype
    // - Player Class is the only class that is called upon from multiple threads
    
    /**
     * Creates a new composition with default values
     */
    public Composition(String title, String composer, double length, double tempo, double meter, int tracknumber, Key key){
    	this.tempo = tempo;
    	this.title = title;
    	this.composer = composer;
    	this.length = length;
    	this.meter = meter;
    	this.trackNumber = tracknumber;
    	this.key = key;
    	this.voices = new HashMap<>();
    	checkRep();
    }
    
    private void checkRep() {
        assert title != null;
        assert voices != null;
        assert key != null;
        assert composer != null;
    }
    /**
     * modifies the player to play all the voices at once
     * @param player the player to have play the music
     */
    public void play(SequencePlayer player) {
        for(String voiceKey: voices.keySet()) {
            voices.get(voiceKey).play(player);
        }
        checkRep();
    }
    
    /**
     * Add in a new listener to be updated with the line and highlighted syllable 
     * on each note
     * @param voice the voice of the composition being listened to
     * @param listener the listener object 
     */
    public void addVoiceListener(String voice, LyricListener listener) {
        if(voices.containsKey(voice)) {
            voices.get(voice).addListener(listener);
        }
        checkRep();
    }
    
    /**
     * @return the duration of the entire Composition
     */
    public double duration() {
        double maxDuration = 0;
        for(String voiceKey: voices.keySet()) {
            Voice voice = voices.get(voiceKey);
            if(voice.duration() > maxDuration) {
                maxDuration = voice.duration();
            }
        }
        checkRep();
        return maxDuration;
    }
    
    
    /**
     * @return the title of the composition
     */
    public String title() {
        return this.title;
    }
    
    /**
     * @return the composer of the composition
     */
    public String composer() {
        return this.composer;
    }
    
    
    /**
     * @return the tempo of the composition
     */
    public double tempo() {
        return this.tempo;
    }
    
    
    /**
     * @return the default length of a note in the composition
     */
    public double length() {
        return this.length;
    }
    
    
    /**
     * @return the track number of the composition in a set of compositions
     */
    public int trackNumber() {
        return this.trackNumber;
    }
    
    
    /**
     * @return the key the composition is written in
     */
    public Key key(){
        return this.key;
    }
    
    /**
     * @return the length of a bar of a composition
     */
    public double meter() {
        return this.meter;
    }
    
    /**
     * Set the voices of this piece to voicemap
     * @param voiceMap a map of voice names to voices
     */
    public void setVoices(Map<String, Voice> voiceMap) {
        voices = new HashMap<>(voiceMap);
        checkRep();
    }
    
    
    /**
     * @return set of all voiceIDs
     */
    public Set<String> getVoices() {
        return voices.keySet();
    }
    @Override 
    public String toString() {
        String outString = "";
        for(String voiceKey: voices.keySet()) {
            Voice voice = voices.get(voiceKey);
            outString+=voice.toString()+"\n";
        }
        checkRep();
        return outString;
    }
}
