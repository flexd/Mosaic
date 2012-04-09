/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import cognitive.graphics.Graphics;
import org.newdawn.slick.Color;
import cognitive.Window;

import java.util.LinkedList;
import cognitive.Position;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author kristoffer
 */
public class Unit extends TexturedEntity {

  private LinkedList<Order> orders = new LinkedList();
  private double movementRate = 0.05;
  private TilePosition tilePosition = new TilePosition();
  
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

  public void addOrder(double x, double y) {
    orders.add(new Order(x, y));
  }

  @Override
  public void update(int delta) {
    super.update(delta);
    tilePosition = identifyTile(pos.x, pos.y);
    executeOrders();
  }

  @Override
  public void draw() {
    super.draw();
    if (selected) {
      Graphics.drawString((int)pos.x+34, (int)pos.y, "Tile: " + tilePosition.x + ", " + tilePosition.y, Graphics.FontSize.Small, Color.yellow);
      
      if (orders.peek() != null) Graphics.drawString((int)pos.x+34, (int)pos.y+12, "Target: " + orders.peek().tileTarget.x + ", " + orders.peek().tileTarget.y,Graphics.FontSize.Small, Color.yellow);
      Graphics.drawLineBox(hitbox.x, hitbox.y, hitbox.width , hitbox.height, true);
   
    }
    if (orders.size() > 0) {
      glPushAttrib(GL_ENABLE_BIT);
      glPushAttrib(GL_CURRENT_BIT);
      glDisable(GL_TEXTURE_2D);
      glDisable(GL_LIGHTING);
      Graphics.drawLineBox(10, 10, 10, 10, false);
      for (Order order : orders) {
        
          glColor3d(1.0, 0.0, 0.0);
          glBegin(GL_QUADS);
            glVertex2d(order.target.x, order.target.y);
            glVertex2d(order.target.x + 4, order.target.y);
            glVertex2d(order.target.x + 4, order.target.y + 4);
            glVertex2d(order.target.x, order.target.y + 4);
          glEnd();
        
      }
      glPopAttrib();
      glPopAttrib();
    }
  }
  
  public void executeOrders() {
    Order order = orders.peek();
    if (order != null) {
     // System.out.println("We have orders to move! Target tile: " + order.tileTarget.x + "," + order.tileTarget.y);
      TilePosition currentTile = identifyTile(pos.x, pos.y);
      if (currentTile.x == order.tileTarget.x && currentTile.y == order.tileTarget.y) {
       // System.out.println("Unit has reached destination!");
        //System.out.println("We are at tile: " + currentTile.x + ", " + currentTile.y);
        orders.remove(order);
        this.dx = 0;
        this.dy = 0;
      } 
      else { // We should be moving to the position then.
        // Major hack!
        //System.out.println("We are at tile: " + currentTile.x + ", " + currentTile.y);
        if (order.target.x > pos.x) {
          this.dx = 2 * movementRate;
        }
        if (order.target.x < pos.x) {
          this.dx = -2 * movementRate;
        }
        if (order.target.y > pos.y) {
          this.dy = 2 * movementRate;
        }
        if (order.target.y < pos.y) {
          this.dy = -2 * movementRate;
        }
      }
    }
  }

  public class TilePosition {

    public int x;
    public int y;
  }
  public class Order {
    public Position target;
    public TilePosition tileTarget;

    public Order(double x, double y) {
      target = new Position(x, y);
      tileTarget = identifyTile(x, y);
    }
}
  public TilePosition identifyTile(double x, double y) {
    TilePosition tilePosition = new TilePosition();
    tilePosition.x = (int) (x + Window.graphics.camera.offsetX) / 32;
    tilePosition.y = (int) (y + Window.graphics.camera.offsetY) / 32;
    return tilePosition;
  }
}