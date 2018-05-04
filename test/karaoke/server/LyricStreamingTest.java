package karaoke.server;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.junit.Test;


public class LyricStreamingTest {

    //Covers: flipCard/look: 1-D, 2-A, watch: ALL
    @Test
    public void testFlipCard1_D_2_A() throws IOException, InterruptedException{
        //setup server
        Board testBoard = Board.parseFromFile("./boards/zoom.txt");
        final WebServer server = new WebServer(testBoard, 0);
        server.start();
        
        URL valid, watch, watch2;
        InputStream input;
        InputStream watchStream, watchStream2;
        BufferedReader in;
        
        valid = new URL("http://localhost:" + server.port() + "/flip/1/2,2");
        input = valid.openStream();
        in = new BufferedReader(new InputStreamReader(input, UTF_8));
        assertEquals("5\n"
                + "5\n"
                + "down\ndown\ndown\ndown\ndown\n"
                + "down\nmy 🚂\ndown\ndown\ndown\n"
                + "down\ndown\ndown\ndown\ndown\n"
                + "down\ndown\ndown\ndown\ndown\n"
                + "down\ndown\ndown\ndown\ndown\n", getBoard(in));
        
        watch = new URL("http://localhost:" + server.port() + "/watch/2");
        watchStream = watch.openStream();
        
        watch2 = new URL("http://localhost:" + server.port() + "/watch/1");
        watchStream2 = watch2.openStream();
        
        //Select second card outside of game
        valid = new URL("http://localhost:" + server.port() + "/flip/1/8,8");
        input = valid.openStream();
        in = new BufferedReader(new InputStreamReader(input, UTF_8));
        assertEquals("5\n"
                + "5\n"
                + "down\ndown\ndown\ndown\ndown\n"
                + "down\nup 🚂\ndown\ndown\ndown\n"
                + "down\ndown\ndown\ndown\ndown\n"
                + "down\ndown\ndown\ndown\ndown\n"
                + "down\ndown\ndown\ndown\ndown\n", getBoard(in));
        
        valid = new URL("http://localhost:" + server.port() + "/flip/1/1,1");
        input = valid.openStream();
        in = new BufferedReader(new InputStreamReader(input, UTF_8));
        assertEquals("5\n"
                + "5\n"
                + "my 🚚\ndown\ndown\ndown\ndown\n"
                + "down\ndown\ndown\ndown\ndown\n"
                + "down\ndown\ndown\ndown\ndown\n"
                + "down\ndown\ndown\ndown\ndown\n"
                + "down\ndown\ndown\ndown\ndown\n", getBoard(in));
        
        //CHeck that watch1 returned
        in = new BufferedReader(new InputStreamReader(watchStream, UTF_8));
        assertEquals("5\n"
                + "5\n"
                + "up 🚚\ndown\ndown\ndown\ndown\n"
                + "down\ndown\ndown\ndown\ndown\n"
                + "down\ndown\ndown\ndown\ndown\n"
                + "down\ndown\ndown\ndown\ndown\n"
                + "down\ndown\ndown\ndown\ndown\n", getBoard(in));
        
        //Check that watch2 returned
        in = new BufferedReader(new InputStreamReader(watchStream2, UTF_8));
        assertEquals("5\n"
                + "5\n"
                + "my 🚚\ndown\ndown\ndown\ndown\n"
                + "down\ndown\ndown\ndown\ndown\n"
                + "down\ndown\ndown\ndown\ndown\n"
                + "down\ndown\ndown\ndown\ndown\n"
                + "down\ndown\ndown\ndown\ndown\n", getBoard(in));

        valid = new URL("http://localhost:" + server.port() + "/look/2");
        input = valid.openStream();
        in = new BufferedReader(new InputStreamReader(input, UTF_8));
        assertEquals("5\n"
                + "5\n"
                + "up 🚚\ndown\ndown\ndown\ndown\n"
                + "down\ndown\ndown\ndown\ndown\n"
                + "down\ndown\ndown\ndown\ndown\n"
                + "down\ndown\ndown\ndown\ndown\n"
                + "down\ndown\ndown\ndown\ndown\n", getBoard(in));

        //should relinquish control of both cards, so thread should evaluate to true
    }
}
