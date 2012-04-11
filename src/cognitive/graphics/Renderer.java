/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cognitive.graphics;

import cognitive.Window;
import org.newdawn.slick.opengl.Texture;
import org.cognitive.shadermanager.Shader;
import entities.Quad;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import java.util.LinkedList;

import org.newdawn.slick.Color;
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
    
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    glViewport(0, 0, Window.DISPLAY_WIDTH, Window.DISPLAY_HEIGHT);
    glOrtho(0, Window.DISPLAY_WIDTH, Window.DISPLAY_HEIGHT, 0, 1, -1);
    glMatrixMode(GL_MODELVIEW);
    glEnable(GL_DEPTH_TEST);
    glDepthMask(true);
    glDepthFunc(GL_LEQUAL);
    glDepthRange(0.0f, 1.0f);
    
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    
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
    
    if (renderQueue.isEmpty()) {
      return;
    }
    texShader.use();
    
    FloatBuffer vertexData = BufferUtils.createFloatBuffer(getQueue().size() * 9);
    for (Vertex2f v : getQueue()) {
      vertexData.put(new float[]{v.x, v.y, v.r, v.g, v.b, v.a, v.u, v.v, v.tex});
    }
    vertexData.flip();
    
    if (tex != null) { 
      glActiveTexture(GL_TEXTURE0);
      glBindTexture(GL_TEXTURE_2D, tex.getTextureID());
      glUniform1i(texShader.uniformLocation("mytex"), 0);
    }

    glBindBuffer(GL_ARRAY_BUFFER, vboHandle);
    
    glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
    
    
    
    glPushAttrib(GL_ENABLE_BIT);
    glPushAttrib(GL_CURRENT_BIT);
    glDisable(GL_TEXTURE_2D);
    glDisable(GL_LIGHTING);
    final int FLOAT_SIZE = 4;
    final int stride = (  2 /* position size */ 
                        + 4 /* color Size */ 
                        + 2 /* textureCoord size */
                        + 1 /* useTex size*/) * FLOAT_SIZE;
    
    glEnableVertexAttribArray(texShader.attribLocation("aVertexPosition"));

    glVertexAttribPointer(texShader.attribLocation("aVertexPosition"), 2, GL_FLOAT, false, stride, 0);
    
    glEnableVertexAttribArray(texShader.attribLocation("aColor"));

    glVertexAttribPointer(texShader.attribLocation("aColor"), 4, GL_FLOAT, false, stride, 2*FLOAT_SIZE);
    
    glEnableVertexAttribArray(texShader.attribLocation("aTextureCoord"));

    glVertexAttribPointer(texShader.attribLocation("aTextureCoord"), 2, GL_FLOAT, false, stride, 6*FLOAT_SIZE);

    glEnableVertexAttribArray(texShader.attribLocation("useTex"));

    glVertexAttribPointer(texShader.attribLocation("useTex"), 1, GL_FLOAT, false, stride, 8*FLOAT_SIZE);


    
    glDrawArrays(GL_QUADS, 0, renderQueue.size());
   
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glPopAttrib();
    glPopAttrib();
    
    texShader.stop();
    renderQueue.clear();
    lastError = glGetError();
    if (lastError != GL_NO_ERROR) System.out.println("GL error: " + lastError);
  }

  public void queue(Vertex2f[] vs) {

    for (Vertex2f v : vs) {
      queue(v);
    }
  }
  public void queue(Quad quad) {
    Texture ntex = quad.getTexture();
    if (ntex != null) {
      if (ntex != tex) {
        flushQueue();
        tex = ntex;
      }
    }
    for (Vertex2f v : quad.getVertices()) {
      queue(v);
    }
  }
}
