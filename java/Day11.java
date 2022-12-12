import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day11 {
    enum MonkeyOperator {
        ADD, MULTIPLY
    }

    class Monkey {
        List<Long> items;
        MonkeyOperator operator;
        Long operand;
        String Test;
        long testValue;
        int trueIndex;
        int falseIndex;
        long inspections;
        boolean withRelief;

        public Monkey(List<String> input, boolean withRelief) {
            this.withRelief = withRelief;
            var line2 = input.get(1);
            items = Arrays.asList(line2.replace("Starting items: ", "").replace(" ", "").split(",")).stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            var line3 = input.get(2);
            line3 = line3.replace("  Operation: new = old ", "");
            if (line3.charAt(0) == '+') {
                operator = MonkeyOperator.ADD;
            } else {
                operator = MonkeyOperator.MULTIPLY;
            }
            line3 = line3.substring(2);
            if (!line3.equals("old")) {
                operand = Long.parseLong(line3);
            }
            testValue = Long.parseLong(input.get(3).replace("  Test: divisible by ", ""));
            trueIndex = Integer.parseInt(input.get(4).replace("    If true: throw to monkey ", ""));
            falseIndex = Integer.parseInt(input.get(5).replace("    If false: throw to monkey ", ""));

        }

        void takeTurn(List<Monkey> monkeys, long m) {
            inspections += items.size();
            worry();
            if (withRelief) {
                bored();
            }
            items = items.stream().map(i -> i % m).collect(Collectors.toList());

            for (var item : items) {
                var rest = item % testValue;
                if (rest == 0) {
                    monkeys.get(trueIndex).items.add(item);
                } else {
                    monkeys.get(falseIndex).items.add(item);
                }
            }
            items.clear();

        }

        void worry() {
            if (operator == MonkeyOperator.ADD) {
                if (operand == null) {
                    items = items.stream().map(i -> i + i).collect(Collectors.toList());
                } else {
                    items = items.stream().map(i -> i + operand).collect(Collectors.toList());
                }
            } else {
                if (operand == null) {
                    items = items.stream().map(i -> i * i).collect(Collectors.toList());
                } else {
                    items = items.stream().map(i -> i * operand).collect(Collectors.toList());
                }
            }
        }

        void bored() {
            items = items.stream().map(i -> i / 3).collect(Collectors.toList());
        }

        public long getInspections() {
            return inspections;
        }

    }

    List<Monkey> monkeysPart1 = new ArrayList<>();
    List<Monkey> monkeysPart2 = new ArrayList<>();

    public Day11(List<String> input) {
        while (!input.isEmpty()) {
            var lines = input.subList(0, 6);
            var monkey1 = new Monkey(lines, true);
            var monkey2 = new Monkey(lines, false);
            monkeysPart1.add(monkey1);
            monkeysPart2.add(monkey2);
            lines.clear();
            if (!input.isEmpty()) {
                input.remove(0);
            }
        }

    }

    private long part1() {
        var rounds = 20;
        for (int i = 0; i < rounds; i++) {
            for (Monkey monkey : monkeysPart1) {
                monkey.takeTurn(monkeysPart1, 1);
            }
        }

        long value = monkeysPart1.stream().mapToLong(Monkey::getInspections).boxed().sorted(Collections.reverseOrder())
                .limit(2).reduce(1L,
                        (a, b) -> a * b);

        return value;

    }

    private long part2() {
        var m = monkeysPart2.stream().mapToLong(monkey -> monkey.testValue).reduce(1L, (a, b) -> a * b);
        var rounds = 10000;
        for (int i = 0; i < rounds; i++) {
            for (Monkey monkey : monkeysPart2) {
                monkey.takeTurn(monkeysPart2, m);
            }
        }

        long value = monkeysPart2.stream().mapToLong(Monkey::getInspections).boxed().sorted(Collections.reverseOrder())
                .limit(2).reduce(1L,
                        (a, b) -> a * b);

        return value;
    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        List<String> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }

        var day = new Day11(data);

        System.out.println("Part 1: " + day.part1());
        System.out.println("Part 2: " + day.part2());

        scanner.close();
    }
}