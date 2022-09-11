package ChessRec;

import ChessRec.Move;
import ChessRec.Piece;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

//while gameover is false
public class ChessGameDemo extends JFrame implements MouseListener, MouseMotionListener {

  JLayeredPane layeredPane;
  JPanel chessBoard;
  JLabel chessPiece;
  JTextArea whiteMoves = new JTextArea();
  JProgressBar progress = new JProgressBar();
  JLabel whiteTimer = new JLabel("10:00", SwingConstants.CENTER);
  JLabel blackTimer = new JLabel("10:00", SwingConstants.CENTER);
  SwingWorker<Integer, Void> swingWorker;
  int xAdjustment;
  int yAdjustment;
  int pieceR = 0;
  int pieceC = 0;
  int playernum = 1;
  int kingrow = 0;
  int kingcol = 0;
  int blackPieces = 16;
  int whitePieces = 16;
  boolean gameOver = false;
  boolean answered = false;
  int difficulty = 3;
  int moveCounter = 0;
  Timer TimerW;
  Timer TimerB;
  final SwingProgressBar it = new SwingProgressBar();
  int currValue = 0;
  boolean done = false;
  int finalMoveCounter = 0;
  long FuncStartTime;


  int secLeftW = 10;
  int secLeftB = 600;
  int timeLeftw = 60;
  int timeLeftb = 600;
  int timePassed = 0;
  boolean nowSafe = false;
  int finalMoveNum = 0;
  String request = "";

  int sliderDifficulty = 600;
  Color primaryCol = Color.blue;
  int minChosen = 10;
  int secChosen = 0;
  boolean toggled = true;
  Color tempCol = Color.blue;
  JComboBox customList;

  JButton goBack;
  JButton goForward;


  int clickRow = 0;
  int clickCol = 0;

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
  ArrayList<String> allPerfMoves = new ArrayList<String>();
  ArrayList<String> boardHash = new ArrayList<String>();
  ArrayList<String> ultraHash = new ArrayList<String>();
  ArrayList<Double> ultraVal = new ArrayList<Double>();

  public ChessGameDemo() {
    //final int[] difficulty = {3};
    JFrame frame2 = new JFrame("Select Difficulty");
    frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame2.setSize(300,200);
    frame2.setAlwaysOnTop(true);
    frame2.setLocationRelativeTo( null );
    Color lightgrey = new Color(241, 243, 244);
    JPanel buttons = new JPanel();
    buttons.setLayout(new GridLayout(4,1));
    JButton easy = new JButton("Very Easy");
    JButton medium = new JButton("Normal");
    JButton tough = new JButton("Intermediate");
    JButton hard = new JButton("Tough");
    buttons.add(easy);
    buttons.add(medium);
    buttons.add(tough);
    buttons.add(hard);
    frame2.add(BorderLayout.CENTER, buttons);
    //frame2.setVisible(true);


    easy.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        difficulty = 1;
        System.out.println("Difficulty set to easy.\n The AI selects randomly.\n");
        answered = true;
        timeLeftb = 60;
        blackTimer.setText("1:00");
        frame2.setVisible(false);
        frame2.dispose();

      }
    });
    medium.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        difficulty = 2;
        timeLeftb = 60;
        blackTimer.setText("1:00");
        System.out.println("Difficulty set to medium.\n The AI analyzes 2 moves deep.\n");
        frame2.setVisible(false);
        answered = true;
        frame2.dispose();
      }
    });
    tough.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        difficulty = 3;
        timeLeftb = 450;
        blackTimer.setText("7:30");
        System.out.println("Difficulty set to intermediate.\n The AI analyzes 3 moves deep. Please allow for ~5-15 seconds between moves. \n");
        frame2.setVisible(false);
        answered = true;
        frame2.dispose();
      }
    });
    hard.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        difficulty = 4;
        timeLeftb = 750;
        blackTimer.setText("12:30");
        System.out.println("Difficulty set to tough.\n The AI analyzes 4 moves deep. Please allow for ~10-30 seconds between moves. \n");
        frame2.setVisible(false);
        answered = true;
        frame2.dispose();
      }
    });



    masterlist = new ArrayList<Move>();
    movelist = new ArrayList<Move>();
    movelist2 = new ArrayList<Move>();
    movelist3 = new ArrayList<Move>();
    allmovelist = new ArrayList<Move>();
    blackmoves = new ArrayList<Move>();
    Dimension boardSize = new Dimension(600, 600);
    Dimension windowSize = new Dimension(600, 600);
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
    JPanel wholeFrame = new JPanel();

    wholeFrame.setLayout(new BorderLayout());

    layeredPane = new JLayeredPane();
    getContentPane().add(wholeFrame);
    wholeFrame.add(layeredPane, BorderLayout.CENTER);
    layeredPane.setPreferredSize(windowSize);
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
        square.setBackground(i % 2 == 0 ? primaryCol : Color.white);
      else
        square.setBackground(i % 2 == 0 ? Color.white : primaryCol);
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
    /**
    JFrame frame3 = new JFrame("Move List");
    frame3.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    frame3.setSize(300,600);
    frame3.setLocation(frame2.getX() + 450, frame2.getY()-200);
    JPanel moveList = new JPanel();
    moveList.setLayout(new GridLayout(2,1));
    JTextField whiteMoves = new JTextField(1);
    whiteMoves.setText("white moves go here");
    whiteMoves.setEditable(true);

    JTextField blackMoves = new JTextField(1);
    blackMoves.setText("black moves go here");
    blackMoves.setEditable(false);


    moveList.add(whiteMoves);
    moveList.add(blackMoves);

    frame3.add(BorderLayout.CENTER, moveList);
    frame3.setVisible(true);
    **/
    JPanel TextmoveList = new JPanel(new BorderLayout());
    JPanel emptyLeft = new JPanel(new BorderLayout());
    JPanel emptyRight = new JPanel(new BorderLayout());
    JPanel emptyTop = new JPanel(new BorderLayout());
    emptyLeft.setPreferredSize(new Dimension(20, 400));
    emptyRight.setPreferredSize(new Dimension(20, 400));
    emptyTop.setPreferredSize(new Dimension(300, 40));
    emptyLeft.setVisible(true);
    emptyRight.setVisible(true);
    emptyTop.setVisible(true);
    TextmoveList.setBackground(Color.white);
    TextmoveList.setPreferredSize(new Dimension(300,600));
    whiteMoves.setFont(whiteMoves.getFont().deriveFont(20f));

    JPanel buttonRegion = new JPanel(new BorderLayout());
    buttonRegion.setPreferredSize(new Dimension(300, 100));
    JPanel moveButtons = new JPanel(new GridLayout(1, 3));
    moveButtons.setPreferredSize(new Dimension(280, 100));
    goBack = new JButton("<<");
    goForward = new JButton(">>");



    JPanel blank1 = new JPanel();
    blank1.setVisible(true);
    JPanel blank2 = new JPanel();
    blank2.setVisible(true);
    JPanel blank3 = new JPanel();
    blank3.setVisible(true);
    JPanel blank4 = new JPanel();
    blank4.setVisible(true);
    //moveButtons.add(blank1);
    //moveButtons.add(blank2);
    //moveButtons.add(blank3);
    moveButtons.add(goBack);
    moveButtons.add(blank4);
    moveButtons.add(goForward);

    buttonRegion.add(moveButtons, BorderLayout.CENTER);
    JPanel emptyLeft2 = new JPanel();
    JPanel emptyRight2 = new JPanel();
    JPanel emptyBot2 = new JPanel();
    JPanel emptyTop2 = new JPanel();
    emptyLeft2.setPreferredSize(new Dimension(10, 20));
    emptyRight2.setPreferredSize(new Dimension(10, 20));
    emptyBot2.setPreferredSize(new Dimension(300, 20));
    emptyTop2.setPreferredSize(new Dimension(300, 40));
    emptyLeft2.setVisible(true);
    emptyRight2.setVisible(true);
    emptyBot2.setVisible(true);
    emptyTop2.setVisible(true);

    buttonRegion.add(emptyLeft2, BorderLayout.LINE_START);
    buttonRegion.add(emptyRight2, BorderLayout.LINE_END);
    buttonRegion.add(emptyBot2, BorderLayout.PAGE_END);
    buttonRegion.add(emptyTop2, BorderLayout.PAGE_START);

    whiteMoves.setRows(23);
    whiteMoves.setColumns(13);
    whiteMoves.setLineWrap(true);
    whiteMoves.setBackground(Color.lightGray);
    whiteMoves.setVisible(true);
    JScrollPane scroll = new JScrollPane (whiteMoves);
    scroll.createVerticalScrollBar();
    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


    TextmoveList.add(scroll, BorderLayout.CENTER);
    TextmoveList.add(buttonRegion, BorderLayout.PAGE_END);

    TextmoveList.add(emptyLeft, BorderLayout.LINE_START);
    TextmoveList.add(emptyRight, BorderLayout.LINE_END);
    TextmoveList.add(emptyTop, BorderLayout.PAGE_START);
    whiteMoves.setText("Move List\n\n");
    whiteMoves.setEditable(false);
    //TextmoveList.add(whiteMoves);

    wholeFrame.add(TextmoveList, BorderLayout.LINE_END);

    //Put numbers and letters along the rows and columns
    JPanel lowerArea = new JPanel(new BorderLayout());
    JPanel rowOfLetters = new JPanel(new GridLayout(1, 8));
    rowOfLetters.setPreferredSize(new Dimension(600, 20));
    String alph = "abcdefgh";
    for(int i = 0; i < 8; i++){
      JLabel letter = new JLabel("e", SwingConstants.CENTER);
      letter.setText(String.valueOf(alph.charAt(i)));
      letter.setVisible(true);
      rowOfLetters.add(letter);
    }
    rowOfLetters.setVisible(true);
    lowerArea.setPreferredSize(new Dimension(600, 20));
    JPanel emptyRight3 = new JPanel();
    emptyRight3.setPreferredSize(new Dimension(300, 20));
    JPanel emptyLeft3 = new JPanel();
    emptyLeft3.setPreferredSize(new Dimension(325, 20));
    lowerArea.add(rowOfLetters, BorderLayout.CENTER);
    lowerArea.add(emptyRight3, BorderLayout.LINE_END);
    lowerArea.add(emptyLeft3, BorderLayout.LINE_START);

    wholeFrame.add(lowerArea, BorderLayout.PAGE_END);

    //

    JPanel leftArea = new JPanel(new BorderLayout());
    leftArea.setPreferredSize(new Dimension(325, 600));

    JPanel colOfNum = new JPanel(new GridLayout(8, 1));
    colOfNum.setPreferredSize(new Dimension(60, 600));
    for(int i = 0; i < 8; i++){
      JLabel letter = new JLabel("e", SwingConstants.RIGHT);
      letter.setText(Integer.toString(9-(i+1)) + "  ");
      letter.setVisible(true);
      colOfNum.add(letter);
    }
    colOfNum.setVisible(true);

    whiteTimer.setFont(new Font("Serif", Font.PLAIN, 40));
    whiteTimer.setBorder(BorderFactory.createLineBorder(Color.black, 3));
    whiteTimer.setBackground(Color.white);
    whiteTimer.setOpaque(true);
    blackTimer.setOpaque(true);
    blackTimer.setBorder(BorderFactory.createLineBorder(Color.black, 3));
    blackTimer.setBackground(Color.white);
    blackTimer.setFont(new Font("Serif", Font.PLAIN, 45));

    whiteTimer.setVisible(true);
    blackTimer.setVisible(true);

    JPanel timerBox = new JPanel(new GridLayout(5, 1));
    timerBox.setPreferredSize(new Dimension(120, 400));

    JPanel empty10 = new JPanel();
    JPanel empty11 = new JPanel();
    JPanel empty12 = new JPanel();
    JPanel empty13 = new JPanel();
    empty10.setVisible(true);
    empty11.setVisible(true);
    empty12.setVisible(true);
    empty13.setVisible(true);
    timerBox.add(empty10);
    timerBox.add(blackTimer);
    //timerBox.add(empty11);
    timerBox.add(empty12);
    timerBox.add(whiteTimer);
    //timerBox.add(empty13);
    leftArea.add(timerBox, BorderLayout.CENTER);

    JPanel emptyLeft5 = new JPanel();
    emptyLeft5.setVisible(true);
    emptyLeft5.setPreferredSize(new Dimension(20, 600));
    JPanel emptyBot3 = new JPanel();
    emptyBot3.setVisible(true);
    emptyBot3.setPreferredSize(new Dimension(275, 40));






    leftArea.add(colOfNum, BorderLayout.LINE_END);
    leftArea.add(emptyLeft5, BorderLayout.LINE_START);
    //leftArea.add(emptyBot3, BorderLayout.PAGE_END);
    wholeFrame.add(leftArea, BorderLayout.LINE_START);


    TimerW = new Timer(1000, new ActionListener(){
      @Override

      public void actionPerformed(ActionEvent e)
      {
        if(gameOver){
          return;
        }
        timePassed += 1;
        int currMin = (timeLeftw-timePassed) / 60;
        int currSec = (timeLeftw - timePassed) % 60;
        String nah = "";
        if(currSec < 10){
          nah = "0" + currSec;
        }
        else{
          nah = String.valueOf(currSec);
        }
        String res = currMin + ":" + nah;
        SimpleDateFormat df=new SimpleDateFormat("mm:ss");
        whiteTimer.setText(res);

        if(timeLeftw - timePassed < 0) {
          colorKing(board, "w", "red");
          System.out.println("Black wins on time!");
          timeLeftw -= timePassed;
          gameOver = true;
          TimerW.stop();
          endGame("Black", true, false);
        }
      }
    });

    TimerB = new Timer(1000, new ActionListener(){
      @Override

      public void actionPerformed(ActionEvent e)
      {
        timeLeftb -= 1000;
        SimpleDateFormat df=new SimpleDateFormat("mm:ss:S");
        whiteTimer.setText(df.format(timeLeftw));
      }
    });









    goBack.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        //do the gobackmove
        if(moveCounter > 0 && toggled) {
            reverseMove(allPerfMoves.get(2 * moveCounter - 1));
            reverseMove(allPerfMoves.get(2 * moveCounter - 2));

          System.out.println("\n You went backwards 1 move. \n");
          moveCounter--;
        }
      }
                             });


    goForward.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        //do the goforwardmove
        if(moveCounter < finalMoveCounter && toggled) {
          System.out.println("\n You went forwards 1 move. \n");
            performMove(allPerfMoves.get(2 * moveCounter));
            performMove(allPerfMoves.get(2 * moveCounter + 1));
          moveCounter++;
        }
      }
    });




    masterlist = blackgetAllMoves(board); // initializes masterlist before proceeding
    System.out.println("It is white's turn. ");

    JPanel upperRegion = new JPanel(new BorderLayout());
    upperRegion.setPreferredSize(new Dimension(900, 20));
    //wholeFrame.add(upperRegion, BorderLayout.PAGE_START);
    JButton settings = new JButton("Settings");
    settings.setBackground(Color.lightGray);
    //settings.setBorder(BorderFactory.createLineBorder(Color.blue, 1));
    settings.setVisible(true);

    settings.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        createSettings();
      }
    });
    TextmoveList.add(settings, BorderLayout.PAGE_START);
    createSettings();

  }

  public void createSettings(){
    JFrame frame6 = new JFrame("Settings");
    frame6.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame6.setSize(600, 500);
    frame6.setAlwaysOnTop(true);
    frame6.setLocationRelativeTo( null );
    frame6.setLayout(new BorderLayout());
    frame6.setVisible(true);

    //frame has been created above, now to add the different components
    JPanel settingsLayout = new JPanel(new GridLayout(13, 1));
    settingsLayout.setPreferredSize(new Dimension(600, 500));

    JSlider difficultySlider = new JSlider(JSlider.HORIZONTAL, 100, 950, sliderDifficulty);
    difficultySlider.setMajorTickSpacing(100);
    difficultySlider.setMinorTickSpacing(50);
    //Create the slider
    difficultySlider.setPaintTicks(true);

//Create the label table
    Hashtable labelTable = new Hashtable();
    labelTable.put( new Integer( 125 ), new JLabel("Easy") );
    labelTable.put( new Integer( 400 ), new JLabel("Normal") );
    labelTable.put( new Integer( 657 ), new JLabel("Intermediate") );
    labelTable.put( new Integer( 900 ), new JLabel("Tough") );
    difficultySlider.setLabelTable( labelTable );
    difficultySlider.setPaintLabels(true);

    JLabel difficultyLabel = new JLabel("  Select Difficulty: ");
    difficultyLabel.setFont(new Font("Serif", Font.BOLD, 16));
    JPanel difficultyTab = new JPanel(new BorderLayout());
    difficultyTab.add(difficultySlider, BorderLayout.CENTER);
    difficultyTab.add(difficultyLabel, BorderLayout.LINE_START);
    settingsLayout.add(difficultyTab);

    //difficulty done

    JLabel colorLabel = new JLabel("  Primary Board Color:               ");
    colorLabel.setFont(new Font("Serif", Font.BOLD, 16));

    JPanel colorTab = new JPanel(new BorderLayout());

    JButton chooseColor = new JButton("Choose Color");
    chooseColor.setOpaque(true);
    chooseColor.setBorderPainted(false);
    chooseColor.setBackground(primaryCol);
    chooseColor.setPreferredSize(new Dimension(60, 30));
    JPanel emptyRight = new JPanel();
    emptyRight.setVisible(true);
    emptyRight.setPreferredSize(new Dimension(125, 20));
    chooseColor.setVisible(true);
    colorTab.add(chooseColor, BorderLayout.CENTER);
    colorTab.add(emptyRight, BorderLayout.LINE_END);
    chooseColor.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        tempCol = JColorChooser.showDialog(chooseColor, "Make a choice", primaryCol);
        if (tempCol != null) {
          chooseColor.setBackground(tempCol);
        }
      }
    });

    colorTab.add(colorLabel, BorderLayout.LINE_START);
    JPanel empty1 = new JPanel();
    empty1.setVisible(true);

    settingsLayout.add(empty1);
    settingsLayout.add(colorTab);

    //color chooser is now done

    JLabel timeLabel = new JLabel(" Time Control (Min/Sec):");
    timeLabel.setFont(new Font("Serif", Font.BOLD, 16));

    JPanel timeTab = new JPanel(new GridLayout(1, 3));
    SpinnerNumberModel minuteChoose = new SpinnerNumberModel(minChosen, 0, 30, 1);
    SpinnerNumberModel secondChoose = new SpinnerNumberModel(secChosen, 0, 59, 5);
    JSpinner spinner1 = new JSpinner(minuteChoose);
    spinner1.setPreferredSize(new Dimension(40, 20));

    JSpinner spinner2 = new JSpinner(secondChoose);
    spinner2.setPreferredSize(new Dimension(40, 20));
    JPanel emptyRight2 = new JPanel();
    emptyRight2.setVisible(true);
    emptyRight2.setPreferredSize(new Dimension(125, 20));
    timeTab.add(timeLabel);
    timeTab.add(spinner1);
    timeTab.add(spinner2);


    JPanel empty4 = new JPanel();
    empty4.setVisible(true);

    settingsLayout.add(empty4);
    settingsLayout.add(timeTab);

    //time controls all done
    JPanel togTab = new JPanel(new BorderLayout());
    JLabel togLabel = new JLabel(" Take-Back Moves:        ");
    togLabel.setFont(new Font("Serif", Font.BOLD, 16));

    JButton tog = new JButton("Take-back Moves enabled. Press to disable.");
    tog.setOpaque(true);

    tog.setBorderPainted(false);
    tog.setBackground(Color.blue);  ;
    tog.setBorderPainted(false);
    tog.setBackground(Color.green);
    JPanel emptyRight4 = new JPanel();
    emptyRight4.setVisible(true);
    emptyRight4.setPreferredSize(new Dimension(40, 30));
    tog.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        if(toggled){
          tog.setText("Take-back Moves disabled. Press to enable.");
          goBack.setText("--");
          goForward.setText("--");
          toggled = false;
          tog.setBackground(Color.gray);
        }
        else{
          tog.setText("Take-back Moves enabled. Press to disable.");
          goBack.setText("<<");
          goForward.setText(">>");
          toggled = true;
          tog.setBackground(Color.green);
        }

      }
    });
    togTab.add(togLabel, BorderLayout.LINE_START);
    togTab.add(tog, BorderLayout.CENTER);
    togTab.add(emptyRight4, BorderLayout.LINE_END);
    JPanel empty6 = new JPanel();
    empty6.setVisible(true);
    settingsLayout.add(empty6);
    settingsLayout.add(togTab);

    //toggle button now completely done above

    JPanel randomTab = new JPanel();
    GridLayout layout = new GridLayout(1,3);
    layout.setHgap(30);
    randomTab.setLayout(layout);
    //JLabel randomLabel = new JLabel("Take-Back Moves:        ");
    //togLabel.setFont(new Font("Serif", Font.BOLD, 16));

    JButton random = new JButton("Randomize!");
    JButton inpRandom = new JButton("Recommend me!");
    //inpRandom.setBorder(new EmptyBorder(10, 10,10, 10));
    JPanel rightMost = new JPanel(new BorderLayout());
    rightMost.setVisible(true);
    rightMost.add(inpRandom, BorderLayout.CENTER);
    JPanel emptyi = new JPanel();
    emptyi.setVisible(true);
    emptyi.setPreferredSize(new Dimension(30, 20));
    rightMost.add(emptyi, BorderLayout.LINE_END);
    tog.setOpaque(true);

    /**
     random.setBorderPainted(false);
     random.setBackground(Color.blue);  ;
     random.setBorderPainted(false);
     random.setBackground(Color.green);
     random.setOpaque(true);

     inpRandom.setBorderPainted(false);
     inpRandom.setBackground(Color.blue);  ;
     inpRandom.setBorderPainted(false);
     inpRandom.setBackground(Color.green);
     **/
    JPanel emptyRight5 = new JPanel();
    emptyRight5.setVisible(true);
    //emptyRight5.setPreferredSize(new Dimension(40, 30));
    JPanel emptyRight7 = new JPanel();
    emptyRight7.setVisible(true);
    //emptyRight7.setPreferredSize(new Dimension(40, 30));
    JPanel emptyRight8 = new JPanel();
    emptyRight8.setVisible(true);
    //emptyRight8.setPreferredSize(new Dimension(40, 30));
    random.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        int newdiff = (int) (Math.random() * 850 + 100);
        difficultySlider.setValue(newdiff);
        Color newCol = new Color((int)(Math.random() * 0x1000000));
        tempCol = newCol;
        chooseColor.setBackground(tempCol);
        int newMin = (int) (Math.random() * 30);
        int newSec = 5 * (int) (Math.random() * 11);
        minuteChoose.setValue(newMin);
        secondChoose.setValue(newSec);
        if(Math.random() < 0.5) {
          toggled = true;
        }
        else{
          toggled = false;
        }
        settingsLayout.revalidate();

      }
    });
    inpRandom.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        JFrame frame9 = new JFrame("Request Game Layout");
        frame9.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame9.setSize(500, 300);
        frame9.setAlwaysOnTop(true);
        frame9.setLocationRelativeTo( null );
        frame9.setLayout(new BorderLayout());
        frame9.setVisible(true);
        frame9.setResizable(false);

        JTextArea userInput = new JTextArea(5, 20);
        userInput.setFont(new Font("Serif", Font.PLAIN, 12));
        userInput.setText(request);
        userInput.setLineWrap(true);
        userInput.setWrapStyleWord(true);
        frame9.add(userInput, BorderLayout.CENTER);
        JTextArea directions = new JTextArea(5, 20);
        directions.setFont(new Font("Serif", Font.ITALIC, 15));
        directions.setLineWrap(true);
        directions.setWrapStyleWord(true);
        directions.setEditable(false);
        directions.append("Write a sentence describing the type of game you want today. Be sure to use keywords such as: {easy/medium/hard, slow-paced/classical/fast-paced, standard/custom}. \nIf you do not specify, the game will choose for you.");
        directions.setBackground(Color.lightGray);
        frame9.add(directions, BorderLayout.PAGE_START);
        JButton layoutConfirm = new JButton("Confirm");
        layoutConfirm.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e){
            request = userInput.getText();
            String toAnalyze = userInput.getText().toLowerCase();
            String[] ez = {"free", "beginner", "new"};
            String[] meh = {"easy", "eas", "learn", "bad", "help", "simple", "calm", "relax", "fair", "ok"};
            String[] inter = {"tough", "experienced", "medium", "average", "moderate", "decent"};
            String[] expert = {"challeng", "hard", "master", "expert", "master", "pro", "skill", "difficult", "rough", "aggressive", "passive"};

            String[] slow = {"classical", "slow", "peaceful", "no stress", "less"};
            String[] fast = {"blitz", "rapid", "fast", "quick", "speed", "stress", "rush"};
            //else do medium

            String[] custom = {"custom", "specialized"};
            difficultySlider.setValue(450);
            for(String s : ez){
              if(toAnalyze.contains(s)){
                difficultySlider.setValue(150);
              }
            }
            for(String s : meh){
              if(toAnalyze.contains(s)){
                difficultySlider.setValue(350);
              }
            }
            for(String s : inter){
              if(toAnalyze.contains(s)){
                difficultySlider.setValue(650);
              }
            }
            for(String s : expert){
              if(toAnalyze.contains(s)){
                difficultySlider.setValue(900);
              }
            }
            minuteChoose.setValue((int) (Math.random() * 5 + 8));
            secondChoose.setValue(0);
            for(String s : slow){
              if(toAnalyze.contains(s)){
                minuteChoose.setValue((int) (Math.random() * 10 + 20));
                secondChoose.setValue(0);
              }
            }
            for(String s : fast){
              if(toAnalyze.contains(s)){
                minuteChoose.setValue((int) (Math.random() * 3));
                secondChoose.setValue((int) (Math.random() * 30 + 25));
              }
            }
            customList.setSelectedIndex(0);
            for(String s : custom){
              if(toAnalyze.contains(s)){
                customList.setSelectedIndex(5);
              }
            }

            frame9.setVisible(false);
            frame9.dispose();
          }
        });
        frame9.add(layoutConfirm, BorderLayout.PAGE_END);






      }
    });
    JLabel randomLabel = new JLabel(" Overall Settings: ");
    randomLabel.setFont(new Font("Serif", Font.BOLD, 16));
    randomTab.add(randomLabel);
    randomTab.add(random);
    //randomTab.add(emptyRight7);
    randomTab.add(rightMost);
    //randomTab.add(emptyRight5);
    randomTab.setPreferredSize(new Dimension(600, 250));
    JPanel empty7 = new JPanel();
    empty7.setVisible(true);
    settingsLayout.add(empty7);
    settingsLayout.add(randomTab);

    //random tab / recommendation tab now complete

    JPanel customTab = new JPanel(new BorderLayout());
    JLabel customLabel = new JLabel(" Choose a custom challenge:        ");
    customLabel.setFont(new Font("Serif", Font.BOLD, 16));

    String[] challenges = { "Standard", "Play without your Queen", "Play without your central 4 pawns", "Play without your knights/bishops", "Play half-blind (can't see black pieces)", "Random"};
    customList = new JComboBox(challenges);
    customList.setSelectedIndex(0);

    JPanel emptyRight9 = new JPanel();
    emptyRight9.setVisible(true);
    emptyRight9.setPreferredSize(new Dimension(40, 30));

    customTab.add(customLabel, BorderLayout.LINE_START);
    customTab.add(customList, BorderLayout.CENTER);
    customTab.add(emptyRight9, BorderLayout.LINE_END);
    JPanel empty9 = new JPanel();
    empty9.setVisible(true);
    settingsLayout.add(empty9);
    settingsLayout.add(customTab);

    //custom challenge done above

    JPanel confirmTab = new JPanel(new FlowLayout());
    togLabel.setFont(new Font("Serif", Font.BOLD, 16));

    JButton confirm = new JButton("Confirm Settings");
    JButton cancel = new JButton("Cancel all Changes");
    confirm.setOpaque(true);
    cancel.setOpaque(true);

    confirm.setBorderPainted(false);
    cancel.setBorderPainted(false);
    confirm.setBackground(Color.yellow.brighter());
    cancel.setBackground(Color.lightGray);
    JPanel emptyRight11 = new JPanel();
    emptyRight11.setVisible(true);
    emptyRight11.setPreferredSize(new Dimension(40, 30));
    confirm.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
        sliderDifficulty = difficultySlider.getValue();

        if(sliderDifficulty < 200){
          difficulty = 1;
          blackTimer.setText("1:00");
          timeLeftb = 60;
        }
        else if(sliderDifficulty < 500){
          difficulty = 2;
          blackTimer.setText("1:00");
          timeLeftb = 60;
        }
        else if (sliderDifficulty < 800){
          difficulty = 3;
          blackTimer.setText("7:30");
          timeLeftb = 450;
        }
        else{
          difficulty = 4;
          blackTimer.setText("12:30");
          timeLeftb = 750;
        }

        minChosen = (int) minuteChoose.getValue();
        secChosen = (int) secondChoose.getValue();
        primaryCol = tempCol;
        for (int j = 0; j < 8; j++) { // reset the colors of board, double for loop
          for (int i = 0; i < 8; i++) {
            JPanel reset = (JPanel) chessBoard.getComponent((i * 8) + j);
            if (i % 2 == j % 2) {
              reset.setBackground(primaryCol);
            } else {
              reset.setBackground(Color.white);
            }
          }
        }

        String bah = "";
        if(secChosen < 10){
          bah = "0" + secChosen;
        }
        else{
          bah = String.valueOf(secChosen);
        }
        String res = minChosen + ":" + bah;
        whiteTimer.setText(res);
        timeLeftw = minChosen * 60 + secChosen;


        frame6.setVisible(false);
        frame6.dispose();
      }
    });

    cancel.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        frame6.setVisible(false);
        frame6.dispose();
      }
    });
    JPanel empty100 = new JPanel();
    empty100.setVisible(true);
    JPanel empty101 = new JPanel();
    empty101.setVisible(true);
    JPanel empty102 = new JPanel();
    empty102.setVisible(true);
    confirmTab.add(empty100);
    confirmTab.add(confirm);
    confirmTab.add(empty102);
    confirmTab.add(cancel);
    confirmTab.add(empty101);
    JPanel empty8 = new JPanel();
    empty8.setVisible(true);
    settingsLayout.add(empty8);
    settingsLayout.add(confirmTab);




    frame6.add(settingsLayout, BorderLayout.CENTER);
    frame6.setBackground(Color.orange);
    frame6.setVisible(true);


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
      return;
    }
    masterlist = null;
    chessPiece = null;
    Component c = chessBoard.findComponentAt(e.getX(), e.getY());


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
      if(board[Ycomp / 75][Xcomp / 75].color == "white"){
        clickRow = Ycomp / 75;
        clickCol = Xcomp / 75;
      }

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
          if(board[temprow][tempcol].name.contains("King")){
            int rowey = temprow;
            int coley = tempcol;
            if(board[temprow][tempcol].moved == false){
              if((coley - 1 > -1) && tempcol == 6){
                if(checkWhiteAttacked(board, rowey, coley - 1)){
                  movelist.remove(movelist.get(i));
                  i -= 1;
                }
              }
              else if ((coley + 1 < 8) && tempcol == 2){
                if(checkWhiteAttacked(board, rowey, coley + 1)){
                  movelist.remove(movelist.get(i));
                  i -= 1;
                }
              }
            }
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
          if(board[temprow][tempcol].name.contains("King")){
            int rowey = temprow;
            int coley = tempcol;
            if(board[temprow][tempcol].moved == false){
              if((coley - 1 > -1) && tempcol == 6){
                if(checkBlackAttacked(board, rowey, coley - 1)){
                  movelist.remove(movelist.get(i));
                  i -= 1;
                }
              }
              else if ((coley + 1 < 8) && tempcol == 2){
                if(checkBlackAttacked(board, rowey, coley + 1)){
                  movelist.remove(movelist.get(i));
                  i -= 1;
                }
              }
            }
          }
          board[oldrow][oldcol] = board[temprow][tempcol];
          board[oldrow][oldcol].row = oldrow;
          board[oldrow][oldcol].col = oldcol;
          board[temprow][tempcol] = original;
        }
      }

      // With move list now finalized, mark valid moves with green color
      if(movelist != null && board[Ycomp / 75][Xcomp / 75].color != "black") {
        for (int j = 0; j < 8; j++) { // reset the colors of board, double for loop
          for (int i = 0; i < 8; i++) {
            JPanel reset = (JPanel) chessBoard.getComponent((i * 8) + j);
            if (i % 2 == j % 2) {
              reset.setBackground(primaryCol);
            } else {
              reset.setBackground(Color.white);
            }
          }
        }
      }
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
    masterlist = blackgetAllMoves(board);
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
    masterlist = whitegetAllMoves(board);
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

  public boolean checkWhiteAttacked(Piece[][] b, int row, int col){
    ArrayList<Move> some = blackgetAllMoves(board);
    for (int k = 0; k < some.size(); k++) {
      int possr = some.get(k).r;
      int possc = some.get(k).c;
      if (possr == row && possc == col) {
        return true;
      }
    }
    return false;
  }

  public boolean checkBlackAttacked(Piece[][] b, int row, int col){
    ArrayList<Move> some = whitegetAllMoves(board);
    for (int k = 0; k < some.size(); k++) {
      int possr = some.get(k).r;
      int possc = some.get(k).c;
      if (possr == row && possc == col) {
        return true;
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
      if(b[oldrow][oldcol] != null) {
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
              5, 5, -10, -5, -5, -10, 5, 5,
              0, 0, 0, 20, 20, 0, 0, 0,
              5, 5, 10, 25, 25, 10, 5, 5,
              10, 10, 20, 30, 30, 20, 10, 10,
              50, 50, 50, 50, 50, 50, 50, 50,
              100, 100, 100, 100, 100, 100, 100, 100};

      if(p.name.contains("bPawn")){
        return pawntable[8*row + col];
      }
      else{
        return pawntable[8*(7-row)+col] * 0.8;
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
          total -= ((8 * b[i][j].value) + (posMultiplier2(i, j, b[i][j])));
        } else if (b[i][j] != null && b[i][j].color == "black") {
          total += (8 * b[i][j].value) + (posMultiplier2(i, j, b[i][j]));
        }
      }
    }

    if (checkwhiteking(board)) {
      total += 10;
    }
    if (checkblackking(board)){
      total -= 10;
    }
    if (WhiteGetSafeMoves(board).size() == 0 && checkwhiteking(board)){
      total = 500000;
    }
    if (WhiteGetSafeMoves(board).size() == 0 && !checkwhiteking(board)){
      total = 0;
    }
    if (BlackGetSafeMoves(board).size() == 0 && checkblackking(board)){
      total = -500000;
    }
    if (BlackGetSafeMoves(board).size() == 0 && !checkblackking(board)){
      total = 0;
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

    if (depth == 2) {
      ArrayList<Move> whiteSpace = WhiteGetSafeMoves(board);
      if (whiteSpace.size() == 0) { //check for mate
        mate = true;
      }
      if (mate != true) {
        for (int k = 0; k < whiteSpace.size(); k++) {
          double tempV = evaluatePosition(b, whiteSpace.get(k), 1);
          if (tempV < min) {
            min = tempV;
          }
        }
      }}
      if(mate != true){

        for (int i = 0; i < 8; i++) {
          for (int j = 0; j < 8; j++) {
            if (b[i][j] != null && b[i][j].color == "white") {
              total -= ((8 * b[i][j].value) + (posMultiplier2(i, j, b[i][j])));
            } else if (b[i][j] != null && b[i][j].color == "black") {
              total += (8 * b[i][j].value) + (posMultiplier2(i, j, b[i][j]));
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

  public ArrayList<Move> smartSort(Piece[][] b, ArrayList<Move> mlist){
    ArrayList<Double> values = new ArrayList<Double>(mlist.size());
    Map<Double, Move> mapie = new Hashtable<Double, Move>();

    Set<Double> keys = mapie.keySet();

    ArrayList<Double> keyCoList = new ArrayList<Double>();
    for(int k = 0; k < mlist.size(); k++){
      double evaluation = 0;
      Move m = mlist.get(k);
      int newrow = m.r;
      int newcol = m.c;
      int oldrow = m.piecemove.row;
      int oldcol = m.piecemove.col;
      if(b[oldrow][oldcol] != null) {
        /**
        Piece original = b[newrow][newcol];
        b[newrow][newcol] = b[oldrow][oldcol];
        b[newrow][newcol].row = newrow;
        b[newrow][newcol].col = newcol;
        b[oldrow][oldcol] = null;
**/
        evaluation = evaluateMove2(b, m, 2);
        /**
        b[oldrow][oldcol] = b[newrow][newcol];
        b[oldrow][oldcol].row = oldrow;
        b[oldrow][oldcol].col = oldcol;
        b[newrow][newcol] = original;
**/
        while (keyCoList.contains(evaluation)) {
          evaluation += 0.01;
        }
      }
      keyCoList.add(evaluation);
      mapie.put(evaluation, m);
     // System.out.println("Added " + m + " with a " + evaluation);
    }
    Collections.sort(keyCoList, Collections.reverseOrder());
    for(int k = 0; k < keyCoList.size(); k++){
      //System.out.println(keyCoList.get(k));
    }
    ArrayList<Move> sortedNow = new ArrayList<Move>();

    for(int i = 0; i < mlist.size(); i++){
      double keyIndex = keyCoList.get(i);
      sortedNow.add(mapie.get(keyIndex));
     // System.out.println(mapie.get(keyIndex) + ", w/ " + keyIndex);
      //mapie.remove(keyIndex, mapie.get(keyIndex));
      //keyColList.remove(0);
    }
    return sortedNow;
    }

  public double evaluateMove2(Piece[][] b, Move m, int depth) { //if depth odd, maximizing player
    double optimalVal;
    if(depth == 0){
      double n = 0;
      int newrow = m.r;
      int newcol = m.c;
      int oldrow = m.piecemove.row;
      int oldcol = m.piecemove.col;
      if(b[oldrow][oldcol] != null) {
        Piece original = b[newrow][newcol];
        b[newrow][newcol] = b[oldrow][oldcol];
        b[newrow][newcol].row = newrow;
        b[newrow][newcol].col = newcol;
        b[oldrow][oldcol] = null;
        n = evalBoard(b);
        b[oldrow][oldcol] = b[newrow][newcol];
        b[oldrow][oldcol].row = oldrow;
        b[oldrow][oldcol].col = oldcol;
        b[newrow][newcol] = original;
      }
      return n;
    }
    optimalVal = 100000;
      int newrow = m.r;
      int newcol = m.c;
      int oldrow = m.piecemove.row;
      int oldcol = m.piecemove.col;
      if(b[oldrow][oldcol] != null) {
        Piece original = b[newrow][newcol];
        b[newrow][newcol] = b[oldrow][oldcol];
        b[newrow][newcol].row = newrow;
        b[newrow][newcol].col = newcol;
        b[oldrow][oldcol] = null;
        //System.out.println("Newrow, newCol is " + b[newrow][newcol].name);
        if (true) {
          ArrayList<Move> whiteSpace = WhiteGetSafeMoves(b);
          for(int i = 0; i < whiteSpace.size(); i++){
            double jolly = evaluateMove2(b, whiteSpace.get(i), 0);
            if(jolly < optimalVal){
              optimalVal = jolly;
            }
          }

        }
        b[oldrow][oldcol] = b[newrow][newcol];
        b[oldrow][oldcol].row = oldrow;
        b[oldrow][oldcol].col = oldcol;
        b[newrow][newcol] = original;
      }

    return optimalVal;

  }

  public double[] evaluatePosition2(Piece[][] b, ArrayList<Move> mlist, int depth, double alpha, double beta, boolean isWhite) { //if depth odd, maximizing player
    int counter = 0;
    int totalSpace = 0;
    int benchMark = 0;
    if((difficulty == 3 && depth == 3) || depth == 5){
      totalSpace = mlist.size();
      benchMark = totalSpace / 5;
    }
    double optimalVal;
    double val;
    int optimalIndex = 0; //the best move
    if((difficulty < 4 && depth == 0) || (difficulty == 4 && depth == 1)){
      double n = evalBoard(b);
      ultraBoardHash(b);
      ultraVal.add(n);
      return (new double[]{n, 0});
    }
    if(!isWhite){
      optimalVal = -10000;
    }
    else{
      optimalVal = 10000;
    }
    //ArrayList<Move> mlist = smartSort(b, blist);
    for(int i = 0; i < mlist.size(); i++){
      if(difficulty == 4){
        long elapsed = System.currentTimeMillis() - FuncStartTime;
        if(elapsed > 30000) {
          break;
        }
      }

      Move m = mlist.get(i);
      int newrow = m.r;
      int newcol = m.c;
      int oldrow = m.piecemove.row;
      int oldcol = m.piecemove.col;
      if(b[oldrow][oldcol] != null) {
        Piece original = b[newrow][newcol];
        b[newrow][newcol] = b[oldrow][oldcol];
        b[newrow][newcol].row = newrow;
        b[newrow][newcol].col = newcol;
        b[oldrow][oldcol] = null;
        if (!isWhite) {
          ArrayList<Move> whiteSpace = WhiteGetSafeMoves(b);
          val = evaluatePosition2(b, whiteSpace, depth - 1, alpha, beta, true)[0];
        } else {
          ArrayList<Move> blackSpace = BlackGetSafeMoves(b);
          val = evaluatePosition2(b, blackSpace, depth - 1, alpha, beta, false)[0];
        }

        if (!isWhite) { //if looking at white
          if (val > optimalVal) {
            optimalVal = val;
            optimalIndex = i;
          }
          alpha = Math.max(alpha, val);
        } else {
          if (val < optimalVal) {
            optimalVal = val;
            optimalIndex = i;
          }
          beta = Math.min(beta, val);
        }
        b[oldrow][oldcol] = b[newrow][newcol];
        b[oldrow][oldcol].row = oldrow;
        b[oldrow][oldcol].col = oldcol;
        b[newrow][newcol] = original;
        if (beta <= alpha) {
          break;
        }
      }
      else{
        break;
      } //do visual stuff

      if(depth > 5){
        counter++;
        int tempcount = counter;
        for(int k = 0; k < tempcount; k++){
          System.out.print("|*");
        }
        for(int h = tempcount; h < 6; h++ ){
          System.out.print("|-");
        }
        System.out.println("|");
      }
      if((difficulty == 3 && depth == 3) || depth == 5){
        counter++;
        if(counter == benchMark){
          System.out.println("|*|-|-|-|-|");
        }
        if(counter == 2*benchMark){
          System.out.println("|*|*|-|-|-|");
        }
        if(counter == 3*benchMark){
          System.out.println("|*|*|*|-|-|");
        }
        if(counter == 4*benchMark){
          System.out.println("|*|*|*|*|-|");
        }
        if(counter == 5*benchMark){
          System.out.println("|*|*|*|*|*|");
        }


      }
    }
    return new double[]{optimalVal, optimalIndex};

  }

  public void mouseDragged(MouseEvent me) { // while mouse moving, piece should follow
    if(gameOver){
      return;
    }
    if (chessPiece == null)
      return;

    chessPiece.setLocation(me.getX() + xAdjustment, me.getY() + yAdjustment);

  }

  // Drop the chess piece back onto the chess board

  public void mouseReleased(MouseEvent e) {
    if(gameOver && nowSafe){
      //JPanel goback = (JPanel) chessBoard.getComponent(pieceR * 8 + pieceC);
      //chessPiece.setVisible(true);
     // goback.add(chessPiece);
      //return;
    }
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
          boolean captured1 = false;
          String captName = "";
          if(board[r][C] != null){
            captured1 = true;
            blackPieces --;
            captName = board[r][C].name;
          }
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
          String tempName = pieceR + "," + pieceC + ";" + r + "," + C + board[pieceR][pieceC].name;
          if(captured1 == true){
            tempName += ("X" + captName);
          }

          if(BlackGetSafeMoves(board).size() < 1 && checkblackking(board)){
            tempName += "#";
          }
          else if(checkblackking(board)){
            tempName += "+";
          }

          if(moveCounter < finalMoveCounter){
            while((2*moveCounter) < allPerfMoves.size()){
              allPerfMoves.remove(2*moveCounter);

            }
            while((2*moveCounter) < boardHash.size()){
              boardHash.remove(2*moveCounter);

            }
            finalMoveCounter = moveCounter;
            whiteMoves.append("\n---New Branch---\n");
          }

          allPerfMoves.add(tempName);
          System.out.println(tempName);
          board[r][C] = board[pieceR][pieceC];
          board[r][C].row = r;
          board[r][C].col = C;
          board[pieceR][pieceC] = null;
          updateBoardHash(board);
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
              TimerW.stop();
              endGame("White", false, false);
            } else if (checkk == true && limit == false) { // moves, in check
              System.out.println("\n---Black is in check!---");
              colorKing(board, "b", "yellow");
            } else if (checkk == false && limit == true) {
              System.out.println("\n---Stalemate; it's a draw!");
              colorKing(board, "b", "gray");
              colorKing(board, "w", "gray");
              gameOver = true;
              TimerW.stop();
              endGame("None", false, false);
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
              TimerW.stop();
              endGame("Black", false, false);
            } else if (checkk == true && limit == false) { // moves, in check
              System.out.println("\n---White is in check!---");
              colorKing(board, "w", "yellow");
            } else if (checkk == false && limit == true) {
              System.out.println("\n---Stalemate; it's a draw!");
              colorKing(board, "b", "gray");
              colorKing(board, "w", "gray");
              gameOver = true;
              endGame("None", false, false);
              TimerW.stop();
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
    else {

      JPanel goback = (JPanel) chessBoard.getComponent(pieceR * 8 + pieceC);
      chessPiece.setVisible(true);
      goback.add(chessPiece);
    }


  }




  public void blackMove(Piece[][] b){
        if(gameOver){
          return;
        }
        TimerW.stop();
        timeLeftw -= timePassed;
        timePassed = 0;

        long startTime = System.currentTimeMillis();



        movelist = BlackGetSafeMoves(board);
        int maxIndex = 0;
        double maxValue = -500;
        int rand = 1;
        if(finalMoveCounter < 2) { //make openings more varied
          ArrayList<Move> opening = new ArrayList<Move>();
          for (int g = 0; g < movelist.size(); g++) {
            if(movelist.get(g).piecemove.row == 1 && (movelist.get(g).piecemove.col == 3 || movelist.get(g).piecemove.col == 4 )){
              opening.add(movelist.get(g));
            }
            else if((movelist.get(g).piecemove.name.contains("Knight")) && (movelist.get(g).c == 2 || movelist.get(g).c == 5)){
              opening.add(movelist.get(g));
            }
          }

          int rand1 = (int) (Math.random() * opening.size());
          for (int d = 0; d < movelist.size(); d++) {
            if(movelist.get(d).equals(opening.get(rand1))){
              rand = d;
            }
          }
        } //GET THE MOVE
        else {

          if (difficulty == 1) { //Easy level; just choose random
           rand = (int) (Math.random() * movelist.size());

            /**
           for(int m = 0; m < movelist.size(); m++){
             if(movelist.get(m).piecemove.name.contains("Knight")){
             if(movelist.get(m).r <= 2 && movelist.get(m).c >= 6){
             rand = m;
             }
             } //THIS HELPS CHECK FOR REPETITION YO
             }**/

          }
          else if (difficulty == 2) { //Medium level; goes two layers deep
            rand = (int) evaluatePosition2(board, movelist, 2, -50000, 50000, false)[1];
            /**
            for (int t = 0; t < movelist.size(); t++) {
              double curr = evaluatePosition(board, movelist.get(t), 1);
              if (curr > maxValue) {
                maxValue = curr;
                maxIndex = t;
              }
            }
             **/
            //rand = maxIndex;
          }
          else if (difficulty == 3){ //Intermediate level; goes three moves deep + alpha/beta pruning
            System.out.println("AI Progress Bar\n");
            System.out.println("|-|-|-|-|-|");
            movelist = smartSort(board, movelist);
            //System.out.println("We have" + movelist.size() + " for new, but " + movelist.size() + " for old");
            rand = (int) evaluatePosition2(board, movelist, 3, -50000, 50000, false)[1];
          }

          else {
            difficulty = 3;
            //Collections.shuffle(movelist);

            int a = 6; //default
            if(whitePieces + blackPieces < 10 && movelist.size() < 20){
              a = 30;
            }
            else if(whitePieces < 6 || blackPieces < 6){
              a = 14;
            }
            else{
              a = 10;
            }
            ArrayList<Move> supreme = new ArrayList<Move>();
            ArrayList<Move> temp = smartSort(board, movelist);
            for(int i = 0; i < Math.min(movelist.size(), a); i++){
              supreme.add(temp.get(i));
            }
            System.out.println("AI Progress Bar\n");
            System.out.println("|-|-|-|-|-|");
            difficulty = 4;
            FuncStartTime = System.currentTimeMillis();
            int layerhigh = (int) evaluatePosition2(board, supreme, 5, -50000, 50000, false)[1];
            difficulty = 3;
            for(int k = 0; k < movelist.size(); k++){

              if(movelist.get(k).piecemove.name.equals(supreme.get(layerhigh).piecemove.name)){

                if(movelist.get(k).r == supreme.get(layerhigh).r && movelist.get(k).c == supreme.get(layerhigh).c){

                  rand = k;
                }
              }
            }
            difficulty = 4;


            //
          }
        }

        //once move is obtained, easy to implement
        pieceR = movelist.get(rand).piecemove.row;
        pieceC = movelist.get(rand).piecemove.col;
        int r = movelist.get(rand).r;
        int C = movelist.get(rand).c;
        JLabel piece7 = new JLabel(new ImageIcon("bKnight.png"));
        JPanel panel7 = (JPanel) chessBoard.getComponent(0);

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

        String temp2 = pieceR + "," + pieceC + ";" + r + "," + C + board[pieceR][pieceC].name;
        if(board[r][C] != null){
          whitePieces --;
          temp2 += ("X" + board[r][C].name);
        }
        board[r][C] = null;

        board[r][C] = board[pieceR][pieceC];
        board[r][C].row = r;
        board[r][C].col = C;
        board[pieceR][pieceC] = null;

        checkSpecials(board, r, C);

        if(WhiteGetSafeMoves(board).size() < 1 && checkwhiteking(board)){
          temp2 += "#";
        }
        else if(checkwhiteking(board)){

          temp2 += "+";
        }
        //start move 0; do white then black, then move 1
        //we go back from move 15 to move 5; must delete moves 6->15 so array values 12->30



        allPerfMoves.add(temp2);
        updateMoveList(allPerfMoves.get(allPerfMoves.size()-2), allPerfMoves.get(allPerfMoves.size()-1), finalMoveCounter);
        updateBoardHash(board);


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
        moveCounter++;
        finalMoveCounter++;
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
          TimerW.stop();
          endGame("Black", false, false);
        } else if (checkk == true && limit == false) { // moves, in check
          System.out.println("\n---White is in check!---");
          colorKing(board, "w", "yellow");
        } else if (checkk == false && limit == true) {
          System.out.println("\n---Stalemate; it's a draw!");
          colorKing(board, "b", "gray");
          colorKing(board, "w", "gray");
          gameOver = true;
          TimerW.stop();
          endGame("None", false, false);
        }

        if(checkBoardHash()){
          System.out.println("\n---Stalemate; it's a draw by 3-move repetition!");
          colorKing(board, "b", "gray");
          colorKing(board, "w", "gray");
          gameOver = true;
          endGame("None", false, true);
          TimerW.stop();
        }

        for (int g = 0; g < allPerfMoves.size(); g++){
          //System.out.print(allPerfMoves.get(g) + " | ");
          //System.out.println(boardHash.get(g));
        }


  TimerW.start();
        long endTime = System.currentTimeMillis();
        int timePasses = (int) ((endTime - startTime) / 1000);
        if(timePasses == 0){
          timePasses = 1;
        }
        System.out.println(timePasses);
        timeLeftb -= timePasses;
    int currMin = (int) (timeLeftb) / 60;
    int currSec = (timeLeftb) % 60;
    String nah = "";
    if(currSec < 10){
      nah = "0" + currSec;
    }
    else{
      nah = String.valueOf(currSec);
    }
    String res = currMin + ":" + nah;
    blackTimer.setText(res);


    if(timeLeftb < 0) {
      colorKing(board, "b", "red");
      System.out.println("White wins on time!");
      gameOver = true;
      TimerW.stop();
      endGame("White", true, false);
    }

    if(whitePieces + blackPieces == 2){
      System.out.println("\n---Stalemate; it's a draw by lack of material");
      colorKing(board, "b", "gray");
      colorKing(board, "w", "gray");
      gameOver = true;
      endGame("None", false, false);
    }

  }

  public void performMove(String s){
    if(s == "None"){
      return;
    }
    //7,2;3,6wBishopXbPawn
    int pieceR = Character.getNumericValue(s.charAt(0));
    String name = "";
    String capturedName = "";
    int pieceC = Character.getNumericValue(s.charAt(2));
    int newR = Character.getNumericValue(s.charAt(4));
    int newC = Character.getNumericValue(s.charAt(6));
    if(s.contains("X")){
      int k = s.indexOf("X");
      name = s.substring(7, k);
      capturedName = s.substring(k);
    }
    else {
      name = s.substring(7);
    }
    JLabel piece7 = new JLabel(new ImageIcon("bKnight.png"));
    JPanel panel7 = (JPanel) chessBoard.getComponent(0);
    piece7 = new JLabel(new ImageIcon("ChessRec/" + board[pieceR][pieceC].name+".png"));
    panel7 = (JPanel) chessBoard.getComponent(8*newR+newC);
    panel7.removeAll();
    panel7.revalidate();
    panel7.repaint();
    panel7.add(piece7);
    panel7 = (JPanel) chessBoard.getComponent(8*pieceR + pieceC);
    panel7.removeAll();
    panel7.revalidate();
    panel7.repaint();

    board[newR][newC] = null;

    board[newR][newC] = board[pieceR][pieceC];
    board[newR][newC].row =newR;
    board[newR][newC].col = newC;
    board[pieceR][pieceC] = null;

    checkSpecials(board, newR, newC);

    if (board[newR][newC].color == "white") { // if white's turn
      boolean checkk = false; // In Check?
      boolean limit = false; // Are there available moves?
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
        //System.out.println("\n---White Wins by checkmate!---");
        colorKing(board, "b", "red");
        playernum = 5;
       // gameOver = true;
        //TimerW.stop();
       // endGame("White", false, false);
      } else if (checkk == true && limit == false) { // moves, in check
       // System.out.println("\n---Black is in check!---");
        colorKing(board, "b", "yellow");
      } else if (checkk == false && limit == true) {
       // System.out.println("\n---Stalemate; it's a draw!");
        colorKing(board, "b", "gray");
        colorKing(board, "w", "gray");
      //  gameOver = true;
      //  TimerW.stop();
       // endGame("None", false, false);
      }
    }

    else {
      /**
      boolean checkk = false; // r we in check?
      boolean limit = false; // r there no moves left?
      playernum = 1;
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
        gameOver = true;
      }
      **/
    }
    int compOrig = (pieceR * 8) + pieceC;
    int compNew = (newR * 8) + newC;
    JPanel template5 = (JPanel) chessBoard.getComponent(compOrig);
    JPanel template6 = (JPanel) chessBoard.getComponent(compNew);
    template5.setBackground(Color.orange);
    template6.setBackground(Color.orange.darker());


    /**
    if(true){
    try {
      Robot bum = new Robot();
      Point p = MouseInfo.getPointerInfo().getLocation();
      bum.mouseMove(p.x+10,p.y);
    } catch (AWTException awtException) {
      awtException.printStackTrace();
    }

  } **/



  }

  public void reverseMove(String s){
    if(s == "None"){
      return;
    }
    String temp = "";
    String name = "";
    int pieceR;
    int pieceC;
    int newR;
    int newC;
    String capturedName = "";
    if(s.contains("X")){
      int index = s.indexOf("X");
      name = s.substring(7, index);
      capturedName = s.substring(index+1);
      pieceR = Character.getNumericValue(s.charAt(0));
      pieceC = Character.getNumericValue(s.charAt(2));
      newR = Character.getNumericValue(s.charAt(4));
      newC = Character.getNumericValue(s.charAt(6));
      temp = newR + "," + newC + ";" + pieceR + "," + pieceC + name;
      performMove(temp);
      JLabel piece7 = new JLabel(new ImageIcon("bKnight.png"));
      JPanel panel7 = (JPanel) chessBoard.getComponent(0);
      piece7 = new JLabel(new ImageIcon("ChessRec/" + capturedName+".png"));
      panel7 = (JPanel) chessBoard.getComponent(8*newR+newC);
      panel7.removeAll();
      panel7.revalidate();
      panel7.repaint();
      panel7.add(piece7);
      String color = "";
      if(capturedName.charAt(0) == 'w'){
        color = "white";
      }
      else{
        color = "black";
      }
      if(capturedName.contains("Pawn")){
        board[newR][newC] = new Pawn(newR, newC, color, false, false);
      }
      if(capturedName.contains("Knight")){
        board[newR][newC] = new Knight(newR, newC, color);
      }
      if(capturedName.contains("Bishop")){
        board[newR][newC] = new Bishop(newR, newC, color);
      }
      if(capturedName.contains("Rook")){
        board[newR][newC] = new Rook(newR, newC, color, false);
      }
      if(capturedName.contains("Queen")){
        board[newR][newC] = new Queen(newR, newC, color);
      }
      board[newR][newC].row = newR;
      board[newR][newC].col = newC;

    }
    else{
      name = s.substring(7);
      pieceR = Character.getNumericValue(s.charAt(0));
      pieceC = Character.getNumericValue(s.charAt(2));
      newR = Character.getNumericValue(s.charAt(4));
      newC = Character.getNumericValue(s.charAt(6));
      temp = newR + "," + newC + ";" + pieceR + "," + pieceC + name;
      performMove(temp);
    }
    //check for castling
    if(board[pieceR][pieceC].name.contains("King")){
      if(pieceC - newC == -2){
        String castleMove = pieceR + ",5;" + pieceR + ",7" + board[pieceR][pieceC+1].name;
        performMove(castleMove);
        board[pieceR][pieceC].moved = false;
        board[pieceR][pieceC].passant = false;
        board[pieceR][7].moved = false;
      }
      if(newC - pieceC == -2){
        String castleMove = pieceR + ",3;" + pieceR + ",0" + board[pieceR][pieceC-1].name;
        performMove(castleMove);
        board[pieceR][pieceC].moved = false;
        board[pieceR][pieceC].passant = false;
        board[pieceR][0].moved = false;
      }
    }
    //check for en passant
    if(board[pieceR][pieceC].name.contains("Pawn")) {
      if (Math.abs(pieceC - newC) == 1 && Math.abs(pieceR - newR) == 1) {
        if (!(s.contains("X"))) {

          if (board[pieceR][pieceC].color.equals("white")) {
            board[newR + 1][newC] = new Pawn(newR + 1, newC, "black", true, false);
            board[newR + 1][newC].row = newR + 1;
            board[newR + 1][newC].col = newC;
            JLabel piece10;
            JPanel panel10;
            piece10 = new JLabel(new ImageIcon("ChessRec/bPawn.png"));
            panel10 = (JPanel) chessBoard.getComponent(8*(newR+1)+newC);
            panel10.removeAll();
            panel10.revalidate();
            panel10.repaint();
            panel10.add(piece10);


          } else {
            board[newR - 1][newC] = new Pawn(newR - 1, newC, "white", true, false);
            board[newR - 1][newC].row = newR - 1;
            board[newR - 1][newC].col = newC;
            JLabel piece10;
            JPanel panel10;
            piece10 = new JLabel(new ImageIcon("ChessRec/wPawn.png"));
            panel10 = (JPanel) chessBoard.getComponent(8*(newR-1)+newC);
            panel10.removeAll();
            panel10.revalidate();
            panel10.repaint();
            panel10.add(piece10);
          }
        }
      }
    }
    //check for pawn promotion / DE-motion
    boolean pawnPromo = false;
    if(s.contains("X")){
      int ind = s.indexOf("X");
      if(s.substring(7, ind).contains("Pawn") && board[pieceR][pieceC].name.contains("Queen")){
        pawnPromo = true;
      }
    }
    else{
      if(s.substring(7).contains("Pawn") && board[pieceR][pieceC].name.contains("Queen")){
        pawnPromo = true;
      }
    }

    if(pawnPromo){

      String pawnColor = board[pieceR][pieceC].color;
      String named = pawnColor;
      if(s.contains("w")){
        named = "wPawn";
      }
      else{
        named = "bPawn";
      }
      JLabel piece10;
      JPanel panel10;
      piece10 = new JLabel(new ImageIcon("ChessRec/" + named+".png"));
      panel10 = (JPanel) chessBoard.getComponent(8*pieceR+pieceC);
      panel10.removeAll();
      panel10.revalidate();
      panel10.repaint();
      panel10.add(piece10);
      board[pieceR][pieceC] = new Pawn(pieceR, pieceC, pawnColor, false, true);
      board[pieceR][pieceC].row = pieceR;
      board[pieceR][pieceC].col = pieceC;

    }
    int compNew = (pieceR * 8) + pieceC;
    int compOrig = (newR * 8) + newC;
    JPanel template5 = (JPanel) chessBoard.getComponent(compOrig);
    JPanel template6 = (JPanel) chessBoard.getComponent(compNew);
    template5.setBackground(Color.orange);
    template6.setBackground(Color.orange.darker());


  }


  public void updateMoveList(String whiteInput, String blackInput, int n){
    String result = "";
    String alphabet = "abcdefgh";
    n++;
    result += (n + ". ");

    char d = whiteInput.charAt(8);
    if(d == 'K' && whiteInput.charAt(9) == 'n'){
      d = 'N';
    }
    if(d != 'P') {
      result += d;
    }

    if(d == 'N'){
      result += alphabet.charAt(Character.getNumericValue(whiteInput.charAt(2)));
    }
    if(d == 'P' && whiteInput.contains("X")){
      result += alphabet.charAt(Character.getNumericValue(whiteInput.charAt(2)));
    }
    if(whiteInput.contains("X")){
      result += "x";
    }
    result += alphabet.charAt(Character.getNumericValue(whiteInput.charAt(6)));
    int val = Character.getNumericValue(whiteInput.charAt(4));
    result += Integer.toString(8-val);

    if(whiteInput.contains("#")){
      result += "#";
    }
    if(whiteInput.contains("+")){
      result += "+";
    }
    if(whiteInput.contains("King")){
      if(whiteInput.charAt(2) == '4' && whiteInput.charAt(6) == '6'){
        result = (n + ". ");
        result += "O-O";
      }
      if(whiteInput.charAt(2) == '4' && whiteInput.charAt(6) == '2'){
        result = (n + ". ");
        result += "O-O-O";
      }
    }

    String result2 = "";
    if(blackInput == "loss"){
      result2 = "1 - 0";
    }
    else {
      char e = blackInput.charAt(8);
      if (e == 'K' && blackInput.charAt(9) == 'n') {
        e = 'N';
      }
      if (e != 'P') {
        result2 += e;
      }


      if (e == 'N') {
        result2 += alphabet.charAt(Character.getNumericValue(blackInput.charAt(2)));
      }
      if (e == 'P' && blackInput.contains("X")) {
        result2 += alphabet.charAt(Character.getNumericValue(blackInput.charAt(2)));
      }
      if (blackInput.contains("X")) {
        result2 += "x";
      }
      result2 += alphabet.charAt(Character.getNumericValue(blackInput.charAt(6)));
      int val2 = Character.getNumericValue(blackInput.charAt(4));
      result2 += Integer.toString(8 - val2);

      if (blackInput.contains("#")) {
        result2 += "#";
      }
      if (blackInput.contains("+")) {
        result2 += "+";
      }
      if (blackInput.contains("King")) {
        if (blackInput.charAt(2) == '4' && blackInput.charAt(6) == '6') {
          result2 = "O-O";
        }
        if (blackInput.charAt(2) == '4' && blackInput.charAt(6) == '2') {
          result2 = "O-O-O";
        }
      }
    }
    //now repeat for black

    int lengthy = result.length();
    whiteMoves.append(result);
    for(int i = 0; i < (16 - lengthy); i++){
      whiteMoves.append(" ");
    }
    whiteMoves.append(result2 + "\n");



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
        if ((r == 7 && C == 6)){
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

        else if ((r == 7 && C == 2)) {
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
        if ((r == 0 && C == 6)) {
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

        else if ((r == 0 && C == 2)) {
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
          reset.setBackground(primaryCol);
        } else {
          reset.setBackground(Color.white);
        }
      }
    } // RESET BOARD

  }

  public void updateBoardHash(Piece[][] b){
    String key = "";
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        if(b[i][j] != null) {
          key += b[i][j].name.charAt(0);
          if (b[i][j].name.contains("Knight")) {
            key += "N";
          } else {
            key += b[i][j].name.charAt(1);
          }
        }
        else{
          key += "yy"; //empty
        }
      }
    }
    boardHash.add(key);
  }


  public void ultraBoardHash(Piece[][] b){
    String key = "";
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        if(b[i][j] != null) {
          key += b[i][j].name.charAt(0);
          if (b[i][j].name.contains("Knight")) {
            key += "N";
          } else {
            key += b[i][j].name.charAt(1);
          }
        }
        else{
          key += "yy"; //empty
        }
      }
    }
    if(ultraHash.size() > 500000){
      ultraHash.remove(0);
      ultraVal.remove(0);
    }
    ultraHash.add(key);
  }

  public boolean checkBoardHash(){
    ArrayList<String> kam = boardHash;
    Collections.sort(kam);
    int dupl = 0;
    for(int i = 1; i < kam.size(); i++){
      if(kam.get(i).equals(kam.get(i-1))){
        dupl++;
      }
      else{
        dupl = 0;
      }
      if(dupl == 3){
        return true;
      }
    }
    return false;
  }

  public void endGame(String player, boolean time, boolean threeMove){
    gameOver = true;
    TimerW.stop();
    TimerB.stop();
    nowSafe = true;
    if(timeLeftw <= 0){
      whiteTimer.setText("0:00");
      whiteTimer.setBackground(Color.red);
    }
    if(timeLeftb <= 0){
      blackTimer.setText("0:00");
      blackTimer.setBackground(Color.red);
    }
    JFrame frame5 = new JFrame("Ending Message");
    frame5.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame5.setSize(300, 100);
    frame5.setAlwaysOnTop(true);
    frame5.setLocationRelativeTo( null );
    frame5.setLayout(new BorderLayout());

    JLabel endMsg = new JLabel("", SwingConstants.CENTER);
    endMsg.setFont(new Font("Serif", Font.PLAIN, 18));
    if(player == "None"){
      if(threeMove){
        endMsg.setText("Stalemate by 3-move repetition!");
      }
      else{
        endMsg.setText("Stalemate! Neither player wins.");
      }
    }
    else{
      if(time){
        endMsg.setText(player + " has won on time!");
      }
      else{
        endMsg.setText(player + " has won by checkmate!");
      }
    }
    frame5.add(endMsg, BorderLayout.CENTER);
    endMsg.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    endMsg.setBackground(Color.white);
    endMsg.setOpaque(true);
    frame5.setVisible(true);
    if(player == "White"){
      moveCounter++;
      finalMoveCounter++;
      finalMoveNum = finalMoveCounter;
      updateMoveList(allPerfMoves.get(allPerfMoves.size()-1), "loss", finalMoveCounter);
      allPerfMoves.add("None");
    } else if (player == "Black"){
      whiteMoves.append("\n0-1");
    }
    else{
      whiteMoves.append("1/2-1/2");
    }
  }

  public void mouseClicked(MouseEvent e) {
    if(gameOver){
      return;
    }
    if(playernum == 0){
      return;
    }
    if(playernum == 1) {
      Component d = chessBoard.findComponentAt(e.getX(), e.getY());
      int newx = 0;
      int newy = 0;
      if (!(d instanceof JPanel)) {
        Point parentLocation2 = d.getParent().getLocation(); // set piece to mouse location, use xAdjustment and yAdjustment
        // to adjust for variation from center

        newx = parentLocation2.y / 75;
        newy = parentLocation2.x / 75;
        if (board[newx][newy].color != "black") {
          return;
        }
        int compnum2 = (newx * 8) + newy;
        JPanel template3 = (JPanel) chessBoard.getComponent(compnum2);
        if ((template3.getBackground() == Color.white || template3.getBackground() == primaryCol)) {
          return;
        }
      }
      if (d instanceof JPanel) {
        if ((d.getBackground() == Color.white || d.getBackground() == primaryCol)) {
          return;
        }
        Point parentLocation2 = d.getLocation(); // set piece to mouse location, use xAdjustment and yAdjustment
        // to adjust for variation from center
        newy = parentLocation2.x / 75;
        newx = parentLocation2.y / 75;
      }



      boolean captured2 = false;
      String captName2 = "";
      if (board[newx][newy] != null) {
        captured2 = true;
        blackPieces --;
        captName2 = board[newx][newy].name;
      }
      board[newx][newy] = null;
      String tempName2 = clickRow + "," + clickCol + ";" + newx + "," + newy + board[clickRow][clickCol].name;
      if (captured2 == true) {
        tempName2 += ("X" + captName2);
      }

      if (BlackGetSafeMoves(board).size() < 1 && checkblackking(board)) {
        tempName2 += "#";
      }
      else if (checkblackking(board)) {
        tempName2 += "+";
      }
      if(moveCounter < finalMoveCounter){
        while((2*moveCounter) < allPerfMoves.size()){
          allPerfMoves.remove(2*moveCounter);

        }
        while((2*moveCounter) < allPerfMoves.size()){
          boardHash.remove(2*moveCounter);

        }
        finalMoveCounter = moveCounter;
        whiteMoves.append("\n---New Branch---\n");
      }
      allPerfMoves.add(tempName2);
      System.out.println(tempName2);



      JLabel piece9 = new JLabel(new ImageIcon("bKnight.png"));
      JPanel panel9 = (JPanel) chessBoard.getComponent(0);
      board[newx][newy] = null;
      piece9 = new JLabel(new ImageIcon("ChessRec/" + board[clickRow][clickCol].name + ".png"));
      panel9 = (JPanel) chessBoard.getComponent(8 * newx + newy);
      panel9.removeAll();
      panel9.revalidate();
      panel9.repaint();
      panel9.add(piece9);
      panel9 = (JPanel) chessBoard.getComponent(8 * clickRow + clickCol);
      panel9.removeAll();
      panel9.revalidate();
      panel9.repaint();
      board[newx][newy] = board[clickRow][clickCol];
      board[newx][newy].row = newx;
      board[newx][newy].col = newy;
      board[clickRow][clickCol] = null;
      updateBoardHash(board);
      checkSpecials(board, newx, newy);

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
          TimerW.stop();
          endGame("White", false, false);
        } else if (checkk == true && limit == false) { // moves, in check
          System.out.println("\n---Black is in check!---");
          colorKing(board, "b", "yellow");
        } else if (checkk == false && limit == true) {
          System.out.println("\n---Stalemate; it's a draw!");
          colorKing(board, "b", "gray");
          colorKing(board, "w", "gray");
          gameOver = true;
          TimerW.stop();
          endGame("None", false, false);
        }
      }


      if (true) {
        try {
          Robot bum = new Robot();
          Point p = MouseInfo.getPointerInfo().getLocation();
          bum.mouseMove(p.x + 10, p.y);
        } catch (AWTException awtException) {
          awtException.printStackTrace();
        }
      }


    }

}

  public void mouseMoved(MouseEvent e) {
    if (gameOver == true) {
      return;
    }
    if(playernum == 0 && gameOver == false) {
      blackMove(board);
    }

  }


  public void mouseEntered(MouseEvent e) {

  }

  public void mouseExited(MouseEvent e) {

  }

}


