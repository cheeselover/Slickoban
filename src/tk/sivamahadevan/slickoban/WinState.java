package tk.sivamahadevan.slickoban;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class WinState extends BasicGameState{

	int stateID;
	boolean won = false;
	GameState temp;
	UnicodeFont font;
	int num_restarts;
	
	public WinState(int stateID){
		this.stateID = stateID;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void init(final GameContainer gc, final StateBasedGame sbg) throws SlickException {
		
		if(won){
			new Sound("res/sounds/win.wav").play();
			num_restarts = temp.num_restarts;
			temp.num_restarts = 0;
		}
		temp = (GameState) sbg.getState(3);
		
		font = new UnicodeFont("res/misc/font.ttf", 64, true, false);
		font.addAsciiGlyphs();
		font.getEffects().add(new ColorEffect(java.awt.Color.white));
		font.loadGlyphs();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		new Image("res/backgrounds/win.jpg").draw();
		
		g.setColor(Color.black);
		g.fillRoundRect(150, 180, 500, 100, 50);
		g.fillRoundRect(150, 360, 500, 100, 50);
		
		g.setFont(font);
		g.setColor(Color.white);
		g.drawString(String.format("In %d Moves\n", temp.num_moves), 250, 200);
		g.drawString(String.format("and %d Restarts", num_restarts), 210, 380);
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Input input = gc.getInput();
		
		if(input.isKeyPressed(Input.KEY_M)){
			MenuState temp = (MenuState) sbg.getState(1);
			temp.init(gc, sbg);
			sbg.enterState(2, new FadeOutTransition(), new FadeInTransition());
		}
	}

	@Override
	public int getID() {
		return stateID;
	}
	
}
