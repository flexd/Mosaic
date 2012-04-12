/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cognitive.graphics;

import cognitive.Window;
import org.newdawn.slick.opengl.Texture;
import org.cognitive.shadermanager.Shader;
import entities.Quad3D;

import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import java.util.LinkedList;

import org.newdawn.slick.Color;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
/**
 *
 * @author kristoffer
 */
public class Renderer3D {
  public LinkedList<Vertex3f> renderQueue = new LinkedList<Vertex3f>();
  private int vboHandle;
  private Texture tex;
  private int lastError = 0;
  private Shader texShader;
  
  public Renderer3D() {
    vboHandle = glGenBuffers();
    texShader = new Shader("texture3D");
  }
  public void queue(Vertex3f vertex) {
    renderQueue.add(vertex);
  }
  public LinkedList<Vertex3f> getQueue() {
    return renderQueue;
  }
  public void flushQueue() {
    
    if (renderQueue.isEmpty()) {
      return;
    }
    texShader.use();
    
    FloatBuffer vertexData = BufferUtils.createFloatBuffer(getQueue().size() * 10);
    for (Vertex3f v : getQueue()) {
      if (v == null) { System.err.println("vertex is null"); }
      vertexData.put(new float[]{v.x, v.y, v.z, v.r, v.g, v.b, v.a, v.u, v.v, v.tex});
    }
    vertexData.flip();
    
    if (tex != null) { 
      glActiveTexture(GL_TEXTURE0);
      glBindTexture(GL_TEXTURE_2D, tex.getTextureID());
      glUniform1i(texShader.uniformLocation("mytex"), 0);
    }

    glBindBuffer(GL_ARRAY_BUFFER, vboHandle);
    
    glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
    
    
    final int FLOAT_SIZE = 4;
    final int stride = (  3 /* position size */ 
                        + 4 /* color Size */ 
                        + 2 /* textureCoord size */
                        + 1 /* useTex size*/) * FLOAT_SIZE;
    
    glEnableVertexAttribArray(texShader.attribLocation("aVertexPosition"));

    glVertexAttribPointer(texShader.attribLocation("aVertexPosition"), 3, GL_FLOAT, false, stride, 0);
    
    glEnableVertexAttribArray(texShader.attribLocation("aColor"));

    glVertexAttribPointer(texShader.attribLocation("aColor"), 4, GL_FLOAT, false, stride, 3*FLOAT_SIZE);
    
    glEnableVertexAttribArray(texShader.attribLocation("aTextureCoord"));

    glVertexAttribPointer(texShader.attribLocation("aTextureCoord"), 2, GL_FLOAT, false, stride, 7*FLOAT_SIZE);

    glEnableVertexAttribArray(texShader.attribLocation("useTex"));

    glVertexAttribPointer(texShader.attribLocation("useTex"), 1, GL_FLOAT, false, stride, 9*FLOAT_SIZE);


    
    glDrawArrays(GL_TRIANGLES, 0, renderQueue.size());
   
    glBindBuffer(GL_ARRAY_BUFFER, 0);

    
    texShader.stop();
    renderQueue.clear();
    lastError = glGetError();
    if (lastError != GL_NO_ERROR) System.out.println("GL error: " + lastError);
  }

  public void queue(Vertex3f[] vs) {

    for (Vertex3f v : vs) {
      queue(v);
    }
  }
  public void queue(Quad3D quad) {
    Texture ntex = quad.getTexture();
    if (ntex != null) {
      if (ntex != tex) {
        flushQueue();
        tex = ntex;
      }
    }
    for (Vertex3f v : quad.getVertices()) {
      queue(v);
    }
  }
}
