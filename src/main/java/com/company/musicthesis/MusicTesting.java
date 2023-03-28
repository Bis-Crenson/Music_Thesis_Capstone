package com.company.musicthesis;

import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;

import javax.sound.midi.InvalidMidiDataException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

public class MusicTesting {
    public static void main(String[] args) throws InvalidMidiDataException, IOException {
        Player player = new Player();
        MusicGeneratorEnhanced music = new MusicGeneratorEnhanced();
//        Pattern song = new Pattern();
//        song.add("20 V0 I[Violin] C5W D5W E5W E4W F4W G4W");
        Pattern song = music.createSong(1);
        music.exportSong(song);
        player.play(song);
//        noteArrayWithoutLength();
    }


    public static String[] noteArrayWithoutLength() throws InvalidMidiDataException, IOException {
        Pattern sheetMusic = MidiFileManager.loadPatternFromMidi(new File("src/test.mid")); //Open the music file
        String[] fullNoteSheet = sheetMusic.toString().split(" "); //Separate by spaces
        String[] finalNotes = new String[fullNoteSheet.length - 2]; //This will be the final notes with only notes
        for(int i = 3; i < fullNoteSheet.length; i++ ){
            finalNotes[i - 3] = fullNoteSheet[i].substring(0, fullNoteSheet[i].length() - 1); //Remove the note length
        }
        return finalNotes; //Remove later
    }
}
