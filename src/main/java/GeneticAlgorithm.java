import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

class GeneticAlgorithm {

    private ArrayList<Genotype> genotypes;
    private ArrayList<Data> dataSet;
    private Random random;
    private int numberOfGenotypes;
    private double proportionOfMutants;

    GeneticAlgorithm(ArrayList<Data> dataSet, int numberOfGenotypes, double proportionOfMutants) {

        this.dataSet = dataSet;
        this.numberOfGenotypes = numberOfGenotypes;
        this.proportionOfMutants = proportionOfMutants;

        random = new Random(System.currentTimeMillis());
        genotypes = new ArrayList<>();

        for (int i = 0; i < numberOfGenotypes; i++) {

            genotypes.add(new Genotype(10, random, dataSet));
        }
    }

    private void nextIteration() {

        for (Genotype genotype : genotypes) {

            if (random.nextDouble() < proportionOfMutants) {
                genotype.mutate(random.nextInt() % 3, 0.05, random);
            }
        }

        Collections.sort(genotypes);

        for (int i = numberOfGenotypes / 2; i < numberOfGenotypes; i++) {

            genotypes.set(i, new Genotype(genotypes.get(Math.abs(random.nextInt()) % (numberOfGenotypes / 2)),
                    genotypes.get(Math.abs(random.nextInt()) % (numberOfGenotypes / 2)),
                    dataSet));
        }
    }

    void work(int workIterations) {

        for (int i = 0; i < workIterations; i++) {
            nextIteration();
        }
    }

    WeightsArray getBestWeights() {

        Collections.sort(genotypes);
        return genotypes.get(0).genes;
    }
}
