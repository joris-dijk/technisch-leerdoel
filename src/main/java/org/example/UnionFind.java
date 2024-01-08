package org.example;

public class UnionFind {
  private int[] parent;
  private int[] rank;

  UnionFind(int size) {
    parent = new int[size];
    rank = new int[size];
    for (int i = 0; i < size; i++) {
      parent[i] = i;
      rank[i] = 0;
    }
  }

  int find(int x) {
    if (parent[x] != x) {
      parent[x] = find(parent[x]);
    }
    return parent[x];
  }

  void union(int x, int y) {
    int xRoot = find(x), yRoot = find(y);
    if (xRoot == yRoot)
      return;
    if (rank[xRoot] < rank[yRoot]) {
      parent[xRoot] = yRoot;
    } else if (rank[xRoot] > rank[yRoot]) {
      parent[yRoot] = xRoot;
    } else {
      parent[yRoot] = xRoot;
      rank[xRoot]++;
    }
  }
}
