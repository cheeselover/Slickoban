package tk.sivamahadevan.slickoban;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class LevelSelectState extends BasicGameState{

	int stateID;
	Image bg, header, footer;
	MouseOverArea arrow_left;
	Rectangle selected;
	Level[][] levels;
	int page_num;
	Animation torch;
	Random r_music;
	
	public LevelSelectState(int stateID){
		this.stateID = stateID;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		bg = new Image("res/backgrounds/levels.jpg");
		header = new Image("res/images/level_select_header.png");
		footer = new Image("res/images/level_select_footer.png");
		
		torch = new Animation(new SpriteSheet("res/spritesheet/torch.png", 58, 113), 100);
		
		arrow_left = new MouseOverArea(gc, new Image("res/images/arrow_left.png"), 211, 518);
		arrow_left.setMouseOverImage(new Image("res/images/arrow_left_over.png"));
		
		r_music = new Random();
		
		page_num = 0;
		
		selected = new Rectangle(0, 0, 150, 110);
		selected.setCenterX(145);
		selected.setCenterY(175);
		
		levels = new Level[4][3];
		
		for(int i = 0; i < levels.length; i++){
			for(int j = 0; j < levels[i].length; j++){
				levels[i][j] = new Level(gc, new Image(String.format("res/levels/%d.png", i + 1 + (j * 4))), 75 + (165 * i), 125 + (125 * j));
			}
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		bg.draw();
		header.draw(170, 10);
		footer.draw(330, 525);
		arrow_left.render(gc, g);
		g.fill(selected);
		
		for(int i = 0; i < levels.length; i++){
			for(int j = 0; j < levels[i].length; j++){
				levels[i][j].render(gc, g);
			}
		}
		
		torch.draw(7, 248);
		torch.draw(735, 248);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Input input = gc.getInput();
		
		for(int i = 0; i < levels.length; i++){
			for(int j = 0; j < levels[i].length; j++){
				if(levels[i][j].isMouseOver() && input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
					GameState temp = (GameState) sbg.getState(3);
					temp.setLevel(i + 1 + (j * 4));
					temp.init(gc, sbg);
					
					final MenuState temp2 = (MenuState) sbg.getState(1);
					temp2.bg_music.fade(1000, 0, true);
					sbg.enterState(3, new FadeOutTransition(), new FadeInTransition());
					
					new Timer().schedule(
							new TimerTask(){

								@Override
								public void run(){
									try{
										temp2.bg_music = new Music(String.format("res/music/%d.ogg", r_music.nextInt(27) + 1));
										temp2.bg_music.setPosition(0);
										temp2.bg_music.loop(1, 0);
										temp2.bg_music.fade(10000, 0.35f, false);
									}catch(SlickException e){
									
									}
								}
							}
					, 2000);
					
				} else if(levels[i][j].isMouseOver()){
					selected.setX(levels[i][j].getX() - 5);
					selected.setY(levels[i][j].getY() - 5);
				}
			}
		}
		
		if(arrow_left.isMouseOver() && input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
			sbg.enterState(1, new FadeOutTransition(), new FadeInTransition());
		}
	}

	@Override
	public int getID() {
		return stateID;
	}
	
}
