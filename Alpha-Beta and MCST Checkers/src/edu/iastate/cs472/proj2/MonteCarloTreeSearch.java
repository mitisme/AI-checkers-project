package edu.iastate.cs472.proj2;
import java.util.Random;

/**
 * 
 * @author Michael Tretter
 *
 */

/**
 * This class implements the Monte Carlo tree search method to find the best
 * move at the current state.
 */
public class MonteCarloTreeSearch extends AdversarialSearch {

     //initialization of global variables
    private static final double C = Math.sqrt(2);               // Constant for UCB1
    //private static final double C = Math.sqrt(4);               // test Constant for UCB1
    private static final int BLACK_PLAYER = CheckersData.BLACK;   // BLACK is the AI agent
    private static final int SIMULATION_DEPTH = 100;              // Max depth for simulation
    private Random random = new Random();

    // Constructor
    public MonteCarloTreeSearch() {
    }

    /**
     * The input parameter legalMoves contains all the possible moves.
     * It contains four integers:  fromRow, fromCol, toRow, toCol
     * which represents a move from (fromRow, fromCol) to (toRow, toCol).
     * It also provides a utility method `isJump` to see whether this
     * move is a jump or a simple move.
     *
     * Each legalMove in the input now contains a single move
     * or a sequence of jumps: (rows[0], cols[0]) -> (rows[1], cols[1]) ->
     * (rows[2], cols[2]).
     *
     * @param legalMoves All the legal moves for the agent at current step.
     */
    // I set a 1 second timer for this method as it will go through many many iterations and can time alot of time.
    // 1 second seems to be a sweet spot for performance and speed
    @Override
    public CheckersMove makeMove(CheckersMove[] legalMoves) {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + 1000; // Run for 1 second

        MCNode rootNode = new MCNode(board, null, BLACK_PLAYER);

        while (System.currentTimeMillis() < endTime) {
            // Perform MCTS phases
            MCNode nodeToExplore = selectNode(rootNode);
            expandNode(nodeToExplore);
            double score = simulateRandomPlayout(nodeToExplore);
            backPropagate(nodeToExplore, score);
        }
        System.out.println(board);
        System.out.println();
        return rootNode.getChildWithMaxScore().getLastMove();
    }
    //Calles the findBestNodeWithUCB1() method to choose the best node
    private MCNode selectNode(MCNode rootNode) {
        MCNode node = rootNode;
        while (!node.getChildren().isEmpty()) {
            node = findBestNodeWithUCB1(node);
        }
        return node;
    }
    // Required method for generating possible moves
    private void expandNode(MCNode node) {
        CheckersMove[] possibleMoves = legalMoves(node.getState(), node.getPlayer());
        for (CheckersMove move : possibleMoves) {
            CheckersData newState = new CheckersData(node.getState());
            newState.makeMove(move);
            int nextPlayer = (node.getPlayer() == CheckersData.RED) ? CheckersData.BLACK : CheckersData.RED;
            MCNode childNode = new MCNode(newState, move, nextPlayer);
            node.addChild(childNode);
        }
    }
    // Helper method that finds a random move if no move leads the a better score
    // sort of a backup if nothing leads to a better score
    private double simulateRandomPlayout(MCNode node) {
        CheckersData tempBoard = new CheckersData(node.getState());
        int currentPlayer = node.getPlayer();
        int moveCount = 0;
    
        while (moveCount < SIMULATION_DEPTH && !isTerminalNode(tempBoard)) {
            CheckersMove[] legalMoves = legalMoves(tempBoard, currentPlayer);
    
            // Check if there are no legal moves
            if (legalMoves.length == 0) {
                break; // No moves possible, exit the loop
            }
    
            int randomIdx = random.nextInt(legalMoves.length);
            tempBoard.makeMove(legalMoves[randomIdx]);
            currentPlayer = (currentPlayer == CheckersData.RED) ? CheckersData.BLACK : CheckersData.RED;
            moveCount++;
        }
    
        return calculateScore(tempBoard);
    }
    // used to score the players based on moves that lead to end states(Wins, Loses, Draws)
    private boolean isTerminalNode(CheckersData board) {
        // Check if either player has no pieces left
        boolean redHasPieces = false, blackHasPieces = false;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int piece = board.pieceAt(row, col);
                if (piece == CheckersData.RED || piece == CheckersData.RED_KING) {
                    redHasPieces = true;
                } else if (piece == CheckersData.BLACK || piece == CheckersData.BLACK_KING) {
                    blackHasPieces = true;
                }
                if (redHasPieces && blackHasPieces) {
                    return false; // Both players have pieces
                }
            }
        }
    
        // If one player has no pieces, the game is over
        return true;
    }
    //This is a helper method that is used to score possible moves based on the pieces on the board
    // More BLACK pieces is good for Agent, more RED is bad
    private double calculateScore(CheckersData board) {
        //Difference in the number of pieces, favoring BLACK
        int redCount = 0, blackCount = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int piece = board.pieceAt(row, col);
                if (piece == CheckersData.RED || piece == CheckersData.RED_KING) {
                    redCount++;
                } else if (piece == CheckersData.BLACK || piece == CheckersData.BLACK_KING) {
                    blackCount++;
                }
            }
        }
        return blackCount - redCount;
    }

    //Backpropagate method to move back up the tree
    private void backPropagate(MCNode nodeToExplore, double score) {
        MCNode tempNode = nodeToExplore;
        while (tempNode != null) {
            tempNode.incrementVisit();
            tempNode.addScore(score);
            tempNode = tempNode.getParent();
        }
    }
    //this goes through the tree to find the best scoring moves for the agent
    private MCNode findBestNodeWithUCB1(MCNode node) {
        int parentVisit = node.getVisitCount();
        return node.getChildren().stream()
                .max((node1, node2) -> Double.compare(ucb1(parentVisit, node1.getWinScore(), node1.getVisitCount()),
                                                       ucb1(parentVisit, node2.getWinScore(), node2.getVisitCount())))
                .orElseThrow(() -> new IllegalStateException("No best node found"));
    }
    // UCB1 that was required by PDF
    private double ucb1(int totalVisits, double nodeWinScore, int nodeVisitCount) {
        if (nodeVisitCount == 0) {
            return Double.MAX_VALUE;
        }
        return (nodeWinScore / (double) nodeVisitCount) + C * Math.sqrt(Math.log(totalVisits) / (double) nodeVisitCount);
    }
}