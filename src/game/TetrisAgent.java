package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TetrisAgent implements Comparable<Object> {
	float[] genes = new float[] { 0.6f, 0.3f, 0.05f, 0.1f, 0.1f, 0.1f, 0.2f };

	List<Integer> clearedRowsArray = Collections.synchronizedList(new ArrayList<Integer>());
	float fitness;

	public float eval(int[][] board) {
		float sum = 0.0f;

		sum += genes[0] * BoardEvaluator.clearedRows(board);
		sum -= genes[1] * BoardEvaluator.pileHeight(board);
		sum -= genes[2] * BoardEvaluator.countSingleHoles(board);
		sum -= genes[3] * BoardEvaluator.countConnectedHoles(board);
		sum -= genes[4] * BoardEvaluator.blocksAboveHoles(board);
		sum -= genes[5] * BoardEvaluator.countWells(board);
		sum -= genes[6] * BoardEvaluator.bumpiness(board);

		return sum;
	}

	public int compareTo(Object obj) {
		if (obj instanceof TetrisAgent) {
			TetrisAgent other = (TetrisAgent) obj;
			if (this.fitness > other.fitness)
				return 1;
			else if (this.fitness < other.fitness)
				return -1;
		}
		return 0;
	}

	public static TetrisAgent randomAgent() {
		TetrisAgent agent = new TetrisAgent();
		Random random = new Random();

		for (int i = 0; i < agent.genes.length; i++)
			agent.genes[i] = random.nextFloat();

		return agent;
	}

	public TetrisAgent cloneAgent() {
		TetrisAgent cloneAgent = new TetrisAgent();

		for (int i = 0; i < this.genes.length; i++)
			cloneAgent.genes[i] = this.genes[i];

		return cloneAgent;
	}
}