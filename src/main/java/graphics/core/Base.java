package graphics.core;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.io.IOException;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL40.GL_FALSE;

// LWJGL = Lightweight Java Gaming Library
// GLFW = Graphics Library FrameWork

public abstract class Base
{
    // window dimensions
    public int windowWidth;
    public int windowHeight;

    // the window handle/reference
    private long window;

    // is the main loop currently active?
    private boolean running;

    // number of seconds application has been running
    public float time;
    // seconds since last iteration of run loop
    public float deltaTime;
    // store timestamp from last iteration of run loop
    private long previousTime;
    private long currentTime;

    // handle user input events
    public Input input;

    // constructor
    public Base()
    {
        windowWidth = 512;
        windowHeight = 512;
    }

    // start the application life cycle
    public void run() throws InvalidMidiDataException, IOException, MidiUnavailableException, InterruptedException {
        // general startup tasks ----------------------
        startup();

        // application-specific startup; mostly initializing objects
        initialize();

        // main loop ----------------------
        while (running)
        {
            // process input ----------------------

            // poll for window events; activates callback functions on user input
            glfwPollEvents();

            // process input
            input.update();

            // click on close icon to quit application,
            if ( glfwWindowShouldClose(window) )
                running = false;

            // update ----------------------

            // recalculate time variables
            currentTime = System.currentTimeMillis();
            deltaTime = (currentTime - previousTime) / 1000f;
            time += deltaTime;
            previousTime = currentTime;

            update();

            // render ----------------------

            // swap the color buffers
            glfwSwapBuffers(window);
        }

        // shutdown ----------------------
        shutdown();
    }

    public void startup()
    {
        System.out.println("Starting program...");

        // print error messages in System.err
        GLFWErrorCallback.createPrint(System.err).set();

        // initialize GLFW
        boolean initSuccess = glfwInit();
        if ( !initSuccess )
            throw new RuntimeException("Unable to initialize GLFW");

        // create window and associated OpenGL context
        // (context stores framebuffer and other state information)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
        window = glfwCreateWindow(
                windowWidth, windowHeight,
                "Graphics Window", 0, 0);

        if ( window == 0 )
            throw new RuntimeException("Failed to create the GLFW window");

        running = true;
        input = new Input(window);

        // make the OpenGL context current: all function calls will apply to this context instance
        glfwMakeContextCurrent(window);

        // specify number of screen updates to wait before swapping buffers (via glfwSwapBuffers)
        //   setting to 1 enables vertical sync - synchronizes application frame rate to display refresh rate
        //   and prevents visual "screen tearing" artifacts
        glfwSwapInterval(1);

        // detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        System.out.println("LWJGL version: " + Version.getVersion() );

        time = 0;
        deltaTime = 1/60f;
        currentTime = System.currentTimeMillis();
        previousTime = System.currentTimeMillis();
    }

    public abstract void initialize() throws InvalidMidiDataException, IOException, MidiUnavailableException, InterruptedException;

    public abstract void update();

    public void shutdown()
    {
        // stop window monitoring for user input
        glfwFreeCallbacks(window);
        // close/destroy the window
        glfwDestroyWindow(window);
        // stop GLFW
        glfwTerminate();
        // stop error callback
        glfwSetErrorCallback(null).free();
    }

    public void setWindowSize(int width, int height)
    {
        windowWidth = width;
        windowHeight = height;
    }
}