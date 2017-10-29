
class WeightsArray {

    double areaWeight, roomsWeight, freeWeight;

    WeightsArray(double areaWeight, double roomsWeight, double freeWeight) {
        this.areaWeight = areaWeight;
        this.roomsWeight = roomsWeight;
        this.freeWeight = freeWeight;
    }

    @Override
    public String toString() {
        return areaWeight + " " + roomsWeight + " " + freeWeight;
    }
}
