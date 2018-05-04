package karaoke.sound;

import static org.junit.Assert.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import org.junit.Test;

/**
 * Tests by hearing, so we do not want didit to run them
 * @category no_didit
 */
public class SequencePlayerTest {

    @Test 
    public void testPlayPiece1() throws MidiUnavailableException, InvalidMidiDataException {
        Instrument piano = Instrument.PIANO;

        // create a new player
        final int beatsPerMinute = 140; // a beat is a quarter note, so this is 120 quarter notes per minute
        final int ticksPerBeat = 64; // allows up to 1/64-beat notes to be played with fidelity
        SequencePlayer player = new MidiSequencePlayer(beatsPerMinute, ticksPerBeat);
        
        // addNote(instr, pitch, startBeat, numBeats) schedules a note with pitch value 'pitch'
        // played by 'instr' starting at 'startBeat' to be played for 'numBeats' beats.
        
        double startBeat = 0;
        double numBeats = 1;
        player.addNote(piano, new Pitch('C'), startBeat++, numBeats);
        player.addNote(piano, new Pitch('C'), startBeat++, numBeats);
        player.addNote(piano, new Pitch('C'), startBeat, .75);
        startBeat+=.75;
        System.out.println(startBeat);
        player.addNote(piano, new Pitch('D'), startBeat, .25);
        startBeat+=.25;
        player.addNote(piano, new Pitch('E'), startBeat, .75);
        startBeat+=.75;
        
        player.addNote(piano, new Pitch('E'), startBeat, .75);
        startBeat+=.75;
        player.addNote(piano, new Pitch('D'), startBeat, .25);
        startBeat+=.25;
        
        player.addNote(piano, new Pitch('E'), startBeat, .75);
        startBeat+=.75;
        
        player.addNote(piano, new Pitch('F'), startBeat, .25);
        startBeat+=.25;

        player.addNote(piano, new Pitch('G'), startBeat, 2);
        startBeat+=2;
        
        player.addNote(piano, new Pitch('C'), startBeat, 2.0/3);
        startBeat=startBeat+2.0/3;
        player.addNote(piano, new Pitch('C'), startBeat, 2.0/3*numBeats);
        startBeat=startBeat+2.0/3;
        player.addNote(piano, new Pitch('C'), startBeat, 2.0/3*numBeats);
        startBeat=startBeat+2.0/3;
        player.addNote(piano, new Pitch('G'), startBeat, 2.0/3*numBeats);
        startBeat=startBeat+2.0/3;
        player.addNote(piano, new Pitch('G'), startBeat, 2.0/3*numBeats);
        startBeat=startBeat+2.0/3;
        player.addNote(piano, new Pitch('G'), startBeat, 2.0/3*numBeats);
        startBeat=startBeat+2.0/3;
        player.addNote(piano, new Pitch('C'), startBeat, 2.0/3*numBeats);
        startBeat+=2.0/3;
        player.addNote(piano, new Pitch('C'), startBeat, 2.0/3*numBeats);
        startBeat+=2.0/3;
        player.addNote(piano, new Pitch('C'), startBeat, 2.0/3*numBeats);
        startBeat+=2.0/3;
        player.addNote(piano, new Pitch('G'), startBeat, 3.0/4*numBeats);
        startBeat+=3.0/4;
        player.addNote(piano, new Pitch('F'), startBeat, 1.0/4*numBeats);
        startBeat+=1.0/4;
        player.addNote(piano, new Pitch('E'), startBeat, 3.0/4*numBeats);
        startBeat+=3.0/4;
        player.addNote(piano, new Pitch('D'), startBeat, 1.0/4*numBeats);
        startBeat+=1.0/4;
        player.addNote(piano, new Pitch('C'), startBeat, 2.0*numBeats);
        startBeat+=2;
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
    
    @Test 
    public void testPlayPiece2() throws MidiUnavailableException, InvalidMidiDataException {
        Instrument piano = Instrument.PIANO;

        // create a new player
        final int beatsPerMinute = 200; // a beat is a quarter note, so this is 120 quarter notes per minute
        final int ticksPerBeat = 64; // allows up to 1/64-beat notes to be played with fidelity
        SequencePlayer player = new MidiSequencePlayer(beatsPerMinute, ticksPerBeat);
        
        // addNote(instr, pitch, startBeat, numBeats) schedules a note with pitch value 'pitch'
        // played by 'instr' starting at 'startBeat' to be played for 'numBeats' beats.
        
        double startBeat = 0;
        double numBeats = 1;
        player.addNote(piano, new Pitch('F').transpose(1), startBeat, 1.0/2);
        player.addNote(piano, new Pitch('E'), startBeat, 1.0/2);
        startBeat+=1.0/2;
        player.addNote(piano, new Pitch('F').transpose(1), startBeat, 1.0/2);
        player.addNote(piano, new Pitch('E'), startBeat, 1.0/2);
        startBeat+=1.0;
        
        player.addNote(piano, new Pitch('F').transpose(1), startBeat, 1.0/2);
        player.addNote(piano, new Pitch('E'), startBeat, 1.0/2);
        startBeat+=1.0;
        
        player.addNote(piano, new Pitch('F').transpose(1), startBeat, 1.0/2);
        player.addNote(piano, new Pitch('C').transpose(Pitch.OCTAVE), startBeat, 1.0/2);
        startBeat+=1.0/2;
        
        player.addNote(piano, new Pitch('F').transpose(1), startBeat, 1.0);
        player.addNote(piano, new Pitch('C').transpose(Pitch.OCTAVE), startBeat, 1.0);
        startBeat+=1.0;
        
        player.addNote(piano, new Pitch('G'), startBeat, 1.0);
        player.addNote(piano, new Pitch('B'), startBeat, 1.0);
        player.addNote(piano, new Pitch('G').transpose(Pitch.OCTAVE), startBeat, 1.0);
        startBeat+=1.0;
        startBeat+=1.0;
        player.addNote(piano, new Pitch('G'), startBeat++, 1.0);
        startBeat+=1.0;
        startBeat+=1.0;
        
        player.addNote(piano, new Pitch('C').transpose(Pitch.OCTAVE), startBeat, 1.0/3);
        startBeat+=1.0/3;
        player.addNote(piano, new Pitch('G'), startBeat, 1.0/2);
        startBeat+=1+1.0/2;
        
        player.addNote(piano, new Pitch('E'), startBeat, 1.0);
        startBeat+=1.0;
        
        player.addNote(piano, new Pitch('E'), startBeat, 1.0/2);
        startBeat+=1.0/2;
        player.addNote(piano, new Pitch('A'), startBeat, 1.0);
        startBeat+=1;
        
        player.addNote(piano, new Pitch('B'), startBeat, 1.0);
        startBeat+=1.0;
        
        player.addNote(piano, new Pitch('B').transpose(-1), startBeat, 1.0/2);
        startBeat+=1.0/2;
        
        player.addNote(piano, new Pitch('A'), startBeat, 1.0);
        startBeat+=1.0;
        
        player.addNote(piano, new Pitch('G'), startBeat, 2.0/3);
        startBeat=startBeat+2.0/3;
        player.addNote(piano, new Pitch('E').transpose(Pitch.OCTAVE), startBeat, 2.0/3*numBeats);
        startBeat=startBeat+2.0/3;
        player.addNote(piano, new Pitch('G').transpose(Pitch.OCTAVE), startBeat, 2.0/3*numBeats);
        startBeat=startBeat+2.0/3;
        
        player.addNote(piano, new Pitch('A').transpose(Pitch.OCTAVE), startBeat, 1.0);
        startBeat+=1.0;
        
        player.addNote(piano, new Pitch('F').transpose(Pitch.OCTAVE), startBeat, 1.0/2);
        startBeat+=1.0/2;
        
        player.addNote(piano, new Pitch('G').transpose(Pitch.OCTAVE), startBeat, 1.0/2);
        startBeat+=1.0/2;
        
        startBeat+=1.0/2;
        
        player.addNote(piano, new Pitch('E').transpose(Pitch.OCTAVE), startBeat, 1.0);
        startBeat+=1.0;
        
        player.addNote(piano, new Pitch('C').transpose(Pitch.OCTAVE), startBeat, 1.0/2);
        startBeat+=1.0/2;
        
        player.addNote(piano, new Pitch('D').transpose(Pitch.OCTAVE), startBeat, 1.0/2);
        startBeat+=1.0/2;
        
        player.addNote(piano, new Pitch('B'), startBeat, 3.0/4);
        startBeat+=6.0/4;

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
    
    @Test 
    public void testPlayPiece3() throws MidiUnavailableException, InvalidMidiDataException {
        Instrument piano = Instrument.PIANO;

        // create a new player
        final int beatsPerMinute = 100; // a beat is a quarter note, so this is 120 quarter notes per minute
        final int ticksPerBeat = 64; // allows up to 1/64-beat notes to be played with fidelity
        SequencePlayer player = new MidiSequencePlayer(beatsPerMinute, ticksPerBeat);
        
        // addNote(instr, pitch, startBeat, numBeats) schedules a note with pitch value 'pitch'
        // played by 'instr' starting at 'startBeat' to be played for 'numBeats' beats.
        
        double startBeat = 2;
        double numBeats = 1.0/2;
        player.addNote(piano, new Pitch('D'), startBeat, 2.0*numBeats);
        startBeat += 2*numBeats;
        player.addEvent(startBeat, (Double beat) -> {
            System.out.print("A");
        });
        player.addNote(piano, new Pitch('G'), startBeat++, 4.0*numBeats);
        startBeat += 4*numBeats;
        player.addEvent(startBeat, (Double beat) -> {
            System.out.print("ma");
        });
        player.addNote(piano, new Pitch('B'), startBeat, 1.0*numBeats);
        startBeat+=1*numBeats;
        player.addEvent(startBeat, (Double beat) -> {
            System.out.print("zing");
        });
        player.addNote(piano, new Pitch('G'), startBeat, 1.0*numBeats);
        startBeat += 1*numBeats;
        
        player.addEvent(startBeat, (Double beat) -> {
            System.out.print(" grace!");
        });
        player.addNote(piano, new Pitch('B'), startBeat, 4.0*numBeats);
        startBeat+=4.0*numBeats;
        player.addEvent(startBeat, (Double beat) -> {
            System.out.print(" How");
        });
        player.addNote(piano, new Pitch('A'), startBeat, 2.0*numBeats);
        startBeat+=2.0*numBeats;
        player.addEvent(startBeat, (Double beat) -> {
            System.out.print(" sweet");
        });
        
        player.addNote(piano, new Pitch('G'), startBeat, 4.0*numBeats);
        startBeat+=4.0*numBeats;
        player.addEvent(startBeat, (Double beat) -> {
            System.out.print(" the");
        });
        player.addNote(piano, new Pitch('E'), startBeat, 2.0*numBeats);
        startBeat+=2.0*numBeats;
        player.addEvent(startBeat, (Double beat) -> {
            System.out.print(" sound");
        });
        player.addNote(piano, new Pitch('D'), startBeat, 4.0*numBeats);
        startBeat+=4.0*numBeats;
        player.addEvent(startBeat, (Double beat) -> {
            System.out.print(" That");
        });
        
        player.addNote(piano, new Pitch('D'), startBeat, 2.0*numBeats);
        startBeat+=2.0*numBeats;
        player.addEvent(startBeat, (Double beat) -> {
            System.out.print(" saved");
        });
        player.addNote(piano, new Pitch('G'), startBeat++, 4.0*numBeats);
        startBeat += 4*numBeats;
        player.addEvent(startBeat, (Double beat) -> {
            System.out.print(" a");
        });
        player.addNote(piano, new Pitch('B'), startBeat, 1.0*numBeats);
        startBeat+=1*numBeats;
        
        player.addNote(piano, new Pitch('G'), startBeat, 1.0*numBeats);
        startBeat += 1*numBeats;
        player.addEvent(startBeat, (Double beat) -> {
            System.out.print(" wretch");
        });
        player.addNote(piano, new Pitch('B'), startBeat, 4.0*numBeats);
        startBeat+=4.0*numBeats;
        player.addEvent(startBeat, (Double beat) -> {
            System.out.print(" like");
        });
        player.addNote(piano, new Pitch('A'), startBeat, 2.0*numBeats);
        startBeat+=2.0*numBeats;
        player.addEvent(startBeat, (Double beat) -> {
            System.out.println(" me.");
        });
        player.addNote(piano, new Pitch('D').transpose(Pitch.OCTAVE), startBeat, 2.0*numBeats);
        startBeat+=2.0*numBeats;
        
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
    
    @Test
    public void test() {
        fail("Not yet implemented");
    }
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
}
