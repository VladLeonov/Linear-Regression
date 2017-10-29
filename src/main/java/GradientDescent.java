import java.util.ArrayList;

import static java.lang.Math.*;


public class GradientDescent {

    ArrayList<Data> dataSet2 = new ArrayList<>();
    double areaWeight, roomsWeight, freeWeight;
    double oldAreaWeight, oldRoomsWeight, oldFreeWeight;
    double dAreaWeight, dRoomsWeight, dFreeWeight;
    double newAreaWeight, newRoomsWeight, newFreeWeight;
    double step;

    public GradientDescent(ArrayList<Data> dataSet) {

        dataSet2.addAll(dataSet);
        areaWeight = 0;
        roomsWeight = 0;
        freeWeight = 0;
        
        oldAreaWeight = 10;
        oldRoomsWeight = 10;
        oldFreeWeight = 10;

        newAreaWeight = 10;
        newRoomsWeight = 10;
        newFreeWeight = 10;

        step = 10;
    }

    public void countDs(ArrayList<Data> dataSet) {

        int n = dataSet.size();
        double sum = 0;
        Data data;

        for (int i = 0; i < n; i++) {

            data = dataSet.get(i);
            sum += data.area * (areaWeight * data.area + roomsWeight * data.rooms - data.price);
        }

        dAreaWeight = -((2 / (n - 1)) * sum);

        for (int i = 0; i < n; i++) {

            data = dataSet.get(i);
            sum += data.rooms * (areaWeight * data.area + roomsWeight * data.rooms - data.price);
        }

        dRoomsWeight = -((2 / (n - 1)) * sum);

        for (int i = 0; i < n; i++) {

            data = dataSet.get(i);
            sum += freeWeight + areaWeight * data.area + roomsWeight * data.rooms - data.price;
        }

        dFreeWeight = -((2 / (n - 1)) * sum);
    }


    public WeightsArray CountWeights() {
        
        countDs(dataSet2);

        WeightsArray weightsArray = new WeightsArray(areaWeight, roomsWeight, freeWeight);
        WeightsArray oldWeightsArray = new WeightsArray(oldAreaWeight, oldRoomsWeight, oldFreeWeight);

        while ((abs(MyMath.getStandardDeviation(dataSet2, oldWeightsArray) -
                MyMath.getStandardDeviation(dataSet2, weightsArray)) /
                MyMath.getStandardDeviation(dataSet2, oldWeightsArray)) < 0.001){
            while ((areaWeight * newAreaWeight + roomsWeight * newRoomsWeight + freeWeight * newFreeWeight) < 0) {

                step = step / (1.5);
                newAreaWeight = areaWeight + step * dAreaWeight;
                newRoomsWeight = roomsWeight + step * dRoomsWeight;
                newFreeWeight = freeWeight + step * dFreeWeight;
            }

            oldAreaWeight = areaWeight;
            oldRoomsWeight = roomsWeight;
            oldFreeWeight = freeWeight;
            
            areaWeight = areaWeight + step * dAreaWeight;
            roomsWeight = roomsWeight + step * dRoomsWeight;
            freeWeight = freeWeight + step * dFreeWeight;

            weightsArray = new WeightsArray(areaWeight, roomsWeight, freeWeight);
            oldWeightsArray = new WeightsArray(newAreaWeight, newRoomsWeight, newFreeWeight);
        }

        return weightsArray;
    }

}
