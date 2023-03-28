package com.company.musicthesis;
import javafx.scene.paint.Color;
import org.jfugue.pattern.*;
import org.jfugue.player.*;
import org.staccato.*;
import org.jfugue.theory.*;
import org.jfugue.rhythm.*;
import org.jfugue.integration.*;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class MusicGenerator {
    double baseLength = 0.0; //Length of the total notes, global variable
    double songLength = 0.0; //Length of the song which will increse over time.
    String[] noteDurations = { "q", "i", "i.", "s", "s.", "h"}; //Duration strings
    Random rand = new Random(); //Random variable
    Player player = new Player(); //The music player

    public Color[] musicShapes;

    public MusicGenerator(){
        //Nothing as of right now
    }



    //This will get the song beats which will play alongside the pattern and rhythm
    public String getMelody(){
        //Important song variables
        songLength = 0.0; //Refresh just in case
        Color[] colorOptions = {Color.AQUA, Color.BLACK, Color.BLUE,  //Correlates with noteNames
                Color.CYAN, Color.RED, Color.YELLOW,
                Color.GREEN, Color.GOLD, Color.GRAY,
                Color.LAVENDER, Color.DARKBLUE, Color.NAVAJOWHITE,
                Color.TAN, Color.MINTCREAM, Color.MAGENTA,
                Color.BLANCHEDALMOND, Color.PINK, Color.LIME};
        String[] noteNames = {"C", "D", "E", "G", "A"};
        int index = noteNames.length / 2; //Get the middle index
        double[] noteLength = {1.0, 0.5, 0.75, 0.25, 0.35, 2.0, 1.0, 0.5, 0.75, 0.25, 0.35, 2.0};
        musicShapes = new Color[(int) (baseLength * 4)]; //Not sure if the 1 is needed
        String melody = "V1 I[Piano] "; //melody which is a string, will be played with rhytm and beat
        int count = 0;
        while(songLength < (int)(baseLength)) { //Change i to whatever you'd like
            int noteDurationIndex = 0;
            try {
                System.out.println(songLength);
                noteDurationIndex = rand.nextInt(noteDurations.length);
                songLength += noteLength[noteDurationIndex];
                index = getIndex(index);
                melody += noteNames[index] + noteDurations[noteDurationIndex] + " ";
                musicShapes[count] = colorOptions[index]; //Add the color to musicShapes
                count += 1;
            } catch (IndexOutOfBoundsException e) {
                index = noteNames.length / 2;
                melody += noteNames[index] + noteDurations[noteDurationIndex] + " ";
                musicShapes[count] = colorOptions[index];
                count += 1;
            }
        }
        System.out.println(melody);
        return melody;
    }



    // Method to get a random index value based of a variance
    public int getIndex(int index){
        double indexVariance = rand.nextDouble();
        if (indexVariance < 0.15) //0 - 15
            index -= 2;
        else if (indexVariance < 0.45) { //15 - 45
            index -= 1; //Subtract by 1
        } else if (indexVariance < 0.55) { //45 - 55
            index = index; //Same thing
        } else if (indexVariance < 0.85) { //55 - 85
            index += 1;
        } else { // 85+
            index += 2;
        }
        return index;
    }


    public int getInt(){ //Lovely function which takes userinput and no is not an answer
        Scanner scan = new Scanner(System.in);
        int userInput;
        while(true){
            try{
                System.out.println("Enter repeat amount: \n");
                userInput = scan.nextInt(); //get user input
                return userInput; //If there was no error, return it.
            } catch(InputMismatchException e){
                System.out.println("Invalid input, try again");
                scan.nextLine(); //Clear the buffer i think
            }
        }
    }

    public Pattern getPattern(int repeat){
        baseLength = 0.0; //Refresh just in case
        String[] beats = {"C4maj7", "Ri", "D4min7", "Ri"}; //The 4 beats used for the pattern
        int baseRepeat = repeat; //The amount of times we are repeating because we are AWESOME
        Pattern p0 = new Pattern("T60 V0 I[Acoustic_Bass]"); //This is the pattern string instrument
        Pattern p;

        for(String beat : beats) {
            p = new Pattern(beat).repeat(baseRepeat); //This will be repeated x amount of times depending on the user
            p0.add(p);
            baseLength += baseRepeat;
        }
        baseLength -= 1;
        System.out.println(p0); //Print for debugging
        System.out.println(baseLength);
        return p0; //Return the pattern
    }

    //Will play the song and can be modified for later use
    public Pattern createSong(int repeat){
        Pattern song = getPattern(repeat); //get the pattern
        Rhythm rhythm = new Rhythm()
                .addLayer("O..oO...O..oOO..")
                .addLayer("..S...S...S...S.")
                .addLayer("````````````````")
                .addLayer("...............+");
        song.add(rhythm);
        song.add(getMelody()); //Add the song to the pattern
        return song;
        //player.play(song); //Uncomment to play
    }

    public void getJazz(){
        //Working on it
    }

    //Exports the song to a mid file.
    public void exportSong(Pattern song){
        try {
            File file = new File("/Users/chrisbenson/IdeaProjects/Music Test/src/test.mid");
            org.jfugue.midi.MidiFileManager.savePatternToMidi(song, file); //Convert to mid file
        }catch(IOException e){e.printStackTrace();}
    }

}
