/*
 */
package cognitive.graphics.texturemanager;

import java.util.ArrayList;

import org.newdawn.slick.opengl.Texture;

import cognitive.graphics.texturemanager.SpriteSheet;

/**
 *
 * @author Kristoffer Berdal <web@flexd.net>
 * @studnr 180212
 * @date Mar 3, 2012
 */

public class Sprite {
  public int frameDelay = 64;

  public static class AnimationFrame {
    public int x=0,y=0;

    public AnimationFrame(int x, int y) {
      this.x = x;
      this.y = y;
    }
    
  }
  public static class Animation {

    public int current = 0;
    public ArrayList <AnimationFrame> frames = new ArrayList<AnimationFrame>();
    public Animation() {
    }
   
    public int length() {
      return frames.size()-1;
    }
    
  }
  private String name;
  private int sX = 0;
  private int sY = 0;
  private int sNo = 0;
  private int tileSize = 32; // A default
  private SpriteSheet sheet;
  private boolean coords = true;
  public boolean animated = false;
  public int frameStart;
  public int frameOffset;
  public ArrayList<Animation> animations = new ArrayList<Animation>();
  
  public Sprite(SpriteSheet sheet, String name, int sX, int sY) {
    this.sheet = sheet;
    this.name = name;
    this.sX = sX;
    this.sY = sY;
    this.tileSize = sheet.getTileSize();
    coords = true;
  }
  public int addAnimation() {
    animations.add(new Animation()); // 10 possible frames, why not?
    return animations.size()-1;
  }
  public void addAnimationFrame(int animationID, int sX, int sY) {
    animations.get(animationID).frames.add(new AnimationFrame(sX,sY));
  }
  public Sprite(SpriteSheet sheet, String name, int sNo) {
    this.sheet = sheet;
    this.name = name;
    this.sNo = sNo;
    this.tileSize = sheet.getTileSize();
    coords = false;
  }
  public boolean isCoords() {
    return coords;
  }

  public int getsNo() {
    return sNo;
  }
  
  public int getTileSize() {
    return tileSize;
  }
  
  public String getName() {
    return name;
  }

  public int getsX() {
    return sX;
  }

  public int getsY() {
    return sY;
  }
  
  public void setsX(int sX) {
    this.sX = sX;
  }

  public void setsY(int sY) {
    this.sY = sY;
  }
  public int getSheetWidth() {
    return sheet.getWidth();
  }
  public int getSheetHeight() {
    return sheet.getHeight();
  }
  public Texture getTexture() {
    return sheet.getTexture();
  }
}