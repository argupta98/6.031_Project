package karaoke;

import java.util.Collections;
import java.util.List;

import karaoke.sound.SequencePlayer;

public class Chord implements Music{
    
    private final List<Music> notes;
    private final int lyricIndex;
    public Chord(List<Music> notes, int lyricIndex) {
        this.notes = notes;
        this.lyricIndex = lyricIndex;
    }
    @Override
    public double duration() {
        return notes.get(0).duration();
    }
    
    @Override
    public void play(SequencePlayer player, double beat, Voice myVoice) {
        player.addEvent(beat, (newBeat) -> myVoice.notifyAll(lyricIndex));
        for(Music note: notes) {
            //Add a new voice that will do nothing when called (callback defined on the chord)
            note.play(player, beat, new Voice(new Rest(0), Collections.emptyList(), ""));
        }
    }

}
