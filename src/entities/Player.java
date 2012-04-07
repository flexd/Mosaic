/*
 */
package entities;

/**
 *
 * @author Kristoffer Berdal <web@flexd.net>
 * @studnr 180212
 * @date Mar 10, 2012
 */
public class Player extends TexturedEntity {

  public Player(double x, double y, String spriteName) {
    super(x, y, spriteName);
    this.sprite.animated = true;
    int animationID = this.sprite.addAnimation(); // Running left!
    this.sprite.addAnimationFrame(animationID, 2 , 0); 
    this.sprite.addAnimationFrame(animationID, 3 , 0);
    this.sprite.addAnimationFrame(animationID, 4 , 0);
    this.sprite.addAnimationFrame(animationID, 5 , 0); // last frame!
    animationID = this.sprite.addAnimation(); // Running right!
    this.sprite.addAnimationFrame(animationID, 2 , 1); 
    this.sprite.addAnimationFrame(animationID, 3 , 1);
    this.sprite.addAnimationFrame(animationID, 4 , 1);
    this.sprite.addAnimationFrame(animationID, 5 , 1); // last frame!
    animationID = this.sprite.addAnimation(); // Running down
    this.sprite.addAnimationFrame(animationID, 2 , 2); 
    this.sprite.addAnimationFrame(animationID, 3 , 2);
    this.sprite.addAnimationFrame(animationID, 4 , 2);
    this.sprite.addAnimationFrame(animationID, 5 , 2); // last frame
    animationID = this.sprite.addAnimation(); // Running up
    this.sprite.addAnimationFrame(animationID, 2 , 3); 
    this.sprite.addAnimationFrame(animationID, 3 , 3);
    this.sprite.addAnimationFrame(animationID, 4 , 3);
    this.sprite.addAnimationFrame(animationID, 5 , 3); // last frame
    
  }
  @Override
  public void move(int dx, int dy) {
    this.dx = dx * 0.15;
    this.dy = dy * 0.15;
  }
  
}
