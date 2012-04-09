/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cognitive.graphics;

import entities.Quad;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import java.util.LinkedList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

/**
 *
 * @author kristoffer
 */
public class Renderer {
  public LinkedList<Vertex2f> renderQueue = new LinkedList();
  private int vboVertexHandle;
  private int vboColorHandle;
  private int lastError = 0;
  public Renderer() {
    vboVertexHandle = glGenBuffers();
    vboColorHandle = glGenBuffers();
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
    System.out.println("Rendering: " + getQueue().size() + " vertexes");
    FloatBuffer colorData = BufferUtils.createFloatBuffer(getQueue().size() * 4);
    FloatBuffer vertexData = BufferUtils.createFloatBuffer(getQueue().size()*2);
    for (Vertex2f v : getQueue()) {
      vertexData.put(new float[]{v.x, v.y});
      colorData.put(new float[]{v.r, v.g, v.b, v.a});
    }
    vertexData.flip();
    colorData.flip();
    
    glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
    
    glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
    
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    if (lastError != GL_NO_ERROR) System.out.println("glError binding vertexBufferData");
    
    glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
    
    glBufferData(GL_ARRAY_BUFFER, colorData, GL_STATIC_DRAW);
    
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    lastError = glGetError();
    if (lastError != GL_NO_ERROR) System.out.println("glError binding colorBufferData");
    
    glPushAttrib(GL_ENABLE_BIT);
    glPushAttrib(GL_CURRENT_BIT);
    glDisable(GL_TEXTURE_2D);
    glDisable(GL_LIGHTING);
    glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
    
    glVertexPointer(2, GL_FLOAT, 0, 0L);
    lastError = glGetError();
    if (lastError != GL_NO_ERROR) System.out.println("glError vertexPointer(): " + lastError);
    glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
    
    glColorPointer(3, GL_FLOAT, 0, 0L);
    lastError = glGetError();
    if (lastError != GL_NO_ERROR) System.out.println("glError colorPointer");
    glEnableClientState(GL_VERTEX_ARRAY);
    
    glDrawArrays(GL_QUADS, 0, renderQueue.size());
   
    glDisableClientState(GL_VERTEX_ARRAY);
    
    glPopAttrib();
    glPopAttrib();
    renderQueue.clear();
  }

  void queue(Quad quad) {
    for (Vertex2f v : quad.getVertices()) {
      queue(v);
    }
  }
}
