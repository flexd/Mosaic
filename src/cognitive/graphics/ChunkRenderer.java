/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cognitive.graphics;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import java.nio.FloatBuffer;
import java.util.LinkedList;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import cognitive.Util;
import cognitive.graphics.shadermanager.Shader;
/**
 *
 * @author kristoffer
 */
public class ChunkRenderer {
  public LinkedList<Chunk> renderQueue = new LinkedList<Chunk>();

  private Shader texShader;
  private Matrix4f projectionMatrix;
  private static int[] locations = new int[10];
  
  Vector3f lightPosition = new Vector3f(12, 50, 20);
  public void initMatrix(Matrix4f projectionMatrix) {
    this.projectionMatrix = projectionMatrix;
  }
  
  public ChunkRenderer() {
    texShader = new Shader("chunk");
    locations[0] = texShader.attribLocation("in_vertex");
    locations[1] = texShader.attribLocation("in_normal");
    locations[2] = texShader.attribLocation("in_color");
    locations[3] = texShader.attribLocation("in_position");
    locations[4] = texShader.uniformLocation("modelProjectionMatrix");
    locations[5] = texShader.uniformLocation("modelViewMatrix");
    locations[6] = texShader.uniformLocation("lightPosition");
    locations[7] = texShader.uniformLocation("diffuseIntensityModifier");
  }
  public void queue(Chunk c) {
    renderQueue.add(c);
  }
  public LinkedList<Chunk> getQueue() {
    return renderQueue;
  }
  
  public void flushQueue(Matrix4f cameraProjMatrix) {
    
    if (renderQueue.isEmpty()) {
      return;
    }
    
    GL11.glEnable(GL_CULL_FACE);
    texShader.use();

    
    for(Chunk c : getQueue()) {
      
      c.render(locations); // generate the vbo
      
      //glBindBuffer(GL_ARRAY_BUFFER, c.getVBO()); // Bind the vbo again, since we unbind it in the Chunk.render()
    
      
      Matrix4f modelProjectionMatrix = new Matrix4f();
      Matrix4f.mul(cameraProjMatrix, c.getModelView(), modelProjectionMatrix);
      FloatBuffer modelProjectionMatrixBuffer = BufferUtils.createFloatBuffer(16*Util.FLOAT_SIZE);
      modelProjectionMatrix.store(modelProjectionMatrixBuffer);
      modelProjectionMatrixBuffer.flip();
      
      FloatBuffer modelViewMatrixBuffer = BufferUtils.createFloatBuffer(16*Util.FLOAT_SIZE);
      c.getModelView().store(modelViewMatrixBuffer);
      modelViewMatrixBuffer.flip();
    
      glUniformMatrix4(locations[4], false, modelProjectionMatrixBuffer); 
      glUniformMatrix4(locations[5], false, modelViewMatrixBuffer);
     
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
        lightPosition.x -= 1 * 0.016f;
      }
      if (moveLeft) {
        lightPosition.z -= 1 * 0.016f;
      }
      if (moveRight) {
        lightPosition.z += 1 * 0.016f;
      }
      if (flyUp) {
        lightPosition.y += 1 * 0.016f;
      }
      if (flyDown) {
        lightPosition.y -= 1 * 0.016f;
      }
      //System.out.println(lightPosition);
      glUniform3f(locations[6], lightPosition.x, lightPosition.y, lightPosition.z); 
 
     float diffuseIntensityModifier = 0.8f;
      
      GL20.glUniform1f(locations[7], diffuseIntensityModifier); 

      
      glDrawArrays(GL_TRIANGLES, 0, c.getIndiceCount());
      glBindBuffer(GL_ARRAY_BUFFER, 0);
      glDisableVertexAttribArray(locations[0]);
      glDisableVertexAttribArray(locations[1]);
      glDisableVertexAttribArray(locations[2]);
      glDisableVertexAttribArray(locations[3]);
    }
    
    texShader.stop();
    renderQueue.clear();
  }
}
