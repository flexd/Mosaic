package entities;

import java.awt.Rectangle;
import org.cognitive.ScrollGame;
import org.cognitive.texturemanager.Sprite;

public abstract class AbstractEntity implements Entity, MoveableEntity {

  protected double dx = 0, dy = 0;
  protected double x, y, width, height;
  protected Rectangle hitbox = new Rectangle();

  public AbstractEntity(double x, double y, double width, double height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  @Override
  public void setLocation(double x, double y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public void setX(double x) {
    this.x = x;
  }

  @Override
  public void setY(double y) {
    this.y = y;
  }

  @Override
  public void setWidth(double width) {
    this.width = width;
  }

  @Override
  public void setHeight(double height) {
    this.height = height;
  }

  @Override
  public double getX() {
    return x;
  }

  @Override
  public double getY() {
    return y;
  }

  @Override
  public double getHeight() {
    return height;
  }

  @Override
  public double getWidth() {
    return width;
  }

  @Override
  public boolean intersects(Entity other) {
    hitbox.setBounds((int) x, (int) y, (int) width/2, (int) height/2);
    return hitbox.intersects(other.getX(), other.getY(), other.getWidth(), other.getHeight());
  }
  
  @Override
  public void update(int delta) {
    boolean valid = true;
    Rectangle new_position = new Rectangle();
    Rectangle world = new Rectangle();
    world.setBounds((int)this.width,(int)this.height,(int)(ScrollGame.DISPLAY_WIDTH-this.width*2),(int)(ScrollGame.DISPLAY_HEIGHT-this.height*2));
    double newX = this.x, newY = this.y;
    newX += delta * dx; 
    newY += delta * dy;
    new_position.setBounds((int)(newX+this.width * 1.2/3), (int)(newY), (int)(width * 1.1/2), (int) height);
    
    for (AbstractEntity other : ScrollGame.entities) {
      if (other != this) {
        if (valid) {
          if (new_position.intersects(other.getX(), other.getY(), other.getWidth(), other.getHeight())) {
            //System.out.println("Colliding!");
            valid = false;
            break;
          }
          if (!world.intersects(newX, newY, this.width, this.height)) {
            // outside the world box!
            valid = false;
            break;
          }
        }
        else {
          break;
        }
      }
    }
    if (valid) {
      this.x = newX;
      this.y = newY;
    }
  }
  
  @Override
  public double getDX() {
    return dx;
  }

  @Override
  public double getDY() {
    return dy;
  }

  @Override
  public void setDX(double dx) {
    this.dx = dx;
  }

  @Override
  public void setDY(double dy) {
    this.dy = dy;
  }
}