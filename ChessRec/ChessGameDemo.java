package ChessRec;

import ChessRec.Move;
import ChessRec.Piece;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

//while gameover is false
public class ChessGameDemo extends JFrame implements MouseListener, MouseMotionListener {

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
  boolean gameOver = false;

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

  public ChessGameDemo() {

    System.out.println("It is white's turn. ");
    masterlist = new ArrayList<Move>();
    movelist = new ArrayList<Move>();
    movelist2 = new ArrayList<Move>();
    movelist3 = new ArrayList<Move>();
    allmovelist = new ArrayList<Move>();
    blackmoves = new ArrayList<Move>();
    Dimension boardSize = new Dimension(600, 600);
    // 2D array
    board = new Piece[8][8];
    for (int j = 0; j < 8; j++) { // initialize board with backend pieces; store values to be accessed later
      board[6][j] = new Pawn(6, j, "white", false, false);
    }
    for (int j = 0; j < 8; j++) {
      board[1][j] = new Pawn(1, j, "black", false, false);
    }
    board[7][1] = new Knight(7, 1, "white");
    board[7][6] = new Knight(7, 6, "white");
    board[0][6] = new Knight(0, 6, "black");
    board[0][1] = new Knight(0, 1, "black");

    board[7][0] = new Rook(7, 0, "white", false);
    board[7][7] = new Rook(7, 7, "white", false);
    board[0][7] = new Rook(0, 7, "black", false);
    board[0][0] = new Rook(0, 0, "black", false);

    board[7][2] = new Bishop(7, 2, "white");
    board[7][5] = new Bishop(7, 5, "white");
    board[0][2] = new Bishop(0, 2, "black");
    board[0][5] = new Bishop(0, 5, "black");

    board[7][3] = new Queen(7, 3, "white");
    board[0][3] = new Queen(0, 3, "black");

    board[7][4] = new King(7, 4, "white", false, false, false);
    board[0][4] = new King(0, 4, "black", false, false, false);

    WhiteKing = board[7][4];
    BlackKing = board[0][4];

    // Layered Pane gives depth, allows to add items on top of chessboard
    layeredPane = new JLayeredPane();
    getContentPane().add(layeredPane);
    layeredPane.setPreferredSize(boardSize);
    layeredPane.addMouseListener(this);
    layeredPane.addMouseMotionListener(this);

    // Add the chess board to the Layered Pane

    chessBoard = new JPanel();
    layeredPane.add(chessBoard, JLayeredPane.DEFAULT_LAYER);
    chessBoard.setLayout(new GridLayout(8, 8));
    chessBoard.setPreferredSize(boardSize);
    chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);

    for (int i = 0; i < 64; i++) {
      JPanel square = new JPanel(new BorderLayout());
      chessBoard.add(square);

      int row = (i / 8) % 2;
      if (row == 0)
        square.setBackground(i % 2 == 0 ? Color.blue : Color.white);
      else
        square.setBackground(i % 2 == 0 ? Color.white : Color.blue);
    } // if (i % 2) ? (if true make white) else (if false make blue)

    // placeholder to create piece and panel
    JLabel piece = new JLabel(new ImageIcon("bKnight.png"));
    JPanel panel = (JPanel) chessBoard.getComponent(0);
    // Add in all the pieces to make board
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (board[i][j] != null) {
          String piecename = board[i][j].name;
          int position = (i * 8) + j;
          piece = new JLabel(new ImageIcon("ChessRec/" + piecename + ".png"));
          panel = (JPanel) chessBoard.getComponent(position);
          panel.add(piece);
        }

      }
    }
    masterlist = blackgetAllMoves(board); // initializes masterlist before proceeding
  }

  public static void wait(int ms) // function to delay timer by ms seconds
  {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
    }
  }

  public void colorKing(Piece[][] b, String playerCol, String color) {// Input w or b for white/black, as well as
                                                                      // desired color. Changes king's square to that
                                                                      // color
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (b[i][j] != null) {
          if (b[i][j].name.equals(playerCol + "King")) {
            int compnum = (i * 8) + j;
            JPanel template2 = (JPanel) chessBoard.getComponent(compnum);
            if (color == "yellow") {
              template2.setBackground(Color.yellow);
            } else if (color == "red") {
              template2.setBackground(Color.red);
            } else {
              template2.setBackground(Color.gray);
            }
            break;
          }
        }

      }

    }
  }

  public void mousePressed(MouseEvent e) { // the moment player clicks
    if (gameOver == true) {
      wait(1000);
      System.exit(0);
    }
    masterlist = null;
    chessPiece = null;
    Component c = chessBoard.findComponentAt(e.getX(), e.getY());

    for (int j = 0; j < 8; j++) { // reset the colors of board, double for loop
      for (int i = 0; i < 8; i++) {
        JPanel reset = (JPanel) chessBoard.getComponent((i * 8) + j);
        if (i % 2 == j % 2) {
          reset.setBackground(Color.blue);
        } else {
          reset.setBackground(Color.white);
        }
      }
    }
    if (c instanceof JPanel)
      return;

    Point parentLocation = c.getParent().getLocation(); // set piece to mouse location, use xAdjustment and yAdjustment
                                                        // to adjust for variation from center
    xAdjustment = parentLocation.x - e.getX();
    yAdjustment = parentLocation.y - e.getY();
    chessPiece = (JLabel) c;
    chessPiece.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
    chessPiece.setSize(chessPiece.getWidth(), chessPiece.getHeight());
    layeredPane.add(chessPiece, JLayeredPane.DRAG_LAYER);

    int Xcomp = e.getX() + xAdjustment;
    int Ycomp = e.getY() + yAdjustment;
    int ComponentNum = (Xcomp / 75) + 8 * (Ycomp / 75);

    if (board[Ycomp / 75][Xcomp / 75] != null) { // if player clicks on valid piece, get all moves and then check if
                                                 // right color

      movelist = board[Ycomp / 75][Xcomp / 75].getMoves(board);

      if (playernum == 1 && board[Ycomp / 75][Xcomp / 75].color == "black") {
        movelist = null;
      }
      if (playernum == 0 && board[Ycomp / 75][Xcomp / 75].color == "white") {
        movelist = null;
      }

      if (movelist != null && board[Ycomp / 75][Xcomp / 75].color == "white") { // account for checks on white king
        for (int i = 0; i < movelist.size(); i++) { // temporarily try all moves, see if it results in a check, and if
                                                    // so, remove it. Then return to current game state
          int temprow = movelist.get(i).r;
          int tempcol = movelist.get(i).c;
          int oldrow = movelist.get(i).piecemove.row;
          int oldcol = movelist.get(i).piecemove.col;
          Piece original = board[temprow][tempcol];
          board[temprow][tempcol] = board[oldrow][oldcol];
          board[temprow][tempcol].row = temprow;
          board[temprow][tempcol].col = tempcol;
          board[oldrow][oldcol] = null;
          masterlist = blackgetAllMoves(board);
          if (checkwhiteking(board) == true) { // if white king checked, remove the "move" being tried
            movelist.remove(movelist.get(i));
            i -= 1;
          }
          board[oldrow][oldcol] = board[temprow][tempcol];
          board[oldrow][oldcol].row = oldrow;
          board[oldrow][oldcol].col = oldcol;
          board[temprow][tempcol] = original;
        }
      }
      if (movelist != null && board[Ycomp / 75][Xcomp / 75].color == "black") { // account for checks on black king
        for (int i = 0; i < movelist.size(); i++) {
          int temprow = movelist.get(i).r;
          int tempcol = movelist.get(i).c;
          int oldrow = movelist.get(i).piecemove.row; // Old positionr
          int oldcol = movelist.get(i).piecemove.col; // Old positionc
          Piece original = board[temprow][tempcol];
          board[temprow][tempcol] = board[oldrow][oldcol];
          board[temprow][tempcol].row = temprow;
          board[temprow][tempcol].col = tempcol;
          board[oldrow][oldcol] = null;
          masterlist = whitegetAllMoves(board);
          if (checkblackking(board) == true) {
            movelist.remove(movelist.get(i));
            i -= 1;
          }
          board[oldrow][oldcol] = board[temprow][tempcol];
          board[oldrow][oldcol].row = oldrow;
          board[oldrow][oldcol].col = oldcol;
          board[temprow][tempcol] = original;
        }
      }

      // With move list now finalized, mark valid moves with green color
      pieceR = Ycomp / 75;
      pieceC = Xcomp / 75;
      if (movelist != null) {
        for (int i = 0; i < movelist.size(); i++) {
          int newrow = movelist.get(i).r;
          int newcol = movelist.get(i).c;
          int compnum = (newrow * 8) + newcol;
          JPanel template = (JPanel) chessBoard.getComponent(compnum);
          if (newrow % 2 == newcol % 2) {
            template.setBackground(Color.green.darker());
          } else {
            template.setBackground(Color.green);
          }
        }
      }

    }
  } // the moment player clicks

  // Get all possible moves for white and black
  public ArrayList<Move> whitegetAllMoves(Piece[][] b) {
    ArrayList<Move> supermasterlist = new ArrayList<Move>();
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (b[i][j] != null) {
          if (b[i][j].color == "white") {
            movelist3 = b[i][j].getMoves(b);
            if (movelist3.size() > 0) {
              for (int m = 0; m < movelist3.size(); m++) {
                supermasterlist.add(0, movelist3.get(m));
              }
            }
          }
        }
      }
    }
    return supermasterlist;
  }

  public ArrayList<Move> blackgetAllMoves(Piece[][] b) {
    ArrayList<Move> supermasterlist = new ArrayList<Move>();
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (b[i][j] != null) {
          if (b[i][j].color == "black") {
            movelist2 = b[i][j].getMoves(b);
            if (movelist2.size() > 0) {
              for (int m = 0; m < movelist2.size(); m++) {
                supermasterlist.add(0, movelist2.get(m));
              }
            }
          }
        }
      }
    }
    return supermasterlist;
  }

  // functions to evaluate whether white/black are in check
  public boolean checkwhiteking(Piece[][] b) {
    for (int k = 0; k < masterlist.size(); k++) {
      int possr = masterlist.get(k).r;
      int possc = masterlist.get(k).c;
      if (b[possr][possc] != null) {
        if (b[possr][possc].name.equals("wKing")) {
          return true;
        }
      }
      // if( WhiteKing.row == possr && WhiteKing.col == possc){
      // return true;
      // }
    }
    return false;
  }

  public boolean checkblackking(Piece[][] b) {
    for (int k = 0; k < masterlist.size(); k++) {
      int possr = masterlist.get(k).r;
      int possc = masterlist.get(k).c;
      if (b[possr][possc] != null) {
        if (b[possr][possc].name.equals("bKing")) {
          return true;
        }
      }
    }
    return false;
  }

  // functions to obtain all possible "safe" moves by accounting for check, etc.
  // using the above functions
  public ArrayList<Move> WhiteGetSafeMoves(Piece[][] b) {
    ArrayList<Move> templ = new ArrayList<Move>();
    ArrayList<Move> superlist = new ArrayList<Move>();
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (b[i][j] != null) {
          if (b[i][j].color == "white") {
            templ = b[i][j].getMoves(b);
            if (templ.size() > 0) {
              for (int m = 0; m < templ.size(); m++) {
                superlist.add(0, templ.get(m));
              }
            }
          }
        }
      }

    }
    for (int i = 0; i < superlist.size(); i++) {
      int temprow = superlist.get(i).r;
      int tempcol = superlist.get(i).c;
      int oldrow = superlist.get(i).piecemove.row; // Old positionr
      int oldcol = superlist.get(i).piecemove.col; // Old positionc
      Piece original = b[temprow][tempcol];
      b[temprow][tempcol] = b[oldrow][oldcol];
      b[temprow][tempcol].row = temprow;
      b[temprow][tempcol].col = tempcol;
      b[oldrow][oldcol] = null;
      masterlist = blackgetAllMoves(board);
      if (checkwhiteking(board) == true) {
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

  public ArrayList<Move> BlackGetSafeMoves(Piece[][] b) {
    ArrayList<Move> templ = new ArrayList<Move>();
    ArrayList<Move> superlist = new ArrayList<Move>();
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (b[i][j] != null) {
          if (b[i][j].color == "black") {
            templ = b[i][j].getMoves(b);
            if (templ.size() > 0) {
              for (int m = 0; m < templ.size(); m++) {
                superlist.add(0, templ.get(m));
              }
            }
          }
        }
      }

    }
    for (int i = 0; i < superlist.size(); i++) {
      int temprow = superlist.get(i).r;
      int tempcol = superlist.get(i).c;
      int oldrow = superlist.get(i).piecemove.row; // Old positionr
      int oldcol = superlist.get(i).piecemove.col; // Old positionc
      Piece original = b[temprow][tempcol];
      b[temprow][tempcol] = b[oldrow][oldcol];
      b[temprow][tempcol].row = temprow;
      b[temprow][tempcol].col = tempcol;
      b[oldrow][oldcol] = null;
      masterlist = whitegetAllMoves(board);
      if (checkblackking(board) == true) {
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

  public double posMultiplier(int row, int col, Piece p){ //give multiplier value based on position within chess board, for each piece
    double val = 1.0;
    if(p.name.contains("King")){
      if (((row == 7) && (col == 6)) || ((row == 7) && (col == 2))){
        return 2.5;
      }
      if (((row == 0) && (col == 6)) || ((row == 0) && (col == 2))){
        return 2.5;
      } //the above 2 make castling desirable
      if (p.name.equals("wKing")){
        if(row < 7){
          return (0.12*row);
        }
      }
      else{
        if(row > 0){
          return 1-(0.12*(row));
        }
      }
    }
      
    else if (p.name.contains("Pawn")){
      if (p.name.contains("wPawn")){
        if (row >= 4){ //max 0.2 in middle
          val += 0.1*(6-row);
        }
        else if (row >= 2){
          val += 0.1;
        }
        else if (row == 1){
          val += 0.5;
        }
        else{
          val += 15;
        }
      }
      else{
        if (row <= 3){ //max 0.2 in middle
          val += 0.1*(6-row);
        }
        else if (row <= 5){
          val += 0.1;
        }
        else if (row == 6){
          val += 0.5;
        }
        else{
          val += 15;
        }
      }
    }
      
    else{
      val += (0.55 - (0.1 * Math.abs(4-row)));
      val += (0.55 - (0.1 * Math.abs(4-col)));
    }
    return val;
  
  } //multiplier to account for board position

  public double posMultiplier2(int row, int col, Piece p){
    double val = 1.0;
    if(p.name.contains("King")) {
      int[] kingstable = {
      20, 30, 10, 0, 0, 10, 30, 20,
              20, 20, 0, 0, 0, 0, 20, 20,
              -10, -20, -20, -20, -20, -20, -20, -10,
              -20, -30, -30, -40, -40, -30, -30, -20,
              -30, -40, -40, -50, -50, -40, -40, -30,
              -30, -40, -40, -50, -50, -40, -40, -30,
              -30, -40, -40, -50, -50, -40, -40, -30,
              -30, -40, -40, -50, -50, -40, -40, -30};
      if(p.name.contains("bKing")){
        return kingstable[8*row + col];
      }
      else{
        return kingstable[8*(7-row)+col];
      }
    }
    if(p.name.contains("Queen")) {
      int[] queenstable = {
      -20, -10, -10, -5, -5, -10, -10, -20,
              -10, 0, 0, 0, 0, 0, 0, -10,
              -10, 5, 5, 5, 5, 5, 0, -10,
              0, 0, 5, 5, 5, 5, 0, -5,
              -5, 0, 5, 5, 5, 5, 0, -5,
              -10, 0, 5, 5, 5, 5, 0, -10,
              -10, 0, 0, 0, 0, 0, 0, -10,
              -20, -10, -10, -5, -5, -10, -10, -20};
      if(p.name.contains("bQueen")){
        return queenstable[8*row + col];
      }
      else{
        return queenstable[8*(7-row)+col];
      }
    }
    if(p.name.contains("Rook")) {
      int[] rookstable = {
      0, 0, 0, 5, 5, 0, 0, 0,
              -5, 0, 0, 0, 0, 0, 0, -5,
              -5, 0, 0, 0, 0, 0, 0, -5,
              -5, 0, 0, 0, 0, 0, 0, -5,
              -5, 0, 0, 0, 0, 0, 0, -5,
              -5, 0, 0, 0, 0, 0, 0, -5,
              5, 10, 10, 10, 10, 10, 10, 5,
              0, 0, 0, 0, 0, 0, 0, 0};
      if(p.name.contains("bRook")){
        return rookstable[8*row + col];
      }
      else{
        return rookstable[8*(7-row)+col];
      }
    }
    if(p.name.contains("Bishop")) {
      int[] bishopstable = {
      -20, -10, -10, -10, -10, -10, -10, -20,
              -10, 5, 0, 0, 0, 0, 5, -10,
              -10, 10, 10, 10, 10, 10, 10, -10,
              -10, 0, 10, 10, 10, 10, 0, -10,
              -10, 5, 5, 10, 10, 5, 5, -10,
              -10, 0, 5, 10, 10, 5, 0, -10,
              -10, 0, 0, 0, 0, 0, 0, -10,
              -20, -10, -10, -10, -10, -10, -10, -20};
      if(p.name.contains("bBishop")){
        return bishopstable[8*row + col];
      }
      else{
        return bishopstable[8*(7-row)+col];
      }
    }
    if(p.name.contains("Knight")) {
      int[] knightstable = {
      -50, -40, -30, -30, -30, -30, -40, -50,
              -40, -20, 0, 5, 5, 0, -20, -40,
              -30, 5, 10, 15, 15, 10, 5, -30,
              -30, 0, 15, 20, 20, 15, 0, -30,
              -30, 5, 15, 20, 20, 15, 5, -30,
              -30, 0, 10, 15, 15, 10, 0, -30,
              -40, -20, 0, 0, 0, 0, -20, -40,
              -50, -40, -30, -30, -30, -30, -40, -50};
      if(p.name.contains("bKnight")){
        return knightstable[8*row + col];
      }
      else{
        return knightstable[8*(7-row)+col];
      }
    }
    if(p.name.contains("Pawn")) {
      int[] pawntable = {
      0, 0, 0, 0, 0, 0, 0, 0,
              5, 10, 10, -20, -20, 10, 10, 5,
              5, -5, -10, 0, 0, -10, -5, 5,
              0, 0, 0, 20, 20, 0, 0, 0,
              5, 5, 10, 25, 25, 10, 5, 5,
              10, 10, 20, 30, 30, 20, 10, 10,
              50, 50, 50, 50, 50, 50, 50, 50,
              0, 0, 0, 0, 0, 0, 0, 0};

      if(p.name.contains("bPawn")){
        return pawntable[8*row + col];
      }
      else{
        return pawntable[8*(7-row)+col];
      }
    }
    else{
      return 0;
    }

  }

  public double evalBoard(Piece[][] b){
    double total = 0;
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (b[i][j] != null && b[i][j].color == "white") {
          total -= ((2 * b[i][j].value) + (posMultiplier2(i, j, b[i][j])));
        } else if (b[i][j] != null && b[i][j].color == "black") {
          total += (2 * b[i][j].value) + (posMultiplier2(i, j, b[i][j]));
        }
      }
    }
    if (checkwhiteking(board)) {
      total += 10;
    }
    if (checkblackking(board)){
      total -= 10;
    }
    if (WhiteGetSafeMoves(board).size() == 0){
      total = 500000;
    }
    if (BlackGetSafeMoves(board).size() == 0){
      total = -500000;
    }
    return total;
  }

  public double evaluatePosition(Piece[][] b, Move m, int depth) {
    double min = 99999;
    boolean mate = false;
    int newrow = m.r;
    int newcol = m.c;
    int oldrow = m.piecemove.row;
    int oldcol = m.piecemove.col;

    Piece original = b[newrow][newcol];
    b[newrow][newcol] = b[oldrow][oldcol];
    b[newrow][newcol].row = newrow;
    b[newrow][newcol].col = newcol;
    b[oldrow][oldcol] = null;
    double total = 0;

    if (depth == 1) {
      ArrayList<Move> whiteSpace = WhiteGetSafeMoves(board);
      if (whiteSpace.size() == 0) { //check for mate
        mate = true;
      }
      if (mate != true) {
        for (int k = 0; k < whiteSpace.size(); k++) {
          double tempV = evaluatePosition(b, whiteSpace.get(k), 2);
          if (tempV < min) {
            min = tempV;
          }
        }
      }}
      if(mate != true){

        for (int i = 0; i < 8; i++) {
          for (int j = 0; j < 8; j++) {
            if (b[i][j] != null && b[i][j].color == "white") {
              total -= ((b[i][j].value) + (posMultiplier2(i, j, b[i][j])));
            } else if (b[i][j] != null && b[i][j].color == "black") {
              total += (b[i][j].value) + (posMultiplier2(i, j, b[i][j]));
            }
          }
        }
        if (checkwhiteking(board)) {
          total += 10;
        }
      }


      b[oldrow][oldcol] = b[newrow][newcol];
      b[oldrow][oldcol].row = oldrow;
      b[oldrow][oldcol].col = oldcol;
      b[newrow][newcol] = original;
      if (mate == true) {
        return 999999;
      } else if (depth == 2) {
        return total;
      } else {
        return min;
      }
    }

  public double[] evaluatePosition2(Piece[][] b, ArrayList<Move> mlist, int depth, double alpha, double beta) { //if depth odd, maximizing player
    double optimalVal;
    double val;
    int optimalIndex = 0; //the best move
    if(depth == 0){
      double n = evalBoard(b);
      return (new double[]{n, 0});
    }
    if(depth % 2 == 1){
      optimalVal = -10000;
    }
    else{
      optimalVal = 10000;
    }
    for(int i = 0; i < mlist.size(); i++){
      Move m = mlist.get(i);
      int newrow = m.r;
      int newcol = m.c;
      int oldrow = m.piecemove.row;
      int oldcol = m.piecemove.col;
      Piece original = b[newrow][newcol];
      b[newrow][newcol] = b[oldrow][oldcol];
      b[newrow][newcol].row = newrow;
      b[newrow][newcol].col = newcol;
      b[oldrow][oldcol] = null;
      if(depth % 2 == 1) {
        ArrayList<Move> whiteSpace = WhiteGetSafeMoves(b);
        val = evaluatePosition2(b, whiteSpace, depth-1, alpha, beta)[0];
      }
      else{
        ArrayList<Move> blackSpace = BlackGetSafeMoves(b);
        val = evaluatePosition2(b, blackSpace, depth - 1, alpha, beta)[0];
      }

      if(depth % 2 == 1){ //if looking at white
        if(val > optimalVal){
          optimalVal = val;
          optimalIndex = i;
        }
        alpha = Math.max(alpha, val);
      }

      else{
        if(val < optimalVal){
          optimalVal = val;
          optimalIndex = i;
        }
        beta = Math.min(beta, val);
      }
      b[oldrow][oldcol] = b[newrow][newcol];
      b[oldrow][oldcol].row = oldrow;
      b[oldrow][oldcol].col = oldcol;
      b[newrow][newcol] = original;
      if(beta <= alpha){
        break;
      }
    }
    return new double[]{optimalVal, optimalIndex};

  }

  public void mouseDragged(MouseEvent me) { // while mouse moving, piece should follow

    if (chessPiece == null)
      return;

    chessPiece.setLocation(me.getX() + xAdjustment, me.getY() + yAdjustment);

  }

  // Drop the chess piece back onto the chess board

  public void mouseReleased(MouseEvent e) {

    if (chessPiece == null)
      return;

    chessPiece.setVisible(false);
    Component c = chessBoard.findComponentAt(e.getX(), e.getY());
    chessPiece.setVisible(true);

    int Xcomp = e.getX();
    int Ycomp = e.getY();
    // int ComponentNum = (Xcomp / 75) + 8 * (Ycomp / 75);

    int C = Xcomp / 75;
    int r = Ycomp / 75;

    // check to make sure piece being clicked is right color
    if (playernum == 1 && board[pieceR][pieceC].color == "black") {
      movelist = null;
    } else if (playernum == 0 && board[pieceR][pieceC].color == "white") {
      movelist = null;
    }

    if (movelist != null) {
      if(playernum == 1){

      boolean works = false; //check if white makes valid move, else go back
      for (int a = 0; a < movelist.size(); a++) {
        int possrow = movelist.get(a).r;
        int posscol = movelist.get(a).c;
        if (r == possrow && C == posscol) {
          board[r][C] = null;
          works = true;
          if (c instanceof JLabel) {
            Container parent = c.getParent();
            parent.remove(0);
            parent.add(chessPiece);
          } else {

            Container parent = (Container) c;

            parent.add(chessPiece);
          }
          chessPiece.setVisible(true);

          board[r][C] = board[pieceR][pieceC];
          board[r][C].row = r;
          board[r][C].col = C;
          board[pieceR][pieceC] = null;
        }
      }
        if(works == false){
          JPanel goback = (JPanel) chessBoard.getComponent(pieceR * 8 + pieceC);
      chessPiece.setVisible(true);
      goback.add(chessPiece);
          return;
        }
      }

      checkSpecials(board, r, C);

          if (playernum == 1) { // if white's turn
            boolean checkk = false; // In Check?
            boolean limit = false; // Are there available moves?
            playernum = 0;
            System.out.println("\n---------------\n");
            System.out.println("It is now black's turn.");
            allmovelist = BlackGetSafeMoves(board);
            if (allmovelist.size() < 1) {
              limit = true;
            }
            masterlist = whitegetAllMoves(board);
            if (checkblackking(board) == true) {
              checkk = true;
              for (int i = 0; i < 8; i++) { // set parameter
                for (int j = 0; j < 8; j++) {
                  if (board[i][j] != null) {
                    if (board[i][j].name.equals("bKing")) {
                      board[i][j].attack = true; // king being attacked, cant castle
                    }
                  }
                }
              }
            } else {
              for (int i = 0; i < 8; i++) { // set parameter
                for (int j = 0; j < 8; j++) {
                  if (board[i][j] != null) {
                    if (board[i][j].name.equals("bKing")) {
                      board[i][j].attack = false; // king not being attacked
                    }
                  }
                }
              }
            }
            if (checkk == true && limit == true) { // no moves, in check
              System.out.println("\n---White Wins by checkmate!---");
              colorKing(board, "b", "red");
              playernum = 5;
              gameOver = true;
            } else if (checkk == true && limit == false) { // moves, in check
              System.out.println("\n---Black is in check!---");
              colorKing(board, "b", "yellow");
            } else if (checkk == false && limit == true) {
              System.out.println("\n---Stalemate; it's a draw!");
              colorKing(board, "b", "gray");
              colorKing(board, "w", "gray");
            }
          } 
          
          else {
            boolean checkk = false; // r we in check?
            boolean limit = false; // r there no moves left?
            playernum = 1;
            System.out.println("\n---------------\n");
            System.out.println("It is now white's turn.");
            allmovelist = WhiteGetSafeMoves(board);
            if (allmovelist.size() < 1) {
              limit = true;
            }
            masterlist = blackgetAllMoves(board);
            if (checkwhiteking(board) == true) {
              checkk = true;
              for (int i = 0; i < 8; i++) { // set parameter
                for (int j = 0; j < 8; j++) {
                  if (board[i][j] != null) {
                    if (board[i][j].name.equals("wKing")) {
                      board[i][j].attack = true;
                    }
                  }
                }
              }
            } else {
              for (int i = 0; i < 8; i++) { // set parameter
                for (int j = 0; j < 8; j++) {
                  if (board[i][j] != null) {
                    if (board[i][j].name.equals("wKing")) {
                      board[i][j].attack = false;
                    }
                  }
                }
              }
            }

            if (checkk == true && limit == true) { // no moves, in check
              System.out.println("\n---Black Wins by checkmate!---");
              colorKing(board, "w", "red");
              playernum = 5;
              gameOver = true;
            } else if (checkk == true && limit == false) { // moves, in check
              System.out.println("\n---White is in check!---");
              colorKing(board, "w", "yellow");
            } else if (checkk == false && limit == true) {
              System.out.println("\n---Stalemate; it's a draw!");
              colorKing(board, "b", "gray");
              colorKing(board, "w", "gray");
            }
          }
          
        }
    
    if(movelist != null){
      try {
        Robot bum = new Robot();
        Point p = MouseInfo.getPointerInfo().getLocation();
        bum.mouseMove(p.x+10,p.y);
      } catch (AWTException awtException) {
        awtException.printStackTrace();
      }

    }
    else{
      JPanel goback = (JPanel) chessBoard.getComponent(pieceR * 8 + pieceC);
      chessPiece.setVisible(true);
      goback.add(chessPiece);
    }


  }


  public void blackMove(Piece[][] b){
    movelist = BlackGetSafeMoves(board);
    //int rand = (int) (Math.random() * movelist.size());
    int maxIndex = 0;
    double maxValue = -500;
    int rand = 0;
    /**
     for (int t = 0; t < movelist.size(); t++){
     double curr = evaluatePosition(board, movelist.get(t), 1);
     if(curr > maxValue){
     maxValue = curr;
     maxIndex = t;
     }
     }
     rand = maxIndex;
     **/
    Collections.shuffle(movelist);
    rand = (int) evaluatePosition2(board, movelist, 3, -50000, 50000)[1];

    //once move is obtained, easy to implement
    pieceR = movelist.get(rand).piecemove.row;
    pieceC = movelist.get(rand).piecemove.col;
    int r = movelist.get(rand).r;
    int C = movelist.get(rand).c;
    JLabel piece7 = new JLabel(new ImageIcon("bKnight.png"));
    JPanel panel7 = (JPanel) chessBoard.getComponent(0);
    board[r][C] = null;
    piece7 = new JLabel(new ImageIcon("ChessRec/" + board[pieceR][pieceC].name+".png"));
    panel7 = (JPanel) chessBoard.getComponent(8*r+C);
    panel7.removeAll();
    panel7.revalidate();
    panel7.repaint();
    panel7.add(piece7);
    panel7 = (JPanel) chessBoard.getComponent(8*pieceR + pieceC);
    panel7.removeAll();
    panel7.revalidate();
    panel7.repaint();
    board[r][C] = board[pieceR][pieceC];
    board[r][C].row = r;
    board[r][C].col = C;
    board[pieceR][pieceC] = null;

    checkSpecials(board, r, C);

    int compOrig = (pieceR * 8) + pieceC;
    int compNew = (r * 8) + C;
    JPanel template5 = (JPanel) chessBoard.getComponent(compOrig);
    JPanel template6 = (JPanel) chessBoard.getComponent(compNew);
    template5.setBackground(Color.orange);
    template6.setBackground(Color.orange.darker());


    boolean checkk = false; // r we in check?
    boolean limit = false; // r there no moves left?
    playernum = 1;
    System.out.println("\n---------------\n");
    System.out.println("It is now white's turn.");
    allmovelist = WhiteGetSafeMoves(board);
    if (allmovelist.size() < 1) {
      limit = true;
    }
    masterlist = blackgetAllMoves(board);
    if (checkwhiteking(board) == true) {
      checkk = true;
      for (int i = 0; i < 8; i++) { // set parameter
        for (int j = 0; j < 8; j++) {
          if (board[i][j] != null) {
            if (board[i][j].name.equals("wKing")) {
              board[i][j].attack = true;
            }
          }
        }
      }
    } else {
      for (int i = 0; i < 8; i++) { // set parameter
        for (int j = 0; j < 8; j++) {
          if (board[i][j] != null) {
            if (board[i][j].name.equals("wKing")) {
              board[i][j].attack = false;
            }
          }
        }
      }
    }

    if (checkk == true && limit == true) { // no moves, in check
      System.out.println("\n---Black Wins by checkmate!---");
      colorKing(board, "w", "red");
      gameOver = true;
    } else if (checkk == true && limit == false) { // moves, in check
      System.out.println("\n---White is in check!---");
      colorKing(board, "w", "yellow");
    } else if (checkk == false && limit == true) {
      System.out.println("\n---Stalemate; it's a draw!");
      colorKing(board, "b", "gray");
      colorKing(board, "w", "gray");
    }



  }

  public void checkSpecials(Piece[][] b, int row, int col){
    int r = row;
    int C = col;
    if (true) {
      // sweep through and deactivate en passant, so that ONLY VALID FOR 1 MOVE
      if (playernum == 0) {// black
        for (int i = 0; i < 7; i++) {
          for (int j = 0; j < 7; j++) {
            if (board[i][j] != null && board[i][j].name.equals("bPawn")) {
              board[i][j].passant = false;
            }
          }
        }
      }

      if (playernum == 1) {// white
        for (int i = 0; i < 7; i++) {
          for (int j = 0; j < 7; j++) {
            if (board[i][j] != null && board[i][j].name.equals("wPawn")) {
              board[i][j].passant = false;
            }
          }
        }
      }

      // check to trigger pawn "en passant" boolean by going through all cases
      if (board[r][C].name.equals("wPawn") && r == 4) {
        if (board[r][C].moved == false) {
          board[r][C].passant = true;
          board[r][C].moved = true;
        }
      } else if (board[r][C].name.equals("wPawn")) {
        board[r][C].moved = true;
        board[r][C].passant = false;
      }

      if (board[r][C].name.equals("bPawn") && r == 3) {
        if (board[r][C].moved == false) {
          board[r][C].passant = true;
          board[r][C].moved = true;
        }
      } else if (board[r][C].name.equals("bPawn")) {
        board[r][C].moved = true;
        board[r][C].passant = false;
      }

      // check if enpassant occured, if so then kill piece
      if (board[r][C].name.equals("wPawn") && r == 2) {
        if (board[r + 1][C] != null) {
          if (board[r + 1][C].name.equals("bPawn") && board[r + 1][C].passant == true) {
            JPanel panel6 = (JPanel) chessBoard.getComponent(8 * (r + 1) + C);
            panel6.removeAll();
            panel6.revalidate();
            panel6.repaint();
            board[r + 1][C] = null;
          }
        }
      }
      if (board[r][C].name.equals("bPawn") && r == 5) {
        if (board[r - 1][C] != null) {
          if (board[r - 1][C].name.equals("wPawn") && board[r - 1][C].passant == true) {
            JPanel panel6 = (JPanel) chessBoard.getComponent(8 * (r - 1) + C);
            panel6.removeAll();
            panel6.revalidate();
            panel6.repaint();
            board[r - 1][C] = null;
          }
        }
      }

    } // CODE FOR EN PASSANT

    if (true) {
      if (board[r][C].name.equals("wPawn") && r == 0) { // if white pawn hits last row, change to queen
        JLabel piece5 = new JLabel(new ImageIcon("bKnight.png"));
        JPanel panel5 = (JPanel) chessBoard.getComponent(8 * r + C);
        board[r][C] = null;
        panel5 = (JPanel) chessBoard.getComponent(8 * r + C);
        panel5.removeAll();
        panel5.revalidate();
        panel5.repaint();
        piece5 = new JLabel(new ImageIcon("ChessRec/wQueen.png"));
        panel5 = (JPanel) chessBoard.getComponent(8 * r + C);
        panel5.add(piece5);
        board[r][C] = new Queen(r, C, "white");
      }

      // check for black pawn promotion
      if (board[r][C].name.equals("bPawn") && r == 7) {
        JLabel piece5 = new JLabel(new ImageIcon("wKnight.png"));
        JPanel panel5 = (JPanel) chessBoard.getComponent(8 * r + C);
        board[r][C] = null;
        panel5 = (JPanel) chessBoard.getComponent(8 * r + C);
        panel5.removeAll();
        panel5.revalidate();
        panel5.repaint();
        piece5 = new JLabel(new ImageIcon("ChessRec/bQueen.png"));
        panel5 = (JPanel) chessBoard.getComponent(8 * r + C);
        panel5.add(piece5);
        board[r][C] = new Queen(r, C, "black");
      }
    } // CODE FOR PAWN PROMOTION

    if (true) {
      // check for castling requirement
      if (board[r][C].name.equals("wRook")) {
        board[r][C].moved = true; // signify that rook has moved, cant castle now
      }
      if ((board[r][C].name.equals("wKing")) && board[r][C].moved == false) { // if king moved, and hasnt moved
        // until now
        if (r == 7 && C == 6) {
          board[r][C].moved = true;
          board[r][C].castle = true;

          JLabel piece2 = new JLabel(new ImageIcon("bKnight.png")); // just template
          JPanel panel2 = (JPanel) chessBoard.getComponent(0);
          board[7][5] = null;
          piece2 = new JLabel(new ImageIcon("ChessRec/wRook.png"));
          panel2 = (JPanel) chessBoard.getComponent(61);
          panel2.add(piece2);

          panel2 = (JPanel) chessBoard.getComponent(63);
          panel2.removeAll();
          panel2.revalidate();
          panel2.repaint();

          board[7][5] = board[7][7];
          board[7][5].row = 7;
          board[7][5].col = 5;
          board[7][7] = null;
        }

        else if (r == 7 && C == 2) {
          board[r][C].moved = true;
          board[r][C].castle = true;

          JLabel piece2 = new JLabel(new ImageIcon("bKnight.png"));
          JPanel panel2 = (JPanel) chessBoard.getComponent(0);
          board[7][3] = null;
          piece2 = new JLabel(new ImageIcon("ChessRec/wRook.png"));
          panel2 = (JPanel) chessBoard.getComponent(59);
          panel2.add(piece2);

          panel2 = (JPanel) chessBoard.getComponent(56);
          panel2.removeAll();
          panel2.revalidate();
          panel2.repaint();

          board[7][3] = board[7][0];
          board[7][3].row = 7;
          board[7][3].col = 3;
          board[7][0] = null;
        } else {
          board[r][C].moved = true;
        }

      }

      if (board[r][C].name.equals("bRook")) {
        board[r][C].moved = true;
      }
      if ((board[r][C].name.equals("bKing")) && board[r][C].moved == false) { // if king moved, and hasnt moved
        // until now
        if (r == 0 && C == 6) {
          board[r][C].moved = true;
          board[r][C].castle = true;

          JLabel piece2 = new JLabel(new ImageIcon("bKnight.png"));
          JPanel panel2 = (JPanel) chessBoard.getComponent(0);
          board[0][5] = null;
          piece2 = new JLabel(new ImageIcon("ChessRec/bRook.png"));
          panel2 = (JPanel) chessBoard.getComponent(5);
          panel2.add(piece2);

          panel2 = (JPanel) chessBoard.getComponent(7);
          panel2.removeAll();
          panel2.revalidate();
          panel2.repaint();

          board[0][5] = board[0][7];
          board[0][5].row = 0;
          board[0][5].col = 5;
          board[0][7] = null;
        }

        else if (r == 0 && C == 2) {
          board[r][C].moved = true;
          board[r][C].castle = true;

          JLabel piece2 = new JLabel(new ImageIcon("bKnight.png"));
          JPanel panel2 = (JPanel) chessBoard.getComponent(0);
          board[0][3] = null;
          piece2 = new JLabel(new ImageIcon("ChessRec/bRook.png"));
          panel2 = (JPanel) chessBoard.getComponent(3);
          panel2.add(piece2);

          panel2 = (JPanel) chessBoard.getComponent(0);
          panel2.removeAll();
          panel2.revalidate();
          panel2.repaint();

          board[0][3] = board[0][0];
          board[0][3].row = 0;
          board[0][3].col = 0;
          board[0][0] = null;
        } else {
          board[r][C].moved = true;
        }

      }

    } // CODE FOR CASTLING


    for (int j = 0; j < 8; j++) { // reset colors again
      for (int i = 0; i < 8; i++) {
        JPanel reset = (JPanel) chessBoard.getComponent((i * 8) + j);
        if (i % 2 == j % 2) {
          reset.setBackground(Color.blue);
        } else {
          reset.setBackground(Color.white);
        }
      }
    } // RESET BOARD

  }


  public void mouseClicked(MouseEvent e) {

  }

  public void mouseMoved(MouseEvent e) {
    if(playernum == 0) {
      blackMove(board);
    }
  }

  public void mouseEntered(MouseEvent e) {

  }

  public void mouseExited(MouseEvent e) {

  }

}


