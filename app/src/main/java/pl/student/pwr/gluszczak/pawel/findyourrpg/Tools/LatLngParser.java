package pl.student.pwr.gluszczak.pawel.findyourrpg.Tools;

public abstract class LatLngParser {


    //sqrt((x2-x1)^2 + (y2-y1)^2)
    //lat deg = 110.574 km
    //long deg = 111.320*cos(lat) km

    public static double calculateDistance(double lat2, double lng2, double lat1, double lng1) {
        double lat2KM = lat2 / 110.574;
        double lat1KM = lat1 / 110.574;

        double lng2KM = lng2 / 111.320 * Math.cos(lat2);
        double lng1KM = lng1 / 111.320 * Math.cos(lat1);

        double distance = Math.sqrt(
                Math.abs(Math.pow((lat2KM - lat1KM), 2)) + Math.abs(Math.pow((lng2KM - lng1KM), 2)));

        return distance;
    }
}
