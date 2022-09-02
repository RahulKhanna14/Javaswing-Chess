import java.util.*;

public class King extends Piece {

  public King(int row, int col, String color, boolean m, boolean c, boolean att) {
    this.row = row;
    this.col = col;
    this.color = color;
    this.name = color.charAt(0) + "King";
    this.castle = c;
    this.moved = m;
    this.attack = att;
    this.value = 30;
  }

  public ArrayList<Move> getMoves(Piece[][] board) { //iterate through all 8 directions and add valid moves
    ArrayList<Move> moves = new ArrayList<Move>();
    for (int i = -1; i < 2; i++) {
      for (int j = -1; j < 2; j++) {

        if (row + i < 8 & row + i > -1) {
          if (col + j < 8 & col + j > -1) {
            if (board[row + i][col + j] != null) {
              if (board[row + i][col + j].color != color) {
                moves.add(new Move(this, row + i, col + j));
              }
            } else {
              moves.add(new Move(this, row + i, col + j));
            }
          }
        }
      }
    }

    if (board[row][col].moved == false && board[row][col].attack == false) {
      if(col + 3 < 8){
      if ((board[row][col + 1] == null && board[row][col + 2] == null) && board[row][col+3] != null) {
        if ((board[row][col+3].name.equals("wRook") || board[row][col+3].name.equals("bRook")) && board[row][col + 3].moved == false) {
          moves.add(new Move(this, row, col + 2));
        }
      }}
      if(col - 4 >= 0){
      if ((board[row][col - 1] == null && board[row][col - 2] == null) && board[row][col-4] != null){
        if (board[row][col - 3] == null && (board[row][col-4].name.equals("wRook") || board[row][col-4].name.equals("bRook"))) {
          if (board[row][col - 4].moved == false){
          moves.add(new Move(this, row, col - 2));
        }}
      }
      }

    }

    for (Move m : moves) {
    }
    return moves;
  }

}
