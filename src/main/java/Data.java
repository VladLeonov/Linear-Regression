import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Data {

    double area, rooms, price;

    public Data(double area, double rooms, double price) {
        this.area = area;
        this.rooms = rooms;
        this.price = price;
    }

    public static ArrayList<Data> loadDataSet(String fileName) {

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

    public static Data normalize(Data data,double areaFactor, double roomsFactor, double priceFactor) {

        return new Data(data.area * areaFactor,
        data.rooms * roomsFactor,
        data.price * priceFactor);
    }
}