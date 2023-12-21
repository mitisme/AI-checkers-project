package edu.iastate.cs472.proj2;

import java.util.ArrayList;
import java.util.List;

public class MCNode {
    private CheckersData state;     // The state of the board at this node
    private MCNode parent;          // The parent node in the MCTS tree
    private List<MCNode> children;  // Children of this node
    private int visitCount;         // Number of times this node has been visited
    private double winScore;        // Cumulative score of this node from simulations
    private CheckersMove lastMove;  // Last move made to reach this node
    private int player;             // The player who made the last move

    /**
    * Node type for the Monte Carlo search tree.
    */
    //constructor
    public MCNode(CheckersData state, CheckersMove lastMove, int player) {
        this.state = state;
        this.lastMove = lastMove;
        this.player = player;
        this.children = new ArrayList<>();
        this.visitCount = 0;
        this.winScore = 0;
    }

    //////////// Getters//////////////////
    public CheckersData getState() {
        return state;
    }

    public MCNode getParent() {
        return parent;
    }

    public List<MCNode> getChildren() {
        return children;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public double getWinScore() {
        return winScore;
    }

    public CheckersMove getLastMove() {
        return lastMove;
    }

    public int getPlayer() {
        return player;
    }

    ///////////////// Setters //////////////////
    public void setState(CheckersData state) {
        this.state = state;
    }

    public void setParent(MCNode parent) {
        this.parent = parent;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public void setWinScore(double winScore) {
        this.winScore = winScore;
    }

    public void setLastMove(CheckersMove lastMove) {
        this.lastMove = lastMove;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public void addChild(MCNode child) {
      this.children.add(child);
      child.parent = this;
  }
  public void addScore(double score) {
    this.winScore += score;
 } 

  // addtiontal helper methods that I found useful to implement
  // Method to increment the visit count of this node
  public void incrementVisit() {
    this.visitCount++;
  }

  //used in the MCTS class to find best scoring child node
  public MCNode getChildWithMaxScore() {
    if (this.children.isEmpty()) {
        return null; // No children to select from
    }
    
    MCNode bestChild = null;
    double maxScore = Double.NEGATIVE_INFINITY;
    
    for (MCNode child : this.children) {
        if (child.winScore > maxScore) {
            bestChild = child;
            maxScore = child.winScore;
        }
    }
    
    return bestChild;
}


  
}