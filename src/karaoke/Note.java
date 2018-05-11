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
        player.addEvent(beat, (endBeat) -> {myVoice.notifyAll(this.lyricIndex);
        System.out.println("Notifying voice");});
        player.addNote(this.instrument, this.pitch, beat, this.duration);
    }
    
    @Override
    public String toString() {
        return this.pitch.toString();
    }

}
