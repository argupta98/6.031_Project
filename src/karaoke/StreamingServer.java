package karaoke;

import com.sun.net.httpserver.HttpServer;

public class StreamingServer {
    
    private final HttpServer server;
    private final Compostion piece;
    
    // Abstraction Function
    // AF(sever, piece) => A webserver server that streams the lyrics of piece for the client.
    
    // Rep Invaraint
    // - server cannot be null
    // - piece cannot be null
    
    /**
     * Make a new server to stream lyrics to a given piece of music
     * @param music to the server will stream the lyrics to
     * @param port number that the server will listen to 
     */
    public StreamingServer(Music music, int port) {
        throw new RuntimeException("Not implemented");
    }
    
    /**
     * @return port server is listening in on
     */
    public int port() {
        throw new RuntimeException("Not implemented yet");
    }
    
    /**
     * Start the server in a new background thread
     */
    public void start() {
        throw new RuntimeException("Not implemented");
    }
    
    /**
     * Stop the server.
     */
    public void stop() {
        throw new RuntimeException("Not implemented");
    }

}
