package game;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IA {
	/**
	 * Pseudo decision tree.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		for (int i = 0; i < 10; i++) {
			
			new Thread() {
				@Override
				public void run() {
					boolean rotado = false;
					JFrame f = new JFrame("Tetris");
					f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					f.setSize(12 * 26 + 10, 26 * 23 + 25);
					f.setVisible(true);
					final Tetris game = new Tetris();
					game.init();
					f.add(game);
					int times = 500;// number of down scrolls
					try {
				        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("I:/Descargas/tetris.wav").getAbsoluteFile());
				        Clip clip = AudioSystem.getClip();
				        clip.open(audioInputStream);
				        clip.start();
				    } catch(Exception ex) {
				        System.out.println("Error with playing sound.");
				        ex.printStackTrace();
				    }
					while (times > 0) {
						//play(game,false);
						try {
							Thread.sleep(50);
							//System.out.println(game.getCurretnPiecePosition().y);
							if(game.getCurretnPiecePosition().y==2){
								//System.out.println(game.getCurretnPiecePosition().y);	
								rotado=false;
							}
							// Insert here calls to IA methods
							rotado=play(game,rotado);
							// rotatePiece(game);
							// end
							
							game.dropDown();
						} catch (InterruptedException e) {
							System.out.println("hpñ");
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
		// System.out.println(numRots);
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
	protected static boolean play(Tetris game,boolean rotado) {
		// Fill The Gap
		
		Color[][] well = game.getWell();
		List<Integer> freePos = new ArrayList<Integer>();
		for (int y = 21; y > 0 && freePos.isEmpty(); y--) {
			for (int x = 1; x < 12 && freePos.isEmpty(); x++) {
				// System.out.println("Pos :" + x+ ' ' + y +"Color:" +
				// well[x][y]);
				if (well[x][y] == Color.BLACK && checkTop(well, x, y)) {
					freePos.add(x);
					freePos.add(y);
				}
			}
		}
		// System.out.println("FreePos:"+freePos.get(0));

		int pos = game.getCurretnPiecePosition().x;

		while (pos - freePos.get(0) != 0) {
			if (pos - freePos.get(0) < 0) {
				game.move(1);
				pos++;
			} else {
				game.move(-1);
				pos--;
			}
		}
		return checkRotation(freePos, well, game,rotado);
	}

	private static boolean checkRotation(List<Integer> freePos, Color[][] well, Tetris game, Boolean rotado) {
		
		
		//public int rotalo(int x, int y) {
			int[] vacio = new int[2];
			
			int prof = 0;
			int x1 = freePos.get(0);
			int x2 = freePos.get(0);;
			int y1 = freePos.get(1);
			int y2 = freePos.get(1);
			int conta = 0;
			while (well[x1][freePos.get(1)] == Color.BLACK) {
				conta++;
				x1++;
			}
			while (well[x1][freePos.get(1)] == Color.BLACK) {
				conta++;
				x1--;
			}
			if (rotado == false) {



				//System.out.print(currentPiece);
				//System.out.println(rotado);
				switch (game.getNextPiece()) {

					//I
					case 0:
						if (conta < 4) {
							game.rotate(1);
							rotado = true;
						} else {
							game.rotate(0);
							rotado = true;
						}
						break;
					//L
					case 1:
						if (conta == 1) {
							game.rotate(2);
							rotado = true;
						}
						if (conta == 2) {
							game.rotate(1);
							rotado = true;
						}
						if (conta >= 3) {
							rotado = true;
						}
						break;
					//J
					case 2:
						if (conta == 1) {
							game.rotate(0);
							rotado = true;
						}
						if (conta == 2) {
							game.rotate(1);
							rotado = true;
						}
						if (conta >= 3) {
							game.rotate(2);
							rotado = true;
						}
						break;
					//O
					case 3:
						rotado = true;
						break;
					//S
					case 4:
						if (conta == 1) {
							game.	rotate(1);
							rotado = true;
						} else {
							rotado = true;
						}
						break;
					case 5:
						if (conta == 1) {
							game.rotate(1);
							rotado = true;
						} else {
							rotado = true;
						}
				}

			}
			//return conta;
		
			
		
		/*// bloqueada 3 alturas
		if (well[freePos.get(0) - 1][freePos.get(1)] != Color.BLACK
				&& well[freePos.get(0) + 1][freePos.get(1)] != Color.BLACK
				&& well[freePos.get(0) - 1][freePos.get(1) - 1] != Color.BLACK
				&& well[freePos.get(0) + 1][freePos.get(1) - 1] != Color.BLACK
				&& well[freePos.get(0) - 1][freePos.get(1) - 2] != Color.BLACK
				&& well[freePos.get(0) + 1][freePos.get(1) - 2] != Color.BLACK) {
			System.out.println(game.getNextPiece());
			if (game.getNextPiece() == 0) {
				game.rotate(-1);
			}
		} else {
			// bloqueada 2 alturas
			if (well[freePos.get(0) - 1][freePos.get(1)] != Color.BLACK
					&& well[freePos.get(0) + 1][freePos.get(1)] != Color.BLACK
					&& well[freePos.get(0) - 1][freePos.get(1) - 1] != Color.BLACK
					&& well[freePos.get(0) + 1][freePos.get(1) - 1] != Color.BLACK) {
				if (game.getNextPiece() == 0) {
					game.rotate(-1);
				}
			} else {
				// bloqueada 1 altura
				if (well[freePos.get(0) - 1][freePos.get(1)] != Color.BLACK
						&& well[freePos.get(0) + 1][freePos.get(1)] != Color.BLACK) {
					//System.out.println(game.getNextPiece());
					if (game.getNextPiece() == 0) {
						game.rotate(-1);
					}
				}
			}
		}
		*/
			corregir(conta,game);
			return rotado;
	}
public static void corregir(int conta,Tetris game){
	if (game.getNextPiece()==0 && conta<4){
		game.move(-1);
	}
	if (game.getNextPiece()==1 && conta==2){
		game.move(-1);
	}
	if (game.getNextPiece()==4 && conta==1){
		game.move(-1);
	}
	if (game.getNextPiece()==5 && conta==1){
		game.move(-1);
	}

}
	private static boolean checkTop(Color[][] well, int x, int y) {
		boolean retorno = true;
		while (y > 1 && retorno) {
			if (well[x][y] != Color.BLACK) {
				retorno = false;
			}
			y--;
		}
		return retorno;
	}
}