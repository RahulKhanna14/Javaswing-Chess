package ChessRec;//The infrastructure (bare chessboard, function headers for MouseListener, essentially JavaSwing-specific code) was taken from https://forgetcode.com/Java/848-Chess-game-Swing; the core code of how the pieces actual move, gameplay, etc. is all original


import ChessRec.ChessGameDemo;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
 

class Main{
  public static void main(String[] args) {
  JFrame frame = new ChessGameDemo();
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  frame.pack();
  frame.setResizable(false);
  frame.setLocationRelativeTo( null );
  frame.setVisible(true);
 }
}
 