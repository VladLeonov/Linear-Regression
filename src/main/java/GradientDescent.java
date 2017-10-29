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

            sum += data.area * (currentWeights.areaWeight * data.area + currentWeights.roomsWeight * data.rooms - data.price);
        }

        double dAreaWeight = sum * 2 / (1 - n);

        sum = 0;
        for (Data data : dataSet) {

            sum += data.rooms * (currentWeights.areaWeight * data.area + currentWeights.roomsWeight * data.rooms - data.price);
        }

        double dRoomsWeight = sum * 2 / (1 - n);

        sum = 0;
        for (Data data : dataSet) {

            sum += currentWeights.freeWeight + currentWeights.areaWeight * data.area + currentWeights.roomsWeight * data.rooms - data.price;
        }

        double dFreeWeight = sum * 2 / (1 - n);

        return new WeightsArray(dAreaWeight, dRoomsWeight, dFreeWeight);
    }


    WeightsArray CountWeights() {
        
        WeightsArray currentWeights = new WeightsArray(0, 0, 0);
        WeightsArray oldWeights = new WeightsArray(10, 10, 10);
        WeightsArray newWeights = new WeightsArray(10, 10, 10);
        WeightsArray dWeights;

        double relativeImprovement;
        double step = 10;

        do {
            dWeights = countDs(dataSet, currentWeights);

            while ((currentWeights.areaWeight * newWeights.areaWeight +
                    currentWeights.roomsWeight * newWeights.roomsWeight +
                    currentWeights.freeWeight * newWeights.freeWeight) < 0) {

                step = step / 1.5;
                newWeights = new WeightsArray(currentWeights.areaWeight + step * dWeights.areaWeight,
                        currentWeights.roomsWeight + step * dWeights.roomsWeight,
                        currentWeights.freeWeight + step * dWeights.freeWeight);//TODO change to method
            }

            oldWeights.areaWeight = currentWeights.areaWeight;//TODO change to copying
            oldWeights.roomsWeight = currentWeights.roomsWeight;
            oldWeights.freeWeight = currentWeights.freeWeight;

            currentWeights.areaWeight += step * dWeights.areaWeight;//TODO change to method
            currentWeights.roomsWeight += step * dWeights.roomsWeight;
            currentWeights.freeWeight += step * dWeights.freeWeight;

            double oldStandardDeviation = MyMath.getStandardDeviation(dataSet, oldWeights);
            double currentStandardDeviation = MyMath.getStandardDeviation(dataSet, currentWeights);
            relativeImprovement = abs(oldStandardDeviation - currentStandardDeviation) / oldStandardDeviation;

        } while (relativeImprovement > 0);

        return currentWeights;
    }

}
