package kr.ac.dankook.SokGangPetTour.util;

public class DistCalculationUtil {

    public static double getDistance(
            double statLatitude, double statLongitude, double desLatitude, double desLongitude
    ) {
        double theta = desLongitude - statLongitude;
        double dist = Math.sin(deg2rad(statLatitude)) *
                Math.sin(deg2rad(desLatitude)) +
                Math.cos(deg2rad(statLatitude)) *
                        Math.cos(deg2rad(desLatitude)) *
                        Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1609.344;
        return dist / 1000;
    }
    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    public static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
