package karaoke;
import karaoke.sound.Instrument;
import karaoke.sound.Pitch;
import karaoke.sound.SequencePlayer;

/**
 * A Note represent a pitch and duration of a sound played on a particular instrument.
 * It can be associated with lyrics or not.
 */
public class Note implements Music{
    
    private final double duration;
    private final Pitch pitch;
    private final Instrument instrument;
    private final int lyricIndex;
    
    // AF(duration, pitch, instrument, lyricIndex) = a Note of duration <duration> with pitch <pitch>
    //                                              played on an instrument <instrument> and associated
    //                                              with a lyric that is at location <lyricIndex> in a line
    //                                              of lyrics.
    
    // Rep Invariant
    // duration and lyricIndex are non-negative
    
    // Safety from Rep Exposure
    // All fields are private and immutable
    
    // Thread Safety Argument
    // - Player Wrapper Class that plays music is a threadsafe datatype
    // - Player Class is the only class that is called upon from multiple threads
    
    
    /**
     * Creates a new Note Instance
     * @param duration the duration of the note
     * @param pitch the pitch of the note 
     * @param instrument the instrument to play the note
     * @param lyricIndex the index of the syllable corresponding to the note in the list of syllables
     */
    public Note(double duration, Pitch pitch, Instrument instrument, int lyricIndex) {
        this.duration = duration;
        this.pitch = pitch;
        this.instrument = instrument;
        this.lyricIndex = lyricIndex;
        checkRep();
    }
    
    private void checkRep() {
        assert pitch != null;
        assert instrument != null;
        
        assert duration >= 0;
        assert lyricIndex >= 0;
    }
    
    @Override
    public double duration() {
        return this.duration;
    }

    @Override
    public void play(SequencePlayer player, double beat, Voice myVoice) {
        player.addEvent(beat, (endBeat) -> myVoice.notifyAll(this.lyricIndex));
        player.addNote(this.instrument, this.pitch, beat, this.duration);
    }
    
    @Override
    public boolean equals(Object that) {
    	return that instanceof Note && ((Note)that).duration == duration && ((Note)that).pitch.equals(pitch)
    			&& ((Note)that).instrument.equals(instrument) && ((Note)that).lyricIndex == lyricIndex;
    }
    
    @Override
    public int hashCode() {
    	return instrument.hashCode() + ((Integer)lyricIndex).hashCode() + ((Double)duration).hashCode() + pitch.hashCode();
    }
    
    @Override
    public String toString() {
        return this.pitch.toString();
    }

}
