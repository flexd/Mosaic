package entities;
 
public interface IMoveableEntity extends IEntity {
        public float getDX();
        public float getDY();
        public void setDX(float dx);
        public void setDY(float dy);
        public void move(int dx, int dy);
}