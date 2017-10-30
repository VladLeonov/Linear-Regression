import javafx.util.Pair;

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
    private JPanel drawPanel;
    private double areaFactor = 0.01, roomsFactor = 1, priceFactor = 0.00001;
    private ArrayList<Data> dataSet;
    private WeightsArray gradientDescentWeights, geneticAlgorithmWeights;

    private Form() {

        dataSet = Data.loadDataSet(System.getProperty("user.dir") + "/src/main/res/prices.txt");
        for (int i = 0; i < dataSet.size(); i++) {
            dataSet.set(i, Data.normalize(dataSet.get(i), areaFactor, roomsFactor, priceFactor));
        }

        GradientDescent gradientDescent = new GradientDescent(dataSet);
        gradientDescentWeights = gradientDescent.CountWeights();

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(dataSet,
                100, 0.2, 0.1, 10);
        geneticAlgorithm.work(1000);
        geneticAlgorithmWeights = geneticAlgorithm.getBestWeights();

        methodsLabel.setText("<html>Gradient Descent:<br>Weights: " +
                gradientDescentWeights.toString(areaFactor, roomsFactor, priceFactor) + "<br>" +
                "Standard Deviation = " +
                String.format("%.4f%n", MyMath.getStandardDeviation(dataSet, gradientDescentWeights) / priceFactor) + "<br>" +
                "Genetic Algorithm:<br>Weights: " +
                geneticAlgorithmWeights.toString(areaFactor, roomsFactor, priceFactor) + "<br>" +
                "Standard Deviation = " +
                String.format("%.4f%n", MyMath.getStandardDeviation(dataSet, geneticAlgorithmWeights) / priceFactor) + "</html>");

        adjustDisplay();

        calculateForNewDataButton.addActionListener(e -> {

            Data data = new Data(Double.parseDouble(areaTextField.getText()) * areaFactor,
                    Double.parseDouble(roomsTextField.getText()) * roomsFactor,0);
            resultLabel.setText("<html>Price by Standard Deviation = " +
                    Math.round(MyMath.getPrice(data, gradientDescentWeights) / priceFactor) + "<br>" +
                    "Price by Genetic Algorithm = " +
                    Math.round(MyMath.getPrice(data, geneticAlgorithmWeights) / priceFactor) + "</html>");

            drawPlot();
        });
    }

    private void drawPlot() {

        Graphics2D g = (Graphics2D) drawPanel.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0,0, drawPanel.getWidth(), drawPanel.getHeight());

        double numberOfRooms = Double.parseDouble(roomsTextField.getText());

        if ((numberOfRooms > 0) && (numberOfRooms <= 5)) {

            double maxArea = 0, maxPrice = 0;

            for (Data data : dataSet) {

                if (data.area > maxArea) {
                    maxArea = data.area;
                }
                if (data.price > maxPrice) {
                    maxPrice = data.price;
                }
            }

            maxArea *= 1.1;
            maxPrice *= 1.1;

            ArrayList<Pair<Long, Long>> coordinates = new ArrayList<>();
            for (Data data : dataSet) {

                if (data.rooms == numberOfRooms) {
                    coordinates.add(new Pair<>(Math.round(data.area / maxArea * drawPanel.getWidth()),
                            Math.round(data.price / maxPrice * drawPanel.getHeight())));
                }
            }

            g.setColor(Color.black);
            int radius = 3;
            for (Pair<Long, Long> coordinate : coordinates) {

                g.fillOval(coordinate.getKey().intValue() - radius,
                        drawPanel.getHeight() - (coordinate.getValue().intValue() - radius),
                        2 * radius, 2 * radius);
            }

            g.setColor(Color.red);
            double startY = numberOfRooms * gradientDescentWeights.roomsWeight + gradientDescentWeights.freeWeight;
            double endY = maxArea * gradientDescentWeights.areaWeight + startY;
            g.drawLine(0,
                    drawPanel.getHeight() - (int) Math.round(startY / maxPrice * drawPanel.getHeight()),
                    drawPanel.getWidth(),
                    drawPanel.getHeight() - (int) Math.round(endY / maxPrice * drawPanel.getHeight()));

            g.setColor(Color.green);
            startY = numberOfRooms * geneticAlgorithmWeights.roomsWeight + geneticAlgorithmWeights.freeWeight;
            endY = maxArea * geneticAlgorithmWeights.areaWeight + startY;
            g.drawLine(0,
                    drawPanel.getHeight() - (int) Math.round(startY / maxPrice * drawPanel.getHeight()),
                    drawPanel.getWidth(),
                    drawPanel.getHeight() - (int) Math.round(endY / maxPrice * drawPanel.getHeight()));


            double area = Double.parseDouble(areaTextField.getText()) * areaFactor;
            double priceGD = area * gradientDescentWeights.areaWeight +
                    numberOfRooms * gradientDescentWeights.roomsWeight +
                    gradientDescentWeights.freeWeight;
            double priceGA = area * geneticAlgorithmWeights.areaWeight +
                    numberOfRooms * geneticAlgorithmWeights.roomsWeight +
                    geneticAlgorithmWeights.freeWeight;

            g.setColor(Color.red);
            g.fillRect((int) Math.round(area / maxArea * drawPanel.getWidth() - radius),
                    (int) Math.round(drawPanel.getHeight() - (priceGD / maxPrice * drawPanel.getHeight()) - radius),
                    2 * radius, 2 * radius);

            g.setColor(Color.green);
            g.fillOval((int) Math.round(area / maxArea * drawPanel.getWidth() - radius),
                    (int) Math.round(drawPanel.getHeight() - (priceGA / maxPrice * drawPanel.getHeight()) - radius),
                    2 * radius, 2 * radius);
        }
    }

    private void adjustDisplay() {

        setContentPane(mainPanel);
        setSize(new Dimension(600, 350));
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
