import java.util.*;

public class EllisAIPlayer extends Player {
  private int lookAhead;
  private int enemyNum;
  
  public EllisAIPlayer(int playerNum) {
    super(("EllisAI" + playerNum), playerNum);
    enemyNum = (num == 1) ? 2 : 1;
    Scanner in = new Scanner(System.in);
    System.out.print("LookAhead: ");
    lookAhead = in.nextInt();
    in.nextLine();
  }
  
  public int playChip(GameBoard theBoard){
    MyGameBoard board = new MyGameBoard(theBoard.getGridCopy());
    ArrayList<Integer> validCols = getValidCols(board);
    
    //check to play winning move
    for(int i = 0; i < validCols.size(); i++){
      if(checkForWinningMove(validCols.get(i), board, num)){
        return validCols.get(i);
      }
    }
    
    //check to block enemy's move
    for(int i = 0; i < validCols.size(); i++){
      if(checkForWinningMove(validCols.get(i), board, enemyNum)) {
        return validCols.get(i);
      }
    }
    
    //plays based on ratings
    ArrayList<int[]> moveRatings = getMoveRatings(board, num, lookAhead);
    int MaxWToLRatioIndex = -1;
    double MaxWToLRatio = -1.0;
    for (int i = 0; i < moveRatings.size(); i++) {
      if (moveRatings.get(i)[1] == 0) {
        if (moveRatings.get(i)[0] > 0) {
          int wRatio = moveRatings.get(i)[0] * 100 + 1;
          if (wRatio > MaxWToLRatio) {
            MaxWToLRatio = wRatio;
            MaxWToLRatioIndex = i;
          }
        } else {
          continue;
        }
      } else {
        double WToLRatio = ((double)(moveRatings.get(i)[0]))/((double)moveRatings.get(i)[1]);
        if ( WToLRatio >= MaxWToLRatio) {
          MaxWToLRatioIndex = i;
          MaxWToLRatio = WToLRatio;
        }
      }
    }
    
    if (MaxWToLRatioIndex == -1) {
      //play the option with the fewest losses
      int fewestLossesIndex = -1;
      int fewestLosses = 99999;
      for (int i = 0; i < moveRatings.size(); i++) {
        if (moveRatings.get(i)[1] < fewestLosses) {
          fewestLosses = moveRatings.get(i)[1];
          fewestLossesIndex = i;
        }
      }
      return fewestLossesIndex;
    }
    
    return validCols.get(MaxWToLRatioIndex);
  }
  
  /*private boolean checkForWinningMove(int col, MyGameBoard board, int player){
    int row = board.getRowForCol(col);
    board = board.copyBoard();
    
    board.playChipNoPrint(col, player);
    System.out.println(board.getWinner() + "" + player);
  }*/
  
  public ArrayList<Integer> getValidCols(MyGameBoard board) {
    ArrayList<Integer> validCols = new ArrayList<Integer>();
    
    for(int i = 0; i < board.getNumCols(); i++){
      if(!board.isColumnFull(i)){
        validCols.add(i);
      }
    }
    
    return validCols;
  }
  
  private boolean checkForWinningMove(int col, MyGameBoard board, int player){
    return board.checkForWinningMove(col, player);
  }
  
  private ArrayList<int[]> getMoveRatings(MyGameBoard board, int player, int levels) {
    ArrayList<Integer> cols = getValidCols(board);
    MyGameBoard[] boards = new MyGameBoard[cols.size()];
    ArrayList<int[]> moveRatings = new ArrayList<int[]>();
    for (int i = 0; i < cols.size(); i++) {
      moveRatings.add(new int[]{0, 0});
    }
  
    //if levels is 0, return blank array
    if (levels == 0) {
      return moveRatings;
    }
    
    for (int i = 0; i < boards.length; i++) {
      boards[i] = board.copyBoard();
      boards[i].playChipNoPrint(cols.get(i), player);
      if (boards[i].getWinner() == player) {
        moveRatings.get(i)[0] += Math.pow(levels, 2);
        continue;
      } else if (boards[i].getWinner() != -1) {
        if (levels == lookAhead - 1) {
          moveRatings.get(i)[1] += 999999999;
        } else {
          moveRatings.get(i)[1] += Math.pow(levels, 5);
        }
        continue;
      } else {
        ArrayList<int[]> mr = getMoveRatings(boards[i], (player == 1)?2:1, levels - 1);
        int[] mrSum = getSumArrayList(mr);
        moveRatings.get(i)[0] += mrSum[1];
        moveRatings.get(i)[1] += mrSum[0];
      }
    }
    return moveRatings;
  }
  
  private int[] getSumArrayList(ArrayList<int[]> arrList) {
    int[] sum = new int[2];
    for (int[] arr : arrList) {
      sum[0] += arr[0];
      sum[1] += arr[1];
    }
    return sum;
  }
}

class MyGameBoard extends GameBoard {
  int[] lastMove; //row, col
  
  public MyGameBoard() {
    this(DEFAULT_ROWS, DEFAULT_COLS);
  }
  
  public MyGameBoard(int row, int col) {
    super(row, col);
    lastMove = new int[]{-1, -1};
  }
  
  public MyGameBoard(int[][] theGrid) {
    super(theGrid);
    lastMove = new int[]{-1, -1};
  }
  
  public MyGameBoard copyBoard() { return new MyGameBoard(grid); }
  
  public int getNumCols() {
    return grid[0].length;
  }
  
  public int getNumRows() {
    return grid.length;
  }
  
  private int checkForWinner(String ints) {
    if (ints.contains("1111")) {
      return 1;
    } else if (ints.contains("2222")) {
      return 2;
    } else {
      return -1;
    }
  }
  
  public int getWinner() {
    //checks only around lastMove
    //return super.getWinner();
    
    if (lastMove[0] == -1 || lastMove[1] == -1) {
      return -1;
    } else if (checkForWinner(getRowAsString(lastMove[0])) != -1) {
      return checkForWinner(getRowAsString(lastMove[0]));
    } else if (checkForWinner(getColumnAsString(lastMove[1])) != -1) {
      return checkForWinner(getColumnAsString(lastMove[1]));
    } else if (checkForWinner(getUpDiagonalAsString(lastMove[0], lastMove[1])) != -1) {
      return checkForWinner(getUpDiagonalAsString(lastMove[0], lastMove[1]));
    } else if (checkForWinner(getDownDiagonalAsString(lastMove[0], lastMove[1])) != -1) {
      return checkForWinner(getDownDiagonalAsString(lastMove[0], lastMove[1]));
    } else {
      return -1;
    }
  }
  
  public boolean checkForWinningMove(int col, int player) {
    int row = getRowForCol(col);
    int[] prevLastMove = new int[2];
    prevLastMove[0] = lastMove[0];
    prevLastMove[1] = lastMove[1];
    
    playChipNoPrint(col, player);
    lastMove[0] = row;
    lastMove[1] = col;
    int winner = getWinner();
    lastMove = prevLastMove;
    grid[row][col] = 0;
    return winner == player;
  }
  
  public boolean playChip(int col, int playerNum) {
    boolean play = super.playChip(col, playerNum);
    if (play) {
      lastMove[0] = getRowForCol(col) + 1;
      lastMove[1] = col;
    }
    return play;
  }
  
  public boolean playChipNoPrint(int col, int playerNum) {
    boolean play = super.placeChipInColumn(playerNum, col);
    if (play) {
      lastMove[0] = getRowForCol(col) + 1;
      lastMove[1] = col;
    }
    return play;
  }
}