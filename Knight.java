import java.util.*;
public class Knight extends Piece{
  public Knight(int row, int col, String color){
    this.row = row;
    this.col = col;
    this.color = color;
    this.name = color.charAt(0) + "Knight";
  
  }
  public ArrayList<Move> getMoves(Piece[][]board){
    
    ArrayList<Move> moves = new ArrayList<Move>();
    moves.add(new Move(this, row - 2, col - 1));
    moves.add(new Move(this, row - 2, col + 1));
    moves.add(new Move(this, row + 2, col - 1));
    moves.add(new Move(this, row + 2, col + 1));
    moves.add(new Move(this, row - 1, col - 2));
    moves.add(new Move(this, row - 1, col + 2));
    moves.add(new Move(this, row + 1, col + 2));
    moves.add(new Move(this, row + 1, col - 2));
    int a = 8;
    for(int i = 0; i < a; i ++){
      int newrow = moves.get(i).r;
      int newcol = moves.get(i).c;
      if(newrow < 8 && newrow > -1){
        if(newcol < 8 && newcol > -1){
          if(board[newrow][newcol] != null){
            if(board[newrow][newcol].color == color){
              moves.remove(i);
              i = i - 1;
              a = a - 1;
            }
          }       
        }
        else{
          moves.remove(i);
          i = i - 1;
          a = a - 1;
        }

      }
      else{
        moves.remove(i);
        i = i - 1;
        a = a - 1;
      }

    }
  for(Move m : moves){
  }
  return moves;
  }
}