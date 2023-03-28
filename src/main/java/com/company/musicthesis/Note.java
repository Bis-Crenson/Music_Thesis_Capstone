package com.company.musicthesis;

public class Note {
    public int val = 0; //Value which will be used, default is 0
    public String name; //The name of the note
    public boolean special; //Special variable, if true give 10 points, false do nothing


    //Constructor to initialize the values.
    public Note(String name, boolean special){
        this.name = name;
        this.special = special;
    }

    //Constructor to initialize the values.
    public Note(String name, int value){ //Give a set value or y value
        this.name = name;
        this.val = value;
    }

    //Getters
    public int getVal() {
        return val;
    }

    public String getName() {
        return name;
    }

    public boolean isSpecial() {
        return special;
    }


    //Setters
    public void setVal(int val) {
        this.val = val;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }

    //Method helpers

    //Method helper to return a bonus value.
    public int getBonusValue(){
        if(this.special) //If true, add a bonus of 10, remember to add an int if we wish to change bonus amount
            return 10;
        return 0; //If false, return nothing.
    }
}
