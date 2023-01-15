import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            runModels();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void runModels() throws IOException {
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

        /* Greedy */
        model = new GreedyModel(fileName);
        model.run();
        total = model.getTotalCost();
        System.out.printf("E_G = %d micro J = %.2f J\n",
                total, total * Math.pow(10, -6)
        );
    }
}
