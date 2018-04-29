package examples;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import karaoke.sound.Instrument;
import karaoke.sound.MidiSequencePlayer;
import karaoke.sound.Pitch;
import karaoke.sound.SequencePlayer;

public class ScaleExample {
    
    /**
     * Play an octave up and back down starting from middle C.
     * 
     * @param args not used
     * @throws MidiUnavailableException if MIDI device unavailable
     * @throws InvalidMidiDataException if MIDI play fails
    */
    public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException {

        Instrument piano = Instrument.PIANO;

        // create a new player
        final int beatsPerMinute = 120; // a beat is a quarter note, so this is 120 quarter notes per minute
        final int ticksPerBeat = 64; // allows up to 1/64-beat notes to be played with fidelity
        SequencePlayer player = new MidiSequencePlayer(beatsPerMinute, ticksPerBeat);
        
        // addNote(instr, pitch, startBeat, numBeats) schedules a note with pitch value 'pitch'
        // played by 'instr' starting at 'startBeat' to be played for 'numBeats' beats.
        
        int startBeat = 0;
        int numBeats = 1;
        player.addNote(piano, new Pitch('C'), startBeat++, numBeats);
        player.addNote(piano, new Pitch('D'), startBeat++, numBeats);
        player.addNote(piano, new Pitch('E'), startBeat++, numBeats);
        player.addNote(piano, new Pitch('F'), startBeat++, numBeats);
        player.addNote(piano, new Pitch('G'), startBeat++, numBeats);
        player.addNote(piano, new Pitch('A'), startBeat++, numBeats);
        player.addNote(piano, new Pitch('B'), startBeat++, numBeats);
        player.addNote(piano, new Pitch('C').transpose(Pitch.OCTAVE), startBeat++, numBeats);
        player.addNote(piano, new Pitch('B'), startBeat++, numBeats);
        player.addNote(piano, new Pitch('A'), startBeat++, numBeats);
        player.addNote(piano, new Pitch('G'), startBeat++, numBeats);
        player.addNote(piano, new Pitch('F'), startBeat++, numBeats);
        player.addNote(piano, new Pitch('E'), startBeat++, numBeats);
        player.addNote(piano, new Pitch('D'), startBeat++, numBeats);
        player.addNote(piano, new Pitch('C'), startBeat++, numBeats);
        
        // add a listener at the end of the piece to tell main thread when it's done
        Object lock = new Object();
        player.addEvent(startBeat, (Double beat) -> {
            synchronized (lock) {
                lock.notify();
            }
        });
        
        // print the configured player
        System.out.println(player);

        // play!
        player.play();
        
        // wait until player is done
        // (not strictly needed here, but useful for JUnit tests)
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                return;
            }
        }
        System.out.println("done playing");
    }
}
