package pl.student.pwr.gluszczak.pawel.findyourrpg.Tools;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class TextFormat {

    public static String precisionStringFromDouble(double value, int leftAfterComma) {
        Double toCut = BigDecimal.valueOf(value)
                .setScale(leftAfterComma, RoundingMode.HALF_UP)
                .doubleValue();

        return String.valueOf(toCut);

    }
}
