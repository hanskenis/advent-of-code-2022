import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day16 {
    class Valve {
        String name;
        int rate;

        public Valve(String name, int rate) {
            this.name = name;
            this.rate = rate;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getEnclosingInstance().hashCode();
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + rate;
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
            Valve other = (Valve) obj;
            if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
                return false;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            if (rate != other.rate)
                return false;
            return true;
        }

        private Day16 getEnclosingInstance() {
            return Day16.this;
        }

        @Override
        public String toString() {
            return "Valve [name=" + name + "]";
        }
    }

    class NodePart1 {
        Valve valve;
        NodePart1 parent;
        int minutesLeft;
        List<Valve> openValves;
        int flowRate;
        int totalRate;

        public NodePart1(Valve valve, NodePart1 parent, int minutesLeft, List<Valve> openValves, int flowRate,
                int totalRate) {
            this.valve = valve;
            this.parent = parent;
            this.minutesLeft = minutesLeft;
            this.openValves = new ArrayList<>(openValves);
            this.flowRate = flowRate;
            this.totalRate = totalRate;
        }

        @Override
        public String toString() {
            return "valve=" + valve + ",  totalRate=" + totalRate
                    + ", minutesLeft=" + minutesLeft;
        }

        public String path() {
            if (parent == null) {
                return valve.name;
            }
            var name = openValves.contains(valve) ? valve.name : "!" + valve.name;
            return parent.path() + " -> " + name;
        }

    }

    class NodePart2 {
        Valve ownValve;
        Valve elephantValve;
        NodePart2 parent;
        int minutesLeft;
        List<Valve> openValves;
        int flowRate;
        int totalRate;

        public NodePart2(Valve ownValve, Valve elephantValve, NodePart2 parent, int minutesLeft, List<Valve> openValves,
                int flowRate,
                int totalRate) {
            this.ownValve = ownValve;
            this.elephantValve = elephantValve;
            this.parent = parent;
            this.minutesLeft = minutesLeft;
            this.openValves = new ArrayList<>(openValves);
            this.flowRate = flowRate;
            this.totalRate = totalRate;
        }

        public String path() {
            if (parent == null) {
                return "(" + ownValve.name + ", " + elephantValve.name + ")";
            }
            var name = openValves.contains(ownValve) ? ownValve.name : "!" + ownValve.name;
            var eName = openValves.contains(elephantValve) ? elephantValve.name : "!" + elephantValve.name;

            return parent.path() + " -> " + "(" + name + "," + eName + ")";
        }

    }

    Map<String, Valve> valves = new HashMap<>();
    Map<String, List<String>> tunnels = new HashMap<>();
    int valvesToOpen;

    public Day16(List<String> input) {
        for (String line : input) {
            var splitted = line.split("; ");
            var valveSplitted = splitted[0].split(" ");
            var name = valveSplitted[1];
            var rate = Integer.parseInt(valveSplitted[4].subSequence(5, valveSplitted[4].length()).toString());
            var valve = new Valve(name, rate);
            valves.put(name, valve);
            var tunnelsArray = splitted[1].split(" ");
            var tunnelsList = Arrays.asList(tunnelsArray).subList(4, tunnelsArray.length).stream()
                    .map(s -> s.trim().replace(",", "")).collect(Collectors.toList());

            tunnels.put(name, tunnelsList);
        }
        valvesToOpen = valves.values().stream().filter(v -> v.rate != 0).collect(Collectors.toList()).size();
    }

    List<NodePart1> generateChildrenPart1(NodePart1 node) {
        var children = new ArrayList<NodePart1>();

        if (node.minutesLeft > 0) {
            children.add(new NodePart1(node.valve, node, node.minutesLeft - 1, node.openValves, node.flowRate,
                    node.totalRate + node.flowRate));

            if (node.openValves.size() < valvesToOpen) {
                for (String tunnel : tunnels.get(node.valve.name)) {
                    var valve = valves.get(tunnel);
                    if (valve == null) {
                        System.out.println("valve null: " + tunnel);
                    }
                    var child = new NodePart1(valves.get(tunnel), node, node.minutesLeft - 1, node.openValves,
                            node.flowRate,
                            node.totalRate + node.flowRate);
                    children.add(child);
                }

                if (!node.openValves.contains(node.valve) && node.valve.rate != 0) {
                    var openValves = new ArrayList<>(node.openValves);
                    openValves.add(node.valve);
                    var child = new NodePart1(node.valve, node, node.minutesLeft - 1, openValves,
                            node.flowRate + node.valve.rate,
                            node.totalRate + node.flowRate + node.valve.rate);
                    children.add(child);
                }
            }
        }

        return children;
    }

    List<NodePart2> generateChildrenPart2(NodePart2 node) {
        var children = new ArrayList<NodePart2>();

        if (node.minutesLeft > 0) {
            // stay at same place and wait
            children.add(new NodePart2(node.ownValve, node.elephantValve, node, node.minutesLeft - 1, node.openValves,
                    node.flowRate,
                    node.totalRate + node.flowRate));

            if (node.openValves.size() < valvesToOpen) {
                // I move, elephant stays
                for (String tunnel : tunnels.get(node.ownValve.name)) {
                    var child = new NodePart2(valves.get(tunnel), node.elephantValve, node, node.minutesLeft - 1,
                            node.openValves,
                            node.flowRate,
                            node.totalRate + node.flowRate);
                    children.add(child);
                }

                // elephant moves
                for (String tunnel : tunnels.get(node.elephantValve.name)) {
                    var child = new NodePart2(node.ownValve, valves.get(tunnel), node, node.minutesLeft - 1,
                            node.openValves,
                            node.flowRate,
                            node.totalRate + node.flowRate);
                    children.add(child);
                }

                // both move
                for (var tunnel1 : tunnels.get(node.ownValve.name)) {
                    for (var tunnel2 : tunnels.get(node.elephantValve.name)) {
                        var child = new NodePart2(valves.get(tunnel1), valves.get(tunnel2), node, node.minutesLeft - 1,
                                node.openValves,
                                node.flowRate,
                                node.totalRate + node.flowRate);
                        children.add(child);
                    }

                }

                // i open valve
                if (!node.openValves.contains(node.ownValve) && node.ownValve.rate != 0) {
                    var openValves = new ArrayList<>(node.openValves);
                    openValves.add(node.ownValve);
                    children.add(
                            new NodePart2(node.ownValve, node.elephantValve, node, node.minutesLeft - 1, openValves,
                                    node.flowRate + node.ownValve.rate,
                                    node.totalRate + node.flowRate + node.ownValve.rate));

                    // elephant moves
                    for (String tunnel : tunnels.get(node.elephantValve.name)) {
                        var child = new NodePart2(node.ownValve, valves.get(tunnel), node, node.minutesLeft - 1,
                                openValves,
                                node.flowRate + node.ownValve.rate,
                                node.totalRate + node.flowRate + node.ownValve.rate);
                        children.add(child);
                    }

                }

                // elephant opens valve
                if (!node.openValves.contains(node.elephantValve) && node.elephantValve.rate != 0) {
                    var openValves = new ArrayList<>(node.openValves);
                    openValves.add(node.elephantValve);
                    children.add(
                            new NodePart2(node.ownValve, node.elephantValve, node, node.minutesLeft - 1, openValves,
                                    node.flowRate + node.elephantValve.rate,
                                    node.totalRate + node.flowRate + node.elephantValve.rate));

                    // i move
                    for (String tunnel : tunnels.get(node.ownValve.name)) {
                        var child = new NodePart2(valves.get(tunnel), node.elephantValve, node, node.minutesLeft - 1,
                                openValves,
                                node.flowRate + node.elephantValve.rate,
                                node.totalRate + node.flowRate + node.elephantValve.rate);
                        children.add(child);
                    }

                }

                // both open valve
                if (!node.openValves.contains(node.elephantValve) && node.elephantValve.rate != 0
                        && !node.openValves.contains(node.ownValve) && node.ownValve.rate != 0
                        && node.ownValve != node.elephantValve) {
                    var openValves = new ArrayList<>(node.openValves);
                    openValves.add(node.elephantValve);
                    openValves.add(node.ownValve);
                    var child = new NodePart2(node.ownValve, node.elephantValve, node,
                            node.minutesLeft - 1, openValves,
                            node.flowRate + node.ownValve.rate + node.elephantValve.rate,
                            node.totalRate + node.flowRate + node.ownValve.rate +
                                    node.elephantValve.rate);
                    children.add(child);
                }
            }
        }

        return children;
    }

    class VisitCheck {
        final String name;
        String name2;
        final int minutesLeft;
        final boolean open;
        boolean open2 = false;

        VisitCheck(String name, int minutesLeft, boolean open) {
            this.name = name;
            this.minutesLeft = minutesLeft;
            this.open = open;
        }

        VisitCheck(String name, String name2, int minutesLeft, boolean open, boolean open2) {
            this.name = name;
            this.name2 = name2;
            this.minutesLeft = minutesLeft;
            this.open = open;
            this.open2 = open2;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getEnclosingInstance().hashCode();
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + ((name2 == null) ? 0 : name2.hashCode());
            result = prime * result + minutesLeft;
            result = prime * result + (open ? 1231 : 1237);
            result = prime * result + (open2 ? 1231 : 1237);
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
            VisitCheck other = (VisitCheck) obj;
            if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
                return false;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            if (name2 == null) {
                if (other.name2 != null)
                    return false;
            } else if (!name2.equals(other.name2))
                return false;
            if (minutesLeft != other.minutesLeft)
                return false;
            if (open != other.open)
                return false;
            if (open2 != other.open2)
                return false;
            return true;
        }

        private Day16 getEnclosingInstance() {
            return Day16.this;
        }

    }

    private int part1() {
        var maxTotalRate = 0;
        var root = new NodePart1(valves.get("AA"), null, 29, new ArrayList<>(), 0, 0);
        PriorityQueue<NodePart1> queue = new PriorityQueue<>((a, b) -> b.totalRate - a.totalRate);
        queue.add(root);
        Map<VisitCheck, Integer> visited = new HashMap<>();
        NodePart1 node = queue.peek();
        NodePart1 maxNode = null;

        while (!queue.isEmpty()) {
            node = queue.poll();
            visited.put(new VisitCheck(node.valve.name, node.minutesLeft, node.openValves.contains(node.valve)),
                    node.totalRate);
            if (node.totalRate > maxTotalRate) {
                maxNode = node;
                maxTotalRate = node.totalRate;
            }

            for (NodePart1 child : generateChildrenPart1(node)) {
                var visitCheck = new VisitCheck(child.valve.name, child.minutesLeft,
                        child.openValves.contains(child.valve));
                if (!visited.containsKey(visitCheck) || visited.get(visitCheck) < child.totalRate) {
                    queue.add(child);
                }
            }

        }

        System.out.println(maxNode.minutesLeft);
        System.out.println(maxNode.path());
        return maxTotalRate;

    }

    private int part2() {
        var maxTotalRate = 0;
        var root = new NodePart2(valves.get("AA"), valves.get("AA"), null, 25, new ArrayList<>(), 0, 0);
        PriorityQueue<NodePart2> queue = new PriorityQueue<>((a, b) -> b.totalRate - a.totalRate);
        queue.add(root);
        Map<VisitCheck, Integer> visited = new HashMap<>();
        NodePart2 node = queue.peek();
        NodePart2 maxNode = null;

        while (!queue.isEmpty()) {
            node = queue.poll();
            visited.put(
                    new VisitCheck(node.ownValve.name, node.elephantValve.name, node.minutesLeft,
                            node.openValves.contains(node.ownValve), node.openValves.contains(node.elephantValve)),
                    node.totalRate);
            if (node.totalRate > maxTotalRate) {
                maxNode = node;
                maxTotalRate = node.totalRate;
            }

            for (NodePart2 child : generateChildrenPart2(node)) {
                var visitCheck = new VisitCheck(child.ownValve.name, child.elephantValve.name, child.minutesLeft,
                        child.openValves.contains(child.ownValve), child.openValves.contains(child.elephantValve));
                if (!visited.containsKey(visitCheck) || visited.get(visitCheck) < child.totalRate) {
                    queue.add(child);
                }
            }

        }

        System.out.println(maxNode.minutesLeft);
        System.out.println(maxNode.path());
        return maxTotalRate;
    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        List<String> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }

        System.out.println("Part 1: " + new Day16(data).part1());
        System.out.println("Part 2: " + new Day16(data).part2());

        scanner.close();
    }
}