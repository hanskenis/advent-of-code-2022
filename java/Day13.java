import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day13 {

    class PacketData {
        List<PacketData> packets;
        Integer value;

        public PacketData(int value) {
            this.value = value;
        }

        public PacketData(List<PacketData> packets) {
            this.packets = packets;
        }

        @Override
        public String toString() {
            if (value != null) {
                return value.toString();
            } else {
                return "[" + packets.stream().map(p -> p.toString()).collect(Collectors.joining(",")) + "]";
            }
        }

    }

    public PacketData fromStringBuilder(StringBuilder input) {
        if (input.charAt(0) == '[') {
            input.delete(0, 1);
            List<PacketData> packets = new ArrayList<>();
            while (input.charAt(0) != ']') {
                packets.add(fromStringBuilder(input));
            }
            input.deleteCharAt(0);
            if (input.length() > 0 && input.charAt(0) == ',') {
                input.delete(0, 1);
            }
            return new PacketData(packets);

        } else {
            var index = 0;
            while (input.charAt(index) != ',' && input.charAt(index) != ']') {
                index++;
            }
            var sub = input.substring(0, index);

            input.delete(0, index);
            if (input.charAt(0) == ',') {
                input.delete(0, 1);
            }
            return new PacketData(Integer.parseInt(sub));
        }
    }

    public PacketData fromString(String input) {
        return fromStringBuilder(new StringBuilder(input));
    }

    List<PacketData> packets = new ArrayList<>();

    public Day13(List<String> input) {
        for (String line : input) {
            if (!line.isEmpty()) {
                packets.add(fromString(line));
            }
        }
    }

    private int compare(int a, int b) {
        return a - b;
    }

    private int compare(List<PacketData> a, List<PacketData> b) {
        for (int i = 0; i < a.size(); i++) {
            if (i >= b.size()) {
                return 1;
            }
            var comp = compare(a.get(i), b.get(i));
            if (comp != 0) {
                return comp;
            }
        }

        if (a.size() == b.size())
            return 0;
        return -1;
    }

    private int compare(PacketData a, PacketData b) {
        if (a.value != null && b.value != null) {
            return compare(a.value, b.value);
        } else if (a.packets != null && b.packets != null) {
            return compare(a.packets, b.packets);
        } else if (a.value != null) {
            var aList = new ArrayList<PacketData>();
            aList.add(a);
            return compare(aList, b.packets);
        } else {
            var bList = new ArrayList<PacketData>();
            bList.add(b);
            return compare(a.packets, bList);
        }
    }

    private int part1() {
        var indexes = new ArrayList<Integer>();

        for (int i = 0; i < packets.size() / 2; i++) {
            var comp = compare(packets.get(i * 2), packets.get(i * 2 + 1));
            if (comp < 0)
                indexes.add(i + 1);
        }

        return indexes.stream().mapToInt(i -> i).sum();
    }

    private int part2() {
        packets.add(fromString("[[2]]"));
        packets.add(fromString("[[6]]"));
        var sorted = packets.stream().sorted((a, b) -> compare(a, b)).collect(Collectors.toList());
        var index1 = 0;
        var index2 = 0;
        for (int i = 0; i < sorted.size(); i++) {
            if (sorted.get(i).toString().equals("[[2]]")) {
                index1 = i + 1;
            }
            if (sorted.get(i).toString().equals("[[6]]")) {
                index2 = i + 1;
            }
        }

        return index1 * index2;
    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        List<String> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }

        var day = new Day13(data);

        System.out.println("Part 1: " + day.part1());
        System.out.println("Part 2: " + day.part2());

        scanner.close();
    }
}