import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;



public class Tetris extends JPanel implements ComponentListener, KeyListener {
    private Color [][] board;
    int delay;
    int moveDelay=300;
    boolean drawboard=true;
    Random rand=new Random();
    java.util.Timer timer;
    TetrisAI ai;
    int score;

    int piece, px, py, pr;
    int opx, opy;

   ArrayList<Integer>nextPieces=new ArrayList<Integer>();

    public Tetris(int w, int h, int delay) {
        board=new Color[h][w];
        int score;
        this.delay=delay;
        setDoubleBuffered(true);
        ai=new TetrisAI(this);
        reset();
    }

    public Tetris(int w, int h) {
        this(w,h,500);
    }
    public Tetris() {
        this(10,22);
    }

    public Color[][] getBoard() { return board; }
    public int getPiece() { return piece; }
    public int getRotation() { return pr; }
    public int getPieceX() { return px; }
    public int getPieceY() { return py; }

    public void start() {
        timer=new java.util.Timer(true);

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                tick();
            }
        },1000,delay);

         ai.start();
    }
    public void stop() {
        timer.cancel();
        timer=null;
    }

    public void keyTyped(KeyEvent e) { }
    public void keyReleased(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_LEFT) left();
        else if(e.getKeyCode()==KeyEvent.VK_RIGHT) right();
        else if(e.getKeyCode()==KeyEvent.VK_UP) rotate(true);
        else if(e.getKeyCode()==KeyEvent.VK_SPACE) drop();
    }


    public void nextPiece() {
        if(nextPieces.isEmpty()){
            Collections.addAll(nextPieces,0,1,2,3,4,5,6);
            Collections.shuffle(nextPieces);
        }
        piece=nextPieces.get(0);
        nextPieces.remove(0);

        pr=rand.nextInt(pieces[piece].length);
        px=board[0].length/2-2;
        py=0;
        //Reset vs stop
        if(!spaceFor(px,py,pieces[piece][pr])) stop();
        ai.next();
    }

    public void reset() {
        for(int y=0; y<board.length; y++) {
            for(int x=0; x<board[y].length; x++) {
                board[y][x]=getColor(0);
            }
        }
        opx=opy=0;
        nextPiece();
        drawboard=true;
    }

    public void tick() {
        if(parkPiece(px,py,pieces[piece][pr])) {
            fixPiece();
            nextPiece();
        } else {
            py++;
        }
        repaint();
    }

    public boolean left() {

            if(!spaceFor(px-1,py,pieces[piece][pr])) return false;
            px--;
            repaint();
        sleep(moveDelay);
        return true;
    }
    public boolean right() {
            if(!spaceFor(px+1,py,pieces[piece][pr])) return false;
            px++;
            repaint();
        sleep(moveDelay);
        return true;
    }
    public boolean drop() {
            while(!parkPiece(px,py,pieces[piece][pr]))
                py++;
            repaint();
        sleep(moveDelay);
        return true;
    }

    public boolean rotate(boolean ccw) {
            int nr=pr+(ccw?1:-1);
            if(nr<0) nr+=pieces[piece].length;
            else if(nr==pieces[piece].length) nr=0;

            if(spaceFor(px, py, pieces[piece][nr])) {
                pr=nr;
                repaint();
                return true;
            }
        sleep(moveDelay);
        return false;
    }

    public boolean spaceFor(int x, int y, int[] piece) {
        for(int i=0; i<piece.length; i++) {
            if(piece[i]==0) continue;

            if(y+i/4<0 || y+i/4>=board.length) return false;
            if(x+i%4<0 || x+i%4>=board[0].length) return false;
            if(board[y+i/4][x+i%4]!=getColor(0)) return false;
        }
        return true;
    }

    /* Should piece be parked here? */
    public boolean parkPiece(int px, int py, int[] piece) {
        for(int xp=0; xp<4; xp++) {
            int m=-1;
            for(int yp=4-1; yp>=0; yp--) {
                if(piece[yp*4+xp]!=0) {
                    m=yp;
                    break;
                }
            }
            if(m>=0) {
                if(py+m+1==board.length || board[py+m+1][px+xp]!=getColor(0)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void fixPiece() {
        for(int i=0; i<16; i++) {
            if(pieces[piece][pr][i]!=0) {
                board[py+i/4][px+i%4]=getColor(pieces[piece][pr][i]);
            }
        }
        clearLines(py,py+4);
    }

    public void clearLines(int f, int t) {
        if(t>board.length) t=board.length;
        int numOfClears=0;
        for(int i=f; i<t; i++) {
            if (lineFull(i)) {
                removeLine(i);
                numOfClears++;
            }
            switch (numOfClears) {
                case 1:
                    score += 100;
                    break;
                case 2:
                    score += 300;
                    break;
                case 3:
                    score += 500;
                    break;
                case 4:
                    score += 800;
                    break;
            }
        }
    }

    private void removeLine(int n) {
        for(int i=0; i<board[n].length; i++)
            board[n][i]=getColor(0);

        Color[] cow=board[n];
        for(int i=n-1; i>=0; i--)
            board[i+1]=board[i];
        board[0]=cow;
        drawboard=true;
    }

    private boolean lineFull(int l) {
        for(int i=0; i<board[l].length; i++) {
            if(board[l][i]==getColor(0)) return false;
        }
        return true;
    }

    public Dimension getPreferredSize() {
        return new Dimension(board[0].length*20, board.length*20);
    }

    protected synchronized void paintComponent(Graphics g) {
        drawboard=true; //I'm too lazy to figure this out
        int yd=getHeight()/board.length;
        int xd=getWidth()/board[0].length;

        if(drawboard) {
            paintArea(g,0,0,board[0].length,board.length);
            drawboard=false;
        }


        paintArea(g,opx,opy,4,4);
        opx=px; opy=py;

        for(int i=0; i<16; i++) {
            if(pieces[piece][pr][i]==0) continue;
            int x=i%4;
            int y=i/4;
            g.setColor(getColor(pieces[piece][pr][i]));
            g.fillRect((opx+x)*xd, (opy+y)*yd, xd,yd);
            g.setColor(Color.white);
            g.drawString("" + score, 19 * 12, 25);
        }

    }


    protected void paintArea(Graphics g, int xs, int ys, int w, int h) {

        int yd=getHeight()/board.length;
        int xd=getWidth()/board[0].length;


        if(ys<0) { h+=ys; ys=0; }
        if(xs<0) { w+=xs; xs=0; }
        if(ys>=board.length) return;
        if(ys+h>=board.length) h=board.length-ys;
        if(xs>=board[0].length) return;
        if(xs+w>=board[0].length) w=board[0].length-xs;

        for(int y=ys; y<ys+h; y++) {
            for(int x=xs; x<xs+w; x++) {
                g.setColor(board[y][x]);
                g.fillRect(x*xd, y*yd,xd,yd);
            }
        }

    }






    public Color getColor(int p) {
        switch(p) {
            default:
            case 0: return Color.black;
            case 1: return Color.yellow;
            case 2: return Color.blue;
            case 3: return Color.red;
            case 4: return Color.cyan;
            case 5: return Color.green;
            case 6: return Color.magenta;
            case 7: return Color.orange;
            //Uri
            case 8 :return Color.GRAY;
        }
    }

    public void reshow() {
        drawboard=true;
        repaint();
    }

    public void componentResized(ComponentEvent e) {
        reshow();
    }
    public void componentMoved(ComponentEvent e) {
        reshow();
    }
    public void componentShown(ComponentEvent e) {
        reshow();
    }
    public void componentHidden(ComponentEvent e) {}

    final public static int[][][] pieces = {
            {{0,0,0,0,
                    0,1,1,0,
                    0,1,1,0,
                    0,0,0,0}},

            {{0,0,0,0,
                    2,2,2,2,
                    0,0,0,0,
                    0,0,0,0},
                    {0,0,2,0,
                            0,0,2,0,
                            0,0,2,0,
                            0,0,2,0}},

            {{0,0,0,0,
                    0,0,3,3,
                    0,3,3,0,
                    0,0,0,0},
                    { 0,0,3,0,
                            0,0,3,3,
                            0,0,0,3,
                            0,0,0,0}},

            {{0,0,0,0,
                    0,4,4,0,
                    0,0,4,4,
                    0,0,0,0},
                    { 0,0,0,4,
                            0,0,4,4,
                            0,0,4,0,
                            0,0,0,0}},

            {{0,0,0,0,
                    0,5,5,5,
                    0,5,0,0,
                    0,0,0,0},
                    { 0,0,5,0,
                            0,0,5,0,
                            0,0,5,5,
                            0,0,0,0},
                    { 0,0,0,5,
                            0,5,5,5,
                            0,0,0,0,
                            0,0,0,0},
                    { 0,5,5,0,
                            0,0,5,0,
                            0,0,5,0,
                            0,0,0,0}},

            {{0,0,0,0,
                    0,6,6,6,
                    0,0,0,6,
                    0,0,0,0},
                    { 0,0,6,6,
                            0,0,6,0,
                            0,0,6,0,
                            0,0,0,0},
                    { 0,6,0,0,
                            0,6,6,6,
                            0,0,0,0,
                            0,0,0,0},
                    { 0,0,6,0,
                            0,0,6,0,
                            0,6,6,0,
                            0,0,0,0}},

                  {{0,0,0,0,
                    0,7,7,7,
                    0,0,7,0,
                    0,0,0,0},
                    { 0,0,7,0,
                            0,0,7,7,
                            0,0,7,0,
                            0,0,0,0},
                    { 0,0,7,0,
                            0,7,7,7,
                            0,0,0,0,
                            0,0,0,0},
                    { 0,0,7,0,
                            0,7,7,0,
                            0,0,7,0,
                            0,0,0,0}}};

    public static void sleep(int ms) {
        try { Thread.sleep(ms); } catch(Exception e) {}
    }


    public static void main(String[] args) throws Exception {
        JFrame f=new JFrame("Tetris2");
        Tetris t=new Tetris();

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //f.addKeyListener(t);
        f.getRootPane().setBorder(BorderFactory.createMatteBorder(16,16,16,16,Color.GRAY));
        f.setSize(12 * 26 + 10, 26 * 23 + 25);
        f.getContentPane().add(t);
        f.pack();
        f.setVisible(true);
        t.start();

    }
}
