import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day04 {
    List<String> input;

    public Day04(List<String> input) {
        this.input = input;
    }

    private int part1() {
        var overlapping = 0;
        for (String line : input) {
            var sections = line.split(",");
            var section1 = Arrays.stream(sections[0].split("-")).mapToInt(Integer::parseInt).toArray();
            var section2 = Arrays.stream(sections[1].split("-")).mapToInt(Integer::parseInt).toArray();

            var biggest = section1;
            var smallest = section2;

            if (section1[1] - section1[0] <= section2[1] - section2[0]) {
                smallest = section1;
                biggest = section2;
            }

            if (smallest[0] >= biggest[0] && smallest[1] <= biggest[1]) {
                overlapping++;
            }

        }

        return overlapping;

    }

    private int part2() {
        var overlapping = 0;
        for (String line : input) {
            var sections = line.split(",");
            var section1 = Arrays.stream(sections[0].split("-")).mapToInt(Integer::parseInt).toArray();
            var section2 = Arrays.stream(sections[1].split("-")).mapToInt(Integer::parseInt).toArray();

            var left = section1;
            var right = section2;

            if (section2[0] < section1[0]) {
                left = section2;
                right = section1;
            }

            if (left[0] >= right[0] && left[0] <= right[1]) {
                overlapping++;
            } else if (left[1] >= right[0] && left[1] <= right[1]) {
                overlapping++;
            } else if (left[0] <= right[0] && left[1] >= right[1])
                overlapping++;
        }

        return overlapping;

    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        List<String> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }

        var day = new Day04(data);

        System.out.println("Part 1: " + day.part1());
        System.out.println("Part 2: " + day.part2());

        scanner.close();
    }
}