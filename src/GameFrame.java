import javax.swing.JFrame;

public class GameFrame extends JFrame {
    GameFrame(boolean withEnemy) {
        this.add(new GamePanel(withEnemy));
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}