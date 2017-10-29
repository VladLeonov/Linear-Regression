import java.util.ArrayList;
import java.util.Random;

class Genotype  implements Comparable<Genotype> {

    WeightsArray genes;
    ArrayList<Data> dataSet;

    Genotype(ArrayList<Data> dataSet) {

        this.dataSet = dataSet;
    }

    Genotype(double genesValueBorder, Random random, ArrayList<Data> dataSet) {

        this(dataSet);

        genes = new WeightsArray(2 * genesValueBorder * random.nextDouble() - genesValueBorder,
                2 * genesValueBorder * random.nextDouble() - genesValueBorder,
                2 * genesValueBorder * random.nextDouble() - genesValueBorder);
    }

    Genotype(Genotype firstParent, Genotype secondParent, ArrayList<Data> dataSet) {

        this(dataSet);

        WeightsArray genes1 = firstParent.genes, genes2 = secondParent.genes;
        genes = new WeightsArray((genes1.areaWeight + genes2.areaWeight) / 2,
                (genes1.roomsWeight + genes2.roomsWeight) / 2,
                (genes1.freeWeight + genes2.freeWeight) / 2);//TODO make better
    }

    void mutate(int geneIndex, double proportionOfScatter, Random random) {

        double valueMultiplier = 1 - proportionOfScatter + (2 * proportionOfScatter) * random.nextDouble();
        switch (geneIndex) {

            case 0:
                genes.areaWeight *= valueMultiplier;
                break;
            case 1:
                genes.roomsWeight *= valueMultiplier;
                break;
            case 2:
                genes.freeWeight *= valueMultiplier;
                break;
        }
    }

    @Override
    public int compareTo(Genotype other) {
        return Double.compare(MyMath.getStandardDeviation(dataSet, genes),
                MyMath.getStandardDeviation(dataSet, other.genes));
    }
}
