package entities;

import java.awt.Rectangle;
import org.cognitive.Position;
import org.cognitive.Window;
import org.cognitive.texturemanager.Sprite;

public abstract class AbstractEntity implements IEntity, IMoveableEntity {

  protected int elapsedDelta = 0;
  protected Position pos;
  protected double dx = 0, dy = 0;
  protected double width, height;
  protected Rectangle hitbox = new Rectangle();
  protected boolean validMove = false;
  public boolean selected = false;

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    // TODO: Remove this
    this.selected = selected;
  }
  
  
  public Rectangle getHitbox() {
    return hitbox;
  }
  private void updateState(double newX, double newY, boolean valid) {
    validMove = valid;
    if (newX > this.pos.x && valid) {
      setState(EntityState.MOVING_RIGHT);
    } else if (newX < this.pos.x && valid) {
      setState(EntityState.MOVING_LEFT);
    }
    if (newY > this.pos.y && dx == 0 && valid) {
      setState(EntityState.MOVING_DOWN);
    } else if (newY < this.pos.y  && dx == 0 && valid) {
      setState(EntityState.MOVING_UP);
    }
  }
  public static enum EntityState {

    MOVING_LEFT,MOVING_RIGHT,MOVING_UP,MOVING_DOWN,STATIONARY;
  }
  
  protected EntityState state = EntityState.STATIONARY;
  protected EntityState lastState = state;
  public AbstractEntity(double x, double y, double width, double height) {
    this.pos = new Position(x,y);
    this.width = width;
    this.height = height;
  }

  @Override
  public void setLocation(double x, double y) {
    this.pos.x = x;
    this.pos.y = y;
  }

  @Override
  public void setX(double x) {
    this.pos.x = x;
  }

  @Override
  public void setY(double y) {
    this.pos.y = y;
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
    return pos.x;
  }

  @Override
  public double getY() {
    return pos.y;
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
  public boolean intersects(IEntity other) {
    
    return hitbox.intersects(other.getX(), other.getY(), other.getWidth(), other.getHeight());
  }
  
  //TODO: Entities can crash/intersect when approached from the right side and get stuck, Fix it!
  @Override
  public void update(int delta) {
    elapsedDelta += delta;
    boolean valid = true;
    Rectangle new_position = new Rectangle();
    Rectangle world = new Rectangle();
    world.setBounds((int)this.width,(int)this.height,(int)(Window.DISPLAY_WIDTH-this.width*2),(int)(Window.DISPLAY_HEIGHT-this.height*2));
    double newX = this.pos.x, newY = this.pos.y;
    newX += delta * dx; 
    newY += delta * dy;
    willMove(newX, newY);
    new_position.setBounds((int)(newX+this.width * 1.2/3), (int)(newY), (int)(width * 1.1/2), (int) height);
    
    for (AbstractEntity other : Window.entities) {
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
    updateState(newX, newY, valid);
    if (valid) {
      setLocation(newX, newY);
      //System.out.println("IEntity: " + this + " State: " + state.name());
    }
    hitbox.setBounds((int) pos.x, (int) pos.y, (int)(pos.x+width), (int)(pos.y+height));
  }

  private void willMove(double newX, double newY) {
    if (newX == this.pos.x && newY == this.pos.y) {
      setState(EntityState.STATIONARY);
    }
  }
  public void setState(EntityState newstate) {
    lastState = state;
    state = newstate;
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
  @Override
  public void move(int dx, int dy) {
    this.dx = dx * 0.15;
    this.dy = dy * 0.15;
  }
}