import java.util.*;

public class GameEngine {
  private Player p1;
  private Player p2;
  private GameBoard board;
  
  public GameEngine() {
    Scanner in = new Scanner(System.in);
    System.out.println("\t1. Play Human vs Human\n\t2. Play Human vs AI\n\t3. Play AI vs AI");
    int gameType = in.nextInt();
    in.nextLine();
    
    board = new GameBoard();
    if (gameType == 1) {
      p1 = new HumanPlayer(1);
      p2 = new HumanPlayer(2);
    } else if (gameType == 2) {
      p2 = new HumanPlayer(1);
      p1 = newAIPlayer(2);
    } else {
      p1 = newAIPlayer(1);
      p2 = newAIPlayer(2);
    }
  }
  
  //makes a new ai player based on user input
  public Player newAIPlayer(int playerNum) {
    Scanner in = new Scanner(System.in);
    
    System.out.println("Please enter 1 for DumbAI, 2 for BasicAI, or 3 for EllisAI.");
    int choice = in.nextInt();
    in.nextLine();
    
    if (choice == 1) {
      return new DumbAIPlayer(playerNum);
    } else if (choice == 2) {
      return new BasicAIPlayer(playerNum);
    } else {
      return new EllisAIPlayer(playerNum);
    }
  }
  
  public void playGame() {
    boolean playerNum = false; //false for p1, true for p2
    int winner = -1;
    board.printBoard();
    while (!board.isFull() && winner == -1) {
      if (!playerNum) {
        board.playChip(p1.playChip(board), 1);
      } else {
        board.playChip(p2.playChip(board), 2);
      }
      
      playerNum = !playerNum;
      board.printBoard();
      winner = board.getWinner();
    }
    
    System.out.println(((winner == 1) ? p1.getName() :
                        (winner == 2) ? p2.getName() : "No one") + " wins!");
  }
}