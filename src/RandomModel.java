import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class RandomModel implements Model {
    private final FlowNetwork fn;
    private boolean done = false;

    public RandomModel(String fileName) throws IOException {
        this.fn = new FlowNetwork(fileName);
    }

    @Override
    public void run() {
        List<Integer> options;
        while (this.fn.hasOverflowPackets()) {
            for (int dnId : this.fn.getDataNodeIds()) {
                options = this.fn.getConnectedNodes(dnId);
                Collections.shuffle(options);
                while (this.fn.isArcExhausted(dnId, options.get(0))) {
                    Collections.shuffle(options);
                }
                this.fn.addFlowTo(dnId, options.get(0));
            }
        }
        this.done = true;
    }

    @Override
    public int getTotalCost() {
        return this.fn.getTotalCost();
    }
}
