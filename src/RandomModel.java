import java.io.IOException;
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
        int optionIndex = -1;
        while (this.fn.hasOverflowPackets()) {
            for (int dnId : this.fn.getDataNodeIds()) {
                options = this.fn.getConnectedNodes(dnId);

                do {
                    optionIndex = (int) (Math.random() * options.size());
                } while (!this.fn.canAddFlowTo(dnId, options.get(optionIndex)));
                this.fn.addFlowTo(dnId, options.get(optionIndex));
            }
        }
        this.done = true;
    }

    @Override
    public int getTotalCost() {
        return this.fn.getTotalCost();
    }
}
