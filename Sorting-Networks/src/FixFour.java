import java.util.ArrayList;
import java.util.List;

public class FixFour implements Fixed
{

    private static final int N = 4;
    private static final List<int[]> COMPARATORS = new ArrayList<>();

    static {
        COMPARATORS.add(new int[]{1, 3});
        COMPARATORS.add(new int[]{2, 4});
        COMPARATORS.add(new int[]{1, 2});
        COMPARATORS.add(new int[]{3, 4});
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
