import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    static final int DELAY = 75;

    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 3;
    int miceEaten;
    char direction = 'R';

    final int enemyX[] = new int[GAME_UNITS];
    final int enemyY[] = new int[GAME_UNITS];
    int enemyParts = 3;
    char enemyDir = 'L';
    boolean enemyAlive = true;

    int mouseX;
    int mouseY;
    
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newMouse();
        running = true;
        enemyAlive = true;
        enemyX[0] = SCREEN_WIDTH - UNIT_SIZE;
        enemyY[0] = 0;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void respawn(){
        direction = 'R';
        bodyParts = 3;
        miceEaten = 0;

        for(int i = 0; i < GAME_UNITS; i++){
            x[i] = 0;
            y[i] = 0;
        }

        respawnEnemy();
        startGame();
    }

    public void respawnEnemy() {
        enemyDir = 'L';
        enemyParts = 3;
        enemyAlive = true;

        for (int i = 0; i < GAME_UNITS; i++) {
            enemyX[i] = 0;
            enemyY[i] = 0;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.yellow);
            g.fillRect(mouseX, mouseY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(181, 230, 29));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            if(enemyAlive){
                for (int i = 0; i < enemyParts; i++) {
                    if (i == 0) {
                        g.setColor(Color.yellow);
                        g.fillRect(enemyX[i], enemyY[i], UNIT_SIZE, UNIT_SIZE);
                    } else {
                        g.setColor(new Color(255, 201, 14));
                        g.fillRect(enemyX[i], enemyY[i], UNIT_SIZE, UNIT_SIZE);
                    }
                }
            }
        
            g.setColor(Color.yellow);
            g.setFont(new Font("Comic Sans", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + miceEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + miceEaten)) / 2,
                    g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    public void newMouse() {
        boolean validPos = false;

        while(!validPos){
            mouseX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
            mouseY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

            validPos = true;

            for(int i = 0; i < GAME_UNITS; i++){
                if(x[i] == mouseX && y[i] == mouseY){
                    validPos = false;
                    break;
                }
            }
        }
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void enemyMove() {
        for (int i = enemyParts; i > 0; i--) {
            enemyX[i] = enemyX[i - 1];
            enemyY[i] = enemyY[i - 1];
        }

        chooseDirection();

        switch (enemyDir) {
            case 'U':
                enemyY[0] = enemyY[0] - UNIT_SIZE;
                break;
            case 'D':
                enemyY[0] = enemyY[0] + UNIT_SIZE;
                break;
            case 'L':
                enemyX[0] = enemyX[0] - UNIT_SIZE;
                break;
            case 'R':
                enemyX[0] = enemyX[0] + UNIT_SIZE;
                break;
        }
    }

    public boolean isDirectionSafe(int x, int y) {
        for (int i = 1; i < enemyParts; i++) {
            if (enemyX[i] == x && enemyY[i] == y) {
                return false;
            }
        }

        for (int i = 0; i < bodyParts; i++) {
            if (this.x[i] == x && this.y[i] == y) {
                return false;
            }
        }

        if (x < 0 || x >= SCREEN_WIDTH || y < 0 || y >= SCREEN_HEIGHT) {
            return false;
        }

        return true;
    }

    public void chooseDirection() {
        char[] directions = { 'U', 'D', 'L', 'R' };
        int bestDistance = Integer.MAX_VALUE;
        char bestDirection = enemyDir;

        for (char dir : directions) {
            int newX = enemyX[0];
            int newY = enemyY[0];

            switch (dir) {
                case 'U':
                    newY -= UNIT_SIZE;
                    break;
                case 'D':
                    newY += UNIT_SIZE;
                    break;
                case 'L':
                    newX -= UNIT_SIZE;
                    break;
                case 'R':
                    newX += UNIT_SIZE;
                    break;
            }

            if (isDirectionSafe(newX, newY)) {
                int dist = Math.abs(mouseX - newX) + Math.abs(mouseY - newY);
                if (dist < bestDistance) {
                    bestDistance = dist;
                    bestDirection = dir;
                }
            }
        }

        enemyDir = bestDirection;
    }

    public void checkMouse() {
        if ((x[0] == mouseX) && (y[0] == mouseY)) {
            bodyParts++;
            miceEaten++;
            newMouse();
        }else if ((enemyX[0] == mouseX) && (enemyY[0] == mouseY)) {
            enemyParts++;
            newMouse();
        }
    }

    public void checkCollisions() {
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == enemyX[i] && y[0] == enemyY[i]) {
                running = false;
            }
        }
       
        if (x[0] < 0) {
            running = false;
        }
       
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
       
        if (y[0] < 0) {
            running = false;
        }
        
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void enemyCheckCollisions() {
        for (int i = enemyParts; i > 0; i--) {
            if ((enemyX[0] == enemyX[i]) && (enemyY[0] == enemyY[i])) {
                enemyAlive = false;
            }
        }

        for (int i = bodyParts; i > 0; i--) {
            if (enemyX[0] == x[i] && enemyY[0] == y[i]) {
                enemyAlive = false;
            }
        }

        if (enemyX[0] < 0) {
            enemyAlive = false;
        }

        if (enemyX[0] > SCREEN_WIDTH) {
            enemyAlive = false;
        }

        if (enemyY[0] < 0) {
            enemyAlive = false;
        }

        if (enemyY[0] > SCREEN_HEIGHT) {
            enemyAlive = false;
        }

        if (!enemyAlive) {
            respawnEnemy();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.yellow);
        g.setFont(new Font("Comic Sans", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + miceEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + miceEaten)) / 2,
                g.getFont().getSize());

        g.setColor(Color.yellow);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            enemyMove();
            checkMouse();
            checkCollisions();
            enemyCheckCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if(!running){
                        respawn();
                    }
                    break;
            }
        }
    }
}