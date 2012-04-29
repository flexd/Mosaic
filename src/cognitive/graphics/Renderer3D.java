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

import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

import static org.lwjgl.opengl.GL20.glUniform3f;

import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.FloatBuffer;
import java.util.LinkedList;

import org.lwjgl.BufferUtils;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.opengl.Texture;

import cognitive.graphics.shadermanager.Shader;

import cognitive.primitives.Cube;
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
  
  private static int[] locations = new int[10];
  
  Vector3f lightPosition = new Vector3f(12, 50, 20);

  public void initMatrix(Matrix4f projectionMatrix) {
    this.projectionMatrix = projectionMatrix;
  }
  
  public Renderer3D() {
    vboHandle = glGenBuffers();
    texShader = new Shader("lighting");
    locations[0] = texShader.uniformLocation("in_color");
    locations[1] = texShader.uniformLocation("modelProjectionMatrix");
    locations[2] = texShader.attribLocation("in_vertex");
    locations[3] = texShader.attribLocation("in_normal");
    locations[4] = texShader.uniformLocation("modelViewMatrix");
    locations[5] = texShader.uniformLocation("lightPosition");
    locations[6] = texShader.uniformLocation("diffuseIntensityModifier");
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
    GL11.glEnable(GL_CULL_FACE);
    texShader.use();
    float[] vertices;
    float[] normals;
    FloatBuffer vertexData;
    
    //queue(new Cube(lightPosition, 1, 1, 1, 1, 2));
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
      vertexData = BufferUtils.createFloatBuffer(vertices.length+normals.length*FLOAT_SIZE); // 36*4  floats (3 vertices* vertices
      for(int i = 0; i < vertices.length/3;i++) {
        vertexData.put(new float[]{vertices[i*3], vertices[i*3+1], vertices[i*3+2]});
        vertexData.put(new float[]{normals[i*3], normals[i*3+1], normals[i*3+2]});
        indiceCount += 6;
      }
      
   // TODO: This is temporary, remove this!
      
      vertexData.flip();

      Vector4f color = r.getColor();
      Matrix4f modelProjectionMatrix = new Matrix4f();
      Matrix4f.mul(cameraProjMatrix, r.getModelView(), modelProjectionMatrix);
      FloatBuffer modelProjectionMatrixBuffer = BufferUtils.createFloatBuffer(16*FLOAT_SIZE);
      modelProjectionMatrix.store(modelProjectionMatrixBuffer);
      modelProjectionMatrixBuffer.flip();
      
      FloatBuffer modelViewMatrixBuffer = BufferUtils.createFloatBuffer(16*FLOAT_SIZE);
      r.getModelView().store(modelViewMatrixBuffer);
      modelViewMatrixBuffer.flip();
      glUniform4f(locations[0], color.x, color.y, color.z, color.w); 
      glUniformMatrix4(locations[1], false, modelProjectionMatrixBuffer); 
      glUniformMatrix4(locations[4], false, modelViewMatrixBuffer);
     
      
      
      boolean moveForward = Keyboard.isKeyDown(Keyboard.KEY_UP);
      boolean moveBackward = Keyboard.isKeyDown(Keyboard.KEY_DOWN);
      boolean moveLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT);
      boolean moveRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
      boolean flyUp = Keyboard.isKeyDown(Keyboard.KEY_T);
      boolean flyDown = Keyboard.isKeyDown(Keyboard.KEY_G);
      boolean moveFaster = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
      boolean moveSlower = Keyboard.isKeyDown(Keyboard.KEY_TAB);
      
      if (moveForward) {
        lightPosition.x += 1 * 0.016f;
      }
      if (moveBackward) {
        lightPosition.x -= 1 * 0.016f;;
      }
      if (moveLeft) {
        lightPosition.z -= 1 * 0.016f;
      }
      if (moveRight) {
        lightPosition.z += 1 * 0.016f;;
      }
      if (flyUp) {
        lightPosition.y += 1* 0.016f;;
      }
      if (flyDown) {
        lightPosition.y -= 1* 0.016f;;
      }
      System.out.println(lightPosition);
      

      
      glUniform3f(locations[5], lightPosition.x, lightPosition.y, lightPosition.z); 
 
     float diffuseIntensityModifier = 0.8f;
      
      GL20.glUniform1f(locations[6], diffuseIntensityModifier); 
      glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);

      
      final int stride = (3 /* vertex Size */ + 3 /* normal size */) * FLOAT_SIZE;

      

      glEnableVertexAttribArray(locations[2]);

      glVertexAttribPointer(locations[2], 3, GL_FLOAT, false, stride, 0);
      
      glEnableVertexAttribArray(locations[3]);

      glVertexAttribPointer(locations[3], 3, GL_FLOAT, false, stride, 3*FLOAT_SIZE);
      
      glDrawArrays(GL_TRIANGLES, 0, indiceCount);
      glBindBuffer(GL_ARRAY_BUFFER, 0);
      glDisableVertexAttribArray(locations[2]);
      glDisableVertexAttribArray(locations[3]);
      
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
