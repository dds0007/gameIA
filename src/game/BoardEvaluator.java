package game;

public class BoardEvaluator {
	public static int clearedRows(int[][] board) {
		int cleared = 0;
		int columns = board[0].length;

		for (int y = 0; y < board.length - 1; y++) {
			boolean ok = true;
			for (int x = 1; (x < columns - 1) && ok; x++)
				if (board[y][x] == 0)
					ok = false;

			if (ok)
				cleared++;
		}
		return cleared;
	}

	public static int pileHeight(int[][] board) {
		int columns = board[0].length;

		for (int y = 0; y < board.length - 1; y++) {
			for (int x = 1; x < columns - 1; x++) {
				if (board[y][x] != 0)
					return board.length - 1 - y;
			}
		}
		return 0;
	}

	public static int countSingleHoles(int[][] board) {
		int holes = 0;
		int columns = board[0].length;

		for (int j = 1; j < columns - 1; j++) {
			boolean swap = false;
			for (int i = 0; i < board.length - 1; i++) {
				if (board[i][j] != 0)
					swap = true;
				else {
					if (swap)
						holes++;
				}
			}
		}
		return holes;
	}

	public static int countConnectedHoles(int[][] board) {
		int holes = 0;
		int columns = board[0].length;

		for (int j = 1; j < columns - 1; j++) {
			boolean swap = false;
			for (int i = 0; i < board.length - 1; i++) {
				if (board[i][j] != 0)
					swap = true;
				else {
					if (swap)
						holes++;
					swap = false;
				}
			}
		}

		return holes;
	}

	public static int blocksAboveHoles(int[][] board) {
		int blocks = 0;
		int cols = board[0].length;

		for (int c = 1; c < cols - 1; c++) {
			boolean swap = false;
			for (int r = board.length - 2; r >= 0; r--) {
				if (board[r][c] == 0)
					swap = true;
				else {
					if (swap)
						blocks++;
				}
			}
		}
		return blocks;
	}

	public static int countWells(int[][] board) {
		int cols = board[0].length;
		int wells = 0;

		for (int col = 2; col < cols - 2; col++) {
			for (int row = 0; row < board.length - 1; row++) {
				if ((board[row][col - 1] > 0) && (board[row][col + 1] > 0) && (board[row][col] == 0))
					wells++;
				else if (board[row][col] > 0)
					break;
			}
		}

		for (int row = 0; row < board.length - 1; row++) {
			if ((board[row][1] == 0) && (board[row][2] > 0))
				wells++;
			else if (board[row][1] > 0)
				break;
		}

		for (int row = 0; row < board.length - 1; row++) {
			if ((board[row][cols - 3] > 0) && (board[row][cols - 2] == 0))
				wells++;
			else if (board[row][cols - 2] > 0)
				break;
		}
		return wells;
	}

	public static float bumpiness(int[][] board) {
		int cols = board[0].length;
		int[] heights = new int[cols - 2];

		for (int x = 1; x < cols - 1; x++) {
			for (int y = 0; y < board.length - 1; y++) {
				if (board[y][x] != 0) {
					heights[x - 1] = board.length - 1 - y;
					break;
				}
			}
		}

		float bmp = 0.0f;
		for (int i = 0; i < cols - 3; i++) {
			float diff = Math.abs(heights[i] - heights[i + 1]);
			bmp += diff;
		}

		return bmp;
	}
}