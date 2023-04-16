package com.company.musicthesis;

import javafx.scene.paint.Color;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.jfugue.rhythm.Rhythm;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class MusicGeneratorEnhanced {
    double baseLength = 0.0; //Length of the total notes, global variable
    double songLength = 0.0; //Length of the song which will increse over time.
    int repeatValue = 0;
    String[] noteDurations = {"i"}; //Duration strings
    Random rand = new Random(); //Random variable
    Player player = new Player(); //The music player

    public int startingIndex;

    public String[] noteNamesTest = {"B5", "C5", "D5", "E5", "F5", "G5", "A6", "B6", "C6", "D6"};
    public String[] beatsTest = {"Bb4W", "A4W", "G3W", "F3W", "Eb3W","D3W", "C3W"}; //The beats used for the pattern

    //Array of notes, name is the melody and each note has a true/false value depending on importance
    public Note[] melodyB4 = {new Note("Bb5", true), new Note("C5", false), new Note("D5", true),
            new Note("Eb5", false), new Note("F5", true), new Note("G5", false),
            new Note("Bb6", true), new Note("C6", false), new Note("D6", true) };

    public Note[] melodyA4 = { new Note("C5", true), new Note("F5",true),
            new Note("A6",true), new Note("C6",true),
            new Note("D5", false), new Note("Eb5", false), new Note("G5", false), new Note("D6", false) };

    public Note[] melodyG3 = { new Note("Bb5", true), new Note("Eb5", true),
            new Note("G5", true) , new Note("Bb6", true) , new Note("C6", false),
            new Note("D6", false) , new Note("C5", false) , new Note("D5", false) , new Note("F5", false) ,
            new Note("A6", false) };

    public Note[] melodyF3 = { new Note("B5", true), new Note("D5", true) ,
            new Note("F5", true) , new Note("Bb6", true) , new Note("D6", true), new Note("C5", false),new Note("Eb5", false) ,
            new Note("G5", false) , new Note("A6", false) ,new Note("C6", false )};  //GOOD

    public Note[] melodyE3 = { new Note("Eb5", true) , new Note("G5", true),
            new Note("Bb5", false) ,new Note( "Bb6", true) , new Note("C5", false) , new Note("F5", false) , new Note("A6", false) ,
            new Note("C6",false ) , new Note("D6", false) };

    public Note[] melodyD3 = { new Note("Bb5", true) , new Note("D5", true),
            new Note("F5", true) , new Note("Bb6", true), new Note("D6",true ) , new Note("C5",false ) , new Note("G5",false ) ,
            new Note("A6",false ) , new Note("C6",false )};

    public Note[] melodyC3 = { new Note("Bb5", true ) , new Note("F5", false),
            new Note("G5",false ) , new Note("Bb6", true), new Note("C5", false ), new Note("D5", false), new Note("Eb5", true),
            new Note("A6", false),  new Note("C6", false) , new Note("D6", false)};

    public Note[][] melodies = {melodyB4, melodyA4, melodyG3, melodyF3, melodyE3, melodyD3, melodyC3};
    //Melody without bad notes and index values



/*
Ideas:

Create a method which uses a for each which will populate each object with values per iteration
If the note is special, add 10 to it.


Probability will be tricky...

 */

//            for(int i = 0; i < repeatValue * 8; i++){ //4 * 2 is 8 which is a whole note.
//                if(songLength >= baseLength)
//                    break; //End the loop
//                double variance = rand.nextDouble(); //Get a random double between 0 and 1
//                if(variance <= 0.70) //If true, add first x value to array
//                    melody += melodyArray[rand.nextInt(1, Integer.parseInt(melodyArray[0]) + 1)] + "i ";
//                else
//                    melody += melodyArray[rand.nextInt(Integer.parseInt(melodyArray[0]) + 1, melodyArray.length)] + "i ";
//                songLength += 0.125;




    public Color[] musicShapes;

    public MusicGeneratorEnhanced(){
        //Nothing as of right now
    }



    //This will get the song beats which will play alongside the pattern and rhythm
    public String getMelody(){
        //Important song variables
        songLength = 0.0; //Refresh just in case
        String[] noteNames = {"B5", "C5", "D5", "E5", "F5", "G5", "A6", "B6", "C6", "D6"};
        double[] noteLength = {1}; //Just 8th notes
        String melody = "T120 V1 I[Piano] "; //melody which is a string, will be played with rhythm and beat
        int count = 0;
        for(Note[] melodyArray : melodies){ //Melodyarray will be the entire array of notes per melody.
            startingIndex  = rand.nextInt(melodyArray.length); //Get a random index value based off the length of the given note array, will be the starting value and will change throughout
            for(int i = 0; i < repeatValue * 4; i++){
                melody += getNote(melodyArray, startingIndex);
            }
        }
        System.out.println(melody);
        return melody;
    }

    public String getNote(Note[] specificNoteArray, int startingIndex){
        int increment = 1; //Start at 1 and make continue until isZero is false
        int leftScoreDivider = 1; //Same as increment, but accounts for possible eliminations not included by default in the bass
        int rightScoreDivider = 1; //Same as increment, but accounts for possible eliminations not included by default in the bass
        while(increment < 10){ //While there is a zero value inside of the array, popualate with zeros
            //TO THE LEFT
            try { //try catch in case we are too far to the left
                if (specificNoteArray[startingIndex].getName().contains("Bb") && specificNoteArray[startingIndex - increment].getName().contains("A")){ //This should work, might be tedious though, but will work
                    specificNoteArray[startingIndex - increment].setVal(0); //Needed to reset the value to 0
                }
                else if (specificNoteArray[startingIndex].getName().contains("Eb") && specificNoteArray[startingIndex - increment].getName().contains("D")){ //This should work, might be tedious though, but will work
                    specificNoteArray[startingIndex - increment].setVal(0); //Needed to reset the value to 0
                }
                else if (specificNoteArray[startingIndex].getName().contains("A") && specificNoteArray[startingIndex - increment].getName().contains("BB")){ //This should work, might be tedious though, but will work
                    specificNoteArray[startingIndex - increment].setVal(0); //Needed to reset the value to 0
                }
                else if (specificNoteArray[startingIndex].getName().contains("D") && specificNoteArray[startingIndex - increment].getName().contains("EB")){ //This should work, might be tedious though, but will work
                    specificNoteArray[startingIndex - increment].setVal(0); //Needed to reset the value to 0
                }
                else {
                    int bonus = specificNoteArray[startingIndex - increment].getBonusValue(); //get the bonus of said note, if any
                    specificNoteArray[startingIndex - increment].setVal(100 / (leftScoreDivider + 1) + bonus);
                    leftScoreDivider += 1; //Only increase by 1 if and only if there was a good note
                }
            } catch(IndexOutOfBoundsException e) {
                ; //Do nothing and continue
            }

            //TO THE RIGHT
            try { //try catch in case we are too far to the right
                if (specificNoteArray[startingIndex].getName().contains("Bb") && specificNoteArray[startingIndex + increment].getName().contains("A")){ //This should work, might be tedious though, but will work
                    specificNoteArray[startingIndex + increment].setVal(0); //Needed to reset the value to 0
                }
                else if (specificNoteArray[startingIndex].getName().contains("Eb") && specificNoteArray[startingIndex + increment].getName().contains("D")){ //This should work, might be tedious though, but will work
                    specificNoteArray[startingIndex + increment].setVal(0); //Needed to reset the value to 0
                }
                else if (specificNoteArray[startingIndex].getName().contains("A") && specificNoteArray[startingIndex + increment].getName().contains("Bb")){ //This should work, might be tedious though, but will work
                    specificNoteArray[startingIndex + increment].setVal(0); //Needed to reset the value to 0
                }
                else if (specificNoteArray[startingIndex].getName().contains("D") && specificNoteArray[startingIndex + increment].getName().contains("Eb")){ //This should work, might be tedious though, but will work
                    specificNoteArray[startingIndex + increment].setVal(0); //Needed to reset the value to 0
                }
                else {
                    int bonus = specificNoteArray[startingIndex + increment].getBonusValue(); //get the bonus of said note, if any
                    specificNoteArray[startingIndex + increment].setVal(100 / (rightScoreDivider + 1) + bonus);
                    rightScoreDivider += 1; //Only increase by 1 if and only if there was a good note
                }
            } catch (IndexOutOfBoundsException e){
                ; //Do nothing and continue
            }
            increment += 1; //This will eventually stop once isZero is false
//            if(scoreDivider < 1)
//                scoreDivider = 1; //We always want this to be greater than 0, not sure if this is needed yet
            specificNoteArray[startingIndex].setVal(0); //Always make the startingIndex 0
        } //End while loop
        System.out.println("Starting note(Will be 0): " + specificNoteArray[startingIndex].getName());
        for(Note specificNote: specificNoteArray) //For debugging
            System.out.println("Note: " + specificNote.getName() + ", Value: " + specificNote.getVal());
        /*
        INSPIRATION FROM STACKOVERFLOW, O(1) time
        Say you have 100 D5 notes, add those notes to an note array, same goes for every note
        Then randomly pick it!!! That would work!!!
         */
        Note[] bigNoteArray = new Note[totalNotes(specificNoteArray)]; //Get total length for array
        int count = 0; //Probably other way to do this, but need for testing

        for(Note specificNote: specificNoteArray){
            for(int i = 0; i < specificNote.getVal(); i++) {
                bigNoteArray[count] = specificNote; //Add repeated note.
                count += 1; //Will keep going until for each is done
            }
        }
        int randomlyPickedNote = rand.nextInt(bigNoteArray.length);
        for(int i = 0; i < specificNoteArray.length; i++){
            if(Objects.equals(bigNoteArray[randomlyPickedNote].getName(), specificNoteArray[i].getName())){ //if two strings are equal
                this.startingIndex = i;  //Set the startingIndex to that value
                break;
            }
        }
        return bigNoteArray[randomlyPickedNote].getName() + "h ";
    } //End method

    //Method for adding every note value and returning it.
    public int totalNotes(Note[] specificNoteArray){
        int valueSum = 0;
        for(Note specificNote : specificNoteArray)
            valueSum += specificNote.getVal(); //Add each note value
        return valueSum; //Return final sum
    }


    //Method idea for checking if any note val is zdero
//    public boolean isZero(note[] specificNoteArray){
//        for(note specificNote : specificNoteArray) {
//            if(specificNote.getVal() == 0) //if the value is zero, array has a zero value so return true
//                return true;
//        }
//        return false; //If nothing is true, can only be false
//    }

    // Method to get a random index value based of a variance
//    public int getIndex(int index){
//        double indexVariance = rand.nextDouble();
//        if (indexVariance < 0.15) //0 - 15
//            index -= 2;
//        else if (indexVariance < 0.45) { //15 - 45
//            index -= 1; //Subtract by 1
//        } else if (indexVariance < 0.55) { //45 - 55
//            index = index; //Same thing
//        } else if (indexVariance < 0.85) { //55 - 85
//            index += 1;
//        } else { // 85+
//            index += 2;
//        }
//        return index;
//    }




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
        repeatValue = repeat;
        baseLength = 0.0; //Refresh just in case
        String[] beats = {"Bb3W", "A3W", "G3W", "F3W", "Eb3W","D3W", "C3W"}; //The 4 beats used for the pattern
        int baseRepeat = repeat; //The amount of times we are repeating because we are AWESOME
        Pattern p0 = new Pattern("100 V0 I[Violin]"); //This is the pattern string instrument, t is temp, [] is the instrument.
        Pattern p;

        for(String beat : beats) {
            p = new Pattern(beat).repeat(baseRepeat); //This will be repeated x amount of times depending on the user
            p0.add(p);
            baseLength += baseRepeat;
        }
        baseLength -= 1;
        //System.out.println(p0); //Print for debugging
        System.out.println(baseLength);
        return p0; //Return the pattern
    }

    //Will play the song and can be modified for later use
    public Pattern createSong(int repeat){
      Pattern song = getPattern(repeat); //get the pattern
//        Rhythm rhythm = new Rhythm()
//                .addLayer("O..oO...O..oOO..")
//                .addLayer("..S...S...S...S.")
//                .addLayer("````````````````")
//                .addLayer("...............+");
//        song.add(rhythm);
//        System.out.println(song);
//        song.add(getMelody()); //Add the song to the pattern
//        player.play(song); //Uncomment to play
//        return song;
        Pattern songNoRhythm = new Pattern(getMelody()); //This has no Rhythm
       // System.out.println(songNoRhythm);
//        song.add(getMelody()); //Add the song to the pattern
        //player.play(songNoRhythm); //Uncomment to play
        return songNoRhythm;
    }

    public void getJazz(){
        //Working on it
    }

    //Exports the song to a mid file.
    public void exportSong(Pattern song){
        try {
            File file = new File("/Users/chrisbenson/IdeaProjects/Music Thesis/src/test.mid");
            org.jfugue.midi.MidiFileManager.savePatternToMidi(song, file); //Convert to mid file
        }catch(IOException e){e.printStackTrace();}
    }

    //Testing implementation of Nov 10 meeting
    public void testing(){
        System.out.println("test");
    }

}
