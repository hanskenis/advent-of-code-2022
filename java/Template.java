import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Template {

    public Template(List<String> input) {
        super();
    }

    private int part1() {
        return 0;

    }

    private int part2() {
        return 0;
    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        List<String> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }

        System.out.println("Part 1: " + new Template(data).part1());
        System.out.println("Part 2: " + new Template(data).part2());

        scanner.close();
    }
}