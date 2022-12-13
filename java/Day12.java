import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day12 {

    List<List<Character>> map = new ArrayList<>();

    public Day12(List<String> input) {
        for (String line : input) {
            map.add(Arrays.asList(line.split("")).stream().map(c -> c.charAt(0)).collect(Collectors.toList()));
        }
    }

    int[] findStart() {
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (map.get(i).get(j).equals('S')) {
                    return new int[] { i, j };
                }
            }
        }
        throw new RuntimeException("No start found");
    }

    int[] findEnd() {
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (map.get(i).get(j).equals('E')) {
                    return new int[] { i, j };
                }
            }
        }
        throw new RuntimeException("No start found");
    }

    List<int[]> neighbors(int[] current, int currentHeight) {
        var neighbors = new ArrayList<int[]>();
        if (current[0] > 0 && height(current[0] - 1, current[1]) - currentHeight <= 1) {
            neighbors.add(new int[] { current[0] - 1, current[1] });
        }
        if (current[0] < map.size() - 1 && height(current[0] + 1, current[1]) - currentHeight <= 1) {
            neighbors.add(new int[] { current[0] + 1, current[1] });
        }
        if (current[1] > 0 && height(current[0], current[1] - 1) - currentHeight <= 1) {
            neighbors.add(new int[] { current[0], current[1] - 1 });
        }
        if (current[1] < map.get(current[0]).size() - 1
                && height(current[0], current[1] + 1) - currentHeight <= 1) {
            neighbors.add(new int[] { current[0], current[1] + 1 });
        }
        return neighbors;
    }

    int height(int i, int j) {
        if (map.get(i).get(j).equals('S')) {
            return 0;
        }
        if (map.get(i).get(j).equals('E')) {
            return 'z' - 'a';
        }
        return map.get(i).get(j) - 'a';
    }

    int shortestPath(int[] start) {
        var dist = new ArrayList<List<Integer>>();
        for (int i = 0; i < map.size(); i++) {
            var row = new ArrayList<Integer>();
            for (int j = 0; j < map.get(i).size(); j++) {
                row.add(Integer.MAX_VALUE);
            }
            dist.add(row);
        }
        dist.get(start[0]).set(start[1], 0);

        var next = new ArrayList<int[]>();
        next.add(start);

        while (!next.isEmpty()) {
            var current = next.remove(0);
            var neighbors = neighbors(current, height(current[0], current[1]));
            for (var neighbor : neighbors) {
                if (map.get(neighbor[0]).get(neighbor[1]).equals('E'))
                    return dist.get(current[0]).get(current[1]) + 1;

                if (dist.get(neighbor[0]).get(neighbor[1]) > dist.get(current[0]).get(current[1]) + 1) {
                    dist.get(neighbor[0]).set(neighbor[1], dist.get(current[0]).get(current[1]) + 1);
                    next.add(neighbor);
                }
            }
        }

        var end = findEnd();
        return dist.get(end[0]).get(end[1]);

    }

    private int part1() {
        var start = findStart();
        return shortestPath(start);
    }

    private int part2() {
        var allStartingPositions = new ArrayList<int[]>();
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (map.get(i).get(j).equals('S') || map.get(i).get(j).equals('a')) {
                    allStartingPositions.add(new int[] { i, j });
                }
            }
        }

        return allStartingPositions.stream().mapToInt(s -> shortestPath(s)).min().getAsInt();

    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        List<String> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }

        var day = new Day12(data);

        System.out.println("Part 1: " + day.part1());
        System.out.println("Part 2: " + day.part2());

        scanner.close();
    }
}