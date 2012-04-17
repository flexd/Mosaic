package cognitive.graphics;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import cognitive.primitives.*;
public class Chunk {
  private ArrayList<Cube> cubes = new ArrayList<Cube>();
  private Vector3f position = new Vector3f(0, 0, 0);
  
  public Chunk(Vector3f position) {
    this.position = position;
  }
}
