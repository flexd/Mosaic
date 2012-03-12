package org.cognitive;

import entities.AbstractEntity;
import entities.Player;
import entities.TexturedEntity;
import java.awt.Font;
import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
  java.awt.Font awtFont = new Font("Georgia", 1, 16);
  public org.newdawn.slick.Font font;
  public static TextureManager textureManager = new TextureManager();
  public static List<AbstractEntity> ground = new ArrayList();
  public static List<AbstractEntity> entities = new ArrayList();
  private int delta = 0;
  private int fps;
  private long lastFPS;
  private long lastFrame;
  private int fpsCounter;
  
  private TexturedEntity tiger;
  private Player player;
  
  private static enum State {

    INTRO, MAIN_MENU, GAME;
  }
  private State state = State.GAME; // INTRO should be default.

  private void drawEntities() {
    for (AbstractEntity e : entities) {
      e.update(delta);
      e.draw();
    }
  }

  private void drawFPS() {
    //System.out.println(getDelta());
    //texture.bind();
    //glEnable(GL_BLEND);
    font.drawString(1F, 1F, "FPS: " + fps, Color.yellow);
    //glDisable(GL_BLEND);
  }

  private void drawGround() {
    for (AbstractEntity e : ground) {
      e.draw();
    }
  }

  private void generateGround() {
    for (int i = 0; i < DISPLAY_WIDTH; i+=32) {
      for (int j = 0; j < DISPLAY_HEIGHT; j+=32) {
        ground.add(new TexturedEntity(i,j, "tile0"));
      }
    }
  }
  
  private long getTime() {
    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
  }

  private int getDelta() {
    long currentTime = getTime();
    int diff = (int) (currentTime - lastFrame);
    lastFrame = getTime();
    return diff;
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

    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

    glEnable(GL_TEXTURE_2D);
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    //glScalef(1f/32f, 1f/32f, 1f);
    textureManager.load("characters", "rpg", 32); // spritesheet name, filename, slotSize
    textureManager.load("world", "ground", 32); // spritesheet name, filename, slotSize
    textureManager.define("characters", "player",3, 3); // Sheet named "world", "name of sprite", slot 0,0 in spritesheet.
    textureManager.define("characters", "girl", 1, 4); // Sheet named "world", "name of sprite", slot 0,0 in spritesheet.
    textureManager.define("world", "tile0", 0,0);
    player = new Player(20, 20, "player"); // Position x, y, sprite named "character"

    tiger = new TexturedEntity(55, 55, "girl"); // Position x, y, sprite named "character"
    entities.add(player);
    entities.add(tiger);
    generateGround();
  }

  public void update() {
    delta = getDelta();
    updateFPS();
  }

  private void gameRender() {
    drawGround();
    drawEntities();
    drawFPS();
  }

  public void processInput() {
    int new_dx = 0, new_dy = 0;
    while (Keyboard.next()) {
      switch (state) {
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
    if (Keyboard.getEventKeyState()) {
      switch (Keyboard.getEventKey()) {
        case Keyboard.KEY_LEFT:
          new_dx = -1;
         // player.move(-1, 0);
          break;
        case Keyboard.KEY_RIGHT:
          new_dx = 1;
          //player.move(1, 0);
          break;
        case Keyboard.KEY_UP:
          new_dy = -1;
          //player.move(0, -1);
          break;
        case Keyboard.KEY_DOWN:
          new_dy = 1;
          //player.move(0, 1);
          break;
      }
    } else {
      new_dx = 0;
      new_dy = 0;
    }
    player.move(new_dx, new_dy);

  }
  public void updateFPS() {
    if (getTime() - lastFPS > 1000) {
      fps = fpsCounter;
      fpsCounter = 0;
      lastFPS += 1000;
    }
    fpsCounter++;
  }
  public void render() {

    switch (state) {
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
    glColor3f(0.0f, 1.0f, 0f);
    glRectf(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
  }

  private void menuRender() {
    glDisable(GL_TEXTURE_2D);
    glColor3f(1.0f, 0f, 0f);
    glRectf(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
  }

  static {
    try {
      LOGGER.addHandler(new FileHandler("errors.log", true));
    } catch (IOException ex) {
      LOGGER.log(Level.WARNING, ex.toString(), ex);
    }
  }

  public static void main(String[] args) {
    ScrollGame main = null;
    try {
      main = new ScrollGame();
      main.create();
      main.run();
    } catch (Exception ex) {
      LOGGER.log(Level.SEVERE, ex.toString(), ex);
    } finally {
      if (main != null) {
        main.destroy();
      }
    }
  }

  public ScrollGame() {
  }

  public void create() throws LWJGLException {
    //Display
    Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT));
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
    lastFPS = getTime();
    //resizeGL();
  }

  public void destroy() {
    //Methods already check if created before destroying.
    Mouse.destroy();
    Keyboard.destroy();
    Display.destroy();
  }

  public void run() {
    while (!Display.isCloseRequested()) { //  && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)
      if (Display.isVisible()) {
        processInput();
        update();
        // Clear for rendering
        glClear(GL_COLOR_BUFFER_BIT);
        render();
      } else {
        if (Display.isDirty()) {
          render();
        }
        try {
          Thread.sleep(100);
        } catch (InterruptedException ex) {
        }
      }
      Display.update();
      Display.sync(60);
    }
  }
}
