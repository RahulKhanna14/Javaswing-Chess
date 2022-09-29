# SoloChess - JavaSwing Application

![2022-09-29_00-14-17 copy (2)](https://user-images.githubusercontent.com/58804439/192968036-e9de43b8-99b5-404b-9252-eab5118827e6.gif)

## Overview
A complex Full-Stack Chess Application allowing users to play a highly personalized game against a challenging AI. All move-sets and rules of chess are completely implemented, including pawn promotion, en passant, castling, three-fold repetition, etc. It features customizable settings including varying AI difficulty (~200 ELO - 1400 ELO), varying time controls, varying color pallete for the board, and unique optional challenges such as playing half-blind (can't see AI's pieces) or playing without one's queen.
 The game also maintains a FIDE-style move-list and allows move travesal, such as taking back a bad move. 

## Technologies and Algorithms Utilized
* Front-end interface was developed using JavaSwing GUI - JFrame, BorderLayout, GridLayout, and SwingWorker for asynchronous threading (W.I.P.)
* Back-end logic was developed using Java; inherited class system for pieces w/ 3000+ lines of code 
* Minimax Recursive algorithm with Alpha-Beta Pruning and Transposition Tables to construct AI; varying difficulty accomplished by varying depth of recursion 
* String Analysis to develop game settings based on user's request - accomplished by phrase-sets

## Journey to Completion
Altogether, this program took roughly 2 months to develop. A lot of time was spent learning JavaSwing syntax at the beginning, particularly in reference to MouseEvents and MouseListeners for the user interface. A parent class "Piece" was first built, followed by child classes for each individual piece. Individual moves were coded within these classes and then the (many) rules of chess were implemented. 

The first iteration of this program was 2-player in which the user controlled both white and black. After fixing all bugs in this version, a simple random-AI was introduced. This was refined to an AI that could look 2 moves deep and optimize its position using a thorough position evaluator function that takes piece value and piece position into account (matrices). After some research into current chess AI, this was generalized to a minimax algorithm with Alpha-Beta pruning to reduce time needed for operation executions. The highest difficulty is set to depth 4, on average taking 10-25 seconds per move. 



The "settings" tab was then implemented last, as I went step-by-step to increase the customization of the game for user enjoyement. 

![2022-09-29_00-12-14 (1)](https://user-images.githubusercontent.com/58804439/192968881-33d39463-5c26-47bb-a7b3-957bcff08f5d.gif)



The ability to go back one or more moves, and then go forward was particularly challenging; it was accomplished by storing a hash-table of all performed moves, and using a function that could reverse a given move. Special care had to be taken to account for cases such as castling or pawn promotion. 

![2022-09-29_00-19-48 (2)](https://user-images.githubusercontent.com/58804439/192968663-0ff88e0b-c55f-4841-b2f4-a04ffcf30123.gif)


## Future Steps for Improvement
The next key improvement is the full implementation of SwingWorker. Currently, all code operations are being run on a single thread, the main event dispatch thread. This can be problematic because it combines the back-end time-intensive operations with the front-end GUI changes. As a result, the front-end currently stalls while the AI is calculating its best move; this is why AI timer appears to "freeze" before correcting itself using System.currentTime. 

SwingWorker would allow the heavier back-end operations to be executed on a newly created thread, which lets the GUI update itself asynchrounsly instead of linearly with respect to move calculation. This would improve the interface considerably. 

*Current Ver: Project goals achieved; version 1.0 complete.*
