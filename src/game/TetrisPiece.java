package game;

import java.awt.*;

public class TetrisPiece {
	int rotation;
	int kind;
	int blockSize = 20;
	Point position = new Point(0, 0); // upper left

	static final int PIECE_SIZE = 5;
	static final int BLOCKS_PIECE = 4;

	static final Color[] COLORS = { new Color(255, 255, 0), // yellow
			new Color(0, 180, 0), // green
			new Color(255, 0, 0), // red
			new Color(255, 127, 0), // orange
			new Color(255, 0, 255), // purple
			new Color(0, 0, 255), // blue
			new Color(0, 255, 255) // cyan
	};

	static final int[][][] TRANSLATIONS = {
			/* T */
			{ { -2, -3 }, { -2, -3 }, { -2, -3 }, { -2, -2 } },
			/* I */
			{ { -2, -2 }, { -2, -4 }, { -2, -2 }, { -2, -3 } },
			/* S */
			{ { -2, -3 }, { -2, -3 }, { -2, -3 }, { -2, -2 } },
			/* Z */
			{ { -2, -3 }, { -2, -3 }, { -2, -3 }, { -2, -2 } },
			/* O */
			{ { -2, -3 }, { -2, -3 }, { -2, -3 }, { -2, -3 } },

			/* J */
			{ { -2, -3 }, { -2, -2 }, { -2, -3 }, { -2, -3 } },
			/* L */
			{ { -2, -3 }, { -2, -3 }, { -2, -3 }, { -2, -2 } }

	};

	static final int[][][][] PIECES = {
			// TPiece
			{ { { 0, 0, 0, 0, 0 }, { 0, 0, 1, 0, 0 }, { 0, 0, 1, 1, 0 }, { 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 0 } },
					{ { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 }, { 0, 1, 1, 1, 0 }, { 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 0 } },
					{ { 0, 0, 0, 0, 0 }, { 0, 0, 1, 0, 0 }, { 0, 1, 1, 0, 0 }, { 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 0 } },
					{ { 0, 0, 0, 0, 0 }, { 0, 0, 1, 0, 0 }, { 0, 1, 1, 1, 0 }, { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 } }

			},

			// IPiece
			{ { { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 }, { 0, 1, 1, 1, 1 }, { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 } },
					{ { 0, 0, 0, 0, 0 }, { 0, 0, 1, 0, 0 }, { 0, 0, 1, 0, 0 }, { 0, 0, 1, 0, 0 }, { 0, 0, 1, 0, 0 } },
					{ { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 }, { 1, 1, 1, 1, 0 }, { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 } },
					{ { 0, 0, 1, 0, 0 }, { 0, 0, 1, 0, 0 }, { 0, 0, 1, 0, 0 }, { 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 0 } } },

			// SPiece
			{ { { 0, 0, 0, 0, 0 }, { 0, 0, 1, 0, 0 }, { 0, 0, 1, 1, 0 }, { 0, 0, 0, 1, 0 }, { 0, 0, 0, 0, 0 } },
					{ { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 }, { 0, 0, 1, 1, 0 }, { 0, 1, 1, 0, 0 }, { 0, 0, 0, 0, 0 } },
					{ { 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0 }, { 0, 1, 1, 0, 0 }, { 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 0 } },
					{ { 0, 0, 0, 0, 0 }, { 0, 0, 1, 1, 0 }, { 0, 1, 1, 0, 0 }, { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 } } },

			// ZPiece
			{ { { 0, 0, 0, 0, 0 }, { 0, 0, 0, 1, 0 }, { 0, 0, 1, 1, 0 }, { 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 0 } },
					{ { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 }, { 0, 1, 1, 0, 0 }, { 0, 0, 1, 1, 0 }, { 0, 0, 0, 0, 0 } },
					{ { 0, 0, 0, 0, 0 }, { 0, 0, 1, 0, 0 }, { 0, 1, 1, 0, 0 }, { 0, 1, 0, 0, 0 }, { 0, 0, 0, 0, 0 } },

					{ { 0, 0, 0, 0, 0 }, { 0, 1, 1, 0, 0 }, { 0, 0, 1, 1, 0 }, { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 } } },

			// OPiece
			{ { { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 }, { 0, 0, 1, 1, 0 }, { 0, 0, 1, 1, 0 }, { 0, 0, 0, 0, 0 } },
					{ { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 }, { 0, 0, 1, 1, 0 }, { 0, 0, 1, 1, 0 }, { 0, 0, 0, 0, 0 } },
					{ { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 }, { 0, 0, 1, 1, 0 }, { 0, 0, 1, 1, 0 }, { 0, 0, 0, 0, 0 } },
					{ { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 }, { 0, 0, 1, 1, 0 }, { 0, 0, 1, 1, 0 }, { 0, 0, 0, 0, 0 } } },

			// JPiece
			{ { { 0, 0, 0, 0, 0 }, { 0, 0, 1, 0, 0 }, { 0, 0, 1, 0, 0 }, { 0, 1, 1, 0, 0 }, { 0, 0, 0, 0, 0 } },
					{ { 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0 }, { 0, 1, 1, 1, 0 }, { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 } },
					{ { 0, 0, 0, 0, 0 }, { 0, 0, 1, 1, 0 }, { 0, 0, 1, 0, 0 }, { 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 0 } },
					{ { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 }, { 0, 1, 1, 1, 0 }, { 0, 0, 0, 1, 0 }, { 0, 0, 0, 0, 0 } } },

			// LPiece
			{ { { 0, 0, 0, 0, 0 }, { 0, 0, 1, 0, 0 }, { 0, 0, 1, 0, 0 }, { 0, 0, 1, 1, 0 }, { 0, 0, 0, 0, 0 } },
					{ { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 }, { 0, 1, 1, 1, 0 }, { 0, 1, 0, 0, 0 }, { 0, 0, 0, 0, 0 } },
					{ { 0, 0, 0, 0, 0 }, { 0, 1, 1, 0, 0 }, { 0, 0, 1, 0, 0 }, { 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 0 } },
					{ { 0, 0, 0, 0, 0 }, { 0, 0, 0, 1, 0 }, { 0, 1, 1, 1, 0 }, { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 } } }

	};

	public void draw(Graphics g) {
		g.setColor(COLORS[this.kind]);

		for (int i = 0; i < PIECE_SIZE; i++) {
			for (int j = 0; j < PIECE_SIZE; j++) {
				if (PIECES[kind][rotation][i][j] == 1) {
					g.fillRect(position.x + j * blockSize, position.y + i * blockSize, blockSize, blockSize);
				}
			}
		}
	}

	public void moveLeft() {
		position.x -= blockSize;
	}

	public void moveRight() {
		position.x += blockSize;
	}

	public void moveDown() {
		position.y += blockSize;
	}

	public void rotateCW() {
		rotation++;
		if (rotation > 3)
			rotation = 0;
	}

	public void rotateCCW() {
		rotation--;
		if (rotation < 0)
			rotation = 3;
	}

	public Point[] boardIndexes(Point pos, int rot) {
		Point[] indexes = new Point[BLOCKS_PIECE];

		int n = 0;
		for (int i = 0; i < PIECE_SIZE; i++) {
			for (int j = 0; j < PIECE_SIZE; j++) {
				if (PIECES[kind][rot][i][j] == 1) {
					int col = j + pos.x / blockSize;
					int row = i + pos.y / blockSize;
					indexes[n] = new Point(col, row);
					n++;
				}
			}
		}

		return indexes;
	}

	public TetrisPiece clonePiece() {
		TetrisPiece p = new TetrisPiece();
		p.kind = this.kind;
		p.rotation = this.rotation;
		p.position = new Point(this.position.x, this.position.y);
		p.blockSize = this.blockSize;

		return p;

	}
}

class Point {
	int x;
	int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point() {
	}
}