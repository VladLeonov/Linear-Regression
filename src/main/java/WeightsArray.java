
class WeightsArray {

    double areaWeight, roomsWeight, freeWeight;

    WeightsArray(double areaWeight, double roomsWeight, double freeWeight) {
        this.areaWeight = areaWeight;
        this.roomsWeight = roomsWeight;
        this.freeWeight = freeWeight;
    }

    WeightsArray(WeightsArray weightsArray) {
        this.areaWeight = weightsArray.areaWeight;
        this.roomsWeight = weightsArray.roomsWeight;
        this.freeWeight = weightsArray.freeWeight;
    }

    WeightsArray(WeightsArray current, WeightsArray gradient, double step) {
        this.areaWeight = current.areaWeight + step * gradient.areaWeight;
        this.roomsWeight = current.roomsWeight + step * gradient.roomsWeight;
        this.freeWeight = current.freeWeight + step * gradient.freeWeight;
    }

    static double dot(WeightsArray weightsArray1, WeightsArray weightsArray2) {
        return weightsArray1.areaWeight * weightsArray2.areaWeight +
                weightsArray1.roomsWeight * weightsArray2.roomsWeight +
                weightsArray1.freeWeight * weightsArray2.freeWeight;
    }

    @Override
    public String toString() {
        return String.format("%.3f%n", areaWeight) + " " +
                String.format("%.3f%n", roomsWeight) + " " +
                String.format("%.3f%n", freeWeight);
    }
}
