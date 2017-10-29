import java.util.ArrayList;

import static java.lang.Math.*;


class GradientDescent {

    private ArrayList<Data> dataSet;

    GradientDescent(ArrayList<Data> dataSet) {

        this.dataSet = dataSet;
    }

    private WeightsArray countDs(ArrayList<Data> dataSet, WeightsArray currentWeights) {

        int n = dataSet.size();

        double sum = 0;
        for (Data data : dataSet) {

            sum += data.area * (currentWeights.areaWeight * data.area +
                    currentWeights.roomsWeight * data.rooms - data.price);
        }
        double dAreaWeight = sum * 2 / (1 - n);

        sum = 0;
        for (Data data : dataSet) {

            sum += data.rooms * (currentWeights.areaWeight * data.area +
                    currentWeights.roomsWeight * data.rooms - data.price);
        }
        double dRoomsWeight = sum * 2 / (1 - n);

        sum = 0;
        for (Data data : dataSet) {

            sum += currentWeights.freeWeight + currentWeights.areaWeight * data.area +
                    currentWeights.roomsWeight * data.rooms - data.price;
        }
        double dFreeWeight = sum * 2 / (1 - n);

        return new WeightsArray(dAreaWeight, dRoomsWeight, dFreeWeight);
    }


    WeightsArray CountWeights() {
        
        WeightsArray currentWeights = new WeightsArray(0, 0, 0);
        WeightsArray oldWeights;
        WeightsArray newWeights;
        WeightsArray dWeights;

        double relativeImprovement;
        double step = 10;

        do {
            dWeights = countDs(dataSet, currentWeights);

            newWeights = new WeightsArray(currentWeights, dWeights, step);
            while (MyMath.getStandardDeviation(dataSet, newWeights) > MyMath.getStandardDeviation(dataSet, currentWeights)) {

                step /= 2;
                newWeights = new WeightsArray(currentWeights, dWeights, step);
            }

            oldWeights = new WeightsArray(currentWeights);
            currentWeights = newWeights;

            double oldStandardDeviation = MyMath.getStandardDeviation(dataSet, oldWeights);
            double currentStandardDeviation = MyMath.getStandardDeviation(dataSet, currentWeights);
            relativeImprovement = abs(oldStandardDeviation - currentStandardDeviation) / oldStandardDeviation;

        } while (relativeImprovement > 0);

        return currentWeights;
    }

}
