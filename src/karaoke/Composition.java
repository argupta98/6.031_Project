package karaoke;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import karaoke.Composition.Key;
import karaoke.Voice.LyricListener;

public class Composition {
    private final Map<String, Voice> voices;
    public enum Key{A, B, C, D, E, F, G}
    
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
     * @param voices a list of the voices in the composition
     */
    public Composition(Map<String, Voice> voices) {
        this.voices = new HashMap<>(voices);
    }
    
    /**
     * play all voices at once
     */
    public void play() {
        throw new UnsupportedOperationException("not implemented yet");
    }
    
    /**
     * Add in a new listener to be updated with the line and highlighted syllable 
     * on each note
     * @param voice the voice of the composition being listened to
     * @param listener the listener object 
     */
    public void addVoiceListener(String voice, LyricListener listener) {
        throw new UnsupportedOperationException("not implemented yet");
    }
    
    /**
     * @return the duration of the entire Composition
     */
    public double duration() {
        throw new UnsupportedOperationException("not implemented yet");
    }
    
    /**
     * @return the title of the composition
     */
    public String title() {
        throw new UnsupportedOperationException("not implemented yet");
    }
    
    /**
     * @return the composer of the composition
     */
    public String composer() {
        throw new UnsupportedOperationException("not implemented yet");
    }
    
    /**
     * @return the tempo of the composition
     */
    public double tempo() {
        throw new UnsupportedOperationException("not implemented yet");
    }
    
    /**
     * @return the default length of a note in the composition
     */
    public double length() {
        throw new UnsupportedOperationException("not implemented yet");
    }
    
    /**
     * @return the track number of the composition in a set of compositions
     */
    public int trackNumber() {
        throw new UnsupportedOperationException("not implemented yet"); 
    }
    
    /**
     * @return the key the composition is written in
     */
    public Key key(){
        throw new UnsupportedOperationException("not implemented yet");
    }
    
    /**
     * @return the length of a bar of a composition
     */
    public double meter() {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
