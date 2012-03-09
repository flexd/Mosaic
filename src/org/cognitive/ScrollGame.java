package org.cognitive;

import entities.Tile;
import java.awt.Font;
import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;


/**
 * @author jediTofu
 * @see <a href="http://lwjgl.org/">LWJGL Home Page</a>
 */
public class ScrollGame {
  public static final int DISPLAY_HEIGHT = 600;
  public static final int DISPLAY_WIDTH = 800;
  public static final Logger LOGGER = Logger.getLogger(ScrollGame.class.getName());
  
  java.awt.Font awtFont = new Font("Times New Roman", 1, 16);
  public org.newdawn.slick.Font font;
  
  public static TextureManager tManager = new TextureManager();
  
  private static enum State {
    INTRO, MAIN_MENU, GAME;
  }
  private State state = State.GAME; // INTRO should be default.

  private long lastFrame;

  
  private Tile box;
  
  private long getTime() {
    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
  }
  
  private int getDelta() {
    long currentTime = getTime();
    int delta = (int) (currentTime - lastFrame);
    lastFrame = getTime();
    return delta;
  }
  
  

  public void initGL() {
    //2D Initialization
    /*glClearColor(0.0f,0.0f,0.0f,0.0f);
    glDisable(GL_DEPTH_TEST);
    glDisable(GL_LIGHTING);*/
    //glEnable(GL_TEXTURE_2D);               
    
    font = new TrueTypeFont(awtFont, false);
    
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    glViewport(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
    glOrtho(0, DISPLAY_WIDTH, DISPLAY_HEIGHT, 0, 1, -1);
    glMatrixMode(GL_MODELVIEW);
  
    glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

    glEnable(GL_TEXTURE_2D);
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
    //glScalef(1f/32f, 1f/32f, 1f);
    tManager.load("world", "world", 32); // spritesheet name, filename, slotSize
    tManager.define("world", "character", 0, 0); // Sheet named "world", "name of sprite", slot 0,0 in spritesheet.
    
    box = new Tile(15, 15, "character"); // Position x, y, sprite named "character"
  }
  
  
  
  
   
  public void update() {

  }
  
  private void gameRender() {
    //System.out.println(getDelta());
    //texture.bind();
    font.drawString(150F, 300F, "HELLO World", Color.red);
    int delta = getDelta();
    box.draw();

  }
  
  public void loadTextures() {

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
  
  static {
    try {
      LOGGER.addHandler(new FileHandler("errors.log",true));
    }
    catch(IOException ex) {
      LOGGER.log(Level.WARNING,ex.toString(),ex);
    }
  }

  public static void main(String[] args) {
    ScrollGame main = null;
    try {
      main = new ScrollGame();
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

  public ScrollGame() {

  }

  public void create() throws LWJGLException {
    //Display
    Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH,DISPLAY_HEIGHT));
    Display.setFullscreen(false);
    Display.setTitle("Scroll!");
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
}

