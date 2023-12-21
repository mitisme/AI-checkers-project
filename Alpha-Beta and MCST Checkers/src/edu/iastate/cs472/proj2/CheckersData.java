package edu.iastate.cs472.proj2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;

/**

 * An object of this class holds data about a game of checkers.
 * It knows what kind of piece is on each square of the checkerboard.
 * Note that RED moves "up" the board (i.e. row number decreases)
 * while BLACK moves "down" the board (i.e. row number increases).
 * Methods are provided to return lists of available legal moves.
 */
public class CheckersData {
    /*  The following constants represent the possible contents of a square
      on the board.  The constants RED and BLACK also represent players
      in the game. */
    static final int 
        EMPTY = 0, 
        RED = 1, 
        RED_KING = 2, 
        BLACK = 3, 
        BLACK_KING = 4;

    int[][] board;  // board[r][c] is the contents of row r, column c.

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    //this is for testing purposes only
    // private static final Logger LOGGER = Logger.getLogger(CheckersData.class.getName());

    // static {
    //     try {
    //         FileHandler fileHandler = new FileHandler("CheckersLog.log", true);
    //         fileHandler.setFormatter(new SimpleFormatter());
    //         LOGGER.addHandler(fileHandler);
    //         LOGGER.setLevel(Level.INFO);
    //     } catch (Exception e) {
    //         LOGGER.log(Level.SEVERE, "Error occurred in logger setup", e);
    //     }
    // }

    // Copy constructor
    public CheckersData(CheckersData other) {
        this.board = new int[8][8];
        for (int row = 0; row < 8; row++) {
            System.arraycopy(other.board[row], 0, this.board[row], 0, 8);
        }
    }
    
    
    /**
     * Constructor.  Create the board and set it up for a new game.
     */
    CheckersData() {
        board = new int[8][8];
        setUpGame();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < board.length; i++) {
            int[] row = board[i];
            sb.append(8 - i).append(" ");
            for (int n : row) {
                if (n == 0) {
                    sb.append(" ");
                } else if (n == 1) {
                    sb.append(ANSI_RED + "R" + ANSI_RESET);
                } else if (n == 2) {
                    sb.append(ANSI_RED + "K" + ANSI_RESET);
                } else if (n == 3) {
                    sb.append(ANSI_YELLOW + "B" + ANSI_RESET);
                } else if (n == 4) {
                    sb.append(ANSI_YELLOW + "K" + ANSI_RESET);
                }
                sb.append(" ");
            }
            sb.append(System.lineSeparator());
        }
        sb.append("  a b c d e f g h");

        return sb.toString();
    }


    /**
     * Set up the board with checkers in position for the beginning
     * of a game.  Note that checkers can only be found in squares
     * that satisfy  row % 2 == col % 2.  At the start of the game,
     * all such squares in the first three rows contain black squares
     * and all such squares in the last three rows contain red squares.
     */
    void setUpGame() {
        board = new int[8][8];
        //Set up initial black pieces
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row % 2 == 0 && col % 2 == 0) || (row % 2 == 1 && col % 2 == 1)) {
                    board[row][col] = BLACK;
                }
            }
        }
        //Set up initial red pieces
        for (int row = 5; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row % 2 == 0 && col % 2 == 0) || (row % 2 == 1 && col % 2 == 1)) {
                    board[row][col] = RED;
                }
            }
        }
    }


    /**
     * Return the contents of the square in the specified row and column.
     */
    int pieceAt(int row, int col) {
        return board[row][col];
    }

    /**
     * Make the specified move.  It is assumed that move
     * is non-null and that the move it represents is legal.
     *
     * Make a single move or a sequence of jumps
     * recorded in rows and cols.
     *
     */
    void makeMove(CheckersMove move) {
       // LOGGER.log(Level.INFO, "makeMove called with move: " + move);

        int l = move.rows.size();
        // Loop over each step in the move
        for (int i = 0; i < l - 1; i++)
            makeMove(move.rows.get(i), move.cols.get(i), move.rows.get(i + 1), move.cols.get(i + 1));
    }


    /**
     * Make the move from (fromRow,fromCol) to (toRow,toCol).  It is
     * assumed that this move is legal.  If the move is a jump, the
     * jumped piece is removed from the board.  If a piece moves to
     * the last row on the opponent's side of the board, the
     * piece becomes a king.
     *
     * @param fromRow row index of the from square
     * @param fromCol column index of the from square
     * @param toRow   row index of the to square
     * @param toCol   column index of the to square
     */
    void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        //LOGGER.log(Level.INFO, "makeMove from [" + fromRow + "," + fromCol + "] to [" + toRow + "," + toCol + "]");

        board[toRow][toCol] = board[fromRow][fromCol];
        board[fromRow][fromCol] = EMPTY;
    
        // Check if jump is possible
        if (fromRow - toRow == 2 || fromRow - toRow == -2) {
            int jumpRow = (fromRow + toRow) / 2;
            int jumpCol = (fromCol + toCol) / 2;
            board[jumpRow][jumpCol] = EMPTY;
        }
    
        // Check if the piece can move to the back row and turns into a king
        if (toRow == 0 && board[toRow][toCol] == RED)
            board[toRow][toCol] = RED_KING;
        if (toRow == 7 && board[toRow][toCol] == BLACK)
            board[toRow][toCol] = BLACK_KING;
    }

    
    /**
     * Return an array containing all the legal CheckersMoves
     * for the specified player on the current board.  If the player
     * has no legal moves, null is returned.  The value of player
     * should be one of the constants RED or BLACK; if not, null
     * is returned.  If the returned value is non-null, it consists
     * entirely of jump moves or entirely of regular moves, since
     * if the player can jump, only jumps are legal moves.
     *
     * @param player color of the player, RED or BLACK
     */
        CheckersMove[] getLegalMoves(int player) {
        if (player != RED && player != BLACK && player != RED_KING && player != BLACK_KING)
            return null;  // Player must be RED or BLACK or KINGS.

        int playerKing;  // The constant representing a King for the player
        if (player == RED)
            playerKing = RED_KING;
        else
            playerKing = BLACK_KING;

        ArrayList<CheckersMove> moves = new ArrayList<>();  // Store legal moves.

        // First, check for any possible jumps Add all of them to the list
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] == player || board[row][col] == playerKing) {
                    CheckersMove[] jumps = getLegalJumpsFrom(player, row, col);
                    if (jumps != null) {
                        Collections.addAll(moves, jumps);
                    }
                }
            }
        }

        // If no jumps are available, then look for regular moves
        if (moves.isEmpty()) {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (board[row][col] == player || board[row][col] == playerKing) {
                        if (canMove(player, row, col, row + 1, col + 1))
                            moves.add(new CheckersMove(row, col, row + 1, col + 1));
                        if (canMove(player, row, col, row - 1, col + 1))
                            moves.add(new CheckersMove(row, col, row - 1, col + 1));
                        if (canMove(player, row, col, row + 1, col - 1))
                            moves.add(new CheckersMove(row, col, row + 1, col - 1));
                        if (canMove(player, row, col, row - 1, col - 1))
                            moves.add(new CheckersMove(row, col, row - 1, col - 1));
                    }
                }
            }
        }

        if (moves.isEmpty())
            return new CheckersMove[0];  // No legal moves available
        else
            return moves.toArray(new CheckersMove[0]);
    }


    private void getAdditionalJumps(ArrayList<CheckersMove> moves, CheckersMove move, int player) {
        

        if (player == RED_KING){
            //LOGGER.log(Level.INFO, "HERE   getAdditionalJumps called for player: " + player + " Current move: " + move.toString());
        }
        else {
            //LOGGER.log(Level.INFO, "getAdditionalJumps called for player: " + player);
        }
        int lastRow = move.rows.get(move.rows.size() - 1);
    int lastCol = move.cols.get(move.cols.size() - 1);
    
    // Determine if the current piece is a king
    boolean isKing = (player == RED_KING || player == BLACK_KING);

    int[][] directions;
    if (isKing) {
        // Kings can move in all directions
        directions = new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
    } else {
        // Regular pieces can only move forward
        directions = (player == RED) ? new int[][]{{-1, 1}, {-1, -1}} : new int[][]{{1, 1}, {1, -1}};
    }

    boolean canJump = false; // Flag to check if any jump is possible from this position
    for (int[] dir : directions) {
        int newRow = lastRow + 2 * dir[0];
        int newCol = lastCol + 2 * dir[1];
        if (canJump(player, lastRow, lastCol, lastRow + dir[0], lastCol + dir[1], newRow, newCol) &&
            !isSquareVisited(move, newRow, newCol)) {
            CheckersMove newMove = move.clone();
            newMove.addMove(newRow, newCol);
            getAdditionalJumps(moves, newMove, player);
            canJump = true;
        }
    }

    // Add the move if no further jumps are possible from the current position
    if (!canJump && !moves.contains(move)) {
        moves.add(move);
    }
}

    private boolean isSquareVisited(CheckersMove move, int row, int col) {
        for (int i = 0; i < move.rows.size(); i++) {
            if (move.rows.get(i) == row && move.cols.get(i) == col) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Return a list of the legal jumps that the specified player can
     * make starting from the specified row and column.  If no such
     * jumps are possible, null is returned.  The logic is similar
     * to the logic of the getLegalMoves() method.
     *
     * Note that each CheckerMove may contain multiple jumps. 
     * Each move returned in the array represents a sequence of jumps 
     * until no further jump is allowed.
     *
     * @param player The player of the current jump, either RED or BLACK.
     * @param fromRow       row index of the start square.
     * @param fromCol       col index of the start square.
     */

     CheckersMove[] getLegalJumpsFrom(int player, int fromRow, int fromCol) {
        if (player != RED && player != BLACK && player != RED_KING && player != BLACK_KING)
            return null;
    
        int playerKing = (player == RED) ? RED_KING : BLACK_KING;
        ArrayList<CheckersMove> moves = new ArrayList<>();
    
        if (board[fromRow][fromCol] == player || board[fromRow][fromCol] == playerKing) {
            int pieceType = board[fromRow][fromCol]; // Holds the actual piece type (man or king)
    
            if (canJump(pieceType, fromRow, fromCol, fromRow + 1, fromCol + 1, fromRow + 2, fromCol + 2))
                getAdditionalJumps(moves, new CheckersMove(fromRow, fromCol, fromRow + 2, fromCol + 2), pieceType);
            if (canJump(pieceType, fromRow, fromCol, fromRow - 1, fromCol + 1, fromRow - 2, fromCol + 2))
                getAdditionalJumps(moves, new CheckersMove(fromRow, fromCol, fromRow - 2, fromCol + 2), pieceType);
            if (canJump(pieceType, fromRow, fromCol, fromRow + 1, fromCol - 1, fromRow + 2, fromCol - 2))
                getAdditionalJumps(moves, new CheckersMove(fromRow, fromCol, fromRow + 2, fromCol - 2), pieceType);
            if (canJump(pieceType, fromRow, fromCol, fromRow - 1, fromCol - 1, fromRow - 2, fromCol - 2))
                getAdditionalJumps(moves, new CheckersMove(fromRow, fromCol, fromRow - 2, fromCol - 2), pieceType);
        }
    
        if (moves.isEmpty())
            return null;
        else
            return moves.toArray(new CheckersMove[0]);
    }
    
    /*
     * This is a helper method used to check if a player can jump from its current position
     * to the toRow, toCol position, midRow, midCol is used to mark the location of the piece currently getting jumped
     * returns True -> if the jump is legal
     * returns False -> if the jump is not allowed
     */
    private boolean canJump(int pieceType, int fromRow, int fromCol, int midRow, int midCol, int toRow, int toCol) {
        if (toRow < 0 || toRow >= 8 || toCol < 0 || toCol >= 8)
            return false;  // Move is out of bounds
        if (board[toRow][toCol] != EMPTY)
            return false;  // Destination square is not empty
    
        // Check for RED pieces or RED_KING
        if (pieceType == RED || pieceType == RED_KING) {
            if (pieceType == RED && toRow > fromRow)
                return false;  // Regular RED piece cannot move downwards
            return (board[midRow][midCol] == BLACK || board[midRow][midCol] == BLACK_KING); // Jump over BLACK or BLACK_KING
        }
    
        // Check for BLACK pieces or BLACK_KING
        if (pieceType == BLACK || pieceType == BLACK_KING) {
            if (pieceType == BLACK && toRow < fromRow)
                return false;  // Regular BLACK piece cannot move upwards
            return (board[midRow][midCol] == RED || board[midRow][midCol] == RED_KING); // Jump over RED or RED_KING
        }
    
        return false; // Not a valid piece type
    }
    
    /*
     * Similar to the canJump() method, this checks if the player can move from fromRow, fromCol to toRow, toCol
     * returns True -> The move is legal
     * returns False -> the move is illegal
     */
    private boolean canMove(int player, int fromRow, int fromCol, int toRow, int toCol) {
        
        if (toRow < 0 || toRow >= 8 || toCol < 0 || toCol >= 8)
            return false;
    
        if (board[toRow][toCol] != EMPTY)
            return false;
    
        if (player == RED) {
            if (board[fromRow][fromCol] == RED && toRow > fromRow)
                return false;

            return true;
        } 
        
        else {

            if (board[fromRow][fromCol] == BLACK && toRow < fromRow)
                return false;

            return true;
        }
    }
    
}
