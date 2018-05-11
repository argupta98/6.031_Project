package karaoke.player;

import karaoke.Composition;
import karaoke.Voice.LyricListener;
import karaoke.parser.MusicParser;
import karaoke.sound.MidiSequencePlayer;
import karaoke.sound.SequencePlayer;

import java.io.File;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import edu.mit.eecs.parserlib.UnableToParseException;

//An immutable wrapper that plays parsed music
public class Player {
    private final Composition music;
    
    //AF(music) = a music player that can play this piece of music 
    //RI: True
    //Safety from rep exposure:
    //  All internal variables are private and final
    //  Client has no reference to internal variables
    //Thread Safety:
    //  Uses monitor pattern
    
    
    /**
     * Creates a new player that plays the music represented in the given file
     * @param input the file containing the music
     * @throws UnableToParseException if the file cannot be parsed
     */
    public Player(File input) throws UnableToParseException {
        //parse input into a Piece object
        this.music = (new MusicParser()).parseFile(input);
        checkRep();
    }
    
    private void checkRep() {
        assert music != null;
    }
    /**
     * Creates a new player that plays the music represented by the given string
     * @param input the string containing the music
     * @throws UnableToParseException if the file cannot be parsed
     */
    public Player(String input) throws UnableToParseException {
        //parse input into a Piece object
        this.music = (new MusicParser()).parse(input);
        checkRep();
    }
    
    /**
     * @return the title and composer of the piece to be played
     */
    public String songInfo() {
        String title = music.title();
        String composer = music.composer();
        return "Title: " + title + ", Composer: " + composer;
    }
    
    /**
     * Plays the music described by this.music
     * @throws InvalidMidiDataException 
     * @throws MidiUnavailableException 
     * @return an object that can be used as a lock to wait for the piece to finish
     */
    public synchronized Object play() throws MidiUnavailableException, InvalidMidiDataException {
        //play the piece
        final int beatsPerMinute = 100;
        final int ticksPerBeat = 64;    
        SequencePlayer player = new MidiSequencePlayer(ticksPerBeat, beatsPerMinute);
        music.play(player);
        Object lock = new Object();
        player.addEvent(music.duration(), (Double beat) -> {
            synchronized (lock) {
                lock.notify();
            }
        });
        
        player.play();
        checkRep();
        return lock;
       }
    
    /**
     * Adds a listener to the lyrics of the voice, voice. This listener is notified 
     * with an updated line of lyrics every time the voice changes notes
     * @param voice the voice whose lyrics are being listened to
     * @param listener the listener being added
     */
    public synchronized void addLyricListener(String voice, LyricListener listener) {
            music.addVoiceListener(voice, listener);
            checkRep();
    }
}
