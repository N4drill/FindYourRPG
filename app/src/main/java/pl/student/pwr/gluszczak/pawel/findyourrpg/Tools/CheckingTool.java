package pl.student.pwr.gluszczak.pawel.findyourrpg.Tools;

public class CheckingTool {

    /**
     * Retruns true if string is empty
     *
     * @param string String to check
     * @return
     */

    public static boolean isEmpty(String string) {
        return string.equals("");
    }


    /**
     * Return true if string are both same
     *
     * @param string1
     * @param string2
     * @return
     */
    public static boolean doesStringMatch(String string1, String string2) {
        return string1.equals(string2);
    }

    /**
     * Return true if input String contains number
     *
     * @param input
     * @return
     */
    public static boolean isStringNumber(String input) {
        try {
            int age = Integer.parseInt(input);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
