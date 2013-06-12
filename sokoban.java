import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;

//CCPS109 - Computer Science I
//August 2012
//Final Project: Sokoban
//Provided Files: m1.txt, LevelReader.java, Tiles (enum) 

public class Sokoban extends JPanel {
    private LevelReader lr;
    private Contents[][] board;
    private int xpos, ypos, moveCount, time, currentLevel;
    private JLabel moves, levelLabel, timer, instructions;
    private Timer t;
    private JFrame f;
    private Image i1, i2, i3, i4, i5, i6, i7;
    
    public Sokoban(String fileName, JFrame f) {
        lr = new LevelReader();
        lr.readLevels(fileName);
        moves = new JLabel("");
        levelLabel = new JLabel("");
        timer = new JLabel("");
        instructions = new JLabel("Arrow Keys:Move Player, 'R':Restart Level, 'N':Next Level");
        this.f = f;
        initLevel(currentLevel); 
        this.add(levelLabel);
        this.add(timer);
        this.add(moves); 
        this.add(instructions);
        this.addKeyListener(new MyKeyListener());
        this.setFocusable(true);
        this.requestFocus();      
                          
        t = new Timer(1000, new MyActionListener());
        t.start();
        //tiles
          i1 = Toolkit.getDefaultToolkit().getImage("EMPTY.jpg");
        MediaTracker m = new MediaTracker(this);
        m.addImage(i1, 0);
          i2 = Toolkit.getDefaultToolkit().getImage("WALL.jpg");
        m.addImage(i2, 0);    
          i3 = Toolkit.getDefaultToolkit().getImage("PLAYER.jpg");   
        m.addImage(i3, 0);
          i4 = Toolkit.getDefaultToolkit().getImage("BOX.jpg");
        m.addImage(i4, 0);
          i5 = Toolkit.getDefaultToolkit().getImage("GOAL.jpg");     
        m.addImage(i5, 0);  
          i6 = Toolkit.getDefaultToolkit().getImage("PLAYERONGOAL.jpg");
        m.addImage(i6, 0); 
          i7 = Toolkit.getDefaultToolkit().getImage("BOXONGOAL.jpg");     
        m.addImage(i7, 0);
        try { m.waitForAll(); } catch(InterruptedException e) { }
    }
    
    public void initLevel(int level) {
        board = new Contents[lr.getWidth(level)][lr.getHeight(level)];
        for (int row=0; row<lr.getWidth(level); row++) {
            for (int col=0; col<lr.getHeight(level); col++) {
                board[row][col] = lr.getTile(level, row, col);
                if (board[row][col]==Contents.PLAYERONGOAL || board[row][col]==Contents.PLAYER) { 
                    xpos=row; ypos=col;
                }
            }
        }
        moveCount=0;
        moves.setText("Moves: "+moveCount);
        levelLabel.setText("Level: "+(currentLevel+1));
        timer.setText("Time: "+time);
        this.setPreferredSize(new Dimension((board.length*50)+50,(board[0].length*50)+50));
        this.setBackground(Color.WHITE);
        f.pack();
        repaint();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;                  
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);                         
        for (int row=0; row<board.length; row++) {
            for (int col=0; col<board[row].length; col++) {
                 if (board[row][col] == Contents.EMPTY){            
                    g2.drawImage(i1,(row*50), (col*50)+50, this);
                }
                if (board[row][col] == Contents.WALL){            
                    g2.drawImage(i2,(row*50), (col*50)+50, this);
                }
                if (board[row][col] == Contents.PLAYER) {
                    g2.drawImage(i3,(row*50), (col*50)+50, this);
                }
                if (board[row][col] == Contents.BOX) {
                    g2.drawImage(i4,(row*50), (col*50)+50, this);
                }
                if (board[row][col] == Contents.GOAL) {
                    g2.drawImage(i5,(row*50), (col*50)+50, this);
                }
                if (board[row][col] == Contents.PLAYERONGOAL) {
                    g2.drawImage(i6,(row*50), (col*50)+50, this);
                }
                if (board[row][col] == Contents.BOXONGOAL) {
                   g2.drawImage(i7,(row*50), (col*50)+50, this);
                }
            }
        }
    }
    
    private class MyKeyListener implements KeyListener {   
    public void keyPressed(KeyEvent key) {     
        boolean keyPressed = false;        
        boolean onEmpty = board[xpos][ypos]==Contents.PLAYER;
        int dx=0;
        int dy=0;
        if (key.getKeyCode() == KeyEvent.VK_N) {
            if (currentLevel<154) {currentLevel+=1; initLevel(currentLevel);}}
        if (key.getKeyCode() == KeyEvent.VK_R) {initLevel(currentLevel);}
        if (key.getKeyCode() == KeyEvent.VK_UP) {dy=-1; keyPressed=true;}  
        if (key.getKeyCode() == KeyEvent.VK_DOWN) {dy=+1; keyPressed=true;}
        if (key.getKeyCode() == KeyEvent.VK_LEFT) {dx=-1; keyPressed=true;}
        if (key.getKeyCode() == KeyEvent.VK_RIGHT) {dx=+1; keyPressed=true;}
        boolean legalMove = board[xpos+dx][ypos+dy]!=Contents.WALL;
        if (board[xpos+dx][ypos+dy]==Contents.BOX || board[xpos+dx][ypos+dy]==Contents.BOXONGOAL) {
        if (board[xpos+(dx*2)][ypos+(dy*2)]!=Contents.EMPTY && board[xpos+(dx*2)][ypos+(dy*2)]!=Contents.GOAL) {legalMove = false;}}             
        if (keyPressed && legalMove){
            //3rd space
            if (board[xpos+dx][ypos+dy]==Contents.BOX || board[xpos+dx][ypos+dy]==Contents.BOXONGOAL) {
                board[xpos+(dx*2)][ypos+(dy*2)] = board[xpos+(dx*2)][ypos+(dy*2)]==Contents.EMPTY ?Contents.BOX:Contents.BOXONGOAL;
            }
            //2nd space
            board[xpos+dx][ypos+dy] = board[xpos+dx][ypos+dy]==Contents.GOAL || board[xpos+dx][ypos+dy]==Contents.BOXONGOAL 
            ?Contents.PLAYERONGOAL:Contents.PLAYER;            
            //first space
            if (onEmpty) {board[xpos][ypos] = Contents.EMPTY;} else {board[xpos][ypos] = Contents.GOAL;}    
            // increase counters, checkwin, repaint
            xpos+=dx;ypos+=dy;
            moveCount++;moves.setText("Moves: "+moveCount);            
            if (checkWin()) {
                if (currentLevel<154) {currentLevel+=1;initLevel(currentLevel);}}
            repaint();          
        }
    }   
    public void keyTyped(KeyEvent key) {}
    public void keyReleased(KeyEvent key) {}
    }
   
    private boolean checkWin(){
       boolean win = true;
       for (int row=0; row<board.length; row++) {
           for (int col=0; col<board[row].length; col++) {
            if (board[row][col]==Contents.BOX) {win=false;}
           }
       }
       return win;
    }
     
    private class MyActionListener implements ActionListener {
        public void actionPerformed(ActionEvent ae){
            time++;
            timer.setText("Time: "+time);
        }
    }  
    public void finalize() {t.stop();}
    
    public static void main(String[] args){
          JFrame f2 = new JFrame("Sokoban!");
          f2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
          f2.setLayout(new FlowLayout());
          f2.add(new Sokoban("m1.txt", f2));
          f2.pack();
          f2.setVisible(true);
    }
}
