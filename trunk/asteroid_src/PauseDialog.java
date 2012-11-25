import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.*;
import java.io.File;

public class PauseDialog extends JFrame
{
    private static final String WINDOW_TITLE = "Game Paused";
    private static final String CONTINUE_BUTTON_TEXT = "Continue Game";
    private static final String OPTIONS_BUTTON_TEXT = "Options";
    private static final String SAVE_BUTTON_TEXT = "Save Game";
    private static final String QUIT_BUTTON_TEXT = "Quit Game";
    private static final int WINDOW_WIDTH = 200;
    private static final int WINDOW_HEIGHT = 114;

    private JButton continueButton;
    private JButton optionsButton;
    private JButton saveButton;
    private JButton quitButton;

    private OptionsDialog optionsWindow;
    private OptionsContainer gameOptions;

    private int returnCode = -1;
    private File saveFile;

    private class CloseListener implements WindowListener
    {
        public void windowClosed(WindowEvent e) {
            gameOptions = optionsWindow.getOptions();
        }

        public void windowClosing(WindowEvent e) {}
        public void windowActivated(WindowEvent e) {}
        public void windowDeactivated(WindowEvent e) {}
        public void windowDeiconified(WindowEvent e) {}
        public void windowIconified(WindowEvent e) {}
        public void windowOpened(WindowEvent e) {}
    }

    private class ButtonListener implements ActionListener
    {
        private PauseDialog parent;

        public ButtonListener(PauseDialog parent) {
            this.parent = parent;
        }

        public void actionPerformed(ActionEvent e)
        {
            String callingButton = ((JButton)e.getSource()).getText();

            if(callingButton.equals(CONTINUE_BUTTON_TEXT))
            {
                if(parent != null)
                {
                    //If the return code has already been set by the options button, don't mess with it
                    if(parent.getReturnCode() == -1) {
                        parent.setReturnCode(Constants.CONTINUE_GAME);
                    }

                    parent.dispose();
                }
            }
            else if(callingButton.equals(SAVE_BUTTON_TEXT))
            {
                //nigger style
                //saveFileChooser.setVisible(true);
                JFileChooser saveFileChooser = new JFileChooser();
                saveFileChooser.setDialogTitle("Save Game");
                saveFileChooser.showSaveDialog(parent);

                saveFile = saveFileChooser.getSelectedFile();

                if(parent != null)
                {
                    parent.setReturnCode(Constants.SAVE_GAME);
                    parent.dispose();
                }
            }
            else if(callingButton.equals(QUIT_BUTTON_TEXT))
            {
                if(parent != null) 
                {
                    parent.setReturnCode(Constants.QUIT_GAME);
                    parent.dispose();
                }
            }
            else if(callingButton.equals(OPTIONS_BUTTON_TEXT))
            {
                optionsWindow.setVisible(true);

                if(parent != null) {
                    parent.setReturnCode(Constants.CONTINUE_GAME_WITH_NEW_OPTIONS);
                }
            }
            else {
                System.out.println("this called me?");
                System.out.println(callingButton);
            }
        }
    }

    public PauseDialog(OptionsContainer currentOptions)
    {
        optionsWindow = new OptionsDialog(currentOptions);
        optionsWindow.addWindowListener(new CloseListener());

        setTitle(WINDOW_TITLE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        continueButton = new JButton(CONTINUE_BUTTON_TEXT);
        optionsButton = new JButton(OPTIONS_BUTTON_TEXT);
        saveButton = new JButton(SAVE_BUTTON_TEXT);
        quitButton = new JButton(QUIT_BUTTON_TEXT);

        continueButton.addActionListener(new ButtonListener(this));
        optionsButton.addActionListener(new ButtonListener(this));
        saveButton.addActionListener(new ButtonListener(this));
        quitButton.addActionListener(new ButtonListener(this));

        setLayout(null);

        add(continueButton);
        add(optionsButton);
        add(saveButton);
        add(quitButton);

        int initialOffset = 0;
        int deltaY = 21;
        int height = 20;

        continueButton.setBounds(initialOffset, 0, WINDOW_WIDTH, height);
        optionsButton.setBounds(initialOffset, continueButton.getY() + deltaY, WINDOW_WIDTH, height);
        saveButton.setBounds(initialOffset, optionsButton.getY() + deltaY, WINDOW_WIDTH, height);
        quitButton.setBounds(initialOffset, saveButton.getY() + deltaY, WINDOW_WIDTH, height);
    }

    public OptionsContainer getOptions() {
        return gameOptions;
    }

    public void dispose()
    {
        super.dispose();
        synchronized(this) {
            this.notify();
        }
    }

    public void setReturnCode(int newReturnCode) {
        returnCode = newReturnCode;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public File getSaveFile() {
        return saveFile;
    }
}
