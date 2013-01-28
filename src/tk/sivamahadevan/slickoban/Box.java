package tk.sivamahadevan.slickoban;
import org.newdawn.slick.geom.Rectangle;

@SuppressWarnings("serial")
public class Box extends Rectangle{

	public Box(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	
	public void move(int x, int y){
		this.x += x;
		this.y += y;
	}
	
	public boolean isPushedLeft(Rectangle r){
		if(this.x + 32 == r.getX() && this.y == r.getY()){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isPushedRight(Rectangle r){
		if(this.x - 32 == r.getX() && this.y == r.getY()){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isPushedUp(Rectangle r){
		if(this.y + 32 == r.getY() && this.x == r.getX()){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isPushedDown(Rectangle r){
		if(this.y - 32 == r.getY() && this.x == r.getX()){
			return true;
		} else {
			return false;
		}
	}
	
}
