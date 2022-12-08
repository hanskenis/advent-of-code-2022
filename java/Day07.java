import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day07 {
    class TreeNode {
        int size;
        String name;
        List<TreeNode> children;
        TreeNode parent;
        int totalSize;

        public TreeNode(int size, String name) {
            this.size = size;
            this.totalSize = size;
            this.name = name;
            this.children = new ArrayList<>();
        }

        public void addChild(TreeNode child) {
            this.children.add(child);
            child.parent = this;
        }

        public int totalSize() {
            if (totalSize != 0)
                return totalSize;
            totalSize = this.children.stream().mapToInt(x -> x.totalSize()).sum() + this.size;
            return totalSize;
        }
    }

    TreeNode root = new TreeNode(0, "/");

    public Day07(List<String> input) {
        var currentNode = root;
        for (String line : input) {
            if (line.startsWith("$ cd /")) {
                continue;
            } else if (line.startsWith("$ ls")) {
                continue;
            } else if (line.startsWith("$ cd ..")) {
                currentNode = currentNode.parent;
            } else if (line.startsWith("$ cd")) {
                var name = line.substring(5);
                currentNode = currentNode.children.stream().filter(x -> x.name.equals(name)).findFirst()
                        .get();
            } else {
                var splitted = line.split(" ");
                if (splitted[0].equals("dir")) {
                    currentNode.addChild(new TreeNode(0, splitted[1]));
                } else {
                    currentNode.addChild(new TreeNode(Integer.parseInt(splitted[0]), splitted[1]));
                }
            }
        }
    }

    private TreeNode dirChild(TreeNode node) {
        for (TreeNode childNode : node.children) {
            if (childNode.totalSize == 0) {
                return childNode;
            }
        }
        return null;

    }

    private TreeNode nextNode(TreeNode node) {
        var currentNode = node;
        TreeNode dir = dirChild(node);
        while (dir != null) {
            currentNode = dir;
            dir = dirChild(currentNode);
        }

        return currentNode;
    }

    public void traverse() {
        var currentNode = nextNode(root);

        while (currentNode != root) {
            currentNode.totalSize();
            currentNode = currentNode.parent;
        }
    }

    private int part1() {
        traverse();
        System.out.println("Starting part 1...");

        var total = 0;
        ArrayDeque<TreeNode> nodesTodo = new ArrayDeque<TreeNode>();
        nodesTodo.add(root);
        List<TreeNode> nodesDone = new ArrayList<TreeNode>();
        while (!nodesTodo.isEmpty()) {
            var currentNode = nodesTodo.pop();
            var nodeTotal = currentNode.totalSize();
            if (nodeTotal > 0 && nodeTotal <= 100000 && currentNode.size == 0) {
                total += nodeTotal;
            }
            nodesDone.add(currentNode);
            for (var child : currentNode.children) {
                if (!nodesDone.contains(child)) {
                    nodesTodo.add(child);
                }
            }
        }

        return total;

    }

    private int part2() {
        var min = Integer.MAX_VALUE;
        List<TreeNode> nodesTodo = new ArrayList<TreeNode>();
        nodesTodo.add(root);
        List<TreeNode> nodesDone = new ArrayList<TreeNode>();
        var unusedSpace = 70000000 - root.totalSize();

        while (!nodesTodo.isEmpty()) {
            var currentNode = nodesTodo.remove(0);
            var nodeTotal = currentNode.totalSize();
            if (unusedSpace + nodeTotal >= 30000000 && currentNode.size == 0) {
                min = Math.min(min, nodeTotal);
            }
            nodesDone.add(currentNode);

            for (var child : currentNode.children) {
                if (!nodesDone.contains(child)) {
                    nodesTodo.add(child);
                }
            }
        }

        return min;

    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        List<String> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }

        var day = new Day07(data);

        System.out.println("Part 1: " + day.part1());
        System.out.println("Part 2: " + day.part2());

        scanner.close();
    }
}