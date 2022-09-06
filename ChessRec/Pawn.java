package ChessRec;

import java.util.*;
public class Pawn extends Piece{
  public Pawn(int row, int col, String color, boolean p, boolean m){
    this.row = row;
    this.col = col;
    this.color = color;
    this.passant = p;
    this.moved = m;
    this.name = color.charAt(0) + "Pawn";
    this.value = 15;
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
      if((row - 1 < 8 && col - 1 > -1) && row - 1 > -1){
        if(board[row-1][col-1] != null && board[row-1][col-1].color != color){
          moves.add(new Move(this, row - 1, col - 1));
        }
      }
    }

    
    //account for en passant by going through all possible cases
    if(color == "white" && col == 0){
      if(checkPassant(board, row,col+1) && board[row-1][col+1] == null){
        moves.add(new Move(this, row-1, col+1));
      }
    }
    else if(color == "white" && col == 7){
      if(checkPassant(board, row,col-1)&& board[row-1][col-1] == null){
        moves.add(new Move(this, row-1, col-1));
      }
    }
    else if (color == "white"){
      if(checkPassant(board, row,col-1)&& board[row-1][col-1] == null){
        moves.add(new Move(this, row - 1, col - 1));
      }
      if(checkPassant(board, row,col+1)&& board[row-1][col+1] == null){
        moves.add(new Move(this, row - 1, col + 1));
      }
    }

    if(color == "black" && col == 0){
      if(checkPassant(board, row,col+1) && board[row+1][col+1] == null){
        moves.add(new Move(this, row+1, col+1));
      }
    }
    else if(color == "black" && col == 7){
      if(checkPassant(board, row,col-1)&& board[row+1][col-1] == null){
        moves.add(new Move(this, row+1, col-1));
      }
    }
    else if (color == "black"){
      if(checkPassant(board, row,col-1)&& board[row+1][col-1] == null){
        moves.add(new Move(this, row + 1, col - 1));
      }
      if(checkPassant(board, row,col+1)&& board[row+1][col+1] == null){
        moves.add(new Move(this, row + 1, col + 1));
      }
    }



    
  for(Move m : moves){
  }
  return moves;
  }

  public boolean checkPassant(Piece[][] board, int row1, int col1){ //function to check if a certain piece is in position for en passant
    if(board[row1][col1] != null){
      if (board[row1][col1].name.equals("wPawn")){
        if(board[row1][col1].passant == true){
          return true;
        }
      }
      if (board[row1][col1].name.equals("bPawn")){
        if(board[row1][col1].passant == true){
          return true;
        }
      }
    }
    else{
      return false;
    }
    return false;
  }


  
}