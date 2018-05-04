package karaoke;

import java.util.HashMap;
import java.util.Map;

public class Composition {
    private final Map<String, Voice> voices;
    
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
    
    public interface LyricListener{
        /**
         * Displays the line given
         * @param line the line to be displayed
         */
        public void diplayLine(String line);
    }
}
