package entities;


public interface IEntity {
  public Quad2D getVertices();
  public void draw();
  public void update(int delta);
  public void setLocation(float x, float y);
  public void setX(float x);
  public void setY(float y);
  public void setWidth(float width);
  public void setHeight(float height);
  public float getX();
  public float getY();
  public float getHeight();
  public float getWidth();
  public boolean intersects(IEntity other);
}