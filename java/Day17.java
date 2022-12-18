import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Day17 {
    class Point {
        long x;
        long y;

        Point(long x, long y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "[" + x + "," + y + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getEnclosingInstance().hashCode();
            result = prime * result + (int) (x ^ (x >>> 32));
            result = prime * result + (int) (y ^ (y >>> 32));
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

        private Day17 getEnclosingInstance() {
            return Day17.this;
        }
    }

    class Rock {
        Set<Point> points;
        boolean inRest;

        Rock(Set<Point> points, boolean inRest) {
            this.points = points;
            this.inRest = inRest;
        }

        public boolean inRest() {
            return inRest;
        }

        public Day17.Rock pushByJet(char dir) {
            int dx = dir == '>' ? 1 : -1;
            var newRock = new Rock(
                    points.stream().map((p) -> new Point(p.x + dx, p.y)).collect(HashSet::new, Set::add, Set::addAll),
                    false);

            for (int x = 0; x < 7; x++) {
                final int xx = x;
                if (newRock.points.stream().anyMatch((p) -> p.x == xx && heights[xx] >= p.y || p.x < 0 || p.x > 6)) {
                    return this;
                }
            }

            return newRock;
        }

        public Day17.Rock moveDown() {
            int dy = -1;
            var newRock = new Rock(
                    points.stream().map((p) -> new Point(p.x, p.y + dy)).collect(HashSet::new, Set::add, Set::addAll),
                    false);

            for (int x = 0; x < 7; x++) {
                final int xx = x;
                if (newRock.points.stream().anyMatch((p) -> p.x == xx && heights[xx] >= p.y)) {
                    return new Rock(points, true);
                }
            }

            return newRock;

        }

        @Override
        public String toString() {
            return "points=" + points;
        }
    }

    String jet;
    List<Rock> rocks = new ArrayList<>();
    long[] heights = new long[] { -1, -1, -1, -1, -1, -1, -1 };

    public Day17(List<String> input) {
        jet = input.get(0);
        addRock1();
        addRock2();
        addRock3();
        addRock4();
        addRock5();
    }

    void addRock1() {
        var points = new HashSet<Point>();
        points.add(new Point(0, 0));
        points.add(new Point(1, 0));
        points.add(new Point(2, 0));
        points.add(new Point(3, 0));
        var rock = new Rock(points, false);

        rocks.add(rock);
    }

    void addRock2() {
        var points = new HashSet<Point>();
        points.add(new Point(1, 0));
        points.add(new Point(0, 1));
        points.add(new Point(1, 1));
        points.add(new Point(2, 1));
        points.add(new Point(1, 2));
        var rock = new Rock(points, false);

        rocks.add(rock);
    }

    void addRock3() {
        var points = new HashSet<Point>();
        points.add(new Point(0, 0));
        points.add(new Point(1, 0));
        points.add(new Point(2, 0));
        points.add(new Point(2, 1));
        points.add(new Point(2, 2));
        var rock = new Rock(points, false);

        rocks.add(rock);
    }

    void addRock4() {
        var points = new HashSet<Point>();
        points.add(new Point(0, 0));
        points.add(new Point(0, 1));
        points.add(new Point(0, 2));
        points.add(new Point(0, 3));
        var rock = new Rock(points, false);

        rocks.add(rock);
    }

    void addRock5() {
        var points = new HashSet<Point>();
        points.add(new Point(0, 0));
        points.add(new Point(0, 1));
        points.add(new Point(1, 0));
        points.add(new Point(1, 1));
        var rock = new Rock(points, false);

        rocks.add(rock);
    }

    private Rock takeNewRock(int index, long maxY) {
        var rock = rocks.get(index);

        final long height = maxY + 4;

        return new Rock(
                rock.points.stream().map((p) -> new Point(p.x + 2, p.y + height)).collect(HashSet::new, Set::add,
                        Set::addAll),
                false);
    }

    private long maxHeight(long nrRocks) {
        int jetIndex = 0;
        int rockIndex = 0;
        var maxY = -1L;

        for (int i = 0; i < nrRocks; i++) {
            var rock = takeNewRock(rockIndex, maxY);
            while (!rock.inRest()) {
                rock = rock.pushByJet(jet.charAt(jetIndex));
                jetIndex = (jetIndex + 1) % jet.length();
                rock = rock.moveDown();
            }

            for (int x = 0; x < 7; x++) {
                for (var point : rock.points) {
                    if (point.x == x) {
                        heights[x] = Math.max(heights[x], point.y);
                    }
                }
            }

            maxY = Arrays.stream(heights).max().getAsLong();
            rockIndex = (rockIndex + 1) % rocks.size();
        }

        return maxY + 1;

    }

    private long part1() {
        return maxHeight(2022);
    }

    private long part2() {
        return maxHeight(1000000000000L);
    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        List<String> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }

        System.out.println("Part 1: " + new Day17(data).part1());
        System.out.println("Part 2: " + new Day17(data).part2());

        scanner.close();
    }
}