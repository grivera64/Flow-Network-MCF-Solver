import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
//            runRandomVsNodeOrderedGreedy();
            runNodeOrderedGreedyVsCostOrderedGreedy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void runRandomVsNodeOrderedGreedy() throws IOException {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Please enter the .inp file: ");
        String fileName = keyboard.nextLine();

        Model model;

        /* Random */
        long total = 0;
        for (int i = 0; i < 10; i++) {
            model = new RandomModel(fileName);
            model.run();
            total += model.getTotalCost();
        }
        total /= 10;
        System.out.printf("E_R = %d micro J = %.2f J\n",
                total, total * Math.pow(10, -6)
        );

        /* Node-Ordered Greedy */
        model = new NodeOrderedGreedyModel(fileName);
        model.run();
        total = model.getTotalCost();
        System.out.printf("E_G = %d micro J = %.2f J\n",
                total, total * Math.pow(10, -6)
        );
    }

    public static void runNodeOrderedGreedyVsCostOrderedGreedy() throws IOException {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Please enter the .inp file: ");
        String fileName = keyboard.nextLine();

        Model model;
        int total;

        /* Node-Ordered Greedy */
        model = new NodeOrderedGreedyModel(fileName);
        model.run();
        total = model.getTotalCost();
        System.out.printf("E_NG = %d micro J = %.2f J\n",
                total, total * Math.pow(10, -6)
        );

        /* Cost-Ordered Greedy */
        model = new CostOrderedGreedyModel(fileName);
        model.run();
        total = model.getTotalCost();
        System.out.printf("E_CG = %d micro J = %.2f J\n",
                total, total * Math.pow(10, -6)
        );
    }
}
