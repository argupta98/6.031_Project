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
    //                          (chord duration is the duration of first note)
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
    //       Comments: Comment at end of string, comment in beginning of string, comment in the midle of string
      
    
    @Test public void test() {
        fail("Not yet implemented");
    }

}
