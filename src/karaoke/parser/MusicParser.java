/* Copyright (c) 2017-2018 MIT 6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package karaoke.parser;

import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import edu.mit.eecs.parserlib.ParseTree;
import edu.mit.eecs.parserlib.Parser;
import edu.mit.eecs.parserlib.UnableToParseException;
import edu.mit.eecs.parserlib.Visualizer;
import karaoke.Chord;
import karaoke.Composition;
import karaoke.Composition.Key;
import karaoke.Music;
import karaoke.Voice;
import karaoke.sound.Pitch;

public class MusicParser {
    private static double DEFAULT_TEMPO = 100;
    private static String DEFAULT_COMPOSER = "Unknown";
    private static double DEFAULT_LENGTH = 1.0/4;
    private static double DEFAULT_METER = 1.0;
    private static Map<Key, List<Character>> SHARPS_FOR_KEY = new HashMap<>();
    static {
        //TODO
    }
    private static Map<Key, List<Character>> FLATS_FOR_KEY = new HashMap<>();
    static {
        //TODO
    }
    /**
     * Main method. Parses and then reprints an example expression.
     * 
     * @param args command line arguments, not used
     * @throws UnableToParseException if example expression can't be parsed
     */
    public static void main(final String[] args) throws UnableToParseException {
        final String input = "X:1\r\n" + 
                "T:Title\r\n" + 
                "M: 4/4 \r\n" + 
                "L: 1/4 \r\n" + 
                "Q: 1/4 = 100\r\n" + 
                "V: voice1\n"+
                "V: voice2\n"+
                "K:D\n"+
                "V: voice1\n"+
                "^^A _b'' c,3/4 D4 | E/4 F/|\n"+
                "w: I a-m so happy~ness!\n"+
                "V: voice2\n"+
                "[AB] [A/2B/] (3ABC (2a/2b/2 c,3/4 D4 | E/4 F/|\n"+
                "w: I a-m so happy~ness!\n";
        
        System.out.println(input);
        MusicParser.parse(input);
        //System.out.println(expression);
    }
    
    // the nonterminals of the grammar
    private static enum MusicGrammar {
        COMPOSITION, HEADER, TRACKNUMBER, COMPOSER, METER, LENGTH, TEMPO,
        VOICENAME, KEY, VOICE, MUSICLINE, MEASURE, NOTE, OCTAVEUP, OCTAVEDOWN,
        NOTENUMERATOR, NOTEDENOMINATOR, ACCIDENTAL, SHARP, FLAT, LYRIC, SYLLABLENOTE, 
        SYLLABLE, LETTER, COMMENT, NUMBER, END, WHITESPACE, WHITESPACEANDCOMMENT, TITLE,
        CHORD, TUPLE, DENOMINATOR, NUMERATOR
    }

    private static Parser<MusicGrammar> parser = makeParser();
    
    /**
     * Compile the grammar into a parser.
     * 
     * @return parser for the grammar
     * @throws RuntimeException if grammar file can't be read or has syntax errors
     */
    private static Parser<MusicGrammar> makeParser() {
        try {
            // read the grammar as a file, relative to the project root.
            final File grammarFile = new File("src/karaoke/parser/Abc.g");
            return Parser.compile(grammarFile, MusicGrammar.COMPOSITION);

            // A better way would read the grammar as a "classpath resource", which would allow this code 
            // to be packed up in a jar and still be able to find its grammar file:
            //
            // final InputStream grammarStream = Expression.class.openResourceAsStream("Expression.g");
            // return Parser.compile(grammarStream, ExpressionGrammar.EXPRESSION);
            //
            // See http://www.javaworld.com/article/2077352/java-se/smartly-load-your-properties.html
            // for a discussion of classpath resources.
            

        // Parser.compile() throws two checked exceptions.
        // Translate these checked exceptions into unchecked RuntimeExceptions,
        // because these failures indicate internal bugs rather than client errors
        } catch (IOException e) {
            throw new RuntimeException("can't read the grammar file", e);
        } catch (UnableToParseException e) {
            throw new RuntimeException("the grammar has a syntax error", e);
        }
    }

    /**
     * Parse a string into an Composition.
     * @param string string to parse
     * @return Expression parsed from the string
     * @throws UnableToParseException if the string doesn't match the Expression grammar
     */
    public static Composition parse(final String string) throws UnableToParseException {
        // parse the example into a parse tree
        final ParseTree<MusicGrammar> parseTree = parser.parse(string);

        // display the parse tree in various ways, for debugging only
        //System.out.println("parse tree " + parseTree);
        //Visualizer.showInBrowser(parseTree);
        
        final Composition composition = makeComposition(parseTree);
       // System.out.println("AST " + expression);
        
        return composition;
    }
    

    /**
     * Parse a File into an Composition.
     * @param file file to parse
     * @return Expression parsed from the string
     * @throws UnableToParseException if the string doesn't match the Expression grammar
     */
    public static Composition parseFile(final File file) throws UnableToParseException {
        // parse the example into a parse tree
        throw new UnsupportedOperationException("not implemented yet");
    }
    

    private static Composition makeComposition(ParseTree<MusicGrammar> compositionTree) {
        ParseTree<MusicGrammar> headerTree = compositionTree.childrenByName(MusicGrammar.HEADER).get(0);
        double tempo = DEFAULT_TEMPO;
        String title;
        String composer = DEFAULT_COMPOSER;
        double length = DEFAULT_LENGTH;
        double meter = DEFAULT_METER;
        Key key;
        int trackNumber;
        List<String> voices = new ArrayList<>();
        
        //Parse Header info
        for(ParseTree<MusicGrammar> field: headerTree.children()) {
            if(field.name() == MusicGrammar.COMPOSER) {
                composer = field.text();
            }
            else if(field.name() == MusicGrammar.TITLE) {
                title = field.text();
            }
            else if(field.name() == MusicGrammar.LENGTH) {
                length = (double) Integer.parseInt(field.childrenByName(MusicGrammar.NUMERATOR).get(0).text())/
                        Integer.parseInt(field.childrenByName(MusicGrammar.DENOMINATOR).get(0).text());
            }
            else if(field.name() == MusicGrammar.METER) {
                meter = (double) Integer.parseInt(field.childrenByName(MusicGrammar.NUMERATOR).get(0).text())/
                        Integer.parseInt(field.childrenByName(MusicGrammar.DENOMINATOR).get(0).text());
            }
            else if(field.name() == MusicGrammar.TRACKNUMBER) {
                trackNumber = Integer.parseInt(field.childrenByName(MusicGrammar.NUMBER).get(0).text());
            }
            else if(field.name() == MusicGrammar.TEMPO) {
                tempo = Integer.parseInt(field.childrenByName(MusicGrammar.NUMBER).get(0).text());
            }
            else if(field.name() == MusicGrammar.KEY) {
                key = Key.valueOf(field.text());
            }
            else if(field.name() == MusicGrammar.VOICENAME) {
                voices.add(field.text());
            }
            else {
                throw new AssertionError("should never get here");
            }
        }
        
        //Parse info from all voices in the piece
        for(ParseTree<MusicGrammar> voice: compositionTree.children()) {
            //skip the header info
            if(voice.name() == MusicGrammar.HEADER) {
                continue;
            }
            //Voice is composed of a Line of Music and a Line of Lyrics
            //ParseLyrics into single note lyrics
            List<String> lyricList = parseLyrics(compositionTree.childrenByName(MusicGrammar.LYRIC));
            int lyricIndex = 0;
            
            //Now create the associated line of music
            List<Music> lineOfMusic = new ArrayList<>();
            
            for(ParseTree<MusicGrammar> measure:voice.children()) {
                for(ParseTree<MusicGrammar> primitive: measure.children()) {
                    //Case of Chord
                    if(primitive.name() == MusicGrammar.CHORD) {
                        List<Music> chordNotes = new ArrayList<>();
                        for(ParseTree<MusicGrammar> note: primitive.childrenByName(MusicGrammar.NOTE)) {
                            chordNotes.add(parseNote(note, 4*length, -1));
                        }
                        lineOfMusic.add(new Chord(chordNotes, lyricIndex));
                        lyricIndex++;
                    }
                    
                    //Case of Tuple
                    else if(primitive.name() == MusicGrammar.TUPLE) {
                        double tupleSize = Integer.parseInt(primitive.childrenByName(MusicGrammar.NUMBER)
                                .get(0).text());
                        assert primitive.childrenByName(MusicGrammar.NOTE).size() == tupleSize;
                        for(ParseTree<MusicGrammar> note: primitive.childrenByName(MusicGrammar.NOTE)) {
                            lineOfMusic.add(parseNote(note, 4*(tupleSize-1)/tupleSize, lyricIndex));
                            lyricIndex++;
                        }
                        
                    }
                    //Case of Note
                    else if(primitive.name() == MusicGrammar.NOTE){ 
                        parseNote(primitive, 4*length, lyricIndex);
                        lyricIndex ++;
                    }
                }
                
            }
        }
    }

    private static Music parseNote(ParseTree<MusicGrammar> note, double multiplier, int lyricIndex) {
        String value = note.childrenByName(MusicGrammar.LETTER).get(0).text();
        Pitch notePitch = new Pitch(value.toUpperCase().charAt(0));
        //case of one octave up due to being lowercase
        if(value.toUpperCase() != value) {
            notePitch = notePitch.transpose(Pitch.OCTAVE);
        }
        
        for(ParseTree<MusicGrammar> accidental: note.childrenByName(MusicGrammar.ACCIDENTAL)) {
            if(accidental.name() == MusicGrammar.FLAT) {
                notePitch = notePitch.transpose(-1);
            }
            else if(accidental.name() == MusicGrammar.SHARP) {
                notePitch = notePitch.transpose(1);
            }
        }
        
        for(ParseTree<MusicGrammar> octaveups: note.childrenByName(MusicGrammar.OCTAVEUP)) {
            notePitch = notePitch.transpose(Pitch.OCTAVE);
        }
        
        for(ParseTree<MusicGrammar> octaveDown: note.childrenByName(MusicGrammar.OCTAVEDOWN)) {
            notePitch = notePitch.transpose(-Pitch.OCTAVE);
        }
        
        double numerator = 1;
        double denominator = 1;
        
        
        return new Note(notePitch, multiplier*lyricIndex)
        
    }

    private static List<String> parseLyrics(List<ParseTree<MusicGrammar>> lyricList) {
        if(lyricList.size() ==0) {
            return Collections.emptyList();
        }
        
        List<String> lyricSyllables = new ArrayList<>();
        for(ParseTree<MusicGrammar> syllableNote: lyricList.get(0).children()) {
            //TODO fill in arrayList
        }
    }
}

