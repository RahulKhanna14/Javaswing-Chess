package ChessRec;

import java.util.*;
public class Rook extends Piece{
  public Rook(int row, int col, String color, boolean m){
    this.row = row;
    this.col = col;
    this.color = color;
    this.name = color.charAt(0) + "Rook";
    this.moved = m;
    this.value = 120;

  }
  public ArrayList<Move> getMoves(Piece[][]board){
    ArrayList<Move> moves = new ArrayList<Move>();
    for(int i = 1; i < 8; i++){ //add all possible moves for rook, assuming not invalid, and stop once another piece is reached
      if(col + i < 8){

        if(board[row][col+i] != null){

          if(board[row][col+i].color != color){

            moves.add(new Move(this, row, col + i));
            break;
          }
          else{

            break;
          }
        }
        else{

          moves.add(new Move(this, row, col + i));
        }
      }
      else{
        break;
      }
    }
    for(int i = 1; i < 8; i++){
      if(col - i > -1){
        if(board[row][col-i] != null){
          if(board[row][col-i].color != color){
            moves.add(new Move(this, row, col - i));
            break;
          }
          else{
            break;
          }
        }
        else{
          moves.add(new Move(this, row, col - i));
        }
      }
      else{
        break;
      }
    }
    for(int i = 1; i < 8; i++){
      if(row + i < 8){
        if(board[row+i][col] != null){
          if(board[row+i][col].color != color){
            moves.add(new Move(this, row+i, col));
            break;
          }
          else{
            break;
          }
        }
        else{
          moves.add(new Move(this, row+i, col));
        }
      }
      else{
        break;
      }
    }
    for(int i = 1; i < 8; i++){
      if(row - i > -1){
        if(board[row-i][col] != null){
          if(board[row-i][col].color != color){
            moves.add(new Move(this, row-i, col));
            break;
          }
          else{
            break;
          }
        }
        else{
          moves.add(new Move(this, row-i, col));
        }
      }
      else{
        break;
      }
    }
  for(Move m : moves){
  }
  return moves;
  }
}