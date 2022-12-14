import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Day14 {
    class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Point(String pointString) {
            var point = pointString.split(",");
            this.x = Integer.parseInt(point[0]);
            this.y = Integer.parseInt(point[1]);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getEnclosingInstance().hashCode();
            result = prime * result + x;
            result = prime * result + y;
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
            return true;
        }

        private Day14 getEnclosingInstance() {
            return Day14.this;
        }
    }

    class Path {
        List<Point> points;

        public Path(List<Point> points) {
            this.points = points;
        }
    }

    List<String> input;

    public Day14(List<String> input) {
        this.input = input;
    }

    Set<Point> parse() {
        var paths = new ArrayList<Path>();
        for (String line : input) {
            var splitted = line.split(" -> ");
            var points = new ArrayList<Point>();
            for (String pointString : splitted) {
                var point = new Point(pointString);
                points.add(point);
            }
            paths.add(new Path(points));
        }

        return drawPaths(paths);
    }

    Set<Point> drawPaths(List<Path> paths) {
        Set<Point> blocked = new HashSet<>();
        for (Path path : paths) {
            for (int i = 0; i < path.points.size() - 1; i++) {
                var point1 = path.points.get(i);
                var point2 = path.points.get(i + 1);
                drawLine(point1, point2, blocked);
            }
        }
        return blocked;
    }

    void drawLine(Point a, Point b, Set<Point> blocked) {
        if (a.x == b.x) {
            for (int i = Math.min(a.y, b.y); i <= Math.max(a.y, b.y); i++) {
                blocked.add(new Point(a.x, i));
            }
        } else {
            for (int i = Math.min(a.x, b.x); i <= Math.max(a.x, b.x); i++) {
                blocked.add(new Point(i, a.y));
            }
        }
    }

    Point nextSand(Point currentSand, Set<Point> blocked, List<Point> sandInRest) {
        if (!blocked.contains(new Point(currentSand.x, currentSand.y + 1))) {
            return new Point(currentSand.x, currentSand.y + 1);
        } else if (!blocked.contains(new Point(currentSand.x - 1, currentSand.y + 1))) {
            return new Point(currentSand.x - 1, currentSand.y + 1);
        } else if (!blocked.contains(new Point(currentSand.x + 1, currentSand.y + 1))) {
            return new Point(currentSand.x + 1, currentSand.y + 1);
        } else {
            sandInRest.add(currentSand);
            blocked.add(currentSand);
            return new Point(500, 0);
        }
    }

    private int part1() {
        var blocked = parse();
        int abyss = blocked.stream().mapToInt(p -> p.y).max().getAsInt();
        Point currentSand = new Point(500, 0);
        List<Point> sandInRest = new ArrayList<>();
        while (currentSand.y < abyss) {
            currentSand = nextSand(currentSand, blocked, sandInRest);
        }

        return sandInRest.size();

    }

    private int part2() {
        var blocked = parse();
        int abyss = blocked.stream().mapToInt(p -> p.y).max().getAsInt();
        int floor = abyss + 2;
        Point currentSand = new Point(500, 0);
        List<Point> sandInRest = new ArrayList<>();
        while (!blocked.contains(currentSand)) {
            if (currentSand.y + 1 == floor) {
                sandInRest.add(currentSand);
                blocked.add(currentSand);
                currentSand = new Point(500, 0);
            } else {
                currentSand = nextSand(currentSand, blocked, sandInRest);
            }
        }

        return sandInRest.size();
    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        List<String> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }

        var day = new Day14(data);

        System.out.println("Part 1: " + day.part1());
        System.out.println("Part 2: " + day.part2());

        scanner.close();
    }
}