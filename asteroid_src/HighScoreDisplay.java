import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.event.*;

public class HighScoreDisplay extends JFrame
{
    private static final String WINDOW_TITLE = "High Scores";
    private static final int WINDOW_WIDTH = 200;
    private static final int WINDOW_HEIGHT = 250;

    private JLabel[][] labels;

    public HighScoreDisplay(HighScore[] highScores)
    {
        setTitle(WINDOW_TITLE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        labels = new JLabel[2][10];
        setLayout(null);

        for(int i = 0; i < 10; i++)
        {

            if(highScores[i].getScore() > 0 && 
               highScores[i].getName() != "" &&
               highScores[i].getName() != null) 
            {
                labels[0][i] = new JLabel(highScores[i].getName());
                labels[1][i] = new JLabel(Integer.toString(highScores[i].getScore()));
            }
            else {
                labels[0][i] = new JLabel("");
                labels[1][i] = new JLabel("");
            }

            labels[0][i].setBounds(10, i * 20, WINDOW_WIDTH / 2, 20);
            labels[1][i].setBounds(WINDOW_WIDTH / 2, i * 20, WINDOW_WIDTH / 2, 20);

            add(labels[0][i]);
            add(labels[1][i]);
        }
    }

    public void dispose()
    {
        super.dispose();
        synchronized(this) {
            this.notify();
        }
    }
}
