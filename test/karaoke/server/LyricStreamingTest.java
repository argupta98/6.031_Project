package karaoke.server;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import examples.StreamingExample;
import karaoke.Composition;
import karaoke.StreamingServer;
import karaoke.parser.MusicParser;
import karaoke.sound.MidiSequencePlayer;
import karaoke.sound.SequencePlayer;
import memory.Board;
import memory.WebServer;
import edu.mit.eecs.parserlib.UnableToParseException;


public class LyricStreamingTest {
    
    
   /*
    * Testing strategy for Lyric Streaming
    * 
    * Timing of lyrics will be tested manually
    * 
    * Set up music files, parse them and stream them,
    * make sure that number of lines matches expected.
    * Also check that words are displayed correctly.
    * 
    */

    //Covers: Streaming single line of lyrics
    @Test
    public void testLyricsSingleLine() throws UnableToParseException, IOException {
    	final int serverPort = 4567;
    	Composition piece = (new MusicParser()).parseFile(new File("sample-abc/piece1.abc"));
        final StreamingServer server = new StreamingServer(piece, serverPort);
        
        // start the server
        server.start();

        final URL valid = new URL("http://localhost:" + serverPort);
        
        // sleep for 10 seconds while we connect to server
        try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        final InputStream input = valid.openStream();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(input, UTF_8));
        
        assertEquals("Amazing grace! How sweet the sound That saved a wretch like me.", reader.readLine()); 
        
    }
        
}
