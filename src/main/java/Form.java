import javafx.util.Pair;
import org.ejml.data.DMatrixRMaj;
import org.ejml.simple.SimpleMatrix;

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
    private ArrayList<Data> dataSet;
    private Pair<Data, Data> factors;
    private double priceFactor;
    private WeightsArray gradientDescentWeights, geneticAlgorithmWeights;

    private Form() {

        dataSet = Data.loadDataSet(System.getProperty("user.dir") + "/src/main/res/prices.txt");
        printTrueSolution();

        factors = Data.normalizeData(dataSet);
        priceFactor = factors.getValue().price;

        final int workIterationsGA = 1000, numberOfGenotypes = 1000;

        GradientDescent gradientDescent = new GradientDescent(dataSet);
        gradientDescentWeights = gradientDescent.CountWeights(numberOfGenotypes * workIterationsGA);

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(dataSet,
                numberOfGenotypes, 0.2, 0.1, 10);
        geneticAlgorithm.work(workIterationsGA);
        geneticAlgorithmWeights = geneticAlgorithm.getBestWeights();

        methodsLabel.setText("<html>Gradient Descent:<br>Weights: " +
                gradientDescentWeights.toString() + "<br>" +
                "Standard Deviation = " +
                String.format("%.7f%n", MyMath.getStandardDeviation(dataSet, gradientDescentWeights) * priceFactor) + "<br>" +
                "Genetic Algorithm:<br>Weights: " +
                geneticAlgorithmWeights.toString() + "<br>" +
                "Standard Deviation = " +
                String.format("%.7f%n", MyMath.getStandardDeviation(dataSet, geneticAlgorithmWeights) * priceFactor) + "</html>");

        adjustDisplay();

        calculateForNewDataButton.addActionListener(e -> {

            Data data = new Data(Double.parseDouble(areaTextField.getText()),
                    Double.parseDouble(roomsTextField.getText()),0);
            resultLabel.setText("<html>Price by Standard Deviation = " +
                    Math.round(MyMath.getPrice(data, gradientDescentWeights) * priceFactor) + "<br>" +
                    "Price by Genetic Algorithm = " +
                    Math.round(MyMath.getPrice(data, geneticAlgorithmWeights) * priceFactor) + "</html>");

            drawPlot();
        });
    }

    private void printTrueSolution() {

        int n = dataSet.size();
        double[][] dataX = new double[n][];
        double[][] dataY = new double[n][];
        for (int i = 0; i < n; i++) {

            dataX[i] = dataSet.get(i).toDoubleArray();
            dataY[i] = new double[]{dataSet.get(i).price};
        }

        SimpleMatrix X = new SimpleMatrix(dataX);
        SimpleMatrix Y = new SimpleMatrix(dataY);

        SimpleMatrix B = (X.transpose().mult(X)).invert().mult(X.transpose()).mult(Y);

        double w1 = B.get(0, 0);
        double w2 = B.get(1, 0);
        double w3 = B.get(2, 0);
        System.out.println(w1 + " " + w2 + " " + w3);
        System.out.println(MyMath.getStandardDeviation(dataSet, new WeightsArray(w1, w2, w3)));
    }

    private void drawPlot() {

        Graphics2D g = (Graphics2D) drawPanel.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0,0, drawPanel.getWidth(), drawPanel.getHeight());

        double numberOfRooms = (Double.parseDouble(roomsTextField.getText()) - factors.getKey().rooms) / factors.getValue().rooms;

        if ((numberOfRooms >= 0) && (numberOfRooms <= 1)) {

            double maxArea = 0, maxPrice = 0;

            for (Data data : dataSet) {

                if (data.area > maxArea) {
                    maxArea = data.area;
                }
                if (data.price > maxPrice) {
                    maxPrice = data.price;
                }
            }

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


            double area = (Double.parseDouble(areaTextField.getText()) - factors.getKey().area) / factors.getValue().area;
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
