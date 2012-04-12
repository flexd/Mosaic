/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cognitive.graphics;

import cognitive.Utilities;
import entities.AbstractEntity;
import entities.Quad2D;
import cognitive.Window;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import java.awt.Font;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

/**
 *
 * @author kristoffer
 */
public class Graphics {

  public static Camera2D camera = new Camera2D(0,0);
  
  private static org.newdawn.slick.Font bigFont = new TrueTypeFont(new Font("Georgia", 1, 20), false);
  private static org.newdawn.slick.Font mediumFont = new TrueTypeFont(new Font("Georgia", 1, 16), false);
  private static org.newdawn.slick.Font smallFont = new TrueTypeFont(new Font("Georgia", 1, 10), false);
  
  private int fps;
  private long lastFPS;

  private int fpsCounter;
 
  
  public Renderer2D renderer;
  public Graphics() {
    renderer = new Renderer2D();
    
    lastFPS = Utilities.getTime();
  }

  public void render(int delta) {
    glClearColor(0.5f, 0.5f, 0.8f, 1.0f);
    glClearDepth(1.0f);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    update();

   // Like a cake, this has layers. We want ground at the bottom so it goes first.
   for (AbstractEntity e : Window.ground) {
      e.draw();
    }
    for (AbstractEntity e : Window.entities) {
      e.update(delta);
      e.draw();
    }
    if (Window.leftButtonHeld) {
      drawLineBox(Window.iMouseX, Window.iMouseY, Window.mouseX, Window.mouseY, true);
    }
    renderer.flushQueue();
    drawFPS(fps);
  }
  

  
  public void updateFPS() {
    if (Utilities.getTime() - lastFPS > 1000) {
      fps = fpsCounter;
      fpsCounter = 0;
      lastFPS += 1000;
    }
    fpsCounter++;
  }
  private static FloatBuffer asFloatBuffer(float[] values) {
    FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
    buffer.put(values);
    buffer.flip();
    return buffer;
  }
  public void update() {
    
    updateFPS();
  }
  //<editor-fold defaultstate="collapsed" desc="drawString">

  public static enum FontSize {

    Small, Medium, Large;
  }

  public static void drawStatic(int x, int y, String string, Graphics.FontSize size, Color color) {
    drawString(x-camera.offsetX, y-camera.offsetY, string, size, color);
  }
  
  public static void drawString(int x, int y, String string, Graphics.FontSize size, Color color) {

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
  //</editor-fold>
  //<editor-fold defaultstate="collapsed" desc="various drawing methods">
  public void drawDot(float x, float y, float r, float g, float b, float a, float size) {
	Quad2D quad = new Quad2D(x, y, size, size, r, g, b, a);
    renderer.queue(quad);
  }
  public void drawFPS(int fps) {
    drawStatic(1, 0, "FPS: " + fps, FontSize.Medium, Color.yellow);
  }

  public void drawLineBox(int x0, int y0, int x1, int y1, boolean stippled) {
//    glPushMatrix();
    glPushAttrib(GL_ENABLE_BIT);
    glPushAttrib(GL_POLYGON_BIT);
    glPushAttrib(GL_CURRENT_BIT);
    glDisable(GL_LIGHTING);
    if (stippled) {
      glLineStipple(1, (short) 0xAAAA);
      glEnable(GL_LINE_STIPPLE);
    }
    glDisable(GL_TEXTURE_2D);
    glColor3d(1.0, 1.0, 1.0); // White is nice

    glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
    // GL11.glRecti(iMouseX, iMouseY, mouseX , mouseY);
    glRecti(x0, y0, x1, y1);

    glPopAttrib();
    glPopAttrib();
    glPopAttrib();
  }
  //</editor-fold>
  
  
}


//    FloatBuffer ambientLight = BufferUtils.createFloatBuffer(4);
//    ambientLight.put(0.9f).put(0.9f).put(0.9f).put(1.0f).flip();
//    glLightModel(GL_LIGHT_MODEL_AMBIENT, ambientLight);
    //<editor-fold defaultstate="collapsed" desc="not in use atm">
    //
    //    FloatBuffer specularColor = BufferUtils.createFloatBuffer(4);
    //    FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
    //    FloatBuffer diffuseColor = BufferUtils.createFloatBuffer(4);
    //
    //    FloatBuffer constantAttenuation = BufferUtils.createFloatBuffer(4);
    //    FloatBuffer linearAttenuation = BufferUtils.createFloatBuffer(4);
    //    FloatBuffer quadraticAttenuation = BufferUtils.createFloatBuffer(4);
    //
    //    constantAttenuation.put(1.0f).put(1.0f).put(0.0f).put(1.0f).flip();
    //
    //    linearAttenuation.put(1.0f).put(0.0f).put(0.0f).put(1.0f).flip();
    //
    //    quadraticAttenuation.put(1.0f).put(0.0f).put(0.0f).put(1.0f).flip();
    //
    //    glLight(GL_LIGHT0, GL_CONSTANT_ATTENUATION, constantAttenuation);
    //    glLight(GL_LIGHT0, GL_LINEAR_ATTENUATION, linearAttenuation);
    //    glLight(GL_LIGHT0, GL_QUADRATIC_ATTENUATION, quadraticAttenuation);
    //

    //
    //    lightPosition.put(mouseX).put(mouseY).put(1.0f).put(1.0f).flip();
    //
    //    specularColor.put(3.0f).put(3.0f).put(3.0f).put(1.0f).flip();
    //
    //    diffuseColor.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
    //
    //
    //    glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    //    glLight(GL_LIGHT0, GL_SPECULAR, specularColor);
    //    glLight(GL_LIGHT0, GL_DIFFUSE, diffuseColor);
    //
    //    glEnable(GL_LIGHT0);
    //    glEnable(GL_COLOR_MATERIAL);
    //
    //</editor-fold>
