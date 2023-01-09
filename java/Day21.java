import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day21 {
    enum Operator {
        add, multiply, divide, subtract
    }

    class Monkey {
        String name;
        Long value;
        Operator operator;
        String left;
        String right;

        Monkey(String name, Long value) {
            this.name = name;
            this.value = value;
        }

        Monkey(String name, Operator operator, String left, String right) {
            this.name = name;
            this.operator = operator;
            this.left = left;
            this.right = right;
        }

        long eval(boolean throwHumanException) {
            if (name.equals("humn") && throwHumanException) {
                throw new RuntimeException("Human");
            }
            if (value != null) {
                return value;
            } else {
                return operate(throwHumanException);
            }
        }

        long part2() {
            Long leftValue = null, rightValue = null;

            try {
                leftValue = monkeys.get(left).eval(true);
            } catch (Exception e) {
            }

            try {
                rightValue = monkeys.get(right).eval(true);
            } catch (Exception e) {
            }

            if (leftValue == null) {
                return monkeys.get(left).searchHumanValue(rightValue);
            } else {
                return monkeys.get(right).searchHumanValue(leftValue);
            }
        }

        long searchHumanValue(long nr) {
            Long leftValue = null, rightValue = null;

            if (name.equals("humn")) {
                return nr;
            }

            try {
                leftValue = monkeys.get(left).eval(true);
            } catch (Exception e) {
            }

            try {
                rightValue = monkeys.get(right).eval(true);
            } catch (Exception e) {
            }

            if (leftValue == null) {
                switch (operator) {
                    case add:
                        return monkeys.get(left).searchHumanValue(nr - rightValue);
                    case multiply:
                        return monkeys.get(left).searchHumanValue(nr / rightValue);
                    case divide:
                        return monkeys.get(left).searchHumanValue(nr * rightValue);
                    case subtract:
                        return monkeys.get(left).searchHumanValue(nr + rightValue);
                    default:
                        return 0;
                }
            } else {
                switch (operator) {
                    case add:
                        return monkeys.get(right).searchHumanValue(nr - leftValue);
                    case multiply:
                        return monkeys.get(right).searchHumanValue(nr / leftValue);
                    case divide:
                        return monkeys.get(right).searchHumanValue(nr * leftValue);
                    case subtract:
                        return monkeys.get(right).searchHumanValue(leftValue - nr);
                    default:
                        return 0;
                }
            }
        }

        long operate(boolean throwHumanException) {
            var mLeft = monkeys.get(left).eval(throwHumanException);
            var mRight = monkeys.get(right).eval(throwHumanException);

            switch (operator) {
                case add:
                    value = mLeft + mRight;
                    return value;
                case multiply:
                    value = mLeft * mRight;
                    return value;
                case divide:
                    value = mLeft / mRight;
                    return value;
                case subtract:
                    value = mLeft - mRight;
                    return value;
                default:
                    return 0;
            }
        }
    }

    Map<String, Monkey> monkeys = new HashMap<>();

    public Day21(List<String> input) {
        for (var line : input) {
            var splitted = line.split(" ");
            var name = splitted[0].substring(0, splitted[0].length() - 1);
            if (splitted.length == 2) {
                var value = Long.parseLong(splitted[1]);
                monkeys.put(name, new Monkey(name, value));
            } else {
                var left = splitted[1];
                var right = splitted[3];
                Operator operator = null;
                switch (splitted[2]) {
                    case "+":
                        operator = Operator.add;
                        break;
                    case "*":
                        operator = Operator.multiply;
                        break;
                    case "/":
                        operator = Operator.divide;
                        break;
                    case "-":
                        operator = Operator.subtract;
                        break;
                }
                var monkey = new Monkey(name, operator, left, right);
                monkeys.put(name, monkey);
            }
        }
    }

    private long part1() {
        var root = monkeys.get("root");

        return root.eval(false);

    }

    private long part2() {
        var root = monkeys.get("root");
        return root.part2();
    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        List<String> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }

        System.out.println("Part 1: " + new Day21(data).part1());
        System.out.println("Part 2: " + new Day21(data).part2());

        scanner.close();
    }
}