package edu.iastate.cs472.proj2;



/**
 * This class is to be extended by the classes AlphaBetaSearch and MonteCarloTreeSearch.
 */
public abstract class AdversarialSearch {
    protected CheckersData board;
    //private int count; //was used for print debugging purposes

    // An instance of this class will be created in the Checkers.Board
    protected void setCheckersData(CheckersData board) {
        this.board = board;
        //count = 0;
    }
    
    /** 
     * 
     * @return an array of valid moves
     */
    protected CheckersMove[] legalMoves(CheckersData state, int player) {
        return state.getLegalMoves(player);
    }
	
    /**
     * Return a move returned from either the alpha-beta search or the Monte Carlo tree search.
     * 
     * @param legalMoves
     * @return CheckersMove 
     */
    public abstract CheckersMove makeMove(CheckersMove[] legalMoves);
}
