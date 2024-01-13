package org.example;

import java.util.*;

public class AStarAlgorithm {

    private static final int[] DX = {0, 1, 0, -1};
    private static final int[] DY = {-1, 0, 1, 0};

    private static List<Node> checkedCells;

    public static List<Node> findPath(int[][] grid, int startX, int startY, int goalX, int goalY) {
        checkedCells = new ArrayList<>();
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

                if (nextX >= 0 && nextX < grid.length && nextY >= 0 && nextY < grid[0].length) {
                    if (grid[nextX][nextY] != 1) {
                        int newCost = current.cost + 1;

                        Node neighbor = new Node(nextX, nextY, newCost, heuristic(nextX, nextY, goalX, goalY), current);

                        if (!closedSet.contains(neighbor) && !containsNodeWithEqualOrLowerCost(openSet, neighbor)) {
                            openSet.add(neighbor);
                            checkCell(neighbor);
                        }
                    }
                }
            }
        }

        return Collections.emptyList();
    }

    private static void checkCell(Node node) {
        if (!containsCell(checkedCells, node.x, node.y)) {
            checkedCells.add(node);
        }
    }

    private static boolean containsCell(List<Node> nodeList, int x, int y) {
        return nodeList.stream().anyMatch(node -> node.x == x && node.y == y);
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

    public static List<Node> getCheckedCells() {
        return checkedCells;
    }
}
