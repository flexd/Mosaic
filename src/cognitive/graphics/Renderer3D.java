/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cognitive.graphics;

import cognitive.Window;
import cognitive.primitives.Renderable;

import org.newdawn.slick.opengl.Texture;
import org.cognitive.shadermanager.Shader;
import entities.Quad3D;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector4f;

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
  public LinkedList<Renderable> renderQueue = new LinkedList<Renderable>();
  private int vboHandle;
  private Texture tex;
  private int lastError = 0;
  private Shader texShader;
  
  public Renderer3D() {
    vboHandle = glGenBuffers();
    texShader = new Shader("texture3D");
  }
  public void queue(Renderable vertex) {
    renderQueue.add(vertex);
  }
  public LinkedList<Renderable> getQueue() {
    return renderQueue;
  }
  public void flushQueue() {
    
    if (renderQueue.isEmpty()) {
      return;
    }
    texShader.use();
    
    FloatBuffer vertexData = BufferUtils.createFloatBuffer(getQueue().size() * (3*4*100));
    FloatBuffer vexPosition = null;
    Vector4f color = null;
    for (Renderable v : getQueue()) {
      if (v == null) { System.err.println("Renderable is null"); }
      vertexData.put(v.getVertices());
      vexPosition = v.getPosition();
      color = v.getColor();
    }
    vertexData.flip();
    
//    if (tex != null) { 
//      glActiveTexture(GL_TEXTURE0);
//      glBindTexture(GL_TEXTURE_2D, tex.getTextureID());
//      glUniform1i(texShader.uniformLocation("mytex"), 0);
//    }

    glBindBuffer(GL_ARRAY_BUFFER, vboHandle);
    
    glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
    
    
    final int FLOAT_SIZE = 4;
    final int stride = (  3 /* vertex Size */ ) * FLOAT_SIZE;
    
    glBindAttribLocation(texShader.getProgram(), 0, "vertex");
    glUniformMatrix4(texShader.uniformLocation("position"), false, vexPosition); 
    
    glUniform4f(texShader.uniformLocation("color"), color.x, color.y, color.z, color.w);
    
    glEnableVertexAttribArray(texShader.attribLocation("vertex"));

    glVertexAttribPointer(texShader.attribLocation("vertex"), 2, GL_FLOAT, false, stride, 0);
    
    glDrawArrays(GL_TRIANGLES, 0, renderQueue.size());
   
    glBindBuffer(GL_ARRAY_BUFFER, 0);

    
    texShader.stop();
    renderQueue.clear();
    lastError = glGetError();
    if (lastError != GL_NO_ERROR) System.out.println("GL error: " + lastError);
  }

  public void queue(Renderable[] vs) {

    for (Renderable v : vs) {
      queue(v);
    }
  }
//  public void queue(Quad3D quad) {
//    Texture ntex = quad.getTexture();
//    if (ntex != null) {
//      if (ntex != tex) {
//        flushQueue();
//        tex = ntex;
//      }
//    }
//    for (Renderable v : quad.getVertices()) {
//      queue(v);
//    }
//  }
}
