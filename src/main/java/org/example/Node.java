package org.example;

class Node implements Comparable<Node> {
    int x, y; // Coordinates of the node
    int cost; // Cost to reach this node
    int heuristic; // Heuristic value (estimated cost to goal)
    Node parent; // Parent node for reconstructing the path

    public Node(int x, int y, int cost, int heuristic, Node parent) {
        this.x = x;
        this.y = y;
        this.cost = cost;
        this.heuristic = heuristic;
        this.parent = parent;
    }

    @Override
    public int compareTo(Node other) {
        // Compare nodes based on the total cost (cost + heuristic)
        return Integer.compare(this.cost + this.heuristic, other.cost + other.heuristic);
    }
}
