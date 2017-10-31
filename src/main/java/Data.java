import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

class Data {

    double area, rooms, price;

    Data(double area, double rooms, double price) {
        this.area = area;
        this.rooms = rooms;
        this.price = price;
    }

    static ArrayList<Data> loadDataSet(String fileName) {

        double area, rooms, price;
        ArrayList<Data> dataSet = new ArrayList<>();

        try {

            Scanner sc = new Scanner(new File(fileName)).useLocale(Locale.US);
            while (sc.hasNext()) {
                area = sc.nextDouble();
                rooms = sc.nextDouble();
                price = sc.nextDouble();
                dataSet.add(new Data(area, rooms, price));
            }
            sc.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        return dataSet;
    }

    private static Data normalize(Data data, Data minValues, Data deltaValues) {

        return new Data((data.area - minValues.area) / deltaValues.area,
                (data.rooms - minValues.rooms) / deltaValues.rooms,
                (data.price - minValues.price) / deltaValues.price);
    }

    static Pair<Data, Data> normalizeData(ArrayList<Data> dataSet) {

        Data minValues = new Data(10000, 100, 1000000), maxValues = new Data(0, 0, 0);

        for (Data data : dataSet) {

            if (data.area < minValues.area) {
                minValues.area = data.area;
            }
            if (data.rooms < minValues.rooms) {
                minValues.rooms = data.rooms;
            }
            if (data.price < minValues.price) {
                minValues.price = data.price;
            }

            if (data.area > maxValues.area) {
                maxValues.area = data.area;
            }
            if (data.rooms > maxValues.rooms) {
                maxValues.rooms = data.rooms;
            }
            if (data.price > maxValues.price) {
                maxValues.price = data.price;
            }
        }

        Data midValues = new Data(maxValues.area - minValues.area,
                maxValues.rooms - minValues.rooms,
                maxValues.price - minValues.price);

        for (int i = 0; i < dataSet.size(); i++) {
            dataSet.set(i, Data.normalize(dataSet.get(i), minValues, midValues));
        }

        return new Pair<>(minValues, midValues);
    }

    double[] toDoubleArray() {

        return new double[]{area, rooms, 1};
    }
}