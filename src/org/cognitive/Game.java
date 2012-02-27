package org.cognitive;

import java.io.File;
import java.io.FileInputStream;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;


/**
 * @author jediTofu
 * @see <a href="http://lwjgl.org/">LWJGL Home Page</a>
 */
public class Game {
  public static final int DISPLAY_HEIGHT = 480;
  public static final int DISPLAY_WIDTH = 640;
  public static final Logger LOGGER = Logger.getLogger(Game.class.getName());
  public int x = 100, y = 200;
  public int dx = 1, dy = 1;
  
  private static enum State {
    INTRO, MAIN_MENU, GAME;
  }
  private State state = State.GAME; // INTRO should be default.
  private List<GameObject> objects = new ArrayList<GameObject>(16);
  private boolean somethingIsSelected = false;
  
  private long lastFrame;
  private Texture nibbler;
  private Texture snake;
  private Texture katana;
  
  private long getTime() {
    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
  }
  
  private int getDelta() {
    long currentTime = getTime();
    int delta = (int) (currentTime - lastFrame);
    lastFrame = getTime();
    return delta;
  }
  
  static {
    try {
      LOGGER.addHandler(new FileHandler("errors.log",true));
    }
    catch(IOException ex) {
      LOGGER.log(Level.WARNING,ex.toString(),ex);
    }
  }

  public static void main(String[] args) {
    Game main = null;
    try {
      main = new Game();
      main.create();
      main.run();
    }
    catch(Exception ex) {
      LOGGER.log(Level.SEVERE,ex.toString(),ex);
    }
    finally {
      if(main != null) {
        main.destroy();
      }
    }
  }

  public Game() {

  }

  public void create() throws LWJGLException {
    //Display
    Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH,DISPLAY_HEIGHT));
    Display.setFullscreen(false);
    Display.setTitle("Hello LWJGL World!");
    Display.create();

    //Keyboard
    Keyboard.create();

    //Mouse
    Mouse.setGrabbed(false);
    Mouse.create();

    //OpenGL
    initGL();
    lastFrame = getTime();
    loadTextures();
    //resizeGL();
  }

  public void destroy() {
    //Methods already check if created before destroying.
    Mouse.destroy();
    Keyboard.destroy();
    Display.destroy();
  }

  public void initGL() {
    //2D Initialization
    /*glClearColor(0.0f,0.0f,0.0f,0.0f);
    glDisable(GL_DEPTH_TEST);
    glDisable(GL_LIGHTING);*/
    //glEnable(GL_TEXTURE_2D);               

    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    glOrtho(0, DISPLAY_WIDTH, DISPLAY_HEIGHT, 0, 1, -1);
    glMatrixMode(GL_MODELVIEW);
    //glEnable(GL_TEXTURE_2D);
    glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

    glEnable(GL_TEXTURE_2D);
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
  }
  
  public void run() {
    while(!Display.isCloseRequested()) { //  && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)
      if(Display.isVisible()) {
        processKeyboard();
        processMouse();
        update();
        // Clear for rendering
        glClear(GL_COLOR_BUFFER_BIT);
        render();
      }
      else {
        if(Display.isDirty()) {
          render();
        }
        try {
          Thread.sleep(100);
        }
        catch(InterruptedException ex) {
        }
      }
      Display.update();
      Display.sync(60);
    }
  }

  public void update() {

  }
  private Texture loadTexture(String key) {
    try {
      
      return TextureLoader.getTexture("PNG", getClass().getResourceAsStream("/res/" + key + ".png")); // new FileInputStream(new File("res/"+ key + ".png")));
    } catch (IOException ex) {
      Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }
  public void loadTextures() {
    
    // load texture from PNG file
    nibbler = loadTexture("nibble");
    snake = loadTexture("solid_snake");
    katana = loadTexture("katana");
    objects.add(new TexturedObject(15,15, nibbler));
    objects.add(new TexturedObject(100, 150, snake));

  }


  public void processKeyboard() {
    while (Keyboard.next()) {
      switch(state) {
        case INTRO:
          if (Keyboard.getEventKey() == Keyboard.KEY_RETURN && Keyboard.getEventKeyState()) {
            state = State.MAIN_MENU;
          }
          break;
        case MAIN_MENU:
          if (Keyboard.getEventKey() == Keyboard.KEY_RETURN && Keyboard.getEventKeyState()) {
            state = State.GAME;
          }
          if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState()) {
            Display.destroy();
            System.exit(0);
          }
        case GAME:
          if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState()) {
            state = State.MAIN_MENU;
          }
          if (Keyboard.getEventKey() == Keyboard.KEY_B && Keyboard.getEventKeyState()) {
            objects.add(new Box(15, 15));
          }
          else if (Keyboard.getEventKey() == Keyboard.KEY_S && Keyboard.getEventKeyState()) {
            objects.add(new TexturedObject(15, 15, snake));
          }
          break;
      }
    
      
      
    }

  }

  public void processMouse() {

  }

  public void render() {
    
    switch(state) {
      case INTRO:
        introRender();
        break;
      case MAIN_MENU:
        menuRender();
        break;
      case GAME:
        gameRender();
        break;
    }
  }
  private void introRender() {
    glDisable(GL_TEXTURE_2D);
    glColor3f(0.0f,1.0f,0f);
    glRectf(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);
  }
  private void menuRender() {
    glDisable(GL_TEXTURE_2D);
    glColor3f(1.0f,0f,0f);
    glRectf(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);
  }
  private void gameRender() {
    //System.out.println(getDelta());
    //texture.bind();
    
    
    int delta = getDelta();
    
    for (GameObject object : objects) {
      if (Mouse.isButtonDown(0) && object.inBounds(Mouse.getX(), 480 - Mouse.getY()) && !somethingIsSelected) {
        somethingIsSelected = true;
        object.selected = true;
      }
      
      if (Mouse.isButtonDown(1)) {
        object.selected = false;
        somethingIsSelected = false;
      }
      
      if (object.selected) {
        object.update(Mouse.getDX(), -Mouse.getDY());
      }
      else {
        object.update(delta);
      }
      if (object.x > DISPLAY_WIDTH) { // Too far right, reset.
        object.x = 0;
      }
      if (object.y > DISPLAY_HEIGHT) { // Too far down, reset.
        object.y = 0;
      }
      
      if (object.x < 0) { // Too far left, reset.
        object.x = DISPLAY_WIDTH;
      }
      if (object.y < 0) { // Too far up, reset.
        object.y = DISPLAY_HEIGHT;
      }
      object.draw();
    }

  }  
}

