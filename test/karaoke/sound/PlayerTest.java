package karaoke.sound;

import java.io.File;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import org.junit.Test;

import edu.mit.eecs.parserlib.UnableToParseException;
import karaoke.player.Player;

/**
 * Tests by hearing, so we do not want didit to run them
 * @category no_didit
 */
public class PlayerTest {
    
  
/**
 * Testing Strategy 
 * play() Partitions
 * - Header Contents:
 *   - Key: C (default key), none default keys
 *   - Tempo: 100 bpm (default), > 0 && < 100 bpm, > 100 bpm
 *   - Voices: 0 voices, 1 voice, > 1 voice
 * - Music Contents:
 *   - Notes:
 *     - Number of notes: 0, 1, > 1
 *     - Raised octaves: 0 notes, 1 note, > 1 note
 *   - Rests:
 *     - Number of rests: 0, 1, > 1
 *   - Chords:
 *     - Number of chords: 0, 1, > 1
 *   - Accidentals:
 *     - Number of accidentals: 0, 1, > 1
 *   - Tuplets:
 *     - Number of tuplets: 0, 1, > 1
 *   - Repeats:
 *     - Repeats: 0, 1 ending, > 1 endings
 *     
 */
    
    /**
     * Manual Tests
     * 
     * Covers: Empty song
     * 1. Create the empty abc file
     * 2. Create new player for this file 
     * 3. Assure that nothing will play
     * 
     * 
     * Covers: Non default Key, default tempo, 1 voice, > 1 notes
     * 1. Create player for abc_song file
     * 2. Call play()
     * 3. Listen to it and make sure it sounds like the ABC song
     * 
     * 
     * Covers: > 100 bpm, > 1 voice, > 1 Accidental, > 1 Rest, > 1 Chord, > 1 Tuplet
     * 1. Create player for fur_elise abc file
     * 2. Call play
     * 3. Listen and ensure that it sounds like expected
     * 
     * Covers: > 0 && < 100 bpm
     * 1. Create player for invention abc file
     * 2. Call play
     * 3. Listen and ensure that it sounds like expected
     * 
     * Covers 1 note, 1 accidental, 1 rest
     * 1. Create abc file of standard header and the music composition of: C^ z
     * 2. Create player for this abc file
     * 3. Listen and ensure that it is a C# note and then a rest
     * 
     * Covers: > 1 endings in repeat
     * 1. Create player for paddy abc file 
     * 2. Call play
     * 3. Listen and ensure that is the expected sound 
     * 
     * Covers: 1 ending in repeat 
     * 1. Create abc file with the standard header and the music composition of: |: C D E F | G A B c :|
     * 2. Create player for this abc file
     * 3. Call play
     * 4. Listen and ensure that the notes repeat 
     * @throws UnableToParseException 
     * @throws InvalidMidiDataException 
     * @throws MidiUnavailableException 
     */
    
    // Covers: Player correctly playing abc 
    
    @Test
    private void testABCSong() throws UnableToParseException, MidiUnavailableException, InvalidMidiDataException {
        File abcSong = new File("sample-abc/abc_song.abc");
        Player abc = new Player(abcSong);
        abc.play();
    }
    
// Covers: Player correctly playing fur_elise 
    @Test
    private void testFurEliseSong() throws UnableToParseException, MidiUnavailableException, InvalidMidiDataException {
        File furElise = new File("sample-abc/fur_elise.abc");
        Player furElisePlayer = new Player(furElise);
        furElisePlayer.play();
    }
    
// Covers: Player correctly playing invention 
    
    @Test
    private void testInventionSong() throws UnableToParseException, MidiUnavailableException, InvalidMidiDataException {
        File inventionSong = new File("sample-abc/invention.abc");
        Player invention = new Player(inventionSong);
        invention.play();
    }
    
// Covers: Player correctly playing paddy 
    
    @Test
    private void testPaddySong() throws UnableToParseException, MidiUnavailableException, InvalidMidiDataException {
        File paddySong = new File("sample-abc/pafy.abc");
        Player paddy = new Player(paddySong);
        paddy.play();
    }
    
}
