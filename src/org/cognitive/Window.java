package org.cognitive;

import entities.AbstractEntity;
import entities.Player;
import entities.TexturedEntity;
import entities.Unit;
import java.awt.Rectangle;
import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cognitive.screens.Intro;
import org.cognitive.screens.MainMenu;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Window {

  public static final int DISPLAY_HEIGHT = 600;
  public static final int DISPLAY_WIDTH = 800;
  public static final Logger LOGGER = Logger.getLogger(Window.class.getName());

  public Graphics graphics;
  public GamePlay gameplay;
  public static TextureManager textureManager = new TextureManager();
  public static List<AbstractEntity> ground = new ArrayList();
  public static List<AbstractEntity> entities = new ArrayList();
  private int delta = 0;
  private int fps;
  private long lastFPS;
  private long lastFrame;
  private int fpsCounter;
  
  private Player player;
  boolean leftButtonHeld = false;
  private int iMouseX = 0, iMouseY = 0;
  private int mouseX = 0, mouseY = 0;
  private Rectangle selectionBox = new Rectangle();
  private Camera camera;
  
  
  private int displayListGround;

  private void loadTextures() {
    //glScalef(1f/32f, 1f/32f, 1f);
    textureManager.load("characters", "rpg", 32); // spritesheet name, filename, slotSize
    textureManager.load("world", "default", 32); // spritesheet name, filename, slotSize
    textureManager.load("hero", "generichero-blackblue", 32); // spritesheet name, filename, slotSize
    textureManager.define("characters", "player",0,0); // Sheet named "world", "name of sprite", slot 0,0 in spritesheet.
    textureManager.define("hero", "hero", 0, 0 ); // Sheet named "world", "name of sprite", slot 0,0 in spritesheet.
    textureManager.define("world", "tile0", 2,3);
    player = new Player(20, 20, "player"); // Position x, y, sprite named "character"
  }
  
  private void processMouse() {
    boolean leftButtonDown = Mouse.isButtonDown(0); // is left mouse button down
    boolean rightButtonDown = Mouse.isButtonDown(1); // is right mouse button down.
    mouseX = Mouse.getX();
    mouseY = (-Mouse.getY()+DISPLAY_HEIGHT);
    if (Mouse.isInsideWindow()) {
      if (leftButtonDown) {
       // System.out.println("Left mouse button is down!");
        if (leftButtonHeld) {
         // System.out.println("Left mouse button is being held!");
          // left mouse is pressed
          selectionBox.setBounds(iMouseX, iMouseY, mouseX, mouseY);
          Graphics.drawLineBox(iMouseX, iMouseY, mouseX, mouseY, true);
          
          //System.out.println("selectionBox x1: " + selectionBox.x + " y1: " + selectionBox.y + "x2: " + selectionBox.width + "y2: " + selectionBox.height);
         // System.out.println("iMouseX: " + iMouseX + " iMouseY: " + iMouseY + " MouseX: " + mouseX + " MouseY: " + mouseY);
        } else {
          //System.out.println("Left mouse button was not being held, setting state to held.");
          leftButtonHeld = true;
          iMouseX = mouseX;
          iMouseY = mouseY;
          for (AbstractEntity entity : entities) {
            if (entity instanceof Unit) { // Only units are selectable for now.
              entity.setSelected(false);
            }
          }
          selectionBox.setBounds(0,0,0,0);
        }


      }

      else {
        if (leftButtonHeld) {
          System.out.println("Left button was down but is not any more, setting held to false");
          leftButtonHeld = false;
          // Let's select some units, shall we?

          for (AbstractEntity entity : entities) {
            if (entity instanceof Unit) { // Only units are selectable for now.
              if (entity.getHitbox().intersects(selectionBox)) {
                System.out.println("Something intersects the selectionBox, marking it selected!");
                entity.setSelected(true);
              }
            }
          }
        }
      }
      if (rightButtonDown) {
        for (AbstractEntity e : entities) {
          if (e instanceof Unit) {
            Unit u = (Unit)e;
            if (u.isSelected()) {
              u.moveTo(mouseX, mouseY);
            }
          }
        }
      }
    }
  }
  
  
  
  private static enum GameState {

    INTRO, MAIN_MENU, GAME;
  }
  private GameState state = GameState.GAME; // INTRO should be default.

  private void drawEntities() {
    for (AbstractEntity e : entities) {
      e.update(delta);
      e.draw();
    }
  }

  

  private void compileGroundList() {
    glNewList(displayListGround, GL_COMPILE);
      for (AbstractEntity e : ground) {
        e.draw();
      }
    glEndList();
  }

  private void generateGroundTiles() {
    for (int i = 0; i < DISPLAY_WIDTH; i+=32) {
      for (int j = 0; j < DISPLAY_HEIGHT; j+=32) {
        ground.add(new TexturedEntity(i,j, "tile0"));  
      }
    }
  }
  
  

  

  private void setupEntities() {
    for (int i = 0; i < 1; i++) {
      Unit unit = new Unit(55+(i*64), 55, "hero");
      entities.add(unit);
    }
    
    entities.add(player);
    generateGroundTiles();
    compileDisplayLists();
  }
  public void compileDisplayLists() {
    displayListGround = glGenLists(1);
    compileGroundList(); // Compile the displaylist
  }
  public void update() {
    delta = getDelta();
    camera.update();
    updateFPS();
  }

  private void gameRender() {
    glCallList(displayListGround);
    drawEntities();
    
    processMouse();
  }

  public void processInput() {
    int new_dx = 0, new_dy = 0;
    while (Keyboard.next()) {
      switch (state) {
        case INTRO:
          if (Keyboard.getEventKey() == Keyboard.KEY_RETURN && Keyboard.getEventKeyState()) {
            state = GameState.MAIN_MENU;
          }
          break;
        case MAIN_MENU:
          if (Keyboard.getEventKey() == Keyboard.KEY_RETURN && Keyboard.getEventKeyState()) {
            state = GameState.GAME;
          }
          if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState()) {
            Display.destroy();
            System.exit(0);
          }
        case GAME:
          if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState()) {
            Display.destroy();
            System.exit(0);
          }
          break;
      }
    }
    if (Keyboard.getEventKeyState()) {
      switch (Keyboard.getEventKey()) {
        case Keyboard.KEY_LEFT:
          camera.setDx(1);
          break;
        case Keyboard.KEY_RIGHT:
          camera.setDx(-1);
          //player.move(1, 0);
          break;
        case Keyboard.KEY_UP:
          camera.setDy(1);
          //player.move(0, -1);
          break;
        case Keyboard.KEY_DOWN:
          camera.setDy(-1);
          //player.move(0, 1);
          break;
        case Keyboard.KEY_SPACE:
          camera.setDx(Mouse.getDX());
          camera.setDy(0);
          break;
      }
    } else {
      new_dx = 0;
      new_dy = 0;
    }
    player.move(new_dx, new_dy);

  }
  
  public void render() {
    
    switch (state) {
      case INTRO:
        Intro.render();
        break;
      case MAIN_MENU:
        MainMenu.render();
        break;
      case GAME:
        gameRender();
        break;
    }
  }

  //<editor-fold defaultstate="collapsed" desc="logging">
  static {
    try {
      LOGGER.addHandler(new FileHandler("errors.log", true));
    } catch (IOException ex) {
      LOGGER.log(Level.WARNING, ex.toString(), ex);
    }
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="main method">
  public static void main(String[] args) {
    Window main = null;
    try {
      main = new Window();
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
  //</editor-fold>

  public void initGL() {
            
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    glViewport(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
    glOrtho(0, DISPLAY_WIDTH, DISPLAY_HEIGHT, 0, 1, -1);
    glMatrixMode(GL_MODELVIEW);

    glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

    glEnable(GL_TEXTURE_2D);
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    
    
  }
  
  //<editor-fold defaultstate="collapsed" desc="fps and delta">
  private long getTime() {
    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
  }

  private int getDelta() {
    long currentTime = getTime();
    int diff = (int) (currentTime - lastFrame);
    lastFrame = getTime();
    return diff;
  }
  public void updateFPS() {
    if (getTime() - lastFPS > 1000) {
      fps = fpsCounter;
      fpsCounter = 0;
      lastFPS += 1000;
    }
    fpsCounter++;
  }
  //</editor-fold>
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
    loadTextures(); // load and define textures
    setupEntities(); // Add entities to lists
    graphics = new Graphics();
    gameplay = new GamePlay();
    camera = new Camera(0,0);
    
    // set initial values for lastFrame and lastFPS
    lastFrame = getTime();
    lastFPS = getTime();
    //resizeGL();
  }

  //<editor-fold defaultstate="collapsed" desc="Destroy method">
  public void destroy() {
    // delete the displaylists
    glDeleteLists(displayListGround, 1);
    //Methods already check if created before destroying.
    Mouse.destroy();
    Keyboard.destroy();
    Display.destroy();
  }
  //</editor-fold>

  public void run() {
    while (!Display.isCloseRequested() || Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) { //  && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)
      if (Display.isVisible()) {
        processInput();
        update();
        gameplay.update(delta);
        // Clear for rendering
        glClear(GL_COLOR_BUFFER_BIT);
        graphics.drawFPS(fps);
        graphics.render();
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
