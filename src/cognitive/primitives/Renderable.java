package cognitive.primitives;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public abstract class Renderable {
  private float vertices[] = null;
//= {
//      -1.0f,-1.0f,-1.0f
//  };
  private Matrix4f position;
  private Vector4f color;
  protected float normals[] = null;
  
  public Matrix4f getModelView() {
//    FloatBuffer out = BufferUtils.createFloatBuffer(16*4); // 4x4 matrix with floats
//    position.store(out);
//    return out;
    return position;
  }
  
  public float[] getVectorPos() {
    return new float[]{position.m00, position.m12, position.m33}; 
  }
  public Renderable (Vector3f pos,float r, float g, float b, float a, float width, float height, float depth) {
    position = new Matrix4f();
    position.translate(pos);
    position.scale(new Vector3f(width, height, depth));
    setColor(new Vector4f(r, g, b ,a));
  }
  public float[] getVertices() {
    return vertices;
  }
  public float[] getNormals() {
    return normals;
  }
  
  public Vector4f getColor() {
    return color;
  }
  public void setColor(Vector4f color) {
    this.color = color;
  }
}
