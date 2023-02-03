public class Test {
  public static void main(String[] args) {
    EllisAIPlayer j = new EllisAIPlayer(1);
    GameBoard b1 = new GameBoard();
    b1.printBoard();
    j.playChip(b1);
  }
}