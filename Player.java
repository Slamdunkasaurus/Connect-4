 public abstract class Player {
  /*INSTANCE VARIABLES*/
  protected String name;
  protected int num;
  
  /*CONSTRUCTORS*/
  public Player(String theName, int playerNum) {
    name = theName;
    num = playerNum;
  }
  
  /*METHODS*/
  /*ABSTRACT METHODS*/
  public abstract int playChip(GameBoard board);
  
  
  /*ACCESSORS*/
  //SIMPLE ACCESSORS
  public String getName() {return name;}
  
  public int getNum() {return num;}
  
  
  //MUTATORS
  //SIMPLE MUTATORS
  public void setNum(int playerNum){num = playerNum;}  
  public void setName(String theName){name = theName;}
}