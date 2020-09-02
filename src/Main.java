import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String filename;
        filename = args[0];
        Scanner input = newScanner(filename);
        Database database = new Database(input);
        Optimistic optimistic = new Optimistic(database);
        optimistic.run();
        input = newScanner(filename);
        database = new Database(input);
        Banker banker = new Banker(database);
        banker.run();
        Output output = new Output();
        System.out.println(banker.getError());
        output.print(optimistic.getTasks(), banker.getTasks());
    }



    private static Scanner newScanner(String fileName) {
        try {
            Scanner input = new Scanner(new BufferedReader(new FileReader(fileName)));
            return input;
        } catch (Exception ex) {
            System.out.printf("Error reading %s\n", fileName);
            System.exit(0);
        }
        return null;
    }



}
