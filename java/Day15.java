import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
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

    private int scanners(Point p) {
        var scanners = 0;
        for (var entry : distances.entrySet()) {
            if (manhattenDistance(entry.getKey(), p) <= entry.getValue()) {
                scanners++;
            }
        }

        return scanners;
    }

    class Node {
        Point p;
        int scanners;

        Node(Point p, int scanners) {
            this.p = p;
            this.scanners = scanners;
        }

    }

    private List<int[]> delta(Node n) {
        var result = new ArrayList<int[]>();
        for (var entry : distances.entrySet()) {

        }
        return result;
    }

    private List<Node> children(Node node, int max, Set<Point> handled) {
        var result = new ArrayList<Node>();
        var delta = delta(node);
        for (int[] d : delta) {
            if (node.p.x + d[0] < 0 || node.p.x + d[0] > max || node.p.y + d[1] < 0 || node.p.y + d[1] > max) {
                continue;
            }
            Point p = new Point(node.p.x + d[0], node.p.y + d[1]);
            if (handled.contains(p)) {
                continue;
            }
            int s = scanners(p);
            result.add(new Node(p, s));
        }

        return result;
    }

    private int part2_temp() {
        // int high = 4000000;
        int high = 20;
        Point p = new Point(high / 2, high / 2);
        var scanners = scanners(p);
        var node = new Node(p, scanners);
        PriorityQueue<Node> queue = new PriorityQueue<>((a, b) -> a.scanners - b.scanners);
        Set<Point> handled = new HashSet<>();

        while (node.scanners != 0) {
            var children = children(node, high, handled);
            for (var child : children) {
                if (!handled.contains(child.p)) {
                    queue.add(child);
                }
            }

            handled.add(node.p);
            node = queue.poll();

        }

        return node.p.x * 4000000 + node.p.y;

    }

    private int part2() {
        var high = 4000000;
        for (int x = 0; x < high; x++) {
            for (int y = 0; y < high; y++) {
                var point = new Point(x, y);
                if (!canHaveBeacon(point)) {
                }
            }
        }

        return node.p.x * 4000000 + node.p.y;
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