package karaoke;''

import karaoke.Voice.LyricListener;
import karaoke.sound.SequencePlayer;

/**
 * Music represents a piece of music played by multiple instruments.
 */
public interface Music {
    
    // Datatype Definiton:
    // Music = Note(duration: double, pitch:Pitch, instrument: Instrument) + Accidental(accidental: Enum, note: Note) + Rest(duration: double)
    //         + Chord(notes: List<Note>) + Tuplet(tupletNumber:int, notes: List<Notes>) + Measure(notes: List<Music>)+ Repeat(measures: List<Measures>, Endings: List<Measures>)

    
    /**
     * @return the length of this piece in beats
     */
    public double duration();
    
    /**
     * play this piece 
     * @param player to play the piece on
     * @param beat to start song on
     */
    public void play(SequencePlayer player, double beat, LyricListener listener);
    
}
