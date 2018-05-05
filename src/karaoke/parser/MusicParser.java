/* Copyright (c) 2017-2018 MIT 6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package karaoke.parser;

import java.io.File;
import java.io.IOException;

import edu.mit.eecs.parserlib.ParseTree;
import edu.mit.eecs.parserlib.Parser;
import edu.mit.eecs.parserlib.UnableToParseException;
import edu.mit.eecs.parserlib.Visualizer;
import karaoke.Composition;

public class MusicParser {
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
    private static enum ExpressionGrammar {
        COMPOSITION, HEADER, TRACKNUMBER, COMPOSER, METER, LENGTH, TEMPO,
        VOICENAME, KEY, VOICE, MUSICLINE, MEASURE, PRIMITIVE, NOTE, OCTAVEUP, OCTAVEDOWN,
        NOTENUMERATOR, NOTEDENOMINATOR, ACCIDENTAL, SHARP, FLAT, LYRIC, SYLLABLENOTE, 
        SYLLABLE, LETTER, COMMENT, NUMBER, END, WHITESPACE, WHITESPACEANDCOMMENT, TITLE,
        CHORD, TUPLE
    }

    private static Parser<ExpressionGrammar> parser = makeParser();
    
    /**
     * Compile the grammar into a parser.
     * 
     * @return parser for the grammar
     * @throws RuntimeException if grammar file can't be read or has syntax errors
     */
    private static Parser<ExpressionGrammar> makeParser() {
        try {
            // read the grammar as a file, relative to the project root.
            final File grammarFile = new File("src/karaoke/parser/Abc.g");
            return Parser.compile(grammarFile, ExpressionGrammar.COMPOSITION);

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
    public static void parse(final String string) throws UnableToParseException {
        // parse the example into a parse tree
        final ParseTree<ExpressionGrammar> parseTree = parser.parse(string);

        // display the parse tree in various ways, for debugging only
        System.out.println("parse tree " + parseTree);
        Visualizer.showInBrowser(parseTree);
        
        //TODO
        // make an AST from the parse tree
        //final Composition composition = makeAbstractSyntaxTree(parseTree);
        // System.out.println("AST " + expression);
        
        //return composition;
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
    
    /**
     * Convert a parse tree into an abstract syntax tree.
     * 
     * @param parseTree constructed according to the grammar in Exression.g
     * @return abstract syntax tree corresponding to parseTree
     */
    private static Composition makeAbstractSyntaxTree(final ParseTree<ExpressionGrammar> parseTree) {
        throw new UnsupportedOperationException("not implemented yet");
    }

}

