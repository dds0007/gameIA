package game;

public class TetrisMove {
	Point position = new Point(0, 0);
	int rotation;
	float eval;

	public TetrisMove() {
	}

	public TetrisMove(Point p, int rot) {
		this.position = p;
		this.rotation = rot;
	}
}