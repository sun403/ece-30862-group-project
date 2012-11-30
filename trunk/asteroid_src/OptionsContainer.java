import java.io.File;
import java.io.*;

public class OptionsContainer implements Serializable
{
    public boolean gravitationalObject = false;
    public boolean visibleGravitationalObject = false;
    public boolean unlimitedLives = false;
    public int numberOfAsteroidsPerLevel = 4;
    public int startingLevel = 1;
    public boolean resetHighScores = false;
    public File saveFile = null;
}
