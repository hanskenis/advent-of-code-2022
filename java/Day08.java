import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day08 {
    List<List<Integer>> forrest = new ArrayList<>();

    public Day08(List<String> input) {
        for (String line : input) {
            List<Integer> row = new ArrayList<>();
            forrest.add(row);
            for (Character c : line.toCharArray()) {
                row.add(Integer.parseInt(c.toString()));
            }
        }
    }

    int[][] delta = { { -1, 0 }, { 0, -1 }, { 1, 0 }, { 0, 1 } };

    private boolean isVisible(int i, int j) {
        int height = forrest.get(i).get(j);
        for (int k = 0; k < delta.length; k++) {
            int x = i + delta[k][0];
            int y = j + delta[k][1];
            while (forrest.get(x).get(y) < height) {
                x += delta[k][0];
                y += delta[k][1];

                if (x < 0 || x >= forrest.size() || y < 0 || y >= forrest.get(x).size()) {
                    return true;
                }
            }
        }

        return false;

    }

    private int part1() {
        int nrVisible = 0;
        for (int i = 0; i < forrest.size(); i++) {
            for (int j = 0; j < forrest.get(i).size(); j++) {
                if (i == 0 || i == forrest.size() - 1 || j == 0 || j == forrest.get(i).size() - 1) {
                    nrVisible++;

                } else if (isVisible(i, j)) {
                    nrVisible++;
                }
            }

        }

        return nrVisible;

    }

    private int scenicScore(int i, int j) {
        int total = 1;
        int height = forrest.get(i).get(j);
        for (int k = 0; k < delta.length; k++) {
            int x = i + delta[k][0];
            int y = j + delta[k][1];
            int score = 0;
            while (x >= 0 && x < forrest.size() && y >= 0 && y < forrest.get(x).size()) {
                score++;
                if (forrest.get(x).get(y) >= height) {
                    break;
                }

                x += delta[k][0];
                y += delta[k][1];

                if (x < 0 || x >= forrest.size() || y < 0 || y >= forrest.get(x).size()) {
                    break;
                }
            }
            total *= score;
        }
        return total;
    }

    private int part2() {
        int max = 0;
        for (int i = 1; i < forrest.size() - 1; i++) {
            for (int j = 1; j < forrest.get(i).size() - 1; j++) {
                max = Math.max(max, scenicScore(i, j));
            }
        }

        return max;
    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        List<String> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }

        var day = new Day08(data);

        System.out.println("Part 1: " + day.part1());
        System.out.println("Part 2: " + day.part2());

        scanner.close();
    }
}