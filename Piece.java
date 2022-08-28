import java.util.*;

abstract public class Piece{
  public String name = "";
  public String color = "";
  public int row = 0;
  public int col = 0;
  public boolean moved = false;
  public boolean castle = false;
  public boolean attack = false;
  abstract public ArrayList<Move> getMoves(Piece[][]board);



}