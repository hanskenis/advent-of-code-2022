import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day10 {
    List<Integer> values;

    public Day10(List<String> input) {
        values = new ArrayList<Integer>();
        var x = 1;
        values.add(x);
        for (String line : input) {
            if (line.equals("noop")) {
                values.add(x);
            } else {
                var value = Integer.parseInt(line.replace("addx ", ""));
                values.add(x);
                x += value;
                values.add(x);
            }
        }

    }

    private int part1() {
        var signalStrength = 0;
        for (int i = 20; i < values.size(); i = i + 40) {
            signalStrength += i * values.get(i - 1);

        }
        return signalStrength;
    }

    private int part2() {
        for (int i = 0; i < 6; i++) {
            for (int j = 1; j < 41; j++) {
                var cycle = (i * 40) + j;
                var value = values.get(cycle - 1);
                var position = j - 1;
                if (Math.abs(position - value) < 2) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
        return 0;
    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        List<String> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }

        var day = new Day10(data);

        System.out.println("Part 1: " + day.part1());
        System.out.println("Part 2: " + day.part2());

        scanner.close();
    }
}