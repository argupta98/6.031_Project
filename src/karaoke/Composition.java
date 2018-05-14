package karaoke;


import java.util.HashMap;
import java.util.Map;
import karaoke.Voice.LyricListener;
import karaoke.sound.SequencePlayer;

//Mutable class
public class Composition {
    private static double DEFAULT_TEMPO = 100;
    private static String DEFAULT_COMPOSER = "Unknown";
    private static double DEFAULT_LENGTH = 1.0/4;
    private static double DEFAULT_METER = 1.0;
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
    
    //Modifiable
    private double tempo;
    private String title;
    private String composer;
    private double length;
    private double meter;
    private int trackNumber;
    private Key key;
    
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
    public Composition() {
        this.voices = new HashMap<>();
        this.tempo = DEFAULT_TEMPO;
        this.meter = DEFAULT_METER;
        this.length= DEFAULT_LENGTH;
        this.composer = DEFAULT_COMPOSER;
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
        //player.addEvent(this.duration(), (beat) -> this.notifyEnd());
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
        return maxDuration;
    }
    
    
    /**
     * @return the title of the composition
     */
    public String title() {
        return this.title;
    }
    
    /**
     * set the title of the composition
     * @param title the title of the piece
     */
    public void setTitle(String title) {
        this.title = title;
        checkRep();
    }
    
    /**
     * @return the composer of the composition
     */
    public String composer() {
        return this.composer;
    }
    
    /**
     * set the composer of the composition
     * @param composer the composer of the piece
     */
    public void setComposer(String composer) {
        this.composer = composer;
        checkRep();
    }
    
    /**
     * @return the tempo of the composition
     */
    public double tempo() {
        return this.tempo;
    }
    
    /**
     * set the tempo of the composition
     * @param tempo the tempo to set the piece to
     */
    public void setTempo(double tempo) {
        this.tempo = tempo;
        checkRep();
    }
    
    /**
     * @return the default length of a note in the composition
     */
    public double length() {
        return this.length;
    }
    
    /**
     * set the default length of a note in the composition
     * @param length the length of the piece
     */
    public void setLength(double length) {
        this.length = length;
        checkRep();
    }
    
    /**
     * @return the track number of the composition in a set of compositions
     */
    public int trackNumber() {
        return this.trackNumber;
    }
    
    /**
     * set the track number of the composition in a set of compositions
     * @param trackNumber the tracknumber of the piece
     */
    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber; 
        checkRep();
    }
    
    /**
     * @return the key the composition is written in
     */
    public Key key(){
        return this.key;
    }
    
    /**
     * set the key the composition is written in
     * @param key the key of the piece
     */
    public void setKey(Key key){
        this.key = key;
        checkRep();
    }
    
    /**
     * @return the length of a bar of a composition
     */
    public double meter() {
        return this.meter;
    }
    
    /**
     * set the length of a bar of a composition
     * @param meter the meter of the composition
     */
    public void setMeter(double meter) {
        this.meter = meter;
        checkRep();
    }
    
    /**
     * Set the voices of this piece to voicemap
     * @param voiceMap a map of voice names to voices
     */
    public void setVoices(Map<String, Voice> voiceMap) {
        voices = new HashMap<>(voiceMap);
        checkRep(); 
    }
    
    @Override 
    public String toString() {
        String outString = "";
        for(String voiceKey: voices.keySet()) {
            Voice voice = voices.get(voiceKey);
            outString+=voice.toString()+"\n";
        }
        return outString;
    }
}
