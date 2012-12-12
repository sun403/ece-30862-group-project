import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;

import org.newdawn.slick.*;
import org.newdawn.slick.font.effects.*;

import java.io.*;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.lang.reflect.*;

public class Asteroids implements Serializable
{
    private static final Font javaFont = new Font("Times New Roman", Font.BOLD, 24);
    private static final UnicodeFont scoreFont = new UnicodeFont(javaFont, 20, false, false);
    private static final ColorEffect scoreFontColorEffect = new ColorEffect();
    private static final File highScoresFile = new File("high_scores.txt");

    private static SoundPlayer soundPlayer;

    public static UserCraft userCraft;
    public static AlienCraft alienCraft;
    public static GravitationalObject gravityObject;

    public Boolean debounceShoot = false;
    public Boolean debounceThrust = false;
    public Boolean gameOver = false;
    public Boolean alienPresent = false;

    public Integer gameScore;
    public Integer gameLevel;

    public HighScore[] highScores;

    public OptionsContainer gameOptions;

    public Asteroids()
    {
        highScores = new HighScore[10];
        soundPlayer = new SoundPlayer();

        SpaceObject.allSpaceObjects = new ArrayList<SpaceObject>();
        SpaceObject.objectsToAdd = new ArrayList<SpaceObject>();
        SpaceObject.objectsToRemove = new ArrayList<SpaceObject>();

        initializeDisplay();
        initializeScoreFont();

        alienCraft = AlienCraft.getInstance();

        userCraft = UserCraft.getInstance();
        userCraft.resetPosition();
        SpaceObject.allSpaceObjects.add(userCraft);

        //necessary
        alienCraft.setUserCraftReference(userCraft);

        gravityObject = GravitationalObject.getInstance();
        SpaceObject.allSpaceObjects.add(gravityObject);

        gameOptions = new OptionsContainer();
        applyGameOptions();

        loadHighScores();
    }

    private void loadHighScores()
    {
        try
        {
            Scanner highScoreScanner = new Scanner(highScoresFile);
            int i = 0;

            while(highScoreScanner.hasNext() && i < 10)
            {
                String nextLine = highScoreScanner.nextLine();
                String[] lineParts = nextLine.split(" ");

                highScores[i] = new HighScore(lineParts[0], 
                        Integer.parseInt(lineParts[1]));
                i++;
            }
            while(i < 10)
            {
                highScores[i] = new HighScore();
                i++;
            }

        }
        catch(FileNotFoundException f) 
        {
            for(int i = 0; i < 10; i++) {
                highScores[i] = new HighScore();
            }
        }
    }

    private void applyGameOptions()
    {
        gameLevel = gameOptions.startingLevel;
        gameScore = 0;

        if(gameOptions.gravitationalObject) {
            gravityObject.turnOn();
        }
        else {
            gravityObject.turnOff();
        }

        if(gameOptions.resetHighScores)
        {
            for(int i = 0; i < 10; i++) {
                highScores[i] = new HighScore();
            }
        }

        if(gameOptions.saveFile != null) {
            loadGame(gameOptions.saveFile);
        }
    }

    //Returns the best DisplayMode to run Asteroids "in"
    private DisplayMode getBestDisplay(DisplayMode[] modeArray)
    {
        if(modeArray.length != 0)
        {
            DisplayModeSorter s = new DisplayModeSorter(modeArray);
            DisplayMode toReturn = s.getBestDisplayMode();

            return toReturn;
        }

        return null;
    }

    //Adds Asteroid Objects to the screen
    private void addAsteroids()
    {
        for(int i = 0; i < gameOptions.numberOfAsteroidsPerLevel; i++)
        {
            Asteroid toAdd;
            Vector toAddPosition;

            do
            {
                //TODO
                //what is this constant
                toAdd = new Asteroid(4 * gameLevel);
                toAddPosition = toAdd.getPosition();
            }
            while(userCraft.getPosition().distanceTo(toAddPosition) <
                    Constants.MIN_BUFFER_DISTANCE);

            SpaceObject.allSpaceObjects.add(toAdd);
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
            System.out.println(s);
            System.exit(-1);
        }
    }

    private void initializeDisplay()
    {
        try
        {
            DisplayMode bestDisplay = getBestDisplay(Display.getAvailableDisplayModes());
            Display.setDisplayMode(bestDisplay);
            Display.setTitle("Asteroids");

            Constants.WINDOW_HEIGHT = bestDisplay.getHeight();
            Constants.WINDOW_WIDTH = bestDisplay.getWidth();
            Constants.MAX_POSITION = new Vector(
                    Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

            Display.create();
            Keyboard.create();
        }
        catch (LWJGLException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }

        return;
    }

    private void updateHUD()
    {
        scoreFont.drawString(0, 0, "Score: " + gameScore, org.newdawn.slick.Color.white);
        scoreFont.drawString(0, 20, "Lives: " + userCraft.getLives(), org.newdawn.slick.Color.white);
        scoreFont.drawString(0, 40, "Level: " + gameLevel, org.newdawn.slick.Color.white);

        //Don't know why this is necessary, but without it, this won't work
        scoreFont.drawString(100, 0, " ", org.newdawn.slick.Color.white);
    }

    //detects collisions
    //deletes asteroids/subtracts lives based on any collisions
    private boolean detectCollisions()
    {
        boolean crashed = false;

        for(SpaceObject s : SpaceObject.allSpaceObjects)
        {
            //If we're too close to something, we crash
            if(s instanceof Asteroid || 
                    s instanceof GravitationalObject ||
                    s instanceof AlienCraft ||
                    s instanceof AlienMissle)
            {
                if(userCraft.getPosition().distanceTo(s.getPosition()) < 
                        s.getRadius() + userCraft.getRadius())
                {

                    if(s instanceof AlienMissle) {
                        s.delete();
                    }

                    if(userCraft.getLives() == 0 && 
                            !gameOptions.unlimitedLives) {
                        gameOver = true;
                            }
                    else
                    {
                        crashed = true;
                    }

                    soundPlayer.play(Constants.EXPLOSION_SOUND);
                }
            }

            if(s instanceof UserMissle)
            {
                for(SpaceObject a : SpaceObject.allSpaceObjects)
                {
                    if(a instanceof Asteroid)
                    {
                        //if(we shoot an asteroid)
                        if(s.getPosition().distanceTo(a.getPosition()) < 
                                a.getRadius() + s.getRadius())
                        {
                            s.delete();
                            a.delete();
                            gameScore += 5;
                            soundPlayer.play(Constants.EXPLOSION_SOUND);
                        }
                    }
                    else if(a instanceof AlienCraft)
                    {
                        if(s.getPosition().distanceTo(a.getPosition()) <
                                ((AlienCraft)a).getRadius() + ((UserMissle)s).getRadius())
                        {
                            AlienCraft aCraft = ((AlienCraft)a);
                            aCraft.subtractLife();
                            s.delete();

                            if(aCraft.getLives() == 0)
                            {
                                aCraft.delete();
                                alienPresent = false;
                                soundPlayer.play(Constants.EXPLOSION_SOUND);
                                soundPlayer.stopPlay(Constants.UFO_SOUND);
                            }
                        }
                    }
                }
            }

            if(s instanceof GravitationalObject)
            {
                if(gameOptions.visibleGravitationalObject) {
                    s.draw();
                }
            }
            else {
                s.draw();
            }

            s.updatePosition();

        }

        return crashed;
    }

    private void saveGame(File saveFile)
    {
        try
        {
            FileOutputStream outputFile = new FileOutputStream(saveFile);
            ObjectOutput output = new ObjectOutputStream(outputFile);

            output.writeObject(this);
            output.writeObject(SpaceObject.allSpaceObjects);

            output.flush();
            outputFile.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
            System.exit(-1);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadGame(File saveFile)
    {
        try
        {
            FileInputStream inputFile = new FileInputStream(saveFile);
            ObjectInputStream input = new ObjectInputStream(inputFile);

            Asteroids loadedGame = (Asteroids)input.readObject();
            SpaceObject.allSpaceObjects = (ArrayList<SpaceObject>)(input.readObject());

            input.close();
            inputFile.close();

            Field[] loadedFields = loadedGame.getClass().getFields();
            Field[] thisFields = this.getClass().getFields();

            for(Field loadedField : loadedFields)
            {
                for(Field thisField : thisFields)
                {
                    if(thisField.getName().equals(loadedField.getName())) 
                    {
                        try {
                            thisField.set(this, loadedField.get(loadedGame));
                        }
                        catch(Exception e)
                        {
                            System.out.println(e);
                            System.exit(-1);
                        }
                    }
                }
            }

            for(SpaceObject s : SpaceObject.allSpaceObjects)
            {
                if(s instanceof UserCraft) {
                    userCraft = (UserCraft)s;
                }
            }
        }
        catch(IOException i)
        {
            System.out.println(i);
            System.exit(-1);
        }
        catch(ClassNotFoundException c)
        {
            System.out.println(c);
            System.exit(-1);
        }
    }

    private void handleEscapeButton() throws QuitGameException
    {
        PauseDialog pauseWindow = new PauseDialog(gameOptions);
        pauseWindow.setVisible(true);

        //Waiting until the pause window is closed
        try
        {
            synchronized(pauseWindow) {
                pauseWindow.wait();
            }
        }
        catch(InterruptedException i) 
        {
            System.out.println(i);
            System.exit(-1);
        }

        switch(pauseWindow.getReturnCode())
        {
            case Constants.QUIT_GAME:
                gameOver = true;
                throw new QuitGameException();

            case Constants.CONTINUE_GAME:
                break;

            case Constants.CONTINUE_GAME_WITH_NEW_OPTIONS:
                gameOptions = pauseWindow.getOptions();
                applyGameOptions();
                break;

            case Constants.SAVE_GAME:
                saveGame(pauseWindow.getSaveFile());
                break;

            default:
                System.out.println(pauseWindow.getReturnCode());
                break;
        }

        //Fixes esc key bug
        //Needs to be fixed in lwjgl
        try 
        {
            Keyboard.destroy();
            Keyboard.create();
        }
        catch(LWJGLException e)
        {
            System.out.println(e);
            System.exit(-1);
        }
    }

    private void handleKeyboardInput() throws QuitGameException
    {
        //up key
        if(Keyboard.isKeyDown(Keyboard.KEY_UP))
        {
            userCraft.thrust(true);

            if(debounceThrust) {
                userCraft.drawThruster();
            }
            debounceThrust = !debounceThrust;
        }
        else {
            userCraft.thrust(false);
        }

        //right key
        if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            userCraft.changeTheta(Constants.ROTATION_SPEED);
        }

        //left key
        if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            userCraft.changeTheta(-Constants.ROTATION_SPEED);
        }

        //space key
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
        {
            if(!debounceShoot)
            {
                userCraft.shoot();
                soundPlayer.play(Constants.SHOOT_SOUND);
                debounceShoot = true;
            }
        }
        else {
            debounceShoot = false;
        }

        //escape key
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            handleEscapeButton();
        }
    }

    public void start()
    {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, 0, 1, -1);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        //Everything we draw will be white, so set it once and forget it(?)
        GL11.glColor3f(1.0f, 1.0f, 1.0f);

        addAsteroids();

        try
        {
            //main game loop
            while(!gameOver) 
            {
                while(!Display.isCloseRequested() && !gameOver)
                {
                    //Handling user inputs
                    handleKeyboardInput();

                    //Clearing the screen
                    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                    //Drawing HUD
                    updateHUD();

                    boolean crashed = detectCollisions();

                    if(Math.random() < 0.001 && !alienPresent)
                    {
                        alienPresent = true;
                        alienCraft.resetVelocity();
                        alienCraft.resetPosition();
                        alienCraft.resetLives();
                        SpaceObject.allSpaceObjects.add(alienCraft);
                        soundPlayer.play(Constants.UFO_SOUND);
                    }
                    if(alienPresent)
                    {
                        if(Math.random() < 0.0005 * gameLevel)
                        {
                            alienCraft.shoot();
                            soundPlayer.play(Constants.SHOOT_SOUND);
                        }
                    }

                    if(crashed)
                    {
                        userCraft.subtractLife();
                        userCraft.resetPosition();
                        userCraft.resetVelocity();
                        userCraft.resetTheta();
                    }
                    else
                    {
                        updateAsteroids();

                        //if we have no more asteroids on the field
                        if(SpaceObject.allSpaceObjects.size() == 2)
                        {
                            gameScore += gameLevel * 100;
                            userCraft.resetLives();
                            gameLevel++;
                            addAsteroids();
                        }
                    }

                    Display.update();
                }
            }
        }
        catch(QuitGameException e) {}

        try {
            soundPlayer.finalize();
        }
        catch(Throwable t) 
        {
            System.out.println(t);
            System.exit(-1);
        }

        Display.destroy();

        updateHighScores();
        displayHighScores();
    }

    private void updateHighScores()
    {
        int i;
        int index = -1;

        if(gameScore == 0) {
            gameScore--;
        }

        for(i = 0; i < highScores.length; i++)
        {
            if(highScores[i].getScore() < gameScore)
            {
                //Moving the high scores back one
                for(int j = highScores.length - 1; j > i; j--) {
                    highScores[j] = highScores[j - 1];
                }

                index = i;

                //Exiting the loop
                i = 99;
            }
        }

        if(i == 100)
        {
            //Prompt
            NameInputDialog box = new NameInputDialog();
            box.setVisible(true);

            try
            {
                synchronized(box) {
                    box.wait();
                }
            }
            catch(InterruptedException z)
            {
                System.out.println(z);
                System.exit(-1);
            }

            if(box.getName().length() > 0) {
                highScores[index] = new HighScore(box.getName(), gameScore);
            }

            saveHighScores();
        }
    }

    private void saveHighScores()
    {
        try
        {
            FileWriter writeStream = new FileWriter(highScoresFile);
            BufferedWriter outputFile = new BufferedWriter(writeStream);

            for(HighScore h : highScores) {
                outputFile.write(String.format("%s %d\n", h.getName(), h.getScore()));
            }

            outputFile.close();
            writeStream.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
            System.exit(-1);
        }
    }

    private void displayHighScores()
    {
        HighScoreDisplay scoreDisplay = new HighScoreDisplay(highScores);
        scoreDisplay.setVisible(true);

        try
        {
            synchronized(scoreDisplay) {
                scoreDisplay.wait();
            }
        }
        catch(InterruptedException z)
        {
            System.out.println(z);
            System.exit(-1);
        }
    }


    //Removes asteroids using a silly but necessary process
    private void updateAsteroids()
    {
        //This is necessary because otherwise I get a java error
        //Something about concurrency modification error
        for(SpaceObject toAdd : SpaceObject.objectsToAdd) {
            SpaceObject.allSpaceObjects.add(toAdd);
        }
        SpaceObject.objectsToAdd.clear();

        for(SpaceObject toRemove : SpaceObject.objectsToRemove) {
            SpaceObject.allSpaceObjects.remove(toRemove);
        }
        SpaceObject.objectsToRemove.clear();
    }

    public static void main(String[] argv)
    {
        Asteroids game = new Asteroids();
        game.start();
    }
}
