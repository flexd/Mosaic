package cognitive;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;
import static org.lwjgl.util.glu.GLU.gluLookAt;

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


public class OldThreeDee {
  public static final int DISPLAY_HEIGHT = 600;
  public static final int DISPLAY_WIDTH = 800;
  
  public static final int WORLD_WIDTH = 10;
  public static final int WORLD_HEIGHT = 10;
  public static final int WORLD_DEPTH = 10;
  public static final Logger LOGGER = Logger.getLogger(ThreeDee.class.getName());
  private int lastError = 0;
  
  private Vector3f    cameraPosition    = new Vector3f(50,-10,50);
  private Vector3f    cameraRotation    = new Vector3f(0,0,0);
  
  /**
   * Defines the maximum angle at which the player can look up.
   */
  public static final int maxLookUp = 85;
  /**
   * Defines the minimum angle at which the player can look down.
   */
  public static final int maxLookDown = -85;
 
  
  private long lastFrame;
  private int delta = 0;
  private final int mouseSpeed = 2;
  private final float movementSpeed = 4f;

  
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
    OldThreeDee mainWindow = null;
    try {
      mainWindow = new OldThreeDee();
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

  public OldThreeDee() {

  }

  public void create() throws LWJGLException {
    //Display
    Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH,DISPLAY_HEIGHT));
    Display.setFullscreen(true);
    Display.setTitle("OLD 3D!");
    Display.create();

    //Keyboard
    Keyboard.create();

    //Mouse
    Mouse.setGrabbed(true);
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

    glEnable(GL_DEPTH_TEST);

              

    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    //glOrtho(0, DISPLAY_WIDTH, DISPLAY_HEIGHT, 0, 0, 100);
    /*
     * 30 FOV, 0.001f zNear, 100f zFar
     * +X mot høyre
     * -Y er opp.
     * og +Z er mot kamera.
     */
    gluPerspective(70f, Display.getWidth() / (float)Display.getHeight(), 0.001f, 100f);
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
    
    glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    
  }
  
  public void run() {
    while(!Display.isCloseRequested()) { //  && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)
      if(Display.isVisible()) {
        processInput();
        update();
        // Clear for rendering
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
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
      Logger.getLogger(ThreeDee.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }
  public void loadTextures() {
    
  }


  public void processKeyboard() {

  }

  public void processInput() {
    processKeyboard();
   
  
    if (Mouse.isGrabbed()) {
      float mouseDX = Mouse.getDX() * mouseSpeed * 0.16f;
      float mouseDY = Mouse.getDY() * mouseSpeed * 0.16f;
      if (cameraRotation.y + mouseDX >= 360) {
        cameraRotation.y = cameraRotation.y + mouseDX - 360;
      } else if (cameraRotation.y + mouseDX < 0) {
        cameraRotation.y = 360 - cameraRotation.y + mouseDX;
      } else {
        cameraRotation.y += mouseDX;
      }
      if (cameraRotation.x - mouseDY >= maxLookDown && cameraRotation.x - mouseDY <= maxLookUp) {
        cameraRotation.x += -mouseDY;
      } else if (cameraRotation.x - mouseDY < maxLookDown) {
        cameraRotation.x = maxLookDown;
      } else if (cameraRotation.x - mouseDY > maxLookUp) {
        cameraRotation.x = maxLookUp;
      }
    }

    if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
      float movementRate = (movementSpeed * 0.002f) * delta; // Going forwards
      System.out.println("Movement rate is: " + movementRate);
      cameraPosition.x -= movementRate * (float)Math.sin(Math.toRadians(cameraRotation.y));
      //cameraPosition.y -= movementSpeed * 0.0002f * Math.sin(Math.toRadians(cameraRotation.y));
      cameraPosition.z += movementRate * (float)Math.cos(Math.toRadians(cameraRotation.y));
      
    }
    if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
      float movementRate = -(movementSpeed * 0.002f) * delta; // Going backwards!
      cameraPosition.x -= movementRate * (float)Math.sin(Math.toRadians(cameraRotation.y));
      //cameraPosition.y -= movementSpeed * 0.0002f * Math.sin(Math.toRadians(cameraRotation.y));
      cameraPosition.z += movementRate * (float)Math.cos(Math.toRadians(cameraRotation.y));
      
    }
    
    
  }
  private void render() {
    delta = getDelta();

    System.out.println("X: " + cameraPosition.x + " Y: " + cameraPosition.y + " Z: " + cameraPosition.z);
    
    GL11.glColor3f(1.0f, 1.0f, 1.0f);
    //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE_SMOOTH);
   

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
      System.out.println("lastError = " + lastError);
    glLoadIdentity();
    
    GL11.glRotatef(cameraRotation.y, 0.0f, 1.0f, 0.0f); // Rotate around Y-axis.
    
    GL11.glTranslatef(cameraPosition.x, cameraPosition.y, cameraPosition.z);
    
    
  }  
}