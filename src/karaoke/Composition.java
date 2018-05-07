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
    public enum Key{A, Am, B, C, D, E, F, G}
    
    public enum Accidental{FLAT(-1), SHARP(1), DOUBLE_FLAT(-2), DOUBLE_SHARP(2), NATURAL(0);
        
        private final int semitonesUp;
        Accidental(int semitonesUp) {
            this.semitonesUp = semitonesUp;
        }
        
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
    private int numberOfVoices;
    private Key key;
    
    //AF(Voices) = A composition music piece which consists of all the voices in voices played
    //             together
    //
    //RI(): True
    //
    //Safety from rep Exposure:
    //  All internal variables are private, final and never returned. 
    //  The client has no access to any of the variables. 
    
    /**
     * Creates a new composition with all the voices
     */
    public Composition() {
        this.voices = new HashMap<>();
        this.numberOfVoices = 0;
        this.tempo = DEFAULT_TEMPO;
        this.meter = DEFAULT_METER;
        this.length= DEFAULT_LENGTH;
        this.composer = DEFAULT_COMPOSER;
    }
    
    /**
     * play all voices at once
     */
    public void play(SequencePlayer player) {
        for(String voiceKey: voices.keySet()) {
            voices.get(voiceKey).play(player);
        }
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
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * @return the composer of the composition
     */
    public String composer() {
        return this.composer;
    }
    
    /**
     * @return the composer of the composition
     */
    public void setComposer(String composer) {
        this.composer = composer;
    }
    
    /**
     * @return the tempo of the composition
     */
    public double tempo() {
        return this.tempo;
    }
    
    /**
     * @return the tempo of the composition
     */
    public void setTempo(double tempo) {
        this.tempo = tempo;
    }
    
    /**
     * @return the default length of a note in the composition
     */
    public double length() {
        return this.length;
    }
    
    /**
     * @return the default length of a note in the composition
     */
    public void setLength(double length) {
        this.length = length;
    }
    
    /**
     * @return the track number of the composition in a set of compositions
     */
    public int trackNumber() {
        return this.trackNumber;
    }
    
    /**
     * @return the track number of the composition in a set of compositions
     */
    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber; 
    }
    
    /**
     * @return the key the composition is written in
     */
    public Key key(){
        return this.key;
    }
    
    /**
     * @return the key the composition is written in
     */
    public void setKey(Key key){
        this.key = key;
    }
    
    /**
     * @return the length of a bar of a composition
     */
    public double meter() {
        return this.meter;
    }
    
    /**
     * @return the length of a bar of a composition
     */
    public void setMeter(double meter) {
        this.meter = meter;
    }

    public void setVoices(Map<String, Voice> voiceMap) {
        voices = new HashMap<>(voiceMap);
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
