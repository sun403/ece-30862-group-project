import javax.swing.JFrame;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

public class OptionsDialog extends JFrame
{
    private JCheckBox gravitationalObject;
    private JCheckBox visibleGravitationalObject;
    private JCheckBox unlimitedLives;

    private JLabel numberOfAsteroidsLabel;
    private JTextField numberOfAsteroidsBox;

    private JLabel startingLevelLabel;
    private JTextField startingLevelBox;

    private JButton resetHighScore;
    private JButton restoreSavedGame;

    public OptionsDialog()
    {
        int windowWidth = 250;
        int windowHeight = 147;
        
        setTitle("Options");
        setSize(windowWidth, windowHeight);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        gravitationalObject = new JCheckBox("Gravitational Object", false);
        visibleGravitationalObject = new JCheckBox("Visible Gravitational Object", false);
        unlimitedLives = new JCheckBox("Unlimited Lives", false);

        numberOfAsteroidsLabel = new JLabel("Number of Asteroids per level");
        numberOfAsteroidsBox = new JTextField("10");

        startingLevelLabel = new JLabel("Starting Level");
        startingLevelBox = new JTextField("1");

        resetHighScore = new JButton("Reset High Score");
        restoreSavedGame = new JButton("Restore Saved Game");

        //checkbox.setFocusable(false);
        setLayout(null);

        add(gravitationalObject);
        add(visibleGravitationalObject);
        add(unlimitedLives);

        add(numberOfAsteroidsLabel);
        add(numberOfAsteroidsBox);

        add(startingLevelLabel);
        add(startingLevelBox);

        add(resetHighScore);
        add(restoreSavedGame);

        int initialOffset = 0;
        int labelOffset = 32;
        int deltaY = 21;
        int height = 20;
        int textBoxWidth = 30;
        int labelWidth = windowWidth - textBoxWidth;

        gravitationalObject.setBounds(initialOffset, 0, windowWidth, height);
        visibleGravitationalObject.setBounds(initialOffset, gravitationalObject.getY() + deltaY, windowWidth, height);
        unlimitedLives.setBounds(initialOffset, visibleGravitationalObject.getY() + deltaY, windowWidth, height);

        numberOfAsteroidsBox.setBounds(initialOffset, unlimitedLives.getY() + deltaY, textBoxWidth, height);
        numberOfAsteroidsLabel.setBounds(initialOffset + labelOffset, numberOfAsteroidsBox.getY(), labelWidth, height);

        startingLevelBox.setBounds(initialOffset, numberOfAsteroidsLabel.getY() + deltaY, textBoxWidth, height);
        startingLevelLabel.setBounds(initialOffset + labelOffset, startingLevelBox.getY(), labelWidth, height);
        
        resetHighScore.setBounds(initialOffset, startingLevelLabel.getY() + deltaY, windowWidth, height);
        restoreSavedGame.setBounds(initialOffset, resetHighScore.getY() + deltaY, windowWidth, height);
    }

    public OptionsContainer getOptions()
    {
        OptionsContainer options = new OptionsContainer();

        options.gravitationalObject = gravitationalObject.isSelected();
        options.visibleGravitationalObject = visibleGravitationalObject.isSelected();
        options.unlimitedLives = unlimitedLives.isSelected();
        options.numberOfAsteroidsPerLevel = Integer.parseInt(numberOfAsteroidsBox.getText());
        options.startingLevel = Integer.parseInt(startingLevelBox.getText());

        return options;
    }
}
