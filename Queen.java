import java.util.*;
public class Queen extends Piece{
  public Queen(int row, int col, String color){
    this.row = row;
    this.col = col;
    this.color = color;
    this.name = color.charAt(0) + "Queen";
  }
  public ArrayList<Move> getMoves(Piece[][]board){
    ArrayList<Move> moves = new ArrayList<Move>();

    for(int i = 1; i < 8; i++){
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
    for(int i = 1; i < 8; i++){
      if(col + i < 8 && row + i < 8){

        if(board[row+i][col+i] != null){

          if(board[row+i][col+i].color != color){
            
            moves.add(new Move(this, row + i, col + i));
            break;
          }
          else{

            break;
          }
        }
        else{

          moves.add(new Move(this, row + i, col + i));
        }
      }
      else{
        break;
      }
    }
    for(int i = 1; i < 8; i++){
      if(col + i < 8 && row - i > -1){

        if(board[row-i][col+i] != null){

          if(board[row-i][col+i].color != color){
            
            moves.add(new Move(this, row - i, col + i));
            break;
          }
          else{

            break;
          }
        }
        else{

          moves.add(new Move(this, row - i, col + i));
        }
      }
      else{
        break;
      }
    }
    for(int i = 1; i < 8; i++){
      if(col - i > -1 && row - i > -1){

        if(board[row-i][col-i] != null){

          if(board[row-i][col-i].color != color){
            
            moves.add(new Move(this, row - i, col - i));
            break;
          }
          else{

            break;
          }
        }
        else{

          moves.add(new Move(this, row - i, col - i));
        }
      }
      else{
        break;
      }
    }
    for(int i = 1; i < 8; i++){
      if(col - i > -1 && row + i < 8){

        if(board[row+i][col-i] != null){

          if(board[row+i][col-i].color != color){

            moves.add(new Move(this, row + i, col - i));
            break;
          }
          else{

            break;
          }
        }
        else{

          moves.add(new Move(this, row + i, col - i));
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