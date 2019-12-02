package cern.accsoft.steering.jmad.domain.result.tfs;

public final class TfsDoubles {
    private static final String POSITIVE_INFINITY = "inf";
    private static final String NEGATIVE_INFINITY = "-inf";

    private TfsDoubles() {
        throw new UnsupportedOperationException("static only");
    }

    public static double parseTfsDouble(String value) {
        if (value.equalsIgnoreCase(POSITIVE_INFINITY)) {
            return Double.POSITIVE_INFINITY;
        } else if (value.equalsIgnoreCase(NEGATIVE_INFINITY)) {
            return Double.NEGATIVE_INFINITY;
        }
        return Double.parseDouble(value);
    }
}
