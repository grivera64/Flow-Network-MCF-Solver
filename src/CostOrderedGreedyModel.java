import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class CostOrderedGreedyModel implements Model {
    private final FlowNetwork fn;
    private boolean done = false;

    public CostOrderedGreedyModel(String fileName) throws IOException {
        this.fn = new FlowNetwork(fileName);
    }

    @Override
    public void run() {
        int[] ids;
        while (this.fn.hasOverflowPackets()) {
            ids = this.getMinCostId();
            while (this.fn.canAddFlowTo(ids[0], ids[1])) {
                this.fn.addFlowTo(ids[0], ids[1]);
            }
        }
        this.done = true;
    }

    private int[] getMinCostId() {
        List<Integer> options;

        int minFromId = 1;
        int minToId = -1;
        int minCost = Integer.MAX_VALUE;

        int currCost;
        int currMinCost = minCost;
        int currMinToId = minToId;
        /* Go through all data nodes */
        for (int dnId : this.fn.getDataNodeIds()) {
            /* Find the candidates that this node can send flow to */
            options = this.fn.getConnectedNodes(dnId)
                    .stream()
                    .filter(i -> this.fn.canAddFlowTo(dnId, i))
                    .collect(Collectors.toList());

            if (options.isEmpty()) {
                continue;
            }

            for (int option : options) {
                currCost = this.fn.getArcCostPerPacket(dnId, option);
                if (currCost < minCost) {
                    currMinCost = currCost;
                    currMinToId = option;
                }
            }
            if (currMinCost < minCost) {
                minCost = currMinCost;
                minFromId = dnId;
                minToId = currMinToId;
            }
        }

        return new int[]{minFromId, minToId};
    }

    @Override
    public int getTotalCost() {
        if (!done) {
            throw new IllegalStateException("Model was not run yet!");
        }
        return this.fn.getTotalCost();
    }
}
