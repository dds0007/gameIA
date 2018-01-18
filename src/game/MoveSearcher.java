package game;

import java.util.ArrayList;

public class MoveSearcher {

	public static ArrayList<TetrisMove> getValidMoves(TetrisBoard board, TetrisPiece piece, TetrisAgent agent) {
		ArrayList<TetrisMove> moves = new ArrayList<TetrisMove>();
		Point originalPosition = new Point(piece.position.x, piece.position.y);

		for (int i = 0; i < 4; i++) {
			piece.rotation = i;
			int startY = piece.position.y;

			while (board.canMoveLeft(piece))
				piece.moveLeft();

			do {
				while (board.canMoveDown(piece))
					piece.moveDown();

				board.updateBoard(piece);

				float eval = agent.eval(board.board);
				TetrisMove tempMove = new TetrisMove();
				tempMove.eval = eval;
				tempMove.position.x = piece.position.x;
				tempMove.position.y = piece.position.y;
				tempMove.rotation = piece.rotation;

				moves.add(tempMove);

				board.removePiece(piece);
				piece.position.y = startY;

				if (!board.canMoveRight(piece)) {
					break;
				} else {
					piece.moveRight();
				}

			} while (true);

			piece.position = originalPosition;
		}

		return moves;
	}

	public static TetrisMove getBestMove(TetrisBoard board, TetrisPiece piece, TetrisAgent agent) {
		TetrisMove best = null;
		ArrayList<TetrisMove> moves = getValidMoves(board, piece, agent);

		for (TetrisMove move : moves) {
			if (best == null)
				best = move;
			else if (move.eval > best.eval)
				best = move;
		}
		return best;
	}
}