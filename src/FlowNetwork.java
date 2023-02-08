import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FlowNetwork {
    private final Map<Integer, SensorNode> nodeMap = new HashMap<>();
    private final Map<Integer, List<Arc>> arcMap = new HashMap<>();
    public FlowNetwork(String fileName) throws IOException {
        this.setup(fileName);
    }

    private void setup(String fileName) throws IOException {
        try (FlowNetworkParser parser = new FlowNetworkParser(fileName)) {
            parser.parse();

            int from;
            int to;
            for (Arc currArc : parser.getArcs()) {
                from = currArc.getTailId();
                to = currArc.getHeadId();
                if (from == parser.getSourceId()) {
                    this.nodeMap.put(
                            to,
                            new DataNode(currArc.getHeadId(), currArc.getMaxFlow())
                    );
                } else if (to == parser.getSinkId()) {
                    this.nodeMap.put(
                            from,
                            new StorageNode(currArc.getTailId(), currArc.getMaxFlow())
                    );
                } else {
                    this.arcMap.putIfAbsent(from, new ArrayList<>());
                    this.arcMap.get(from).add(currArc);
                }
            }
        }
    }

    public int getMinFlow(int fromId, int toId) {
        Arc arc = this.getArc(fromId, toId);
        return arc.getMinFlow();
    }

    public int getFlow(int fromId, int toId) {
        Arc arc = this.getArc(fromId, toId);
        return arc.getFlow();
    }

    public int getMaxFlow(int fromId, int toId) {
        Arc arc = this.getArc(fromId, toId);
        return arc.getMaxFlow();
    }

    public int getArcCostPerPacket(int fromId, int toId) {
        return this.getArc(fromId, toId).getCost();
    }

    public List<Integer> getDataNodeIds() {
        return this.getDataNodes().stream()
                .map(SensorNode::getId)
                .collect(Collectors.toList());
    }

    public boolean hasOverflowPackets() {
        for (DataNode dn : this.getDataNodes()) {
            if (!dn.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private List<DataNode> getDataNodes() {
        return this.nodeMap.values().stream()
                .filter(n -> n instanceof DataNode)
                .map(n -> (DataNode) n)
                .collect(Collectors.toList());
    }

    public boolean hasSpaceLeft() {
        for (StorageNode sn : this.getStorageNodes()) {
            if (!sn.isFull()) {
                return true;
            }
        }
        return false;
    }

    private List<StorageNode> getStorageNodes() {
        return this.nodeMap.values().stream()
                .filter(n -> n instanceof StorageNode)
                .map(n -> (StorageNode) n)
                .collect(Collectors.toList());
    }

    public boolean isArcExhausted(int dnId, int snId) {
        return this.getArc(dnId, snId)
                .isExhausted() || this.getStorageNode(snId).isFull() || this.getDataNode(dnId).isEmpty();
    }

    public List<Integer> getConnectedNodes(int dnId) {
        return this.getArcsFrom(dnId).stream()
                .map(Arc::getHeadId)
                .collect(Collectors.toList());
    }

    public void addFlowTo(int dnId, int snId) {
        DataNode dn = this.getDataNode(dnId);
        StorageNode sn = this.getStorageNode(snId);
        Arc arc = this.getArc(dnId, snId);

        if (dn.isEmpty()) {
            throw new IllegalArgumentException(String.format("Data node %d is empty!", dnId));
        }

        if (sn.isFull()) {
            throw new IllegalArgumentException(String.format("Storage node %d is full!", snId));
        }

        dn.removePackets(1);
        sn.addPackets(1);
        arc.addFlow(1);
    }

    private List<Arc> getArcsFrom(int fromId) {
        return Collections.unmodifiableList(this.arcMap.get(fromId));
    }

    private Arc getArc(int fromId, int toId) {
        List<Arc> arcs = this.getArcsFrom(fromId);
        for (Arc arc : arcs) {
            if (arc.getHeadId() == toId) {
                return arc;
            }
        }
        throw new NoSuchElementException(String.format("No arc exists connecting nodes %d and %d!", fromId, toId));
    }

    private DataNode getDataNode(int id) {
        SensorNode n = this.nodeMap.get(id);
        if (!(n instanceof DataNode dn)) {
            throw new NoSuchElementException(String.format("No Data Node with id %d exists!", id));
        }
        return dn;
    }

    private StorageNode getStorageNode(int id) {
        SensorNode n = this.nodeMap.get(id);
        if (!(n instanceof StorageNode sn)) {
            throw new NoSuchElementException(String.format("No Storage Node with id %d exists!", id));
        }
        return sn;
    }

    public int getTotalCost() {
        if (this.hasOverflowPackets()) {
            return -1;
        }

        int cost = 0;
        for (int dnId : this.getDataNodeIds()) {
            for (Arc arc : this.getArcsFrom(dnId)) {
                cost += arc.getCost() * arc.getFlow();
            }
        }
        return cost;
    }

//    public String getArcDebug(int dnId, int snId) {
//        Arc arc = this.getArc(dnId, snId);
//        DataNode dn = this.getDataNode(dnId);
//        StorageNode sn = this.getStorageNode(snId);
//        return String.format("Arc exhausted: %b\nDN %d empty: %b\nSN %d full: %b\n",
//                arc.isExhausted(), dnId, dn.isEmpty(), snId, sn.isFull()
//        );
//    }
}
