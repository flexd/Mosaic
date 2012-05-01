package cognitive.primitives;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public abstract class Renderable {
  protected float vertices[] = null;
  protected float normals[] = null;
  protected Matrix4f position;
  protected Vector4f color;
  protected boolean visible = true;

  public Matrix4f getModelView() {
    return position;
  }
  public float[] getVectorPos() {
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
  public void setVisible(boolean b) {
    visible  = b; 
  }
  public void toggleVisible() {
    visible = !visible;
    System.out.println("Cube is now: " + visible);
  }
  public boolean isVisible() {
    return visible;
  }
  public Vector3f getFaceNormal(Vector3f a, Vector3f b, Vector3f c) {
    Vector3f out = new Vector3f();
    out.x = a.y * b.z - a.z * b.y;
    out.y = a.z * b.x - a.x * b.z;
    out.z = a.x * b.y - a.y * b.x;
    return out;
  }
}
