package org.cognitive;

import entities.AbstractTexturedMoveableEntity;
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


/**
 * @author jediTofu
 * @see <a href="http://lwjgl.org/">LWJGL Home Page</a>
 */
public class ScrollGame {
  public static final int DISPLAY_HEIGHT = 480;
  public static final int DISPLAY_WIDTH = 640;
  public static final Logger LOGGER = Logger.getLogger(ScrollGame.class.getName());

  private static SpriteSheet textureManager = new SpriteSheet(16,16, "sheet");
  
  private static enum State {
    INTRO, MAIN_MENU, GAME;
  }
  private State state = State.GAME; // INTRO should be default.

  private long lastFrame;

  
  private AbstractTexturedMoveableEntity box;
  
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

    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    glOrtho(0, DISPLAY_WIDTH, DISPLAY_HEIGHT, 0, 1, -1);
    glMatrixMode(GL_MODELVIEW);
    //glEnable(GL_TEXTURE_2D);
    glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

    glEnable(GL_TEXTURE_2D);
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
    glScalef(1f/16f, 1f/16f, 1f);
    
    box = new AbstractTexturedMoveableEntity(15, 15, 0, 0); // Position x, y, spriteID x, y
  }
  
  
  
  
   
  public void update() {

  }
  
  private void gameRender() {
    //System.out.println(getDelta());
    //texture.bind();
    
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

