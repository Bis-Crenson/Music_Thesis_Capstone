package com.company.musicthesis;

public class NoteData
{
    public String data; // example: "Eb5h" or "E5h". Assumes no "dotted durations"; String length should be 3 or 4.

    public String name;     // "Eb"
    public String letter;   // "E"
    public boolean flat;    // if contains "b"; true
    public boolean sharp;   // if contains "#"; false
    public int octave;      // 5
    public String duration; // "h"

    public NoteData(String data)
    {
        int len = data.length(); // 3 or 4
        letter = String.valueOf( data.charAt(0) );
        if (data.contains("b"))
        {
            flat = true;
            name = letter + "b";
        }
        if (data.contains("#"))
        {
            sharp = true;
            name = letter + "#";
        }
        octave = Integer.valueOf( String.valueOf(data.charAt(len-2)) );
        duration = String.valueOf( data.charAt(len-1) );
    }
}
