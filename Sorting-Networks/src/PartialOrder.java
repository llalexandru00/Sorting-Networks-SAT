import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

public class PartialOrder {

    int n;
    private final Set<Integer>[] adj;
    private final Set<Integer>[] reverse;

    private final BitSet mins;
    private final BitSet maxs;
    int low, high;

    public PartialOrder(int n)
    {
        this.n = n;

        this.adj =  new Set[n+1];
        for (int i=1; i<=n; i++)
            this.adj[i] = new HashSet<>();

        this.reverse =  new Set[n+1];
        for (int i=1; i<=n; i++)
            this.reverse[i] = new HashSet<>();

        mins = new BitSet();
        maxs = new BitSet();

        low = 1;
        high = n;
        reset(mins);
        reset(maxs);
    }

    public PartialOrder(PartialOrder poset)
    {
        this.n = poset.n;

        this.adj =  new Set[n+1];
        for (int i=1; i<=n; i++)
            this.adj[i] = new HashSet<>();

        this.reverse =  new Set[n+1];
        for (int i=1; i<=n; i++)
            this.reverse[i] = new HashSet<>();

        if (poset.adj != null)
        {
            for (int i = 1; i <= n; i++)
                this.adj[i].addAll(poset.adj[i]);
        }

        if (poset.reverse != null)
        {
            for (int i = 1; i <= n; i++)
                this.reverse[i].addAll(poset.reverse[i]);
        }

        this.mins = (BitSet) poset.mins.clone();
        this.maxs = (BitSet) poset.maxs.clone();
        this.low = poset.low;
        this.high = poset.high;
    }

    private void reset(BitSet s)
    {
        s.clear();
        for (int i=low; i<=high; i++)
            s.set(i);
    }

    public boolean add(int x, int y)
    {
        int cx = x, cy = y;
        x = Math.min(cx, cy);
        y = Math.max(cx, cy);
        if (adj[x].contains(y))
            return false;

        Set<Integer> pre = new HashSet<>(reverse[x]);
        Set<Integer> post = new HashSet<>(adj[y]);

        for (Integer i : reverse[x])
        {
            adj[i].remove(x);
        }

        for (Integer i : adj[y])
        {
            reverse[i].remove(y);
        }

        adj[y].clear();
        reverse[x].clear();

        for (Integer i : pre)
        {
            if (reverse[y].contains(i))
            {
                adj[i].add(x);
                reverse[x].add(i);
            }
            else if (move(i, x, i, y))
            {
                // minmax(i, y);
            }
        }

        for (Integer i : post)
        {
            if (adj[x].contains(i))
            {
                adj[y].add(i);
                reverse[i].add(y);
            }
            else if (move(y, i, x, i))
            {
                // minmax(x, i);
            }
        }

        adj[x].add(y);
        reverse[y].add(x);
        minmax(x, y);

        return true;
    }

    private void minmax(int x, int y)
    {
        if (mins.get(y))
        {
            mins.set(x);
        }

        mins.clear(y);

        if (maxs.get(x))
        {
            maxs.set(y);
        }

        maxs.clear(x);

        if (mins.cardinality() == 1)
        {
            low++;
            reset(mins);
            for (int i = low; i<=high; i++)
            {
                add(low-1, i);
            }
        }

        if (maxs.cardinality() == 1)
        {
            high--;
            reset(maxs);
            for (int i = low; i<=high; i++)
            {
                add(i, high+1);
            }
        }
    }

    private boolean move(int x1, int y1, int x2, int y2)
    {
        adj[x1].remove(y1);
        reverse[y1].remove(x1);

        if (adj[x2].contains(y2))
            return false;

        adj[x2].add(y2);
        reverse[y2].add(x2);

        return true;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        for (int i=1; i<=n; i++)
        {
            for (Integer v : adj[i])
            {
                sb.append("(").append(i).append(", ").append(v).append("), ");
            }
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }

    public int size()
    {
        int tot = 0;
        for (int i=1; i<=n; i++)
        {
            tot += adj[i].size();
        }
        return tot;
    }

    public Set<int[]> getMissing() {
        Set<int[]> set = new HashSet<>();

        for (int i=1; i<=n; i++)
        {
            for (int j = i+1; j<=n; j++)
            {
                if (!adj[i].contains(j))
                {
                    set.add(new int[]{i, j});
                }
            }
        }

        return set;
    }

    public String getMissingString() {
        StringBuilder sb = new StringBuilder();
        Set<int[]> set = getMissing();
        for (int[] i : set)
        {
            sb.append("(").append(i[0]).append(", ").append(i[1]).append("), ");
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }

    public int[] getLowestDegreeEdge() {
        int whox = 0, whoy = 0, maxim = -1;
        for (int[] edge : getMissing())
        {
            int x = edge[0];
            int y = edge[1];

            if (adj[x].size() + adj[y].size() > maxim)
            {
                maxim = adj[x].size() + adj[y].size();
                whox = x;
                whoy = y;
            }
        }
        assert whox != 0;
        return new int[] {whox, whoy};
    }

    public boolean isComplete(int n) {
        return size() == n * (n - 1) / 2;
    }
}
