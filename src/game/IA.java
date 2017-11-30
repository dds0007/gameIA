package game;

import javax.swing.JFrame;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class IA {
	/**
	 * Random intelligent agent.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		for (int i = 0; i < 1; i++) {
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
					int times = 2000;
					while (times > 0) {
						try {
							Thread.sleep(100);
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
		for(int i=1 ;i<12&&freePos.isEmpty();i++){
			for(int j=21;j>0&&freePos.isEmpty();j--){
				//System.out.println("Pos :" + i+ ' ' + j +"Color:" + well[i][j]);
				if(well[i][j] == Color.BLACK){
					freePos.add(i);
				}
			}
		}
		System.out.println("FreePos:"+freePos.get(0));
			int pos=5;
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
}