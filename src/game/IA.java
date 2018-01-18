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
		int tries=1;
		for (int i = 0; i < tries; i++) {
			
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
					int dlay = 100; //delay milliseconds
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
					while (!game.isLost()) {
						try {
							Thread.sleep(dlay);
							if(game.getCurretnPiecePosition().y==2){
							}
							// Insert here calls to IA methods
							play(game);

							while(game.dropDown()){
							Thread.sleep(dlay/50);
							}
						} catch (InterruptedException e) {
							System.out.println("Crash");
						}
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
		int rotation;
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

		rotation = checkRotation(freePos, well, game);
		while (pos - freePos.get(0) != 0) {
			if (pos - freePos.get(0) < 0) {
				game.move(1);
				pos++;
			} else {
				game.move(-1);
				pos--;
			}
		}
		game.rotate(rotation);
		game.move(compensateShittyGame(rotation,game.getNextPiece(),freePos));
	}
	
	/**
	 * compensates the start point of the peice not being where it should.
	 * 
	 * @param rotation
	 * @param piece
	 */
	private static int compensateShittyGame(int rotation,int piece,List<Integer> freePos){
		int move=0;
		switch(piece){
		case 0:
			//I
			if(rotation!=0&&freePos.get(0)<=7){
				move=-1;
			}
			if(rotation!=0&&freePos.get(0)>=9){
				move=1;
			}
			if(rotation!=0&&freePos.get(0)>=10){
				move=2;
			}
			break;
		case 1:
			//L azul
			if(rotation!=0&&rotation!=2&&freePos.get(0)<=8){
				move=-1;
			}
			if(rotation!=0&&rotation!=2&&freePos.get(0)>=10){
				move=1;
			}
		case 2:
			//J
			if(rotation!=0&&rotation!=2&&rotation!=1&&freePos.get(0)<=8){
				move=-1;
			}
			if(rotation!=0&&rotation!=2&&freePos.get(0)>=10){
				move=1;
			}
			
			break;
		case 3:
			//O
			
			break;
		case 4:
			//S verde
			if(rotation!=0&&freePos.get(0)<=8){
				move=-1;
			}
			if(rotation!=0&&freePos.get(0)>=9){
				move=-1;
			}
			break;
			
		case 5:
			//T
			if(rotation!=0&&freePos.get(0)<=7){
				move=-1;
			}
			if(rotation!=0&&freePos.get(0)>=9){
				move=1;
			}
			break;
		case 6:
			//Z red
			if(rotation==0&&freePos.get(0)<=7){
				move=-1;
			}
			if(rotation==0&&freePos.get(0)>=8&&freePos.get(0)!=10){
				move=-1;
			}
			
			if(rotation==0&&freePos.get(0)==7){
				move=-1;
			}
			break;
		}
		return move;
	}

	/**
	 * determines how much to rotate.
	 * 
	 * @param freePos
	 * @param well
	 * @param game
	 * @return
	 */
	private static int checkRotation(List<Integer> freePos, Color[][] well, Tetris game) {
		int rotation=0;
		List<ArrayList<Integer>> shape = checkSpots(freePos,well); // right and left blank spots
		//System.out.println(freePos.get(0)+"X  "+freePos.get(1)+"Y");
		
		switch(game.getNextPiece()){
		case 0:
			//I
			//System.out.println('I');
			if(shape.get(0).get(0)<4){// no hueco der
				rotation=-1;
			}
			break;
		case 1:
			//L azul
			//System.out.println('L');
			if(shape.get(0).get(0)==2){			
				rotation=1;
			}else{
				if(shape.get(1).get(0)<2){
					rotation=-1;
				}else{
					if(shape.get(1).get(0)==2){
						rotation=2;
					}
				}
			}
			break;
			
		case 2:
			//J
			//System.out.println('J');
			if(shape.get(0).get(0)>=3){//3hueco der
				rotation=2;
			}else{
				if(shape.get(0).get(0)==2){			
					rotation=1;
				}else{
					if(shape.get(1).get(0)<2){
						rotation=-1;
					}
				}
			}
			break;
		case 3:
			//O
			//System.out.println('O');
			//nothing to rotate here
			break;
		case 4:
			//S verde
			//System.out.println('S');
			if(shape.get(0).get(0)<2||shape.get(0).get(0)>2){
				if(shape.get(1).get(1)>=2){
					rotation=-1;
				}
			}
			break;
			
		case 5:
			//T
			//System.out.println('T');
			if(shape.get(0).get(0)<3){
				if(shape.get(1).get(1)>1){
					rotation=1;
				}else{
					if(shape.get(1).get(0)>1){
						rotation=-1;
					}
				}
			}
		break;
		case 6:
			//Z red
			//System.out.println('Z');
			if(shape.get(0).get(0)<2||shape.get(0).get(1)>1){
				if(shape.get(1).get(0)>=2){
					rotation=1;
				}
			}
		break;
		}	
		//System.out.println("X:"+freePos.get(0)+" Y:"+freePos.get(1));
		//System.out.println("rotation "+rotation);
		return rotation;
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
			try{
				while (well[x][freePos.get(1)-i] == Color.BLACK&&libreUp(well,x,freePos.get(1)-i)) {
					conta++;
					x++;
				}
			}catch (ArrayIndexOutOfBoundsException e ){
				
			}
			x = freePos.get(0);
			shape.add(new ArrayList<Integer>());
			shape.get(i).add(conta);
			conta=0;
			try{
			while (well[x][freePos.get(1)-i] == Color.BLACK) {
				conta++;
				x--;
			}
			}catch (ArrayIndexOutOfBoundsException e ){
				
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