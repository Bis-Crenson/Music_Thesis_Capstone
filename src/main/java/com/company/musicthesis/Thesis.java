package com.company.musicthesis;

import graphics.core.Mesh;
import graphics.core.Texture;
import graphics.extras.*;


import graphics.extras.AxesHelper;
import graphics.geometry.SphereGeometry;
import graphics.material.TextureMaterial;
import graphics.math.Vector;
import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;

import javax.sound.midi.*;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public class Thesis extends graphics.core.Base
{
    public graphics.core.Mesh[] noteMeshArray;
    public int currentNoteIndex;
    public graphics.math.Vector noteOnColor  = new graphics.math.Vector(1.00, 0.50, 0.50);
    public graphics.math.Vector noteOffColor = new graphics.math.Vector(0.25, 0.00, 0.50);

    public graphics.core.Renderer renderer;
    public graphics.core.Scene scene;
    public graphics.core.Camera camera;
    public graphics.core.MovementRig rig;

    public Mesh snowFall;

    public static void main(String[] args) throws InvalidMidiDataException, MidiUnavailableException, IOException, InterruptedException {
        graphics.core.Base test = new Thesis();
        //Base.windowWidth = 1024;
        //Base.windowHeight = 512;
        test.setWindowSize(1024, 1024);
        test.run();
        // test.run(1024, 512);
    }

    public Pattern generateMusic()
    {
        // return new Pattern("T240 V0 I[Violin] D5w G5q A5q B5q"); // use different length pattern for testing staff width formula
        //return new Pattern("T120 V0 I[Violin] D5h G5q A5q B5q C6q D6i D6i D6i D6i G6h");
        MusicGeneratorEnhanced musicGen = new MusicGeneratorEnhanced();
        return musicGen.createSong(1);
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
        String[] noteNameArray = {"B5", "C5", "D5", "E5", "F5", "G5", "A6", "B6", "C6", "D6" };
        List<String> noteNameList = Arrays.asList(noteNameArray);
        String noteName = noteData.letter + noteData.octave;
        return noteNameList.indexOf(noteName);
    }

    public void initialize()
    {
        // generate music
        Pattern musicPattern = generateMusic();

        // export MIDI file
        String midiFileName = "src/test.mid";
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
        renderer = new graphics.core.Renderer();
        scene = new graphics.core.Scene();
        camera = new graphics.core.Camera();


        rig = new graphics.core.MovementRig(20, 45);
        rig.attach(camera);
        rig.setPosition(new graphics.math.Vector(0.5, 1, 4));
        scene.add(rig);

        camera.setPosition(new graphics.math.Vector(2, -2, 30));

//        // adding lights; required IF using LambertMaterial
//        Light ambientLight = new AmbientLight( new graphics.math.Vector(0.5, 0.5, 0.5) );
//        scene.add(ambientLight);
//        Light directionalLight = new DirectionalLight( new graphics.math.Vector(0.8, 0.8, 0.8), new graphics.math.Vector(0.0, -1.0, -0.1) );
//        scene.add(directionalLight);

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

        graphics.geometry.Geometry staffGeometry = new graphics.geometry.BoxGeometry(staffWidth, staffHeight, staffDepth);
        graphics.core.Texture staffTexture  = new graphics.core.Texture("images/grid.png");
        graphics.material.Material staffMaterial = new graphics.material.TextureMaterial(staffTexture);

        // for convenience, position bottom-left corner of staff at (0,0,0);
        //   requires shifting staffMesh origin (center) to the right.
        for (int i = 0; i < 5; i++)
        {
            graphics.core.Mesh staffMesh = new graphics.core.Mesh(staffGeometry, staffMaterial);
            staffMesh.setPosition( new graphics.math.Vector(staffWidth/2, i * staffVerticalSpacing, 0) );
            scene.add(staffMesh);
        }

        // --------------------
        // | Treble Clef Mesh |
        // --------------------

        graphics.geometry.Geometry trebleClefGeometry = new graphics.extras.OBJGeometry("src/main/java/models/TrebleClef.obj");
        graphics.core.Texture trebleClefTexture  = new graphics.core.Texture("src/main/java/images/lava.jpg");
        graphics.material.Material trebleClefMaterial = new graphics.material.TextureMaterial(trebleClefTexture);
       // Material trebleClefMaterial = new LambertMaterial(null); // null -or- Texture;
        trebleClefMaterial.uniforms.get("baseColor").data = noteOffColor;
        graphics.core.Mesh trebleClefMesh = new graphics.core.Mesh(trebleClefGeometry, trebleClefMaterial);
        trebleClefMesh.rotateY(Math.PI/2, true);
        trebleClefMesh.translate(0, -1.5, 0, true);
        scene.add(trebleClefMesh);

        // ---------------
        // | Note Meshes |
        // ---------------

        // Improved QuarterNote.obj model:
        //   - lower left corner at (0, 0, 0)
        //   - dimensions approx. 1 * 2 * 0.1
        graphics.geometry.Geometry noteGeometry = new graphics.extras.OBJGeometry("src/main/java/models/QuarterNote.obj");

        // array is defined at class level for global access
        noteMeshArray = new graphics.core.Mesh[noteDataList.size()];

        for(int i = 0; i < noteDataList.size(); i++)
        {
            graphics.material.Material noteMaterial = new graphics.material.SurfaceMaterial();
            noteMaterial.uniforms.get("baseColor").data = noteOffColor;

            graphics.core.Mesh noteMesh = new graphics.core.Mesh(noteGeometry, noteMaterial);
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

        graphics.geometry.Geometry skyGeometry = new SphereGeometry();
        graphics.core.Texture skyTexture  = new graphics.core.Texture("src/main/java/images/StormySkybox.jpeg");
        graphics.material.Material skyMaterial = new TextureMaterial(skyTexture);
        graphics.core.Mesh skyMesh     = new graphics.core.Mesh(skyGeometry, skyMaterial);
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


        //snow test
        snowFall = new ParticleSystem( 1000000,
                new Texture("src/main/java/images/fluffy.png") );
        snowFall.material.uniforms.get("minPosition").data = new Vector(-50, 5, -50);
        snowFall.material.uniforms.get("maxPosition").data = new Vector( 150, 5,  50);
        snowFall.material.uniforms.get("minSpeed").data = 0.5f;
        snowFall.material.uniforms.get("maxSpeed").data = 1.5f;
        snowFall.material.uniforms.get("minAge").data = 6.0f;
        snowFall.material.uniforms.get("maxAge").data = 18.0f;
        // slightly different fall directions
        snowFall.material.uniforms.get("minGravity").data = new Vector(-0.1, -1, -0.1);
        snowFall.material.uniforms.get("maxGravity").data = new Vector( 0.1, -1,  0.1);
        // mesh.translate( 0,1,0, true );
        scene.add(snowFall);

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
        snowFall.material.uniforms.get("time").data = time; //Update snow fall
        if (input.isKeyDown(GLFW_KEY_SPACE))
        {
            System.out.println("Hello!");
        }

        rig.update(input, deltaTime);   // update camera controls
        renderer.render(scene, camera); // render the 3D scene
    }

}
