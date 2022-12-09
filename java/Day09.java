import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day09 {
    List<String> input;

    public Day09(List<String> input) {
        this.input = input;
    }

    void markVisited(Map<String, Integer> visited, Knot tail) {
        String key = tail.pos[0] + "," + tail.pos[1];
        if (visited.containsKey(key)) {
            visited.put(key, visited.get(key) + 1);
        } else {
            visited.put(key, 1);
        }
    }

    class Knot {
        int[] pos;
        Knot next;

        public Knot(int[] pos) {
            this.pos = pos;
        }

        Knot addKnots(int nr) {
            if (nr == 0) {
                return this;
            } else {
                next = new Knot(new int[] { pos[0], pos[1] });
                return next.addKnots(nr - 1);
            }
        }

        void move(int[] delta) {
            pos[0] += delta[0];
            pos[1] += delta[1];
            next.moveInDirection(this.pos);
        }

        void moveInDirection(int[] head) {
            if (Math.abs(head[0] - pos[0]) <= 1 && Math.abs(head[1] - pos[1]) <= 1)
                return;
            else {
                var diag = Math.abs(head[0] - pos[0]) > 0 && Math.abs(head[1] - pos[1]) > 0;
                if (Math.abs(head[0] - pos[0]) > 1 || diag) {
                    if (head[0] > pos[0]) {
                        pos[0]++;
                    } else {
                        pos[0]--;
                    }
                }
                if (Math.abs(head[1] - pos[1]) > 1 || diag) {
                    if (head[1] > pos[1]) {
                        pos[1]++;
                    } else {
                        pos[1]--;
                    }
                }
            }
            if (next != null) {
                next.moveInDirection(this.pos);
            }
        }
    }

    private int part1() {
        Map<String, Integer> visited = new HashMap<>();
        var head = new Knot(new int[] { 0, 0 });
        var tail = head.addKnots(1);
        for (String line : input) {
            var splitted = line.split(" ");
            var delta = new int[] { 0, 0 };
            switch (splitted[0]) {
                case "R":
                    delta[0] = 1;
                    break;
                case "L":
                    delta[0] = -1;
                    break;
                case "U":
                    delta[1] = 1;
                    break;
                case "D":
                    delta[1] = -1;
                    break;
            }
            for (int i = 0; i < Integer.parseInt(splitted[1]); i++) {
                head.move(delta);
                markVisited(visited, tail);
            }

        }

        return visited.keySet().size();

    }

    private int part2() {
        Map<String, Integer> visited = new HashMap<>();
        var head = new Knot(new int[] { 0, 0 });
        var tail = head.addKnots(9);
        for (String line : input) {
            var splitted = line.split(" ");
            var delta = new int[] { 0, 0 };
            switch (splitted[0]) {
                case "R":
                    delta[0] = 1;
                    break;
                case "L":
                    delta[0] = -1;
                    break;
                case "U":
                    delta[1] = 1;
                    break;
                case "D":
                    delta[1] = -1;
                    break;
            }
            for (int i = 0; i < Integer.parseInt(splitted[1]); i++) {
                head.move(delta);
                markVisited(visited, tail);
            }

        }

        return visited.keySet().size();

    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        List<String> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }

        var day = new Day09(data);

        System.out.println("Part 1: " + day.part1());
        System.out.println("Part 2: " + day.part2());

        scanner.close();
    }
}