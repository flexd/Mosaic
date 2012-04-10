/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cognitive.graphics;

import org.newdawn.slick.opengl.Texture;
import org.cognitive.shadermanager.Shader;
import entities.Quad;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import java.util.LinkedList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
/**
 *
 * @author kristoffer
 */
public class Renderer {
  public LinkedList<Vertex2f> renderQueue = new LinkedList();
  private int vboHandle;
  private Texture tex;
  private int lastError = 0;
  private Shader texShader;
  
  public Renderer() {
    vboHandle = glGenBuffers();
    texShader = new Shader("texture");
  }
  public void queue(Vertex2f vertex) {
    renderQueue.add(vertex);
  }
  public LinkedList<Vertex2f> getQueue() {
    return renderQueue;
  }
  public void flushQueue() {
   // System.out.println("queue size: " + renderQueue.size() );
    if (renderQueue.isEmpty()) {
      System.out.println("Nothing to render");
      return;
    }
    texShader.use();
    System.out.println("Rendering: " + getQueue().size() + " vertexes");
    FloatBuffer vertexData = BufferUtils.createFloatBuffer(getQueue().size() * 9);
    for (Vertex2f v : getQueue()) {
      vertexData.put(new float[]{v.x, v.y});
    }
    vertexData.flip();
    
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, tex.getTextureID());
    glUniform1i(texShader.uniformLocation("mytex"), 0);
    glBindBuffer(GL_ARRAY_BUFFER, vboHandle);
    
    glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
    
    
    
    glPushAttrib(GL_ENABLE_BIT);
    glPushAttrib(GL_CURRENT_BIT);
    glDisable(GL_TEXTURE_2D);
    glDisable(GL_LIGHTING);
    
    glVertexAttribPointer(texShader.attribLocation("aVertexPosition"), 2, GL_FLOAT, false, 0, 0);
    glEnableVertexAttribArray(texShader.attribLocation("aVertexPosition"));
    
    glVertexAttribPointer(texShader.attribLocation("aTextureCoord"), 2, GL_FLOAT, false, 0, 2);
    glEnableVertexAttribArray(texShader.attribLocation("aTextureCoord"));
    
    glVertexAttribPointer(texShader.attribLocation("aColor"), 4, GL_FLOAT, false, 0, 4);
    glEnableVertexAttribArray(texShader.attribLocation("aColor"));
    
    glVertexAttribPointer(texShader.attribLocation("useTex"), 1, GL_FLOAT, false, 0, 8);
    glEnableVertexAttribArray(texShader.attribLocation("useTex"));
    //glVertexPointer(2, GL_FLOAT, 0, 0L);

    
    glEnableClientState(GL_VERTEX_ARRAY);
    glEnableClientState(GL_COLOR_ARRAY);
    glDrawArrays(GL_QUADS, 0, renderQueue.size());
   
    glDisableClientState(GL_VERTEX_ARRAY);
    glDisableClientState(GL_COLOR_ARRAY);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glPopAttrib();
    glPopAttrib();

    texShader.stop();
    renderQueue.clear();
    lastError = glGetError();
    if (lastError != GL_NO_ERROR) System.out.println("GL error: " + lastError);
  }
//  public void flushQueue() {
//   // System.out.println("queue size: " + renderQueue.size() );
//    if (renderQueue.isEmpty()) {
//      System.out.println("Nothing to render");
//      return;
//    }
////    texShader.use();
//    System.out.println("Rendering: " + getQueue().size() + " vertexes");
//    FloatBuffer colorData  = BufferUtils.createFloatBuffer(getQueue().size() * 4);
//    FloatBuffer vertexData = BufferUtils.createFloatBuffer(getQueue().size() * 2);
//    for (Vertex2f v : getQueue()) {
//      vertexData.put(new float[]{v.x, v.y});
//      colorData.put(new float[]{v.r, v.g, v.b, v.a});
//    }
//    vertexData.flip();
//    colorData.flip();
//    
//    glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
//    
//    glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
//    
//    glBindBuffer(GL_ARRAY_BUFFER, 0);
//
//    glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
//
//    glBufferData(GL_ARRAY_BUFFER, colorData, GL_STATIC_DRAW);
//    glBindBuffer(GL_ARRAY_BUFFER, 0);
//    
//    glPushAttrib(GL_ENABLE_BIT);
//    glPushAttrib(GL_CURRENT_BIT);
//    glDisable(GL_TEXTURE_2D);
//    glDisable(GL_LIGHTING);
//    
//    
//    glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
//    
//    
//    glVertexPointer(2, GL_FLOAT, 0, 0L);
//
//    
//    glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
//    glColorPointer(4, GL_FLOAT, 0, 0L);
//    
//    glEnableClientState(GL_VERTEX_ARRAY);
//    glEnableClientState(GL_COLOR_ARRAY);
//    glDrawArrays(GL_QUADS, 0, renderQueue.size());
//   
//    glDisableClientState(GL_VERTEX_ARRAY);
//    glDisableClientState(GL_COLOR_ARRAY);
//    glPopAttrib();
//    glPopAttrib();
//
//
//    renderQueue.clear();
//    lastError = glGetError();
//    if (lastError != GL_NO_ERROR) System.out.println("GL error: " + lastError);
//  }
  void queue(Vertex2f[] vs) {

    for (Vertex2f v : vs) {
      queue(v);
    }
  }
  void queue(Quad quad) {
    Texture ntex = quad.getTexture();
    if (ntex != null) {
      if (ntex != tex) {
        System.out.println("Different texture, switching!");
        flushQueue();
        tex = ntex;
      }
    }
    for (Vertex2f v : quad.getVertices()) {
      queue(v);
    }
  }
}
