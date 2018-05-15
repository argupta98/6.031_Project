package karaoke;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import edu.mit.eecs.parserlib.UnableToParseException;
import karaoke.Voice.LyricListener;
import karaoke.parser.MusicParser;
import karaoke.player.Player;

public class StreamingServer {
    
    private final HttpServer server;
    private final Player karaoke;
    // Abstraction Function
    // AF(sever, piece, karaoke) => A webserver server that streams the music in piece, through the player karaoke and streams the lyrics
    //                              of piece to clients. 
    
    // Rep Invaraint
    // - server cannot be null
    // - piece cannot be null
    // - karaoke cannot be null
    
    // Safety from Rep Exposure
    // - All fields are private and final
    // - No mutable fields are directly returned to the client
    
    
    // Thread Safety Argument
    // - Player object is a threadsafe datatype allowing multiple clients to stream lyrics 
    // - Playback will only occur on the thread that started the server
    
    /**
     * Make a new server to stream lyrics to a given piece of music
     * @param filename to the server will stream the lyrics to
     * @param port number that the server will listen to 
     * @throws IOException 
     * @throws UnableToParseException 
     */
    public StreamingServer(String filename, int port) throws IOException, UnableToParseException {
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        File abcFile = new File(filename);
        this.karaoke = new Player(abcFile);
        
        // handle concurrent requests with multiple threads
        server.setExecutor(Executors.newCachedThreadPool());
        
        //  handle requests for /voice/voiceID
        HttpContext voice = server.createContext("/voice/", new HttpHandler() {
            public void handle(HttpExchange exchange) throws IOException {
                handleVoice(exchange);
                checkRep();
            }
        }); 
        
        //  handle requests for /play/
        HttpContext play = server.createContext("/play/", new HttpHandler() {
            public void handle(HttpExchange exchange) throws IOException {
                try {
                    handlePlay(exchange);
                } catch (UnableToParseException | MidiUnavailableException | InvalidMidiDataException e) {
                    e.printStackTrace();
                }
                checkRep();
            }
        }); 
    }
    
    /**
     * Plays back piece that on the machine running the StreamingServer
     *  
     * @throws UnableToParseException if the file is invalid. 
     * @throws InvalidMidiDataException 
     * @throws MidiUnavailableException 
     * @return An object that can be used to wait for the song to finish
     */
    public Object playback() throws UnableToParseException, MidiUnavailableException, InvalidMidiDataException {
        return this.karaoke.play();
    }
    /**
     * Handles requests for streaming the lyrics for a voice from the piece 
     * @param HTTP request/response, modified by this method to send a response and close the exchange
     * @throws IOException 
     */
    private void handleVoice(HttpExchange exchange) throws IOException {
        final int successCode = 200;
        final int failureCode = 404;
        
        // requested path
        final String path = exchange.getRequestURI().getPath();
        // check that the correct exchange is being handled in this case
        final String base = exchange.getHttpContext().getPath();
        assert path.startsWith(base);
        
        // Get the voice for the server to stream 
        String voice = path.substring(base.length());
        String voiceID = voice.split("/")[0];
        exchange.sendResponseHeaders(successCode, 0);
        

        
        // Callback in order to get current lyric associated with the note being played
        // and write out line to client socket 
        this.karaoke.addLyricListener(voiceID, (line) -> {
                if(line.equals("END")) {
                    exchange.close();
                }
                else {
                    OutputStream body = exchange.getResponseBody();
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(body, UTF_8), true);
                    out.println(line);
                    out.flush();
                }
        });
        
        checkRep();
    }
    
    /**
     * Handles requests for streaming the lyrics for a voice from the piece 
     * @param HTTP request/response, modified by this method to send a response and close the exchange
     * @throws UnableToParseException 
     * @throws InvalidMidiDataException 
     * @throws MidiUnavailableException 
     * @throws IOException 
     */
    private void handlePlay(HttpExchange exchange) throws UnableToParseException, MidiUnavailableException, InvalidMidiDataException, IOException {
        final int successCode = 200;
        final int failureCode = 404;
        
        // requested path
        final String path = exchange.getRequestURI().getPath();
        // check that the correct exchange is being handled in this case
        final String base = exchange.getHttpContext().getPath();
        assert path.startsWith(base);
        
        exchange.sendResponseHeaders(successCode, 0);
        
        // Play song 
        this.karaoke.play();
        
        checkRep();
        exchange.close();
    }
    private void checkRep() {
        assert server != null;
        assert karaoke != null;
    }
    
    /**
     * @return port server is listening in on
     */
    public int port() {
        return server.getAddress().getPort();
    }
    
    /**
     * Start the server in a new background thread
     */
    public void start() {
        System.err.println("Server will listen on " + server.getAddress());
        server.start();
    }
    
    /**
     * Stop the server.
     */
    public void stop() {
       server.stop(0);
    }
    
    /**
     * @return address that the server is hosted on
     * @throws UnknownHostException 
     */
    public InetSocketAddress getAddress() throws UnknownHostException {
        return server.getAddress();
    }

}
