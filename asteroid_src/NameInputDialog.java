import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.*;
import java.io.File;

public class NameInputDialog extends JFrame
{
    private static final String WINDOW_TITLE = "Enter your initials";
    private static final String CONTINUE_BUTTON_TEXT = "Continue";
    private static final int WINDOW_WIDTH = 200;
    private static final int WINDOW_HEIGHT = 70;

    private String inputtedString;
    private JButton continueButton;
    private JTextField nameBox;

    private class ButtonListener implements ActionListener
    {
        private NameInputDialog parent;

        public ButtonListener(NameInputDialog parent) {
            this.parent = parent;
        }

        public void actionPerformed(ActionEvent e)
        {
            String callingButton = ((JButton)e.getSource()).getText();

            if(callingButton.equals(CONTINUE_BUTTON_TEXT))
            {
                if(parent != null)
                {
                    inputtedString = nameBox.getText();

                    //Initials are only allowed to be 4 characters or less
                    if(inputtedString.length() > 5) {
                        inputtedString = inputtedString.substring(0, 5);
                    }
                    parent.dispose();
                }
            }
        }
    }

    public NameInputDialog()
    {
        setTitle(WINDOW_TITLE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        continueButton = new JButton(CONTINUE_BUTTON_TEXT);
        nameBox = new JTextField();

        continueButton.addActionListener(new ButtonListener(this));

        setLayout(null);

        add(continueButton);
        add(nameBox);

        nameBox.setBounds(0, 0, WINDOW_WIDTH, 20);
        continueButton.setBounds(0, 20, WINDOW_WIDTH, 20);
    }

    public void dispose()
    {
        super.dispose();
        synchronized(this) {
            this.notify();
        }
    }

    public String getName() {
        return inputtedString;
    }
}
