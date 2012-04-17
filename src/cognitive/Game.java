package cognitive;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetError;

import java.awt.Font;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;

import cognitive.graphics.Camera3D;
import cognitive.graphics.Renderer3D;
import cognitive.graphics.texturemanager.Sprite;
import cognitive.primitives.Cube;
import cognitive.primitives.Plane;


public class Game {
  private static final float zFar = 1000f;
  private static final float zNear = 0.002f;
  private static final float fov = 70f;
  public static final int DISPLAY_HEIGHT = 600;
  public static final int DISPLAY_WIDTH = 800;
  
  public static final int WORLD_WIDTH = 10;
  public static final int WORLD_HEIGHT = 10;
  public static final int WORLD_DEPTH = 10;
  public static final Logger LOGGER = Logger.getLogger(Game.class.getName());
  private int lastError = 0;
  
  private long lastFrame;
  private float delta = 0;
  private Camera3D camera;
  private Renderer3D renderer;
  public static TextureManager tm = new TextureManager();
  private Sprite manSprite;
  private ArrayList<Cube> cubes = new ArrayList<Cube>();
  private int lastPos = 0;
  
  private int fps = 0;
  private long lastFPS = 0;

  private int fpsCounter = 0;
  private static org.newdawn.slick.Font bigFont = new UnicodeFont(new Font("Georgia", 1, 20));
  private static org.newdawn.slick.Font mediumFont = new UnicodeFont(new Font("Georgia", 1, 16));
  private static org.newdawn.slick.Font smallFont = new UnicodeFont(new Font("Georgia", 1, 10));
  
  private static Matrix4f projectionMatrix;
  
  public void updateFPS() {
    if (Utilities.getTime() - lastFPS > 1000) {
      fps = fpsCounter;
      fpsCounter = 0;
      lastFPS += 1000;
    }
    fpsCounter++;
  }
  
  private float getDelta() {
    long currentTime = Utilities.getTime();
    float delta = (float) (currentTime - lastFrame);
    lastFrame = Utilities.getTime();
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
    Game mainWindow = null;
    try {
      mainWindow = new Game();
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

  public Game() {

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
    
    glEnable(GL_DEPTH_TEST);

    //glMatrixMode(GL_PROJECTION);
    //glLoadIdentity();
    //glOrtho(0, DISPLAY_WIDTH, DISPLAY_HEIGHT, 0, 0, 100);
    /*
     * 30 FOV, 0.001f zNear, 100f zFar
     * +X mot høyre
     * -Y er opp.
     * og +Z er mot kamera.
     */
    //gluPerspective(70f, Display.getWidth() / (float)Display.getHeight(), 0.001f, 1000f);
    projectionMatrix = LoadPerspective(fov, Display.getWidth() / (float)Display.getHeight(), zNear, zFar);
//    glMatrixMode(GL_MODELVIEW);
//    glLoadIdentity();
    
//    glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    
    
    camera = new Camera3D (new Vector3f(0,0,-10));
    camera.initMatrix(projectionMatrix);
    renderer = new Renderer3D();
    renderer.initMatrix(projectionMatrix);
    
    lastFrame = Utilities.getTime();
    lastFPS = Utilities.getTime();
    //resizeGL();
    
//    tm.load("crate", "crate", 256); // spritesheet name, filename, slotSize
//    tm.load("world", "default", 32); // spritesheet name, filename, slotSize
//    tm.load("hero", "generichero-blackblue", 32); // spritesheet name, filename, slotSize
//    tm.define("characters", "player",0,0); // Sheet named "world", "name of sprite", slot 0,0 in spritesheet.
//    tm.define("hero", "hero", 0, 0 ); // Sheet named "world", "name of sprite", slot 0,0 in spritesheet.
//    tm.define("crate", "crate", 0,0);
    
//    manSprite = tm.getSpriteByName("crate");
  }

  public void destroy() {
    //Methods already check if created before destroying.
    Mouse.destroy();
    Keyboard.destroy();
    Display.destroy();
  }
  public static enum FontSize {

    Small, Medium, Large;
  }

 public void drawString(int x, int y, String string, FontSize size, Color color) {

    switch (size) {
      case Small:
        smallFont.drawString(x, y, string, color);
        break;
      case Medium:
        mediumFont.drawString(x, y, string, color);
        break;
      case Large:
        bigFont.drawString(x, y, string, color);
        break;
      default:
        mediumFont.drawString(x, y, string, Color.yellow);
        break;
    }   
  }
 public FloatBuffer getProjectionMatrix() {
   FloatBuffer out = BufferUtils.createFloatBuffer(16*4);
   projectionMatrix.store(out);
   return (FloatBuffer) out.flip();
 }
 public Matrix4f LoadPerspective(float fov, float aspect, float zNear, float zFar)
 {
     float f = (float) (1.0f / Math.tan(Math.toRadians(fov)/2.0f));
     Matrix4f out = new Matrix4f();
     
     out.m00 = f / aspect;
     out.m01 = 0;
     out.m02 = 0;
     out.m03 = 0;
     
     out.m10 = 0;
     out.m11 = f;
     out.m12 = 0;
     out.m13 = 0;
     
     out.m20 = 0;
     out.m21 = 0;
     out.m22 = zFar/(zNear-zFar);
     out.m23 = -1.0f;
     
     out.m30 = 0;
     out.m31 = 0;
     out.m32 = zNear*zFar/(zNear-zFar);
     out.m33 = 0;
     return out;
 }
  public void run() {
    while(!Display.isCloseRequested()) { //  && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)
      
      if(Display.isVisible()) {
        updateFPS();
        
      
        // Clear for rendering
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
        render();
        renderer.flushQueue(camera.getCameraMatrix());
        camera.update(delta); // ALWAYS LAST
        System.out.println("FPS: " + fps);
      }
      else {
        if(Display.isDirty()) {
          render();
          renderer.flushQueue(camera.getCameraMatrix());
          camera.update(delta); // ALWAYS LAST
        }
        try {
          Thread.sleep(100);
        }
        catch(InterruptedException ex) {
        }
      }
      projectionMatrix = LoadPerspective(fov, Display.getWidth() / (float)Display.getHeight(), zNear, zFar);
      Display.update();
      Display.sync(60);
    }
  }

  private void render() {
    delta = getDelta();
    glClearColor(1,1,1,0);
    
//    if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
//      amountofRows++;
//    }
//      for(int i = 0; i < lastPos/10;i++) {
//        for (int y = 0; y < lastPos/10;y++) {
//          cubes.add(new Cube(new Vector3f(lastPos+10*y,0,i*10), 1, 0, 1, 1, 2));
//          cubes.add(new Cube(new Vector3f(10*i,0,lastPos+10*y), 1, 0, 1, 1, 2));
//        }
//        
//        //cubes.add(new Cube(new Vector3f(lastPos+10*i,0,lastPos), 1, 0, 1, 1, 2));
//      }
//      //cubes.add(new Cube(new Vector3f(0,0,lastPos+10), lastPos*0.02f, 0, 1, 1, 2));
//      lastPos += 10;
        renderer.queue(new Plane(new Vector3f(), 1, 0, 0, 1, 100, 100));
//      for(int i = 0; i < 10;i++) {
//        for (int y = 0; y < 10;y++) {
//          Cube cube = new Cube(new Vector3f(i*10,y*10,0), 1, 0, 1, 1, 2);
//          renderer.queue(cube);
//        }
//        for (int y = 0; y < 10;y++) {
//          Cube cube = new Cube(new Vector3f(i*10,0,y*10), 1, 0, 1, 1, 2);
//          renderer.queue(cube);
//          for (int z = 0; z < 10;z++) {
//            Cube cube2 = new Cube(new Vector3f(i*10,y*10,z*10), 1, 0, 1, 1, 2);
//            renderer.queue(cube2);
//          }
//        }
//        Cube cube = new Cube(new Vector3f(i*10,0,0), 1, 0, 1, 1, 2);
//        
//        renderer.queue(cube);
//        
//      }
//    for(Cube c : cubes) {
//      renderer.queue(c);
//    }
    System.out.println("Cube count: " + cubes.size());
    //renderer.queue(new Quad3D(0, 0, 0, 10, 10, 10, 1, 1, 1, 1, manSprite));
//    renderer.queue(new Cube(new Vector3f(10,0,10), 1, 0, 1, 1, 2));
//    renderer.queue(new Cube(new Vector3f(20,00,10), 1, 0, 1, 1, 2));
//    renderer.queue(new Cube(new Vector3f(30,00,10), 1, 0, 1, 1, 2));
    
    lastError = glGetError();
    //System.out.println("lastError = " + lastError);
  
    
    
  }  
}

