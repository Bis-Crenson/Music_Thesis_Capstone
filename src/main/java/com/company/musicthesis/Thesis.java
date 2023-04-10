package com.company.musicthesis;

import graphics.core.*;
import graphics.core.MovementRig;
import graphics.extras.*;
import graphics.geometry.*;
import graphics.material.*;
import graphics.math.*;
import graphics.light.*;

import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import javax.sound.midi.*;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

public class Thesis extends Base
{
    public Mesh[] noteMeshArray;
    public int currentNoteIndex;
    public Vector noteOnColor  = new Vector(1.00, 0.50, 0.50);
    public Vector noteOffColor = new Vector(0.25, 0.00, 0.50);

    public Renderer renderer;
    public Scene scene;
    public Camera camera;
    public MovementRig rig;

    public static void main(String[] args) throws InvalidMidiDataException, MidiUnavailableException, IOException, InterruptedException {
        Base test = new Thesis();
        test.setWindowSize(800, 800);
        test.run();
    }

    public Pattern generateMusic()
    {
        // return new Pattern("T240 V0 I[Violin] D5w G5q A5q B5q"); // use different length pattern for testing staff width formula
        return new Pattern("T120 V0 I[Violin] D5h G5q A5q B5q C6q D6i D6i D6i D6i G6h");
    }

    public void exportMidiFile(Pattern pattern, String midiFileName)
    {
        try
        {
            File file = new File(midiFileName);
            MidiFileManager.savePatternToMidi(pattern, file); //Convert to mid file
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public List<NoteData> parseMidiFile(String midiFileName) throws InvalidMidiDataException, IOException
    {
        File file = new File(midiFileName);
        Pattern pattern = MidiFileManager.loadPatternFromMidi(file);

        String patternString = pattern.toString();
        System.out.println("String: " + patternString);

        String[] patternArray = patternString.split(" ");
        System.out.println( "Array: " + Arrays.toString(patternArray) );

        // pattern array may include some combination of
        //   tempo (T), voice number (V), and instrument name/number (I);
        // remove these from the data structure.

        List<NoteData> noteDataList = new ArrayList<NoteData>();

        for(int i = 0; i < patternArray.length; i++ )
        {
            char firstChar = patternArray[i].charAt(0);
            if (firstChar == 'T' || firstChar == 'V' || firstChar == 'I')
                continue; // skip to next iteration of for loop

            NoteData noteData = new NoteData( patternArray[i] );
            noteDataList.add( noteData );

            String test = noteData.letter + noteData.octave;
            System.out.println("Parsed Note: " + test);
        }

        return noteDataList;
    }

    /*
     * determine the index corresponding to the position of a note;
     * use for calculating Y-coordinate of note position on treble clef staff;
     * anything out of range returns -1.

     10 G6
      9 F6 ----------
      8 E6
      7 D6 ----------
      6 C6
      5 B5 ----------
      4 A5
      3 G5 ----------
      2 F5
      1 E5 ----------
      0 D5

    */
    public double trebleStaffPosition(NoteData noteData)
    {
        String[] noteNameArray = {"D5", "E5", "F5", "G5", "A5", "B5", "C6", "D6", "E6", "F6", "G6"};
        List<String> noteNameList = Arrays.asList(noteNameArray);
        String noteName = noteData.letter + noteData.octave;
        return noteNameList.indexOf(noteName);
    }

    public void initialize()
    {
        // generate music
        Pattern musicPattern = generateMusic();

        // export MIDI file
        String midiFileName = "midi/test2.mid";
        exportMidiFile(musicPattern, midiFileName);

        // re-import MIDI file + data in a user-friendly format:
        List<NoteData> noteDataList = new ArrayList<NoteData>();
        try
        {
            noteDataList = parseMidiFile(midiFileName);
        }
        catch (Exception ex) // Invalid MIDI data exception
        {
            ex.printStackTrace();
        }

        // Set up the basics for 3D graphics:
        renderer = new Renderer();
        scene = new Scene();
        camera = new Camera();

        rig = new MovementRig(20, 45);
        rig.attach(camera);
        rig.setPosition(new Vector(0.5, 1, 4));
        scene.add(rig);

        camera.setPosition(new Vector(2, -2, 30));

        // adding lights; required IF using LambertMaterial
        Light ambientLight = new AmbientLight( new Vector(0.5, 0.5, 0.5) );
        scene.add(ambientLight);
        Light directionalLight = new DirectionalLight( new Vector(0.8, 0.8, 0.8), new Vector(0.0, -1.0, -0.1) );
        scene.add(directionalLight);

        // Stem's visualization helpers
        scene.add( new AxesHelper(1, 1) );

        // ----------------
        // | Staff Meshes |
        // ----------------

        double noteWidth   = 1; // the width of a note, also the spacing between notes
        double noteMarginX = 4;
        double noteCount   = noteDataList.size();

        double staffWidth  = noteMarginX + noteCount * noteWidth * 2;
        double staffHeight = 0.2;
        double staffDepth  = 0.01; // super thin, so that noteMeshes which intersect the line overlap it
        double staffVerticalSpacing = 1.0; // chosen based on dimensions of noteMesh

        Geometry staffGeometry = new BoxGeometry(staffWidth, staffHeight, staffDepth);
        Texture  staffTexture  = new Texture("images/grid.png");
        Material staffMaterial = new TextureMaterial(staffTexture);

        // for convenience, position bottom-left corner of staff at (0,0,0);
        //   requires shifting staffMesh origin (center) to the right.
        for (int i = 0; i < 5; i++)
        {
            Mesh staffMesh = new Mesh(staffGeometry, staffMaterial);
            staffMesh.setPosition( new Vector(staffWidth/2, i * staffVerticalSpacing, 0) );
            scene.add(staffMesh);
        }

        // --------------------
        // | Treble Clef Mesh |
        // --------------------

        Geometry trebleClefGeometry = new OBJGeometry("src/main/java/models/TrebleClef01.obj");
        Texture  trebleClefTexture  = new Texture("src/main/java/images/lava.jpg");
        // Material trebleClefMaterial = new TextureMaterial(trebleClefTexture);
        Material trebleClefMaterial = new LambertMaterial(null); // null -or- Texture;
        trebleClefMaterial.uniforms.get("baseColor").data = noteOffColor;
        Mesh trebleClefMesh = new Mesh(trebleClefGeometry, trebleClefMaterial);
        trebleClefMesh.rotateY(Math.PI/2, true);
        trebleClefMesh.translate(0, -1.5, 0, true);
        scene.add(trebleClefMesh);

        // ---------------
        // | Note Meshes |
        // ---------------

        // Improved QuarterNote.obj model:
        //   - lower left corner at (0, 0, 0)
        //   - dimensions approx. 1 * 2 * 0.1
        Geometry noteGeometry = new OBJGeometry("src/main/java/models/WholeNote.obj");

        // array is defined at class level for global access
        noteMeshArray = new Mesh[noteDataList.size()];

        for(int i = 0; i < noteDataList.size(); i++)
        {
            Material noteMaterial = new SurfaceMaterial();
            noteMaterial.uniforms.get("baseColor").data = noteOffColor;

            Mesh noteMesh = new Mesh(noteGeometry, noteMaterial);
            noteMesh.rotateY(Math.PI/2, true);

            NoteData data = noteDataList.get(i);
            double x = 4 + 2*i;
            double y = staffHeight + trebleStaffPosition(data) * staffVerticalSpacing/2.0 - 1;
            double z = 0;
            noteMesh.setPosition( new Vector(x,y,z) );

            noteMeshArray[i] = noteMesh;
            scene.add(noteMesh);
        }

        // -------------------
        // | Background Mesh |
        // -------------------

        Geometry skyGeometry = new SphereGeometry();
        Texture  skyTexture  = new Texture("images/grid.png");
        Material skyMaterial = new TextureMaterial(skyTexture);
        Mesh     skyMesh     = new Mesh(skyGeometry, skyMaterial);
        skyMesh.scale(500, true);
        scene.add(skyMesh);

        // -------------------
        // | Play the music! |
        // -------------------

        currentNoteIndex = 0;
        try
        {
            File midiFile = new File(midiFileName);
            Sequence sequence = MidiSystem.getSequence(midiFile);
            Sequencer sequencer = MidiSystem.getSequencer();
            Transmitter transmitter = sequencer.getTransmitter();
            Receiver receiver = new CustomReceiver();
            transmitter.setReceiver(receiver);
            sequencer.open();
            sequencer.setSequence(sequence);
            sequencer.start();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    // written as inner class, since only used here
    class CustomReceiver implements Receiver
    {
        public CustomReceiver() {    }

        public static final int NOTE_ON  = 0x90;
        public static final int NOTE_OFF = 0x80;
        @Override
        public void send(MidiMessage message, long timeStamp)
        {
            if (message instanceof ShortMessage)
            {
                ShortMessage sm = (ShortMessage) message;
                if (sm.getCommand() == NOTE_ON)
                {
                    noteMeshArray[currentNoteIndex].material.uniforms.get("baseColor").data = noteOnColor;
                }
                else if (sm.getCommand() == NOTE_OFF)
                {
                    noteMeshArray[currentNoteIndex].material.uniforms.get("baseColor").data = noteOffColor;
                    currentNoteIndex++;
                }
            }
            else
            {
                System.out.println("Other message: " + message.getClass());
            }
        }

        @Override
        public void close()
        { }
    }

    public void update()
    {
        if (input.isKeyDown(GLFW_KEY_SPACE))
        {
            System.out.println("Hello!");
        }

        rig.update(input, deltaTime);   // update camera controls
        renderer.render(scene, camera); // render the 3D scene
    }

}
