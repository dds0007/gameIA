package game;

import java.util.concurrent.*;

public class AIGame extends GamePanel {
	private static final long serialVersionUID = 1L;
	private CountDownLatch doneSignal;
	TetrisAgent agent = new TetrisAgent();
	int cleared = 0;

	public AIGame(int r, int c, int bs, int seed, CountDownLatch signal) {
		this(r, c, bs, seed);
		this.doneSignal = signal;
	}

	public AIGame(int r, int c, int bs, int seed) {
		super(r, c, bs, seed);
	}

	public AIGame(int r, int c, int bs) {
		super(r, c, bs);
	}

	public void run() {
		fallingPiece = randomPiece();

		while (!gameOver) {
			if (board.canMoveDown(fallingPiece)) {
				TetrisMove bestMove = MoveSearcher.getBestMove(board, fallingPiece.clonePiece(), agent);

				fallingPiece.position = bestMove.position;
				fallingPiece.rotation = bestMove.rotation;

				board.updateBoard(fallingPiece);

			} else {
				board.updateBoard(fallingPiece);
				cleared += board.checkCompleteRows();
				addNewPiece();
			}
			repaint();
		}

		agent.clearedRowsArray.add(cleared);
		if (doneSignal != null)
			doneSignal.countDown();

	}
}