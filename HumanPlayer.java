import java.util.*;

public class HumanPlayer extends Player {
  private static Scanner in = new Scanner(System.in);
  
  public HumanPlayer(int playerNum) {
    super("name", playerNum);
    
    System.out.println("Please enter player " + playerNum + "'s name.");
    name = in.nextLine();
  }
  
  public int playChip(GameBoard board) {
    //request and store the user's choice
    System.out.println("Please enter a column in which to place a chip, " + name + ". (1-" + (board.getGridCopy()[0].length) + ")");
    int col = in.nextInt() - 1;
    in.nextLine();
    
    ArrayList<Integer> validCols = new ArrayList<Integer>();
    
    for(int i = 0; i < board.getGridCopy()[0].length; i++){
      if(!board.isColumnFull(i)){
        validCols.add(i);
      }
    }
    
    //make the user re-enter if the entry is invalid, plays the chip if valid
    while (!validCols.contains(col)) {
      System.out.println("Invalid column. Please enter a valiid column (1-" + (board.getGridCopy()[0].length) + ").");
      col = in.nextInt() - 1;
      in.nextLine();
    }
    
    return col;
  }
}