package edu.iastate.cs472.proj2;


public class MCTree {
    private MCNode root;
    private int size;
//Constructor for tree
    public MCTree() {
        this.root = null;
        this.size = 0;
    }
//Constructor for tree
    public MCTree(MCNode root) {
        this.root = root;
        this.size = 1; // Starts with the root
    }

    // Getter and setter for the root
    public MCNode getRoot() {
        return root;
    }

    public void setRoot(MCNode root) {
        this.root = root;
        this.size = (root == null) ? 0 : 1;
    }

    // Method to increment size of the tree
    public void incrementSize() {
        size++;
    }

    // Getter for the size
    public int getSize() {
        return size;
    }
}
