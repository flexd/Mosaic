package cognitive;

import cognitive.graphics.Camera3D;
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
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;


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
    Display.setTitle("FUCK YOU NETBEANS");
    Display.create();

    //Keyboard
    Keyboard.create();

    //Mouse
    Mouse.setGrabbed(true);
    Mouse.create();

    //OpenGL
    camera = new Camera3D (new Vector3f(10,10,10));
    lastFrame = getTime();
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

  private Texture loadTexture(String key) {
    try {
      
      return TextureLoader.getTexture("PNG", getClass().getResourceAsStream("/res/" + key + ".png")); // new FileInputStream(new File("res/"+ key + ".png")));
    } catch (IOException ex) {
      Logger.getLogger(ThreeDee.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  private void render() {
    delta = getDelta();

    //System.out.println("X: " + camera.cameraPosition.x + " Y: " + camera.cameraPosition.y + " Z: " + camera.cameraPosition.z);
    
    GL11.glColor3f(1.0f, 1.0f, 1.0f);
   

    glBegin(GL_QUADS);
      GL11.glColor3f(1.0f, 1.0f, 1.0f);

      GL11.glVertex3i(0, 0, 0); // Upper left
      GL11.glVertex3i(WORLD_WIDTH, 0, 0); // Upper right
      GL11.glVertex3i(WORLD_WIDTH, WORLD_HEIGHT, 0); // bottom right
      GL11.glVertex3i(0, WORLD_HEIGHT, 0); // bottom left
      GL11.glColor3f(1.0f, 0.0f, 0.0f);

      //surface 2
      GL11.glVertex3i(WORLD_WIDTH, 0, 0); // Upper left
      GL11.glVertex3i(WORLD_WIDTH, 0, -WORLD_DEPTH); // Upper right
      GL11.glVertex3i(WORLD_WIDTH, WORLD_HEIGHT, -WORLD_DEPTH); // bottom right
      GL11.glVertex3i(WORLD_WIDTH, WORLD_HEIGHT, 0); // bottom left
      
      // surface 3
      GL11.glColor3f(0.0f, 1.0f, 1.0f);

      GL11.glVertex3i(WORLD_WIDTH, 0, -WORLD_HEIGHT); // Upper right
      GL11.glVertex3i(0, 0, -WORLD_DEPTH); // Upper left
      GL11.glVertex3i(0, WORLD_HEIGHT, -WORLD_DEPTH); // bottom right
      GL11.glVertex3i(WORLD_WIDTH, WORLD_HEIGHT, -WORLD_DEPTH); // bottom left
      
      // surface 4
      GL11.glColor3f(0.0f, 0.0f, 1.0f);

      GL11.glVertex3i(0, 0, -WORLD_HEIGHT); // Upper right
      GL11.glVertex3i(0, 0, 0); // Upper left
      GL11.glVertex3i(0, WORLD_HEIGHT, 0); // bottom right
      GL11.glVertex3i(0, WORLD_HEIGHT, -WORLD_DEPTH); // bottom left
      
      // surface 5
      GL11.glColor3f(1.0f, 0.0f, 1.0f);

      GL11.glVertex3i(0, WORLD_HEIGHT, 0); // Upper right
      GL11.glVertex3i(WORLD_WIDTH, WORLD_HEIGHT, 0); // Upper left
      GL11.glVertex3i(WORLD_WIDTH, WORLD_HEIGHT, -WORLD_DEPTH); // bottom left
      GL11.glVertex3i(0, WORLD_HEIGHT, -WORLD_DEPTH); // bottom right
       
      // surface 6
      GL11.glColor3f(0.5f, 0.0f, 1.0f);

      GL11.glVertex3i(0, 0, 0); // Upper right
      GL11.glVertex3i(0, 0, -WORLD_DEPTH); // Upper left
      GL11.glVertex3i(WORLD_WIDTH, 0, -WORLD_DEPTH); // bottom left
      GL11.glVertex3i(WORLD_WIDTH, 0, 0); // bottom right
        
      
      
      glEnd();
    lastError = glGetError();
    //System.out.println("lastError = " + lastError);
  
    
    
  }  
}

