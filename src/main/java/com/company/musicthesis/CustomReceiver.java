package com.company.musicthesis;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;


//CODE FROM //https://stackoverflow.com/questions/54723047/output-midi-file-to-console-while-being-played-in-java
public class CustomReceiver implements Receiver {

    public CustomReceiver() {

    }

    public static int key;
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
   // public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
   public static final String[] NOTE_NAMES = {"B5", "C5", "D5", "E5", "F5", "G5", "A6", "B6", "C6", "D6"};
    @Override
    public void send(MidiMessage message, long timeStamp) {
        if (message instanceof ShortMessage) {
            ShortMessage sm = (ShortMessage) message;
            //System.out.print("Channel: " + sm.getChannel() + " ");
            if (sm.getCommand() == NOTE_ON) {
                key = sm.getData1();
                int octave = (key / NOTE_NAMES.length) - 1;
                int note = key % NOTE_NAMES.length;
                String noteName = NOTE_NAMES[note];
                int velocity = sm.getData2();
                System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity);
            } else if (sm.getCommand() == NOTE_OFF) {
//                //Don't care
//                int key = sm.getData1();
//                int octave = (key / 8) - 1;
//                int note = key % 8;
//                String noteName = NOTE_NAMES[note];
//                int velocity = sm.getData2();
//                System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
            } else {
                //System.out.println("Command:" + sm.getCommand());
            }
        } else {
            //System.out.println("Other message: " + message.getClass());
        }
    }


    public int returnKey(){
        return key; //This is to test our compatibility with the test player class.
    }

    @Override
    public void close() {

    }
}
