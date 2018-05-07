package karaoke;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import karaoke.Voice.LyricListener;

public class StreamingServer {
    
    private final HttpServer server;
    private final Composition piece;
    
    // Abstraction Function
    // AF(sever, piece) => A webserver server that streams the lyrics of piece for the client and plays piece
    //                       locally
    
    // Rep Invaraint
    // - server cannot be null
    // - piece cannot be null
    
    // Safety from Rep Exposure
    
    
    // Thread Safety Argument
    
    /**
     * Make a new server to stream lyrics to a given piece of music
     * @param music to the server will stream the lyrics to
     * @param port number that the server will listen to 
     * @throws IOException 
     */
    public StreamingServer(Composition music, int port) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        this.piece = music;
        
        // handle concurrent requests with multiple threads
        server.setExecutor(Executors.newCachedThreadPool());
        
        //  handle requests for /voice/voiceID
        HttpContext voice = server.createContext("/voice/", new HttpHandler() {
            public void handle(HttpExchange exchange) throws IOException {
                handleVoice(exchange);
                checkRep();
            }
        }); 
    }
    
    /**
     * Handles requests for streaming the lyrics for a voice from the piece 
     * @param HTTP request/response, modified by this method to send a response and close the exchange
     */
    private void handleVoice(HttpExchange exchange) {
        final int successCode = 200;
        final int failureCode = 404;
        
        // requested path
        final String path = exchange.getRequestURI().getPath();
        // check that the correct exchange is being handled in this case
        final String base = exchange.getHttpContext().getPath();
        assert path.startsWith(base);
        
        String voice = path.substring(base.length());
        String voiceID = voice.split("/")[0];
     
        piece.addVoiceListener(voiceID, new LyricListener() {
            public void notePlayed(String line) {
                try {
                    exchange.sendResponseHeaders(successCode, 0);
                } catch (IOException e) {
                    try {
                        exchange.sendResponseHeaders(failureCode, 0);
                    } catch (IOException e1) {

                        e1.printStackTrace();
                    }
                }
                OutputStream body = exchange.getResponseBody();
                PrintWriter out = new PrintWriter(new OutputStreamWriter(body, UTF_8), true);
                out.println(line);
            }
        });
        
        checkRep();
        exchange.close();
    }
    
    private void checkRep() {
        assert server != null;
        assert piece != null;
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
        server.start();
    }
    
    /**
     * Stop the server.
     */
    public void stop() {
       server.stop(0);
    }

}
