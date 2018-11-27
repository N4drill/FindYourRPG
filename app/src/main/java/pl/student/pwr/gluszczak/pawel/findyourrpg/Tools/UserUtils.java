package pl.student.pwr.gluszczak.pawel.findyourrpg.Tools;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.User;

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


    public static float calculateUserAverageAsGM(User user) {
        float a = user.getMasterCreativity();
        float b = user.getMasterBehaviour();
        float c = user.getMasterGameFeel();

        float result = (a + b + c) / 3;

        return result;
    }

    public static float calculateUserAverageAsPlayer(User user) {
        float a = user.getPlayerCreativity();
        float b = user.getPlayerBehaviour();
        float c = user.getPlayerGameFeel();

        float result = (a + b + c) / 3;

        return result;
    }

    public static float calculateUserAverage(User user) {
        float a = user.getMasterCreativity();
        float b = user.getMasterBehaviour();
        float c = user.getMasterGameFeel();
        float a2 = user.getPlayerCreativity();
        float b2 = user.getPlayerBehaviour();
        float c2 = user.getPlayerGameFeel();

        float result = (((a + b + c) / 3) + ((a2 + b2 + c2) / 3)) / 2;

        return result;
    }

    public static int calculateUserGames(User user) {
        return user.getGamesAsMaster() + user.getGamesAsPlayer();
    }

}
