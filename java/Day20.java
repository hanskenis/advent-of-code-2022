import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Collectors;

public class Day20 {

    List<Integer> numbers = new ArrayList<>();

    public Day20(List<String> input) {
        for (var line : input) {
            numbers.add(Integer.parseInt(line));
        }
    }

    long mix(long key, int mixTimes) {
        var encrypted = numbers.stream().map(n -> n * key).collect(Collectors.toList());

        var mixingList = new ArrayList<SimpleEntry<Integer, Long>>();
        for (var i = 0; i < encrypted.size(); i++) {
            mixingList.add(new SimpleEntry<Integer, Long>(i, encrypted.get(i)));
        }

        var size = encrypted.size();
        for (var mixTime = 0; mixTime < mixTimes; mixTime++) {
            for (var i = 0; i < size; i++) {
                var number = new SimpleEntry<>(i, encrypted.get(i));

                var index = mixingList.indexOf(number);

                var newIndex = index + number.getValue();
                newIndex = newIndex % (size - 1);
                newIndex = newIndex <= 0 ? newIndex + size - 1 : newIndex;

                mixingList.remove(index);
                mixingList.add((int) newIndex, number);
            }
        }

        var zero = mixingList.stream().filter(n -> n.getValue() == 0).findFirst().get();
        var index = mixingList.indexOf(zero);

        var nr1 = mixingList.get((index + 1000) % size).getValue();
        var nr2 = mixingList.get((index + 2000) % size).getValue();
        var nr3 = mixingList.get((index + 3000) % size).getValue();
        return nr1 + nr2 + nr3;

    }

    private long part1() {
        return mix(1, 1);
    }

    private long part2() {
        return mix(811589153, 10);
    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        List<String> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }

        System.out.println("Part 1: " + new Day20(data).part1());
        System.out.println("Part 2: " + new Day20(data).part2());

        scanner.close();
    }
}