/* Copyright (c) 2017-2018 MIT 6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package karaoke.parser;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import edu.mit.eecs.parserlib.ParseTree;
import edu.mit.eecs.parserlib.Parser;
import edu.mit.eecs.parserlib.UnableToParseException;
import karaoke.Composition.Accidental;
import karaoke.Chord;
import karaoke.Composition;
import karaoke.Composition.Key;
import karaoke.Concat;
import karaoke.Music;
import karaoke.Note;
import karaoke.Repeat;
import karaoke.Rest;
import karaoke.Tuplet;
import karaoke.Voice;
import karaoke.sound.Instrument;
import karaoke.sound.Pitch;

public class MusicParser {
    private static final double DEFAULT_DENOMINATOR = 2;
    private static final Instrument DEFAULT_INSTRUMENT = Instrument.PIANO;
    private static final double DEFAULT_NOTE_LENGTH = 1.0/4;
    private static final double DEFAULT_TEMPO = 100;
    private static final Map<Key, Map<String, Accidental>> KEY_SIGNATURES = new HashMap<>();
    static {
    	Map<String, Accidental> c = new HashMap<>();
    	c.put("A", Accidental.NATURAL);
    	c.put("B", Accidental.NATURAL);
    	c.put("C", Accidental.NATURAL);
    	c.put("D", Accidental.NATURAL);
    	c.put("E", Accidental.NATURAL);
    	c.put("F", Accidental.NATURAL);
    	c.put("G", Accidental.NATURAL);    	
    	
        KEY_SIGNATURES.put(Key.C, c);
        KEY_SIGNATURES.put(Key.Am, c);
        
    	Map<String, Accidental> g = new HashMap<>();
    	g.put("A", Accidental.NATURAL);
    	g.put("B", Accidental.NATURAL);
    	g.put("C", Accidental.NATURAL);
    	g.put("D", Accidental.NATURAL);
    	g.put("E", Accidental.NATURAL);
    	g.put("F", Accidental.SHARP);
    	g.put("G", Accidental.NATURAL);    	
    	
        KEY_SIGNATURES.put(Key.G, g);
        KEY_SIGNATURES.put(Key.Em, g);
        
    	Map<String, Accidental> d = new HashMap<>();
    	d.put("A", Accidental.NATURAL);
    	d.put("B", Accidental.NATURAL);
    	d.put("C", Accidental.SHARP);
    	d.put("D", Accidental.NATURAL);
    	d.put("E", Accidental.NATURAL);
    	d.put("F", Accidental.SHARP);
    	d.put("G", Accidental.NATURAL);    	
    	
        KEY_SIGNATURES.put(Key.D, d);
        KEY_SIGNATURES.put(Key.Bm, d);
        
    	Map<String, Accidental> a = new HashMap<>();
    	a.put("A", Accidental.NATURAL);
    	a.put("B", Accidental.NATURAL);
    	a.put("C", Accidental.SHARP);
    	a.put("D", Accidental.NATURAL);
    	a.put("E", Accidental.NATURAL);
    	a.put("F", Accidental.SHARP);
    	a.put("G", Accidental.SHARP);    	
    	
        KEY_SIGNATURES.put(Key.A, a);
        KEY_SIGNATURES.put(Key.Fsharpm, a);
        
    	Map<String, Accidental> e = new HashMap<>();
    	e.put("A", Accidental.NATURAL);
    	e.put("B", Accidental.NATURAL);
    	e.put("C", Accidental.SHARP);
    	e.put("D", Accidental.SHARP);
    	e.put("E", Accidental.NATURAL);
    	e.put("F", Accidental.SHARP);
    	e.put("G", Accidental.SHARP);    	
    	
        KEY_SIGNATURES.put(Key.E, e);
        KEY_SIGNATURES.put(Key.Csharpm, e);
        
    	Map<String, Accidental> b = new HashMap<>();
    	b.put("A", Accidental.SHARP);
    	b.put("B", Accidental.NATURAL);
    	b.put("C", Accidental.SHARP);
    	b.put("D", Accidental.SHARP);
    	b.put("E", Accidental.NATURAL);
    	b.put("F", Accidental.SHARP);
    	b.put("G", Accidental.SHARP);    	
    	
        KEY_SIGNATURES.put(Key.B, b);
        KEY_SIGNATURES.put(Key.Gsharpm, b);
        
    	Map<String, Accidental> cFlat = new HashMap<>();
    	cFlat.put("A", Accidental.FLAT);
    	cFlat.put("B", Accidental.FLAT);
    	cFlat.put("C", Accidental.FLAT);
    	cFlat.put("D", Accidental.FLAT);
    	cFlat.put("E", Accidental.FLAT);
    	cFlat.put("F", Accidental.FLAT);
    	cFlat.put("G", Accidental.FLAT);    	
    	
        KEY_SIGNATURES.put(Key.Cflat, cFlat);
        KEY_SIGNATURES.put(Key.Aflatm, cFlat);
        
    	Map<String, Accidental> fSharp = new HashMap<>();
    	fSharp.put("A", Accidental.SHARP);
    	fSharp.put("B", Accidental.NATURAL);
    	fSharp.put("C", Accidental.SHARP);
    	fSharp.put("D", Accidental.SHARP);
    	fSharp.put("E", Accidental.SHARP);
    	fSharp.put("F", Accidental.SHARP);
    	fSharp.put("G", Accidental.SHARP);    	
    	
        KEY_SIGNATURES.put(Key.Fsharp, fSharp);
        KEY_SIGNATURES.put(Key.Dsharpm, fSharp);
        
    	Map<String, Accidental> gFlat = new HashMap<>();
    	gFlat.put("A", Accidental.FLAT);
    	gFlat.put("B", Accidental.FLAT);
    	gFlat.put("C", Accidental.FLAT);
    	gFlat.put("D", Accidental.FLAT);
    	gFlat.put("E", Accidental.FLAT);
    	gFlat.put("F", Accidental.NATURAL);
    	gFlat.put("G", Accidental.FLAT);    	
    	
        KEY_SIGNATURES.put(Key.Gflat, gFlat);
        KEY_SIGNATURES.put(Key.Eflatm, gFlat);
        
    	Map<String, Accidental> cSharp = new HashMap<>();
    	cSharp.put("A", Accidental.SHARP);
    	cSharp.put("B", Accidental.SHARP);
    	cSharp.put("C", Accidental.SHARP);
    	cSharp.put("D", Accidental.SHARP);
    	cSharp.put("E", Accidental.SHARP);
    	cSharp.put("F", Accidental.SHARP);
    	cSharp.put("G", Accidental.SHARP);    	
    	
        KEY_SIGNATURES.put(Key.Csharp, cSharp);
        KEY_SIGNATURES.put(Key.Asharpm, cSharp);
        
    	Map<String, Accidental> dFlat = new HashMap<>();
    	dFlat.put("A", Accidental.FLAT);
    	dFlat.put("B", Accidental.FLAT);
    	dFlat.put("C", Accidental.NATURAL);
    	dFlat.put("D", Accidental.FLAT);
    	dFlat.put("E", Accidental.FLAT);
    	dFlat.put("F", Accidental.NATURAL);
    	dFlat.put("G", Accidental.FLAT);    	
    	
        KEY_SIGNATURES.put(Key.Dflat, dFlat);
        KEY_SIGNATURES.put(Key.Bflatm, dFlat);
        
    	Map<String, Accidental> aFlat = new HashMap<>();
    	aFlat.put("A", Accidental.FLAT);
    	aFlat.put("B", Accidental.FLAT);
    	aFlat.put("C", Accidental.NATURAL);
    	aFlat.put("D", Accidental.FLAT);
    	aFlat.put("E", Accidental.FLAT);
    	aFlat.put("F", Accidental.NATURAL);
    	aFlat.put("G", Accidental.NATURAL);    	
    	
        KEY_SIGNATURES.put(Key.Aflat, aFlat);
        KEY_SIGNATURES.put(Key.Fm, aFlat);
        
    	Map<String, Accidental> eFlat = new HashMap<>();
    	eFlat.put("A", Accidental.FLAT);
    	eFlat.put("B", Accidental.FLAT);
    	eFlat.put("C", Accidental.NATURAL);
    	eFlat.put("D", Accidental.NATURAL);
    	eFlat.put("E", Accidental.FLAT);
    	eFlat.put("F", Accidental.NATURAL);
    	eFlat.put("G", Accidental.NATURAL);    	
    	
        KEY_SIGNATURES.put(Key.Eflat, eFlat);
        KEY_SIGNATURES.put(Key.Cm, eFlat);
        
    	Map<String, Accidental> bFlat = new HashMap<>();
    	eFlat.put("A", Accidental.NATURAL);
    	eFlat.put("B", Accidental.FLAT);
    	eFlat.put("C", Accidental.NATURAL);
    	eFlat.put("D", Accidental.NATURAL);
    	eFlat.put("E", Accidental.FLAT);
    	eFlat.put("F", Accidental.NATURAL);
    	eFlat.put("G", Accidental.NATURAL);    	
    	
        KEY_SIGNATURES.put(Key.Bflat, bFlat);
        KEY_SIGNATURES.put(Key.Gm, bFlat);
        
    	Map<String, Accidental> f = new HashMap<>();
    	f.put("A", Accidental.NATURAL);
    	f.put("B", Accidental.FLAT);
    	f.put("C", Accidental.NATURAL);
    	f.put("D", Accidental.NATURAL);
    	f.put("E", Accidental.NATURAL);
    	f.put("F", Accidental.NATURAL);
    	f.put("G", Accidental.NATURAL);    	
    	
        KEY_SIGNATURES.put(Key.F, f);
        KEY_SIGNATURES.put(Key.Dm, f);
    }
    
    /**
     * Main method. Parses and then reprints an example expression.
     * 
     * @param args command line arguments, not used
     * @throws UnableToParseException if example expression can't be parsed
     */
    public static void main(final String[] args) throws UnableToParseException {
        //final String input = "";
        final File input = new File("sample-abc/piece3.abc");
        //System.out.println(input);
        (new MusicParser()).parseFile(input);
        //System.out.println(expression);
    }
    
    // the nonterminals of the grammar
    private static enum MusicGrammar {
        COMPOSITION, HEADER, TRACKNUMBER, COMPOSER, METER, LENGTH, TEMPO, HYPHEN, HYPHENS,SPACES, FLATKEY,
        VOICENAME, KEY, VOICE, MUSICLINE, MEASURE, NOTE, OCTAVEUP, OCTAVEDOWN, TILDA, STAR, MINOR, SHARPKEY, 
        NOTEDENOMINATOR, ACCIDENTAL, SHARP, FLAT, LYRIC, SYLLABLENOTE, NEWMEASURE, BACKSLASHHYPHEN,
        SYLLABLE, LETTER, COMMENT, NUMBER, WHITESPACE, WHITESPACEANDCOMMENT, TITLE, HOLD, SPACE, 
        CHORD, TUPLE, DENOMINATOR, NUMERATOR, REPEAT, DOUBLEFLAT, DOUBLESHARP, NATURAL, REST, ENDING
    }
    
    private static enum BaseGrammar{
    	ABCTUNE, VOICE, VOICENAME, LYRICS, MUSICLINE, NEWLINE, ANYTHING, ABCHEADER, FIELDNUMBER, FIELDTITLE,
    	OTHERFIELDS, FIELDCOMPOSER, FIELDDEFAULTLENGTH, FIELDMETER, FIELDTEMPO, FIELDVOICE, FIELDKEY,
    	COMMENT, ENDOFLINE, SPACEORTAB,
    	ABCBODY
    }
    
    private static Parser<BaseGrammar> baseParser = makeBaseParser();
    
    /**
     * Compile the grammar into a parser.
     * 
     * @return parser for the grammar
     * @throws RuntimeException if grammar file can't be read or has syntax errors
     */
    private static Parser<BaseGrammar> makeBaseParser() {
        try {
            // read the grammar as a file, relative to the project root.
            final File grammarFile = new File("src/karaoke/parser/VoiceGrammar.g");
            return Parser.compile(grammarFile, BaseGrammar.ABCTUNE);

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
     * @return Composition parsed from the string
     * @throws UnableToParseException if the string doesn't match the Abc grammar
     */
    public Composition parse(final String string) throws UnableToParseException {
    	System.out.println(string);
    	final Map<String, List<String>> voiceNoteDict = new HashMap<>();
    	final Map<String, List<String>> voiceLyricDict = new HashMap<>();
    	final ParseTree<BaseGrammar> abcBodyTree = baseParser.parse(string).childrenByName(BaseGrammar.ABCBODY).get(0);
    	final ParseTree<BaseGrammar> abcHeaderTree = baseParser.parse(string).childrenByName(BaseGrammar.ABCHEADER).get(0);
    	for(ParseTree<BaseGrammar> voice: abcBodyTree.children()) {
    		String voiceName = voice.childrenByName(BaseGrammar.VOICENAME).get(0).text();
    		if (voiceName.isEmpty()) {
    			voiceName = "empty";
    		}
    		if (voiceNoteDict.containsKey(voiceName)){
    			voiceNoteDict.get(voiceName).add(voice.childrenByName(BaseGrammar.MUSICLINE).get(0).childrenByName(BaseGrammar.ANYTHING).get(0).text());
    		}
    		else {
    			voiceNoteDict.put(voiceName, new ArrayList<>());
    			voiceNoteDict.get(voiceName).add(voice.childrenByName(BaseGrammar.MUSICLINE).get(0).childrenByName(BaseGrammar.ANYTHING).get(0).text());
    		}
    		if (voice.children().size() > 2) {
        		if (voiceLyricDict.containsKey(voiceName)){
        			voiceLyricDict.get(voiceName).add(voice.childrenByName(BaseGrammar.LYRICS).get(0).childrenByName(BaseGrammar.ANYTHING).get(0).text());
        		}
        		else {
        			voiceLyricDict.put(voiceName, new ArrayList<>());
        			voiceLyricDict.get(voiceName).add(voice.childrenByName(BaseGrammar.LYRICS).get(0).childrenByName(BaseGrammar.ANYTHING).get(0).text());
        		}
    		}
    	}
    	
    	String reconstructedString = "";
    	for(ParseTree<BaseGrammar> field: abcHeaderTree.children()) {
    		reconstructedString += field.text();
    	}
    	for(String voice: voiceNoteDict.keySet()) {
    		reconstructedString = reconstructedString + String.join(" ", voiceNoteDict.get(voice)) + "\n";
    		if (voiceLyricDict.containsKey(voice)) {
    			reconstructedString = reconstructedString + "w:" + String.join(" ", voiceLyricDict.get(voice)) + "\n";
    		}
    	}
        // parse the example into a parse tree
        final ParseTree<MusicGrammar> parseTree = parser.parse(reconstructedString);

        // display the parse tree in various ways, for debugging only
        //System.out.println("parse tree " + parseTree);
        //Visualizer.showInBrowser(parseTree);
        
        final Composition composition = makeCompositionHeader(parseTree);
        //System.out.println("AST " + expression);
        Map<String, Voice> voiceMap = new HashMap<>();
        for(int voiceNumber = 1; voiceNumber < parseTree.children().size(); voiceNumber++) {
            ParseTree<MusicGrammar> voice = parseTree.children().get(voiceNumber);
            List<String> lyricList = parseLyrics(voice.childrenByName(MusicGrammar.LYRIC));
            //System.out.println(lyricList);
            String voiceName = "";
            if(voice.childrenByName(MusicGrammar.VOICENAME).size() > 0) {
                voiceName = voice.childrenByName(MusicGrammar.VOICENAME)
                        .get(0).text();
            }
            //use parsetree to make a line of music aligned with voices
            int indexModifier = 0;
            if(voiceMap.containsKey(voiceName)) {
                indexModifier = voiceMap.get(voiceName).lyricLength();
            }
            NoteEnvironment environment = new NoteEnvironment(composition, lyricList, indexModifier);

            Music line = makeMusicAST(voice.childrenByName(MusicGrammar.MUSICLINE)
                    .get(0), environment);
            //System.out.println(line);
            Voice newVoice = new Voice(line, lyricList, voiceName);
            if(voiceMap.containsKey(voiceName)) {
                voiceMap.put(voiceName, voiceMap.get(voiceName).join(newVoice));
            }
            else {
                voiceMap.put(voiceName, newVoice);
            }
        }
        
        composition.setVoices(voiceMap);
        
        return composition;
    }
    

    /**
     * Parse a File into an Composition.
     * @param file file to parse
     * @return Composition parsed from the string
     * @throws UnableToParseException if the string doesn't match the ABC grammar 
     */
    public Composition parseFile(final File file) throws UnableToParseException {
        // parse the example into a parse tree
        try {
            String input = "";
            Scanner inFile = new Scanner(file);
            while(inFile.hasNextLine()) {
                input+=inFile.nextLine()+"\n";
            }
            return this.parse(input);
        }
        catch(IOException e) {
            throw new UnableToParseException("File not found");
        }
    }
    

    private static Composition makeCompositionHeader(ParseTree<MusicGrammar> compositionTree) {
        ParseTree<MusicGrammar> headerTree = compositionTree.childrenByName(MusicGrammar.HEADER).get(0);
        Composition composition = new Composition();
        //Parse Header info
        for(ParseTree<MusicGrammar> field: headerTree.children()) {
            if(field.name() == MusicGrammar.COMPOSER) {
                composition.setComposer(field.text());
            }
            else if(field.name() == MusicGrammar.TITLE) {
                composition.setTitle(field.text());
            }
            else if(field.name() == MusicGrammar.LENGTH) {
                composition.setLength((double) Integer.parseInt(field.childrenByName(MusicGrammar.NUMERATOR).get(0).text())/
                        Integer.parseInt(field.childrenByName(MusicGrammar.DENOMINATOR).get(0).text()));
            }
            else if(field.name() == MusicGrammar.METER) {
                composition.setMeter((double) Integer.parseInt(field.childrenByName(MusicGrammar.NUMERATOR).get(0).text())/
                        Integer.parseInt(field.childrenByName(MusicGrammar.DENOMINATOR).get(0).text()));
            }
            else if(field.name() == MusicGrammar.TRACKNUMBER) {
                composition.setTrackNumber(Integer.parseInt(field.childrenByName(MusicGrammar.NUMBER)
                        .get(0).text()));
            }
            else if(field.name() == MusicGrammar.TEMPO) {
                composition.setTempo(Integer.parseInt(field.childrenByName(MusicGrammar.NUMBER).get(0).text()));
            }
            else if(field.name() == MusicGrammar.KEY) {
            	String key = field.childrenByName(MusicGrammar.LETTER).get(0).text();
            	if(field.childrenByName(MusicGrammar.FLATKEY).size() != 0) {
            		key+="flat";
            	}
            	else if(field.childrenByName(MusicGrammar.SHARPKEY).size() != 0) {
            		key+="sharp";
            	}
            	
            	if(field.childrenByName(MusicGrammar.MINOR).size() != 0) {
            		key+="m";
            	}
                composition.setKey(Key.valueOf(key));
            }
            else if(field.name() == MusicGrammar.VOICENAME) {
                continue;
            }
            else {
                throw new AssertionError("should never get here");
            }
        }
        return composition;
    }
       
    private static Music makeMusicAST(ParseTree<MusicGrammar> musicTree, NoteEnvironment environment) {
        if(musicTree.name() == MusicGrammar.MUSICLINE) // expression ::= resize ('|' resize)*;
            {
                Music base = new Rest(0);
                for(ParseTree<MusicGrammar> measure:musicTree.children()) {
                    base = new Concat(base, makeMusicAST(measure, environment));
                }        
                return base;
            }
        else if(musicTree.name()== MusicGrammar.REPEAT)
            {
                Music measures = new Rest(0);
                //Don't let the syllable counter increment when parsing notes in chord
                for(ParseTree<MusicGrammar> measure:musicTree.childrenByName(MusicGrammar.MEASURE)) {
                    measures = new Concat(measures, makeMusicAST(measure, environment));
                } 
                
                ArrayList<Music> endings = new ArrayList<>();
                for(ParseTree<MusicGrammar> ending:musicTree.childrenByName(MusicGrammar.ENDING)) {
                    Music endingMeasures = new Rest(0);
                    //Don't let the syllable counter increment when parsing notes in chord
                    for(ParseTree<MusicGrammar> endingMeasure: ending.childrenByName(MusicGrammar.MEASURE)) {
                        endingMeasures = new Concat(endingMeasures, makeMusicAST(endingMeasure, environment));
                    }
                    endings.add(endingMeasures);
                } 
                Music repeat = new Repeat(measures, endings);
                return repeat;
            }
        else if(musicTree.name() == MusicGrammar.MEASURE)
            {
                environment.newMeasure();
                Music base = new Rest(0);
                for(ParseTree<MusicGrammar> primitive: musicTree.children()) {
                    base = new Concat(base, makeMusicAST(primitive, environment));
                }
                environment.resetAccidentals();
                return base;
            }
        else if(musicTree.name() == MusicGrammar.TUPLE)
            {
                List<Music> notes = new ArrayList<>();
                double tupleSize = Integer.parseInt(musicTree.childrenByName(MusicGrammar.NUMBER)
                        .get(0).text());
                if(tupleSize != 2) {
                    environment.scaleLength((tupleSize-1)/tupleSize);
                }
                else {
                    environment.scaleLength((tupleSize+1)/tupleSize);
                }
                for(ParseTree<MusicGrammar> primitive: musicTree.children()) {
                    if(primitive.name() == MusicGrammar.NUMBER){
                        continue;
                    }
                    notes.add(makeMusicAST(primitive, environment));
                    environment.incrementSyllable();
                }
                //reset length to normal size
                environment.resetLength();
                
                return new Tuplet((int) tupleSize, notes);
            }
        else if(musicTree.name() == MusicGrammar.CHORD)
            {
                List<Music> notes = new ArrayList<>();
                //Don't let the syllable counter increment when parsing notes in chord
                environment.lockSyllableCounter();
                for(ParseTree<MusicGrammar> primitive: musicTree.children()) {
                    notes.add(makeMusicAST(primitive, environment));
                }
                Music chord = new Chord(notes, environment.lyricIndex());
                environment.lockSyllableCounter();
                environment.incrementSyllable();
                return chord;
            }
        else if(musicTree.name()== MusicGrammar.NOTE)
            {
                Music note = parseNote(musicTree, environment);
                environment.incrementSyllable();
                return note;
            }
        
        else if(musicTree.name()== MusicGrammar.REST)
        {
            double numerator = 1;
            double denominator = 1;
            
            if(musicTree.childrenByName(MusicGrammar.NUMERATOR).size() > 0) {
                numerator = Integer.parseInt(musicTree.childrenByName(MusicGrammar.NUMERATOR)
                        .get(0).text());
            }
            
            if(musicTree.childrenByName(MusicGrammar.NOTEDENOMINATOR).size() > 0) {
                ParseTree<MusicGrammar> noteDenominator = musicTree.childrenByName(MusicGrammar.NOTEDENOMINATOR)
                        .get(0);
                if(noteDenominator.childrenByName(MusicGrammar.DENOMINATOR).size() > 0) {
                    denominator= Integer.parseInt(noteDenominator.childrenByName(MusicGrammar.DENOMINATOR)
                            .get(0).text());
                }
                else {
                    denominator = DEFAULT_DENOMINATOR;
                }
            }
            return new Rest(environment.defaultDuration()*(numerator/denominator));
        }
        
        
         
        else {
            throw new AssertionError("Invalid Music: "+musicTree.name());
        }
    }
    
    private static Music parseNote(ParseTree<MusicGrammar> note, NoteEnvironment environment) {
        String value = note.childrenByName(MusicGrammar.LETTER).get(0).text();
        Pitch notePitch = new Pitch(value.toUpperCase().charAt(0));
        //case of one octave up due to being lowercase
        String octaveModifier = "";
        if(value.toUpperCase() != value) {
            notePitch = notePitch.transpose(Pitch.OCTAVE);
            octaveModifier = "\'";
            value = value.toUpperCase();
        }
        
        for(ParseTree<MusicGrammar> octaveups: note.childrenByName(MusicGrammar.OCTAVEUP)) {
            notePitch = notePitch.transpose(Pitch.OCTAVE);
            octaveModifier+="\'";
        }
        
        for(ParseTree<MusicGrammar> octaveDown: note.childrenByName(MusicGrammar.OCTAVEDOWN)) {
            notePitch = notePitch.transpose(-Pitch.OCTAVE);
            octaveModifier+=",";
        }
        
        if(note.childrenByName(MusicGrammar.ACCIDENTAL).size() > 0) {
            ParseTree<MusicGrammar> accidental = note.childrenByName(MusicGrammar.ACCIDENTAL).get(0);
            MusicGrammar name = accidental.children().get(0).name();
            if(name == MusicGrammar.FLAT) { 
                notePitch = notePitch.transpose(Accidental.FLAT.getTranspose());
                environment.setAccidental(value+octaveModifier, Accidental.FLAT);
            }
            else if(name == MusicGrammar.DOUBLEFLAT) {
                notePitch = notePitch.transpose(Accidental.DOUBLE_FLAT.getTranspose());
                environment.setAccidental(value+octaveModifier, Accidental.DOUBLE_FLAT);
            }
            else if(name == MusicGrammar.SHARP) {
                notePitch = notePitch.transpose(Accidental.SHARP.getTranspose());
                environment.setAccidental(value+octaveModifier, Accidental.SHARP);
            }
            else if(name == MusicGrammar.DOUBLESHARP) { 
                notePitch = notePitch.transpose(Accidental.DOUBLE_SHARP.getTranspose());
                environment.setAccidental(value+octaveModifier, Accidental.DOUBLE_SHARP);
            }
            else if(name == MusicGrammar.NATURAL) {
                environment.setAccidental(value+octaveModifier, Accidental.NATURAL);
            }
            else {
                throw new AssertionError("should never get here: "+accidental.children().get(0).name().toString());
            }
        }
        
        else if(environment.accidentalsForMeasure().containsKey(value+octaveModifier)) {
            Accidental modifier = environment.accidentalsForMeasure().get(value+octaveModifier);
            notePitch = notePitch.transpose(modifier.getTranspose());
        }
        
        else if(environment.accidentalsForKey().containsKey(value+octaveModifier)) {
            Accidental modifier = environment.accidentalsForKey().get(value);
            notePitch = notePitch.transpose(modifier.getTranspose());
        }
        
            
        //Parse time info
        double numerator = 1;
        double denominator = 1;
        
        if(note.childrenByName(MusicGrammar.NUMERATOR).size() > 0) {
            numerator = Integer.parseInt(note.childrenByName(MusicGrammar.NUMERATOR)
                    .get(0).text());
        }
        
        if(note.childrenByName(MusicGrammar.NOTEDENOMINATOR).size() > 0) {
            ParseTree<MusicGrammar> noteDenominator = note.childrenByName(MusicGrammar.NOTEDENOMINATOR)
                    .get(0);
            if(noteDenominator.childrenByName(MusicGrammar.DENOMINATOR).size() > 0) {
                denominator= Integer.parseInt(noteDenominator.childrenByName(MusicGrammar.DENOMINATOR)
                        .get(0).text());
            }
            else {
                denominator = DEFAULT_DENOMINATOR;
            }
        }
               
        return new Note(environment.defaultDuration()*(numerator/denominator), 
                notePitch, DEFAULT_INSTRUMENT, environment.lyricIndex());
        
    }

    private static List<String> parseLyrics(List<ParseTree<MusicGrammar>> lyricList) {
        if(lyricList.size() ==0) {
            return Collections.emptyList();
        }
        
        List<String> lyricSyllables = new ArrayList<>();
        for(ParseTree<MusicGrammar> syllableNote: lyricList.get(0).children()) {
            String syllable ="";
            //System.out.println(syllableNote.name());
            if(syllableNote.name() == MusicGrammar.SYLLABLENOTE) {
                for(ParseTree<MusicGrammar> child: syllableNote.children()) {
                    if(child.text().equals("~")) {
                        syllable += " ";
                    }
                    else if(child.text().equals("\\-")) {
                        syllable += "-";
                    }
                    else {
                        syllable+=child.text();
                    }
                }
                lyricSyllables.add(syllable);
            }   
            else if(syllableNote.name() == MusicGrammar.HYPHENS){
                for(int i = 1; i < syllableNote.children().size(); i++) {
                    lyricSyllables.add("");
                }
            }
            else if(syllableNote.name() == MusicGrammar.SPACES) {
            	String replace = lyricSyllables.get(lyricSyllables.size()-1)+" ";
                lyricSyllables.remove(lyricSyllables.size()-1);
                lyricSyllables.add(replace);
            }
            else {
                lyricSyllables.add(syllableNote.text());
            }
            
            
        }
        return lyricSyllables;
    }
    
    
    private class NoteEnvironment{
        private final double defaultDuration;
        private double duration;
        private int lyricIndex;
        private final List<String> lyrics;
        private boolean newMeasure;
        private Map<String, Accidental> keyMap;
        private Map<String, Accidental> measureMap;
        private boolean lock;
        private final int indexModifier;
        
        private NoteEnvironment(Composition composition, List<String> lyricList, int indexModifier) {
            keyMap = new HashMap<>(KEY_SIGNATURES.get(composition.key()));
            measureMap = new HashMap<>();
            lyricIndex = 0;
            lyrics = lyricList;
            newMeasure = true;
            defaultDuration = (1/DEFAULT_NOTE_LENGTH)*DEFAULT_TEMPO*composition.length()*(1/composition.tempo());
            duration = defaultDuration;
            lock = false;
            this.indexModifier = indexModifier;
        }
        
        
        private void lockSyllableCounter() {
            lock = true;
        }
        
        private void unlockSylableCounter() {
            lock = false;
        }

        private void newMeasure() {
            newMeasure = true;
        }
        

        private void scaleLength(double scaleFactor) {
            this.duration*=scaleFactor;
        }
        
        private void resetLength() {
            this.duration = defaultDuration;
        }
        
        
        private void incrementSyllable() {
            if(lyricIndex >= lyrics.size()) {
                return;
            }
            if(!lock) {
                if(!lyrics.get(lyricIndex).trim().equals("|") || newMeasure) {
                    if(lyrics.get(lyricIndex).trim().equals("|")) {
                        lyrics.remove(lyricIndex);
                        lyricIndex+=1;
                    }
                    else {
                        lyricIndex+=1;
                    }
                    newMeasure = false;
                }
                //otherwise do nothing
            }
        }
        
        private void setAccidental(String key, Accidental accidental) {
            measureMap.put(key, accidental);
        }
        
        private void resetAccidentals() {
            measureMap = new HashMap<>();
        }
        
        private double defaultDuration() {
            return this.duration;
        }
        
        private Map<String, Accidental> accidentalsForKey(){
            return new HashMap<>(this.keyMap);
        }
        
        private Map<String, Accidental> accidentalsForMeasure(){
            return new HashMap<>(this.measureMap);
        }
        
        private int lyricIndex() {
            return this.lyricIndex+this.indexModifier;
        }
        
    }
}

