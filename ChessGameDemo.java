import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
//while gameover is false
class ChessGameDemo extends JFrame implements MouseListener, MouseMotionListener {

  JLayeredPane layeredPane;
  JPanel chessBoard;
  JLabel chessPiece;
  int xAdjustment;
  int yAdjustment;
  int pieceR = 0;
  int pieceC = 0;
  int playernum = 1;
  int kingrow = 0;
  int kingcol = 0;
  boolean gameOverW = false;
  boolean gameOverB = false;
  Piece[][] board;
  Piece WhiteKing;
  Piece BlackKing;
  ArrayList<Move> whitemoves;
  ArrayList<Move> blackmoves;

  ArrayList<Move> movelist;
  ArrayList<Move> movelist2;
  ArrayList<Move> movelist3;
  ArrayList<Move> masterlist;
  ArrayList<Move> allmovelist;
  public ChessGameDemo(){
  
  System.out.println("It is white's turn. "); 
  masterlist = new ArrayList <Move>();
  movelist = new ArrayList <Move>();
  movelist2 = new ArrayList <Move>();
  movelist3 = new ArrayList <Move>();
  allmovelist = new ArrayList <Move>();
  blackmoves = new ArrayList <Move>();
  Dimension boardSize = new Dimension(600, 600);
  //2D array
  board = new Piece[8][8];
  for(int j = 0; j < 8; j++){
    board[6][j] = new Pawn(6,j,"white");
  }
  for(int j = 0; j < 8; j++){
    board[1][j] = new Pawn(1,j,"black");
  }
  board[7][1] = new Knight(7,1,"white");
  board[7][6] = new Knight(7,6,"white");
  board[0][6] = new Knight(0,6,"black");
  board[0][1] = new Knight(0,1,"black");

  board[7][0] = new Rook(7,0,"white", false);
  board[7][7] = new Rook(7,7,"white", false);
  board[0][7] = new Rook(0,7,"black", false);
  board[0][0] = new Rook(0,0,"black", false);

  board[7][2] = new Bishop(7,2,"white");
  board[7][5] = new Bishop(7,5,"white");
  board[0][2] = new Bishop(0,2,"black");
  board[0][5] = new Bishop(0,5,"black");

  board[7][3] = new Queen(7,3,"white");
  board[0][3] = new Queen(0,3,"black");

  board[7][4] = new King(7,4,"white", false, false, false);
  board[0][4] = new King(0,4,"black", false, false, false);

  WhiteKing = board[7][4];
  BlackKing = board[0][4];
  
  // Layered Pane gives depth, allows to add items on top of chessboard
 layeredPane = new JLayeredPane();
  getContentPane().add(layeredPane);
  layeredPane.setPreferredSize(boardSize);
  layeredPane.addMouseListener(this);
  layeredPane.addMouseMotionListener(this);
 
  //Add the chess board to the Layered Pane 
 
  chessBoard = new JPanel();
  layeredPane.add(chessBoard, JLayeredPane.DEFAULT_LAYER);
  chessBoard.setLayout( new GridLayout(8, 8) );
  chessBoard.setPreferredSize( boardSize );
  chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);

  for (int i = 0; i < 64; i++) {
  JPanel square = new JPanel( new BorderLayout() );
  chessBoard.add( square );
 
  int row = (i / 8) % 2;
  if (row == 0)
  square.setBackground( i % 2 == 0 ? Color.blue : Color.white );
  else
  square.setBackground( i % 2 == 0 ? Color.white : Color.blue );
  } //if (i % 2) ? (if true make white) else (if false make blue)
    
  //placeholder to create piece and panel
  JLabel piece = new JLabel(new ImageIcon("bKnight.png"));
  JPanel panel = (JPanel)chessBoard.getComponent(0);
  //Add in all the pieces to make board
  for(int i = 0; i < 8; i++){
    for(int j = 0; j < 8; j++){
      if (board[i][j] != null){
        String piecename = board[i][j].name;
        int position = (i*8) + j;
        piece = new JLabel(new ImageIcon(piecename + ".png"));
        panel = (JPanel)chessBoard.getComponent(position);
        panel.add(piece);
        }
      
    }
  }
  masterlist = blackgetAllMoves(board);

 
  }

  public static void wait(int ms)
{
    try
    {
        Thread.sleep(ms);
    }
    catch(InterruptedException ex)
    {
        Thread.currentThread().interrupt();
    }
}
  public void yellowKingW(Piece[][] b){
    for (int i = 0; i < 8; i++){
      for (int j = 0; j < 8; j++){
        if(b[i][j] != null){
          if(b[i][j].name.equals("wKing")){
            int compnum = (i * 8) + j;
            JPanel template2 = (JPanel)chessBoard.getComponent(compnum);
            template2.setBackground(Color.yellow);
            break;
      }
        }

    }

  }
  }

  public void yellowKingB(Piece[][] b){
    for (int i = 0; i < 8; i++){
      for (int j = 0; j < 8; j++){
        if(b[i][j] != null){
          if(b[i][j].name.equals("bKing")){
            int compnum = (i * 8) + j;
            JPanel template2 = (JPanel)chessBoard.getComponent(compnum);
            template2.setBackground(Color.yellow);
            break;
      }
        }

    }

  }
  }

  public void redKingW(Piece[][] b){
    for (int i = 0; i < 8; i++){
      for (int j = 0; j < 8; j++){
        if(b[i][j] != null){
          if(b[i][j].name.equals("wKing")){
            int compnum = (i * 8) + j;
            JPanel template2 = (JPanel)chessBoard.getComponent(compnum);
            template2.setBackground(Color.red);
            break;
      }
        }

    }

  }
  }

  public void redKingB(Piece[][] b){
    for (int i = 0; i < 8; i++){
      for (int j = 0; j < 8; j++){
        if(b[i][j] != null){
          if(b[i][j].name.equals("bKing")){
            int compnum = (i * 8) + j;
            JPanel template2 = (JPanel)chessBoard.getComponent(compnum);
            template2.setBackground(Color.red);
            break;
      }
        }

    }

  }
  }

  public void grayKingW(Piece[][] b){
    for (int i = 0; i < 8; i++){
      for (int j = 0; j < 8; j++){
        if(b[i][j] != null){
          if(b[i][j].name.equals("wKing")){
            int compnum = (i * 8) + j;
            JPanel template2 = (JPanel)chessBoard.getComponent(compnum);
            template2.setBackground(Color.gray);
            break;
      }
        }

    }

  }
  }

  public void grayKingB(Piece[][] b){
    for (int i = 0; i < 8; i++){
      for (int j = 0; j < 8; j++){
        if(b[i][j] != null){
          if(b[i][j].name.equals("bKing")){
            int compnum = (i * 8) + j;
            JPanel template2 = (JPanel)chessBoard.getComponent(compnum);
            template2.setBackground(Color.gray);
            break;
      }
        }

    }

  }
  }


  public void mousePressed(MouseEvent e){
    if (gameOverW == true){
      wait(1000);
      System.exit(0);
    }
    if (gameOverB == true){
      wait(1000);
      System.exit(0);
    }
  masterlist = null;
  chessPiece = null;
  Component c =  chessBoard.findComponentAt(e.getX(), e.getY());
  
  for(int j = 0; j < 8; j ++){
    for(int i = 0; i < 8; i ++){
      JPanel reset = (JPanel)chessBoard.getComponent((i*8) + j);
      if(i % 2 == j % 2){
      reset.setBackground(Color.blue);
      }
      else{
      reset.setBackground(Color.white);
      }
    }
  }
  if (c instanceof JPanel) 
  return;
 
  Point parentLocation = c.getParent().getLocation();
  xAdjustment = parentLocation.x - e.getX();
  yAdjustment = parentLocation.y - e.getY();
  chessPiece = (JLabel)c;
  chessPiece.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
  chessPiece.setSize(chessPiece.getWidth(), chessPiece.getHeight());
  layeredPane.add(chessPiece, JLayeredPane.DRAG_LAYER);

  int Xcomp = e.getX() + xAdjustment;
  int Ycomp = e.getY() + yAdjustment;
  int ComponentNum = (Xcomp / 75) + 8 * (Ycomp / 75);


  if (board[Ycomp/75][Xcomp/75] != null){
    
    //allmovelist = WhiteGetAllSafeMoves(board);
      
    movelist = board[Ycomp/75][Xcomp/75].getMoves(board);

    if(playernum == 1 && board[Ycomp/75][Xcomp/75].color == "black"){
      movelist = null;
    }
    if(playernum == 0 && board[Ycomp/75][Xcomp/75].color == "white"){
      movelist = null;
    }
    /*
    if (board[Ycomp/75][Xcomp/75].color == "white" && movelist != null){
      int total = movelist.size();
      for(int m = 0; m < total; m ++){
        boolean check = false;
        int rowcheck = movelist.get(m).r;
        int colcheck = movelist.get(m).c;
        for(int x = 0; x < allmovelist.size(); x ++){
          int rowneeded = allmovelist.get(x).r;
          int colneeded = allmovelist.get(x).c;
          if (rowcheck == rowneeded && colcheck == colneeded){
            check = true;
            
            
          }
        }
        if (check == false){
          
          movelist.remove(movelist.get(m));
          total = total - 1;
          m = m - 1;

        }
      
      }
    }
    */
    
    if(movelist != null && board[Ycomp/75][Xcomp/75].color == "white"){ //account for checks on white king
      for (int i = 0; i < movelist.size(); i++){
        int temprow = movelist.get(i).r;
        int tempcol = movelist.get(i).c;
        int oldrow = movelist.get(i).piecemove.row; //Old positionr
        int oldcol = movelist.get(i).piecemove.col; //Old positionc
        Piece original = board[temprow][tempcol];
        board[temprow][tempcol] = board[oldrow][oldcol];
        board[temprow][tempcol].row = temprow;
        board[temprow][tempcol].col = tempcol;
        board[oldrow][oldcol] = null;
        masterlist = blackgetAllMoves(board);
        if (checkwhiteking(board) == true){
          movelist.remove(movelist.get(i));
          i -= 1;
        }
        board[oldrow][oldcol] = board[temprow][tempcol];
        board[oldrow][oldcol].row = oldrow;
        board[oldrow][oldcol].col = oldcol;
        board[temprow][tempcol] = original;
      }
    }
    if(movelist != null && board[Ycomp/75][Xcomp/75].color == "black"){ //account for checks on black king
      for (int i = 0; i < movelist.size(); i++){
        int temprow = movelist.get(i).r;
        int tempcol = movelist.get(i).c;
        int oldrow = movelist.get(i).piecemove.row; //Old positionr
        int oldcol = movelist.get(i).piecemove.col; //Old positionc
        Piece original = board[temprow][tempcol];
        board[temprow][tempcol] = board[oldrow][oldcol];
        board[temprow][tempcol].row = temprow;
        board[temprow][tempcol].col = tempcol;
        board[oldrow][oldcol] = null;
        masterlist = whitegetAllMoves(board);
        if (checkblackking(board) == true){
          movelist.remove(movelist.get(i));
          i -= 1;
        }
        board[oldrow][oldcol] = board[temprow][tempcol];
        board[oldrow][oldcol].row = oldrow;
        board[oldrow][oldcol].col = oldcol;
        board[temprow][tempcol] = original;
      }
    }
     
     
    //getting the moves
    pieceR = Ycomp/75;
    pieceC = Xcomp/75;
    if(movelist != null){
      for(int i = 0; i < movelist.size(); i ++){
        int newrow = movelist.get(i).r;
        int newcol = movelist.get(i).c;
        int compnum = (newrow * 8) + newcol;
        JPanel template = (JPanel)chessBoard.getComponent(compnum);
        if(newrow % 2 == newcol % 2){
          template.setBackground(Color.green.darker());
        }
        else{
          template.setBackground(Color.green);
        }
      }
    }

  }
  }
  
 
  //Move the chess piece around
  public ArrayList <Move> whitegetAllMoves(Piece[][] b){
    ArrayList <Move> supermasterlist = new ArrayList <Move>();
    for(int i = 0; i < 8; i++){
    for(int j = 0; j < 8; j ++){
      if(b[i][j] != null){
        if(b[i][j].color == "white"){
          movelist3 = b[i][j].getMoves(b);
          if(movelist3.size() > 0){
            for(int m = 0; m < movelist3.size(); m ++){
              supermasterlist.add(0,movelist3.get(m));
            }
          }
        }
      }
    }
  }
  return supermasterlist;
  }
  public ArrayList <Move> blackgetAllMoves(Piece[][] b){
    ArrayList <Move> supermasterlist = new ArrayList <Move>();
    for(int i = 0; i < 8; i++){
    for(int j = 0; j < 8; j ++){
      if(b[i][j] != null){
        if(b[i][j].color == "black"){
          movelist2 = b[i][j].getMoves(b);
          if(movelist2.size() > 0){
            for(int m = 0; m < movelist2.size(); m ++){
              supermasterlist.add(0,movelist2.get(m));
            }
          }
        }
      }
    }
  }
  return supermasterlist;
  }

  public boolean checkwhiteking(Piece[][] b){
    for(int k = 0; k < masterlist.size(); k ++){
      int possr = masterlist.get(k).r;
      int possc = masterlist.get(k).c;
      if(b[possr][possc] != null){
        if(b[possr][possc].name.equals("wKing")){
          return true;
        }
      }
      //if( WhiteKing.row == possr && WhiteKing.col == possc){
     //   return true;
     // }
    }
  return false;
  }
  public boolean checkblackking(Piece[][] b){
    for(int k = 0; k < masterlist.size(); k ++){
      int possr = masterlist.get(k).r;
      int possc = masterlist.get(k).c;
      if(b[possr][possc] != null){
        if(b[possr][possc].name.equals("bKing")){
          return true;
        }
      }
    }
  return false;
  }
//White in check
  public ArrayList <Move> WhiteGetAllSafeMoves(Piece[][] b){
    ArrayList <Move> tempwhitelist = whitegetAllMoves(b);
    ArrayList <Move> safelist = new ArrayList <Move>();
    for(int l = 0; l < tempwhitelist.size(); l ++ ){
      Piece[][] tempboard = new Piece[b.length][];
      for (int i = 0; i < b.length; i++) {
        tempboard[i] = Arrays.copyOf(b[i], b[i].length);
      }
      
      int tempr = tempwhitelist.get(l).r;  //New positionr
      int tempc = tempwhitelist.get(l).c; //New positionc

      int temppieceR = tempwhitelist.get(l).piecemove.row; //Old positionr
      int oldr = temppieceR;
      int temppieceC = tempwhitelist.get(l).piecemove.col; //Old positionc
      int oldc = temppieceC;

      tempboard[tempr][tempc] = tempboard[temppieceR][temppieceC]; //oldtempposition = newtempposition


      tempboard[tempr][tempc].row = tempr;
      tempboard[tempr][tempc].col = tempc;

      tempboard[temppieceR][temppieceC] = null;
      //masterlist = blackgetAllMoves(tempboard);
      ArrayList <Move> supertemplist = new ArrayList <Move>();
      for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j ++){
        if(tempboard[i][j] != null){
          if(tempboard[i][j].color == "black"){
            movelist = tempboard[i][j].getMoves(tempboard);
            if(movelist.size() > 0){
              for(int m = 0; m < movelist.size(); m ++){
                supertemplist.add(0,movelist.get(m));
              }
            }
          }
        }
      }
    }
      boolean check = false;
      for(int k = 0; k < supertemplist.size(); k ++){
        
            int possr = supertemplist.get(k).r;
            int possc = supertemplist.get(k).c;
            if(tempboard[possr][possc] != null){
              if(tempboard[possr][possc].name.equals("wKing")){
                check = true;
              }
              if(tempboard[possr][possc].name == "wKing"){
                check = true;
              }
            }
            //if( WhiteKing.row == possr && WhiteKing.col == possc){
           //   return true;
           // }
          }
      if (check == false){
        safelist.add(0, tempwhitelist.get(l));
      }
      








      
      //if(checkwhiteking(tempboard) == false){
       // safelist.add(0,tempwhitelist.get(l));
      //}
      tempboard[temppieceR][temppieceC] = tempboard[tempr][tempc];
      tempboard[temppieceR][temppieceC].row = temppieceR;
      tempboard[temppieceR][temppieceC].col = temppieceC;
      tempboard[tempr][tempc] = null;
      
    }
  return safelist;
  }

  public ArrayList<Move> WhiteGetSafeMoves(Piece[][] b){
    ArrayList<Move> templ = new ArrayList<Move>();
    ArrayList<Move> superlist = new ArrayList<Move>();
    for(int i = 0; i < 8; i++){
    for(int j = 0; j < 8; j ++){
      if(b[i][j] != null){
        if(b[i][j].color == "white"){
          templ = b[i][j].getMoves(b);
          if(templ.size() > 0){
            for(int m = 0; m < templ.size(); m ++){
              superlist.add(0,templ.get(m));
            }
          }
        }
      }
    }
      
      
  }
    for (int i = 0; i < superlist.size(); i++){
        int temprow = superlist.get(i).r;
        int tempcol = superlist.get(i).c;
        int oldrow = superlist.get(i).piecemove.row; //Old positionr
        int oldcol = superlist.get(i).piecemove.col; //Old positionc
        Piece original = b[temprow][tempcol];
        b[temprow][tempcol] = b[oldrow][oldcol];
        b[temprow][tempcol].row = temprow;
        b[temprow][tempcol].col = tempcol;
        b[oldrow][oldcol] = null;
        masterlist = blackgetAllMoves(board);
        if (checkwhiteking(board) == true){
          superlist.remove(superlist.get(i));
          i -= 1;
        }
        b[oldrow][oldcol] = b[temprow][tempcol];
        b[oldrow][oldcol].row = oldrow;
        b[oldrow][oldcol].col = oldcol;
        b[temprow][tempcol] = original;
      }
    return superlist;
    
  }

public ArrayList<Move> BlackGetSafeMoves(Piece[][] b){
    ArrayList<Move> templ = new ArrayList<Move>();
    ArrayList<Move> superlist = new ArrayList<Move>();
    for(int i = 0; i < 8; i++){
    for(int j = 0; j < 8; j ++){
      if(b[i][j] != null){
        if(b[i][j].color == "black"){
          templ = b[i][j].getMoves(b);
          if(templ.size() > 0){
            for(int m = 0; m < templ.size(); m ++){
              superlist.add(0,templ.get(m));
            }
          }
        }
      }
    }
      
      
  }
    for (int i = 0; i < superlist.size(); i++){
        int temprow = superlist.get(i).r;
        int tempcol = superlist.get(i).c;
        int oldrow = superlist.get(i).piecemove.row; //Old positionr
        int oldcol = superlist.get(i).piecemove.col; //Old positionc
        Piece original = b[temprow][tempcol];
        b[temprow][tempcol] = b[oldrow][oldcol];
        b[temprow][tempcol].row = temprow;
        b[temprow][tempcol].col = tempcol;
        b[oldrow][oldcol] = null;
        masterlist = whitegetAllMoves(board);
        if (checkblackking(board) == true){
          superlist.remove(superlist.get(i));
          i -= 1;
        }
        b[oldrow][oldcol] = b[temprow][tempcol];
        b[oldrow][oldcol].row = oldrow;
        b[oldrow][oldcol].col = oldcol;
        b[temprow][tempcol] = original;
      }
    return superlist;
    
  }






  


  public void mouseDragged(MouseEvent me) {
    
  if (chessPiece == null) return;


 chessPiece.setLocation(me.getX() + xAdjustment, me.getY() + yAdjustment);
  
 }
 
  //Drop the chess piece back onto the chess board
 
  public void mouseReleased(MouseEvent e) {
  
  if(chessPiece == null) return;

  chessPiece.setVisible(false);
  Component c =  chessBoard.findComponentAt(e.getX(), e.getY());
  chessPiece.setVisible(true);
 

  int Xcomp = e.getX();
  int Ycomp = e.getY();
  //int ComponentNum = (Xcomp / 75) + 8 * (Ycomp / 75);

  int C = Xcomp / 75;
  int r = Ycomp / 75;
    
 //check to make sure piece being clicked is right color
  if (playernum == 1 && board[pieceR][pieceC].color == "black"){
    movelist = null;
  }
  else if (playernum == 0 && board[pieceR][pieceC].color == "white"){
    movelist = null;
  }
  
  if(movelist != null){
    for(int a = 0; a < movelist.size(); a ++){
      int possrow = movelist.get(a).r;
      int posscol = movelist.get(a).c;
      if(r == possrow && C == posscol){
        board[r][C] = null;
        if (c instanceof JLabel){
          Container parent = c.getParent();
          parent.remove(0);
          parent.add( chessPiece );
          }
        else {
        Container parent = (Container)c;
        parent.add( chessPiece );
        }
        chessPiece.setVisible(true);
        
       

        
        board[r][C] = board[pieceR][pieceC];
        board[r][C].row = r;
        board[r][C].col = C;
        board[pieceR][pieceC] = null;
          //check for castling requirement
        if (board[r][C].name.equals("wRook")){
          board[r][C].moved = true;
        }
        if ((board[r][C].name.equals("wKing")) && board[r][C].moved == false){ //if king moved, and hasnt moved until now
          if (r == 7 && C == 6){
            board[r][C].moved = true;
            board[r][C].castle = true;
   
             JLabel piece2 = new JLabel(new ImageIcon("bKnight.png"));
            JPanel panel2 = (JPanel)chessBoard.getComponent(0);
            board[7][5] = null;
            piece2 = new JLabel(new ImageIcon("wRook.png"));
            panel2 = (JPanel)chessBoard.getComponent(61);
            panel2.add(piece2);

            panel2 = (JPanel)chessBoard.getComponent(63);
            panel2.removeAll();
            panel2.revalidate();
            panel2.repaint();

            
            
  
          
         
  
          
          board[7][5] = board[7][7];
          board[7][5].row = 7;
          board[7][5].col = 5;
          board[7][7] = null;
          }
          
          else if (r == 7 && C == 2){
            board[r][C].moved = true;
            board[r][C].castle = true;

            JLabel piece2 = new JLabel(new ImageIcon("bKnight.png"));
            JPanel panel2 = (JPanel)chessBoard.getComponent(0);
            board[7][3] = null;
            piece2 = new JLabel(new ImageIcon("wRook.png"));
            panel2 = (JPanel)chessBoard.getComponent(59);
            panel2.add(piece2);

            panel2 = (JPanel)chessBoard.getComponent(56);
            panel2.removeAll();
            panel2.revalidate();
            panel2.repaint();

          board[7][3] = board[7][0];
          board[7][3].row = 7;
          board[7][3].col = 3;
          board[7][0] = null;
          }
          else{
            board[r][C].moved = true;
          }
          


          
        }

      if (board[r][C].name.equals("bRook")){
          board[r][C].moved = true;
        }
        if ((board[r][C].name.equals("bKing")) && board[r][C].moved == false){ //if king moved, and hasnt moved until now
          if (r == 0 && C == 6){
            board[r][C].moved = true;
            board[r][C].castle = true;
   
             JLabel piece2 = new JLabel(new ImageIcon("bKnight.png"));
            JPanel panel2 = (JPanel)chessBoard.getComponent(0);
            board[0][5] = null;
            piece2 = new JLabel(new ImageIcon("bRook.png"));
            panel2 = (JPanel)chessBoard.getComponent(5);
            panel2.add(piece2);

            panel2 = (JPanel)chessBoard.getComponent(7);
            panel2.removeAll();
            panel2.revalidate();
            panel2.repaint();

            
            
  
          
         
  
          
          board[0][5] = board[0][7];
          board[0][5].row = 0;
          board[0][5].col = 5;
          board[0][7] = null;
          }
          
          else if (r == 0 && C == 2){
            board[r][C].moved = true;
            board[r][C].castle = true;

            JLabel piece2 = new JLabel(new ImageIcon("bKnight.png"));
            JPanel panel2 = (JPanel)chessBoard.getComponent(0);
            board[0][3] = null;
            piece2 = new JLabel(new ImageIcon("bRook.png"));
            panel2 = (JPanel)chessBoard.getComponent(3);
            panel2.add(piece2);

            panel2 = (JPanel)chessBoard.getComponent(0);
            panel2.removeAll();
            panel2.revalidate();
            panel2.repaint();

          board[0][3] = board[0][0];
          board[0][3].row = 0;
          board[0][3].col = 0;
          board[0][0] = null;
          }
          else{
            board[r][C].moved = true;
          }

        }





        
        
        for(int j = 0; j < 8; j ++){
          for(int i = 0; i < 8; i ++){
            JPanel reset = (JPanel)chessBoard.getComponent((i*8) + j);
            if(i % 2 == j % 2){
            reset.setBackground(Color.blue);
            }
            else{
            reset.setBackground(Color.white);
            }
          }
        }
        if(playernum == 1){
          boolean checkk = false; // r we in check?
          boolean limit = false; //r there no moves left?
          playernum = 0;
          System.out.println("\n---------------\n");
          System.out.println("It is now black's turn.");
          allmovelist = BlackGetSafeMoves(board);
          if(allmovelist.size() < 1){
            limit = true;
          }
          masterlist = whitegetAllMoves(board);
          if(checkblackking(board) == true){
            checkk = true;
            for (int i = 0; i < 8; i++){ //set parameter
              for (int j = 0; j < 8; j++){
                if(board[i][j] != null){
                  if(board[i][j].name.equals("bKing")){
                    board[i][j].attack = true;
          }
                }
              }
            }}
          else{
            for (int i = 0; i < 8; i++){ //set parameter
              for (int j = 0; j < 8; j++){
                if(board[i][j] != null){
                  if(board[i][j].name.equals("bKing")){
                    board[i][j].attack = false;
          }
                }
              }
            }
          }

          if (checkk == true && limit == true){ //no moves, in check
            System.out.println("\n---White Wins by checkmate!---");
            redKingB(board);
            boolean gameOverW = true;
          }
          else if (checkk == true && limit == false){ //moves, in check
            System.out.println("\n---Black is in check!---");
            yellowKingB(board);
          }
          else if (checkk == false && limit == true){
            System.out.println("\n---Stalemate; it's a draw!");
            grayKingB(board);
            grayKingW(board);
          }
        }
        else{
          boolean checkk = false; // r we in check?
          boolean limit = false; //r there no moves left?
          playernum = 1;
          System.out.println("\n---------------\n");
          System.out.println("It is now white's turn.");
          allmovelist = WhiteGetSafeMoves(board);
          if(allmovelist.size() < 1){
            limit = true;
          }
          masterlist = blackgetAllMoves(board);
          if(checkwhiteking(board) == true){
            checkk = true;
            for (int i = 0; i < 8; i++){ //set parameter
              for (int j = 0; j < 8; j++){
                if(board[i][j] != null){
                  if(board[i][j].name.equals("wKing")){
                    board[i][j].attack = true;
          }
                }
              }
            }}
          else{
            for (int i = 0; i < 8; i++){ //set parameter
              for (int j = 0; j < 8; j++){
                if(board[i][j] != null){
                  if(board[i][j].name.equals("wKing")){
                    board[i][j].attack = false;
          }
                }
              }
            }
          }

          if (checkk == true && limit == true){ //no moves, in check
            System.out.println("\n---Black Wins by checkmate!---");
            redKingW(board);
            boolean gameOverB = true;
          }
          else if (checkk == true && limit == false){ //moves, in check
            System.out.println("\n---White is in check!---");
            yellowKingW(board);
          }
          else if (checkk == false && limit == true){
            System.out.println("\n---Stalemate; it's a draw!");
            grayKingB(board);
            grayKingW(board);
          }
        }
        return;
      }
      }
  }





  JPanel goback = (JPanel)chessBoard.getComponent(pieceR * 8 + pieceC);
  chessPiece.setVisible(true);
  goback.add(chessPiece);



  }
  
  public void mouseClicked(MouseEvent e){

  }
  public void mouseMoved(MouseEvent e){

  }
  public void mouseEntered(MouseEvent e){

  }
  public void mouseExited(MouseEvent e){

  }

  
 

 
}

 