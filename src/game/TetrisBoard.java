package game;

public class TetrisBoard {
	int rows;
	int cols;
	int[][] board;

	public TetrisBoard(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;

		board = new int[rows + 1][cols + 2];

		for (int i = 0; i < rows + 1; i++) {
			board[i][0] = 1;
			board[i][cols + 1] = 1;
		}
		for (int j = 0; j < cols + 2; j++)
			board[rows][j] = 1;

	}

	public void removePiece(TetrisPiece piece) {
		Point[] indexes = piece.boardIndexes(piece.position, piece.rotation);

		for (Point p : indexes) {
			int row = p.y;
			if (row < 0)
				continue;
			int col = p.x + 1;

			board[row][col] = 0;
		}
	}

	public void resetBoard() {
		for (int i = 0; i < board.length - 1; i++) {
			for (int j = 1; j < board[i].length - 1; j++) {
				board[i][j] = 0;
			}
		}
	}

	public void printBoard() {
		for (int i = 0; i < board.length - 1; i++) {
			for (int j = 1; j < board[i].length - 1; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	private boolean isColliding(Point[] indexes) {
		boolean collide = false;

		for (Point p : indexes) {
			int row = p.y;
			int col = p.x + 1;

			if (col < 1 || col > cols) {
				collide = true;
				break;
			}
			if (row < 0)
				continue;

			if (board[row][col] > 0) {
				collide = true;
				break;
			}
		}

		return collide;
	}

	public boolean canMoveLeft(TetrisPiece piece) {
		Point pos = new Point(piece.position.x - piece.blockSize, piece.position.y);
		Point[] indexes = piece.boardIndexes(pos, piece.rotation);

		return !isColliding(indexes);
	}

	public boolean canMoveRight(TetrisPiece piece) {
		Point pos = new Point(piece.position.x + piece.blockSize, piece.position.y);
		Point[] indexes = piece.boardIndexes(pos, piece.rotation);

		return !isColliding(indexes);
	}

	public boolean canMoveDown(TetrisPiece piece) {
		Point pos = new Point(piece.position.x, piece.position.y + piece.blockSize);
		Point[] indexes = piece.boardIndexes(pos, piece.rotation);

		return !isColliding(indexes);
	}

	public boolean canRotateCW(TetrisPiece piece) {
		int rot = piece.rotation;
		rot++;

		if (rot > 3)
			rot = 0;

		Point[] indexes = piece.boardIndexes(piece.position, rot);

		return !isColliding(indexes);
	}

	public boolean canRotateCCW(TetrisPiece piece) {
		int rot = piece.rotation;
		rot--;

		if (rot < 0)
			rot = 3;

		Point[] indexes = piece.boardIndexes(piece.position, rot);

		return !isColliding(indexes);
	}

	public boolean willFitNext(TetrisPiece piece) {
		Point[] indexes = piece.boardIndexes(piece.position, piece.rotation);

		return !isColliding(indexes);
	}

	public boolean pieceLandOffScreen(TetrisPiece piece) {
		Point[] indexes = piece.boardIndexes(piece.position, piece.rotation);
		boolean offscreen = false;

		for (Point p : indexes) {
			if (p.y < 0) {
				offscreen = true;
				break;
			}
		}
		return offscreen;
	}

	public void updateBoard(TetrisPiece piece) {
		Point[] indexes = piece.boardIndexes(piece.position, piece.rotation);

		for (Point p : indexes) {
			int row = p.y;
			if (row < 0)
				continue;
			int col = p.x + 1;

			board[row][col] = piece.kind + 1;
		}
	}

	public int checkCompleteRows() {
		int completeRows = 0;
		for (int y = 0; y < rows; y++) {
			boolean ok = true;
			for (int x = 1; (x < cols + 1) && ok; x++)
				if (board[y][x] == 0)
					ok = false;
			if (ok) {
				completeRows++;
				deleteRow(y);
			}
		}
		return completeRows;
	}

	public void deleteRow(int r) {
		for (int i = r; i > 0; i--) {
			for (int j = 1; j < cols + 1; j++) {
				board[i][j] = board[i - 1][j];
			}
		}
		for (int j = 1; j < cols + 1; j++) {
			board[0][j] = 0;
		}

	}

}