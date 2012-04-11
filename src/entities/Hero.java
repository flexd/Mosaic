/*
 */
package entities;

/**
 *
 * @author Kristoffer Berdal <web@flexd.net>
 * @studnr 180212
 * @date Mar 10, 2012
 */
public class Hero extends TexturedEntity {

  public Hero(float x, float y, String spriteName) {
    super(x, y, spriteName);
    this.sprite.animated = true;
    int animationID = this.sprite.addAnimation(); // Running left!
    this.sprite.addAnimationFrame(animationID, 0 , 1); 
    this.sprite.addAnimationFrame(animationID, 1 , 1);
    this.sprite.addAnimationFrame(animationID, 2 , 1);
    animationID = this.sprite.addAnimation(); // Running right!
    this.sprite.addAnimationFrame(animationID, 0 , 3); 
    this.sprite.addAnimationFrame(animationID, 1 , 3);
    this.sprite.addAnimationFrame(animationID, 2 , 3);
    animationID = this.sprite.addAnimation(); // Running down
    this.sprite.addAnimationFrame(animationID, 0 , 0); 
    this.sprite.addAnimationFrame(animationID, 1 , 0);
    this.sprite.addAnimationFrame(animationID, 2 , 0);
    animationID = this.sprite.addAnimation(); // Running up
    this.sprite.addAnimationFrame(animationID, 0 , 2); 
    this.sprite.addAnimationFrame(animationID, 1 , 2);
    this.sprite.addAnimationFrame(animationID, 2 , 2);
    
  }
  @Override
  public void move(int dx, int dy) {
    this.dx = dx * 0.15f;
    this.dy = dy * 0.15f;
  }
  
}
