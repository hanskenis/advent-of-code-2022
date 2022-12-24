import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Day15 {
    class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Point other = (Point) obj;
            if (this.x != other.x) {
                return false;
            }
            if (this.y != other.y) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + this.x;
            hash = 97 * hash + this.y;
            return hash;
        }

        @Override
        public String toString() {
            return "Point{" + "x=" + x + ", y=" + y + '}';
        }
    }

    Map<Point, Point> map = new HashMap<>();
    Map<Point, Integer> distances = new HashMap<>();
    Set<Point> beacons = new HashSet<>();
    Point topLeft;
    Point bottomRight;
    int maxDist;

    public Day15(List<String> input) {
        int minx = Integer.MAX_VALUE;
        int miny = Integer.MAX_VALUE;
        int maxx = Integer.MIN_VALUE;
        int maxy = Integer.MIN_VALUE;
        maxDist = Integer.MIN_VALUE;

        for (String line : input) {
            var splitted = line.split(":");
            var sensor = parseSensor(splitted[0]);
            var beacon = parseBeacon(splitted[1]);
            map.put(sensor, beacon);
            beacons.add(beacon);

            var dist = manhattenDistance(sensor, beacon);
            maxDist = Math.max(maxDist, dist);
            distances.put(sensor, dist);

            minx = Math.min(Math.min(minx, sensor.x), beacon.x);
            miny = Math.min(Math.min(miny, sensor.y), beacon.y);
            maxx = Math.max(Math.max(maxx, sensor.x), beacon.x);
            maxy = Math.max(Math.max(maxy, sensor.y), beacon.y);

        }
        topLeft = new Point(minx, miny);
        bottomRight = new Point(maxx, maxy);

    }

    int manhattenDistance(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    int manhattenDistance(Point a, int x, int y) {
        return Math.abs(a.x - x) + Math.abs(a.y - y);
    }

    Point parseSensor(String sensor) {
        sensor = sensor.replace("Sensor at ", "");
        var splitted = sensor.split(",");
        var x = Integer.parseInt(splitted[0].trim().replace("x=", ""));
        var y = Integer.parseInt(splitted[1].trim().replace("y=", ""));
        return new Point(x, y);
    }

    Point parseBeacon(String beacon) {
        beacon = beacon.trim().replace("closest beacon is at ", "");
        var splitted = beacon.split(",");
        var x = Integer.parseInt(splitted[0].trim().replace("x=", ""));
        var y = Integer.parseInt(splitted[1].trim().replace("y=", ""));
        return new Point(x, y);
    }

    private boolean canHaveBeacon(Point point) {
        for (var entry : distances.entrySet()) {
            if (manhattenDistance(entry.getKey(), point) <= entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    private int part1() {
        // var y = 10;
        var y = 2000000;
        var result = 0;
        for (int i = topLeft.x - maxDist; i < bottomRight.x + maxDist; i++) {
            var point = new Point(i, y);
            if (!beacons.contains(point) && !canHaveBeacon(point)) {
                result++;
            }
        }

        return result;

    }

    private boolean hasScanners(int x, int y, Point ignore) {
        for (var entry : distances.entrySet()) {
            if (entry != ignore && manhattenDistance(entry.getKey(), x, y) <= entry.getValue()) {
                return true;
            }
        }

        return false;
    }

    boolean inRange(int x, int y, int max) {
        return x >= 0 && x < max && y >= 0 && y < max;
    }

    boolean found(int x, int y, int max, Point ignore) {
        return inRange(x, y, max) && !hasScanners(x, y, ignore) && !beacons.contains(new Point(x, y));
    }

    private long part2() {
        // var max = 20;
        int max = 4000000;
        for (var entry : distances.entrySet()) {
            var scanner = entry.getKey();
            var dist = entry.getValue();
            int x = scanner.x + dist + 1;
            int y = scanner.y;
            while (x != scanner.x && y != scanner.y) {
                if (found(x, y, max, scanner)) {
                    return x * 4000000L + y;
                }
                x--;
                y--;
            }

            x = scanner.x;
            y = scanner.y - dist - 1;
            while (x != scanner.x - dist - 1 && y != scanner.y) {
                if (found(x, y, max, scanner)) {
                    return x * 4000000L + y;
                }
                x--;
                y++;
            }

            x = scanner.x - dist - 1;
            y = scanner.y;
            while (x != scanner.x && y != scanner.y + dist + 1) {
                if (found(x, y, max, scanner)) {
                    return x * 4000000L + y;
                }
                x++;
                y++;
            }
            x = scanner.x;
            y = scanner.y + dist + 1;
            while (x != scanner.x + dist + 1 && y != scanner.y) {
                if (found(x, y, max, scanner)) {
                    return x * 4000000L + y;
                }
                x++;
                y--;
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        List<String> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }

        System.out.println("Part 1: " + new Day15(data).part1());
        System.out.println("Part 2: " + new Day15(data).part2());

        scanner.close();
    }
}