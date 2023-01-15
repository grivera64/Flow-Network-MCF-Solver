public class DataNode extends SensorNode {
    private int overflowPackets;

    public DataNode(int id, int overflowPackets) {
        super(id);
        this.overflowPackets = overflowPackets;
    }

    public int getOverflowPackets() {
        return this.overflowPackets;
    }

    public boolean isEmpty() {
        return this.overflowPackets <= 0;
    }

    public void removePackets(int count) {
        if (this.overflowPackets - count < 0) {
            throw new IllegalStateException("Not enough overflow packets!");
        }

        this.overflowPackets--;
    }
}
