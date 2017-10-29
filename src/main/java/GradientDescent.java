import java.util.ArrayList;

import static java.lang.Math.*;


public class GradientDescent {

    ArrayList<Data> dataSet;
    double areaWeight, roomsWeight, freeWeight;
    double oldAreaWeight, oldRoomsWeight, oldFreeWeight;
    double dAreaWeight, dRoomsWeight, dFreeWeight;
    double newAreaWeight, newRoomsWeight, newFreeWeight;
    double step;

    public GradientDescent(ArrayList<Data> dataSet) {

        this.dataSet = dataSet;

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

        dAreaWeight = sum * 2 / (1 - n);

        for (int i = 0; i < n; i++) {

            data = dataSet.get(i);
            sum += data.rooms * (areaWeight * data.area + roomsWeight * data.rooms - data.price);
        }

        dRoomsWeight = sum * 2 / (1 - n);

        for (int i = 0; i < n; i++) {

            data = dataSet.get(i);
            sum += freeWeight + areaWeight * data.area + roomsWeight * data.rooms - data.price;
        }

        dFreeWeight = sum * 2 / (1 - n);
    }


    public WeightsArray CountWeights() {
        
        WeightsArray weightsArray;
        WeightsArray oldWeightsArray;

        int counter = 0;
        double relativeImprovement;

        do {
            countDs(dataSet);
            while ((areaWeight * newAreaWeight + roomsWeight * newRoomsWeight + freeWeight * newFreeWeight) < 0) {

                step = step / 1.5;
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
            oldWeightsArray = new WeightsArray(oldAreaWeight, oldRoomsWeight, oldFreeWeight);

            counter++;

            relativeImprovement = abs(MyMath.getStandardDeviation(dataSet, oldWeightsArray) -
                    MyMath.getStandardDeviation(dataSet, weightsArray)) /
                    MyMath.getStandardDeviation(dataSet, oldWeightsArray);

            System.out.println(relativeImprovement);
        } while (relativeImprovement > 0.000000000000001);

        System.out.println(counter);
        return weightsArray;
    }

}
