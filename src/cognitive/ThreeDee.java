package cognitive;

import cognitive.graphics.Camera3D;
import cognitive.graphics.Renderer3D;
import org.cognitive.texturemanager.*;

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
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import entities.Quad3D;


public class ThreeDee {
  public static final int DISPLAY_HEIGHT = 600;
  public static final int DISPLAY_WIDTH = 800;
  
  public static final int WORLD_WIDTH = 10;
  public static final int WORLD_HEIGHT = 10;
  public static final int WORLD_DEPTH = 10;
  public static final Logger LOGGER = Logger.getLogger(ThreeDee.class.getName());
  private int lastError = 0;
  
  private long lastFrame;
  private float delta = 0;
  private Camera3D camera;
  private Renderer3D renderer;
  public static TextureManager tm = new TextureManager();
  private Sprite manSprite;
  
  
  private long getTime() {
    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
  }
  
  private float getDelta() {
    long currentTime = getTime();
    float delta = (float) (currentTime - lastFrame);
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
    ThreeDee mainWindow = null;
    try {
      mainWindow = new ThreeDee();
      mainWindow.create();
      mainWindow.run();
    }
    catch(Exception ex) {
      LOGGER.log(Level.SEVERE,ex.toString(),ex);
    }
    finally {
      if(mainWindow != null) {
        mainWindow.destroy();
      }
    }
  }

  public ThreeDee() {

  }

  public void create() throws LWJGLException {
    //Display
    Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH,DISPLAY_HEIGHT));
    Display.setFullscreen(true);
    Display.setTitle("3D!");
    Display.create();

    //Keyboard
    Keyboard.create();

    //Mouse
    Mouse.setGrabbed(true);
    Mouse.create();

    //OpenGL
    camera = new Camera3D (new Vector3f(10,10,10));
    renderer = new Renderer3D();
    lastFrame = getTime();
    //resizeGL();
    
    tm.load("characters", "rpg", 32); // spritesheet name, filename, slotSize
    tm.load("world", "default", 32); // spritesheet name, filename, slotSize
    tm.load("hero", "generichero-blackblue", 32); // spritesheet name, filename, slotSize
    tm.define("characters", "player",0,0); // Sheet named "world", "name of sprite", slot 0,0 in spritesheet.
    tm.define("hero", "hero", 0, 0 ); // Sheet named "world", "name of sprite", slot 0,0 in spritesheet.
    tm.define("world", "tile0", 2,3);
    
    manSprite = tm.getSpriteByName("tile0");
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
        camera.controls();
        // Clear for rendering
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
        render();

        camera.update(delta); // ALWAYS LAST
      }
      else {
        if(Display.isDirty()) {
          render();
          camera.update(delta); // ALWAYS LAST
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

  private void render() {
    delta = getDelta();

    //System.out.println("X: " + camera.cameraPosition.x + " Y: " + camera.cameraPosition.y + " Z: " + camera.cameraPosition.z);
    
    renderer.queue(new Quad3D(0, 0, 0, 10, 10, 1, 1, 1, 1, manSprite));
    //renderer.queue(new Quad3D(0, 0, 0, 10, 10, 1, 0, 0, 1));
    renderer.flushQueue();
    lastError = glGetError();
    //System.out.println("lastError = " + lastError);
  
    
    
  }  
}

