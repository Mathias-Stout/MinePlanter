import java.util.Optional;

public interface MinePlanter {

    public Sign[][] plant(int rows, int cols, Sign[][] field);

    public static String toString(Sign[][] field) {
        StringBuilder builder = new StringBuilder();
        for(int r = 0; r < field.length; r++) {
            for(int c = 0; c < field[r].length; c++) {
                builder.append(
                    Optional.ofNullable(field[r][c])
                               .map(Sign::toChar)
                               .orElse(' ')
                );
            }
            builder.append('\n');
        }
        return builder.toString();
    }

}
