/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cognitive.graphics;

import entities.AbstractEntity;
import cognitive.Window;
import org.cognitive.shadermanager.Shader;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import java.awt.Font;
import java.nio.ByteBuffer;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

/**
 *
 * @author kristoffer
 */
public class Graphics {

  public static Camera camera = new Camera(0,0);
  Shader lightingShader = new Shader("lighting");;
  //<editor-fold defaultstate="collapsed" desc="font declarations and inits">
  private static org.newdawn.slick.Font bigFont = new TrueTypeFont(new Font("Georgia", 1, 20), false);
  private static org.newdawn.slick.Font mediumFont = new TrueTypeFont(new Font("Georgia", 1, 16), false);
  private static org.newdawn.slick.Font smallFont = new TrueTypeFont(new Font("Georgia", 1, 10), false);
  //</editor-fold>

  private Renderer renderer;
  public Graphics() {
    renderer = new Renderer();
  }

  public void render(int delta) {
//    for (int i = 0; i < 1; i++) {
//      renderer.queue(new Vertex2f(100+(i*20), 100+(i*20)));
//      renderer.queue(new Vertex2f(100+(i*20) + 50, 100+(i*20)));
//      renderer.queue(new Vertex2f(100+(i*20) + 50, 100+(i*20) + 50));
//      renderer.queue(new Vertex2f(100+(i*20), 100+(i*20) + 50));
//    }

    
    for (AbstractEntity e : Window.entities) {
      e.update(delta);
      renderer.queue(e.dance());
    }

    
    renderer.flushQueue();

    

  }
  private static FloatBuffer asFloatBuffer(float[] values) {
    FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
    buffer.put(values);
    buffer.flip();
    return buffer;
  }
  public void update() {
    
  }
  //<editor-fold defaultstate="collapsed" desc="drawString">

  public static enum FontSize {

    Small, Medium, Large;
  }

  public static void drawStatic(int x, int y, String string, Graphics.FontSize size, Color color) {
    drawString(x-camera.offsetX, y-camera.offsetY, string, size, color);
  }
  
  public static void drawString(int x, int y, String string, Graphics.FontSize size, Color color) {
    glPushAttrib(GL_ENABLE_BIT);
    glPushAttrib(GL_CURRENT_BIT);
    glDisable(GL_LIGHTING);
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
    glPopAttrib();
    glPopAttrib();
  }
  //</editor-fold>
  //<editor-fold defaultstate="collapsed" desc="various draw methods">

  public static void drawFPS(int fps) {
    drawStatic(500, 1, "FPS: " + fps, FontSize.Medium, Color.yellow);
  }

  public static void drawLineBox(int x0, int y0, int x1, int y1, boolean stippled) {
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
//  final int amountOfVertices = 4;
//  final int vertexSize = 3;
//  final int colorSize = 3;
//  private FloatBuffer vertexData;
//  private FloatBuffer colorData;
//  
//  int vboVertexHandle;
//  int vboColorHandle;
///////////////

//    vertexData = BufferUtils.createFloatBuffer(amountOfVertices * vertexSize);
//    vertexData.put(new float[]{400,400, 0,450,400, 0,450,450,0,400,450,0}); // YES?
////            glVertex2d(400, 400);
////            glVertex2d(400 + 50, 400);
////            glVertex2d(400 + 50, 400 + 50);
////            glVertex2d(400, 400 + 50);
//    vertexData.flip();
//    
//    colorData = BufferUtils.createFloatBuffer(amountOfVertices * colorSize);
//    colorData.put(new float[]{1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0});
//    colorData.flip();
//    
//    vboVertexHandle = glGenBuffers();
//    glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
//    glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
//    glBindBuffer(GL_ARRAY_BUFFER, 0);
//    
//    vboColorHandle = glGenBuffers();
//    glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
//    glBufferData(GL_ARRAY_BUFFER, colorData, GL_STATIC_DRAW);
//    glBindBuffer(GL_ARRAY_BUFFER, 0);
/////////////////////

//      // rendering
//    glPushAttrib(GL_ENABLE_BIT);
//    glPushAttrib(GL_CURRENT_BIT);
//    glDisable(GL_TEXTURE_2D);
//    glDisable(GL_LIGHTING);
//    glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
//    glVertexPointer(vertexSize, GL_FLOAT, 0, 0L);
//    glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
//    glColorPointer(colorSize, GL_FLOAT, 0, 0L);
//    
//    glEnableClientState(GL_VERTEX_ARRAY);
//    glEnableClientState(GL_COLOR_ARRAY);
//    glDrawArrays(GL_QUADS, 0, amountOfVertices);
//    //if (glGetError() == GL_NO_ERROR) { System.out.println("No errors!"); }
//    glDisableClientState(GL_VERTEX_ARRAY);
//    glDisableClientState(GL_COLOR_ARRAY);
//
//  
//    
//    glBegin(GL_QUADS);
//      glVertex2d(200, 200);
//      glVertex2d(200 + 50, 200);
//      glVertex2d(200 + 50, 200 + 50);
//      glVertex2d(200, 200 + 50);
//    glEnd();
//
//
//    glPopAttrib();
//    glPopAttrib();
    
//    glPushAttrib(GL_ENABLE_BIT);
//    glPushAttrib(GL_CURRENT_BIT);
//    glDisable(GL_TEXTURE_2D);
//    glDisable(GL_LIGHTING);
//    lightingShader.begin();
//
//    glBegin(GL_QUADS);
//      glVertex2d(400, 400);
//      glVertex2d(400 + 50, 400);
//      glVertex2d(400 + 50, 400 + 50);
//      glVertex2d(400, 400 + 50);
//    glEnd();
//
//    lightingShader.end();
//
//    glPopAttrib();
//    glPopAttrib();

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
