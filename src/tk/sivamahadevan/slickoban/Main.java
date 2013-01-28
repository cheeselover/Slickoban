package tk.sivamahadevan.slickoban;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Main extends StateBasedGame{

	public final int MENU_ID = 1;
	public final int LEVEL_SELECT_ID = 2;
	public final int GAME_ID = 3;
	public final int WIN_ID = 4; 
	
	public Main(String name) {
		super(name);
	}

	public static void main(String[] args) throws SlickException{
		AppGameContainer app = new AppGameContainer(new Main("SLICKoban Dungeons 1.0"));
		
		app.setDisplayMode(800, 608, false);
		app.setIcon("res/tiles/finished_box.png");
		app.setTargetFrameRate(30);
		app.setShowFPS(false);
		app.start();
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		this.addState(new MenuState(MENU_ID));
		this.addState(new LevelSelectState(LEVEL_SELECT_ID));
		this.addState(new GameState(GAME_ID));
		this.addState(new WinState(WIN_ID));
		this.enterState(MENU_ID);
	}

}
