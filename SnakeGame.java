import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

class SnakeGame extends JFrame {
    private static final int GRID_SIZE = 20;
    private static final int CELL_SIZE = 20;

    private ArrayList<Point> snake;
    private Point food;
    private char direction;
    private boolean isGameOver;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e.getKeyCode());
            }
        });

        initializeGame();
        Timer timer = new Timer(150, new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (!isGameOver) {
                    move();
                    checkCollision();
                    repaint();
                }
            }
        });
        timer.start();
    }

    private void initializeGame() {
        snake = new ArrayList<>();
        snake.add(new Point(GRID_SIZE / 2, GRID_SIZE / 2));
        direction = 'R';
        spawnFood();
        isGameOver = false;
    }

    private void spawnFood() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(GRID_SIZE);
            y = rand.nextInt(GRID_SIZE);
        } while (snake.contains(new Point(x, y)));
        food = new Point(x, y);
    }

    private void move() {
        Point head = snake.get(0);
        Point newHead = null;

        switch (direction) {
            case 'U':
                newHead = new Point(head.x, (head.y - 1 + GRID_SIZE) % GRID_SIZE);
                break;
            case 'D':
                newHead = new Point(head.x, (head.y + 1) % GRID_SIZE);
                break;
            case 'L':
                newHead = new Point((head.x - 1 + GRID_SIZE) % GRID_SIZE, head.y);
                break;
            case 'R':
                newHead = new Point((head.x + 1) % GRID_SIZE, head.y);
                break;
        }

        snake.add(0, newHead);

        if (newHead.equals(food)) {
            spawnFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    private void checkCollision() {
        Point head = snake.get(0);
        if (snake.lastIndexOf(head) > 0) {
            isGameOver = true;
        }
    }

    private void handleKeyPress(int keyCode) {
        switch (keyCode) {
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
        }
    }

    public void paint(Graphics g) {
        super.paint(g);

        if (isGameOver) {
            gameOver(g);
        } else {
            drawSnake(g);
            drawFood(g);
        }
    }

    private void drawSnake(Graphics g) {
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }

    private void drawFood(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(food.x * CELL_SIZE, food.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    private void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Game Over!", GRID_SIZE * CELL_SIZE / 2 - 80, GRID_SIZE * CELL_SIZE / 2 - 10);
        g.drawString("Press R to restart", GRID_SIZE * CELL_SIZE / 2 - 100, GRID_SIZE * CELL_SIZE / 2 + 20);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SnakeGame().setVisible(true);
        });
    }
}
