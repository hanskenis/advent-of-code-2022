import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Day05 {
    static int NR_STACKS = 10;

    List<ArrayDeque<String>> part1stacks = new ArrayList<ArrayDeque<String>>(NR_STACKS);
    List<ArrayDeque<String>> part2stacks = new ArrayList<ArrayDeque<String>>(NR_STACKS);
    List<int[]> moves = new ArrayList<>();

    public Day05(List<String> input) {

        var lists = new ArrayList<ArrayList<String>>(NR_STACKS);

        for (int i = 0; i < NR_STACKS; i++) {
            lists.add(i, new ArrayList<String>());
        }

        var index = 0;
        var line = input.get(index);
        while (line.charAt(1) != '1') {
            for (int i = 0; i < NR_STACKS; i++) {
                if (i * 4 < line.length() && line.charAt(i * 4) == '[') {
                    var sub = line.substring(i * 4, i * 4 + 2);
                    sub = sub.replace("[", "").replace("]", "");
                    lists.get(i).add(sub);
                }
            }
            index++;
            line = input.get(index);
        }

        for (int i = 0; i < NR_STACKS; i++) {
            part1stacks.add(i, new ArrayDeque<String>(lists.get(i)));
            part2stacks.add(i, new ArrayDeque<String>(lists.get(i)));
        }

        var movesInput = input.subList(index + 2, input.size());

        for (String move : movesInput) {
            Scanner scanner = new Scanner(move);
            scanner.next(); // move
            var amount = scanner.nextInt();
            scanner.next();
            var from = scanner.nextInt();
            scanner.next();
            var to = scanner.nextInt();
            moves.add(new int[] { amount, from, to });
            scanner.close();
        }
    }

    private String part1() {

        for (int[] move : moves) {
            var amount = move[0];
            for (int i = 0; i < amount; i++) {
                var letter = part1stacks.get(move[1] - 1).pop();
                part1stacks.get(move[2] - 1).push(letter);
            }
        }

        var result = new StringBuilder();
        for (int i = 0; i < NR_STACKS; i++) {
            var top = part1stacks.get(i).peek();
            if (top != null) {
                result.append(top);
            }

        }

        return result.toString();

    }

    private String part2() {
        for (int[] move : moves) {
            var amount = move[0];
            var letters = new ArrayList<String>();
            for (int i = 0; i < amount; i++) {
                letters.add(part2stacks.get(move[1] - 1).pop());
            }
            Collections.reverse(letters);

            for (String string : letters) {
                part2stacks.get(move[2] - 1).push(string);

            }
        }

        var result = new StringBuilder();
        for (int i = 0; i < NR_STACKS; i++) {
            var top = part2stacks.get(i).peek();
            if (top != null) {
                result.append(top);
            }
        }

        return result.toString();
    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        List<String> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }

        var day = new Day05(data);

        System.out.println("Part 1: " + day.part1());
        System.out.println("Part 2: " + day.part2());

        scanner.close();
    }
}