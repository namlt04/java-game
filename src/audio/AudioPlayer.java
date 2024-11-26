package audio;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import gamestates.Gamestate;

public class AudioPlayer {

    public static int MENU_1 = 0;
    public static int LEVEL_1 = 1;
    public static int LEVEL_2 = 2;

    public static int DIE = 0;
    public static int JUMP = 1;
    public static int GAMEOVER = 2;
    public static int LVL_COMPLETED = 3;
    public static int ATTACK_ONE = 4;
    public static int ATTACK_TWO = 5;
    public static int ATTACK_THREE = 6;
    private Clip[] songs, effects;
    private int currID;
    private Stack<Integer> stack = new Stack<>(); 
    private HashMap<Integer, Long> hashMap = new HashMap<>();
    public AudioPlayer() {
        loadSong();
        loadEffect();
        playSong(0);
    }
    private void loadEffect() {
        String[] effect = {"die", "jump", "gameover", "lvlcomplete", "attack1", "attack2", "attack3", "collect_diamond", "collect_heart"};
        effects = new Clip[effect.length];
        for (int i = 0; i < effect.length; i++) {
            effects[i] = getClip(effect[i]);
        }

    }

    private void loadSong() {
        String[] song = {"menu", "level1", "level2"};
        songs = new Clip[song.length];
        for (int i = 0; i < song.length; i++) {
            songs[i] = getClip(song[i]);
        }

    }

    public void setLevelSong(int lvlIndex) {
        if (lvlIndex % 2 == 0) {
            playSong(LEVEL_1);
        } else {
            playSong(LEVEL_2);
        }

    }

    public void playAttackSound() {
        int start = 4;
        Random rand = new Random();
        start = start + rand.nextInt(3);
        playEffect(start);
    }

    private Clip getClip(String name) {
        URL url = getClass().getResource("/audio/" + name + ".wav");
        AudioInputStream audio;
        try {
            audio = AudioSystem.getAudioInputStream(url);
            Clip c = AudioSystem.getClip();
            c.open(audio);
            return c;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        }
        return null;
    }
    public void pauseSong(){
        long index = songs[currID].getMicrosecondPosition();
        songs[currID].stop();
        stack.push(currID);
        hashMap.put(currID, index);
        
    }
    public void update() {
        playSong(0);
    }
    public void playingToMenu(int song){
        pauseSong(); 
        currID = song; 
        songs[currID].setMicrosecondPosition(0);
        songs[currID].loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void setLevelSongToPause() { // TU MENU TRO VE MAN CHOI ( PLAYING )
        stopSong();
        int currID = stack.pop();
        long time = hashMap.get(currID);
        songs[currID].setMicrosecondPosition(time);
        songs[currID].loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void playSong(int song) {
        stopSong();
        currID = song;
        songs[currID].setMicrosecondPosition(0);
        songs[currID].loop(Clip.LOOP_CONTINUOUSLY);
    }

    private void stopSong() {
        if (songs[currID].isActive()) {
            songs[currID].stop();
        }
    }

    public void playEffect(int effect) {
        if (effects[effect].getMicrosecondPosition() > 0) {
            effects[effect].setMicrosecondPosition(0);
        }
        effects[effect].start();
    }

    public void updateMusicMute() {
        for (Clip c : songs) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            if (Gamestate.getMusic() == 0) {
                booleanControl.setValue(false);
            } else {
                booleanControl.setValue(true);
            }
        }
    }

    public void updateSfxMute() {
        for (Clip c : effects) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            if (Gamestate.getSfx() == 0) {
                booleanControl.setValue(false);
            } else {
                booleanControl.setValue(true);
            }
        }
    }
}
