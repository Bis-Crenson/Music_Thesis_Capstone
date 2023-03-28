package com.company.musicthesis;

import graphics.core.*;
import graphics.extras.OBJGeometry;
import graphics.geometry.Geometry;
import graphics.geometry.RectangularPrismGeometry;
import graphics.geometry.SphereGeometry;
import graphics.material.Material;
import graphics.material.SurfaceMaterial;
import graphics.material.TextureMaterial;
import graphics.math.Vector;
import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import javax.sound.midi.*;

import java.io.File;
import java.io.IOException;
import java.util.Random;


public class Thesis extends Base {
    public static CustomReceiver synthRcvr = new CustomReceiver();//Create a customReceiver object
    public static Transmitter seqTrans;
    public static Synthesizer synth;
    public static Sequencer sequencer;
    public static Sequence sequence;

    public static Player player = new Player(); //The music player

    SurfaceMaterial[] materialArray;

    public Renderer renderer;
    public Scene scene;
    public Camera camera;
    public Mesh mesh, sky;
    public MovementRig rig;

    public Mesh tCleff, staff, staff2, staff3, staff4, staff5;
    public Material world, matt, trebleMatt;
    public Geometry block1, trebleCleff, round, sphere, sphere2, sphere3, sphere4, musicNote;

    public int currentKeyID;
    public int previousKeyID = -1; //These two values will be used to change the colors of the note, -1 for the first cycle of update

    public int meshIncrementer = 0; //This will work with currentKeyID/previousKeyID, will change the color of a note when a new key plays

    // Create object of Music Mathin
    public MusicMathin mathy = new MusicMathin();

    public Mesh[] meshArray;

    public float turnSpeed;

    public Random rand = new Random();


    public static void main(String[] args) throws InvalidMidiDataException, IOException, MidiUnavailableException, InterruptedException {

        //Messing around 3(IT WORKED)

        Base test = new Thesis();
        test.setWindowSize(800, 800);
        test.run();
//        while(sequencer.isOpen()){
//            System.out.println(synthRcvr.returnKey());
//            Thread.sleep(10); //NOT NEEDED
//        }
    }

    public void initialize() throws InvalidMidiDataException, IOException, MidiUnavailableException, InterruptedException { //I swear if this works.... Imma be so HAPPPPPPPPPY, UPDATE I AM HAPPPPPPPPY
        // Set up the basics:
        renderer = new Renderer();
        scene = new Scene();
        camera = new Camera();

        rig = new MovementRig(20, 45);
        rig.attach(camera);
        rig.setPosition(new Vector(0.5, 1, 4));
        scene.add(rig);

        camera.setPosition(new Vector(2, -2, 30));

        int songLength = noteArrayWithoutLength().length; //Return the length of the song

        //Faiths declaration
        // Original coordinate
        double x = songLength; // represents x coordinate value of original staff placement
        double y = 0; // represents y coordinate value of original staff placement
        double z = 0; // represents z coordinate value of original staff placement

        // Staff
        double widthOfStaff = 30 * songLength / 8.0 ;  // dx
        double heightOfStaff = .1; // dy
        double depthOfStaff = .1; // dz
        double spaceOfStaff = 1.15; // dh
        double staffNumber = 0;

        // Note
        double spaceOfNote = 1; // ds
        double radiusOfNote = 1; // r
        double noteNumber = 0; // note number (note 1, note2, note3, note4, etc.)
        //End

//———————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————
        // | Staff |
        // ——————————
        // Geometric Shape
        Geometry block1 = new RectangularPrismGeometry(widthOfStaff, heightOfStaff, depthOfStaff);
        Geometry block2 = new RectangularPrismGeometry(widthOfStaff, heightOfStaff, depthOfStaff);
        Geometry block3 = new RectangularPrismGeometry(widthOfStaff, heightOfStaff, depthOfStaff);
        Geometry block4 = new RectangularPrismGeometry(widthOfStaff, heightOfStaff, depthOfStaff);
        // Material
        Texture tyeDye = new Texture("src/main/java/images/istockphoto-184372785-170667a.jpg");
        Material matt = new TextureMaterial(tyeDye);
        // Create Mesh
        staff = new Mesh(block1, matt);
        staff2 = new Mesh(block2, matt);
        staff3 = new Mesh(block3, matt);
        staff4 = new Mesh(block4, matt);
        staff5 = new Mesh(block4, matt);
        // Find the position
        staff.setPosition(new Vector(x, mathy.findYval(1, y, spaceOfStaff, heightOfStaff), 0));
        staff2.setPosition(new Vector(x, mathy.findYval(2,y, spaceOfStaff, heightOfStaff), 0));
        staff3.setPosition(new Vector(x, mathy.findYval(3,y, spaceOfStaff, heightOfStaff), 0));
        staff4.setPosition(new Vector(x, mathy.findYval(4,y, spaceOfStaff, heightOfStaff), 0));
        staff5.setPosition(new Vector(x, mathy.findYval(5,y, spaceOfStaff, heightOfStaff), 0));
        // Add Mesh to Scene
        scene.add(staff);
        scene.add(staff2);
        scene.add(staff3);
        scene.add(staff4);
        scene.add(staff5);

        // ———————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————

        // Notes:
        // Geometric Shape
//———————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————
        // | Notes |
        // ——————————
        // Geometric Shape
        Geometry sphere = new OBJGeometry("src/main/java/models/WholeNote.obj");
        // Create material
        String[] noteArray = noteArrayWithoutLength();
        materialArray = new SurfaceMaterial[noteArray.length - 1];
        for(int i = 0; i < materialArray.length; i++){ //Create array of surface materials.
            materialArray[i] = new SurfaceMaterial();
            materialArray[i].uniforms.get("baseColor").data = new Vector(0.0, 0.0, 0.0);
            Mesh note = new Mesh(sphere, materialArray[i]);
            note.scale(.25, true);
            note.rotateY(Math.PI/2, true);
            double x1 = mathy.findXval( i, spaceOfNote, -5, radiusOfNote);
            double noteIndex = mathy.whichStaff(noteArray[i]);
            System.out.println(noteIndex);
            double y1 = mathy.exactStaffSpacing(noteIndex, heightOfStaff, spaceOfStaff, y) - 0.5;
            double z1 = 1.75;
            note.setPosition(new Vector(x1, y1, z1));
            scene.add(note);
        }

        // Create Mesh
        //C5W D5W E5W E4W F4W G4W
        // Scale Mesh
        // Find note height on y-axis && Find the position
        // Add Mesh to Scene


        // ———————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————

        // ———————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————
        // | Cleff |
        // ——————————
        // Geometric Shape
        Geometry trebleCleff = new OBJGeometry("src/main/java/images/ClefNote.obj");
        // Material
        Material trebleMatt = new TextureMaterial(new Texture("src/main/java/images/nightStar2.png"));
        // Add to scene & scale to size
        tCleff = new Mesh(trebleCleff, trebleMatt);
        tCleff.translate(-11.2, -2.823832783728, 1, true);
        tCleff.scale(2.5, true);
        tCleff.rotateY(45, true);
        scene.add(tCleff);

        // ———————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————
        // | Background |
        // ——————————————
        // Geometric Shape
        Geometry round = new SphereGeometry();
        // Material
        Material world = new TextureMaterial(new Texture("src/main/java/images/yeet.jpg"));
        // Create Mesh
        sky = new Mesh(round, world); // Create sky object
        // Scale Mesh
        sky.scale(500, true); // Scale it for the world to see
        // Add Mesh to Scene
        scene.add(sky); //Add sky

        turnSpeed = (float)Math.toRadians(180);//Used for box rotation in future

        //"src/main/java/images/sky-earth.jpg"
        Thread.sleep(500);
        sequencer = MidiSystem.getSequencer();
        sequence = MidiSystem.getSequence(new File("src/test.mid"));
        Sequencer sequencer = MidiSystem.getSequencer();
        seqTrans = sequencer.getTransmitter();
        seqTrans.setReceiver((Receiver) synthRcvr); //Cast it
        sequencer.open(); //Open this in the initialize class, will always stay open
        sequencer.setSequence(sequence);
        sequencer.start();
    }

    public void update() {
        float turnAmount = turnSpeed * deltaTime; //Get the Turn amount



         currentKeyID = synthRcvr.returnKey(); //Key as soon as the update method starts
//        System.out.println("Current Key: " + currentKeyID +
//                "\nPrevious Key: " + previousKeyID);

        if ((meshIncrementer >= materialArray.length) && (currentKeyID != previousKeyID)) {
            materialArray[meshIncrementer - 1].uniforms.get("baseColor").data = new Vector(0.0, 0.0, 0.0); //Change last note to white since this is the end of the array
            meshIncrementer = 0; //Go back to the beginning.
        }

        if(currentKeyID != previousKeyID){ //If true, change the color of the current note and revert previous note to white.
            materialArray[meshIncrementer].uniforms.get("baseColor").data = new Vector(0.5, 0.5, 1); //Change color to that
           try {
               materialArray[meshIncrementer - 1].uniforms.get("baseColor").data = new Vector(0.0, 0.0, 0.0); //Change color to white
               meshIncrementer += 1; //Increase to next note
           } catch (IndexOutOfBoundsException e){
               meshIncrementer += 1; //If meshIncrementer is 0, catch error
           }
        }

//        musicBox.material.uniforms.get("baseColor").data = new Vector((synthRcvr.returnKey() % 7 )/10.0,
//                (synthRcvr.returnKey() % 10 )/10.0,
//                (synthRcvr.returnKey() % 5 )/10.0); //For changing colors via music



        previousKeyID = synthRcvr.returnKey(); //Key after the update method is finished

        rig.update(input, deltaTime);
        renderer.render(scene, camera); //Do the update magic
    }

    public String[] noteArrayWithoutLength() throws InvalidMidiDataException, IOException  {
        Pattern sheetMusic = MidiFileManager.loadPatternFromMidi(new File("src/test.mid")); //Open the music file
        String[] fullNoteSheet = sheetMusic.toString().split(" "); //Separate by spaces
        String[] finalNotes = new String[fullNoteSheet.length - 1]; //This will be the final notes with only notes
        for(int i = 3; i < fullNoteSheet.length; i++ ){
            finalNotes[i - 3] = fullNoteSheet[i].substring(0, fullNoteSheet[i].length() - 1); //Remove the note length
            finalNotes[i - 3] = finalNotes[i-3].replace("b", ""); //Remove the lowercase b
            System.out.println("For loop Note: " + finalNotes[i-3]);
        }

        return finalNotes; //Remove later
    }
}




//       MusicGeneratorEnhanced music = new MusicGeneratorEnhanced();
////
////        Pattern song = music.createSong(2); //Get song
//          Pattern song = new Pattern();
//          song.add("20 V0 I[Violin] Bb3w A3w G3w F3w Eb3w D3w C3w V1 I[Piano] Bb A G F Eb D C F");
//
//
//        music.exportSong(song);

//        // TODO convert the event into a readable/desired output
//        ControllerEventListener controllerEventListener = System.out::println;
//
//
//        Sequencer sequencer = MidiSystem.getSequencer();
//        int[] controllersOfInterest = { 1, 2, 4, 8, 16};
//        sequencer.setSequence(MidiSystem.getSequence(new File("src/test.mid")));
//        sequencer.open();
//        sequencer.addControllerEventListener(controllerEventListener, controllersOfInterest);
//        sequencer.start();
////        while(sequencer.isRunning()) {
////            System.out.println(sequencer.c);
////        }
//        //sequencer.close();

//test 2
//        MidiParser parser = new MidiParser(); //Parser
//        StaccatoParserListener listener = new StaccatoParserListener(); //Staccato?
//        parser.addParserListener(listener); //Add the listener here
//        parser.parse(MidiSystem.getSequence(new File("src/test.mid"))); //Get the parser to parse everywhere
//        Pattern staccatoPattern = listener.getPattern();
//        System.out.println(staccatoPattern); //Prints out the notes
//
//        Player player = new Player(); //Create music player
//        player.play(staccatoPattern); //Play the cool notes.
//Test 1
//        Pattern sheetMusic = MidiFileManager.loadPatternFromMidi(new File("src/test.mid"));
//        System.out.println(sheetMusic); //This will return the notes of a midi file
// Initialize:

