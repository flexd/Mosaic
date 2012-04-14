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
import org.lwjgl.util.vector.Vector3f;
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
  private static final int FLOAT_SIZE = 4;
  private static int frameCount = 0;
  
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
    glBindAttribLocation(texShader.getProgram(), 0, "in_vertex");

    //System.err.println("lastGL error: " + glGetError());
    //System.out.println("This is frame: " + frameCount);
    texShader.use();
    
    for(Renderable r : getQueue()) {
      glBindBuffer(GL_ARRAY_BUFFER, vboHandle);
      float[] vertices = r.getVertices();
      System.out.println("Vertices begin:");
      FloatBuffer vertexData = BufferUtils.createFloatBuffer(vertices.length*FLOAT_SIZE); // 36*4  floats (3 vertices* vertices
      for(int i = 0; i < vertices.length/3;i++) {
        System.out.println("Rendering vertex " + i + " : " + vertices[i*3] + ", " + vertices[i*3+1] + ", " + vertices[i*3+2] );
        vertexData.put(new float[]{vertices[i*3], vertices[i*3+1], vertices[i*3+2]});
      }
      System.out.println("Vertices end.");
      
      FloatBuffer positionData = BufferUtils.createFloatBuffer(3*FLOAT_SIZE); // 3 floats
      FloatBuffer colorData = BufferUtils.createFloatBuffer(4*FLOAT_SIZE); // rgba
      
      
      vertexData.flip();
      Vector3f pos = r.getVectorPos();
//      positionData.put(r.getPosition());
//      positionData.flip();
//      colorData.put(r.getColor());
//      colorData.flip();
      Vector4f color = r.getColor();
      
      System.out.println("There should be a triangle at: " + pos + ", with the color: " + color);
      glUniform3f(texShader.uniformLocation("in_position"), pos.x, pos.y, pos.z); 
      glUniform4f(texShader.uniformLocation("in_color"), color.x, color.y, color.z, color.w); 
      

      glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);

      
      final int stride = (3 /* vertex Size */) * FLOAT_SIZE;

      

      int in_vertexLocation = texShader.attribLocation("in_vertex");
      glEnableVertexAttribArray(in_vertexLocation);

      glVertexAttribPointer(in_vertexLocation, 3, GL_FLOAT, false, stride, 0);
      
      
      glDrawArrays(GL_TRIANGLES, 0, renderQueue.size());
      glBindBuffer(GL_ARRAY_BUFFER, 0);
      glDisableVertexAttribArray(in_vertexLocation);
    }
    
    texShader.stop();
    renderQueue.clear();
    lastError = glGetError();
    if (lastError != GL_NO_ERROR) System.out.println("GL error: " + lastError);
    frameCount++;
  }

  //
//
//glUniform4f(texShader.uniformLocation("color"), color.x, color.y, color.z, color.w);
//
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
