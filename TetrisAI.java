import java.awt.*;
import java.util.TreeMap;

class TetrisAI implements Runnable {
    Tetris tetris;
    boolean next;

    int wHole=10000;        //Weight for the holes left below, Increase it to make this feature more important.

    Color[][] board;
    int px, py, pr, piece;

    public TetrisAI(Tetris t) {
        tetris=t;
        board=tetris.getBoard();
    }
    public void start() {
        new Thread(this).start();
    }




    public synchronized void next() {
        next=true;
        notifyAll();
    }

    public synchronized void waitForNext() {
        try {
            while(!next) wait();
        } catch(InterruptedException e) {
            e.printStackTrace(System.err);
        }
        next=false;
    }
    public boolean get(int x, int y) {
        if(x<px || x>=px+4 || y<py || y>=py+4)

            return board[y][x]!=getColor(0);

        if(Tetris.pieces[piece][pr][(y-py)*4+(x-px)]!=0) return true;
        return board[y][x]!=getColor(0);
    }

    public void run() {
        while(true) {
            int maxscore=Integer.MIN_VALUE;
            int maxpx=0, maxpr=0;

            waitForNext();
            px=pr=288;
            piece=tetris.getPiece();

            for(px=-4; px<board[0].length+4; px++) {
                for(pr=0; pr<Tetris.pieces[piece].length; pr++) {
                    if(tetris.spaceFor(px, 0, Tetris.pieces[piece][pr])!= true) {
                        continue;
                    }


                    py=0;
                    while(!tetris.parkPiece(px,py,Tetris.pieces[piece][pr])) {
                        py++;
                    }
                    int n=evaluate();
                    if(n>maxscore) {
                        maxscore=n;
                        maxpx=px;
                        maxpr=pr;
                    }
                }
            }

            for(int i=0; i<4 && tetris.getRotation()!=maxpr; i++)
                tetris.rotate(true);

            System.out.println("Max Value: "+maxscore);

            int displace=maxpx-tetris.getPieceX();
            System.out.println("Move: "+displace);

            while(displace>0) {
                if(!tetris.right()) System.out.println("Cannot get to"+maxpx+" Stucked in "+tetris.getPieceX());
                displace--;
            }
            while(displace<0) {
                if(!tetris.left()) System.out.println("Cannot get to"+maxpx+" Stucked in "+tetris.getPieceX());
                displace++;
            }
            if(tetris.getPieceX()!=maxpx) {
                System.out.println("Game Over");
                System.out.println("Score was "+tetris.getScore() );
            } else {
                tetris.drop();
            }
        }
    }
    int evaluate() {
        int r=evaluate2();
        System.out.println("Value: "+r);
        return r;
    }


    int evaluate2() {
        int sum=0;
        sum+=evaluateHigh();
        sum-=countCoveredHoles()*wHole;
        return sum;
    }

    int countCoveredHoles() {
        int holes=0;
        for(int x=0; x<board[0].length; x++) {
            boolean flag=false;
            for(int y=0; y<board.length; y++) {


                if(get(x,y))
                    flag=true;
                else {
                    if(flag) holes++;
                    flag=false;
                }

            }
        }
        return holes;
    }

    int evaluateHigh() {
        int value=0;
        for(int y=0; y<board.length; y++) {
            for(int x=0; x<board[0].length; x++) {
                if(get(x,y)) {
                    value += y * y * y;   //Formula used to calculate the weight for the heigth
                }
            }
        }
        return value;
    }


    public Color getColor(int p) {
        switch (p) {
            default:
            case 0:
                return Color.black;
            case 1:
                return Color.yellow;
            case 2:
                return Color.blue;
            case 3:
                return Color.red;
            case 4:
                return Color.cyan;
            case 5:
                return Color.green;
            case 6:
                return Color.magenta;
            case 7:
                return Color.white;
            case 8:
                return Color.GRAY;
        }
    }
}

