public class Move {
  public Piece piecemove;
  public int r = 0;
  public int c = 0;
  public Move(Piece piecemove, int r, int c){
    this.piecemove = piecemove;
    this.r = r;
    this.c = c;
  }

  public String toString(){
    String a = piecemove.name + "(" + r + "," + c + ")";
    return a;



  }



}