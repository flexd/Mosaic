/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.util.PriorityQueue;
import org.cognitive.Position;

/**
 *
 * @author kristoffer
 */
public class Unit extends TexturedEntity {

  private PriorityQueue<Order> orders = new PriorityQueue();
  private double movementRate = 0.05;
  public boolean selected = false;
  
  public Unit(double x, double y, String spriteName) {
    super(x, y, spriteName);
    this.sprite.animated = true;
    int animationID = this.sprite.addAnimation(); // Running left!
    this.sprite.addAnimationFrame(animationID, 0, 1);
    this.sprite.addAnimationFrame(animationID, 1, 1);
    this.sprite.addAnimationFrame(animationID, 2, 1);
    animationID = this.sprite.addAnimation(); // Running right!
    this.sprite.addAnimationFrame(animationID, 0, 3);
    this.sprite.addAnimationFrame(animationID, 1, 3);
    this.sprite.addAnimationFrame(animationID, 2, 3);
    animationID = this.sprite.addAnimation(); // Running down
    this.sprite.addAnimationFrame(animationID, 0, 0);
    this.sprite.addAnimationFrame(animationID, 1, 0);
    this.sprite.addAnimationFrame(animationID, 2, 0);
    animationID = this.sprite.addAnimation(); // Running up
    this.sprite.addAnimationFrame(animationID, 0, 2);
    this.sprite.addAnimationFrame(animationID, 1, 2);
    this.sprite.addAnimationFrame(animationID, 2, 2);
  }

  public void moveTo(double x, double y) {
    orders.add(new Order(x, y));
  }

  @Override
  public void update(int delta) {
    super.update(delta);
  }

  @Override
  public void move(int dx, int dy) {
    this.dx = dx * 0.15;
    this.dy = dy * 0.15;

    Order order = orders.peek();
    TilePosition currentTile = identifyTile(pos.x, pos.y, 0, 0);
    if (currentTile.x == order.tileTarget.x && currentTile.y == order.tileTarget.y) {
      // Completed (this will not work, position will not be accurate lol).
      orders.remove(order);
    } else { // We should be moving to the position then.
      // Major hack!
      if (order.target.x > pos.x) {
        this.dx = 1 * movementRate;
      }
      if (order.target.x < pos.x) {
        this.dx = -1 * movementRate;
      }
      if (order.target.y > pos.y) {
        this.dy = 1 * movementRate;
      }
      if (order.target.y < pos.y) {
        this.dy = -1 * movementRate;
      }
    }
  }

  public class TilePosition {

    public int x;
    public int y;
  }

  public TilePosition identifyTile(double x, double y, int camerax1, int cameray1) {
    TilePosition tilePosition = new TilePosition();
    tilePosition.x = (int) (x + camerax1) % 32;
    tilePosition.y = (int) (y + cameray1) % 32;
    return tilePosition;
  }

  private class Order {

    public Position target;
    public TilePosition tileTarget;

    public Order(double x, double y) {
      target = new Position(x, y);
      tileTarget = identifyTile(x, y, 0, 0); // No camera yet, so 0,0 is topleft.
    }
  }
}