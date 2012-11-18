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
import java.util.Scanner;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JDialog;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
//import org.lwjgl.util.*;
//
import org.newdawn.slick.*;
import org.newdawn.slick.font.effects.*;
import java.util.List;

public final class Asteroids
{
	Font javaFont = new Font("Times New Roman", Font.BOLD, 24);
    UnicodeFont scoreFont = new UnicodeFont(javaFont, 20, false, false);
    ColorEffect scoreFontColorEffect = new ColorEffect();

	boolean debounceShoot = false;
	boolean debounceThrust = false;
	boolean gameOver = false;

	int gameLevel = 1;
	int gameScore = 0;

	public DisplayMode largestDisplay(DisplayMode modeArray[])
	{
		if(modeArray.length != 0)
        {
			int maxWidth = modeArray[0].getWidth();
			//int maxHeight = modeArray[0].getHeight();
			int maxHeight = 600;
			int maxFrequency = modeArray[0].getFrequency();
			int maxBitsPerPixel = modeArray[0].getBitsPerPixel();

			//Setting maxWidth, maxHeight, maxFrequency, and maxBitsPerPixel.
            //hacked kinda to runat 800x600

			for(int i = 1; i < modeArray.length; i++)
            {
				if(modeArray[i].getWidth() > maxWidth) {
					//maxWidth = modeArray[i].getWidth();
                    maxWidth = 800;
				}

				if(modeArray[i].getHeight() > maxHeight) {
					//maxHeight = modeArray[i].getHeight();
                    maxHeight = 600;
				}

				if(modeArray[i].getFrequency() > maxFrequency) {
					maxFrequency = modeArray[i].getFrequency();
				}

				if(modeArray[i].getBitsPerPixel() > maxBitsPerPixel) {
					maxBitsPerPixel = modeArray[i].getBitsPerPixel();
				}

				for(i = 0; i < modeArray.length; i++)
                {
                    System.out.println(modeArray[i]);
					if (modeArray[i].getWidth() == maxWidth &&
						modeArray[i].getHeight() == maxHeight &&
						modeArray[i].getFrequency() == maxFrequency &&
						modeArray[i].getBitsPerPixel() == maxBitsPerPixel)
                    {
							return modeArray[i];
                    }
				}

				System.out.println("Not 1 DisplayMode holds all of the max properties. This shouldn't happen.");
                System.out.println(String.format("%d %d %d %d", maxWidth, maxHeight, maxFrequency, maxBitsPerPixel));

				return null;
			}
		}

		System.out.println("No DisplayModes supplied.");

		return null;
	}

	public void addAsteroids()
	{
		for(int i = 0; i < 10 + 5*(gameLevel - 1); i++)
        {
			SpaceObject.allSpaceObjects.add(new Asteroid(new Vector(10 * Math.random(),
																	10 * Math.random()),

										    new Vector(Constants.SCREEN_WIDTH * Math.random(),
										    		   Constants.SCREEN_HEIGHT * Math.random()),

										    2));
		}
	}

    @SuppressWarnings("unchecked")
    public void initializeScoreFont()
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

	public void start()
	{
		try
        {
			DisplayMode biggestDisplay = largestDisplay(Display.getAvailableDisplayModes());
			// Display.setFullscreen(true);
			Display.setDisplayMode(biggestDisplay);
			Display.setTitle("Asteroids");
			//Display.setIcon(...);

			Constants.SCREEN_HEIGHT = biggestDisplay.getHeight();
			Constants.SCREEN_WIDTH = biggestDisplay.getWidth();

			Constants.SCALING_FACTOR = Constants.SCREEN_WIDTH / 768;
			Constants.DELTA *= Constants.SCALING_FACTOR;
			Constants.DELTA_T = .1;
			Constants.MAX_POSITION = new Vector(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

			SpaceObject.allSpaceObjects = new ArrayList<SpaceObject>();
			SpaceObject.objectsToAdd = new ArrayList<SpaceObject>();
			SpaceObject.objectsToRemove = new ArrayList<SpaceObject>();

			Display.create();
            Keyboard.create();
            //Keyboard.enableRepeatEvents(false);

		}
		catch (LWJGLException e)
        {
			e.printStackTrace();
			System.exit(0);
		}

		//Craft that user controls.
		Craft userCraft = Craft.getInstance();

		//Putting userCraft at the middle of the screen
		userCraft.setPosition(new Vector(Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT / 2));

		SpaceObject.allSpaceObjects.add(userCraft);

		addAsteroids();

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, 0, 1, -1);

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		//Everything we draw will be white, so set it once and ft it
		GL11.glColor3f(1.0f, 1.0f, 1.0f);

        initializeScoreFont();

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
                scoreFont.drawString(0, 0, "Score: " + gameScore, org.newdawn.slick.Color.white);
                scoreFont.drawString(100, 0, " ", org.newdawn.slick.Color.white);


				//GL11.glBegin(GL11.GL_QUADS);
				//GL11.glBegin(GL11.GL_POINTS);

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
								userCraft.setPosition(new Vector(Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT / 2));

								System.out.println("Here");

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
