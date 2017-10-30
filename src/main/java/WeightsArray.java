
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

    String toString(double areaFactor, double roomsFactor, double priceFactor) {
        return String.format("%.3f%n", areaWeight * areaFactor / priceFactor) + " " +
                String.format("%.3f%n", roomsWeight * roomsFactor / priceFactor) + " " +
                String.format("%.3f%n", freeWeight / priceFactor);
    }
}
