public class Arc implements Comparable<Arc> {
    private int tailId;
    private int headId;
    private int minFlow;
    private int flow;
    private int maxFlow;
    private int cost;

    public Arc(int tailId, int headId, int minFlow, int maxFlow, int cost) {
        this.tailId = tailId;
        this.headId = headId;
        this.minFlow = minFlow;
        this.flow = minFlow;
        this.maxFlow = maxFlow;
        this.cost = cost;
    }

    public int getTailId() {
        return this.tailId;
    }

    public int getHeadId() {
        return this.headId;
    }

    public int getMinFlow() {
        return this.minFlow;
    }

    public int getFlow() {
        return this.flow;
    }

    public int getMaxFlow() {
        return this.maxFlow;
    }

    public int getCost() {
        return this.cost;
    }

    public boolean isExhausted() {
        return this.flow >= this.maxFlow;
    }

    public void addFlow(int count) {
        int newFlow = this.flow + count;

        if (newFlow > this.maxFlow) {
            throw new IllegalArgumentException("Cannot overflow arc!");
        } else if (newFlow < this.minFlow) {
            throw new IllegalArgumentException("Cannot underflow arc!");
        }

        this.flow = newFlow;
    }

    @Override
    public int compareTo(Arc o) {
        if (this.tailId != o.tailId) {
            return o.tailId - this.tailId;
        }
        return o.headId - this.headId;
    }
}
