package karaoke;

import karaoke.sound.Instrument;
import karaoke.sound.Pitch;
import karaoke.sound.SequencePlayer;

public class Note implements Music{
    
    private final double duration;
    private final Pitch pitch;
    private final Instrument instrument;
    private final int lyricIndex;
    
    public Note(double duration, Pitch pitch, Instrument instrument, int lyricIndex) {
        this.duration = duration;
        this.pitch = pitch;
        this.instrument = instrument;
        this.lyricIndex = lyricIndex;
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

}
