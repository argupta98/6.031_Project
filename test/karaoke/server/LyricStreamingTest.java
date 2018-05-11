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
import edu.mit.eecs.parserlib.UnableToParseException;

/**
 * Tests require music be played, so we do not want didit to run them
 * @category no_didit
 */
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
    * Number of specified voices: 0, 1, 2, >2
    * 
    * Lyrics: Voice has lyrics, voice does not have lyrics (place holder)
    * 
    * MANUAL TESTING Partitions:
    * 
    * Lyrics: Lyrics appear in time with music
    */

    //Covers: Streaming single line of lyrics
    @Test
    public void testLyricsSingleLine() throws UnableToParseException, IOException, MidiUnavailableException, InvalidMidiDataException {
    	final int serverPort = 4567;
    	Composition piece = (new MusicParser()).parseFile(new File("sample-abc/piece3.abc"));
        final StreamingServer server = new StreamingServer("sample-abc/piece3.abc", serverPort);
        
        // start the server
        server.start();
        
        
        final URL valid = new URL("http://localhost:" + serverPort + "/voice/");
        
        // start and wait for duration of song .....
        
        final InputStream input = valid.openStream();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(input, UTF_8));
        
        Object lock = server.playback();
        assertNotEquals(lock, null);
        /*
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                return;
            }
        }
        */
        String expected = "*A*mazing grace! How sweet the sound That saved a wretch like me.\n" + 
                "A*ma*zing grace! How sweet the sound That saved a wretch like me.\n" + 
                "Ama*zing* grace! How sweet the sound That saved a wretch like me.\n" + 
                "Ama*zing* grace! How sweet the sound That saved a wretch like me.\n" + 
                "Amazing *grace!* How sweet the sound That saved a wretch like me.\n" + 
                "Amazing grace! *How* sweet the sound That saved a wretch like me.\n" + 
                "Amazing grace! How *sweet* the sound That saved a wretch like me.\n" + 
                "Amazing grace! How sweet *the* sound That saved a wretch like me.\n" + 
                "Amazing grace! How sweet the *sound* That saved a wretch like me.\n" + 
                "Amazing grace! How sweet the sound *That* saved a wretch like me.\n" + 
                "Amazing grace! How sweet the sound That *saved* a wretch like me.\n" + 
                "Amazing grace! How sweet the sound That saved *a* wretch like me.\n" + 
                "Amazing grace! How sweet the sound That saved *a* wretch like me.\n" + 
                "Amazing grace! How sweet the sound That saved a *wretch* like me.\n" + 
                "Amazing grace! How sweet the sound That saved a wretch *like* me.\n" + 
                "Amazing grace! How sweet the sound That saved a wretch like *me.*\n";
        
        String result = "";
        for(int i = 0; i< 16 ; i++) {
            result+=reader.readLine()+"\n";
        }
        
        assertEquals(expected, result);
        server.stop();
    }
        
}
