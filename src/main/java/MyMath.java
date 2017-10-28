import java.util.ArrayList;

public abstract class MyMath {

    private MyMath() {}

    public static double getStandardDeviation(ArrayList<Data> dataSet, WeightsArray weightsArray) {

        int n = dataSet.size();
        double sum = 0;
        Data data;

        for (int i = 0; i < n; i++) {

            data = dataSet.get(i);
            sum += Math.pow(data.area * weightsArray.areaWeight +
                    data.rooms * weightsArray.roomsWeight +
                    weightsArray.freeWeight - data.price, 2);
        }

        return Math.pow(sum / (n - 1), 0.5);
    }
}
