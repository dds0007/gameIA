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
							Thread.sleep(0);
							algorithmAI(game);
							while(game.dropDown()){
								sleep(10);
							}
							Thread.sleep(30);
						} catch (InterruptedException e) {
						}
					}
					//System.out.println(game.getScore());
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
		int [][] heights = getHeights(game);
		int [][] edges = countEdges(game, heights);
		int [][] holes = lastHolesCovered(game, heights);
		int r=-1, x=0;
		double points =-100, y=0;
		for (int i=0; i<heights.length; i++){
			for (int j=0; j<heights[i].length; j++){
				y=heights[i][j];
				if(y!=0){
					if(game.getCurrentPiece()==0 && edges[i][j]>6){
						if(1.3*edges[i][j]-y >= points){
							points = 1.3*edges[i][j]-y;
							r = i;
							x = j-5;
						}
					}
					if(1.3*edges[i][j] - 1.5*holes[i][j] - y > points){
						points = 1.3*edges[i][j] - 1.5*holes[i][j] - y;
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
	
	/**
	 * @author BSC
	 * 
	 * Counts the holes that would be on 4 lines under the piece, for each possible drop down of it.
	 * 
	 */
	private static int[][] lastHolesCovered(Tetris game, int[][] heights){
		int[][] holes = new int [4][12];
		Color[][] well = game.getWell();
		int c = 0;
		for (int i=0; i<holes.length; i++){
			Point[] tetrisPiece = game.getTetrisPiece(i);
			int counter=0;
			for (Point p : tetrisPiece){
				if(p.y!=0){
					counter++;
				}
			}
			for (int j=0; j<holes[i].length; j++){
				if(heights[i][j]!=0){
					int[] x = {-1, -1, -1, -1};
					int[] y = {-1, -1, -1, -1};
					int height=22-heights[i][j];
					if (counter==4){
						height--;
					}
					for (Point p : tetrisPiece) {
						c = 0;
						while(c<4){
							if (x[c]==-1 || (p.x==x[c] && p.y>y[c])){
								x[c] = p.x;
								y[c] = p.y;
								c = 4;
							} else if (p.x==x[c]) {
								c = 4;
							}
							c++;
						}
					}
					c = 0;
					while(c<x.length && x[c]>=0){
						for (int k=height+y[c]+1; k<well[0].length-1 && k<height+y[c]+5; k++){
							if (well[x[c]+j][k]==Color.BLACK){
								holes[i][j]++;
							}
						}
						c++;
					}
				}
			}
		}
		return holes;
	}
	
	/**
	 * @author BSC
	 * 
	 * Counts the unit long edges of the piece that would make direct contact to walls or
	 * other pieces on the well, for each possible drop down of it.
	 * 
	 * 
	 */
	private static int[][] countEdges(Tetris game, int[][] heights){
		int [][] edges = new int [4][12];
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
	
	/**
	 * @author BSC
	 * 
	 * Calculates the final height of the piece, for each possible drop down of it.
	 * 
	 */
	private static int[][] getHeights(Tetris game){
		int [][] heights = new int [4][12];
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