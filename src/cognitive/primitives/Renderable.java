package cognitive.primitives;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Renderable {
  private final float vertices[] = {
      -1.0f,-1.0f,-1.0f
  };
  private Matrix4f position;
  private Vector4f color;
  
  public Matrix4f getModelView() {
//    FloatBuffer out = BufferUtils.createFloatBuffer(16*4); // 4x4 matrix with floats
//    position.store(out);
//    return out;
    return position;
  }
  
  public Vector3f getVectorPos() {
    return new Vector3f(position.m00, position.m12, position.m33); 
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
  
  public Vector4f getColor() {
    return color;
  }
  public void setColor(Vector4f color) {
    this.color = color;
  }
}
