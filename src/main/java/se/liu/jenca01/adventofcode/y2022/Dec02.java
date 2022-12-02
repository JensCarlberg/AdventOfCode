package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.stream.Stream;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec02 extends Christmas {

    enum RPS {
        ROCK,
        PAPER,
        SCISSORS
    }
    static class Round {
        private RPS elf;
        private RPS you;

        static class Builder {
            static Round build(String line, int puzzlePart) {
                var parts = line.split(" ");
                var elf = elf(parts[0]);
                var you = you(parts[1], puzzlePart, elf);
                return new Round(elf, you);
            }

            private static RPS elf(String selected) {
                switch (selected) {
                    case "A": return RPS.ROCK;
                    case "B": return RPS.PAPER;
                    case "C": return RPS.SCISSORS;
                }
                throw new RuntimeException();
            }

            private static RPS you(String selected, int puzzlePart, RPS elf) {
                if (puzzlePart == 1) {
                    switch (selected) {
                        case "X": return RPS.ROCK;
                        case "Y": return RPS.PAPER;
                        case "Z": return RPS.SCISSORS;
                    }
                    throw new RuntimeException();
                }
                switch (selected) {
                    case "X": return elf == RPS.ROCK ? RPS.SCISSORS : elf == RPS.PAPER ? RPS.ROCK : RPS.PAPER; //lose
                    case "Y": return elf;
                    case "Z": return elf == RPS.ROCK ? RPS.PAPER : elf == RPS.PAPER ? RPS.SCISSORS : RPS.ROCK; // win return RPS.SCISSORS;
                }
                throw new RuntimeException();
            }
        }

        Round(RPS elf, RPS you) {
            this.elf = elf;
            this.you = you;
        }

        int score() {
            return winLoseDraw() + selectionScore();
        }

        private int winLoseDraw() {
            if (you == elf)
                return 3;
            if (you == RPS.ROCK && elf == RPS.SCISSORS || you == RPS.PAPER && elf == RPS.ROCK|| you == RPS.SCISSORS && elf == RPS.PAPER )
                return 6;
            return 0;
        }

        private int selectionScore() {
            switch (you) {
                case PAPER: return 2;
                case ROCK: return 1;
                case SCISSORS: return 3;
                default: throw new RuntimeException();
            }
        }
    }
    long sampleAnswer1 = 15;
    long sampleAnswer2 = 12;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec02();
        christmas.solveSample();
        christmas.solveMy();
    }

    @Override
    public void solveSample() throws Exception {
        long start = System.currentTimeMillis();
        long solve1 = solve1(sampleData());
        long stop = System.currentTimeMillis();
        System.out.println("Example solve1 timing: " + (stop-start) + "ms");
        assertEquals(sampleAnswer1, solve1, "solve1 is expected to return " + sampleAnswer1);

        start = System.currentTimeMillis();
        long solve2 = solve2(sampleData());
        stop = System.currentTimeMillis();
        System.out.println("Example solve2 timing: " + (stop-start) + "ms");
        assertEquals(sampleAnswer2, solve2, "solve2 is expected to return " + sampleAnswer2);
    }

    @Override
    public void solveMy() throws Exception {
        System.out.println(simpleClassName() + " solve1: " + solve1(myData()));
        System.out.println(simpleClassName() + " solve2: " + solve2(myData()));
    }

    public long solve1(Stream<String> stream) {
        var lines = stream.toList();
        int totalScore = 0;
        for (var line: lines) {
            var round = Round.Builder.build(line, 1);
            totalScore += round.score();
        }
        return totalScore;
    }

    public long solve2(Stream<String> stream) {
        var lines = stream.toList();
        int totalScore = 0;
        for (var line: lines) {
            var round = Round.Builder.build(line, 2);
            totalScore += round.score();
        }
        return totalScore;
    }
}

/*



*/