package tictactoe;

import java.util.Arrays;

public class treeBoard {
	//a tree diagram of tic tac toe boards.
	//A data structure that will be used by the AI to generate tree diagrams of all possible outcomes and determine the min/max score of each possible move for the AI
	//To use this data structure/method, the AI only needs to create the initial treeBoard. This treeboard will then construct all of it's children,
	//who will then construct all of their children etc. until every single possible board state has a treeBoard.
	//Then, each terminal (final board) is evaluated as either a win (1), a loss (-1) or a tie (0)
	//Those scores will then be passed on to the parents of those terminal boards
	//When a treeBoard has more than one child, it's score will be either the smallest of the scores of its children if its children's Max = false
	//Or it's score will be the largest of the scores of its children if its children's Max  = true
	//if boolean Max = true, its a maximizing turn, meaning it's the AI's turn to place and o and they are trying to win.
	//if boolean Max = false, its a minimizing turn, meaning its the human player's turn to place an X and the AI is trying not to lose.
	//Once the scores have been passed up to the children of the initial treeBoard, the AI will choose the child that has the highest score and make that move.
	//If that score is a 1, the AI gets a guaranteed win.
	//If that score is a 0, the AI can only win or tie.
	//If that score is a -1, the AI can win, lose or tie.
	//The AI should never be able to lose tic tac toe using this strategy.
	
	//Each instance of treeBoard will:
	//Will store a certain board state, the treeboard which it comes from (the parent board)
	//AND the treeboards which come from this board (the children board)
	//Will also store whether the board state is on a player turn (Minimizing turn) or AI turn (Maximizing turn)
	//if boolean Max = true, its a maximizing ai turn. if boolean Max = false, its a minimizing human turn.
	//Will store a score that can be 0, 1 or -1
	
	
	char[][] boardState = new char[3][3];
	treeBoard parent;
	treeBoard[] children;
	boolean Max;
	int score;

	public treeBoard(char board[][]) {
		//constructor for the initial treeBoard. Will be used by the aiTurn method
		//this will be the board after the human player's turn. Since the next turn is the AI's and therefore a max turn, this turn is a min turn
		copyBoard(boardState,board);
		Max = false;
		generateChildren();
	}
	
	public treeBoard(char board[][], treeBoard Parent) {
		//constructor for a board coming from another board. Should only be used by the generateChildren function.
		copyBoard(boardState, board);
		parent = Parent;
		//minOrMax will determine if this board is on a min or max turn based off it's parents Max value
		minOrMax();
		//generateChildren will check if the board state is terminal and if so it will calculate it's score. Otherwise if the board is not terminal
		//it will generate this treeBoard's children and check if they're terminal and so on.
		generateChildren();
	}
	
	
	
	public void generateChildren() {
		
		//first determine if this treeboard does have children, or if its terminal
		char state = LeoTicTacToe.checkBoard(boardState);
		if (state == 'a') {
			//this is the case that the treeBoard does have children. This will create children boards and then use their score to determine this board's score
			//uses the constructor for boards coming from another board
			//first finds the open spots on the board
			//then determines whether it will fill with x's or o's
			//Then generates a board for each possible place where the filler could be placed on the board
			String[] possibilities = LeoTicTacToe.openSpots(boardState);
			this.children = new treeBoard[possibilities.length];
			char filler;
			if (Max) {
				filler = 'x';
				//the current turn is an AI max turn, therefore the children turns will be player min turns
			} else {
				filler = 'o';
				//the current turn is a player min turn, therefore the children turns will be an ai max turn
			}
			char[][] tempBoard = new char[3][3];
			//this for loop runs thorugh each possible placement of the filler 
			for (int i = 0; i < possibilities.length; i++) {
				//First the tempboard is set to the current board, then the filler is placed in it's spot according to the possibilities
				//then a new treeBoard is created in the children[] array.
				copyBoard(tempBoard, boardState);
				int x = Integer.parseInt(possibilities[i].substring(0, 1));
				int y = Integer.parseInt(possibilities[i].substring(1, 2));
				tempBoard[x][y] = filler;
				this.children[i] = new treeBoard(tempBoard, this);
			}
			//now find the score of the children boards and min/max them
			//first get an array of the scores.
			int[] scores = new int[children.length];
			for (int i = 0; i < children.length; i++) {
				scores[i] = children[i].score;
			}
			Arrays.sort(scores);
			
			if (Max) {
				//if this treeBoard is a max, then min the children's scores. Find the lowest element in the array, which will be the first element in a sorted array
				score = scores[0];
			} else {
				//if this treeBoard is a min, then max the children's scores. Find the highest element in the array, which will be the last element in a sorted array
				score = scores[scores.length-1];
			}
			
			
			
		} 
		//if this board is terminal determine it's score.
		else if (state == 'x') {
			score = -1;
		} else if (state == 'o') {
			score = 1;
		} else if (state == 't') {
			score = 0;
		}
		
	}
	

	
	public void minOrMax() {
		//this method determines if a treeBoard is on a min or max turn, by making its Max the opposite of it's parent's
		Max = !parent.Max;
	}
	
	public char[][] findBest() {
		
		//this method finds the best option for the AI. is only to be used by the aiTurn method.
		//Finds the highest score of the children of the initial treeBoard, and then returns that child's boardState
		
		//first looks for children with scores of 1, which means a guaranteed victory for the AI
		for (int i = 0; i < children.length; i++) {
			if (children[i].score == 1) {
				return children[i].boardState;
			}
		}
		//Now looks for scores of 0, which means the AI can only tie or win
		for (int i = 0; i < children.length; i++) {
			
			if (children[i].score == 0) {
				return children[i].boardState;
			}
		}
		System.out.println("Checkpoint");
		//No winning or tying solutions were found (which should be impossible i think) so will return the board of the first child.
		return children[0].boardState;
	}
	
	public void copyBoard(char[][] board, char[][] template) {
		//copies one board onto another without making one of them a pointer to the other
		//doing board = template caused a lot of issues for my program, so i made this
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				board[x][y] = template[x][y];
			}
		}
	}
	

}
