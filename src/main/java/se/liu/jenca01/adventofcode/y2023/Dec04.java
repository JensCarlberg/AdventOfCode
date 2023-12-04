package se.liu.jenca01.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec04 extends Christmas {

    long sampleAnswer1 = 13;
    long sampleAnswer2 = 0;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec04();
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

    private List<Card> convertData(Stream<String> data) {
        return data
                .map(l -> parseCard(l))
                .collect(Collectors.toList());
    }

    private Card parseCard(String line) {
        var cardParts = line.split(":");
        var cardNo = getCardNo(cardParts[0]);
        var winners = parseInts(cardParts[1].split("\\|")[0]);
        var numbers = parseInts(cardParts[1].split("\\|")[1]);

        return new Card(cardNo, winners, numbers);
    }

    private int getCardNo(String cardInfo) {
        while (cardInfo.contains("  "))
            cardInfo = cardInfo.replace("  ", " ");
        return Integer.parseInt(cardInfo.split(" ")[1]);
    }

    private List<Integer> parseInts(String nums) {
        return Arrays.stream(nums.split(" "))
                .filter(s -> s.length() != 0)
                .map(s -> Integer.valueOf(s))
                .toList();
    }

    public long solve1(Stream<String> stream) {
        var cards = convertData(stream);

        return cards.stream().mapToInt(card -> calcScore(card)).sum();
    }

    private int calcScore(Card card) {
        int[] winningPoints = new int [] {0, 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024 };

        var winnerCount = (int) card.numbers.stream().filter(n -> card.winners.contains(n)).count();
        return winningPoints[winnerCount];
    }

    public long solve2(Stream<String> stream) {
        return 0;
    }

    record Card(int no, List<Integer> winners, List<Integer> numbers) {}
}
