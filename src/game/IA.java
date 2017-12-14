package game;

import javax.swing.JFrame;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class IA {
	/**
	 * Pseudo decission tree.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		for (int i = 0; i < 10; i++) {
			new Thread() {
				@Override
				public void run() {
					JFrame f = new JFrame("Tetris");
					f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					f.setSize(12 * 26 + 10, 26 * 23 + 25);
					f.setVisible(true);
					final Tetris game = new Tetris();
					game.init();
					f.add(game);
					int times = 500;//number of down scrolls
					while (times > 0) {
						try {
							Thread.sleep(10);
							// Insert here calls to IA methods
							play(game);
							//rotatePiece(game);
							// end
							game.dropDown();
						} catch (InterruptedException e) {
						}
						times--;
					}
					System.out.println("Score :" + game.getScore());
				}
			}.start();
		}
	}

	/**
	 * Decides to turn the piece or not.
	 * 
	 * @param game
	 */
	protected static void rotatePiece(Tetris game) {
		// Random IA
		// Min + (int)(Math.random() * ((Max - Min) + 1))
		int numRots = ((int) (Math.random() * 4));
		//System.out.println(numRots);
		while (numRots > 0) {
			game.rotate(-1);
			numRots--;
		}

	}

	/**
	 * Decides to move or not.
	 * 
	 * @param game
	 */
	protected static void play(Tetris game) {
		// Fill The Gap
		Color[][] well = game.getWell();
		List<Integer> freePos=new ArrayList<Integer>();
		for(int y=21;y>0&&freePos.isEmpty();y--){
			for(int x=1 ;x<12&&freePos.isEmpty();x++){
				//System.out.println("Pos :" + x+ ' ' + y +"Color:" + well[x][y]);
				if(well[x][y] == Color.BLACK && checkTop(well,x,y)){
					freePos.add(x);
				}
			}
		}
		//System.out.println("FreePos:"+freePos.get(0));
		
		int pos=game.getCurretnPiecePosition().x;
			
			while(pos-freePos.get(0)!=0){
				if(pos-freePos.get(0)<0){
					game.move(1);
					pos ++;
				}else{
					game.move(-1);
					pos --;
				}
		}
	}
	private static boolean checkTop(Color[][] well ,int x ,int y){
		boolean retorno = true;
		while(y>1&&retorno){
			if(well[x][y] != Color.BLACK){
				retorno=false;
			}
			y--;
		}
		return retorno;
	}
}