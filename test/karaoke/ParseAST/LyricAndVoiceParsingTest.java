package karaoke.ParseAST;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import org.junit.Test;

import edu.mit.eecs.parserlib.UnableToParseException;
import karaoke.Composition;
import karaoke.Composition.Key;
import karaoke.parser.MusicParser;
import karaoke.sound.MidiSequencePlayer;
import karaoke.sound.SequencePlayer;

/**
 * Tests require music be played, so we do not want didit to run them
 * @category no_didit
 */
public class LyricAndVoiceParsingTest {
	
	    // Parititions for parsing voices and Lyrics
	    //       Voices: number: 1 voice, 2 voices, >2 voices
	    //               ordering: interleaved (one voice spread over multiple lines), not interleaved
	    //
	    //       Lyrics: Operators: -, _,*,~,<,|
	    //               Operator partitions: - : location: between 2 strings, after a space, after a -, before a _
	    //                                     |: enough notes in bar (ignored), fewer notes than bar (advances to next bar)
	    //                                     _: 1, 2, >2 in a row 
	
	 	private static String generateHeader(int defaultNoteDenominator, int meterNumerator, int meterDenominator, int tempo, Key key) {
	        return "X:1\r\n" + 
	                "T:Title\r\n" + 
	                "M:"+meterNumerator+"/"+meterDenominator+"\r\n" + 
	                "L: 1/"+defaultNoteDenominator+"\r\n" + 
	                "Q: 1/"+defaultNoteDenominator+"="+tempo+"\r\n" + 
	                "K: "+key+"\n";
	    }
	    
	    private static void playMusic(Composition music, List<String> lyriclines) throws MidiUnavailableException, InvalidMidiDataException {
	        final int beatsPerMinute = 100;
	        final int ticksPerBeat = 64;
	        SequencePlayer player = new MidiSequencePlayer(ticksPerBeat, beatsPerMinute);
	        music.play(player);
	        Object lock = new Object();
	        player.addEvent(music.duration(), (Double beat) -> {
	            synchronized (lock) {
	                lock.notify();
	            }
	        });
	        
	        player.play();
	        
	        synchronized (lock) {
	            try {
	                lock.wait();
	            } catch (InterruptedException e) {
	                return;
	            }
	        }
	    }
	    
	    //VOICES tests (normal tests cover 1 voice not interleaved)
	    
	    //Covers: parseString: Voices: 2 voices, not interleaved
	    @Test public void testParseStringTwoVoice() throws UnableToParseException {
	        File headerFile = new File("sample-abc/fur_elise.abc");
	        Composition music = (new MusicParser()).parseFile(headerFile);
	        //check some property of voice is correct
	    }
	    
	    //Covers: parseString: Voices: >2 voices, interleaved
	    @Test public void testParseStringMultiVoiceInterleaved() throws UnableToParseException {
	        File headerFile = new File("sample-abc/prelude.abc");
	        Composition music = (new MusicParser()).parseFile(headerFile);
	        //check that some property of voice is correct
	    }
	    
		//LYRICS Nick
	    //Covers: hyphen between strings
	    @Test public void testParseLyricsHyphenBetweenStrings() throws UnableToParseException, MidiUnavailableException, InvalidMidiDataException {
	        String basicSong = generateHeader(4, 4, 4, 100, Key.C) + "C C C C|\nw: syll-a-ble";
	        Composition music = (new MusicParser()).parse(basicSong);
	        List<String> lines = new ArrayList<>();
	        music.addVoiceListener("", (String line) -> {lines.add(line);});
	        playMusic(music, lines);
	        List<String> expected = Arrays.asList("*syll*able", "syll*a*ble", "sylla*ble*", "syllable", "END");
	        assertEquals(expected, lines);
	    }
	    
	    //Covers: hyphen before underscore
	    @Test public void testParseLyricsHyphenBeforeUnderScore() throws UnableToParseException, MidiUnavailableException, InvalidMidiDataException {
	        String basicSong = generateHeader(4, 4, 4, 100, Key.C) + "C C C C|\nw: syll-a-_ble";
	        Composition music = (new MusicParser()).parse(basicSong);
	        List<String> lines = new ArrayList<>();
	        music.addVoiceListener("", (String line) -> {lines.add(line);});
	        playMusic(music, lines);
	        List<String> expected = Arrays.asList("*syll*able", "syll*a*ble", "syll*a*ble", "sylla*ble*", "END");
	        assertEquals(expected, lines);
	    }
	    
	    //Covers: hyphen after hyphen
	    @Test public void testParseLyricsHyphenAfterHyphen() throws UnableToParseException, MidiUnavailableException, InvalidMidiDataException {
	        String basicSong = generateHeader(4, 4, 4, 100, Key.C) + "C C C C|\nw: syll-a--ble";
	        Composition music = (new MusicParser()).parse(basicSong);
	        List<String> lines = new ArrayList<>();
	        music.addVoiceListener("", (String line) -> {lines.add(line);});
	        playMusic(music, lines);
	        List<String> expected = Arrays.asList("*syll*able", "syll*a*ble", "syllable", "sylla*ble*", "END");
	        assertEquals(expected, lines);
	    }
	    
	    //Covers: hyphen after space
	    @Test public void testParseLyricsHyphenAfterSpace() throws UnableToParseException, MidiUnavailableException, InvalidMidiDataException {
	        String basicSong = generateHeader(4, 4, 4, 100, Key.C) + "C C C C|\nw: syll-a -ble";
	        Composition music = (new MusicParser()).parse(basicSong);
	        List<String> lines = new ArrayList<>();
	        music.addVoiceListener("", (String line) -> {lines.add(line);});
	        playMusic(music, lines);
	        List<String> expected = Arrays.asList("*syll*able", "syll*a*ble", "syllable", "sylla*ble*", "END");
	        assertEquals(expected, lines);
	    }
	    
	    //Covers: single underscore
	    @Test public void testParseStringLyricsSingleUnderScore() throws UnableToParseException, MidiUnavailableException, InvalidMidiDataException {
	        String basicSong = generateHeader(4, 4, 4, 100, Key.C) + "C C C C|\nw: time_";
	        Composition music = (new MusicParser()).parse(basicSong);
	        List<String> lines = new ArrayList<>();
	        music.addVoiceListener("", (String line) -> {lines.add(line);});
	        playMusic(music, lines);
	        List<String> expected = Arrays.asList("*time*", "*time*", "time", "time", "END");
	        assertEquals(expected, lines);
	    }
	    
	    //Covers: multiple underscore
	    @Test public void testParseStringLyricsMultipleUnderScore() throws UnableToParseException, MidiUnavailableException, InvalidMidiDataException {
	        String basicSong = generateHeader(4, 4, 4, 100, Key.C) + "C C C C|\nw: time__";
	        Composition music = (new MusicParser()).parse(basicSong);
	        List<String> lines = new ArrayList<>();
	        music.addVoiceListener("", (String line) -> {lines.add(line);});
	        playMusic(music, lines);
	        List<String> expected = Arrays.asList("*time*", "*time*", "*time*", "time", "END");
	        assertEquals(expected, lines);
	    }
	    
	    //Covers: blank operator
	    @Test public void testParseStringLyricsBlankSyllable() throws UnableToParseException, MidiUnavailableException, InvalidMidiDataException {
	        String basicSong = generateHeader(4, 4, 4, 100, Key.C) + "C C C C|\nw: syll*ble";
	        Composition music = (new MusicParser()).parse(basicSong);
	        List<String> lines = new ArrayList<>();
	        music.addVoiceListener("", (String line) -> {lines.add(line);});
	        playMusic(music, lines);
	        List<String> expected = Arrays.asList("*syll*ble", "syllble", "syll*ble*", "syllble", "END");
	        assertEquals(expected, lines);
	    }
	    
	    //Covers: squiggle operator
	    @Test public void testParseLyricsSquiggleOperator() throws UnableToParseException, MidiUnavailableException, InvalidMidiDataException {
	        String basicSong = generateHeader(4, 4, 4, 100, Key.C) + "C C C C|\nw: of~the~day";
	        Composition music = (new MusicParser()).parse(basicSong);
	        List<String> lines = new ArrayList<>();
	        music.addVoiceListener("", (String line) -> {lines.add(line);});
	        playMusic(music, lines);
	        List<String> expected = Arrays.asList("*of the day*", "of the day", "of the day", "of the day", "END");
	        assertEquals(expected, lines);
	    }
	    
	    //Covers: \- operator
	    @Test public void testParseLyricsSlashDash() throws UnableToParseException, MidiUnavailableException, InvalidMidiDataException {
	        String basicSong = generateHeader(4, 4, 4, 100, Key.C) + "C C C C|\nw: of\\-the day";
	        Composition music = (new MusicParser()).parse(basicSong);
	        List<String> lines = new ArrayList<>();
	        music.addVoiceListener("", (String line) -> {lines.add(line);});
	        playMusic(music, lines);
	        List<String> expected = Arrays.asList("*of-the* day", "of-the *day*", "of-the day", "of-the day", "END");
	        assertEquals(expected, lines);
	    }
	    
	    //Covers: | operator, enough notes in bar (ignored)
	    @Test public void testParseLyricsBarEnoughNotes() throws UnableToParseException, MidiUnavailableException, InvalidMidiDataException {
	        String basicSong = generateHeader(4, 4, 4, 100, Key.C) + "C C C C| C C C C|\nw: a-b-c-d | a-b-c-d";
	        Composition music = (new MusicParser()).parse(basicSong);
	        List<String> lines = new ArrayList<>();
	        music.addVoiceListener("", (String line) -> {lines.add(line);});
	        playMusic(music, lines);
	        List<String> expected = Arrays.asList("*a*bcd abcd", "a*b*cd abcd", "ab*c*d abcd", "abc*d* abcd", "abcd *a*bcd", "abcd a*b*cd", "abcd ab*c*d", "abcd abc*d*", "END");
	        assertEquals(expected, lines);
	    }
	    
	    //Covers: | operator, fewer notes than bar (advances to next bar)
	    @Test public void testParseLyricsBarNotEnoughNotes() throws UnableToParseException, MidiUnavailableException, InvalidMidiDataException {
	        String basicSong = generateHeader(4, 4, 4, 100, Key.C) + "C C C z | C C C C|\nw: a-b-c | a-b-c-d";
	        Composition music = (new MusicParser()).parse(basicSong);
	        List<String> lines = new ArrayList<>();
	        music.addVoiceListener("", (String line) -> {lines.add(line);});
	        playMusic(music, lines);
	        List<String> expected = Arrays.asList("*a*bc abcd", "a*b*c abcd", "ab*c* abcd", "abc *a*bcd", "abc a*b*cd", "abc ab*c*d", "abc abc*d*", "END");
	        assertEquals(expected, lines);
	    }

}
