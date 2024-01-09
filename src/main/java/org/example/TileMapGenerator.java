package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Random;

public class TileMapGenerator extends JFrame {

  private static final int TILE_SIZE = 15; // Size of each tile
  private static final int ROWS = 50;
  private static final int COLS = 75;

  private int[][] tileMap;
  private Point pointA, pointB;
  private List<Node> path;

  public TileMapGenerator() {
    setTitle("Tile Map Generator");
    setSize(COLS * TILE_SIZE + 20, ROWS * TILE_SIZE + 40);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    generateTileMap();

    JPanel panel = getPanel();

    panel.setFocusable(true);

    getContentPane().add(panel);
  }

  private JPanel getPanel() {
    JPanel panel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawTileMap(g);
        drawPoints(g);
        drawPath(g);
      }
    };

    panel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int row = e.getY() / TILE_SIZE;
        int col = e.getX() / TILE_SIZE;

        if (pointA == null) {
          pointA = new Point(col, row);
        } else if (pointB == null) {
          pointB = new Point(col, row);
          calculatePath();
        } else {
          pointA = new Point(col, row);
          pointB = null;
          path = null;
        }

        panel.repaint(); // Redraw the panel
      }
    });
    return panel;
  }

  private void generateTileMap() {
    tileMap = new int[COLS][ROWS];
    Random random = new Random();

    for (int i = 0; i < COLS; i++) {
      for (int j = 0; j < ROWS; j++) {
        if (random.nextFloat() > 0.7) {
          tileMap[i][j] = 1;
        } else {
          tileMap[i][j] = 0;
        }

      }
    }
  }

  private void drawTileMap(Graphics g) {
    for (int i = 0; i < COLS; i++) {
      for (int j = 0; j < ROWS; j++) {
        int x = i * TILE_SIZE;
        int y = j * TILE_SIZE;

        if (tileMap[i][j] == 0) {
          // Walkable tile (white)
          g.setColor(Color.WHITE);
        } else if (tileMap[i][j] == 1) {
          // Unwalkable tile (gray)
          g.setColor(Color.GRAY);
        } else {
          // Checked tile (yellow)
          g.setColor(Color.ORANGE);
        }

        g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, TILE_SIZE, TILE_SIZE);
      }
    }
  }

  private void drawPoints(Graphics g) {
    if (pointA != null) {
      g.setColor(Color.BLUE);
      g.fillRect(pointA.x * TILE_SIZE, pointA.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    if (pointB != null) {
      g.setColor(Color.RED);
      g.fillRect(pointB.x * TILE_SIZE, pointB.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
  }

  private void drawPath(Graphics g) {
    if (path != null) {
      g.setColor(Color.GREEN);
      for (Node node : path) {
        int x = node.x * TILE_SIZE;
        int y = node.y * TILE_SIZE;
        g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, TILE_SIZE, TILE_SIZE);
      }
    }
  }

  // Modify the calculatePath method
  private void calculatePath() {
    new Thread(() -> {
      // Using the A* algorithm to calculate the shortest path
      List<Node> fullPath = AStarAlgorithm.findPath(tileMap, pointA.x, pointA.y, pointB.x,
          pointB.y);

      // Retrieve checked cells from the A* algorithm
      List<Node> checkedCells = AStarAlgorithm.getCheckedCells();

      // Show checked cells iteratively
      for (Node checkedCell : checkedCells) {
        tileMap[checkedCell.x][checkedCell.y] = 2; // Mark as checked during the algorithm

        // Trigger panel repaint on the EDT
        SwingUtilities.invokeLater(() -> getContentPane().getComponent(0).repaint());

        try {
          Thread.sleep(5);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

      // Show the shortest path in black
      path = fullPath;

      // Trigger panel repaint on the EDT
      SwingUtilities.invokeLater(() -> getContentPane().getComponent(0).repaint());

      // Reset marked cells after the process
      for (Node checkedCell : checkedCells) {
        tileMap[checkedCell.x][checkedCell.y] = 0; // Reset checked cells to walkable
      }

    }).start();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      TileMapGenerator tileMapGenerator = new TileMapGenerator();
      tileMapGenerator.setVisible(true);
    });
  }
}