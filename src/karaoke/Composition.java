package karaoke;

import java.util.Map;

public class Composition {
    private final Map<String, Voice> voices;
    
    /**
     * Creates a new composition with all the voices
     * @param voices a list of the voices in the composition
     */
    public Composition(Map<String, Voice> voices) {
        this.voices = voices;
    }
    
    /**
     * play all voices at once
     */
    public void play() {
        
    }
    
    /**
     * Add in a new listener to be updated with the line and highlighted syllable 
     * on each note
     * @param voice the voice of the composition being listened to
     * @param listener the listener object 
     */
    public void addVoiceListener(String voice, LyricListener listener) {
        
    }
    
    public interface LyricListener{
        /**
         * Displays the line given
         * @param line the line to be displayed
         */
        public void diplayLine(String line);
    }
}
