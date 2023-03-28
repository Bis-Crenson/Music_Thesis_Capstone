package com.company.musicthesis;
import com.company.musicthesis.MusicGenerator;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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

import javafx.application.*;   // Application
import javafx.stage.*;         // Stage, FileChooser
import javafx.scene.*;         // Scene
import javafx.scene.layout.*;  // VBox, HBox, GridPane, BorderPane, ...
import javafx.scene.input.*;   // KeyEvent, MouseEvent
import javafx.event.*;         // ActionEvent
import javafx.geometry.*;      // Insets, Pos
import javafx.scene.control.*; // Label, TextField, Button, Alert, ...



//September 29, 2022

public class Main extends Application
{
    public Player player = new Player(); //Create music player
    public MusicGeneratorEnhanced music = new MusicGeneratorEnhanced();; //Create Music object
    public Pattern song;

    double x = 0.0;
    double y = 0.0;
    double s = 0.9;
    double incrementor = 0.1; //This is the time variable

    public static void main(String[] args) throws Exception {

        try
        {
            launch(args);
        }
        catch (Exception error)
        {
            error.printStackTrace();
        }
    }
    // the Application.launch method automatically creates
    //   a Stage (the "window", which contains an icon, title bar,
    //            min/max/close buttons, borders (optionally resizable),
    //            and a Scene, which contains all the content)
    //   and then calls the start() method, which sets up the Scene(s).
    public void start(Stage mainStage)
    {
        // ---------------------------------
        // general Stage & Scene setup code
        // ---------------------------------

        // set window title bar text
        mainStage.setTitle("Mock and Benson Music Productions");

        // a VBox is used to arrange things vertically
        VBox root = new VBox();
        // add space around the outside of the VBox
        root.setPadding( new Insets(16) );
        // add space between things added to the VBox
        root.setSpacing( 16 );
        // center all the things added to the VBox
        root.setAlignment( Pos.CENTER );

        // add the VBox to the Scene, which contains all the content
        Scene mainScene = new Scene(root);
        // have the Stage display the Scene
        // (in theory, Stage can switch between multiple Scenes)
        mainStage.setScene( mainScene );

        // ------------------
        // add Scene content
        // ------------------

        Label message = new Label("Enter total repeats");
        root.getChildren().add(message);

        // a TextArea is used to enter multiple lines of text
        TextField textField = new TextField();
        // to display this or any object in the window,
        //   must add it to the List of "children" managed by the VBox
        //   (or whatever the Scene's root element is)
        root.getChildren().add( textField );

        // a Button can be clicked on to activate code
        Button button1 = new Button("Print Message to Console");
        // add Button to the root element
        root.getChildren().add( button1 );
        // when you click on a button, it generates an ActionEvent,
        //   which can be used to run a method
        button1.setOnAction(
                // a method containing code that runs when action occurs;
                //   writing code in this style is called a "lambda expression"
                (ActionEvent event) ->
                {
                    String text = textField.getText();
                    System.out.println("You entered: " + text);
                }
        );

        Canvas canvie = new Canvas(500, 500); //Create canvas with a lw of 500
        root.getChildren().add(canvie); //Adopt canvie
        GraphicsContext context = canvie.getGraphicsContext2D();

        // interactive music
        Button button3 = new Button("Create Some Music"); //Create music but DOES NOT PLAY
        root.getChildren().add( button3 );
      //  Player player = new Player();
        button3.setOnAction(
                (ActionEvent event) ->
                {

                    int repeat = Integer.parseInt(textField.getText()); //Parse the string into an int
                    song = music.createSong(repeat);
                }
        );

        Button button4 = new Button("Create Shapes based off Music");

        root.getChildren().add( button4 );
        button4.setOnAction(
                (ActionEvent event) ->
                {

                    new AnimationTimer()
                    {

                        public void handle(long currentNanoTime)
                        {

                        context.clearRect(0, 0, 500, 500);
                        int numPoints = 413;
                        double[] xCoordinates = new double[numPoints];
                        double[] yCoordinates = new double[numPoints];
                        double deltaX = 800.0 / numPoints;
                        for (int j = 0; j < numPoints; j++)
                        {
                            if(s >= 1.0)
                                incrementor = -0.0002; //Go down at 0
                            if (s <= 0.0)
                                incrementor = 0.0002; //Go up at 0
                            double x = j * deltaX;
                            xCoordinates[j] = j * 10;
                            yCoordinates[j] = (s * Math.sin(4 * j) + (1 - s) * Math.sin(j) + 25) * 10; //Gets the frequency based off the note.
                            s += incrementor;
                        }
                        context.setStroke(Color.BLACK); //Fill the color based off the index/note
                        context.strokePolyline( xCoordinates, yCoordinates, numPoints ); //Create the stroke


                            // background image clears canvas
//                            context.clearRect(0, 0, 500, 500);
//                            context.setFill(Color.BLACK);
//                            context.fillRect(10 + x, 10 + x, 10 + x, 10 + x);

//                            x += 0.1;

                        }
                    }.start();

                    }
                    // create arrays to store x- and y- coordinates of points on polygon
//                    for(int i = 0; i < music.musicShapes.length; i++){ //Music length is the number of squares per note
//
//                        if (music.musicShapes[i] == null)
//                            break; //If we reach the last shape, break the for loop.
//
//                        System.out.println((int)music.baseLength);
//                        int numPoints = 213;
//                        double[] xCoordinates = new double[numPoints];
//                        double[] yCoordinates = new double[numPoints];
//                        double deltaX = 800.0 / numPoints;
//                        for (int j = 0; j < numPoints; j++)
//                        {
//                            double x = j * deltaX;
//                            xCoordinates[j] = x;
//                            yCoordinates[j] = getFrequency(i, x, music.musicShapes, separator); //Gets the frequency based off the note.
//
//                        }
//                        context.setStroke(music.musicShapes[i]); //Fill the color based off the index/note
//                        context.strokePolyline( xCoordinates, yCoordinates, numPoints ); //Create the stroke
//                        separator += 25; //Add a sep so there can be more than 5 values.

                   // }

//                    for(int i = 0; i < (int)music.baseLength; i++){ //Music length is the number of squares per note
//                        try {
//                            //Rectangle rectangle = new Rectangle(5 * i+1, 5 * i+1, 5 * i+1, 5 * i+1);
//                            context.fillRect(5 * i+1, 5 * i+1, 5 * i+1, 5 * i+1); //Cascading effect
//                            context.setFill(music.musicShapes[i]); //Fill the color based off the index
//                            System.out.println(music.baseLength);
//                        }catch(ArrayIndexOutOfBoundsException e){
//                            System.out.println("Dumb");
//                        }
//                    }
        );
        Button button5 = new Button("Play Music"); //Play the music after creating it
        root.getChildren().add( button5 );
        button5.setOnAction(
                (ActionEvent event) ->
                {
                    music.player.play(song);
                }
        );

        mainStage.show();
    }

    //Method will return the correct mathematical formula created by the amazing and genius Faith Mock
    //Color array is the array of colors selected which correspond to the shape.
    //Current Colors
    //Color.AQUA, Color.BLACK, Color.BLUE,  //Correlates with noteNames
    //Color.CYAN, Color.RED
    public double getFrequency(int i, double x, Color[] colorArray, int separator){
        int index = -1; //For now, used this solution for the colors

        if(colorArray[i].equals(Color.AQUA))
            index = 0;
        else if(colorArray[i].equals(Color.BLACK))
            index = 1;
        else if(colorArray[i].equals(Color.BLUE))
            index = 2;
        else if(colorArray[i].equals(Color.CYAN))
            index = 3;
        else if(colorArray[i].equals(Color.RED))
            index = 4;
        else
            System.out.println(colorArray[i]);


        //For the future, create anohter array in Music Generator which stores each index value after adding a note based off the index.

        double[] frequencyArray = {10 * Math.sin(2 * Math.PI * x) + separator,
                                   10 * Math.sin(16 * Math.PI / 19 * x ) + separator,
                                   10 * Math.sin(128 * Math.PI / 81 * x) + separator,
                                   10 * Math.sin(4 * Math.PI / 3 * x) + separator,
                                   10 * Math.sin(Math.PI * x) + separator}; //C, D, E, G, A
        return frequencyArray[index]; //Index is based off the current shape value which is based off the selected note.
    }

}