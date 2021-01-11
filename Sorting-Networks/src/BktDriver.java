import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BktDriver
{
    private static final int N = 7;
    private static int minim = (int) 1e9;

    private static void bkt(PartialOrder po, int steps)
    {
        if (steps >= minim)
            return;

        if (po.isComplete(N))
        {
            if (steps < minim)
            {
                minim = steps;
                System.out.println(minim);
            }
            return;
        }

        PartialOrder cpy = new PartialOrder(po);
        List<Object> intList = Arrays.asList(po.getMissing().toArray());
        Collections.shuffle(intList);
        for (Object obj : intList)
        {
            int[] edge = (int[]) obj;
            if (po.add(edge[0], edge[1]))
            {
                bkt(po, steps + 1);

                po = cpy;
                cpy = new PartialOrder(cpy);
            }
        }
    }

    private static int greenFilter(PartialOrder order)
    {
        int lng = 1;
        int step = 1;
        int cnt = 0;
        while (lng < N)
        {
            for (int k = 1; k <= lng; k++)
            {
                int i = k;
                while (i <= N - lng)
                {
                    order.add(i + lng - 1, step);
                    cnt++;
                    step += 1;
                    i += 2 * lng;
                    lng *= 2;
                }
            }
        }
        return cnt;
    }

    public static void main(String[] args)
    {
        PartialOrder po = new PartialOrder(N);
        int steps = greenFilter(po);
        bkt(po, steps);
        System.out.println(minim);
    }
}
