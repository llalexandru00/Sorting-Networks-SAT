
public class MainDriver
{
    private static final Fixed provider = new FixFour();
    private static final PartialOrder order = new PartialOrder(provider.getN());

    private static void addComp(int x, int y)
    {
        if (!order.add(x, y))
        {
            throw new RuntimeException("Useless comparator!");
        }
    }

    public static void main(String[] args)
    {
        int idx = 0;
        for (int[] comparator : provider.getComparators())
        {
            int x = comparator[0];
            int y = comparator[1];
            idx++;

            addComp(x, y);
            System.out.println("Step " + idx + ": " + order.toString());
        }

        if (order.isComplete(provider.getN()))
        {
            System.out.println("Solved");
        }
        else
        {
            System.out.println("New comparators must be added (upperbound): " + (provider.getN() * (provider.getN() - 1) / 2 - order.size()));
            System.out.println("Missing comparators: " + order.getMissingString());
        }
    }
}
