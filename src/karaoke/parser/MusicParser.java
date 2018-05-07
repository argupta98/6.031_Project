/* Copyright (c) 2017-2018 MIT 6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package karaoke.parser;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import edu.mit.eecs.parserlib.ParseTree;
import edu.mit.eecs.parserlib.Parser;
import edu.mit.eecs.parserlib.UnableToParseException;
import edu.mit.eecs.parserlib.Visualizer;
import karaoke.Composition.Accidental;
import karaoke.Chord;
import karaoke.Composition;
import karaoke.Composition.Key;
import karaoke.Concat;
import karaoke.Music;
import karaoke.Note;
import karaoke.Rest;
import karaoke.Tuplet;
import karaoke.Voice;
import karaoke.sound.Instrument;
import karaoke.sound.Pitch;

public class MusicParser {
    private static final double DEFAULT_DENOMINATOR = 2;
    private static final Instrument DEFAULT_INSTRUMENT = Instrument.PIANO;
    private static final Map<Key, Map<String, Accidental>> KEY_SIGNATURES = new HashMap<>();
    static {
        //TODO
        KEY_SIGNATURES.put(Key.A, new HashMap<>());
        KEY_SIGNATURES.put(Key.B, new HashMap<>());
        KEY_SIGNATURES.put(Key.C, new HashMap<>());
        KEY_SIGNATURES.put(Key.D, new HashMap<>());
        KEY_SIGNATURES.put(Key.E, new HashMap<>());
        KEY_SIGNATURES.put(Key.F, new HashMap<>());
        KEY_SIGNATURES.put(Key.G, new HashMap<>());
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
                "^^A _b'' c,3/4 D4 E/4 F/|\n"+
                "w: I a-m so happy~ness!\n"+
                "V: voice2\n"+
                "[AB] [A/2B/] (3ABC (2a/2b/2 c,3/4 D4 | E/4 F/|\n"+
                "w: I a-m so happy~ness!\n";
        
        System.out.println(input);
        (new MusicParser()).parse(input);
        //System.out.println(expression);
    }
    
    // the nonterminals of the grammar
    private static enum MusicGrammar {
        COMPOSITION, HEADER, TRACKNUMBER, COMPOSER, METER, LENGTH, TEMPO,
        VOICENAME, KEY, VOICE, MUSICLINE, MEASURE, NOTE, OCTAVEUP, OCTAVEDOWN,
        NOTEDENOMINATOR, ACCIDENTAL, SHARP, FLAT, LYRIC, SYLLABLENOTE, 
        SYLLABLE, LETTER, COMMENT, NUMBER, END, WHITESPACE, WHITESPACEANDCOMMENT, TITLE,
        CHORD, TUPLE, DENOMINATOR, NUMERATOR, REPEAT, DOUBLEFLAT, DOUBLESHARP, NATURAL
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
    public Composition parse(final String string) throws UnableToParseException {
        // parse the example into a parse tree
        final ParseTree<MusicGrammar> parseTree = parser.parse(string);

        // display the parse tree in various ways, for debugging only
        //System.out.println("parse tree " + parseTree);
        //Visualizer.showInBrowser(parseTree);
        
        final Composition composition = makeCompositionHeader(parseTree);
       // System.out.println("AST " + expression);
        Map<String, Voice> voiceMap = new HashMap<>();
        for(int voiceNumber = 1; voiceNumber < parseTree.children().size(); voiceNumber++) {
            ParseTree<MusicGrammar> voice = parseTree.children().get(voiceNumber);
            List<String> lyricList = parseLyrics(voice.childrenByName(MusicGrammar.LYRIC));
            NoteEnvironment environment = new NoteEnvironment(composition, lyricList);
            String voiceName = "";
            if(voice.childrenByName(MusicGrammar.VOICENAME).size() > 0) {
                voiceName = voice.childrenByName(MusicGrammar.VOICENAME)
                        .get(0).text();
            }
            //use parsetree to make a line of music aligned with voices
            Music line = makeMusicAST(voice.childrenByName(MusicGrammar.MUSICLINE)
                    .get(0), environment);
            
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
     * @return Expression parsed from the string
     * @throws UnableToParseException if the string doesn't match the Expression grammar
     * @throws FileNotFoundException 
     */
    public Composition parseFile(final File file) throws UnableToParseException {
        // parse the example into a parse tree
        try {
            String input = "";
            Scanner inFile = new Scanner(file);
            while(inFile.hasNextLine()) {
                input+=inFile.nextLine();
            }
            return this.parse(input);
        }
        catch(Exception e) {
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
                composition.setKey(Key.valueOf(field.text()));
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
                return new Rest(0);
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
                environment.scaleLength((tupleSize-1)/tupleSize);
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
                Music chord = new Chord(notes, environment.lyricIndex);
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
                notePitch = notePitch.transpose(-1);
                environment.setAccidental(value+octaveModifier, Accidental.FLAT);
            }
            else if(name == MusicGrammar.DOUBLEFLAT) {
                notePitch = notePitch.transpose(-2);
                environment.setAccidental(value+octaveModifier, Accidental.DOUBLE_FLAT);
            }
            else if(name == MusicGrammar.SHARP) {
                notePitch = notePitch.transpose(1);
                environment.setAccidental(value+octaveModifier, Accidental.SHARP);
            }
            else if(name == MusicGrammar.DOUBLESHARP) { 
                notePitch = notePitch.transpose(2);
                environment.setAccidental(value+octaveModifier, Accidental.DOUBLE_SHARP);
            }
            else if(name == MusicGrammar.NATURAL) {
                environment.setAccidental(value+octaveModifier, Accidental.NATURAL);
            }
            else {
                throw new AssertionError("should never get here: "+accidental.children().get(0).name().toString());
            }
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
               
        return new Note(environment.defaultDuration()*(numerator/denominator), notePitch, DEFAULT_INSTRUMENT, environment.lyricIndex);
        
    }

    private static List<String> parseLyrics(List<ParseTree<MusicGrammar>> lyricList) {
        if(lyricList.size() ==0) {
            return Collections.emptyList();
        }
        
        List<String> lyricSyllables = new ArrayList<>();
        for(ParseTree<MusicGrammar> syllableNote: lyricList.get(0).children()) {
            String syllable ="";
            for(ParseTree<MusicGrammar> child: syllableNote.children()) {
                if(child.text().equals("~")) {
                    syllable += " ";
                }
                else {
                    syllable+= child.text();
                }
            }
            lyricSyllables.add(syllable);
        }
        return lyricSyllables;
    }
    
    
    protected class NoteEnvironment{
        private final double defaultDuration;
        private double duration;
        private int lyricIndex;
        private final List<String> lyrics;
        private boolean newMeasure;
        private Map<String, Accidental> noteMap;
        private final Key key;
        private boolean lock;
        
        private NoteEnvironment(Composition composition, List<String> lyricList) {
            noteMap = new HashMap<>(KEY_SIGNATURES.get(composition.key()));
            lyricIndex = 0;
            lyrics = lyricList;
            newMeasure = true;
            defaultDuration = 4*100*composition.length()*(1/composition.tempo());
            duration = defaultDuration;
            key = composition.key();
            lock = false;
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
            if(!lock && lyricIndex < lyrics.size()-1) {
                if(!lyrics.get(lyricIndex+1).equals("|") || newMeasure) {
                    lyricIndex+=1;
                    newMeasure = false;
                }
            }
        }
        
        private void setAccidental(String key, Accidental accidental) {
            noteMap.put(key, accidental);
        }
        
        private void resetAccidentals() {
            noteMap = new HashMap<>(KEY_SIGNATURES.get(key));
        }
        
        private double defaultDuration() {
            return this.duration;
        }
        
        private Map<String, Accidental> accidentalMap(){
            return new HashMap<>(this.noteMap);
        }
        
    }
}

