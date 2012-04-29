package cognitive.primitives;

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
//    System.out.println("Actual matrix is: ");
//    System.out.println(position);
    return new float[]{position.m30, position.m31, position.m32}; 
  }
  public Renderable (Vector3f pos, Vector4f color, Vector3f size) {
    position = new Matrix4f();
    position.translate(pos);
    position.scale(size);
    setColor(color);
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
