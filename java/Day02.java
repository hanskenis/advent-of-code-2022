import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day02 {
    enum RPS {
        R {
            @Override
            RPS winningOpponent() {
                return P;
            }

            @Override
            RPS losingOpponent() {
                return S;
            }
        },
        P {
            @Override
            RPS winningOpponent() {
                return S;
            }

            @Override
            RPS losingOpponent() {
                return R;
            }

        },
        S {
            @Override
            RPS winningOpponent() {
                return R;
            }

            @Override
            RPS losingOpponent() {
                return P;
            }
        };

        abstract RPS winningOpponent();

        abstract RPS losingOpponent();
    }

    class Round {
        public RPS player1;
        public RPS player2;

        Round(RPS player1, RPS player2) {
            this.player1 = player1;
            this.player2 = player2;
        }

        public int score() {
            return shapeScore() + roundScore();
        }

        public int roundScore() {
            if (player2Lost())
                return 0;
            if (draw())
                return 3;

            return 6;

        }

        public boolean player2Lost() {
            return player2 == player1.losingOpponent();
        }

        public boolean draw() {
            return player1 == player2;
        }

        public int shapeScore() {
            switch (player2) {
                case R:
                    return 1;
                case P:
                    return 2;
                case S:
                    return 3;

            }
            return 0;
        }
    }

    List<Round> strategyPart1;
    List<Round> strategyPart2;

    public Day02(List<String> input) {
        strategyPart1 = input.stream().map((l) -> roundPart1(l)).collect(Collectors.toList());
        strategyPart2 = input.stream().map((l) -> roundPart2(l)).collect(Collectors.toList());
    }

    Round roundPart1(String line) {
        var splitted = line.split(" ");
        RPS player1 = RPS.R;
        RPS player2 = RPS.R;
        switch (splitted[0]) {
            case "A":
                player1 = RPS.R;
                break;
            case "B":
                player1 = RPS.P;
                break;
            case "C":
                player1 = RPS.S;
                break;
        }
        switch (splitted[1]) {
            case "X":
                player2 = RPS.R;
                break;
            case "Y":
                player2 = RPS.P;
                break;
            case "Z":
                player2 = RPS.S;
                break;
        }
        return new Round(player1, player2);
    }

    Round roundPart2(String line) {
        var splitted = line.split(" ");
        RPS player1 = RPS.R;
        RPS player2 = RPS.R;
        switch (splitted[0]) {
            case "A":
                player1 = RPS.R;
                break;
            case "B":
                player1 = RPS.P;
                break;
            case "C":
                player1 = RPS.S;
                break;
        }
        switch (splitted[1]) {
            case "X":
                player2 = player1.losingOpponent();
                break;
            case "Y":
                player2 = player1;
                break;
            case "Z":
                player2 = player1.winningOpponent();
                break;
        }
        return new Round(player1, player2);
    }

    private int part1() {
        return strategyPart1.stream().mapToInt(Round::score).sum();
    }

    private int part2() {
        return strategyPart2.stream().mapToInt(Round::score).sum();
    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        List<String> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }

        var day = new Day02(data);

        System.out.println("Part 1: " + day.part1());
        System.out.println("Part 2: " + day.part2());

        scanner.close();
    }
}
