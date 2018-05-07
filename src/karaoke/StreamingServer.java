package karaoke;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import karaoke.parser.MusicParser;
import karaoke.server.LyricStreamingTest;

public class StreamingServer {
    
    private final HttpServer server;
    private final Composition piece;
    
    // Abstraction Function
    // AF(sever, piece) => A webserver server that streams the lyrics of piece for the client.
    
    // Rep Invaraint
    // - server cannot be null
    // - piece cannot be null
    
    // Safety from Rep Exposure
    
    
    // Thread Safety Argument
    
    /**
     * Make a new server to stream lyrics to a given piece of music
     * @param music to the server will stream the lyrics to
     * @param port number that the server will listen to 
     */
    public StreamingServer(Composition piece, int port) throws IOException {
        this.piece = piece;
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        
        // handle concurrent requests with multiple threads
        server.setExecutor(Executors.newCachedThreadPool());
        
        // register handler
        server.createContext("/htmlWaitReload", StreamingServer::htmlWaitReload);
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
        System.err.println("Server will stop");
        server.stop(0);
    }
    
    /**
     * This handler waits for an event to occur in the server
     * before sending a complete HTML page to the web browser.
     * The page ends with a Javascript command that immediately starts
     * reloading the page at the same URL, which causes this handler to be
     * run, wait for the next event, and send an updated HTML page.
     * In this simple example, the "server event" is just a brief timeout, but it
     * could synchronize with another thread instead.
     * 
     * @param exchange request/reply object
     * @throws IOException if network problem
     */
    private static void htmlWaitReload(HttpExchange exchange) throws IOException {
        final String path = exchange.getRequestURI().getPath();
        System.err.println("received request " + path);

        // html response
        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
        
        // must call sendResponseHeaders() before calling getResponseBody()
        final int successCode = 200;
        final int lengthNotKnownYet = 0;
        exchange.sendResponseHeaders(successCode, lengthNotKnownYet);

        // get output stream to write to web browser
        final boolean autoflushOnPrintln = true;
        PrintWriter out = new PrintWriter(
                              new OutputStreamWriter(
                                  exchange.getResponseBody(), 
                                  StandardCharsets.UTF_8), 
                              autoflushOnPrintln);
        
        try {

            // Wait until an event occurs in the server.
            // In this example, the event is just a brief fixed-length delay, but it
            // could synchronize with another thread instead.
            final int millisecondsToWait = 200;
            try {
                Thread.sleep(millisecondsToWait);
            } catch (InterruptedException e) {
                return;
            }
            
            // Send a full HTML page to the web browser
            out.println(System.currentTimeMillis() + "<br>");
            
            // End the page with Javascript that causes the browser to immediately start 
            // reloading this URL, so that this handler runs again and waits for the next event
            out.println("<script>location.reload()</script>");
            
        } finally {
            exchange.close();
        }
        System.err.println("done streaming request");
    }

}
