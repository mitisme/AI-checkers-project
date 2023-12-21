package edu.iastate.cs472.proj2;

/**
 * This class implements the Alpha-Beta pruning algorithm to find the best 
 * move at current state.
*/
public class AlphaBetaSearch extends AdversarialSearch {
    //initialization variable
    private static final int MAX_DEPTH =8; // Maximum depth for iterative deepening 10+ starts getting slow
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
    @Override
    public CheckersMove makeMove(CheckersMove[] legalMoves) {
        int depth = 1;
        CheckersMove bestMove = null;
        //double printScore =0;
        // Iterative deepening loop
        while (depth < MAX_DEPTH) {
            //System.out.println("Thinking for Depth " + depth + " . . . ");
            double bestValue = Double.NEGATIVE_INFINITY;
            for (CheckersMove move : legalMoves) {
                CheckersData tempBoard = new CheckersData(board);
                tempBoard.makeMove(move);

                double value = minValue(tempBoard, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, depth, CheckersData.RED);
                //System.out.println("value: " + value + " bestValue: " + bestValue);
                if (value > bestValue) {
                    bestValue = value;
                    //printScore = bestValue;
                    bestMove = move;
                }
            }
            depth++;
        }
        System.out.println(board);
        System.out.println();
        return bestMove;
    }
    //AI Agent Value Score
    private double maxValue(CheckersData board, double alpha, double beta, int depth, int player) {
        /*
         *  utility function for terminal states has the
         *  following values:
         •  1 if a win by the agent,
         •  −1 if a loss by the agent,
         •  0 if a draw.
         */
        if (depth == 0 || isTerminal(board)) {
            if (!canMove(board, CheckersData.RED)) {
            return evaluateBoard(board) + 1; //Player Loses, thats good
            }
            else if (!canMove(board, CheckersData.BLACK)) {
                 return evaluateBoard(board) - 1; //Agent Loses, thats bad
            }
            else {
                return evaluateBoard(board); //draw, net 0 
            }
        }

        double value = Double.NEGATIVE_INFINITY;
        for (CheckersMove move : legalMoves(board, CheckersData.BLACK)) {
            CheckersData tempBoard = new CheckersData(board);
            tempBoard.makeMove(move);
            value = Math.max(value, minValue(tempBoard, alpha, beta, depth - 1, CheckersData.RED));

            if (value >= beta) return value;
            alpha = Math.max(alpha, value);
        }
        return value;
    }
    //Player Value Score
    private double minValue(CheckersData board, double alpha, double beta, int depth, int player) {
        /*
         *  utility function for terminal states has the
         *  following values:
         •  1 if a win by the agent,
         •  −1 if a loss by the agent,
         •  0 if a draw.
         */
        
        if (depth == 0 || isTerminal(board)) {
            if (!canMove(board, CheckersData.BLACK)) {
            return evaluateBoard(board) + 1;    //Agent Loses, thats good for player
            }
            else if (!canMove(board, CheckersData.RED)) {
                 return evaluateBoard(board) - 1; //player Loses, thats good for agent
            }
            else {
                return evaluateBoard(board); //draw, net 0 
            }
        }

        double value = Double.POSITIVE_INFINITY;
        for (CheckersMove move : legalMoves(board, CheckersData.RED)) {
            CheckersData tempBoard = new CheckersData(board);
            tempBoard.makeMove(move);
            value = Math.min(value, maxValue(tempBoard, alpha, beta, depth - 1, CheckersData.BLACK));

            if (value <= alpha) return value;
            beta = Math.min(beta, value);
        }
        return value;
    }
    


    //This helper method goes over the board adn scores the Player and Agent based on their pieces
    // Kings are worth double the man value as they are more valuable
    //this is used to help determine optimal moves for the Agent
    private double evaluateBoard(CheckersData board) {
        // Define the value of pieces
        final double MAN_VALUE = .25;
        final double KING_VALUE = .5;
        double score = 0.0;
    
        // Iterate over the board to calculate the score
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                switch (board.pieceAt(row, col)) {
                    case CheckersData.RED: // Human player pieces
                        score -= MAN_VALUE;
                        break;
                    case CheckersData.RED_KING:
                        score -= KING_VALUE;
                        break;
                    case CheckersData.BLACK: // AI pieces
                        score += MAN_VALUE;
                        break;
                    case CheckersData.BLACK_KING:
                        score += KING_VALUE;
                        break;
                }
            }
        }
        return score;
    }



    //Checks if a state is an end state returns true or false
    private boolean isTerminal(CheckersData board) {
        boolean redCanMove = canMove(board, CheckersData.RED);
        boolean blackCanMove = canMove(board, CheckersData.BLACK);
    
        // Check for win/loss conditions
        if (!redCanMove || !blackCanMove) {
            // Game is terminal if either player cannot move - win for the other player
            return true;
        }
    
        // Check for draw conditions
        if (!redCanMove && !blackCanMove) {
            // Game is a draw if neither player can move
            return true;
        }
        return false; // Game is not terminal
    }
    
    // Checkes if a player can move on the current state, this is used by the isTerminal() method to 
    //determine if any states lead to a win, loss, or draw
    private boolean canMove(CheckersData board, int player) {
        // Check if player has any pieces left
        boolean hasPieces = false;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((player == CheckersData.RED && (board.pieceAt(row, col) == CheckersData.RED || board.pieceAt(row, col) == CheckersData.RED_KING)) ||
                    (player == CheckersData.BLACK && (board.pieceAt(row, col) == CheckersData.BLACK || board.pieceAt(row, col) == CheckersData.BLACK_KING))) {
                    hasPieces = true;
                    break;
                }
            }
            if (hasPieces) break;
        }
    
        // If player has no pieces, they cannot move
        if (!hasPieces) return false;
    
        // Check if there are any legal moves available for the player
        return board.getLegalMoves(player).length > 0;
    }

}
