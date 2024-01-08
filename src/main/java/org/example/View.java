package org.example;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Stack;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author leo
 */
public class View extends JFrame implements KeyListener {

  private static final int WIDTH = 1800;
  private static final int HEIGHT = 1000;
  private static final int TILE_SIZE = 5;
  private static final int ROWS = 300;
  private static final int COLS = 200;

  private boolean[][] visited;

  public View() {
    setTitle("Maze Generator");
    setSize(WIDTH, HEIGHT);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(true);
    addKeyListener(this);
    setFocusable(true);

    // Initialize maze
    visited = new boolean[ROWS][COLS];

    setVisible(true);
  }

  private void generateMaze() {
    // Reset visited array
    for (int i = 0; i < ROWS; i++) {
      for (int j = 0; j < COLS; j++) {
        visited[i][j] = false;
      }
    }

    Stack<Point> stack = new Stack<>();
    Point start = new Point(0, 0);
    stack.push(start);
    visited[start.x][start.y] = true;

    while (!stack.isEmpty()) {
      Point current = stack.pop();
      int x = current.x;
      int y = current.y;

      int[] dx = {0, 0, 1, -1};
      int[] dy = {1, -1, 0, 0};

      int[] direction = {0, 1, 2, 3};
      shuffleArray(direction);

      for (int d : direction) {
        int nx = x + dx[d] * 2;
        int ny = y + dy[d] * 2;

        if (nx >= 0 && nx < ROWS && ny >= 0 && ny < COLS && !visited[nx][ny]) {
          visited[nx][ny] = true;
          visited[x + dx[d]][y + dy[d]] = true;

          stack.push(new Point(nx, ny));
        }
      }
    }
  }

  private void shuffleArray(int[] array) {
    for (int i = array.length - 1; i > 0; i--) {
      int index = (int) (Math.random() * (i + 1));
      int temp = array[index];
      array[index] = array[i];
      array[i] = temp;
    }
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);

    for (int i = 0; i < ROWS; i++) {
      for (int j = 0; j < COLS; j++) {
        if (visited[i][j]) {
          g.setColor(Color.WHITE);
        } else {
          g.setColor(Color.BLACK);
        }
        g.fillRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        g.setColor(Color.BLACK);
        g.drawRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
      }
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new View());
  }

  @Override
  public void keyTyped(KeyEvent e) {
    System.out.println("typed");
  }

  @Override
  public void keyPressed(KeyEvent e) {
    System.out.println("pressed");
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
      generateMaze();
      repaint();
    }
    System.out.println("released");
  }
}
