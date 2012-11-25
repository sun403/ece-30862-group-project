import javax.swing.JFrame;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.*;

public class OptionsDialog extends JFrame
{
    private static final String RESET_HIGH_SCORE_BUTTON_TEXT = "Reset High Scores";
    private static final String RESTORE_SAVED_GAME_BUTTON_TEXT = "Restore Saved Game";
    private static final int windowWidth = 250;
    private static final int windowHeight = 147;

    private JCheckBox gravitationalObject;
    private JCheckBox visibleGravitationalObject;
    private JCheckBox unlimitedLives;

    private JLabel numberOfAsteroidsLabel;
    private JTextField numberOfAsteroidsBox;

    private JLabel startingLevelLabel;
    private JTextField startingLevelBox;

    private JButton resetHighScores;
    private JButton restoreSavedGame;

    private OptionsContainer returnOptions;

    private class ButtonListener implements ActionListener
    {
        private OptionsDialog parent;

        public ButtonListener(OptionsDialog parent) {
            this.parent = parent;
        }

        public void actionPerformed(ActionEvent e)
        {
            String callingButton = ((JButton)e.getSource()).getText();

            if(callingButton.equals(RESTORE_SAVED_GAME_BUTTON_TEXT))
            {
                JFileChooser saveFileChooser = new JFileChooser();
                saveFileChooser.setDialogTitle("Open Game");
                saveFileChooser.showSaveDialog(parent);
                returnOptions.saveFile = saveFileChooser.getSelectedFile();

                if(parent != null) {
                    parent.dispose();
                }
            }
            else if(callingButton.equals(RESET_HIGH_SCORE_BUTTON_TEXT))
            {
                returnOptions.resetHighScores = true;

                if(parent != null) {
                    parent.dispose();
                }
            }
        }
    }

    public OptionsDialog(OptionsContainer currentOptions)
    {
        returnOptions = new OptionsContainer();
        
        setTitle("Options");
        setSize(windowWidth, windowHeight);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        gravitationalObject = new JCheckBox(
                "Gravitational Object", currentOptions.gravitationalObject);
        visibleGravitationalObject = new JCheckBox(
                "Visible Gravitational Object", currentOptions.visibleGravitationalObject);
        unlimitedLives = new JCheckBox(
                "Unlimited Lives", currentOptions.unlimitedLives);

        numberOfAsteroidsLabel = new JLabel("Number of Asteroids per level");
        numberOfAsteroidsBox = new JTextField(
                Integer.toString(currentOptions.numberOfAsteroidsPerLevel));

        startingLevelLabel = new JLabel("Starting Level");
        startingLevelBox = new JTextField(
                Integer.toString(currentOptions.startingLevel));

        resetHighScores = new JButton(RESET_HIGH_SCORE_BUTTON_TEXT);
        restoreSavedGame = new JButton(RESTORE_SAVED_GAME_BUTTON_TEXT);

        resetHighScores.addActionListener(new ButtonListener(this));
        restoreSavedGame.addActionListener(new ButtonListener(this));

        //checkbox.setFocusable(false);
        setLayout(null);

        add(gravitationalObject);
        add(visibleGravitationalObject);
        add(unlimitedLives);

        add(numberOfAsteroidsLabel);
        add(numberOfAsteroidsBox);

        add(startingLevelLabel);
        add(startingLevelBox);

        add(resetHighScores);
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
        
        resetHighScores.setBounds(initialOffset, startingLevelLabel.getY() + deltaY, windowWidth, height);
        restoreSavedGame.setBounds(initialOffset, resetHighScores.getY() + deltaY, windowWidth, height);
    }

    public OptionsContainer getOptions()
    {
        returnOptions.gravitationalObject = gravitationalObject.isSelected();
        returnOptions.visibleGravitationalObject = visibleGravitationalObject.isSelected();
        returnOptions.unlimitedLives = unlimitedLives.isSelected();
        returnOptions.numberOfAsteroidsPerLevel = Integer.parseInt(numberOfAsteroidsBox.getText());
        returnOptions.startingLevel = Integer.parseInt(startingLevelBox.getText());

        return returnOptions;
    }
}
