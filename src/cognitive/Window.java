package cognitive;

import cognitive.graphics.Graphics;
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
import org.cognitive.shadermanager.Shader;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;

public class Window {

  public static final int DISPLAY_HEIGHT = 600;
  public static final int DISPLAY_WIDTH = 800;
  public static final Logger LOGGER = Logger.getLogger(Window.class.getName());

  public static Graphics graphics;
  public GamePlay gameplay;
  public static TextureManager tm = new TextureManager();
  public static List<AbstractEntity> ground = new ArrayList();
  public static List<AbstractEntity> entities = new ArrayList();
  private int delta = 0;
  private int fps;
  private long lastFPS;
  private long lastFrame;
  private int fpsCounter;
  
  boolean leftButtonHeld = false;
  private int iMouseX = 0, iMouseY = 0;
  public static int mouseX = 0, mouseY = 0;
  private Rectangle selectionBox = new Rectangle();
  
  
  private int displayListGround;
  private boolean lightingToggle = false;

  private void loadTextures() {
    //glScalef(1f/32f, 1f/32f, 1f);
    tm.load("characters", "rpg", 32); // spritesheet name, filename, slotSize
    tm.load("world", "default", 32); // spritesheet name, filename, slotSize
    tm.load("hero", "generichero-blackblue", 32); // spritesheet name, filename, slotSize
    tm.define("characters", "player",0,0); // Sheet named "world", "name of sprite", slot 0,0 in spritesheet.
    tm.define("hero", "hero", 0, 0 ); // Sheet named "world", "name of sprite", slot 0,0 in spritesheet.
    tm.define("world", "tile0", 2,3);
  }
  
  private void processMouse() {
    mouseX = Mouse.getX() - graphics.camera.offsetX;
    mouseY = (-Mouse.getY() + Window.DISPLAY_HEIGHT) - graphics.camera.offsetY;
    unitSelection();
  }

 
  //<editor-fold defaultstate="collapsed" desc="mouse unit selection">
  private void unitSelection() {
    boolean leftButtonDown = Mouse.isButtonDown(0); // is left mouse button down
    boolean rightButtonDown = Mouse.isButtonDown(1); // is right mouse button down.
    if (Mouse.isInsideWindow()) {
      if (leftButtonDown) {
        // System.out.println("Left mouse button is down!");
        if (leftButtonHeld) {
          // System.out.println("Left mouse button is being held!");
          // left mouse is pressed
          selectionBox.setBounds(iMouseX, iMouseY, mouseX, mouseY);
          graphics.drawLineBox(iMouseX, iMouseY, mouseX, mouseY, true);
          
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
          //System.out.println("Left button was down but is not any more, setting held to false");
          leftButtonHeld = false;
          // Let's select some units, shall we?
          
          for (AbstractEntity entity : entities) {
            if (entity instanceof Unit) { // Only units are selectable for now.
              if (entity.getHitbox().intersects(selectionBox)) {
               // System.out.println("Something intersects the selectionBox, marking it selected!");
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
              u.addOrder(mouseX, mouseY);
            }
          }
        }
      }
    }
  }
  //</editor-fold>
  
  
  

  private void drawEntities() {
    
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
    for (int i = 0; i < 10; i++) {
      Unit unit = new Unit(100+(70*i), 100, "hero");
      entities.add(unit);
    }
  
    generateGroundTiles();
    compileDisplayLists();
  }
  public void compileDisplayLists() {
    displayListGround = glGenLists(1);
    compileGroundList(); // Compile the displaylist
  }
  public void update() {
    delta = getDelta();
    graphics.camera.update();
    updateFPS();
  }

  private void gameRender() {
    
    //glCallList(displayListGround);
    
    processMouse();
  }

  public void processInput() {

    while (Keyboard.next()) {
      switch (gameplay.getState()) {
        case INTRO:
          if (Keyboard.getEventKey() == Keyboard.KEY_RETURN && Keyboard.getEventKeyState()) {
            gameplay.setState(GamePlay.GameState.MAIN_MENU);
          }
          break;
        case MAIN_MENU:
          if (Keyboard.getEventKey() == Keyboard.KEY_RETURN && Keyboard.getEventKeyState()) {
            gameplay.setState(GamePlay.GameState.GAME);
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
        case Keyboard.KEY_L:
          if (lightingToggle) {
            lightingToggle = false;
            System.out.println("Lighting off!");
          }
          else {
            lightingToggle = true;
            System.out.println("Lighting on!");
          }
          break;
      }
    }

  }
  
  public void render() {
    
    if (lightingToggle) {
      glEnable(GL_LIGHTING);
    } else {
      glDisable(GL_LIGHTING);
    }
    //graphics.camera.setCameraX(mouseX);
    //graphics.camera.setY(mouseY);
    glPushMatrix();
    gameRender();
    graphics.render(delta);
    //graphics.camera.followEntity((int)entities.get(0).getX(), (int)entities.get(0).getY());
    //graphics.camera.followMouse(true);
    glPopMatrix();
    
    graphics.drawFPS(fps);
    graphics.drawStatic(1, 1, "Lighting: " + (lightingToggle ? "On" : "Off"), Graphics.FontSize.Large, Color.yellow);

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

    glClearColor(0.5f, 0.5f, 0.8f, 1.0f);

    //glEnable(GL_TEXTURE_2D);
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
    Display.setTitle("Generic generic game");
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
        //System.out.println("Delta is: " + delta);
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
