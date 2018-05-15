package karaoke.ParseAST;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import edu.mit.eecs.parserlib.UnableToParseException;
import karaoke.Composition;
import karaoke.Composition.Key;
import karaoke.parser.MusicParser;


//Test class for parser and AST
public class ParseASTTest {
    
    //Test Partitions
    //  parseFile():
    //      File Existence: File exists, File Doesn't exist
    //      File Formatting: File is correctly formatted, file is not correctly formatted
    //      (Actual parsing is done by parseString which is tested for more parsing related things)
    //
    //  parseString():
    //      Header: Correctly handles following Cases:
    //              C: Name of the composer.
    //              K: Key, which determines the key signature for the piece.
    //              L: Default length or duration of a note.
    //              M: Meter. It determines the sum of the durations of all notes within a bar, expressed as a rational number like 4/4 or 3/8. 
    //                 The special meter C (�common time�) means 4/4, and C| (�cut common time�) means 2/2.
    //              Q: Tempo. It represents the number of beats of the given length to play per minute.
    //              T: Title of the piece.
    //              V: Voices used in the piece.
    //              X: Track number
    //              Correctly handles omission of M, L, Q, C, and resulting AST has default values
    //
    //       Note:  Handles uppercase A-G, lowercase a-g, handle ' operator
    //
    //       Note Length: Handles numerator and denominator, numerator omitted, denominator omitted
    //
	//		 Tempo and Length: length: note length 1/4, note length > 1/4, note length < 1/4
	//						   tempo: 100, <100, >100
	//
	//
    //       Accidentals: Can parse ^,_,= operators, 
    //                    Octaves: only one instance of that note in the bar, 
    //                             multiple instances of the note in the bar,
    //                             multiple instances of the note at different octaves
    //                    number: 0 accidentals in bar, 
    //                            1 accidental in bar, 
    //                           >1 accidental in bar
    //                    Overriden: Accidental is overriden over the course of a bar
    //                               Accidental is not overriden over the course of a bar
    //
    //      Rests: Handles 'z' rest operator, note length tests apply
    //
    //      Chords: Duration: All notes have same duration, different notes have different durations 
    //                        (chord duration is the duration of first note)
    //                        Consider case with staggering... example [C2E4]G2 should 
    //                        be C played with E and then E played with G
    //              Can be parsed
    //
    //      Tuplets: Tuplets can be validly parsed
    //               Number: Duplet, Triplet, Quadruplet (check duration of each)
    //               Contains: notes, chords, both
    //               Notes of different lengths, same length
    //                 
    //      Repeats: Ending: 1 ending, 2 different endings, >2 different endings
    //               syntax: enclosed by |: :|, only ends with :|, has [1, [2 ending notation
    //               number bars: repeat encloses 1 bar, 2 bars, >2 bars
    //               repeat occurs over mutiple lines
	//				 repeat starts at end of major section
	//
	//		 Voices and lyric Tests in LyricAndVoiceParsingTest.java 
    //       
    //       Comments: Comment at end of string, comment in beginning of string, comment in the middle of string
     
    private static final String DEFAULT_HEADER = "X: 1\r\n" + 
            "T:Title\n" + 
            "K:C\n";
    
    private static final String NEW_METER_HEADER = "X: 1\r\n" + 
            "T:Title\r\n"
            + "L: 1/8\n" + 
            "K:C\n";
    
    private static String generateHeader(int defaultNoteDenominator, int meterNumerator, int meterDenominator, int tempo, Key key) {
        return "X:1\r\n" + 
                "T:Title\r\n" + 
                "M:"+meterNumerator+"/"+meterDenominator+"\r\n" + 
                "L: 1/"+defaultNoteDenominator+"\r\n" + 
                "Q: 1/"+defaultNoteDenominator+"="+tempo+"\r\n" + 
                "K: "+key+"\n";
    }
    
    //HEADER Test Cases
    //Covers: parseString: Header: Handles all cases
    @Test public void testParseStringHeaderAllPresent() throws UnableToParseException {
        File headerFile = new File("sample-abc/abc_song.abc");
        Composition music = (new MusicParser()).parseFile(headerFile);
        assertEquals("Alphabet Song", music.title());
        assertEquals("Traditional Kid's Song", music.composer());
        assertEquals(100, music.tempo(), .001);
        assertEquals(1.0/4, music.length(), .001);
        assertEquals(1, music.trackNumber());
        assertEquals(Key.D, music.key());
        assertEquals(4.0/4.0, music.meter(), .0001);
    }
    
    //Covers: parseString: Header: Handles Omission of all possible omitted fields
    @Test public void testParseStringHeaderWithDefaults() throws UnableToParseException {
        String basicSong = DEFAULT_HEADER+"C C G";
        Composition music = (new MusicParser()).parse(basicSong);
        assertEquals("Title", music.title());
        assertEquals("Unknown", music.composer());
        assertEquals(100, music.tempo(), .001);
        assertEquals(1.0/4, music.length(),.001);
        assertEquals(1, music.trackNumber());
        assertEquals(Key.C, music.key());
        assertEquals(4.0/4.0, music.meter(), .001);
    }
    
    //NOTE test cases
    //Covers: parseString: Note: A-G
    @Test public void testParseStringNoteUpperCase() throws UnableToParseException {
        String basicSong = DEFAULT_HEADER+"A B C DE F  G";
        Composition music = (new MusicParser()).parse(basicSong);
        assertEquals("ABCDEFG\n", music.toString());
    }
    
    //Covers: parseString: Note: a-g
    @Test public void testParseStringNoteLowerCaseNoOperator() throws UnableToParseException {
        String basicSong = DEFAULT_HEADER+"a b c de f  g";
        Composition music = (new MusicParser()).parse(basicSong);
        assertEquals("A'B'C'D'E'F'G'\n", music.toString());
    }
    
    //Covers: parseString: Note: a-g, ' operator: 1, >1
    @Test public void testParseStringNoteLowerCaseOctaveOperator() throws UnableToParseException {
        String basicSong = DEFAULT_HEADER+"a'' b' c''' de' f'  g";
        Composition music = (new MusicParser()).parse(basicSong);
        //figure out how to test multiple octave's up
    }
    
    //NOTE LENGTH Test cases
    //Covers: parseString: Note Length: Handles numerator and denominator
    @Test public void testParseStringNoteLengthDenominatorAndNumerator() throws UnableToParseException {
        String basicSong = DEFAULT_HEADER+"A4/3 A17/578";
        Composition music = (new MusicParser()).parse(basicSong);
        assertEquals(4.0/3+17.0/578, music.duration(), .001);
    }
    
    //Covers: parseString: Note Length: Denominator
    @Test public void testParseStringNoteLengthDenominatorOnly() throws UnableToParseException {
        String basicSong = NEW_METER_HEADER+"A/3";
        Composition music = (new MusicParser()).parse(basicSong);
        assertEquals(1.0/6, music.duration(), .001);
    }
    
    //Covers: parseString: Note Length: Handles denominator missing
    @Test public void testParseStringNoteLengthDenominatorMissing() throws UnableToParseException {
        String basicSong = NEW_METER_HEADER+"A/";
        Composition music = (new MusicParser()).parse(basicSong);
        assertEquals(1.0/4, music.duration(), .001);
    }
    
    //Covers: parseString: Note Length: Numerator
    @Test public void testParseStringNoteLengthOnlyNumerator() throws UnableToParseException {
        String basicSong = DEFAULT_HEADER+"A14";
        Composition music = (new MusicParser()).parse(basicSong);
        assertEquals(14, music.duration(), .001);
    }
    
    //TEMPO and LENGTH ( 1/4 = 100 covered in other tests)
    
    //covers length < 1/4, tempo > 100
    @Test public void testParseStringLengthEigthTempo200() throws UnableToParseException {
    	String basicSong = generateHeader(8, 4, 8, 200, Key.C)+"^A A B A | A A";
    	Composition music = (new MusicParser()).parse(basicSong);
    	assertEquals(1.5, music.duration(), .001);
    }
    
    //covers length > 1/4, tempo < 100
    @Test public void testParseStringLengthHalfTempo50() throws UnableToParseException {
    	String basicSong = generateHeader(2, 4, 2, 50, Key.Cm)+"^A A B A | A A";
    	Composition music = (new MusicParser()).parse(basicSong);
    	assertEquals(24, music.duration(), .001);
    }
    
    
    //ACCIDENTAL Test Cases (other test cases cover 0 accidentals)
    //Covers: parseString: Accidental: Can be parsed
    @Test public void testParseStringAccidentalParse() throws UnableToParseException {
        String basicSong = NEW_METER_HEADER+"^A __c ^^B' =A";
        Composition music = (new MusicParser()).parse(basicSong);
        assertEquals("^A^A^C''A\n", music.toString());
    }
    
    //Covers: parseString: Accidental: applied to one note in bar, >1 Accidental
    @Test public void testParseStringAccidentalOneNote() throws UnableToParseException {
        String basicSong = generateHeader(8, 4, 4, 100, Key.F)+"^A";
        Composition music = (new MusicParser()).parse(basicSong);
        assertEquals("^A\n", music.toString());
        
    }
    
    //Covers: parseString: Accidental: same note repeated in bar, not overridden, 1 accidental
    @Test public void testParseStringAccidentalMultipleNote() throws UnableToParseException {
        String basicSong = generateHeader(8, 4, 8, 100, Key.C)+"^A A B A | A A";
        Composition music = (new MusicParser()).parse(basicSong);
        assertEquals("^A^AB^AAA\n", music.toString());
    }
    
    //Covers: parseString: Accidental: same note different octaves in bar
    @Test public void testParseStringAccidentalDifferentOctaves() throws UnableToParseException {
        String basicSong = generateHeader(8, 4, 8, 100, Key.C)+"^A a B a''| A A";
        Composition music = (new MusicParser()).parse(basicSong);
        assertEquals("^AA'BA'''AA\n", music.toString());
    }
    
    //Covers: parseString: Accidental: Overriden, same note repeated, >1 accidental
    @Test public void testParseStringAccidentalOverride() throws UnableToParseException {
        String basicSong = generateHeader(8, 8, 8, 100, Key.C)+"^A _A A =A A";
        Composition music = (new MusicParser()).parse(basicSong);
        assertEquals("^A^G^GAA\n", music.toString());
    }
    
    //REST Test Cases Dotun
    //Covers: parseString: rest: Handles 'z' rest operator
    @Test public void testParseStringRest() throws UnableToParseException {
        String basicSong = generateHeader(8, 8, 8, 100, Key.C) + "z ^A z3/4 _A";
        Composition music = (new MusicParser()).parse(basicSong);
        assertEquals("z^Az^G\n", music.toString());
    }
    
    //REPEAT Test Cases
    // Covers: Repeat with the same ending
    @Test public void testParseStringRepeat() throws UnableToParseException{
        String basicSong = generateHeader(8, 8, 8, 100, Key.C) + "|: C D E F | G A B c :|";
        Composition music = (new MusicParser()).parse(basicSong);
        assertEquals(Key.C, music.key());
        assertEquals("|:CDEFGABC':|\n", music.toString());
    }
    
    // Covers: Repeat with different endings
    @Test public void testParseStringRepeatDiffEnding() throws UnableToParseException{
        String basicSong = generateHeader(8, 8, 8, 100, Key.C) + "|: C D E F |[1 G A B c | G A B B :|[2 F E D C |\n";
        Composition music = (new MusicParser()).parse(basicSong);
        assertEquals("|:CDEF[1GABC'GABB:|[2FEDC\n", music.toString()); 
    }
    
    // Covers: Repeat over multiple lines
    @Test public void testParseStringRepeatDiffLines() throws UnableToParseException{
        String basicSong = generateHeader(8, 8, 8, 100, Key.C) + "|: C D E F |[1 G A B c \n"+
                             "| G A B B :|[2 F E D C |\n";
        Composition music = (new MusicParser()).parse(basicSong);
        assertEquals("|:CDEF[1GABC'GABB:|[2FEDC\n", music.toString()); 
    }
    
    //Covers: Repeat over Multiple lines, Repeat starts after major section ending
    @Test public void testParseStringPaddyRepeats() throws UnableToParseException {
    	Composition music = (new MusicParser()).parseFile(new File("sample-abc/paddy.abc"));
    	System.out.println(music.toString());
        assertEquals("|:D'F'F'C'E'E'D'E'F'G'F'E'D'F'F'C'"
        		+ "E'E'D'F'E'D'BAD'F'F'C'E'E'D'E'F'G'F'E'"
        		+ "F'A'F'G'F'E'[1D'F'E'D'BA:|[2D'F'E'D'C'B|:AB"
        		+ "G'F'E'F'D'BA^FABC'D'F'E'D'C'BABE'F'E'E'F'G'F'"
        		+ "A'F'G'F'E'[1D'F'E'D'C'B:|[2D'F'E'D'BA|:F'AAE'AA"
        		+ "D'E'F'G'F'E'F'AAE'AAD'F'E'D'BAF'AAE'AAD'E'F'G'F'"
        		+ "E'F'A'F'G'F'E'D'F'E'D'BA:|\n"
        		, music.toString()); 
    }
    
    //CHORDS Test Cases Nick implements
    //Covers: parseString: Chord: All same duration
    @Test public void testParseStringChordSameDuration() throws UnableToParseException{
        String basicSong = generateHeader(4, 4, 4, 100, Key.C) + "[CEG]";
        Composition music = (new MusicParser()).parse(basicSong);
        assertEquals(1.0, music.duration(), 0.001);
    }
    
    //Covers: parseString: Chord: Different durations
    @Test public void testParseStringChordDifferentDuration() throws UnableToParseException {
        String basicSong = generateHeader(4, 4, 4, 100, Key.C) + "[C/EG]";
        Composition music = (new MusicParser()).parse(basicSong);
        assertEquals(1.0/2, music.duration(), 0.001);
    }
    
    //Covers: parseString: Chord: Staggering
    // example [C2E4]G2 -> should be and E and C playing together and then an E and G playing together
    @Test public void testParseStringChordsStaggered() throws UnableToParseException {
        String basicSong = generateHeader(4, 4, 4, 100, Key.C) + "[C2E4]G2";
        Composition music = (new MusicParser()).parse(basicSong);
        assertEquals(4.0, music.duration(), 0.001);
    } 
    
    //TUPLETS  Nick Implements
    
    //Covers: duplet -> notes should be played 3/2 of the original duration
    @Test public void testParseStringDuplet() throws UnableToParseException{
        String basicSong = generateHeader(4, 4, 4, 100, Key.C) + "(2CC A B C";
        Composition music = (new MusicParser()).parse(basicSong);
        assertEquals(6.0, music.duration(), 0.001);   
    }
    
    //Covers: triplet -> notes should be played for 2/3 of the original duration
    @Test public void testParseStringTriplet() throws UnableToParseException{
        String basicSong = generateHeader(4, 4, 4, 100, Key.C) + "(3CCC D";
        Composition music = (new MusicParser()).parse(basicSong);
        assertEquals(3.0, music.duration(), 0.001);
    }
    
    //Covers: quadruplet -> notes should be played for 3/4 of the original duration
    @Test public void testParseStringQuadruplet() throws UnableToParseException{
        String basicSong = generateHeader(4, 4, 4, 100, Key.C) + "(4CCCC F G";
        Composition music = (new MusicParser()).parse(basicSong);
        assertEquals(5.0, music.duration(), 0.001);
    }
    
    //Covers: tuplet containing chords
    @Test public void testParseStringDupletWithChords() throws UnableToParseException{
        String basicSong = generateHeader(4, 4, 4, 100, Key.C) + "(2[CEG]C";
        Composition music = (new MusicParser()).parse(basicSong);
        assertEquals(3.0, music.duration(), 0.001);
    }
    
    //Covers: tuplet where notes are different lengths
    @Test public void testParseStringTripletDifferentLengths() throws UnableToParseException{
        String basicSong = generateHeader(4, 4, 4, 100, Key.C) + "(3C/EG";
        Composition music = (new MusicParser()).parse(basicSong);
        assertEquals(5.0/3, music.duration(), 0.001);
    }
    
    //Covers: Comments: ALL, Parseability: ALL
    @Test public void testParseAllFiles() throws UnableToParseException {
    	List<String> filenames = Arrays.asList("abc_song", "fur_elise", "invention", "little_night_music",
    			"paddy", "piece1", "piece2", "piece3", "prelude", "rains_of_castamere", "sample1", "sample2", 
    			"sample3", "scale", "waxies_dargle");
    	for(String name: filenames) {
    		(new MusicParser()).parseFile(new File("sample-abc/"+name+".abc"));
    	}
    }
    
}   
