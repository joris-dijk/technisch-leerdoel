package org.example;

import java.util.*;

public class AStarAlgorithm {

    private static final int[] DX = {0, 1, 0, -1};
    private static final int[] DY = {-1, 0, 1, 0};

    public static List<Node> findPath(int[][] grid, int startX, int startY, int goalX, int goalY) {
        int rows = grid.length;
        int cols = grid[0].length;

        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(node -> node.cost + node.heuristic));
        Set<Node> closedSet = new HashSet<>();

        Node startNode = new Node(startX, startY, 0, heuristic(startX, startY, goalX, goalY), null);
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.x == goalX && current.y == goalY) {
                return reconstructPath(current);
            }

            closedSet.add(current);

            for (int i = 0; i < 4; i++) {
                int nextX = current.x + DX[i];
                int nextY = current.y + DY[i];

                if (nextX >= 0 && nextX < rows && nextY >= 0 && nextY < cols) {
                    if (grid[nextX][nextY] != 1) {
                        int newCost = current.cost + 1; // Assuming each step has a cost of 1

                        Node neighbor = new Node(nextX, nextY, newCost, heuristic(nextX, nextY, goalX, goalY), current);

                        if (!closedSet.contains(neighbor) && !containsNodeWithEqualOrLowerCost(openSet, neighbor)) {
                            openSet.add(neighbor);
                        }
                    }
                }
            }
        }

        return Collections.emptyList();
    }

    private static int heuristic(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private static List<Node> reconstructPath(Node goalNode) {
        List<Node> path = new ArrayList<>();
        Node current = goalNode;

        while (current != null) {
            path.add(current);
            current = current.parent;
        }

        Collections.reverse(path);
        return path;
    }

    private static boolean containsNodeWithEqualOrLowerCost(PriorityQueue<Node> openSet, Node node) {
        return openSet.stream().anyMatch(n -> n.x == node.x && n.y == node.y && n.cost <= node.cost);
    }
}
