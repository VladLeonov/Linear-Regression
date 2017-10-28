import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Form  extends JFrame {

    private JPanel mainPanel;
    private JLabel methodsLabel;
    private ArrayList<Data> dataSet = new ArrayList<>();
    private double areaFactor = 0.01, roomsFactor = 1, priceFactor = 0.00001;
    private WeightsArray gradientDescentWeights, geneticAlgorithmWeights;

    private Form() {

        dataSet = Data.loadDataSet(System.getProperty("user.dir") + "/src/main/res/prices.txt");
        for (int i = 0; i < dataSet.size(); i++) {
            dataSet.set(i, Data.normalize(dataSet.get(i), areaFactor, roomsFactor, priceFactor));
        }

        gradientDescentWeights = new WeightsArray(0, 0, 0);//TODO
        geneticAlgorithmWeights = new WeightsArray(0, 0, 0);//TODO

        methodsLabel.setText("<html>Gradient Descent:<br>Weights: " +
                gradientDescentWeights.toString() + "<br>" +
                "Standard Deviation = " +
                MyMath.getStandardDeviation(dataSet, gradientDescentWeights) + "<br>" +
                "Genetic Algorithm:<br>Weights: " +
                geneticAlgorithmWeights.toString() + "<br>" +
                "Standard Deviation = " +
                MyMath.getStandardDeviation(dataSet, geneticAlgorithmWeights) + "</html>");

        adjustDisplay();
    }


    private void adjustDisplay() {

        setContentPane(mainPanel);
        setSize(new Dimension(600, 600));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Linear regression");
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) throws InterruptedException {
        new Form();
    }
}
