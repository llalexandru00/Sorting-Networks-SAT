import java.util.Random;

public class LowerDegreeDriver
{
    private static final int N = 10;

    private static int[] getConn(PartialOrder order, int n)
    {
        return order.getLowestDegreeEdge();
    }

    private static int resolve()
    {
        int comps = 0;
        PartialOrder order = new PartialOrder(N);
        while(!order.isComplete(N))
        {
            int[] conn = getConn(order, N);
            if (order.add(conn[0], conn[1]))
            {
                comps++;
            }
        }

        return comps;
    }

    public static void main(String[] args)
    {
        int ans = resolve();
        System.out.println("" + ans);
    }
}
