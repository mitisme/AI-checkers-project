
This is a quick explanation/walkthrough of this Project

CheckersData:

    GetLegalMoves(int player):
        - Used to get all legal moves for the player, calls getLegalJumpsFrom(player, row, col)
            to find jumps
        - If no jumps are found, then we search for all legal moves, as jumps are required
    
    GetAdditionalJumps(ArrayList moves, CheckersMove, int player):
        - Used by getLegalJumpsFrom(player, row, col) to recurive check for additional jumps
        - if an addition jump is found then we recurively call this method for the new position
        - also call isSquareVisited() to check if a square has already been jumped to
            This helps it not get stuck in a loop of thinking it can jump back and forth
        - Calls canJump() to see if a jump is valid or not, if it returns truem then we trigger the 
            recurive call to check for additional jumps

    getLegalJumpsFrom(int player, int fromRow, int fromCol):
        - Used by GetLegalMoves(), goes through the beices on board to see if any can jump,
            if so then we call GetAdditionalJumps recurively until no more jumps are found.
        - returns the jumps as an array
    
    canJump(int pieceType, int fromRow, int fromCol, int midRow, int midCol, int toRow, int toCol):
        - used to check if a piece in the from position can jump a piece and mid position, finally
            landing on the to position. Returns true or false based on if this jump is possible
    
    canMove(int player, int fromRow, int fromCol, int toRow, int toCol):
        - Similar to canJump() but just checks for valid moves instead


AdversarialSearch:

    - Pretty similar to the original code provided, just added the returns statements and such


AlphaBetaSearch:

    - Personally this one works much faster and I think it performs better than MCTS, and requires less code overall
    - The max depth of the search is set to 8, I found 10 to be a little too long of a wait

    makeMove():
        - starts by starting at depth 0, while depth is less than the max depth
        - we get moves, then make a temporary state, then call minValue to find the untility score
            of the new state.
        - if the score is higher than the last, we set bestMove to the newest move, then increase depth

    maxValue():
        - is used to get the best score for the AI agent, it follows the rule for scoring based on
            possible terminal statements
                /*
                *  utility function for terminal states has the
                *  following values:
                •  1 if a win by the agent,
                •  −1 if a loss by the agent,
                •  0 if a draw.
                */
        - It sets value equal to the previous depths best value, then if beta score is less than value 
            we return the value as it beats beta, then set aplha to whichever is higher alpha or value
    
    minValue():
        - Used in the same since as maxValue(), but for the player instead

    evaluateBoard():
        - This method is a helper method I implemented to score the board based on number of pieces
            and the types of pieces. Kings are worth twice as much in this evaluation.
        - I found this helpful to add as sometimes a terminal state isnt found for a very long time
        - This also persuades the agent to take pieces, and to line up multiple jumps (2+ for 1 is a good trade)

    isTerminal():
        - is used to check if the game has ended in a win, lose, or draw

    canMove():
        - is a different method than the one found in checkersData
        - I found this helpful for calling isTerminal() to check see if the player or agent has any moves

    MCNode():

        - Used by MCTS for the nodes
        
        - Has getters and setters for the following:
            State, Parent, Visitcount,
            Winscore, lastMove, Child,
            Player.

        addChild(MCNode child):
            - adds a child to a node and sets the current node as its Parent
        
        addScore(double score):
            - adds the score of a node to the win score 
                This makes it possible to calulate a series of moves
            
        incrementVisit():
            - used to increment the visitCount, used for how deep the search goes
        
        getChildWithMaxScore():
            Performs a for loop to loop through the entire tree finding the highest scoring node,
                This help calulate the best move possible

    MCTree:
         - has two variables: root and size
        
        MCTree():
            - Constructor that initializes the tree
            - root is null and size is 0

        MCTree(MCNode root):
            - another constructor
            - seets the current root to root
            - then sets the size to 1

        getRoot():
            - returns the current root;
        
        setRoot(MCNode root):
            - changes the current root to the root pass in
            - then if root equal null, size is 0, else its 1
        
        incrementSize():
            - used to increase size as nodes are added to tree

        getSize():
            - returns the current size

    MonteCarloTreeSearch:
        - Things to note
            - we use the double C for the UCB1 constant
            - BLACK_PLAYER is to globally mark the agent as BLACK_PLAYER
            - SIMULATION_DEPTH is used to limit how deep the MCST searches

        makeMove(legalMoves):
            - we set a time limit for the MCTS so we dont take too long finding the best move 
                I found about a second is the sweet spot for optimal move, and time
            
            - Start by using a while loop on the current time
            - start by exploring the root node
            - expand it to find more moves and children nodes
            - have score start by doing a random move
            - then propagate backwards to see if a different node is better
            - return the move thats from the child with best score
        
        selectNode(MCNode rootNode):
            - used to return the node with the best score using the UBC1 constant
            - if nothing is found return the node passed
            - else node = new best node
        
        expandNode(MCNode node):
            - this is used to generate possible moves
            - get possible moves from the node passed in
            - it then travserves through the possible moves and add them as a child node

        simulateRandomPlayout(MCNode node):
            - used to get a random move for the current node/state
            - used for exploration and to start the tree search
            - counts the moves, and uses a while loop to make sure it doesnt pass the SIMULATION_DEPTH
            - then returns the calulated score of the temp state its on

        isTerminalNode(CheckersData board):
            - used to see if the temp board is in a terminal state
            - checks if there are pieces left, and if they have moves
            - returns true or false
        
        calculateScore(CheckersData board):
            - this is used to find the score of the state the MCTS is currently exploring
            - counts pieces of both sides, this is useful as typically more friendly pieces is good
                and more enemy pieces is based

        backPropagate(nodeToExplore, score):
            - looks at the node passed in, increase the visit count
            - then adds the score to the node
            - then backPropagates to the nodes Parent
            - this works by adding up the scores as we do back up the tree

        findBestNodeWithUCB1(MCNode node):
            - used to score the moves for the agent specifically
            - compares the number of visited nodes and the scores using the UCB1 constant.
            - then chooses and returns the best node

        ucb1(totalVists, nodeWinScore, nodeVisitCount):
            - checks if any nodes have been visited, if so return the Winscore

            

        



