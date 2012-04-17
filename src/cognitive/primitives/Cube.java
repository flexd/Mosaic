package cognitive.primitives;

import org.lwjgl.util.vector.Vector3f;

import cognitive.primitives.Renderable;

public class Cube extends Renderable {
  private float[] normals = {
      -1.0f, 0.0f, 0.0f,
      0.0f, 0.0f, -1.0f,
      0.0f, -1.0f, 0.0f,
      0.0f, 0.0f, -1.0f,
     -1.0f, 0.0f, 0.0f,
      0.0f, -1.0f, 0.0f,
      0.0f, 0.0f, 1.0f,
      1.0f, 0.0f, 0.0f,
      1.0f, 0.0f, 0.0f,
      0.0f, 1.0f, 0.0f,
      0.0f, 1.0f, 0.0f,
      0.0f, 0.0f, 1.0f,
  };
  public Cube(Vector3f pos, float r, float g, float b, float a, float size) {
    super(pos, r, g, b, a, size, size, size);
  }
  public float[] getNormals() {
    return normals;
  }
  public Vector3f getFaceNormal(Vector3f a, Vector3f b, Vector3f c) {
    Vector3f out = new Vector3f();
    out.x = a.y * b.z - a.z * b.y;
    out.y = a.z * b.x - a.x * b.z;
    out.z = a.x * b.y - a.y * b.x;
    return out;
  }
  private final float vertices[] = {
    
    -1.0f,-1.0f,-1.0f, // triangle 1 : begin
    -1.0f,-1.0f, 1.0f,
    -1.0f, 1.0f, 1.0f, // triangle 1 : end
    // NORMAL: (-1,0,0)
    1.0f, 1.0f,-1.0f, // triangle 2 : begin
    -1.0f,-1.0f,-1.0f,
    -1.0f, 1.0f,-1.0f, // triangle 2 : end
    //NORMAL: (0,0,-1)
    1.0f,-1.0f, 1.0f,
    -1.0f,-1.0f,-1.0f,
    1.0f,-1.0f,-1.0f,
    
    //NORMAL (0,-1, 0)
    1.0f, 1.0f,-1.0f,
    1.0f,-1.0f,-1.0f,
    -1.0f,-1.0f,-1.0f,
    //NORMAL: (0, 0, -1)
    -1.0f,-1.0f,-1.0f,
    -1.0f, 1.0f, 1.0f,
    -1.0f, 1.0f,-1.0f,
    //NORMAL: (-1, 0, 0)
    1.0f,-1.0f, 1.0f,
    -1.0f,-1.0f, 1.0f,
    -1.0f,-1.0f,-1.0f,
    //NORMAL: (0, -1, 0)
    -1.0f, 1.0f, 1.0f,
    -1.0f,-1.0f, 1.0f,
    1.0f,-1.0f, 1.0f,
    //NORMAL: (0, 0, 1)
    1.0f, 1.0f, 1.0f,
    1.0f,-1.0f,-1.0f,
    1.0f, 1.0f,-1.0f,
    //NORMAL: (1, 0, 0)
    1.0f,-1.0f,-1.0f,
    1.0f, 1.0f, 1.0f,
    1.0f,-1.0f, 1.0f,
    //NORMAL: (1, 0, 0)
    1.0f, 1.0f, 1.0f,
    1.0f, 1.0f,-1.0f,
    -1.0f, 1.0f,-1.0f,
    //NORMAL: (0, 1, 0)
    1.0f, 1.0f, 1.0f,
    -1.0f, 1.0f,-1.0f,
    -1.0f, 1.0f, 1.0f,
    //NORMAL: (0, 1, 0)
    1.0f, 1.0f, 1.0f,
    -1.0f, 1.0f, 1.0f,
    1.0f,-1.0f, 1.0f
    //NORMAL: (0, 0, 1)
  };
  
  public float[] getVertices() {
    return vertices;
  }
}
