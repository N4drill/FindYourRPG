package pl.student.pwr.gluszczak.pawel.findyourrpg.Tools;

abstract public class UserUtils {

    /**
     * Returns float average of 3 float values
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static float calculateAverage(float a, float b, float c) {
        return (a + b + c) / 3;
    }

    /**
     * Returns float average of 2 float values
     *
     * @param a
     * @param b
     * @return
     */
    public static float calculateAverage(float a, float b) {
        return (a + b) / 2;
    }


}
