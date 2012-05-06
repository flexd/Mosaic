package cognitive.primitives;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import cognitive.Game;
import cognitive.graphics.Chunk;
import cognitive.primitives.Renderable;

public class Cube extends Renderable {
  private Vector3f chunkpos = new Vector3f();
  private Vector3f chunkID = new Vector3f();
  
  
  public Vector3f getChunkpos() {
    return chunkpos;
  }


  public void setChunkPos(Vector3f chunkpos) {
    this.chunkpos = chunkpos;
  }


  public Cube(Vector3f pos, Vector4f color, float size) {
    super(pos, color, new Vector3f(size, size, size));
    
    normals = new float[]{
        -1.0f, 0.0f, 0.0f,
        -1.0f, 0.0f, 0.0f,
        -1.0f, 0.0f, 0.0f,
        // Triangle 1
        -1.0f, 0.0f, 0.0f,
        -1.0f, 0.0f, 0.0f,
        -1.0f, 0.0f, 0.0f,
        // Triangle 2
        
        0.0f, -1.0f, 0.0f,
        0.0f, -1.0f, 0.0f,
        0.0f, -1.0f, 0.0f,
        // Triangle 3
        0.0f, -1.0f, 0.0f,
        0.0f, -1.0f, 0.0f,
        0.0f, -1.0f, 0.0f,
        //Triangle 4
        
        0.0f, 0.0f, -1.0f,
        0.0f, 0.0f, -1.0f,
        0.0f, 0.0f, -1.0f,
        // Triangle 5
        
        0.0f, 0.0f, -1.0f,
        0.0f, 0.0f, -1.0f,
        0.0f, 0.0f, -1.0f,
        // Triangle 6
        
        1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        // Triangle 7
        
        1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        // Triangle 8
        
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        // Triangle 9
        
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        // Triangle 10
        
        0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f,
        // Triangle 11
        
        0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f,
        // Triangle 12
    };
    
    vertices = new float[]{
        
        -0.5f,-0.5f,-0.5f, // triangle 1 : begin
        -0.5f,-0.5f, 0.5f,
        -0.5f, 0.5f, 0.5f, // triangle 1 : end
        // NORMAL: (-1,0,0)
        -0.5f,-0.5f,-0.5f,
        -0.5f, 0.5f, 0.5f,
        -0.5f, 0.5f,-0.5f,
        //NORMAL: (-1, 0, 0)
        
        0.5f,-0.5f, 0.5f,
        -0.5f,-0.5f,-0.5f,
        0.5f,-0.5f,-0.5f,
        
        //NORMAL (0,-1, 0)
        
        0.5f,-0.5f, 0.5f,
        -0.5f,-0.5f, 0.5f,
        -0.5f,-0.5f,-0.5f,
        //NORMAL: (0, -1, 0)

        0.5f, 0.5f,-0.5f,
        0.5f,-0.5f,-0.5f,
        -0.5f,-0.5f,-0.5f,
        //NORMAL: (0, 0, -1)
        
        0.5f, 0.5f,-0.5f, 
        -0.5f,-0.5f,-0.5f,
        -0.5f, 0.5f,-0.5f, 
        //NORMAL: (0,0,-1)
        
        0.5f, 0.5f, 0.5f,
        0.5f,-0.5f,-0.5f,
        0.5f, 0.5f,-0.5f,
        //NORMAL: (1, 0, 0)
        
        0.5f,-0.5f,-0.5f,
        0.5f, 0.5f, 0.5f,
        0.5f,-0.5f, 0.5f,
        //NORMAL: (1, 0, 0)
        
        0.5f, 0.5f, 0.5f,
        0.5f, 0.5f,-0.5f,
        -0.5f, 0.5f,-0.5f,
        //NORMAL: (0, 1, 0)
        
        0.5f, 0.5f, 0.5f,
        -0.5f, 0.5f,-0.5f,
        -0.5f, 0.5f, 0.5f,
        //NORMAL: (0, 1, 0)
        
        -0.5f, 0.5f, 0.5f,
        -0.5f,-0.5f, 0.5f,
        0.5f,-0.5f, 0.5f,
        //NORMAL: (0, 0, 1)
        0.5f, 0.5f, 0.5f,
        -0.5f, 0.5f, 0.5f,
        0.5f,-0.5f, 0.5f
        //NORMAL: (0, 0, 1)
      };
    
  }
  public void update (float delta) {
    // I have no idea what I'm doing! 0_0
    Chunk c = Game.getChunks()[(int) chunkID.x][(int) chunkID.y][(int) chunkID.z];
    for(int x = 0; x < c.WIDTH; x++) {
      for (int y = 0; y < c.HEIGHT; y++) {
        for (int z = 0; z < c.DEPTH;z++) {
          
        } 
      }
    }
  }


  public void setChunkID(Vector3f chunkID) {
    this.chunkID  = chunkID;
    
  }
}
