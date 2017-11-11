import java.util.ArrayList;


class GradientDescent {

    private ArrayList<Data> dataSet;

    GradientDescent(ArrayList<Data> dataSet) {

        this.dataSet = dataSet;
    }

    private WeightsArray countDs(ArrayList<Data> dataSet, WeightsArray currentWeights) {

        double sum = 0;
        for (Data data : dataSet) {

            sum += data.area * (currentWeights.areaWeight * data.area +
                    currentWeights.roomsWeight * data.rooms + currentWeights.freeWeight - data.price);
        }
        double dAreaWeight = - sum;

        sum = 0;
        for (Data data : dataSet) {

            sum += data.rooms * (currentWeights.areaWeight * data.area +
                    currentWeights.roomsWeight * data.rooms + currentWeights.freeWeight - data.price);
        }
        double dRoomsWeight = - sum;

        sum = 0;
        for (Data data : dataSet) {

            sum += currentWeights.freeWeight + currentWeights.areaWeight * data.area +
                    currentWeights.roomsWeight * data.rooms - data.price;
        }
        double dFreeWeight = - sum;

        double denominator = Math.pow(Math.pow(dAreaWeight, 2) + Math.pow(dRoomsWeight, 2) + Math.pow(dFreeWeight, 2), 0.5);

        return new WeightsArray(dAreaWeight / denominator, dRoomsWeight / denominator, dFreeWeight / denominator);
    }


    WeightsArray CountWeights(int workIterations) {
        
        WeightsArray currentWeights = new WeightsArray(0, 0, 0);
        WeightsArray newWeights;
        WeightsArray dWeights;
        double step;

        for (int i = 0; i < workIterations; i++) {

            dWeights = countDs(dataSet, currentWeights);

            step = 1;
            newWeights = new WeightsArray(currentWeights, dWeights, step);
            while (MyMath.getStandardDeviation(dataSet, newWeights) > MyMath.getStandardDeviation(dataSet, currentWeights)) {

                step /= 2;
                newWeights = new WeightsArray(currentWeights, dWeights, step);
            }

            if (MyMath.getStandardDeviation(dataSet, newWeights) == MyMath.getStandardDeviation(dataSet, currentWeights)) {

                step /= 2;
                newWeights = new WeightsArray(currentWeights, dWeights, step);
            }

            currentWeights = newWeights;

        }

        return currentWeights;
    }

}
