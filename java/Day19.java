import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day19 {
    enum ResourceType {
        Ore, Clay, Obsidian, Geode
    }

    class Robot {
        ResourceType generates;
        Map<ResourceType, Integer> costs;

        Robot(ResourceType generates, Map<ResourceType, Integer> costs) {
            this.generates = generates;
            this.costs = costs;
        }
    }

    class Blueprint {
        int id;
        List<Robot> robots = new ArrayList<>();
        Map<ResourceType, Integer> maxes = new HashMap<>();

        int maxGeodes(int minutes) {
            var startGathering = new HashMap<ResourceType, Integer>();
            startGathering.put(ResourceType.Ore, 1);
            var root = new Node(minutes, new HashMap<>(), startGathering, this, new HashMap<>());
            currentMax = 0;
            return root.maxGeodes();
        }

        int max(ResourceType resource) {
            if (!maxes.containsKey(resource)) {
                maxes.put(resource, robots.stream().mapToInt(r -> r.costs.getOrDefault(resource, 0)).max().orElse(0));
            }

            return maxes.get(resource);

        }

        void calculcateMaxes() {
            maxes = new HashMap<>();
        }

        int qualityLevel() {
            return id * maxGeodes(24);
        }
    }

    class State {
        final int minute;
        final Map<ResourceType, Integer> resources;
        final Map<ResourceType, Integer> robots;

        State(int minute, Map<ResourceType, Integer> resources, Map<ResourceType, Integer> robots) {
            this.minute = minute;
            this.resources = resources;
            this.robots = robots;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getEnclosingInstance().hashCode();
            result = prime * result + minute;
            result = prime * result + ((resources == null) ? 0 : resources.hashCode());
            result = prime * result + ((robots == null) ? 0 : robots.hashCode());
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
            if (resources == null) {
                if (other.resources != null)
                    return false;
            } else if (!resources.equals(other.resources))
                return false;
            if (robots == null) {
                if (other.robots != null)
                    return false;
            } else if (!robots.equals(other.robots))
                return false;
            return true;
        }

        private Day19 getEnclosingInstance() {
            return Day19.this;
        }

    }

    static int currentMax = 0;

    class Node {
        final int minute;
        final Map<ResourceType, Integer> resources;
        final Map<ResourceType, Integer> robots;
        final Blueprint blueprint;
        final Map<State, Integer> visited;
        final State state;

        Node(int minute, Map<ResourceType, Integer> resources, Map<ResourceType, Integer> gathering,
                Blueprint blueprint, Map<State, Integer> visited) {
            this.minute = minute;
            this.resources = resources;
            this.robots = gathering;
            this.blueprint = blueprint;
            this.visited = visited;
            this.state = state();
        }

        int maxGeodes() {
            if (visited.keySet().contains(state))
                return visited.get(state);

            if (minute == 0) {
                var value = amountOf(ResourceType.Geode);
                visited.put(state(), value);
                currentMax = Math.max(currentMax, value);
                return value;
            } else {
                var value = generateChildren().stream().mapToInt(Node::maxGeodes).max().orElse(0);
                visited.put(state(), value);
                currentMax = Math.max(currentMax, value);
                return value;
            }
        }

        State state() {
            return new State(minute, resources, robots);
        }

        int amountOf(ResourceType resource) {
            return resources.getOrDefault(resource, 0);
        }

        List<Node> generateChildren() {
            var children = new ArrayList<Node>();

            var maxPossible = amountOf(ResourceType.Geode);
            var nrGeodeRobots = robots.getOrDefault(ResourceType.Geode, 0);
            for (var i = minute; i > 0; i--) {
                maxPossible += nrGeodeRobots;
                nrGeodeRobots++;
            }

            if (maxPossible < currentMax)
                return children;

            for (var robot : blueprint.robots) {
                if (robot.generates != ResourceType.Geode) {
                    var generatingAmount = robots.getOrDefault(robot.generates, 0);
                    var maxNeeded = blueprint.max(robot.generates);
                    if (generatingAmount >= maxNeeded)
                        continue;
                }

                var newResources = new HashMap<>(resources);
                var newRobots = new HashMap<>(robots);
                var newMinute = minute - 1;
                var canProduce = true;
                for (var cost : robot.costs.entrySet()) {
                    var amount = newResources.getOrDefault(cost.getKey(), 0);
                    if (amount < cost.getValue()) {
                        canProduce = false;
                        break;
                    }
                    newResources.put(cost.getKey(), amount - cost.getValue());
                }
                if (!canProduce)
                    continue;

                for (var resource : robots.entrySet()) {
                    var amount = newResources.getOrDefault(resource.getKey(), 0);
                    newResources.put(resource.getKey(), amount + resource.getValue());
                }

                var amountGathering = newRobots.getOrDefault(robot.generates, 0);
                newRobots.put(robot.generates, amountGathering + 1);

                children.add(new Node(newMinute, newResources, newRobots, blueprint, visited));
            }

            for (var robot : blueprint.robots) {
                var canWait = true;
                for (var cost : robot.costs.entrySet()) {
                    if (!robots.containsKey(cost.getKey())
                            || resources.getOrDefault(cost.getKey(), 0) >= cost.getValue()) {
                        canWait = false;
                        break;
                    }
                }

                if (canWait && minute > 1) {
                    var minutesToWait = 0;

                    for (var cost : robot.costs.entrySet()) {
                        var generating = robots.get(cost.getKey());
                        var amount = resources.getOrDefault(cost.getKey(), 0);
                        var needed = cost.getValue();
                        var wait = (int) Math.ceil((double) (needed - amount) / generating);
                        minutesToWait = Math.max(minutesToWait, wait);
                    }

                    var newMinute = minute - minutesToWait;
                    if (newMinute < 0)
                        continue;
                    var newResources = new HashMap<>(resources);
                    var newRobots = new HashMap<>(robots);

                    for (var resource : robots.entrySet()) {
                        var amount = newResources.getOrDefault(resource.getKey(), 0);
                        newResources.put(resource.getKey(), amount + (resource.getValue() *
                                minutesToWait));
                    }

                    children.add(new Node(newMinute, newResources, newRobots, blueprint, visited));

                }

            }

            return children;
        }
    }

    List<Blueprint> blueprints = new ArrayList<>();

    public Day19(List<String> input) {
        for (var blueprintString : input) {
            var splitted = blueprintString.split(" ");
            Blueprint blueprint = new Blueprint();
            blueprint.id = Integer.parseInt(splitted[1].replace(":", ""));
            var costs = new HashMap<ResourceType, Integer>();
            costs.put(ResourceType.Ore, Integer.parseInt(splitted[6]));
            blueprint.robots.add(new Robot(ResourceType.Ore, costs));

            costs = new HashMap<>();
            costs.put(ResourceType.Ore, Integer.parseInt(splitted[12]));
            blueprint.robots.add(new Robot(ResourceType.Clay, costs));

            costs = new HashMap<>();
            costs.put(ResourceType.Ore, Integer.parseInt(splitted[18]));
            costs.put(ResourceType.Clay, Integer.parseInt(splitted[21]));
            blueprint.robots.add(new Robot(ResourceType.Obsidian, costs));

            costs = new HashMap<>();
            costs.put(ResourceType.Ore, Integer.parseInt(splitted[27]));
            costs.put(ResourceType.Obsidian, Integer.parseInt(splitted[30]));
            blueprint.robots.add(new Robot(ResourceType.Geode, costs));

            blueprints.add(blueprint);
        }
    }

    private int part1() {
        return blueprints.stream().mapToInt(Blueprint::qualityLevel).sum();
    }

    private int part2() {
        return blueprints.stream().limit(3).mapToInt((b) -> b.maxGeodes(32)).reduce(1, (a, b) -> a * b);
    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        List<String> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }

        System.out.println("Part 1: " + new Day19(data).part1());
        System.out.println("Part 2: " + new Day19(data).part2());

        scanner.close();
    }
}