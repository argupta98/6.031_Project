package karaoke.ParseAST;

import static org.junit.Assert.*;

import org.junit.Test;

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
    //                 The special meter C (“common time”) means 4/4, and C| (“cut common time”) means 2/2.
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
     
    
    //HEADER Test Cases
    //Covers: parseString: Header: Handles all cases
    @Test public void testParseStringHeaderAllPresent() {
        
    }
    
    //Covers: parseString: Header: Handles Omission of all possible omitted fields
    @Test public void testParseStringHeaderWithDefaults() {
        
    }
    
    //NOTE test cases
    //Covers: parseString: Note: A-G
    @Test public void testParseStringNoteUpperCase() {
        
    }
    
    //Covers: parseString: Note: a-g
    @Test public void testParseStringNoteLowerCaseNoOperator() {
        
    }
    
    //Covers: parseString: Note: a-g, ' operator: 1, >1
    @Test public void testParseStringNoteLowerCaseOctaveOperator() {
        
    }
    
    //NOTE LENGTH Test cases
    //Covers: parseString: Note Length: Handles numerator and denominator
    @Test public void testParseStringNoteLengthDenominatorAndNumerator() {
        
    }
    
    //Covers: parseString: Note Length: Denominator
    @Test public void testParseStringNoteLengthDenominatorOnly() {
        
    }
    
    //Covers: parseString: Note Length: Handles denominator missing
    @Test public void testParseStringNoteLengthDenominatorMissing() {
        
    }
    
    //Covers: parseString: Note Length: Handles numerator missing
    @Test public void testParseStringNoteLengthNumeratorMissing() {
        
    }
    
    //Covers: parseString: Note Length: Numerator
    @Test public void testParseStringNoteLengthOnlyNumerator() {
        
    }
    
    //ACCIDENTAL Test Cases (other test cases cover 0 accidentals)
    //Covers: parseString: Accidental: Can be parsed
    @Test public void testParseStringAccidentalParse() {
        
    }
    
    //Covers: parseString: Accidental: applied to one note in bar, >1 Accidental
    @Test public void testParseStringAccidentalOneNote() {
        
    }
    
    //Covers: parseString: Accidental: same note repeated in bar, not overridden, 1 accidental
    @Test public void testParseStringAccidentalMultipleNote() {
        
    }
    
    //Covers: parseString: Accidental: same note different octaves in bar
    @Test public void testParseStringAccidentalDifferentOctaves() {
        
    }
    
    //Covers: parseString: Accidental: Overriden, same note repeated, >1 accidental
    @Test public void testParseStringAccidentalOverride() {
        
    }
    
    //REST Test Cases Dotun
    //Covers: parseString: rest: Handles 'z' rest operator
    @Test public void testParseStringRest() {
        
    }
    
    //CHORDS Test Cases Nick implements
    //Covers: parseString: Chord: All same duration
    @Test public void testParseStringChordSameDuration() {
        
    }
    
    //Covers: parseString: Chord: Different durations
    @Test public void testParseStringChordDifferentDuration() {
        
    }
    
    //Covers: parseString: Chord: All same duration
    @Test public void testParseString() {
        
    }
    
    //TUPLETS  Nick Implements
    
    //VOICES tests
    
    
    //LYRICS Nick
    
    
    
}   
