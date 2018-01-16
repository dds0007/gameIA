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
		
		for (int i = 0; i < 1; i++) {
			
			new Thread() {
				@Override
				public void run() {
					//boolean rotado = false;
					JFrame f = new JFrame("Tetris");
					f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					f.setSize(12 * 26 + 10, 26 * 23 + 25);
					f.setVisible(true);
					final Tetris game = new Tetris();
					game.init();
					f.add(game);
					int times = 500;// number of down scrolls
					int dlay = 5000; //delay milliseconds
					/*
					try {
				        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("I:/Descargas/tetris.wav").getAbsoluteFile());
				        Clip clip = AudioSystem.getClip();
				        clip.open(audioInputStream);
				        clip.start();
				    } catch(Exception ex) {
				        System.out.println("Error with playing sound.");
				        ex.printStackTrace();
				    }
				    */
					while (times > 0) {
						//play(game,false);
						try {
							Thread.sleep(dlay);
							//System.out.println(game.getCurretnPiecePosition().y);
							if(game.getCurretnPiecePosition().y==2){
								//System.out.println(game.getCurretnPiecePosition().y);	
								//rotado=false;
							}
							// Insert here calls to IA methods
							play(game);
							// rotatePiece(game);
							// end
							while(game.dropDown()){
							//game.dropDown();
							Thread.sleep(dlay/50);
							}
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
	/*protected static void rotatePiece(Tetris game) {
		// Random IA
		// Min + (int)(Math.random() * ((Max - Min) + 1))
		int numRots = ((int) (Math.random() * 4));
		// System.out.println(numRots);
		while (numRots > 0) {
			game.rotate(-1);
			numRots--;
		}

	}*/

	/**
	 * Decides to move or not.
	 * 
	 * @param game
	 */
	protected static void play(Tetris game) {
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
		int pos = game.getCurretnPiecePosition().x;

		checkRotation(freePos, well, game);
		while (pos - freePos.get(0) != 0) {
			if (pos - freePos.get(0) < 0) {
				game.move(1);
				pos++;
			} else {
				game.move(-1);
				pos--;
			}
		}
		
		if(game.getNextPiece()==0||game.getNextPiece()==4||game.getNextPiece()==6){
			game.move(-1);
		}
		
	}

	private static void checkRotation(List<Integer> freePos, Color[][] well, Tetris game) {
		List<ArrayList<Integer>> shape = checkSpots(freePos,well); // right and left blank spots
		//System.out.println(freePos.get(0)+"X  "+freePos.get(1)+"Y");
		
		switch(game.getNextPiece()){
		case 0:
			//I
			if(shape.get(0).get(1)>=4){//hueco izq
			//	game.move(4);
			}else{
				if(shape.get(0).get(0)<4){//hueco der
					game.rotate(-1);
					//game.move(-1);
				}
			}
			break;
		case 1:
			//L azul
		//	System.out.println("asul");
			if(shape.get(0).get(0)>=3){//3hueco der
				game.rotate(0);
			}else{
				if(shape.get(0).get(0)==2){
					game.rotate(1);
					//game.move(-1);
				}else{
					if(shape.get(1).get(0)==2){
						game.rotate(2);
					//	game.move(-1);
					}else{
						if(shape.get(2).get(1)>=2){
							game.rotate(-1);
						//	game.move(1);
						}
					}
				}
			}
			break;
			
		case 2:
			//J
			if(shape.get(0).get(0)>=3){//3hueco der
				game.rotate(2);
			}else{
				if(shape.get(0).get(0)>1){
					game.rotate(1);
					//game.move(1);
				}else{
					if(shape.get(1).get(0)>=2){
						game.rotate(-1);
					//	game.move(-1);
					}
				}
			}
			
			break;
		case 3:
			//O
			//nothing to rotate here
			break;
		case 4:
			//S verde
			if(shape.get(0).get(0)<2){
				if(shape.get(1).get(1)>=2){
					game.rotate(-1);
				//	game.move(-1);
				}
			}
			break;
			
		case 5:
			//T
			if(shape.get(0).get(0)<3){
				if(shape.get(1).get(1)>1){
					game.rotate(1);
					game.move(-1);
				}else{
					if(shape.get(1).get(0)>1){
						game.rotate(-1);
						//game.move(-1);
					}
				}
			}
		break;
		case 6:
			//Z red
			if(shape.get(0).get(0)<2){
				if(shape.get(1).get(0)>=2){
					game.rotate(1);
				}
			}else{
			
					//game.move(-1);
				}
			
		break;
		
		}	
	}
	
	/**
	 * checks if there is a free way.
	 * 
	 * @param well
	 * @param x
	 * @param y
	 * @return
	 */
	private static boolean libreUp(Color[][] well,int x, int y){
		while(y>0){
			y--;
			if(well[x][y] != Color.black){
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * gets a matrix with the left andd right blank spots.
	 * 
	 * @param freePos
	 * @param well
	 * @param game
	 * @return matrix
	 */
	private static List<ArrayList<Integer>> checkSpots(List<Integer> freePos, Color[][] well){
		int x,conta;
		List<ArrayList<Integer>> shape = new ArrayList<ArrayList<Integer>>(); // right and left blank spots
		for(int i=0;i<5;i++){
			x = freePos.get(0);
			conta=0;
			shape.add(new ArrayList<Integer>());
			while (well[x][freePos.get(1)-i] == Color.BLACK&&libreUp(well,x,freePos.get(1)-i)) {
				conta++;
				x++;
			}
			x = freePos.get(0);
			shape.add(new ArrayList<Integer>());
			shape.get(i).add(conta);
			conta=0;
			while (well[x][freePos.get(1)-i] == Color.BLACK) {
				conta++;
				x--;
			}
			shape.get(i).add(conta);
		}
		return shape;
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