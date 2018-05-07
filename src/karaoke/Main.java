package karaoke;

import java.io.File;

import edu.mit.eecs.parserlib.UnableToParseException;
import karaoke.player.Player;

/**
 * Main entry point of your application.
 */
public class Main {

    /**
     * TODO
     * @param args TODO
     */
    public static void main(String[] args) {
        final File input = new File("sample-abc/piece3.abc");
        try {
            Player musicPlayer = new Player(input);
            musicPlayer.addLyricListener("",  (String line) -> System.out.println(line));
            musicPlayer.play();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
