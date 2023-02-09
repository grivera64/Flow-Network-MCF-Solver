public class StorageNode extends SensorNode {
    private int usedSpace;
    private final int capacity;

    public StorageNode(int id, int capacity) {
        super(id);
        this.usedSpace = 0;
        this.capacity = capacity;
    }

    public int getUsedSpace() {
        return this.usedSpace;
    }

    public boolean isFull() {
        return this.usedSpace >= this.capacity;
    }

    public boolean hasSpace() {
        return this.usedSpace < this.capacity;
    }

    public void addPackets(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Cannot add non-positive counts of packets");
        }

        int newValue = this.usedSpace + count;
        if (this.isFull() || newValue > this.capacity) {
            throw new IllegalArgumentException("Packet count exceeds the capacity of this storage node!");
        }
        this.usedSpace = newValue;
    }
}
