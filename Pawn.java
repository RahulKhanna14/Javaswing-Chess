import java.util.*;
public class Pawn extends Piece{
  public Pawn(int row, int col, String color){
    this.row = row;
    this.col = col;
    this.color = color;
    this.name = color.charAt(0) + "Pawn";
  }
  public ArrayList<Move> getMoves(Piece[][]board){
    ArrayList<Move> moves = new ArrayList<Move>();
    if(board[row][col].color == "black"){
      if(row == 1){
        if(board[row+1][col] == null){
          moves.add(new Move(this, row+1, col));
          if(board[row+2][col] == null){
            moves.add(new Move(this, row+2, col));
          }
        }

      }
      else if(row + 1 < 8){
        if(board[row+1][col] == null){
          moves.add(new Move(this, row+1, col));
        }
      }
      if(row + 1 < 8 && col + 1 < 8){
        if(board[row+1][col+1] != null && board[row+1][col+1].color != color){
          moves.add(new Move(this, row + 1, col + 1));
        }
      }
      if(row + 1 < 8 && col - 1 > -1){
        if(board[row+1][col-1] != null && board[row+1][col-1].color != color){
          moves.add(new Move(this, row + 1, col - 1));
        }
      }
    }
    else{
      if(row == 6){
        if(board[row-1][col] == null){
          moves.add(new Move(this, row-1, col));
          if(board[row-2][col] == null){
            moves.add(new Move(this, row-2, col));
          }
        }

      }
      else if(row - 1 > -1){
        if(board[row-1][col] == null){
          moves.add(new Move(this, row-1, col));
        }
      }
      if(row - 1 > -1 && col + 1 < 8){
        if(board[row-1][col+1] != null && board[row-1][col+1].color != color){
          moves.add(new Move(this, row - 1, col + 1));
        }
      }
      if(row - 1 < 8 && col - 1 > -1){
        if(board[row-1][col-1] != null && board[row-1][col-1].color != color){
          moves.add(new Move(this, row - 1, col - 1));
        }
      }
    }
  for(Move m : moves){
  }
  return moves;
  }
}