package tk.sivamahadevan.slickoban;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;

public class Level extends MouseOverArea{

	public static int global_level_num = 0;
	public int level_num;
	
	
	public Level(GUIContext container, Image image, int x, int y) {
		super(container, image, x, y);
		global_level_num += 1;
		level_num = global_level_num;
	}
	
	public int getNum(){
		return this.level_num;
	}
	
}
