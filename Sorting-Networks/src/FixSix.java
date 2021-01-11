import java.util.ArrayList;
import java.util.List;

public class FixSix implements Fixed
{

    private static final int N = 6;
    private static final List<int[]> COMPARATORS = new ArrayList<>();

    static {
        COMPARATORS.add(new int[]{1, 2});
        COMPARATORS.add(new int[]{3, 4});
        COMPARATORS.add(new int[]{5, 6});
        COMPARATORS.add(new int[]{1, 3});
        COMPARATORS.add(new int[]{2, 4});
        COMPARATORS.add(new int[]{1, 5});
        COMPARATORS.add(new int[]{2, 6});
        COMPARATORS.add(new int[]{3, 5});
        COMPARATORS.add(new int[]{4, 6});
        COMPARATORS.add(new int[]{4, 5});
        COMPARATORS.add(new int[]{2, 4});
        COMPARATORS.add(new int[]{2, 3});
    }

    public List<int[]> getComparators()
    {
        return COMPARATORS;
    }

    @Override
    public int getN() {
        return N;
    }
}
