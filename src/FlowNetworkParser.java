import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class FlowNetworkParser implements Closeable {
    private Scanner scanner;
    private int lineNumber = 1;
    private int pCount = 0;
    private int nCount = 0;
    private int aCount = 0;

    private int sourceId = -1;
    private int sinkId = -1;
    private final List<Arc> arcs = new ArrayList<>();

    public FlowNetworkParser(String name) throws IOException {
        File file = new File(name);
        if (!file.exists()) {
            throw new IllegalArgumentException("Invalid file path!");
        }

        this.scanner = new Scanner(file);
        if (!this.scanner.hasNext()) {
            throw new NoSuchElementException("There is no data in the file!");
        }
    }

    public void parse() {
        int nodesLength = -1;
        int arcsLength = -1;

        String line;
        String[] args;
        while (scanner.hasNext()) {
            line = scanner.nextLine();
            args = line.split(" ");

            switch (args[0]) {
                case "c", "\n", "" -> {
                }
                case "p" -> {
                    int[] tmp = this.parseProblem(args);
                    nodesLength = tmp[0];
                    arcsLength = tmp[1];
                }
                case "n" -> {
                    this.parseNode(args);
                }
                case "a" -> {
                    this.arcs.add(this.parseArc(args, arcsLength));
                }
                default -> throw new Error(
                        String.format("Error on line %d", this.lineNumber)
                );
            }
            this.lineNumber++;
        }
    }

    private int[] parseProblem(String[] args) {
        if (this.pCount != 0 || !args[1].equals("min")) {
            throw new Error(
                    String.format("(P) Error on line %d", this.lineNumber)
            );
        }
        this.pCount++;

        return new int[] {
                Integer.parseInt(args[2]) - 2,  // Number of Nodes
                Integer.parseInt(args[3])       // Arc length
        };
    }

    private void parseNode(String[] args) {
        if (this.nCount > 1 || args.length != 3) {
            throw new Error(
                    String.format("(N) Error on line %d", this.lineNumber)
            );
        }
        if (this.nCount == 0) {
            this.sourceId = Integer.parseInt(args[1]);
        } else {
            this.sinkId = Integer.parseInt(args[1]);
        }
        this.nCount++;
    }

    private Arc parseArc(String[] args, int arcsLength) {
        if (aCount > arcsLength || pCount != 1) {
            throw new Error(
                    String.format("(A) Error on line %d", this.lineNumber)
            );
        }
        aCount++;
        return new Arc(
                Integer.parseInt(args[1]),      // From
                Integer.parseInt(args[2]),      // To
                Integer.parseInt(args[3]),      // Min Flow
                Integer.parseInt(args[4]),      // Max Flow
                Integer.parseInt(args[5])       // Max Cost
        );
    }

    public int getSourceId() {
        return this.sourceId;
    }

    public int getSinkId() {
        return this.sinkId;
    }

    public List<Arc> getArcs() {
        return this.arcs;
    }

    @Override
    public void close() throws IOException {
        this.scanner.close();
    }
}
