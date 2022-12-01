import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Day01 {
    List<Integer> elfTotals;

    public Day01(List<String> input) {
        elfTotals = new ArrayList<>();
        var elfTotal = 0;
        for (String line : input) {
            if (line.isBlank()) {
                elfTotals.add(elfTotal);
                elfTotal = 0;
            } else {
                var c = Integer.parseInt(line);
                elfTotal += c;
            }
        }
    }

    private int totalCalories(int nrElves) {
        return elfTotals
                .stream()
                .sorted(Comparator.reverseOrder())
                .limit(nrElves)
                .mapToInt(Integer::intValue)
                .sum();
    }

    private int part1() {
        return totalCalories(1);
    }

    private int part2() {
        return totalCalories(3);
    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        List<String> input = new ArrayList<>();
        while (scanner.hasNextLine()) {
            input.add(scanner.nextLine());
        }

        var day = new Day01(input);

        System.out.println("Part 1: " + day.part1());
        System.out.println("Part 2: " + day.part2());

        scanner.close();
    }
}