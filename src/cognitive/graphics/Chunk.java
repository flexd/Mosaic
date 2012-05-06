package cognitive.graphics;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import cognitive.Util;
import cognitive.primitives.*;
public class Chunk {
  public static final int WIDTH = 4;
  public static final int HEIGHT = 4;
  public static final int DEPTH = 4;
  private Vector3f chunkID = new Vector3f();

  private Cube[][][] cubes = new Cube[WIDTH][HEIGHT][DEPTH];
  private Matrix4f position = new Matrix4f();

  private int vboHandle;
  private int indiceCount = 0;

  private ByteBuffer vertexData = BufferUtils.createByteBuffer(WIDTH*HEIGHT*DEPTH /* 27 cubes, (3x3 chunk)*/ * ((36 /* 36 vertices per cube */ + 36 /* 36 normals per cube */) * 3  + 36*4 /* colors */ + 36*3 /* Position*/) * Util.FLOAT_SIZE);
  private ByteBuffer driverSideBuffer = null;

  private boolean initialized = false;
  private boolean dirty = true;
  public Chunk(Vector3f position, Vector3f chunkID) {
    this.chunkID = chunkID;
    vboHandle = glGenBuffers();
    // 4x4x4 = 64
    for(int x = 0; x < WIDTH; x++) {
      for (int y = 0; y < HEIGHT; y++) {
        for (int z = 0; z < DEPTH;z++) {
          Cube cube = new Cube(new Vector3f(x,y,z), new Vector4f((float)Math.random(), (float)Math.random(), (float)Math.random(), 1f), 1f); // Spacing and size should be the same!
          cube.setChunkPos(new Vector3f(x, y, z));
          cube.setChunkID(chunkID);
          cubes[x][y][z] = cube;
        }
      }
    }
    System.err.println("Cube count: " + cubes.length);
    this.position.translate(position); // Since we move it, the chunk is dirty by default.
    prepareBuffer();
  }
  public int getCubeCount() {
    return cubes.length;
  }
  public int getVBO() {
    return vboHandle;
  }
  public int getIndiceCount() {
    return indiceCount;
  }
  public Matrix4f getModelView() {
    return position;
  }
  public Vector3f getVectorPos() {
    return new Vector3f(position.m30, position.m31, position.m32); 
  }
  public void prepareBuffer() {
    vertexData.clear();
    indiceCount = 0; // Reset this!
    float[] vertices;
    float[] normals;
    Vector4f color;
    float[] pos;
    for(int x = 0; x < WIDTH; x++) {
      for (int y = 0; y < HEIGHT; y++) {
        for (int z = 0; z < DEPTH;z++) {
          Cube cube = cubes[x][y][z];
          if (!cube.isVisible()) continue;
          vertices = cube.getVertices(); // Set vertices and normals (again)
          normals = cube.getNormals();
          color = cube.getColor();
          pos = cube.getVectorPos();
          // Loop through the vertices and normals, and add them to the vertexData buffer 3 by 3, as well as all the other stuff, like color and crap. Textures in the future!
          for(int i = 0; i < vertices.length/3;i++) {
            // Vertices
            vertexData.putFloat(vertices[i*3]);
            vertexData.putFloat(vertices[i*3+1]);
            vertexData.putFloat(vertices[i*3+2]);
            // Normals
            vertexData.putFloat(normals[i*3]);
            vertexData.putFloat(normals[i*3+1]);
            vertexData.putFloat(normals[i*3+2]);
            // Colors
            vertexData.putFloat(color.x);
            vertexData.putFloat(color.y);
            vertexData.putFloat(color.z);
            vertexData.putFloat(color.w);
            // Position
            vertexData.putFloat(pos[0]);
            vertexData.putFloat(pos[1]);
            vertexData.putFloat(pos[2]);

            indiceCount += 1;
          }
        }
      }
    }
    vertexData.order(ByteOrder.nativeOrder());
    vertexData.flip();
    dirty = false;
  }
  public void update(float delta) {
    for(int x = 0; x < WIDTH; x++) {
      for (int y = 0; y < HEIGHT; y++) {
        for (int z = 0; z < DEPTH;z++) {
          Cube cube = cubes[x][y][z];
          cube.update(delta);
        }
      }
    }
    
  }
  public void render(int[] locations, float delta)  {
    glBindBuffer(GL_ARRAY_BUFFER, vboHandle);
    if (dirty) {
      prepareBuffer();
    }
    if (!initialized) {
      glBufferData(GL_ARRAY_BUFFER, vertexData, GL_DYNAMIC_DRAW);
      initialized = true;
    }
    else {
      driverSideBuffer = glMapBuffer(GL_ARRAY_BUFFER, GL15.GL_WRITE_ONLY, driverSideBuffer);
      driverSideBuffer.clear();
      driverSideBuffer.put(vertexData);
      glUnmapBuffer(GL_ARRAY_BUFFER);
    }

    final int stride = (3 /* vertex Size */ + 3 /* normal size */ + 4 /* color size*/ + 3 /* position size */) * Util.FLOAT_SIZE;

    glEnableVertexAttribArray(locations[0]);

    glVertexAttribPointer(locations[0], 3, GL_FLOAT, false, stride, 0);

    glEnableVertexAttribArray(locations[1]);

    glVertexAttribPointer(locations[1], 3, GL_FLOAT, false, stride, 3*Util.FLOAT_SIZE);

    glEnableVertexAttribArray(locations[2]);

    glVertexAttribPointer(locations[2], 4, GL_FLOAT, false, stride, 6*Util.FLOAT_SIZE);

    glEnableVertexAttribArray(locations[3]);

    glVertexAttribPointer(locations[3], 3, GL_FLOAT, false, stride, 10*Util.FLOAT_SIZE);

  }
  public void randomiseColors() {
    for(int x = 0; x < WIDTH; x++) {
      for (int y = 0; y < HEIGHT; y++) {
        for (int z = 0; z < DEPTH;z++) {
          Cube c = cubes[x][y][z];
          c.setColor(new Vector4f((float)Math.random(), (float)Math.random(), (float)Math.random(), (float)Math.random()));
        }
      }
    }
    dirty = true;
  }
  public void setDirty() {
    dirty = true;

  }
  public void setColors(Vector4f color) {
    for(int x = 0; x < WIDTH; x++) {
      for (int y = 0; y < HEIGHT; y++) {
        for (int z = 0; z < DEPTH;z++) {
          Cube c = cubes[x][y][z];
          c.setColor(color);
        }
      }
    }
    dirty = true;
  }
  public void setVisible(boolean b) {
    for(int x = 0; x < WIDTH; x++) {
      for (int y = 0; y < HEIGHT; y++) {
        for (int z = 0; z < DEPTH;z++) {
          Cube c = cubes[x][y][z];
          c.setVisible(b);
        }
      }
    }
    dirty = true;
  }
  public void toggleVisible() {
    for(int x = 0; x < WIDTH; x++) {
      for (int y = 0; y < HEIGHT; y++) {
        for (int z = 0; z < DEPTH;z++) {
          Cube c = cubes[x][y][z];
          c.toggleVisible();
        }
      }
    }
    dirty = true;
  }
  public int getVisibleCubeCount() {
    int visible = 0;
    for(int x = 0; x < WIDTH; x++) {
      for (int y = 0; y < HEIGHT; y++) {
        for (int z = 0; z < DEPTH;z++) {
          Cube c = cubes[x][y][z];
          if (c.isVisible()) visible++;
        }
      }
    }
    return visible;
  }

}
