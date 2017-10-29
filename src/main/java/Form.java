import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Form  extends JFrame {

    private JPanel mainPanel;
    private JLabel methodsLabel;
    private JButton calculateForNewDataButton;
    private JTextField areaTextField;
    private JTextField roomsTextField;
    private JLabel resultLabel;
    private double areaFactor = 0.01, roomsFactor = 1, priceFactor = 0.00001;
    private WeightsArray gradientDescentWeights, geneticAlgorithmWeights;

    private Form() {

        ArrayList<Data> dataSet = Data.loadDataSet(System.getProperty("user.dir") + "/src/main/res/prices.txt");
        for (int i = 0; i < dataSet.size(); i++) {
            dataSet.set(i, Data.normalize(dataSet.get(i), areaFactor, roomsFactor, priceFactor));
        }

        gradientDescentWeights = new WeightsArray(1, 1, 1);//TODO

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(dataSet, 100, 0.2);
        geneticAlgorithm.work(1000);
        geneticAlgorithmWeights = geneticAlgorithm.getBestWeights();

        methodsLabel.setText("<html>Gradient Descent:<br>Weights: " +
                gradientDescentWeights.toString() + "<br>" +
                "Standard Deviation = " +
                MyMath.getStandardDeviation(dataSet, gradientDescentWeights) + "<br>" +
                "Genetic Algorithm:<br>Weights: " +
                geneticAlgorithmWeights.toString() + "<br>" +
                "Standard Deviation = " +
                MyMath.getStandardDeviation(dataSet, geneticAlgorithmWeights) + "</html>");

        adjustDisplay();

        calculateForNewDataButton.addActionListener(e -> {

            Data data = new Data(Double.parseDouble(areaTextField.getText()) * areaFactor,
                    Double.parseDouble(roomsTextField.getText()) * roomsFactor,0);
            resultLabel.setText("<html>Price by Standard Deviation = " +
                    MyMath.getPrice(data, gradientDescentWeights) / priceFactor + "<br>" +
                    "Price by Genetic Algorithm = " +
                    MyMath.getPrice(data, geneticAlgorithmWeights) / priceFactor + "</html>");
        });
    }


    private void adjustDisplay() {

        setContentPane(mainPanel);
        setSize(new Dimension(300, 300));
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
