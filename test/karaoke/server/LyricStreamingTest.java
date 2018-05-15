package karaoke.server;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import org.junit.Test;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import karaoke.Composition;
import karaoke.StreamingServer;
import karaoke.parser.MusicParser;
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
    * Number of voices: 1, >=2
    * Number of lines / voice: 1, >=2
    * 
    * Lyrics: Voice has lyrics, voice does not have lyrics (place holder)
    * 
    * MANUAL TESTING Partitions:
    * 
    * Lyrics: Lyrics appear in time with music
    */
	
    //Covers: No lyrics
    @Test
    public void testStreamingNoLyrics() throws UnableToParseException, IOException, MidiUnavailableException, InvalidMidiDataException {
    	final int serverPort = 4567;
        final StreamingServer server = new StreamingServer("sample-abc/piece2.abc", serverPort);
        
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
        String expected = "No Lyrics";
        String result = reader.readLine();
        assertEquals(expected, result);
        server.stop();
    }
    
    //Covers: Streaming single voice, single line
    @Test
    public void testLyricsSingleVoiceSingleLine() throws UnableToParseException, IOException, MidiUnavailableException, InvalidMidiDataException {
    	final int serverPort = 4568;
        final StreamingServer server = new StreamingServer("sample-abc/piece3.abc", serverPort);
        
        // start the server
        server.start();
        
        
        final URL valid = new URL("http://localhost:" + serverPort + "/voice/");
        
        // start and wait for duration of song .....
        
        final InputStream input = valid.openStream();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(input, UTF_8));
        
        Object lock = server.playback();
        assertNotEquals(lock, null);

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
    
    //Covers: Streaming single voice, multiple lines
    @Test
    public void testLyricsSingleVoiceMultipleLines() throws UnableToParseException, IOException, MidiUnavailableException, InvalidMidiDataException {
    	final int serverPort = 4568;
        final StreamingServer server = new StreamingServer("sample-abc/rains_of_castamere.abc", serverPort);
        
        // start the server
        server.start();
        
        
        final URL valid = new URL("http://localhost:" + serverPort + "/voice/");
        
        // start and wait for duration of song .....
        
        final InputStream input = valid.openStream();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(input, UTF_8));
        
        Object lock = server.playback();
        assertNotEquals(lock, null);

        String expected = 
        		
        		"*and* who are you, the proud lord said\n" +
        		"and *who* are you, the proud lord said\n" +
        		"and who *are* you, the proud lord said\n" +
        		"and who are *you,* the proud lord said\n" +
        		"and who are you, *the* proud lord said\n" +
        		"and who are you, the *proud* lord said\n" +
        		"and who are you, the proud *lord* said\n" +
        		"and who are you, the proud lord *said*\n" +
        		
        		"*that* I must bow so low?\n" +
        		"that *I* must bow so low?\n" +
        		"that I *must* bow so low?\n" +
        		"that I must *bow* so low?\n" +
        		"that I must bow *so* low?\n" +
        		"that I must bow so *low?*\n" +
        				
        		"*on*ly a cat of a different coat\n" +
        		"on*ly* a cat of a different coat\n" +
        		"only *a* cat of a different coat\n" +
        		"only a *cat* of a different coat\n" +
        		"only a cat *of* a different coat\n" +
        		"only a cat of *a* different coat\n" +
        		"only a cat of a *diff*erent coat\n" +
        		"only a cat of a diff*erent* coat\n" +
        		"only a cat of a different *coat*\n" +
        		
        		"*that's* all the truth I know\n" +
        		"that's *all* the truth I know\n" +
        		"that's *all* the truth I know\n" +
        		"that's all *the* truth I know\n" +
        		"that's all the *truth* I know\n" +
        		"that's all the truth *I* know\n" +
        		"that's all the truth I *know*\n" +
        		
        		"*in a* coat of gold or a coat of red\n" +
        		"in a *coat* of gold or a coat of red\n" +
        		"in a coat *of* gold or a coat of red\n" +
        		"in a coat of *gold* or a coat of red\n" +
        		"in a coat of gold *or a* coat of red\n" +
        		"in a coat of gold or a *coat* of red\n" +
        		"in a coat of gold or a coat *of* red\n" +
        		"in a coat of gold or a coat of *red*\n" +
        		
        		"*a* lion still has claws\n" +
        		"a *li*on still has claws\n" +
        		"a li*on* still has claws\n" +
        		"a lion *still* has claws\n" +
        		"a lion still *has* claws\n" +
        		"a lion still has *claws*\n" +
        		
        		"*and* mine are long and sharp, my lord\n" +
        		"and *mine* are long and sharp, my lord\n" +
        		"and mine *are* long and sharp, my lord\n" +
        		"and mine are *long* and sharp, my lord\n" +
        		"and mine are long *and* sharp, my lord\n" +
        		"and mine are long and *sharp,* my lord\n" +
        		"and mine are long and sharp, *my* lord\n" +
        		"and mine are long and sharp, my *lord*\n" +
        		
        		"*as* long and sharp as yours\n" +
        		"as *long* and sharp as yours\n" +
        		"as long *and* sharp as yours\n" +
        		"as long and *sharp* as yours\n" +
        		"as long and sharp *as* yours\n" +
        		"as long and sharp as *yours*\n" +
        		
        		"*and* so he spoke, and so he spoke\n" +
        		"and *so* he spoke, and so he spoke\n" +
        		"and so *he* spoke, and so he spoke\n" +
        		"and so he *spoke,* and so he spoke\n" +
        		"and so he spoke, and so he spoke\n" +
        		"and so he spoke, *and* so he spoke\n" +
        		"and so he spoke, and *so* he spoke\n" +
        		"and so he spoke, and so *he* spoke\n" +
        		"and so he spoke, and so he *spoke*\n" +
        		"and so he spoke, and so he spoke\n" +
        		
        		"*that* lord of castamere\n" +
        		"that *lord* of castamere\n" +
        		"that lord *of* castamere\n" +
        		"that lord of *cas*tamere\n" +
        		"that lord of *cas*tamere\n" +
        		"that lord of cas*ta*mere\n" +
        		"that lord of casta*mere*\n" +
        		"that lord of casta*mere*\n" +
        		
        		"*but* now the rains weep o'er his hall\n" +
        		"but *now* the rains weep o'er his hall\n" +
        		"but now *the* rains weep o'er his hall\n" +
        		"but now the *rains* weep o'er his hall\n" +
        		"but now the rains weep o'er his hall\n" +
        		"but now the rains *weep* o'er his hall\n" +
        		"but now the rains weep *o'er* his hall\n" +
        		"but now the rains weep o'er *his* hall\n" +
        		"but now the rains weep o'er his *hall*\n" +
        		
        		"*with* no one there to hear\n" +
        		"with *no* one there to hear\n" +
        		"with no *one* there to hear\n" +
        		"with no one *there* to hear\n" +
        		"with no one there *to* hear\n" +
        		"with no one there to *hear*\n" +
        		
        		"*yes* now the rains weep o'er his hall\n" +
        		"yes *now* the rains weep o'er his hall\n" +
        		"yes now *the* rains weep o'er his hall\n" +
        		"yes now the *rains* weep o'er his hall\n" +
        		"yes now the rains *weep* o'er his hall\n" +
        		"yes now the rains weep *o'er* his hall\n" +
        		"yes now the rains weep o'er *his* hall\n" +
        		"yes now the rains weep o'er his *hall*\n" +
        		
        		"*and* not a soul to hear\n" +
        		"and *not* a soul to hear\n" +
        		"and not *a* soul to hear\n" +
        		"and not a *soul* to hear\n" +
        		"and not a *soul* to hear\n" +
        		"and not a *soul* to hear\n" +
        		"and not a soul *to* hear\n" +
        		"and not a soul to *hear*\n" +
        		
        		"No Lyrics\n" +
        		"No Lyrics\n" +
        		"No Lyrics\n";
        
        String result = "";
        for(int i = 0; i< 110 ; i++) {
            result+=reader.readLine()+"\n";
        }
        
        assertEquals(expected, result);
        server.stop();
    }
        
}
