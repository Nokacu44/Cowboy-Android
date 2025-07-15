package com.example.armando.game.managers;

import android.util.Log;

import com.badlogic.androidgames.framework.Audio;
import com.badlogic.androidgames.framework.Music;
import com.badlogic.androidgames.framework.Sound;
import com.badlogic.androidgames.framework.impl.AndroidAudio;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AudioManager {

    private static final Map<String, Sound> soundMap = new HashMap<>();
    private static final Map<String, Music> musicMap = new HashMap<>();
    private static Audio androidAudio;

    public static void init(Audio audio) {
        if (AudioManager.androidAudio == null) {
            AudioManager.androidAudio = audio;
            preloadSoundsFromDir("audio");
            //Log.d("AUDIO", "AudioManager initialized");

        }
    }

    public static Sound loadSound(String fileName) {
        return soundMap.computeIfAbsent((fileName), key -> androidAudio.newSound(key));
    }

    public static Music loadMusic(String fileName) {
        return musicMap.computeIfAbsent((fileName), key -> androidAudio.newMusic(key));
    }

    public static Sound getSound(String fileName) {
        Sound sound = soundMap.get(fileName);
        if (sound == null) {
            Log.w("AudioManager", "Sound not loaded yet: " + fileName + " -> loading lazily");
            sound = loadSound(fileName);
        }
        return sound;
    }

    public static Music getMusic(String fileName) {
        Music music = musicMap.get(fileName);
        if (music == null) {
            Log.w("AudioManager", "Music not loaded yet: " + fileName + " -> loading lazily");
            music = loadMusic(fileName);
        }
        return music;
    }

    public static void removeSound(String fileName) {
        Sound removed = soundMap.remove(fileName);
        if (removed != null) {
            removed.dispose();
        }
    }

    public static void removeMusic(String fileName) {
        Music removed = musicMap.remove(fileName);
        if (removed != null) {
            removed.dispose();
        }
    }

    public static void clearAll() {
        for (Sound s : soundMap.values()) {
            s.dispose();
        }
        for (Music m : musicMap.values()) {
            m.dispose();
        }
        soundMap.clear();
        musicMap.clear();
    }

    public static void preloadSoundsFromDir(String dirName) {
        try {
            String[] fileList = ((AndroidAudio)androidAudio).assets.list(dirName);
            if (fileList != null) {
                for (String file : fileList) {
                    String fullPath = dirName + "/" + file;
                    if (file.endsWith(".mp3") || file.endsWith(".ogg") || file.endsWith(".wav")) {
                        loadSound(fullPath);  // chiama il metodo del manager
                    }
                }
            }
        } catch (IOException e) {
            Log.e("AudioManager", "Errore nel caricamento dei suoni da: " + dirName, e);
        }
    }

}
