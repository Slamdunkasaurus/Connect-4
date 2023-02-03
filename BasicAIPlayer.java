import java.util.*;

public class BasicAIPlayer extends Player{  
  private int enemyNum;
  
  public BasicAIPlayer(int playerNum) {
    super(("BasicAI" + playerNum), playerNum);
    enemyNum = num == 1 ? 2 : 1;
  }
  
  
  public int playChip(GameBoard board){
    ArrayList<Integer> validCols = new ArrayList<Integer>();
    
    for(int i = 0; i < board.getGridCopy()[0].length; i++){
      if(!board.isColumnFull(i)){
        validCols.add(i);
      }
    }
    
    //check to play winning move
    for(int i = 0; i < validCols.size(); i++){
      if(checkForWinningMove(validCols.get(i), board, num)){
        return validCols.get(i);
      }
    }
    
    //check to block enemy's move
    for(int i = 0; i < validCols.size(); i++){
      if(checkForWinningMove(validCols.get(i), board, enemyNum)){
        return validCols.get(i);
      }
    }
    
    //plays random col
    return validCols.get((int) (Math.random() * validCols.size()));
  }
  
  
  private boolean checkForWinningMove(int col, GameBoard board, int player){
    int row = board.getRowForCol(col);
    board = board.copyBoard();
    
    board.placeChipInColumn(player, col);
    return board.getWinner() == player;
  }
}