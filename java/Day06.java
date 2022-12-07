import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day06 {
    String input;

    public Day06(List<String> input) {
        this.input = input.get(0);
    }

    private int start(int nr) {
        StringBuilder builder = new StringBuilder();
        builder.append(input.substring(0, nr));
        for (int i = nr; i < input.length(); i++) {
            var set = builder.toString().chars().boxed().collect(Collectors.toSet());
            if (set.size() == nr)
                return i;

            builder.deleteCharAt(0);
            builder.append(Character.toString(input.charAt(i)));
        }
        return 0;

    }

    private int part1() {
        return start(4);

    }

    private int part2() {
        return start(14);
    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        List<String> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }

        var day = new Day06(data);

        System.out.println("Part 1: " + day.part1());
        System.out.println("Part 2: " + day.part2());

        scanner.close();
    }
}