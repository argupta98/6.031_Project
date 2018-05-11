package karaoke;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import edu.mit.eecs.parserlib.UnableToParseException;
import karaoke.player.Player;


public class Main {


    /**
     * Main entry point of your application.
     * 
     * Command line arguments:
     *  PATH PORT
     *  
     *  PATH => path to the abc file to be played
     *  PORT => port the server is to be hosted on
     *  
     *  @param args is a list of command line arguments as specified above
     * @throws UnableToParseException 
     * @throws IOException 
     *  
     */
    public static void main(String[] args) throws UnableToParseException, IOException {
        /*
        final File input = new File("sample-abc/waxies_dargle.abc");
        try {
            Player musicPlayer = new Player(input);
            musicPlayer.addLyricListener("",  (String line) -> System.out.println(line));
            musicPlayer.play();
        } catch (Exception e) {
            //  Auto-generated catch block
            e.printStackTrace();
        }
        */

        final Queue<String> arguments = new LinkedList<>(Arrays.asList(args));
        final String filename;
        final int port;
        // grab filename from the command line arguments
        try {
            filename = arguments.remove();
        } catch (NoSuchElementException nse) {
            throw new IllegalArgumentException("missing PROTOCOL", nse);
        }
        // grab port from command line arguments 
        try {
            port = Integer.parseInt(arguments.remove());
        } catch (NoSuchElementException | NumberFormatException e) {
            throw new IllegalArgumentException("missing or invalid PORT", e);
        }
        
        StreamingServer server = new StreamingServer(filename,port);
        server.start();
        Player karaoke = new Player(new File(filename));
        String songInfo = karaoke.songInfo();
        System.out.println(songInfo);
        
        String streamingInstructions = "To stream lyrics go to http:/" + server.getAddress() + ":" + port + "/voice/{WANTED VOICE ID}";
        System.out.println(streamingInstructions);
        String playBackInstructions = "To play song go to http:/" + server.getAddress() + ":" + port + "/play/";
        System.out.println(playBackInstructions);
    }
}
