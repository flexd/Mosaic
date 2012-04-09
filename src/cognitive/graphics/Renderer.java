/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cognitive.graphics;

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
  private LinkedList<Vertex2f> renderQueue = new LinkedList();
  private int vboVertexHandle;
  private int vboColorHandle;
  
  public Renderer() {
    vboVertexHandle = glGenBuffers();
    vboColorHandle = glGenBuffers();
  }
  public void queue(Vertex2f vertex) {
    renderQueue.add(vertex);
  }
  public void flushQueue() {
    if (renderQueue.isEmpty()) {
      System.out.println("Render queue is empty!");
      return;
    }
    FloatBuffer vertexData;
    FloatBuffer colorData;
    vertexData = BufferUtils.createFloatBuffer(renderQueue.size()*2);
    for (Vertex2f v : renderQueue) {
      vertexData.put(new float[]{v.x, v.y});
    }
    if (vertexData.remaining() == 0) return; 
    colorData = BufferUtils.createFloatBuffer(renderQueue.size() * 3);
    colorData.put(new float[]{1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0});
    colorData.flip();
    
    glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
    glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    
    
    glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
    glBufferData(GL_ARRAY_BUFFER, colorData, GL_STATIC_DRAW);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    
    glPushAttrib(GL_ENABLE_BIT);
    glPushAttrib(GL_CURRENT_BIT);
    glDisable(GL_TEXTURE_2D);
    glDisable(GL_LIGHTING);
    glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
    glVertexPointer(2, GL_FLOAT, 0, 0L);
    glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
    glColorPointer(3, GL_FLOAT, 0, 0L);
    
    glEnableClientState(GL_VERTEX_ARRAY);
    glDrawArrays(GL_QUADS, 0, renderQueue.size());
    if (glGetError() != GL_NO_ERROR) { System.out.println("OpenGL errors!"); }
    glDisableClientState(GL_VERTEX_ARRAY);
    renderQueue.clear();
  }
}
