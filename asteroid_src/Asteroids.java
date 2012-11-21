import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import java.io.*;

import java.util.ArrayList;

import java.io.*;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JDialog;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;

import org.newdawn.slick.*;
import org.newdawn.slick.font.effects.*;

import java.util.Arrays;
import java.util.HashMap;

//TODO:
//WHEN THE CRAFT DIES, RESET IT'S MOMENTUM
public final class Asteroids
{
	private static final Font javaFont = new Font("Times New Roman", Font.BOLD, 24);
    private static final UnicodeFont scoreFont = new UnicodeFont(javaFont, 20, false, false);
    private static final ColorEffect scoreFontColorEffect = new ColorEffect();

	private boolean debounceShoot = false;
	private boolean debounceThrust = false;
	private boolean gameOver = false;

	private int gameLevel = 1;
	private int gameScore = 0;


    //Returns the best DisplayMode to run Asteroids "in"
	private DisplayMode getBestDisplay(DisplayMode[] modeArray)
	{
		if(modeArray.length != 0)
        {
            DisplayModeSorter s = new DisplayModeSorter(modeArray);
            DisplayMode toReturn = s.getBestDisplayMode();
            System.out.println(toReturn);

            return toReturn;
        }

		System.out.println("No DisplayModes supplied.");
		return null;
	}

    //Adds Asteroid Objects to the screen
	private void addAsteroids()
	{
        //TODO update this shit
		for(int i = 0; i < 1 + 0*(gameLevel - 1); i++)
        {
			SpaceObject.allSpaceObjects.add(new Asteroid(new Vector(10 * Math.random(),
																	10 * Math.random()),

										    new Vector(Constants.WINDOW_WIDTH * Math.random(),
										    		   Constants.WINDOW_HEIGHT * Math.random()),

										    2));
		}
	}

    @SuppressWarnings("unchecked")
    private void initializeScoreFont()
    {
        try 
        {
            scoreFont.addAsciiGlyphs();
            scoreFont.getEffects().add(scoreFontColorEffect);
            scoreFont.loadGlyphs();
        }
        catch(SlickException s)
        {
            System.out.println("wtf");
        }
    }

    private void initializeDisplay()
    {
		try
        {
			DisplayMode bestDisplay = getBestDisplay(Display.getAvailableDisplayModes());
			//Display.setFullscreen(true);
			Display.setDisplayMode(bestDisplay);
			Display.setTitle("Asteroids");
			//Display.setIcon(...);

			Constants.WINDOW_HEIGHT = bestDisplay.getHeight();
			Constants.WINDOW_WIDTH = bestDisplay.getWidth();

			//Constants.SCALING_FACTOR = Constants.WINDOW_WIDTH / 768;
			//Constants.DELTA *= Constants.SCALING_FACTOR;
			//Constants.DELTA_T = .1;
			Constants.MAX_POSITION = new Vector(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

			SpaceObject.allSpaceObjects = new ArrayList<SpaceObject>();
			SpaceObject.objectsToAdd = new ArrayList<SpaceObject>();
			SpaceObject.objectsToRemove = new ArrayList<SpaceObject>();

			Display.create();
            Keyboard.create();
		}
		catch (LWJGLException e)
        {
			e.printStackTrace();
			System.exit(0);
		}

        return;
    }

	public void start()
	{
        initializeDisplay();
        initializeScoreFont();

		//Craft that user controls.
		Craft userCraft = Craft.getInstance();

		//Putting userCraft at the middle of the screen
		userCraft.setPosition(new Vector(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 2));

		SpaceObject.allSpaceObjects.add(userCraft);

		addAsteroids();

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, 0, 1, -1);

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		//Everything we draw will be white, so set it once and ft it
		GL11.glColor3f(1.0f, 1.0f, 1.0f);

		while(!gameOver) 
        {
			while(!Display.isCloseRequested() && !gameOver)
            {
                if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
                {
                    System.out.println("ESC BUTTON PRESSED");
                    System.out.println(Keyboard.isRepeatEvent());

                    PauseDialog pauseWindow = new PauseDialog();
                    pauseWindow.setVisible(true);

                    //Waiting until the pause window is closed
                    try
                    {
                        synchronized(pauseWindow) {
                            pauseWindow.wait();
                        }
                    }
                    catch(InterruptedException i) {System.out.println("help me"); }

                    switch(pauseWindow.getReturnCode())
                    {
                        case Constants.QUIT_GAME:
                            System.out.println("Asteroids got QUIT_GAME");
                            Keyboard.destroy();
                            Display.destroy();
                            gameOver = true;
                            return;

                        case Constants.CONTINUE_GAME:
                            System.out.println("Asteroids got CONTINUE_GAME");
                            break;

                       case Constants.CONTINUE_GAME_WITH_NEW_OPTIONS:
                            System.out.println("Asteroids got CONTINUE_GAME_WITH_NEW_OPTIONS");
                            break;

                       case Constants.SAVE_GAME:
                            System.out.println("Asteroids got SAVE_GAME");
                            break;

                       default:
                            System.out.println("bad");
                            System.out.println(pauseWindow.getReturnCode());
                            break;
                    }

                    //Fixes esc key bug
                    //needs to be fixed in lwjgl
                    try 
                    {
                        Keyboard.destroy();
                        Keyboard.create();
                    }
                    catch(LWJGLException e) {}
                }

                if(Keyboard.isKeyDown(Keyboard.KEY_UP))
                {
                    userCraft.thrust();

                    if(debounceThrust) {
                        userCraft.drawThruster();
                    }
                    debounceThrust = !debounceThrust;
                }

                if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                    userCraft.changeTheta(Constants.ROTATION_SPEED);
                }

                if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                    userCraft.changeTheta(-Constants.ROTATION_SPEED);
                }

                if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
                {
                    if(!debounceShoot)
                    {
                        userCraft.shoot();
                        //soundPlayer.play(0);
                        debounceShoot = true;
                    }
                }
                else {
                    debounceShoot = false;
                }

				//Clearing the screen
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                //Drawing HUD
                scoreFont.drawString(0, 0, "Score: " + gameScore, org.newdawn.slick.Color.white);
                scoreFont.drawString(0, 20, "Lives: " + userCraft.getLives(), org.newdawn.slick.Color.white);
                scoreFont.drawString(0, 40, "Level: " + gameLevel, org.newdawn.slick.Color.white);
                scoreFont.drawString(100, 0, " ", org.newdawn.slick.Color.white);

				try
                {
				    for(SpaceObject s : SpaceObject.allSpaceObjects)
                    {
			    		if(s instanceof Asteroid)
                        {
			    			if(userCraft.position.distanceTo(s.position) < ((Asteroid)s).radius + userCraft.radius)
                            {
			    				userCraft.subtractLife();

			    				//Resetting userCraft to the middle of the screen
								userCraft.setPosition(new Vector(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 2));

			    				if(userCraft.getLives() == 0) {
			    					gameOver = true;
			    				}
			    				else {
			    					throw new Exception("Reset Asteroids");
			    				}
	 		    			}
			    		}

			    		if(s instanceof Missle)
                        {
			    			for(SpaceObject a : SpaceObject.allSpaceObjects)
                            {
			    				if(a instanceof Asteroid)
                                {

			    					//if(we shoot an asteroid)
			    					if(s.position.distanceTo(a.position) < ((Asteroid)a).radius + ((Missle)s).radius)
                                    {
			    						s.delete();
			    						a.delete();
			    						gameScore += 5;
			    					}
			    				}
			    			}
			    		}

				    	s.updatePosition();
				    	s.draw();
				    }

				    for(SpaceObject toAdd : SpaceObject.objectsToAdd) {
				    	SpaceObject.allSpaceObjects.add(toAdd);
				    }
				    SpaceObject.objectsToAdd.clear();

				    for(SpaceObject toRemove: SpaceObject.objectsToRemove) {
				    	SpaceObject.allSpaceObjects.remove(toRemove);
				    }
				    SpaceObject.objectsToRemove.clear();

				    if(SpaceObject.allSpaceObjects.size() == 1) {
				    	gameLevel++;
				    	addAsteroids();
                        System.out.println("Next level");
				    }

					Display.update();
				}

				//Crash, reset asteroids
				catch(Exception e)
                {
					SpaceObject.allSpaceObjects.clear();
					SpaceObject.allSpaceObjects.add(userCraft);

					addAsteroids();
				}
			}
		}

		try {
			//soundPlayer.finalize();
		}
		catch(Throwable t) {}

		Display.destroy();
	}

	public static void main(String[] argv)
	{
		Asteroids game = new Asteroids();
		game.start();
	}
}
