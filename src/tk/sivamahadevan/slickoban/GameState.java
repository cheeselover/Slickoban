package tk.sivamahadevan.slickoban;
import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.tiled.TiledMap;

public class GameState extends BasicGameState{

	int stateID;
	TiledMap map;
	SpriteSheet charSheet;
	
	Sound hit, goal;
	
	Animation sprite, up, down, left, right;
	int[] duration = {100, 100, 100, 100};
	
	String level = "res/map/level1.tmx";
	
	float char_x, char_y;
	static final int TILE_SIZE = 32;
	boolean[][] blocked;
	boolean[][] goals;

	ArrayList<Box> boxes;
	Image[] box_images;
	
	Rectangle collision_box;
	String name = "";
	int name_x, name_y;
	
	public int num_moves, num_restarts = 0;
	
	UnicodeFont font; 
	
	public GameState(int stateID){
		this.stateID = stateID;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		map = new TiledMap(level);
		charSheet = new SpriteSheet(new Image("res/spritesheet/character.png"), 32, 32);
		
		num_moves = 0;
	
		hit = new Sound("res/sounds/hit.wav");
		goal = new Sound("res/sounds/goal.wav");
		
		Image[] moveUp = {charSheet.getSubImage(0, 3), charSheet.getSubImage(1, 3), charSheet.getSubImage(2, 3), charSheet.getSubImage(1, 3)};
		Image[] moveDown = {charSheet.getSubImage(0, 0), charSheet.getSubImage(1, 0), charSheet.getSubImage(2, 0), charSheet.getSubImage(1, 0)};
		Image[] moveLeft = {charSheet.getSubImage(0, 1), charSheet.getSubImage(1, 1), charSheet.getSubImage(2, 1), charSheet.getSubImage(1, 1)};
		Image[] moveRight = {charSheet.getSubImage(0, 2), charSheet.getSubImage(1, 2), charSheet.getSubImage(2, 2), charSheet.getSubImage(1, 2)};
		
		up = new Animation(moveUp, duration, false);
		down = new Animation(moveDown, duration, false);
		left = new Animation(moveLeft, duration, false);
		right = new Animation(moveRight, duration, false);
		sprite = down;
		
		font = new UnicodeFont("res/misc/font.ttf", 48, true, false);
		font.addAsciiGlyphs();
		font.getEffects().add(new ColorEffect(java.awt.Color.black));
		font.loadGlyphs();
		
		blocked = new boolean[map.getWidth()][map.getHeight()];
		goals = new boolean[map.getWidth()][map.getHeight()];
		boxes = new ArrayList<Box>();
		
		for(int x = 0; x < map.getWidth(); x++){
			for(int y = 0; y < map.getHeight(); y++){
				int tileID = map.getTileId(x, y, 0);
				String value = map.getTileProperty(tileID, "blocked", "false");
				
				if("true".equals(value)){
					blocked[x][y] = true;
				}

				value = map.getTileProperty(tileID, "moveable", "false");

				if("true".equals(value)){
					boxes.add(new Box(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE));
				}
				
				value = map.getTileProperty(tileID, "goal", "false");
				
				if("true".equals(value)){
					goals[x][y] = true;
				}
				
				value = map.getTileProperty(tileID, "player", "false");
				
				if("true".equals(value)){
					char_x = x * 32;
					char_y = y * 32;
				}
				
				value = map.getTileProperty(tileID, "name", "false");
				
				if(!"false".equals(value) && !"true".equals(value)){
					name = value;
					name_x = x * 32;
					name_y = y * 32;
				}
			}
		}

		box_images = new Image[boxes.size()];

		for(int i = 0; i < boxes.size(); i++){
			box_images[i] = new Image("res/tiles/box.png");
		}
		
		collision_box = new Rectangle(0, 0, 32, 32);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		map.render(0, 0);
		for(int i = 0; i < boxes.size(); i++){
			box_images[i].draw(boxes.get(i).getX(), boxes.get(i).getY());
		}
		sprite.draw(char_x, char_y);
		g.setFont(font);
		g.drawString(name, name_x, name_y);
		g.drawString(("res/map/level12.tmx".equals(level)) ? String.format("Moves\n-----\n%d", num_moves) : String.format("Moves: %d", num_moves), ("res/map/level12.tmx".equals(level)) ? 570 : 470, ("res/map/level12.tmx".equals(level)) ? 330 : 530);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Input input = gc.getInput();
		
		collision_box.setCenterX(char_x + 16);
		collision_box.setCenterY(char_y + 16);
		
		if(input.isKeyPressed(Input.KEY_UP) || input.isKeyPressed(Input.KEY_W)){
			sprite = up;
			
			if(!isBlocked(char_x, char_y - 1)){
				sprite.update(delta);
				
				char_y -= 32;
				num_moves++;
				
				if(doesBoxCollide("up")){
					char_y += 32;
					num_moves--;
				}
			}
		} else if(input.isKeyPressed(Input.KEY_DOWN) || input.isKeyPressed(Input.KEY_S)){
			sprite = down;
			
			if(!isBlocked(char_x, char_y + TILE_SIZE + 1)){
				sprite.update(delta);
				
				char_y += 32;
				num_moves++;
				
				if(doesBoxCollide("down")){
					char_y -= 32;
					num_moves--;
				}
			}
		} else if(input.isKeyPressed(Input.KEY_LEFT) || input.isKeyPressed(Input.KEY_A)){
			sprite = left;
			
			if(!isBlocked(char_x - 1, char_y)){
				sprite.update(delta);
				
				char_x -= 32;
				num_moves++;
				
				if(doesBoxCollide("left")){
					char_x += 32;
					num_moves--;
				}
			}
		} else if(input.isKeyPressed(Input.KEY_RIGHT) || input.isKeyPressed(Input.KEY_D)){
			sprite = right;
			
			if(!isBlocked(char_x + TILE_SIZE + 1, char_y)){
				sprite.update(delta);
				
				char_x += 32;
				num_moves++;
				
				if(doesBoxCollide("right")){
					char_x -= 32;
					num_moves--;
				}
			}
		}
		
		else if(input.isKeyPressed(Input.KEY_R)){
			this.init(gc, sbg);
			num_restarts++;
		}
		
		else if(input.isKeyPressed(Input.KEY_ESCAPE)){
			num_restarts = 0;
			num_moves = 0;
			MenuState temp = (MenuState) sbg.getState(1);
			temp.init(gc, sbg);
			sbg.enterState(2, new FadeOutTransition(), new FadeInTransition());
		}
		
		if(isGameWon()){
			MenuState temp = (MenuState) sbg.getState(1);
			temp.bg_music.stop();
			WinState temp2 = (WinState) sbg.getState(4);
			temp2.won = true;
			temp2.init(gc, sbg);
			sbg.enterState(4, new FadeOutTransition(), new FadeInTransition());
		}
		
		for(int i = 0; i < boxes.size(); i++){
			Box box = boxes.get(i);
			
			if(isGoal(box.getX(), box.getY())){
				box_images[i] = new Image("res/tiles/finished_box.png");
			} else {
				box_images[i] = new Image("res/tiles/box.png");
			}
		}
	}

	@Override
	public int getID() {
		return stateID;
	}
	
	private boolean isBlocked(float x, float y){
		int xBlock = (int) x / TILE_SIZE;
		int yBlock = (int) y / TILE_SIZE;
		
		return blocked[xBlock][yBlock];
	}
	
	private boolean isGoal(float x, float y){
		int xBlock = (int) x / TILE_SIZE;
		int yBlock = (int) y / TILE_SIZE;
		
		return goals[xBlock][yBlock];
	}
	
	private boolean doesBoxCollide(String type) throws SlickException{
		if(type.equals("left")){
			for(int i = 0; i < boxes.size(); i++){
				Box box = boxes.get(i);
				
				if(box.isPushedLeft(collision_box)){
					for(int j = 0; j < boxes.size(); j++){
						Box box2 = boxes.get(j);
						
						if(!box.equals(box2)){
							if(box2.isPushedLeft(box)){
								return true;
							}
						}
						
						if(isBlocked(box.getX() - 32, box.getY())){
							return true;
						}
					}
					
					box.move(-32, 0);
					
					if(isGoal(box.getX(), box.getY())){
						goal.play(1, 0.5f);
					} else {
						hit.play();
					}
				}
			}
		}
		
		if(type.equals("right")){
			for(int i = 0; i < boxes.size(); i++){
				Box box = boxes.get(i);
				
				if(box.isPushedRight(collision_box)){
					for(int j = 0; j < boxes.size(); j++){
						Box box2 = boxes.get(j);
						
						if(!box.equals(box2)){
							if(box2.isPushedRight(box)){
								return true;
							}
						}
						
						if(isBlocked(box.getX() + 32, box.getY())){
							return true;
						}
					}
					
					box.move(32, 0);
					
					if(isGoal(box.getX(), box.getY())){
						goal.play(1, 0.5f);
					} else {
						hit.play();
					}
				}
			}
		}
		
		if(type.equals("up")){
			for(int i = 0; i < boxes.size(); i++){
				Box box = boxes.get(i);
				
				if(box.isPushedUp(collision_box)){
					for(int j = 0; j < boxes.size(); j++){
						Box box2 = boxes.get(j);
						
						if(!box.equals(box2)){
							if(box2.isPushedUp(box)){
								return true;
							}
						}
						
						if(isBlocked(box.getX(), box.getY() - 32)){
							return true;
						}
					}
					
					box.move(0, -32);
					
					if(isGoal(box.getX(), box.getY())){
						goal.play(1, 0.5f);
					} else {
						hit.play();
					}
				}
			}
		}
		
		if(type.equals("down")){
			for(int i = 0; i < boxes.size(); i++){
				Box box = boxes.get(i);
				
				if(box.isPushedDown(collision_box)){
					for(int j = 0; j < boxes.size(); j++){
						Box box2 = boxes.get(j);
						
						if(!box.equals(box2)){
							if(box2.isPushedDown(box)){
								return true;
							}
						}
						
						if(isBlocked(box.getX(), box.getY() + 32)){
							return true;
						}
					}
					
					box.move(0, 32);
					
					if(isGoal(box.getX(), box.getY())){
						goal.play(1, 0.5f);
					} else {
						hit.play();
					}
				}
			}
		}
		
		return false;
	}
	
	private boolean isGameWon(){
		int num_fin = 0;
		
		for(Image i : box_images){
			if("res/tiles/finished_box.png".equals(i.getResourceReference())){
				num_fin += 1;
			}
		}
		
		return (num_fin == boxes.size()) ? true : false;
	}
	
	public void setLevel(int lnum){
		this.level = String.format("res/map/level%d.tmx", lnum);
	}
	
}
