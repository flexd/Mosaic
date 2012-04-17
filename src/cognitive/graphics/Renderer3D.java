/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cognitive.graphics;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.FloatBuffer;
import java.util.LinkedList;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.opengl.Texture;

import cognitive.graphics.shadermanager.Shader;
import cognitive.primitives.Renderable;
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
  private Matrix4f projectionMatrix;
  private static final int FLOAT_SIZE = 4;
  private static int frameCount = 0;

  public void initMatrix(Matrix4f projectionMatrix) {
    this.projectionMatrix = projectionMatrix;
  }
  
  public Renderer3D() {
    vboHandle = glGenBuffers();
    texShader = new Shader("lighting");
  }
  public void queue(Renderable vertex) {
    renderQueue.add(vertex);
  }
  public LinkedList<Renderable> getQueue() {
    return renderQueue;
  }
  
  public void flushQueue(Matrix4f cameraProjMatrix) {
    
    if (renderQueue.isEmpty()) {
      return;
    }
    
    glBindAttribLocation(texShader.getProgram(), 0, "in_vertex");

    //System.err.println("lastGL error: " + glGetError());
    //System.out.println("This is frame: " + frameCount);
    glEnable(GL_CULL_FACE);
    texShader.use();
    float[] vertices;
    float[] normals;
    for(Renderable r : getQueue()) {
      glBindBuffer(GL_ARRAY_BUFFER, vboHandle);
      int indiceCount = 0;
       vertices = r.getVertices();
       normals = r.getNormals();
//      System.out.println("Vertices begin:");
      if (vertices == null) {
        System.err.println("There's no vertices in this, this is BAD!");
        renderQueue.clear();
        return;
      }
      if (normals == null) {
        System.err.println("There's no normals in this, this is BAD!");
        renderQueue.clear();
        return;
      }
      FloatBuffer vertexData = BufferUtils.createFloatBuffer(vertices.length+normals.length*FLOAT_SIZE); // 36*4  floats (3 vertices* vertices
      for(int i = 0; i < vertices.length/3;i++) {
        int y = i/3;
        vertexData.put(new float[]{vertices[i*3], vertices[i*3+1], vertices[i*3+2], normals[y], normals[y+1], normals[y+2]});
        indiceCount += 6;
      }
//    System.out.println("Rendering vertex " + i + " : " + vertices[i*3] + ", " + vertices[i*3+1] + ", " + vertices[i*3+2] );
//      System.out.println("Vertices end.");
      
      //FloatBuffer positionData = BufferUtils.createFloatBuffer(3*FLOAT_SIZE); // 3 floats
      //FloatBuffer colorData = BufferUtils.createFloatBuffer(4*FLOAT_SIZE); // rgba
      
      
      vertexData.flip();
      //FloatBuffer pos = (FloatBuffer) r.getPosition().flip();
//      positionData.put(r.getPosition());
//      positionData.flip();
//      colorData.put(r.getColor());
//      colorData.flip();
      Vector4f color = r.getColor();
      Matrix4f modelProjectionMatrix = new Matrix4f();
      Matrix4f.mul(cameraProjMatrix, r.getModelView(), modelProjectionMatrix);
      FloatBuffer modelProjectionMatrixBuffer = BufferUtils.createFloatBuffer(16*FLOAT_SIZE);
      modelProjectionMatrix.store(modelProjectionMatrixBuffer);
      modelProjectionMatrixBuffer.flip();
      
      glUniform4f(texShader.uniformLocation("in_color"), color.x, color.y, color.z, color.w); 
      glUniformMatrix4(texShader.uniformLocation("modelProjectionMatrix"), false, modelProjectionMatrixBuffer); 
      

      glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);

      
      final int stride = (3 /* vertex Size */ + 3 /* normal size */) * FLOAT_SIZE;

      

      int in_vertexLocation = texShader.attribLocation("in_vertex");
      glEnableVertexAttribArray(in_vertexLocation);

      glVertexAttribPointer(in_vertexLocation, 3, GL_FLOAT, false, stride, 0);
      
      int in_normalLocation = texShader.attribLocation("in_normal");
      glEnableVertexAttribArray(in_normalLocation);

      glVertexAttribPointer(in_normalLocation, 3, GL_FLOAT, false, stride, 3*FLOAT_SIZE);
      
      glDrawArrays(GL_TRIANGLES, 0, indiceCount);
      glBindBuffer(GL_ARRAY_BUFFER, 0);
      glDisableVertexAttribArray(in_vertexLocation);
      glDisableVertexAttribArray(in_normalLocation);
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
