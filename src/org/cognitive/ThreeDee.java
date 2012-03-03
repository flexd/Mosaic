package org.cognitive;

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


public class ThreeDee {
  public static final int DISPLAY_HEIGHT = 1440;
  public static final int DISPLAY_WIDTH = 900;
  
  public static final int WORLD_WIDTH = 10;
  public static final int WORLD_HEIGHT = 10;
  public static final int WORLD_DEPTH = 10;
  public static final Logger LOGGER = Logger.getLogger(ThreeDee.class.getName());
  private int lastError = 0;
  
  private Vector3f    position    = new Vector3f();
  private float       yaw         = 0.0f;
  private float       pitch       = 0.0f;
  private float       distance    = 0.0f;
  
  
  private static enum State {
    INTRO, MAIN_MENU, GAME;
  }
  private State state = State.GAME; // INTRO should be default.

  private long lastFrame;

  
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
    ThreeDee main = null;
    try {
      main = new ThreeDee();
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

  public ThreeDee() {

  }

  public void create() throws LWJGLException {
    //Display
    Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH,DISPLAY_HEIGHT));
    Display.setFullscreen(true);
    Display.setTitle("Hello LWJGL World!");
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
    //2D Initialization
    /*glClearColor(0.0f,0.0f,0.0f,0.0f);*/
    //glEnable(GL_DEPTH_TEST);
    //glEnable(GL_LIGHTING);
              

    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    //glOrtho(0, DISPLAY_WIDTH, DISPLAY_HEIGHT, 0, 0, 100);
    /*
     * 30 FOV, 0.001f zNear, 100f zFar
     * +X mot høyre
     * +Y er opp
     * og +Z er mot kamera.
     */
    gluPerspective(90.0f, 640f / 480f, 0.001f, 100f);
    glMatrixMode(GL_MODELVIEW);
    GL11.glRotatef(3.0f, 1.0f, 0.0f, 0.0f);
   
    
    //glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

   // glEnable(GL_TEXTURE_2D);
    //glEnable(GL_BLEND);
    //glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
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

  public void processInput() {
    processKeyboard();
    
    yaw += Mouse.getDX()* 0.01f;
    pitch -= Mouse.getDY()* 0.01f;
    position.x -= distance * (float)Math.sin(Math.toRadians(yaw));
    position.z += distance * (float)Math.cos(Math.toRadians(yaw));
    if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            distance += 0.001f;
    }
    if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
      distance -= 0.001f;
    }
    if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
        position.x -= distance * (float)Math.sin(Math.toRadians(yaw-90));
        position.z += distance * (float)Math.cos(Math.toRadians(yaw-90));
    }
    if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
        position.x -= distance * (float)Math.sin(Math.toRadians(yaw+90));
        position.z += distance * (float)Math.cos(Math.toRadians(yaw+90));
    }
      if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
              distance = 0f;
      }
      // If we're pressing the "c" key reset our speed to zero and reset our position
      if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
              position.x = 0;
              position.y = 0;
              position.z = 0;
              distance = 0;
      }
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
    glColor3f(0.0f,1.0f,0f);
    glRectf(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);
  }
  private void menuRender() {
    glColor3f(1.0f,0f,0f);
    glRectf(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);
  }
  private void gameRender() {
    int delta = getDelta();

    //glTranslatef(0,0,0.01f);
    //GL11.glRotatef(15, 0, 0, 0);
    //
    //roatate the yaw around the Y axis
    GL11.glRotatef(yaw, 0.0f, 1.0f, 0.0f);
    //translate to the position vector's location
    GL11.glTranslatef(position.x, position.y, position.z);
    System.out.println("Yaw: " + yaw + " Pitch: " + pitch);
    System.out.println("X: " + position.x + " Y: " + position.y + " Z: " + position.z);
    System.out.println("Distance: " + distance);
    GL11.glColor3f(1.0f, 1.0f, 1.0f);
    glPolygonMode(GL_FRONT_AND_BACK, GL_LINE_SMOOTH);
   
    //gluLookAt(3.0f, 2.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
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
    System.out.println(lastError);
  }  
}

