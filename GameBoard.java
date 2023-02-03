public class GameBoard {
  /*INSTANCE*/
  public static final int DEFAULT_ROWS = 6;
  public static final int DEFAULT_COLS = 7;
  
  protected int[][] grid;
  //int[] lastMove; //row, col
  
  /*KEY FOR INT ASSIGNMENTS
  0 = empty
  1 = player 1's chip
  2 = player 2's chip*/
  
  
  /*CONSTRUCTORS*/
  public GameBoard() {
    this(DEFAULT_ROWS, DEFAULT_COLS);
  }
  
  public GameBoard(int rows, int cols) {
    grid = new int[rows][cols];
    
    //sets all slots to 0 
    for(int i = 0; i < grid.length; i++) {
      for(int j = 0; j < grid[i].length; j++) {
        grid[i][j] = 0;
      }
    }
    //sets last move to -1
    //lastMove = new int[]{-1, -1};
  }
  
  //passed grid
  public GameBoard(int[][] theGrid) {
    grid = new int[theGrid.length][theGrid[0].length];
    
    for(int i = 0; i < grid.length; i++) {
      for(int j = 0; j < grid[i].length; j++) {
        grid[i][j] = theGrid[i][j];
      }
    }
    //sets last move to -1
    //lastMove = new int[]{-1, -1};
  }
  
  
  /*METHODS*/
  /*ACCESSORS*/
  //SIMPLE ACCESSORS
  //GameBoards
  public GameBoard copyBoard() { return new GameBoard(grid); }
  
  //ints
  //public int getNumRows() { return grid.length; }
  
  //public int getNumCols() { return grid[0].length; }
  
  public int[][] getGridCopy() {
    int[][] gridCopy = new int[grid.length][grid[0].length];
    
    for(int i = 0; i < grid.length; i++) {
      for(int j = 0; j < grid[i].length; j++) {
        gridCopy[i][j] = grid[i][j];
      }
    }
    return gridCopy;
  }
  
  //COMPLEX ACCESSORS
  public void printBoard() {
    System.out.print("\033c");
    System.out.println();
    for (int i = 0; i < grid[0].length; i++) {
      System.out.print((i+1) + "      ");
    }
    
    System.out.println();
    for(int i = 0; i < grid.length; i++) {
      for(int j = 0; j < grid[i].length; j++) {
        System.out.print((grid[i][j] == 0) ? "__     " : 
                         (grid[i][j] == 1) ? (char)27 + "[41m  " + (char)27 + "[49m     " : 
                         (char)27 + "[43m  " + (char)27 + "[49m     "); //_ if 0, red (color 41) O if 1, yellow (color 43) O if 2, then changes back to default (color 49) using ANSI escape sequences for color
      }
      System.out.println("\n");
    }
  }
  
  public String getRowAsString(int row) {
    String s = "";
    for(int i : grid[row]) {
      s += i;
    }
    return s;
  }
  
  public String getColumnAsString(int col) {
    String s = "";
    for(int[] i : grid) {
      s += i[col];
    }
    
    return s;
  }
  
  public String getUpDiagonalAsString(int row, int col) {
    String s = "";
    while (row < grid.length - 1 && col > 0) {
      row ++;
      col --;
    }
    
    while (row >= 0 && col < grid[0].length) {
      s += grid[row][col];
      row --;
      col ++;
    }
    
    return s;
  }
  
  public String getDownDiagonalAsString(int row, int col) {
    String s = "";
    while (row > 0 && col > 0) {
      row --;
      col --;
    }
    
    while (row < grid.length && col < grid[0].length) {
      s += grid[row][col];
      row ++;
      col ++;
    }
    
    return s;
  }
  
  
  /*MISC METHODS*/
  //places a chip in the first free row of a columns
  public boolean playChip(int col, int playerNum) {
    int row = getRowForCol(col);
    if (row == -1) {
      return false;
    } else {
      for (int i = 0; i <= row; i++) {
        grid[i][col] = playerNum;
        if (i != 0) {
          grid[i-1][col] = 0;
        }
        printBoard();
        try {
          Thread.sleep(100);
        } catch (Exception e){}
      }
      return true;
    }
  }
  
  public boolean placeChipInColumn(int playerNum, int col) {
    int row = getRowForCol(col);
    if (row == -1) {
      return false;
    } else {
      grid[row][col] = playerNum;
      return true;
    }
  }
  
  //checks for 4 in a row and returns 0 for no winner, 1 for player one wins, and 2 for player 2 wins.
  private int checkForWinner(String ints) {
    if (ints.contains("1111")) {
      return 1;
    } else if (ints.contains("2222")) {
      return 2;
    } else {
      return -1;
    }
  }
  
  //checks full board for winner and returns -1 for no winner, 1 for player one wins, and 2 for player 2 wins.
  public int getWinner() {
    //checking rows and side diagonals
    for(int i = 0; i < grid.length; i++) {
      //checking rows 
      int winner = checkForWinner(getRowAsString(i));
      if(winner != -1) {
        return winner;
      }
      
      //checking left diagonals
      winner = checkForWinner(getDownDiagonalAsString(i, 0));
      if(winner != -1) {
        return winner;
      }
      
      //checking right diagonals
      winner = checkForWinner(getUpDiagonalAsString(i, grid[0].length - 1));
      if(winner != -1) {
        return winner;
      }
    }
    
    //checking columns and top
    for(int i = 0; i < grid[0].length; i++) {
      //checking columns
      int winner = checkForWinner(getColumnAsString(i));
      if(winner != -1) {
        return winner;
      }
      
      //checking up diagonals on top
      winner = checkForWinner(getUpDiagonalAsString(0, i));
      if(winner != -1) {
        return winner;
      }
      
      //checking down diagonals on bottom
      winner = checkForWinner(getDownDiagonalAsString(0, i));
      if(winner != -1) {
        return winner;
      }
    }
    
    //no winner found, returns -1
    return -1;
  }
  
  //gets first free row for input column
  public int getRowForCol(int col) {
    //iterate through the rows
    for (int r = grid.length - 1; r >= 0; r--) {
      if (grid[r][col] == 0) {
        //return the row if there is no chip
        return r;
      }
    }
    //return -1 if column is full
    return -1;
  }
  
  //returns whether or not the column is full
  public boolean isColumnFull(int col) {
    return grid[0][col] != 0;
  }
  
  //returns true if board is full
  public boolean isFull() {
    //iterates through columns
    for (int i = 0; i < grid[0].length; i++) {
      if (!isColumnFull(i)) {
        //returns false if column is not full
        return false;
      }
    }
    //returns true if all columns are full
    return true;
  }
}

/*Archaic methods
 OLD getWinner(): CHECKS ENTIRE BOARD FOR WINNERS, INEFFICIENT
    //checking rows and side diagonals
    for(int i = 0; i < grid.length; i++) {
      //checking rows 
      int winner = checkForWinner(getRowAsString(i));
      if(winner != -1) {
        return winner;
      }
      
      //checking left diagonals
      winner = checkForWinner(getDownDiagonalAsString(i, 0));
      if(winner != -1) {
        return winner;
      }
      
      //checking right diagonals
      winner = checkForWinner(getUpDiagonalAsString(i, grid[0].length - 1));
      if(winner != -1) {
        return winner;
      }
    }
    
    //checking columns and top
    for(int i = 0; i < grid[0].length; i++) {
      //checking columns
      int winner = checkForWinner(getColumnAsString(i));
      if(winner != -1) {
        return winner;
      }
      
      //checking up diagonals on top
      winner = checkForWinner(getUpDiagonalAsString(0, i));
      if(winner != -1) {
        return winner;
      }
      
      //checking down diagonals on bottom
      winner = checkForWinner(getDownDiagonalAsString(0, i));
      if(winner != -1) {
        return winner;
      }
    }
    
    //no winner found, returns -1
    return -1;
*/
