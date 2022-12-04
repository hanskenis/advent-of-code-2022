import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day03 {
    List<String> input;

    public Day03(List<String> input) {
        this.input = input;
    }

    private int priority(char item) {
        if (Character.isUpperCase(item)) {
            return ((int) item - 'A') + 27;
        } else {

            return ((int) item - 'a') + 1;
        }
    }

    private String commonTypes(String string1, String string2) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < string1.length(); i++) {
            var c = string1.charAt(i);
            for (int j = 0; j < string2.length(); j++) {
                if (string2.charAt(j) == c) {
                    builder.append(c);
                }
            }
        }
        return builder.toString();
    }

    private char commonType(String line) {
        return commonTypes(line.substring(0, line.length() / 2), line.substring(line.length() / 2, line.length()))
                .charAt(0);
    }

    private int part1() {
        return input.stream().map((x) -> priority(commonType(x))).mapToInt(Integer::intValue).sum();
    }

    private int part2() {
        var total = 0;
        var strings = new String[3];
        for (int i = 0; i < input.size(); i++) {
            strings[i % 3] = input.get(i);
            if (i % 3 == 2 && i != 0) {
                var common = commonTypes(strings[0], strings[1]);
                common = commonTypes(common, strings[2]);
                total += priority(common.charAt(0));
            }
        }

        return total;
    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        List<String> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }

        var day = new Day03(data);

        System.out.println("Part 1: " + day.part1());
        System.out.println("Part 2: " + day.part2());

        scanner.close();
    }
}