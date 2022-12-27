import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

public class Day24 {
    public enum Blizzard {
        Left(),
        Right(),
        Up(),
        Down();
    }

    class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
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

        private Day24 getEnclosingInstance() {
            return Day24.this;
        }

    }

    int mapWidth;
    int mapHeight;
    int period;
    Point currentStart;
    Point currentGoal;

    static Map<Integer, Set<Point>> blizzardsAtMinute = new HashMap<>();

    class Node implements Comparable<Node> {
        int minute;
        Point expedition;
        int priority;
        State state;

        public Node(Point expedition, int minute) {
            this.minute = minute;
            this.expedition = expedition;
            this.priority = minute + (Math.abs(expedition.x - currentGoal.x) + Math.abs(expedition.y - currentGoal.y));
            this.state = new State(expedition, minute);
        }

        List<Node> generateChildren() {
            List<Node> children = new ArrayList<>();
            var blizzards = blizzardsAtMinute.get((minute + 1) % period);

            var delta = new int[][] { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 }, { 0, 0 } };

            for (var d : delta) {
                var point = new Point(expedition.x + d[0], expedition.y + d[1]);
                if (blizzards.contains(point) || !validPoint(point))
                    continue;

                var node = new Node(point, minute + 1);
                children.add(node);
            }

            return children;
        }

        boolean validPoint(Point point) {
            if (point.equals(currentGoal) || point.equals(currentStart))
                return true;

            return point.x > 0 && point.x < mapWidth - 1 && point.y > 0 && point.y < mapHeight - 1;
        }

        @Override
        public int compareTo(Day24.Node o) {
            return this.priority - o.priority;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getEnclosingInstance().hashCode();
            result = prime * result + ((state == null) ? 0 : state.hashCode());
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
            Node other = (Node) obj;
            if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
                return false;
            if (state == null) {
                if (other.state != null)
                    return false;
            } else if (!state.equals(other.state))
                return false;
            return true;
        }

        private Day24 getEnclosingInstance() {
            return Day24.this;
        }
    }

    class State {
        final int minute;
        final Point expedition;

        public State(Point expedition, int minute) {
            this.minute = minute % period;
            this.expedition = expedition;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getEnclosingInstance().hashCode();
            result = prime * result + minute;
            result = prime * result + ((expedition == null) ? 0 : expedition.hashCode());
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
            State other = (State) obj;
            if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
                return false;
            if (minute != other.minute)
                return false;
            if (expedition == null) {
                if (other.expedition != null)
                    return false;
            } else if (!expedition.equals(other.expedition))
                return false;
            return true;
        }

        private Day24 getEnclosingInstance() {
            return Day24.this;
        }

    }

    static int gcd(int a, int b) {
        if (b == 0)
            return a;
        return gcd(b, a % b);
    }

    static int lcm(int a, int b) {
        return a * (b / gcd(a, b));
    }

    public Day24(List<String> input) {
        mapHeight = input.size();
        mapWidth = input.get(0).length();
        period = lcm(mapHeight - 2, mapWidth - 2);

        var map = new HashMap<Point, List<Blizzard>>();
        for (int i = 1; i < input.size() - 1; i++) {
            for (int j = 1; j < input.get(i).length() - 1; j++) {
                var point = new Point(j, i);
                switch (input.get(i).charAt(j)) {
                    case '<':
                        map.put(point, List.of(Blizzard.Left));
                        break;
                    case '>':
                        map.put(point, List.of(Blizzard.Right));
                        break;
                    case '^':
                        map.put(point, List.of(Blizzard.Up));
                        break;
                    case 'v':
                        map.put(point, List.of(Blizzard.Down));
                        break;
                }
            }
        }
        blizzardsAtMinute.put(0, map.keySet());

        for (int i = 1; i <= period; i++) {
            var newMap = new HashMap<Point, List<Blizzard>>();

            for (var entry : map.entrySet()) {
                var point = entry.getKey();
                var blizzards = entry.getValue();
                for (Blizzard blizzard : blizzards) {
                    int x = 0;
                    int y = 0;
                    switch (blizzard) {
                        case Left:
                            x = point.x - 1 == 0 ? mapWidth - 2 : point.x - 1;
                            y = point.y;
                            break;
                        case Right:
                            x = point.x + 1;
                            x = x == mapWidth - 1 ? 1 : x;
                            y = point.y;
                            break;
                        case Up:
                            x = point.x;
                            y = point.y - 1 == 0 ? mapHeight - 2 : point.y - 1;
                            break;
                        case Down:
                            x = point.x;
                            y = point.y + 1;
                            y = y == mapHeight - 1 ? 1 : y;
                            break;
                    }

                    var newPoint = new Point(x, y);
                    if (newMap.containsKey(newPoint))
                        newMap.get(newPoint).add(blizzard);
                    else
                        newMap.put(newPoint, new ArrayList<Blizzard>(List.of(blizzard)));

                    blizzardsAtMinute.put(i, newMap.keySet());
                }
            }
            map = newMap;
        }
    }

    private int astar(Point start, Point goal, int startMinute) {
        currentStart = start;
        currentGoal = goal;
        var queue = new PriorityQueue<Node>();
        queue.add(new Node(start, startMinute));
        Set<State> visited = new HashSet<>();

        while (!queue.isEmpty()) {
            var node = queue.poll();
            if (node.expedition.equals(goal)) {
                return node.minute;
            }

            for (var child : node.generateChildren()) {
                if (!visited.contains(child.state) && !queue.contains(child)) {
                    queue.add(child);
                }
            }

            visited.add(node.state);

        }
        return 0;
    }

    private int part1() {

        System.out.println("start part 1");

        Point start = new Point(1, 0);
        Point goal = new Point(mapWidth - 2, mapHeight - 1);
        return astar(start, goal, 0);
    }

    private int part2() {
        Point start = new Point(1, 0);
        Point goal = new Point(mapWidth - 2, mapHeight - 1);
        var t1 = astar(start, goal, 0);
        var t2 = astar(goal, start, t1);
        return astar(start, goal, t2);
    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        List<String> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }

        System.out.println("Part 1: " + new Day24(data).part1());
        System.out.println("Part 2: " + new Day24(data).part2());

        scanner.close();
    }
}