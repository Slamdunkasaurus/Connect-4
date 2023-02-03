import java.util.*;

public class DumbAIPlayer extends Player {
  
  public DumbAIPlayer(int playerNum) {
    super(("DumbAI" + playerNum), playerNum);
  }
  
  public int playChip(GameBoard board) {
    ArrayList<Integer> validCols = new ArrayList<Integer>();
    
    for(int i = 0; i < board.getGridCopy()[0].length; i++){
      if(!board.isColumnFull(i)){
        validCols.add(i);
      }
    }
    
    return validCols.get((int) (Math.random() * validCols.size()));
  }
}