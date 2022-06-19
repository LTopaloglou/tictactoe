package tictactoe;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class LeoTicTacToe {
	//Player is X, AI is O
	//this part initializes the variables that are used in multiple methods
	static Scanner sc = new Scanner(System.in);
	public static char[][] board = new char[3][3];
	static int bestOf;
	static boolean hardmode;
	static Random r = new Random();
	
	public static void main(String args[]) {
		//this main method will call on other functions to set up and run the game.
		
		//the settings function introduces the player and sets up the difficulty and best of however many games
		hardmode = settings();
		
		int playerScore = 0;
		int aiScore = 0;
		
		boolean first = r.nextBoolean();
		while((aiScore < bestOf/2.0) && (playerScore < bestOf/2.0)) {
			//this loop runs for every game of tic tac toe
			//Before every match this displays the score, clears the board and decides who goes first.
			System.out.println("You: " + playerScore + " AI: " + aiScore);
			boolean win = false;
			clearBoard();
			if(first) {
				System.out.println("You go first this game.");
			} else {
				System.out.println("The AI goes first this game.");
				aiTurn(board);
			}
			
			while (!win) {
				//this loops runs once for every player/ai turn until the game is over
				
				//player turn, accept input and draw the board
				System.out.println();
				drawBoard(board);
				boolean invalid = false;
				while (!invalid) {
					System.out.println("Please enter the tile you would like to fill: ");
					invalid = checkInput(sc.nextLine());
					
				}
				//check if the game is over
				char state = checkBoard(board);
				if (state == 'x') {
					drawBoard(board);
					System.out.println("You Win!");
					playerScore++;
					break;
				} else if (state == 't') {
					drawBoard(board);
					System.out.println("Tie Game!");
					break;
				}
				
				//AI turn method runs either the easy or hard mode algorithm, depending on the difficulty setting set at the start of the game
				aiTurn(board);
				
				//check if the game is over
				state = checkBoard(board);
				if (state == 'o') {
					drawBoard(board);
					System.out.println("AI Wins!");
					aiScore++;
					break;
				} else if (state == 't') {
					drawBoard(board);
					System.out.println("Tie Game!");
					break;
				}
				
				
			}
			//seperate the games, switch who gets to go first
			System.out.println("-------------------------------------------------------------");
			first = !first;
		}
		
		//this runs after either the player or AI has won the best of however many games
		if (playerScore > aiScore) {
			System.out.println("Congratulations, you have beaten the AI in a best of " + bestOf + " challenge with the score:");
			System.out.println("You: " + playerScore + " AI: " + aiScore);
		} else {
			System.out.println("The AI has beaten you in a best of " + bestOf + " challenge with the score:");
			System.out.println("You: " + playerScore + " AI: " + aiScore);
		}
		System.out.println("Thank you for playing. If you would like to challenge the AI again, relaunch the program.");
	}
	
	public static void drawBoard(char[][] board) {
		//TESTED
		//This method draws the tic tac toe board with seperators between columns based on the 2d array of x's, o's and spaces
		//Empty slots are printed as a 2 digit number to the player can identify which box they want to fill
		for (int y = 0; y < 3; y++) {
			System.out.print('|');
			for (int x = 0; x < 3; x++) {
				if (board[y][x] == ' ') {
					System.out.print(y + "" + x + "|");
				} else {
					System.out.print(board[y][x] + " |");
				}
			}
			System.out.println();
		}
	}
	
	public static boolean settings() {
		//TESTED
		//this method welcomes the player and asks for their settings, only accepting valid input
		System.out.println("Welcome to Tic Tac Toe!");
		System.out.println("You will be x's and the AI will be o's.");
		while (true) {
			System.out.println("Would you like to play best of 3, 5 or 7: ");
			String number = sc.nextLine();
			if (!(number.length() == 0)) {
				char best = number.charAt(0);
				if (best == '3' || best == '5' || best == '7') {
					bestOf = best - 48;
					break;
				}
			}
		}
		
		while(true) {
			System.out.println("Please choose either hard or easy as your difficulty (WARNING: hard AI is unbeatable): ");
			String input = sc.nextLine();
			if (input.equals("hard")) {
				return true;
			} else if (input.equals("easy")) {
				return false;
			} else {
				
			}
		}
		
	}
	
	public static boolean checkInput(String input) {
		//TESTED
		//this method checks for valid input from the player, and then checks if the box they chose is empty, and then if it is it fills the box with an x
		if (input.length() <= 3) {
			return false;
		}
		if ((input.charAt(0) == 48 || input.charAt(0) == 49 || input.charAt(0) == 50) && (input.charAt(1) == 48 || input.charAt(1) == 49 || input.charAt(1) == 50)) {
			int x = Integer.parseInt(input.substring(0, 1));
			int y = Integer.parseInt(input.substring(1, 2));
			if (board[x][y] == ' ') {
				board[x][y] = 'x';
				return true;
			}
			
			
		}
		
		return false;
	}
	
	
	public static char checkBoard(char[][] board) {
		//INCOMPLETE
		//this method will return x if the player has won, o if the AI has won, t if the board is full and it is a tie game and a if the game is incomplete
		
		
		
		
		for (int i = 0; i < 3; i++) {
			//first check vertical lines 
			if (board[i][0] == board[i][1] && board[i][0] == board[i][2]) {
				if (board[i][0] == ' ') {
				} else {
				return board[i][0];
				}
				
			}// then  check horizontal lines 
			else if (board[0][i] == board[1][i] && board[0][i] == board[2][i]) {
				if (board[0][i] == ' ') {
				} else {
				return board[0][i];
				}
				
			}//then check the diagonals 
			else if ((board[0][0] == board[1][1] && board[0][0] == board[2][2]) || (board[2][0] == board[1][1] && board[2][0] == board[0][2])) {
				if (board[1][1] == ' ') {
				}else {
				return board[1][1];
				}
			}
		}
		
		//finally, check if it's a tie game
		boolean full = true;
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (board[x][y] == ' ') {
					full = false;
				}
			}
		}
		if (full) {
			return 't';
		}
		
		//return a for incomplete game
		return 'a';
	}
	
	public static void aiTurn (char[][] board) {
		//COMPLETE
		//This method decides which box the AI will fill with an o
		//There is a hard setting and an easy setting.
		//The easy setting will fill a random box.
		//The hard setting uses a min/max algorithm which is explained and run in the treeBoard.java file
		
		if (hardmode) {
			//hard mode will use a min/max algorithm, which assumes the opponent is playing optimally
			
			treeBoard init = new treeBoard(board);
			init.copyBoard(board, init.findBest());
		} else {
			//easy mode will just fill a random open square
			String[] spot = openSpots(board);
			int i = r.nextInt(spot.length);
			int x = Integer.parseInt(spot[i].substring(0, 1));
			int y = Integer.parseInt(spot[i].substring(1, 2));
			board[x][y] = 'o';
		}
	}
	
	public static void clearBoard() {
		//This method erases the board and fills it with ' '
		for(int x =  0; x <3; x++) {
			for(int y = 0; y < 3; y++) {
				board[y][x] = ' ';
			}
		}
	}
	
	public static String[] openSpots(char board[][]) {
		//This method finds all the empty spots on the board that the AI could put an o in
		String[] spots = new String[9];
		int i = 0;
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (board[x][y] == ' ') {
					spots[i] = x + "" + y;
					i++;
				}
			}
		}
		spots = Arrays.copyOf(spots, i);
		
		return spots;
	}
	

}
