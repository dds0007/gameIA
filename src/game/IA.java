package game;

import javax.swing.JFrame;

public class IA {
	/**
	 * Random intelligent agent.
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
					int times = 2000;
					while (times > 0) {
						try {
							Thread.sleep(1);
							// Insert here calls to IA methods
							play(game);
							rotatePiece(game);
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
		// Random IA
		if (Math.random() > 0.5) {
			game.move(1);
			//System.out.println("RIGHT");
		} else {
			if(Math.random() > 0.5)
			game.move(-1);
			//System.out.println("LEFT");
		}

	}
}
