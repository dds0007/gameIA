package game;

import java.awt.*;

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
					while (!game.isLost()) {
						try {
							Thread.sleep(1);
							algorithmAI(game);
							while(game.dropDown()){
								sleep(3);
							}
							Thread.sleep(5);
						} catch (InterruptedException e) {
						}					}
					System.out.println("Score :" + game.getScore());
					System.out.println("Lines completed :" + game.getLines());
				}
			}.start();
		}
	}
	
	/**
	 * @author BSC
	 * 
	 * Decision Making Algorithm, evaluating all the possible positions after dropping down the current piece.
	 * 
	 */
	protected static void algorithmAI(Tetris game){
		//int [] rotations = {2,4,4,1,2,4,2};
		int [][] heights = getHeights(game);
		int [][] edges = countEdges(game, heights);
		int r=-1, x=0;
		double points =-20;
		for (int i=0; i<heights.length; i++){
			for (int j=0; j<heights[i].length; j++){
				if(heights[i][j]!=0){
					if(edges[i][j]-heights[i][j] > points){
						points = 1.15*edges[i][j]-heights[i][j];
						r = i;
						x = j-5;
					}
				}
			}
		}
		if(r>-1){
			game.rotate(r);
			game.move(x);
		}
	}
	
	private static int[][] countEdges(Tetris game, int[][] heights){
		int [][] edges = {{0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0}};
		Color[][] well = game.getWell();
		for (int i=0; i<edges.length; i++){
			Point[] tetrisPiece = game.getTetrisPiece(i);
			int counter=0;
			for (Point p : tetrisPiece){
				if(p.y!=0){
					counter++;
				}
			}
			for (int j=0; j<edges[i].length; j++){
				if(heights[i][j]!=0){
					int y=22-heights[i][j];
					if (counter==4){
						y--;
					}
					for (Point p : tetrisPiece) {
						if (well[p.x+j-1][p.y+y]!=Color.BLACK){
							edges[i][j]++;
						}
						if (well[p.x+j+1][p.y+y]!=Color.BLACK){
							edges[i][j]++;
						}
						if (well[p.x+j][p.y+y-1]!=Color.BLACK){
							edges[i][j]++;
						}
						if (well[p.x+j][p.y+y+1]!=Color.BLACK){
							edges[i][j]++;
						}
					}
				}
			}
		}
		return edges;
	}
	
	
	private static int[][] getHeights(Tetris game){
		int [][] heights = {{0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0}};
		int [] rotations = {2,4,4,1,2,4,2};
		for(int r=0; r<rotations[game.getCurrentPiece()]; r++){
			Point[] tetrisPiece = game.getTetrisPiece(r);
			int counter = 0;
			for (Point p : tetrisPiece){
				if(p.y!=0){
					counter++;
				}
			}
			int x = 5;
			//Trying moving to the left
			while (!game.collidesAt(x, 2, r)){
				int y = 2;
				while (!game.collidesAt(x, y, r)){
					y++;
				}				
				if (counter==4){
					heights[r][x] = 22-y;
				} else {
					heights[r][x] = 23-y;
				}
				x--;
			}
			if (x!=5){
				//Trying moving to the right
				x=6;
				while (!game.collidesAt(x, 2, r)){
					int y = 2;
					while (!game.collidesAt(x, y, r)){
						y++;
					}
					if (counter==4){
						heights[r][x] = 22-y;
					} else {
						heights[r][x] = 23-y;
					}
					x++;
				}
			}
		}
		return heights;
	}

}