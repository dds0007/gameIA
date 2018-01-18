package game;

import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.Random;

public class GeneticAlgorithm {
	public static void main(String[] args) {
		int popSize = 20; // agents in generation
		int maxGens = 50; // maximum mutation number
		float probMutate = 0.2f; // probability to mutate
		int gamesPerAgent = 6; // games every agent plays(then avg)
		float eliteRate = 0.2f; // elite ratio

		runGA(popSize, maxGens, probMutate, gamesPerAgent, eliteRate);
	}

	public static TetrisAgent[] blendCrossover(TetrisAgent father, TetrisAgent mother) {
		TetrisAgent copy1 = father.cloneAgent();
		TetrisAgent copy2 = mother.cloneAgent();

		Random random = new Random();
		int N_GENES = father.genes.length;
		int index = random.nextInt(N_GENES);

		for (int i = index; i < N_GENES; i++) {
			float beta = random.nextFloat();
			float var1 = father.genes[i];
			float var2 = mother.genes[i];

			float newVar1 = beta * var1 + (1 - beta) * var2;
			float newVar2 = (1 - beta) * var1 + beta * var2;

			copy1.genes[i] = newVar1;
			copy2.genes[i] = newVar2;
		}

		return new TetrisAgent[] { copy1, copy2 };
	}

	public static void mutateNoise(TetrisAgent agent, float probMutate) {
		int N_GENES = agent.genes.length;
		Random random = new Random();

		for (int i = 0; i < N_GENES; i++) {
			float sort = random.nextFloat();
			if (sort < probMutate) {

				float prob = random.nextFloat();
				if (prob < 0.5f) {
					float genValue = agent.genes[i];
					float complement = 1.0f - genValue;
					float noise = random.nextFloat() * complement;
					agent.genes[i] += noise;

				} else {
					float genValue = agent.genes[i];
					float noise = random.nextFloat() * genValue;
					agent.genes[i] -= noise;
				}
			}
		}
	}

	public static TetrisAgent doSelection(TetrisAgent[] population) {
		TetrisAgent sorted = null;

		float totalFitness = 0.0f;
		for (TetrisAgent agent : population)
			totalFitness += agent.fitness;

		float frac = new Random().nextFloat();
		float cut = frac * totalFitness;

		for (TetrisAgent agent : population) {
			cut -= agent.fitness;
			if (cut <= 0.0f) {
				sorted = agent;
				break;
			}
		}
		return sorted;
	}

	public static ArrayList<TetrisAgent[]> runGA(int popSize, int maxGens, float probMutate, int gamesPerAgent,
			float eliteRate) {
		ArrayList<TetrisAgent[]> hist = new ArrayList<TetrisAgent[]>();
		// we make the population even so that the elite is defined
		if (popSize % 2 == 1)
			popSize += 1;
		int elite = (int) (popSize * eliteRate);
		if (elite % 2 == 1)
			elite += 1;

		int currentGeneration = 0;

		Random random = new Random();
		TetrisAgent[] population = new TetrisAgent[popSize];
		TetrisAgent bestAgent = null;
		int rows = 20;
		int cols = 10;
		int blockSize = 1;

		// initial population is random
		for (int i = 0; i < population.length; i++)
			population[i] = TetrisAgent.randomAgent();

		int seed = random.nextInt();

		do {
			CountDownLatch doneSignal = new CountDownLatch(popSize * gamesPerAgent);
			for (TetrisAgent agent : population) {
				for (int i = 0; i < gamesPerAgent; i++) {
					AIGame game = new AIGame(rows, cols, blockSize, i + seed, doneSignal);
					game.agent = agent;
					new Thread(game).start();
				}
			}

			try {
				doneSignal.await();
			} catch (InterruptedException ex) {
			}

			// calculate fitness as the avg points
			for (TetrisAgent agent : population) {
				float average = 0.0f;

				for (Integer cleared : agent.clearedRowsArray)
					average += cleared;
				agent.fitness = 1.0f * average / gamesPerAgent;
				agent.clearedRowsArray.clear();

			}


			for (TetrisAgent agent : population)
				if (bestAgent == null || agent.fitness > bestAgent.fitness)
					bestAgent = agent;

			TetrisAgent[] newPopulation = new TetrisAgent[popSize];
			java.util.Arrays.sort(population);

			for (int i = 0; i < elite; i++)
				newPopulation[i] = population[popSize - i - 1];

			int newIndex = elite;
			for (int i = 0; i < (popSize - elite) / 2; i++) {
				TetrisAgent parentA = doSelection(population);
				TetrisAgent parentB = doSelection(population);

				TetrisAgent[] children = blendCrossover(parentA, parentB);
				TetrisAgent childA = children[0];
				TetrisAgent childB = children[1];

				mutateNoise(childA, probMutate);
				mutateNoise(childB, probMutate);

				newPopulation[newIndex] = childA;
				newIndex++;
				newPopulation[newIndex] = childB;
				newIndex++;
			}

			hist.add(population);
			population = newPopulation;
			currentGeneration++;

			System.out.println("Best in  generation " + currentGeneration + " AVG " + gamesPerAgent + " games :"
					+ bestAgent.fitness);
			for (int i = 0; i < bestAgent.genes.length; i++)
				System.out.print(bestAgent.genes[i] + "  ");
			System.out.println();

		} while (currentGeneration < maxGens);

		return hist;
	}
} 