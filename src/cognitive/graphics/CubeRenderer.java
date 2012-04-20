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
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.opengl.Texture;

import cognitive.graphics.shadermanager.Shader;
import cognitive.primitives.Renderable;
/**
 *
 * @author kristoffer
 */
public class CubeRenderer {
  public LinkedList<Renderable> renderQueue = new LinkedList<Renderable>();
  private int vboHandle;
  private Texture tex;
  private int lastError = 0;
  private Shader texShader;
  private Matrix4f projectionMatrix;
  private static final int FLOAT_SIZE = 4;
  private static int frameCount = 0;
  private static int[] locations = new int[10];
  public void initMatrix(Matrix4f projectionMatrix) {
    this.projectionMatrix = projectionMatrix;
  }
  
  public CubeRenderer() {
    vboHandle = glGenBuffers();
    texShader = new Shader("cubelighting");
    locations[0] = texShader.attribLocation("in_color");
    locations[1] = texShader.uniformLocation("projectionMatrix");
    locations[2] = texShader.attribLocation("in_vertex");
    locations[3] = texShader.attribLocation("in_normal");
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
    GL11.glDisable(GL_CULL_FACE);
    texShader.use();
    float[] vertices;
    float[] normals;
    FloatBuffer vertexData = BufferUtils.createFloatBuffer(getQueue().size()*(36+36)*3*4*FLOAT_SIZE); // 36*4  floats (3 vertices* vertices
    FloatBuffer projectionMatrixBuffer = BufferUtils.createFloatBuffer(16*FLOAT_SIZE);
    glBindBuffer(GL_ARRAY_BUFFER, vboHandle);
    int indiceCount = 0;
    for(Renderable r : getQueue()) {
       vertices = r.getVertices();
       normals = r.getNormals();
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
      Vector4f color = r.getColor();
      for(int i = 0; i < vertices.length/3;i++) {
        Vector4f vertex = new Vector4f(vertices[i*3], vertices[i*3+1], vertices[i*3+2], 1.0f);
        
        Vector4f out = Matrix4f.transform(r.getModelView(), vertex, null);
//        System.err.println(out);
        vertexData.put(new float[]{out.x, out.y, out.z, out.w});
        vertexData.put(new float[]{normals[i*3], normals[i*3+1], normals[i*3+2]});
        vertexData.put(new float[]{color.x, color.y, color.z, color.w});
        indiceCount += 10;
      }      
      vertexData.flip();
    }
    
    cameraProjMatrix.store(projectionMatrixBuffer);
    projectionMatrixBuffer.flip();
    
    glUniformMatrix4(locations[1], false, projectionMatrixBuffer); 
    

    glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);

    
    final int stride = (3 /* vertex Size */ + 3 /* normal size */ + 4 /* color size*/) * FLOAT_SIZE;

    

    glEnableVertexAttribArray(locations[2]);
    
    // Vertex/Position
    glVertexAttribPointer(locations[2], 4, GL_FLOAT, false, stride, 0);
    
    glEnableVertexAttribArray(locations[3]);
    
    // Normals
    glVertexAttribPointer(locations[3], 3, GL_FLOAT, false, stride, 4*FLOAT_SIZE);
    
    glEnableVertexAttribArray(locations[0]);
    // Color
    glVertexAttribPointer(locations[0], 4, GL_FLOAT, false, stride, 7*FLOAT_SIZE);
    
    glDrawArrays(GL_TRIANGLES, 0, indiceCount);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glDisableVertexAttribArray(locations[2]);
    glDisableVertexAttribArray(locations[3]);
    
    
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
