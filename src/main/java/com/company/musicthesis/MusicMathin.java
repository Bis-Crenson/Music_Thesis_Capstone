package com.company.musicthesis;

import graphics.math.Vector;

import java.util.Objects;

public class MusicMathin
{

    // ———————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————
    // | Staff |
    // ——————————
            // Function to determine the y-value location of each staff.
            public double findYval(double lineNum, double y, double dh, double dy)
            {
                y = y + lineNum*(dh + dy);
                return y;
            }

    // ———————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————
    // | Notes |
    // ——————————
            // Function to determine the x-value position of each note.
            public double findXval(double noteNum, double ds, double x, double r)
            {
                x = x+ noteNum*( (2*r) + (ds) );
                return x;
            }

            //HI STEM
            // Function to determine where the note sits on the staff based on the musical note that was placed in the parameter.
           // C5W D5W E5W E4W F4W G4W
            //"B5", "C5", "D5", "E5", "F5", "G5", "A6", "B6", "C6", "D6"
            public double whichStaff(String noteName) {
                Note[] noteNames = {new Note("B5", 0), new Note("C5", 1), new Note("D5", 2),
                                    new Note("E5", 3), new Note("F5", 4), new Note("G5", 5),
                                    new Note("A6", 6), new Note("B6", 7), new Note("C6", 8),new Note("D6", 9)}; //Note array which contains note name and y value
                for (int y = 0; y < noteNames.length; y++)
                {
                    if (Objects.equals(noteNames[y].name, noteName)) //If note object name is equal to provided name, return it's y valu
                    {
                        return (noteNames[y].val);
                    }
                }
                return -1;
            }


            // Function to determine the spacing of the note position in relation to the staff lines.
            public double exactStaffSpacing(double noteIndex, double dy, double dh, double y)
            {
                y = y+ (noteIndex/2)*(dh+dy);
                return y;
            }


            //    // the note has the x and y from these functions and then the z value stays the same
            //    public double findDsVal()
            //    {
            //             could have this function take dx subtracted by (# of notes * each of their diameter). then take that new number and divide by 5
            //                 numberOfNotes -- could use a counter in findXval;
            //                 double temp1 = numberOfNotes * diameterOfNotes;
            //                 temp2 = dx = temp1;
            //                 ds = temp2/(numberOfNotes+1)
            // }

            Vector notePos(String noteName, int beatNumber)
            {
                return new Vector();
            }

}
