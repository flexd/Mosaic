package entities;
 
public interface IMoveableEntity extends IEntity {
        public double getDX();
        public double getDY();
        public void setDX(double dx);
        public void setDY(double dy);
        public void move(int dx, int dy);
}