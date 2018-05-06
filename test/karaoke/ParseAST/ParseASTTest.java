package karaoke.ParseAST;

import static org.junit.Assert.*;

import java.io.File;

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
    //              Can be parsed
    //
    //      Tuplets: Tuplets can be validly parsed
    //               Number: Duplet, Triplet, Quadruplet (check duration of each)
    //               Contains: notes, chords, both
    //                 
    //      Repeats: Ending: 1 ending, 2 different endings, >2 different endings
    //               syntax: enclosed by |: :|, only ends with :|, has [1, [2 ending notation
    //               number bars: repeat encloses 1 bar, 2 bars, >2 bars
    //
    //       Voices: number: 1 voice, 2 voices, >2 voices
    //               ordering: interleaved (one voice spread over multiple lines), not interleaved
    //
    //       Lyrics: Operators: -, _,*,~,<,|
    //               Operator partitions: - : location: between 2 strings, after a space, after a -, before a _
    //                                     |: enough notes in bar (ignored), fewer notes than bar (advances to next bar)
    //                                     _: 1, 2, >2 in a row 
    //       
    //       Comments: Comment at end of string, comment in beginning of string, comment in the middle of string
     
    private static final String DEFAULT_HEADER = "X: 1\r\n" + 
            "T:Title\r\n" + 
            "K:C\n";
    
    private static final String NEW_METER_HEADER = "X: 1\r\n" + 
            "T:Title\r\n"
            + "L: 1/8" + 
            "K:C\n";
    
    private static String generateHeader(double defaultNote, double meterNumerator, double meterDenominator, double tempo, Key key) {
        return "X:1\r\n" + 
                "T:Title\r\n" + 
                "M:"+meterNumerator+"/"+meterDenominator+"\r\n" + 
                "L:"+defaultNote+"\r\n" + 
                "Q:"+defaultNote+"="+tempo+"\r\n" + 
                "K:D";
    }
    //HEADER Test Cases
    //Covers: parseString: Header: Handles all cases
    @Test public void testParseStringHeaderAllPresent() throws UnableToParseException {
        File headerFile = new File("sample-abc/abc_song.abc");
        Composition music = MusicParser.parseFile(headerFile);
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
        Composition music = MusicParser.parse(basicSong);
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
        Composition music = MusicParser.parse(basicSong);
        //figure out how to test that this is correct AST
    }
    
    //Covers: parseString: Note: a-g
    @Test public void testParseStringNoteLowerCaseNoOperator() throws UnableToParseException {
        String basicSong = DEFAULT_HEADER+"a b c de f  g";
        Composition music = MusicParser.parse(basicSong);
        //figure out how to test one octave up
    }
    
    //Covers: parseString: Note: a-g, ' operator: 1, >1
    @Test public void testParseStringNoteLowerCaseOctaveOperator() throws UnableToParseException {
        String basicSong = DEFAULT_HEADER+"a'' b' c''' de' f'  g";
        Composition music = MusicParser.parse(basicSong);
        //figure out how to test multiple octave's up
    }
    
    //NOTE LENGTH Test cases
    //Covers: parseString: Note Length: Handles numerator and denominator
    @Test public void testParseStringNoteLengthDenominatorAndNumerator() throws UnableToParseException {
        String basicSong = DEFAULT_HEADER+"4A/3 17A/578";
        Composition music = MusicParser.parse(basicSong);
        assertEquals(4.0/3+17.0/578, music.duration(), .001);
    }
    
    //Covers: parseString: Note Length: Denominator
    @Test public void testParseStringNoteLengthDenominatorOnly() throws UnableToParseException {
        String basicSong = NEW_METER_HEADER+"A/3";
        Composition music = MusicParser.parse(basicSong);
        assertEquals(1/6, music.duration(), .001);
    }
    
    //Covers: parseString: Note Length: Handles denominator missing
    @Test public void testParseStringNoteLengthDenominatorMissing() throws UnableToParseException {
        String basicSong = NEW_METER_HEADER+"A/";
        Composition music = MusicParser.parse(basicSong);
        assertEquals(1/4, music.duration(), .001);
    }
    
    //Covers: parseString: Note Length: Numerator
    @Test public void testParseStringNoteLengthOnlyNumerator() throws UnableToParseException {
        String basicSong = DEFAULT_HEADER+"14A";
        Composition music = MusicParser.parse(basicSong);
        assertEquals(14, music.duration(), .001);
    }
    
    //ACCIDENTAL Test Cases (other test cases cover 0 accidentals)
    //Covers: parseString: Accidental: Can be parsed
    @Test public void testParseStringAccidentalParse() throws UnableToParseException {
        String basicSong = NEW_METER_HEADER+"^A __c ^^B =A";
        Composition music = MusicParser.parse(basicSong);
        assertEquals(NEW_METER_HEADER+"^A __c ^^B =A", music.toString());
    }
    
    //Covers: parseString: Accidental: applied to one note in bar, >1 Accidental
    @Test public void testParseStringAccidentalOneNote() throws UnableToParseException {
        String basicSong = generateHeader(1.0/8, 4, 4, 100, Key.F)+"^A";
        Composition music = MusicParser.parse(basicSong);
        assertEquals(generateHeader(1.0/8, 4, 4, 100, Key.F)+"^A", music.toString());
        
    }
    
    //Covers: parseString: Accidental: same note repeated in bar, not overridden, 1 accidental
    @Test public void testParseStringAccidentalMultipleNote() throws UnableToParseException {
        String basicSong = generateHeader(1.0/8, 4, 8, 100, Key.C)+"^A A B A A A";
        Composition music = MusicParser.parse(basicSong);
        assertEquals(generateHeader(1.0/8, 4, 8, 100, Key.C)+"^A ^A B ^A A A", music.toString());
    }
    
    //Covers: parseString: Accidental: same note different octaves in bar
    @Test public void testParseStringAccidentalDifferentOctaves() throws UnableToParseException {
        String basicSong = generateHeader(1.0/8, 4, 8, 100, Key.C)+"^A a B a'' A A";
        Composition music = MusicParser.parse(basicSong);
        assertEquals(generateHeader(1.0/8, 4, 8, 100, Key.C)+"^A a B a'' | A A", music.toString());
    }
    
    //Covers: parseString: Accidental: Overriden, same note repeated, >1 accidental
    @Test public void testParseStringAccidentalOverride() throws UnableToParseException {
        String basicSong = generateHeader(1.0/8, 8, 8, 100, Key.C)+"^A _A A =A A";
        Composition music = MusicParser.parse(basicSong);
        assertEquals(generateHeader(1.0/8, 8, 8, 100, Key.C)+"^A _A _A =A A", music.toString());
    }
    
    //REST Test Cases Dotun
    //Covers: parseString: rest: Handles 'z' rest operator
    @Test public void testParseStringRest() throws UnableToParseException {
        String basicSong = generateHeader(1.0/8, 8, 8, 100, Key.C) + "z ^A z _A";
        Composition music = MusicParser.parse(basicSong);
        assertEquals(basicSong, music.toString());
    }
    
    //REPEAT Test Cases
    // Covers: Repeat with the same ending
    @Test public void testParseStringRepeat() throws UnableToParseException{
        String basicSong = generateHeader(1.0/8, 8, 8, 100, Key.C) + "|: C D E F | G A B c :|";
        Composition music = MusicParser.parse(basicSong);
        assertEquals(basicSong, music.toString());
    }
    
    // Covers: Repeat with different endings
    @Test public void testParseStringRepeatDiffEnding() throws UnableToParseException{
        String basicSong = generateHeader(1.0/8, 8, 8, 100, Key.C) + "|: C D E F |[1 G A B c | G A B B :|[2 F E D C |";
        Composition music = MusicParser.parse(basicSong);
        assertEquals(basicSong, music.toString()); 
    }
    
    //CHORDS Test Cases Nick implements
    //Covers: parseString: Chord: All same duration
    @Test public void testParseStringChordSameDuration() {
        
    }
    
    //Covers: parseString: Chord: Different durations
    @Test public void testParseStringChordDifferentDuration() {
        
    }
    
    
    //TUPLETS  Nick Implements
    

    //VOICES tests (normal tests cover 1 voice not interleaved)
    
    //Covers: parseString: Voices: 2 voices, not interleaved
    @Test public void testParseStringTwoVoice() throws UnableToParseException {
        File headerFile = new File("sample-abc/fur_elise.abc");
        Composition music = MusicParser.parseFile(headerFile);
        //check some property of voice is correct
    }
    
    //Covers: parseString: Voices: >2 voices, interleaved
    @Test public void testParseStringMultiVoiceInterleaved() throws UnableToParseException {
        File headerFile = new File("sample-abc/prelude.abc");
        Composition music = MusicParser.parseFile(headerFile);
        //check that some property of voice is correct
    }
    
    //LYRICS Nick
    
    //COMMENTS
    
    
    
}   
