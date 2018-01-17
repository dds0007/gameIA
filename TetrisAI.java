import java.awt.*;
import java.util.TreeMap;

class TetrisAI implements Runnable {
    Tetris tetris;
    boolean next;

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
                    //Mayor cuanto mas abajo el color
                    //Menor cuantos mas huecos
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

            System.out.println("Valor maximo: "+maxscore);

            int displace=maxpx-tetris.getPieceX();
            System.out.println("Displace: "+displace);

            while(displace>0) {
                if(!tetris.right()) System.out.println("No llego a"+maxpx+" Estoy en "+tetris.getPieceX());
                displace--;
            }
            while(displace<0) {
                if(!tetris.left()) System.out.println("No llego a"+maxpx+" Estoy en "+tetris.getPieceX());
                displace++;
            }
            if(tetris.getPieceX()!=maxpx) {
                System.out.println("Mery tenia una ovejita");
            } else {
                tetris.drop();
            }
        }
    }
    int evaluate() {
        int r=evaluate2();
        System.out.println("Evaluating: "+r);
        return r;
    }
    int evaluate2() {
        int sum=0;
        /*
        switch(countRows()) {
            case 1: sum+=1; break;
            case 2: sum+=500; break;
            case 3: sum+=5000; break;
            case 4: sum+=100000; break;
        }
        */
        sum+=highCosa();
        sum-=countCoveredHoles()*10000;
        return sum;
    }

    int countRows() {
        int r=0;

        for(int y=0; y<board.length; y++) {
            boolean ok=true;
            for(int x=0; x<board[y].length && ok; x++)
                if(!get(x,y)) ok=false;
            if(ok) r++;
        }
        return r;
    }

    int countCoveredHoles() {
        int holes=0;
        for(int x=0; x<board[0].length; x++) {
            //Evitar contar una linea full negros
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
        //System.out.println("Holes: "+holes);
        return holes;
    }

    int countHeight() {
        for(int y=0; y<board.length; y++) {
            for(int x=0; x<board[0].length; x++) {
                if(get(x,y)) return board.length-y;
            }
        }
        return 0;
    }

    int highCosa() {
        int value=0;
        for(int y=0; y<board.length; y++) {
            for(int x=0; x<board[0].length; x++) {
                if(get(x,y)) {
                    value += y * y * y;
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
            //Uri
            case 8:
                return Color.GRAY;
        }
    }
}

