package tk.sivamahadevan.slickoban;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class MenuState extends BasicGameState{

	int stateID;
	Image bg, header;
	UnicodeFont font;
	Animation torch;
	MouseOverArea play, options;
	Music bg_music;
	
	public MenuState(int stateID){
		this.stateID = stateID;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		bg = new Image("res/backgrounds/levels.jpg");
		header = new Image("res/images/menu_header.png");

		torch = new Animation(new SpriteSheet("res/spritesheet/torch.png", 58, 113), 100);
		
		play = new MouseOverArea(gc, new Image("res/images/menu_play.png"), 70, 100);
		play.setMouseOverImage(new Image("res/images/menu_play_over.png"));
		options = new MouseOverArea(gc, new Image("res/images/menu_options.png"), 395, 100);
		options.setMouseOverImage(new Image("res/images/menu_options_over.png"));
		
		font = new UnicodeFont("res/misc/font.ttf", 64, true, false);
		font.addAsciiGlyphs();
		font.getEffects().add(new ColorEffect(java.awt.Color.black));
		font.loadGlyphs();
		
		bg_music = new Music("res/music/title.ogg");
		bg_music.loop();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		bg.draw();
		header.draw(192, 10);
		g.setFont(font);
		g.drawString("Dungeons Edition", 165, 537);
		
		torch.draw(7, 248);
		torch.draw(735, 248);
		
		play.render(gc, g);
		options.render(gc, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Input input = gc.getInput();
		
		if(play.isMouseOver() && input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
			LevelSelectState temp = (LevelSelectState) sbg.getState(2);
			temp.init(gc, sbg);
			sbg.enterState(2, new FadeOutTransition(), new FadeInTransition());
		} else if(options.isMouseOver() && input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
			JOptionPane.showMessageDialog(new JFrame(), "What options could you possibly need in this overly simplistic game?!?!\n\nJust play it like you're supposed to.", "No Options", JOptionPane.PLAIN_MESSAGE, new ImageIcon("res/tiles/finished_box.png"));
		}
		
	}

	@Override
	public int getID() {
		return stateID;
	}
	
}
