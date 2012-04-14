package cognitive.primitives;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Renderable {
  private final float vertices[] = null;
  private Matrix4f position;
  private Vector4f color;
  
  public FloatBuffer getPosition() {
    FloatBuffer out = BufferUtils.createFloatBuffer(16*4); // 4x4 matrix with floats
    position.store(out);
    return out;
  }
  public Renderable (Vector3f pos,float r, float g, float b, float a, float size) {
    position = new Matrix4f();
    position.translate(pos);
    position.scale(new Vector3f(size, size, size));
    setColor(new Vector4f(r, g, b ,a));
  }
  public float[] getVertices() {
    if (vertices != null) {
      return vertices;
    }
    else {
      return new float[0]; // Empty!
    }
  }
  public float[] getColor() {
    return new float[]{color.x, color.y, color.z, color.w};
  }
  public void setColor(Vector4f color) {
    this.color = color;
  }
}
