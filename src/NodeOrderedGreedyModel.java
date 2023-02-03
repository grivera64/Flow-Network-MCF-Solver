import java.io.IOException;
import java.util.List;

public class NodeOrderedGreedyModel implements Model {
    private final FlowNetwork fn;
    private boolean done = false;

    public NodeOrderedGreedyModel(String fileName) throws IOException {
        this.fn = new FlowNetwork(fileName);
    }

    @Override
    public void run() {
        List<Integer> options;
        int minId;
        int minCost;
        int currCost;
        while (this.fn.hasOverflowPackets()) {
            for (int dnId : this.fn.getDataNodeIds()) {
                options = this.fn.getConnectedNodes(dnId);
                minId = Integer.MAX_VALUE;
                minCost = Integer.MAX_VALUE;
                for (int option : options) {
                    currCost = this.fn.getArcCostPerPacket(dnId, option);
                    if (currCost < minCost && !this.fn.isArcExhausted(dnId, option)) {
                        minCost = currCost;
                        minId = option;
                    }
                }
                this.fn.addFlowTo(dnId, minId);
            }
        }
        this.done = true;
    }

    @Override
    public int getTotalCost() {
        if (!done) {
            throw new IllegalStateException("Model was not run yet!");
        }
        return this.fn.getTotalCost();
    }
}
