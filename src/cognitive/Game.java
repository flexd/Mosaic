package cognitive;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import java.util.LinkedList;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import org.lwjgl.opengl.GL11;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;

import cognitive.graphics.Camera3D;

import cognitive.graphics.Chunk;
import cognitive.graphics.ChunkRenderer;
import cognitive.graphics.Renderer3D;

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

  private ChunkRenderer renderer;

  private ArrayList<Cube> cubes = new ArrayList<Cube>();
  private int lastPos = 0;
  
  private int fps = 0;
  private long lastFPS = 0;

  private int fpsCounter = 0;

  private int cubeCount = 0;
  private LinkedList<Chunk> chunks = new LinkedList<Chunk>();

  private static org.newdawn.slick.Font bigFont = new UnicodeFont(new Font("Georgia", 1, 20));
  private static org.newdawn.slick.Font mediumFont = new UnicodeFont(new Font("Georgia", 1, 16));
  private static org.newdawn.slick.Font smallFont = new UnicodeFont(new Font("Georgia", 1, 10));
  
  private static Matrix4f projectionMatrix;
  
  public void updateFPS() {

    if (Util.getTime() - lastFPS > 1000) {
      fps = fpsCounter;
      fpsCounter = 0;
      lastFPS += 1000;
    }
    fpsCounter++;
  }
  
  private float getDelta() {
    long currentTime = Util.getTime();
    float delta = (float) (currentTime - lastFrame);
    lastFrame = Util.getTime();
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
   // glEnable(GL_LIGHTING);

    /*
     * 70 FOV, 0.001f zNear, 200f zFar
     * +X mot h¿yre
     * -Y er opp.
     * og +Z er mot kamera.
     */
    projectionMatrix = LoadPerspective(fov, Display.getWidth() / (float)Display.getHeight(), zNear, zFar);
        
    camera = new Camera3D (new Vector3f(0,0,-10));
    camera.initMatrix(projectionMatrix);
    renderer = new ChunkRenderer();
    renderer.initMatrix(projectionMatrix);
    
    lastFrame = Util.getTime();
    lastFPS = Util.getTime();
    //resizeGL();
    for(int x = 0; x < 1; x++) {
      for(int y = 0; y < 1; y++) {
        for(int z = 0; z < 1; z++) {
          chunks.add(new Chunk(new Vector3f(x*100, y*100, z*100)));
        }
      }
    }
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
      //Display.sync(60);
    }
  }

  private void render() {
    delta = getDelta();
    glClearColor(0,0,0,0);

    for (Chunk c : chunks) {
      renderer.queue(c);
    }
    System.out.println("Chunk count: " + chunks.size());
    //System.out.println("That's " + 64*chunks.size() + " cubes.");
   // System.out.println("That's " + 64*108*chunks.size() + " vertices.");
//    renderer.queue(new Chunk(new Vector3f(10, 10, 10)));
//    renderer.queue(new Plane(new Vector3f(0,-3,0), 0.2f, 0.2f, 0.3f, 1, 100, 100)); 
  }

  private void cubeGrid(Vector3f position, int width, int height, int depth) {
    for(int i = 0; i < width;i++) {
      for (int y = 0; y < 1;y++) {
        Cube cube = new Cube(new Vector3f(position.x+i*10,position.y+y*10,position.z+0), 1, 0, 1, 1, 2);
        cubes.add(cube);
      }
      for (int y = 0; y < height;y++) {
        Cube cube = new Cube(new Vector3f(position.x+i*10,position.y+0,position.z+y*10), 1, 0, 1, 1, 2);
        cubes.add(cube);
        for (int z = 0; z < depth;z++) {
          Cube cube2 = new Cube(new Vector3f(position.x+i*10,position.y+y*10,position.z+z*10), 1, 0, 1, 1, 2);
          cubes.add(cube2);
        }
      }
      Cube cube = new Cube(new Vector3f(position.x+i*10,position.y+0,position.z+0), 1, 0, 1, 1, 2);
      
      cubes.add(cube);
      
    }
  }  
}

