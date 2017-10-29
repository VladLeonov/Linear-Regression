import java.util.ArrayList;

abstract class MyMath {

    private MyMath() {}

    static double getStandardDeviation(ArrayList<Data> dataSet, WeightsArray weightsArray) {

        int n = dataSet.size();
        double sum = 0;

        for (Data data : dataSet) {

            sum += Math.pow(getPrice(data, weightsArray) - data.price, 2);
        }

        return Math.pow(sum / (n - 1), 0.5);
    }

    static double getPrice(Data data, WeightsArray weightsArray) {

        return data.area * weightsArray.areaWeight +
                data.rooms * weightsArray.roomsWeight +
                weightsArray.freeWeight;
    }
}
