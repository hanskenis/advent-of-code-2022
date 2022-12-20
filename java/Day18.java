import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Day18 {
    class Point {
        int x;
        int y;
        int z;

        public Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Point(String input) {
            var parts = Arrays.asList(input.split(",")).stream().mapToInt(Integer::parseInt).toArray();
            this.x = parts[0];
            this.y = parts[1];
            this.z = parts[2];
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getEnclosingInstance().hashCode();
            result = prime * result + x;
            result = prime * result + y;
            result = prime * result + z;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Point other = (Point) obj;
            if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
                return false;
            if (x != other.x)
                return false;
            if (y != other.y)
                return false;
            if (z != other.z)
                return false;
            return true;
        }

        private Day18 getEnclosingInstance() {
            return Day18.this;
        }
    }

    List<Point> positions;

    public Day18(List<String> input) {
        positions = input.stream().map(Point::new).collect(Collectors.toList());
    }

    private int notConnectedSides(List<Point> positions) {
        int result = 0;
        int adjacent = 0;

        List<Point> sortedByX = positions.stream().sorted((p1, p2) -> p1.x - p2.x).collect(Collectors.toList());
        for (int i = 0; i < sortedByX.size(); i++) {
            for (int j = i + 1; j < sortedByX.size(); j++) {
                var p1 = sortedByX.get(i);
                var p2 = sortedByX.get(j);
                if (p2.x - p1.x == 1 && p2.y == p1.y && p2.z == p1.z) {
                    adjacent += 2;
                }
            }
        }

        result += sortedByX.size() * 2 - adjacent;

        adjacent = 0;
        List<Point> sortedByY = positions.stream().sorted((p1, p2) -> p1.y - p2.y).collect(Collectors.toList());
        for (int i = 0; i < sortedByY.size(); i++) {
            for (int j = i + 1; j < sortedByY.size(); j++) {
                var p1 = sortedByY.get(i);
                var p2 = sortedByY.get(j);
                if (p2.y - p1.y == 1 && p2.x == p1.x && p2.z == p1.z) {
                    adjacent += 2;
                }
            }
        }
        result += sortedByY.size() * 2 - adjacent;

        adjacent = 0;
        List<Point> sortedByZ = positions.stream().sorted((p1, p2) -> p1.z - p2.z).collect(Collectors.toList());
        for (int i = 0; i < sortedByZ.size(); i++) {
            for (int j = i + 1; j < sortedByZ.size(); j++) {
                var p1 = sortedByZ.get(i);
                var p2 = sortedByZ.get(j);
                if (p2.z - p1.z == 1 && p2.x == p1.x && p2.y == p1.y) {
                    adjacent += 2;
                }
            }
        }
        result += sortedByY.size() * 2 - adjacent;

        return result;
    }

    private int part1() {
        return notConnectedSides(positions);
    }

    List<Point> getAdjacentAir(Point point) {
        List<Point> result = new ArrayList<>();
        result.add(new Point(point.x + 1, point.y, point.z));
        result.add(new Point(point.x - 1, point.y, point.z));
        result.add(new Point(point.x, point.y + 1, point.z));
        result.add(new Point(point.x, point.y - 1, point.z));
        result.add(new Point(point.x, point.y, point.z + 1));
        result.add(new Point(point.x, point.y, point.z - 1));
        return result.stream().filter(p -> !positions.contains(p)).collect(Collectors.toList());
    }

    boolean lockedIn(Point point, int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        List<Point> queue = new ArrayList<>();
        queue.add(point);
        Set<Point> visited = new HashSet<>();
        while (!queue.isEmpty()) {
            var next = queue.remove(0);
            if (visited.contains(next)) {
                continue;
            }

            if (next.x < minX || next.x > maxX || next.y < minY || next.y > maxY || next.z < minZ || next.z > maxZ) {
                return false;
            }

            visited.add(next);
            var adjacent = getAdjacentAir(next);
            queue.addAll(adjacent);

        }

        return true;

    }

    private int part2() {
        var notConnected = notConnectedSides(positions);
        var minX = positions.stream().mapToInt(p -> p.x).min().getAsInt();
        var minY = positions.stream().mapToInt(p -> p.y).min().getAsInt();
        var minZ = positions.stream().mapToInt(p -> p.z).min().getAsInt();
        var maxX = positions.stream().mapToInt(p -> p.x).max().getAsInt();
        var maxY = positions.stream().mapToInt(p -> p.y).max().getAsInt();
        var maxZ = positions.stream().mapToInt(p -> p.z).max().getAsInt();

        Set<Point> airlocked = new HashSet<>();

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    var point = new Point(x, y, z);
                    if (!positions.contains(point) && lockedIn(point, minX, maxX, minY, maxY, minZ, maxZ)) {
                        airlocked.add(point);
                    }
                }
            }
        }

        var airlockedNotConnected = notConnectedSides(new ArrayList<Point>(airlocked));

        return notConnected - airlockedNotConnected;

    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        List<String> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }

        System.out.println("Part 1: " + new Day18(data).part1());
        System.out.println("Part 2: " + new Day18(data).part2());

        scanner.close();
    }
}