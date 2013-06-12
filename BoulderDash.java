import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.Random;
import java.io.*;
import javax.sound.sampled.*;

//CCPS209 - Computer Science II
//April 2013
//Final Project: Boulderdash
//Provided Files: levels.xml, BDTile.java (Tile enums) , BDLevelReader.java
//Grade: 100%

public class BoulderDash extends JPanel {

    final int WIDTH = 40; final int HEIGHT = 22;
    BDLevelReader lr;
    BDObject[][] board = new BDObject[WIDTH][HEIGHT];
    int xpos, ypos, dx, dy, topX, topY, xIdeal, yIdeal;
    int currentLevel, diamondsCollected, dLabel, slideTicks;
    boolean sliding, pushing, legalMove;
    Image i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12;
    Timer t1, t2, t3, t4;
    Random rng = new Random();
    JLabel diamonds_label;
 
    BoulderDash() {

        diamonds_label = new JLabel("");
        diamonds_label.setForeground(Color.WHITE);
        diamonds_label.setFont(new Font("Sans Serif", Font.PLAIN, 25));
        this.add(diamonds_label); 
        this.setPreferredSize(new Dimension(500, 500));
        this.setBackground(Color.BLACK);
        this.addKeyListener(new MyKeyListener());
        this.setFocusable(true);
        this.requestFocus();
       
        topX=0; topY=0; xIdeal=0; yIdeal=0;
        currentLevel = 1; slideTicks = 0; 
        sliding = false; pushing = false; 
        
        lr = new BDLevelReader();
        try { lr.readLevels("levels.xml"); }
        catch (Exception e) { System.out.print("Error1"); }
        try { lr.setCurrentLevel(currentLevel); }
        catch (Exception e) { System.out.print("Error2"); }
        
        initLevel();
            
        t1 = new Timer(30, new FrameRate());
        t2 = new Timer(150, new GameActions());
        t3 = new Timer(15, new Move());
        t4 = new Timer(15, new Fall());
        t1.start();
        t2.start();
        t4.start();
        
        //tiles
        i1 = Toolkit.getDefaultToolkit().getImage("empty.jpg");
        i2 = Toolkit.getDefaultToolkit().getImage("dirt.jpg");  
        i3 = Toolkit.getDefaultToolkit().getImage("wall.jpg");
        i4 = Toolkit.getDefaultToolkit().getImage("rock.jpg");
        i5 = Toolkit.getDefaultToolkit().getImage("fallingrock.jpg");        
        i6 = Toolkit.getDefaultToolkit().getImage("diamond.jpg");      
        i7 = Toolkit.getDefaultToolkit().getImage("fallingdiamond.jpg");
        i8 = Toolkit.getDefaultToolkit().getImage("amoeba.jpg");     
        i9 = Toolkit.getDefaultToolkit().getImage("firefly.jpg"); 
        i10 = Toolkit.getDefaultToolkit().getImage("butterfly.jpg");
        i11 = Toolkit.getDefaultToolkit().getImage("exit.jpg");
        i12 = Toolkit.getDefaultToolkit().getImage("player.jpg");
        
    }
    
    void initLevel() {        
        for (int row=0; row<WIDTH; row++) {
            for (int col=0; col<HEIGHT; col++) {
                board[row][col] = null;
                if (lr.getTile(row, col) == BDTile.PLAYER) { 
                    board[row][col] = new Player(row, col); 
                    xpos=row; ypos=col; 
                }
                if (lr.getTile(row, col) == BDTile.EXIT) { board[row][col] = new Exit(row, col); }
                if (lr.getTile(row, col) == BDTile.WALL) { board[row][col] = new Wall(row, col); }
                if (lr.getTile(row, col) == BDTile.ROCK) { board[row][col] = new Rock(row, col); }
                if (lr.getTile(row, col) == BDTile.FALLINGROCK) { board[row][col] = new FRock(row, col); }
                if (lr.getTile(row, col) == BDTile.DIAMOND) { board[row][col] = new Diamond(row, col); }
                if (lr.getTile(row, col) == BDTile.FALLINGDIAMOND) { board[row][col] = new FDiamond(row, col); }
                if (lr.getTile(row, col) == BDTile.AMOEBA) { board[row][col] = new Amoeba(row, col); }
                if (lr.getTile(row, col) == BDTile.FIREFLY) { board[row][col] = new Firefly(row, col); }
                if (lr.getTile(row, col) == BDTile.BUTTERFLY) { board[row][col] = new Butterfly(row, col); }
                if (lr.getTile(row, col) == BDTile.DIRT) { board[row][col] = new Dirt(row, col); }
                if (lr.getTile(row, col) == BDTile.EMPTY) { board[row][col] = new Empty(row, col); }
            }
        }     
        diamondsCollected = 0;
    }
    
    public void paintComponent(Graphics g) {        
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;                  
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);    
        g2.translate(-topX, -topY);
        for (int row=0; row<WIDTH; row++) {
            for (int col=0; col<HEIGHT; col++) {                
                if (board[row][col] instanceof Dirt){            
                   g2.drawImage(i2, board[row][col].x, board[row][col].y, this);
                }
                if (board[row][col] instanceof Wall) {
                   g2.drawImage(i3, board[row][col].x, board[row][col].y, this);
                }
                if (board[row][col] instanceof Rock) {
                   g2.drawImage(i4, board[row][col].x, board[row][col].y, this);
                }
                if (board[row][col] instanceof Diamond) {
                    g2.drawImage(i6, board[row][col].x, board[row][col].y, this);
                }
                if (board[row][col] instanceof FDiamond) {
                    g2.drawImage(i7, board[row][col].x, board[row][col].y, this);
                }  
                if (board[row][col] instanceof Amoeba) {
                   g2.drawImage(i8, row*50, col*50, this);
                }
                if (board[row][col] instanceof Firefly) {
                   g2.drawImage(i9, row*50, col*50, this);
                }
                if (board[row][col] instanceof Butterfly) {
                   g2.drawImage(i10, row*50, col*50, this);
                }
                if (board[row][col] instanceof Exit) {
                   g2.drawImage(i11, board[row][col].x, board[row][col].y, this);
                }
                if (board[row][col] instanceof Player) {
                   g2.drawImage(i12, board[row][col].x, board[row][col].y, this);
                }
                if (board[row][col] instanceof Empty) {            
                    g2.drawImage(i1, board[row][col].x, board[row][col].y, this);
                }
                if (board[row][col] instanceof FRock) {
                   g2.drawImage(i5, board[row][col].x, board[row][col].y, this);
                }
            }
        }     
        g2.translate(topX, topY);
    }
    
    class MyKeyListener implements KeyListener {         
        public void keyPressed(KeyEvent key) {            
            if (!sliding) {               
                dx=0; dy=0;
                legalMove = true;     
                if (key.getKeyCode() == KeyEvent.VK_N) { 
                    try {if (currentLevel<10) {lr.setCurrentLevel(++currentLevel); initLevel();}} 
                    catch (Exception e) { System.out.print("Error 3"); }
                    return;
                }
                if (key.getKeyCode() == KeyEvent.VK_R) { lose(); return; } 
                if (key.getKeyCode() == KeyEvent.VK_UP) { dy=-1; }  
                if (key.getKeyCode() == KeyEvent.VK_DOWN) { dy=+1; }
                if (key.getKeyCode() == KeyEvent.VK_LEFT) {
                    dx=-1;
                    i12 = Toolkit.getDefaultToolkit().getImage("player.jpg");
                }
                if (key.getKeyCode() == KeyEvent.VK_RIGHT) {
                    dx=+1;
                    i12 = Toolkit.getDefaultToolkit().getImage("playerRight.jpg");
                }                                        
                if (board[xpos+dx][ypos+dy] instanceof Wall) { legalMove = false; }
                if (board[xpos+dx][ypos+dy] instanceof Amoeba) { legalMove = false; }
                if (board[xpos+dx][ypos+dy] instanceof FRock) { legalMove = false; }              
                if (board[xpos+dx][ypos+dy] instanceof Exit) { 
                    if (diamondsCollected >= lr.getDiamondsNeeded()) 
                    { 
                        try {if (currentLevel<10) {lr.setCurrentLevel(++currentLevel); initLevel();}} 
                        catch (Exception e) { System.out.print("Error"); }
                        return;
                    } 
                    legalMove = false; 
                }                
                if (board[xpos+dx][ypos+dy] instanceof Rock) {
                    if ((board[xpos+2*dx][ypos+2*dy] instanceof Dirt) ||
                        (board[xpos+2*dx][ypos+2*dy] instanceof Wall) ||
                        (board[xpos+2*dx][ypos+2*dy] instanceof Rock) ||
                        (board[xpos+2*dx][ypos+2*dy] instanceof Exit) ||
                        (board[xpos+2*dx][ypos+2*dy] instanceof Diamond)) {
                            legalMove = false; 
                    }       
                }
                
                if (legalMove) {                   
                    sliding = true;
                    int dc = diamondsCollected;                   
                    if (board[xpos+dx][ypos+dy] instanceof Dirt ||                    
                        board[xpos+dx][ypos+dy] instanceof Empty) { 
                            board[xpos+dx][ypos+dy] = board[xpos][ypos];                          
                    }
                    if (board[xpos+dx][ypos+dy] instanceof Rock &&
                        board[xpos+2*dx][ypos+2*dy] instanceof Empty) {             
                            board[xpos+2*dx][ypos+2*dy] = board[xpos+dx][ypos+dy];
                            board[xpos+dx][ypos+dy] = board[xpos][ypos];
                            pushing = true;
                    }
                    if (board[xpos+dx][ypos+dy] instanceof Diamond ||
                        board[xpos+dx][ypos+dy] instanceof FDiamond) { 
                            board[xpos+dx][ypos+dy] = board[xpos][ypos]; 
                            diamondsCollected++;
                            play_sound("diam.wav");
                    }
                    t3.start();
                    xpos+=dx; ypos+=dy;
                    if (dc == diamondsCollected) play_sound("move2.wav");
                }                    
            }
        }            
        public void keyTyped(KeyEvent key) {}
        public void keyReleased(KeyEvent key) {}        
    }
        
    class Move implements ActionListener {
        public void actionPerformed(ActionEvent ae){                    
            slideTicks++;       
            board[xpos][ypos].x += dx*5;
            board[xpos][ypos].y += dy*5;           
            if (pushing) {
                board[xpos+dx][ypos+dy].x += dx*5;
                board[xpos+dx][ypos+dy].y += dy*5;         
            }            
            if (slideTicks >= 10) {
                slideTicks=0;
                sliding = false; pushing = false;
                board[xpos-dx][ypos-dy] = new Empty(xpos-dx,ypos-dy);
                t3.stop();
            }       
        }
    }
    
    class Fall implements ActionListener {
        public void actionPerformed(ActionEvent ae){                                     
            for (int row=0; row<WIDTH; row++) {
                for (int col=0; col<HEIGHT; col++) {    
                    if (board[row][col] instanceof FRock || board[row][col] instanceof FDiamond) {
                        board[row][col].y += 5;
                    }   
                }
            }       
            repaint();
        }
    }
    
    class FrameRate implements ActionListener {
        public void actionPerformed(ActionEvent ae){  
            xIdeal = xpos*50-250; yIdeal = ypos*50-250;
            if (yIdeal < 0) yIdeal = 0; if (yIdeal > 605) yIdeal = 605;
            if (xIdeal < 0) xIdeal = 0; if (xIdeal > 1510) xIdeal = 1510;        
            topX += (xIdeal-topX)/10;
            topY += (yIdeal-topY)/10;   
            dLabel = lr.getDiamondsNeeded() - diamondsCollected;
            if (dLabel <= 0) dLabel = 0;
            diamonds_label.setText("Diamonds Needed: " + dLabel);
        }
    }
        
    class GameActions implements ActionListener {
        public void actionPerformed(ActionEvent ae) {                                     
            for (int row=0; row<WIDTH; row++) {
                for (int col=20; col>=0; col--) {                    
                    //stationary rocks
                    if (board[row][col] instanceof Rock) {                        
                        if (board[row][col+1] instanceof Empty) { 
                            board[row][col] = new FRock(row,col); 
                        }                        
                        else if ((board[row][col+1] instanceof Wall) || 
                            (board[row][col+1] instanceof Rock) ||
                            (board[row][col+1] instanceof Exit) ||
                            (board[row][col+1] instanceof Diamond)) {                                
                            if (board[row-1][col] instanceof Empty &&
                                board[row-1][col+1] instanceof Empty) {
                                    board[row-1][col] = new FRock(row-1, col); 
                                    board[row][col] = new Empty(row,col);   
                            }
                            else if (board[row+1][col] instanceof Empty &&
                                     board[row+1][col+1] instanceof Empty) {
                                         board[row+1][col] = new FRock(row+1, col); 
                                         board[row][col] = new Empty(row,col);                           
                            }                                    
                        }
                    }                      
                    //stationary diamonds
                    if (board[row][col] instanceof Diamond) {                    
                        if (board[row][col+1] instanceof Empty){ 
                            board[row][col] = new FDiamond(row, col); 
                        }                        
                        else if ((board[row][col+1] instanceof Wall) || 
                            (board[row][col+1] instanceof Rock) ||
                            (board[row][col+1] instanceof Exit) ||
                            (board[row][col+1] instanceof Diamond)) {                              
                            if (board[row-1][col] instanceof Empty &&
                                board[row-1][col+1] instanceof Empty) {
                                    board[row-1][col] = new FDiamond(row-1,col); 
                                    board[row][col] = new Empty(row,col);   
                            }                            
                            else if (board[row+1][col] instanceof Empty &&
                                    board[row+1][col+1] instanceof Empty) {
                                        board[row+1][col] = new FDiamond(row+1,col);  
                                        board[row][col] = new Empty(row,col);                           
                            }                                    
                        }
                    }           
                    //falling diamonds
                    if (board[row][col] instanceof FDiamond) {                       
                        if (board[row][col+1] instanceof Empty) { 
                            board[row][col+1] = board[row][col]; 
                            board[row][col] = new Empty(row,col); 
                        }                                                
                        else if ((board[row][col+1] instanceof Rock) ||
                            (board[row][col+1] instanceof Dirt) ||
                            (board[row][col+1] instanceof Wall) ||
                            (board[row][col+1] instanceof Exit) ||
                            (board[row][col+1] instanceof Diamond) ||
                            (board[row][col+1] instanceof Player) ||
                            (board[row][col+1] instanceof Firefly) ||
                            (board[row][col+1] instanceof Butterfly)) { 
                                board[row][col] = new Diamond(row, col); 
                        }              
                    }
                    //flies
                    int x = 0; int y = 0;  
                    if (!board[row][col].hasMoved) {
                        if (board[row][col] instanceof Firefly || board[row][col] instanceof Butterfly) {
                            x = board[row][col].dirX; y = board[row][col].dirY;
                            if (board[row+x][col+y] instanceof Empty) {
                                board[row][col].hasMoved = true;
                                board[row+x][col+y] = board[row][col];
                                board[row][col] = new Empty(row, col);      
                            }
                            else if (board[row+x][col+y] instanceof Player) {
                                lose(); return;     
                            }  
                        }           
                        if (board[row][col] instanceof Firefly) {                                             
                            if (!(board[row+x][col+y] instanceof Empty)) { 
                                board[row][col].changeDirectionCW(); 
                            }   
                        }    
                        if (board[row][col] instanceof Butterfly) {                           
                            if (!(board[row+x][col+y] instanceof Empty)) { 
                                board[row][col].changeDirectionCC(); 
                            }                     
                        }
                    }              
                    //amoeba
                    int chance = 400; 
                    if (rng.nextInt(chance) == 0) {
                        if (board[row][col] instanceof Amoeba) {
                            if (board[row+1][col] instanceof Empty ||
                                board[row+1][col] instanceof Dirt) {
                                    board[row+1][col] = board[row][col];
                                } 
                        }     
                    }
                    if (rng.nextInt(chance) == 1) {
                        if (board[row][col] instanceof Amoeba) {
                            if (board[row-1][col] instanceof Empty ||
                                board[row-1][col] instanceof Dirt) {
                                    board[row-1][col] = board[row][col];
                                }
                        }     
                    }
                    if (rng.nextInt(chance) == 2) {
                        if (board[row][col] instanceof Amoeba) {
                            if (board[row][col+1] instanceof Empty ||
                                board[row][col+1] instanceof Dirt) {
                                    board[row][col+1] = board[row][col]; 
                                }
                        }     
                    }
                    if (rng.nextInt(chance) == 3) {
                        if (board[row][col] instanceof Amoeba) {
                            if (board[row][col-1] instanceof Empty ||
                                board[row][col-1] instanceof Dirt) {
                                    board[row][col-1] = board[row][col];
                                }
                        }     
                    }                    
                    //falling rocks
                    if (board[row][col] instanceof FRock) {                        
                        if (board[row][col+1] instanceof Empty) { 
                              board[row][col+1] = board[row][col]; 
                              board[row][col] = new Empty(row,col);                             
                        }                    
                        else if (board[row][col+1] instanceof Player){ 
                                lose(); return;
                        }                        
                        else if (board[row][col+1] instanceof Firefly ||
                                board[row][col+1] instanceof Butterfly) {
                                explode(board[row][col+1], row, col+1);
                        }                                 
                        else if ((board[row][col+1] instanceof Rock) ||
                            (board[row][col+1] instanceof Dirt) ||
                            (board[row][col+1] instanceof Wall) ||
                            (board[row][col+1] instanceof Exit) ||
                            (board[row][col+1] instanceof Diamond)) { 
                                board[row][col] = new Rock(row,col); 
                        }
                    }
                }
            }
            for (int row=0; row<WIDTH; row++) {
                for (int col=20; col>=0; col--) {
                    board[row][col].hasMoved = false;
                }
            }
        }
    }
    
    void explode(BDObject fly, int row, int col){
        for (int i=row-1; i<row+2; i++) {
            for (int j=col-1; j<col+2; j++) {                
                if (fly instanceof Firefly) {
                    if (board[i][j] instanceof Player) { lose();  return; }
                    board[i][j] = new Empty(i, j);
                }               
                if (fly instanceof Butterfly) {
                    if (!(board[i][j] instanceof Player)) {
                        board[i][j] = new Diamond(i, j);
                    }
                    else diamondsCollected++;
                }
            }
        }
    }
    
    void play_sound(String filename) {
        try {
            File yourFile = new File(filename);
            AudioInputStream stream;
            AudioFormat format;
            DataLine.Info info;
            Clip clip;
            stream = AudioSystem.getAudioInputStream(yourFile);
            format = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);
            clip.start();        
        }
        catch (Exception e) { }
    }
    
    void lose() {
         try { play_sound("lose2.wav"); t3.stop(); pushing=false; sliding=false;  
             lr.setCurrentLevel(currentLevel); initLevel();} 
         catch (Exception e) { System.out.print("Lose Error"); }    
    }

    static void main(String[] args){
          JFrame f = new JFrame("BoulderDash!");
          f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
          f.setLayout(new FlowLayout());
          f.add(new BoulderDash());
          f.pack();
          f.setVisible(true);
    }
}
