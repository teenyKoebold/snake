import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ModeSelectFrame extends JFrame {
    private JButton withEnemyButton;
    private JButton withoutEnemyButton;
    private GameFrame gameFrame;
    Image background;

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;

    public ModeSelectFrame() {
        setTitle("Select Game Mode");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        background = new ImageIcon(getClass().getResource("/images/background.png")).getImage();
        setContentPane(new BackgroundPanel(background));

        setLayout(new GridBagLayout());

        withoutEnemyButton = new JButton("Easy");
        withEnemyButton = new JButton("Hard");

        withoutEnemyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame = new GameFrame(false);
                dispose();
            }
        });

        withEnemyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame = new GameFrame(true);
                dispose();
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        gbc.gridy = 0;
        add(withoutEnemyButton, gbc);

        gbc.gridy = 1;
        add(withEnemyButton, gbc);

        setVisible(true);
    }

    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(Image image) {
            this.backgroundImage = image;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
            draw(g);
        }

        public void draw(Graphics g){
            g.setColor(Color.yellow);
            g.setFont(new Font("Ink Free", Font.BOLD, 75));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("Snake", (SCREEN_WIDTH - metrics2.stringWidth("Snake")) / 2, 200);
        }
    }
}