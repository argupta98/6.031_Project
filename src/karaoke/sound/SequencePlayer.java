package karaoke.sound;

import java.util.function.Consumer;

/**
 * Schedules and plays a sequence of notes at given times.
 * Times and durations are specified in doubles, but implementations of this
 * interface may round to implementation-specific precision.
 * For example, 0.501 beats may be rounded to 0.5 beats.
 * (In MidiSequencePlayer, this precision is controlled by the ticksPerBeat
 * parameter.)
 */
public interface SequencePlayer {

    /**
     * Schedule a note to be played starting at startBeat for the duration numBeats.
     * @param instr instrument for the note
     * @param pitch pitch value of the note
     * @param startBeat the starting beat
     * @param numBeats the number of beats the note is played
     */
    public void addNote(Instrument instr, Pitch pitch, double startBeat, double numBeats);

    /**
     * Schedule a callback when the synthesizer reaches a time.
     * @param atBeat beat at which to call the callback
     * @param callback function to call, with type double->void. 
     *              The double parameter is the time when actually called, in beats.
     *              This time may be slightly different from atBeat because of rounding.
     */
    public void addEvent(double atBeat, Consumer<Double> callback);

    /**
     * Play the scheduled music.
     */
    public void play();
    

}
