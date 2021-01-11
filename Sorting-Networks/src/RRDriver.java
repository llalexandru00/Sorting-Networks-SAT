import java.util.Random;

public class RRDriver
{
    private static final int N = 10, TRIES = 1000000;
    private static final Random rnd = new Random();

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

    private static int[] getRandomConn(PartialOrder order)
    {
        Object[] s = order.getMissing().toArray();
        int i = Math.abs(rnd.nextInt()) % s.length;
        return (int[]) s[i];
    }

    private static int resolve()
    {
        PartialOrder order = new PartialOrder(N);
        int comps = greenFilter(order);
        while(!order.isComplete(N))
        {
            int[] conn = getRandomConn(order);
            if (order.add(conn[0], conn[1]))
            {
                comps++;
            }
        }

        return comps;
    }

    public static void main(String[] args)
    {
        int minim = (int) 1e9;
        int ans = 0;
        for (int i = 0; i < TRIES; i++)
        {
            ans = resolve();
            if (ans < minim)
            {
                minim = ans;
                System.out.println(ans);
            }

        }
    }
}
