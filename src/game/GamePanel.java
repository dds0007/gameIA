package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;
	TetrisBoard board;
	int blockSize;
	TetrisPiece fallingPiece;
	boolean gameOver;
	Random random;
	ScheduledThreadPoolExecutor animator;
	int cols;
	int rows;

	public GamePanel(int rows, int cols, int blockSize, int seed) {
		this(rows, cols, blockSize);
		random = new Random(seed);
	}

	public GamePanel(int rows, int cols, int blockSize) {
		this.blockSize = blockSize;
		this.setPreferredSize(new Dimension(cols * blockSize, rows * blockSize));
		setDoubleBuffered(true);
		random = new Random();
		board = new TetrisBoard(rows, cols);
		this.cols = cols;
		this.rows = rows;

		setFocusable(true);
		requestFocus();
		addKeyWatcher();

	}

	public void startNewGame() {
		gameOver = false;
		board.resetBoard();
		fallingPiece = randomPiece();

		long delayToStart = 100L;
		long step = 1000L / 4;

		animator = null;
		animator = new ScheduledThreadPoolExecutor(1);
		animator.scheduleAtFixedRate(this, delayToStart, step, TimeUnit.MILLISECONDS);
	}

	public void run() {
		if (gameOver) {
			animator.shutdown();
			return;
		}

		if (board.canMoveDown(fallingPiece)) {
			fallingPiece.moveDown();
		} else {
			board.updateBoard(fallingPiece);

			addNewPiece();
		}

		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		renderGame(g);
	}

	private void renderGame(Graphics g) {
		if (fallingPiece != null)
			fallingPiece.draw(g);

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				int block = board.board[i][j + 1];

				if (block != 0) {
					g.setColor(TetrisPiece.COLORS[block - 1]);
					g.fillRect(j * blockSize, i * blockSize, blockSize, blockSize);
				}
			}
		}
	}

	protected void addNewPiece() {
		if (board.pieceLandOffScreen(fallingPiece)) {
			gameOver = true;
			fallingPiece = null;
			return;
		}

		TetrisPiece next = randomPiece();
		if (board.willFitNext(next)) {
			fallingPiece = next;
		} else {
			fallingPiece = null;
			gameOver = true;
		}

	}

	protected TetrisPiece randomPiece() {
		int kind = random.nextInt(7);
		int rot = random.nextInt(4);

		TetrisPiece piece = new TetrisPiece();
		piece.blockSize = this.blockSize;
		piece.kind = kind;
		piece.rotation = rot;

		int x = blockSize * (cols / 2 + TetrisPiece.TRANSLATIONS[kind][rot][0]);
		int y = blockSize * TetrisPiece.TRANSLATIONS[kind][rot][1];

		piece.position = new Point(x, y);

		return piece;
	}

	private void addKeyWatcher() {
		addKeyListener(

				new KeyAdapter() {
					public void keyPressed(KeyEvent e) {
						if (gameOver) {
							return;
						}

						int key = e.getKeyCode();
						if (key == KeyEvent.VK_SPACE) {

						} else if (key == KeyEvent.VK_LEFT) {
							if (board.canMoveLeft(fallingPiece))
								fallingPiece.moveLeft();

						} else if (key == KeyEvent.VK_RIGHT) {
							if (board.canMoveRight(fallingPiece))
								fallingPiece.moveRight();

						} else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_NUMPAD3) {
							if (board.canRotateCW(fallingPiece))
								fallingPiece.rotateCW();
						} else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_NUMPAD1) {
							if (board.canRotateCCW(fallingPiece))
								fallingPiece.rotateCCW();
						}

						repaint();
					}
				});
	}
}