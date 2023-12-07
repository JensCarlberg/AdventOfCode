package se.liu.jenca01.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec07 extends Christmas {

    final char T = (char) ('9' + 1);
    final char J = (char) ('9' + 2);
    final char Q = (char) ('9' + 3);
    final char K = (char) ('9' + 4);
    final char A = (char) ('9' + 5);

    long sampleAnswer1 = 6440;
    long sampleAnswer2 = 5905;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec07();
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

    private List<Hand> convertData(Stream<String> data, boolean useJoker) {
        return data.map(s -> parseHand(s, useJoker)).toList();
    }

    private Hand parseHand(String line, boolean useJoker) {
        var parts = line.split(" ");
        var cards = parseCards(parts[0], useJoker);
        var type = parseType(cards, useJoker);
        var bid = Long.parseLong(parts[1]);
        return new Hand(type, parts[0], cards[0], cards[1], cards[2], cards[3], cards[4], bid);
    }

    private char[] parseCards(String cards, boolean useJoker) {
        var betterCards = cards
                .replace('T', T)
                .replace('J', useJoker ? '1' : J)
                .replace('Q', Q)
                .replace('K', K)
                .replace('A', A);

        return betterCards.toCharArray();
    }

    private Type parseType(char[] cards, boolean useJoker) {
        var cardValues = new TreeMap<Character, Integer>();
        for(var c: cards)
            cardValues.put(c, plusOne(cardValues, c));

        if (useJoker)
            useJoker(cardValues);

        switch (cardValues.size()) {
        case 1: return Type.FIVE_OF_A_KIND;
        case 2: return analyze2(cardValues);
        case 3: return analyze3(cardValues);
        case 4: return Type.ONE_PAIR;
        case 5: return Type.HIGH_CARD;
        }

        throw new RuntimeException();
    }

    private void useJoker(TreeMap<Character, Integer> cardValues) {
        if (cardValues.size() == 1)
            return;
        if (!cardValues.containsKey('1'))
            return;

        int jokerCount = cardValues.remove('1');
        int maxCount = 0;
        char strongestCard = '0';
        for (var c: cardValues.keySet()) {
            if (cardValues.get(c) > maxCount) {
                maxCount = cardValues.get(c);
                strongestCard = c;
            } else if (cardValues.get(c) == maxCount && c > strongestCard)
                strongestCard = c;
        }
        cardValues.put(strongestCard, cardValues.get(strongestCard) + jokerCount);
    }

    private Type analyze2(TreeMap<Character, Integer> cardValues) {
        var maxCount = cardValues.values().stream().mapToInt(v -> v).max().getAsInt();
        if (maxCount == 4)
            return Type.FOUR_OF_A_KIND;
        return Type.FULL_HOUSE;
    }

    private Type analyze3(TreeMap<Character, Integer> cardValues) {
        var maxCount = cardValues.values().stream().mapToInt(v -> v).max().getAsInt();
        if (maxCount == 3)
            return Type.THREE_OF_A_KIND;
        return Type.TWO_PAIR;
    }

    private int plusOne(TreeMap<Character, Integer> cardValues, char c) {
        if (cardValues.containsKey(c))
            return cardValues.get(c) + 1;
        return 1;
    }

    public long solve1(Stream<String> stream) {
        var hands = convertData(stream, false);
        return solve12(hands);
    }

    private long solve12(List<Hand> hands) {
        var sortedHands = new TreeSet<>(hands);
        long winnings = 0;
        int handCount = 1;
        for (var hand: sortedHands)
            winnings += hand.bid * handCount++;
        return winnings;
    }

    public long solve2(Stream<String> stream) {
        var hands = convertData(stream, true);
        return solve12(hands);
    }

    enum Type {
        HIGH_CARD(1),
        ONE_PAIR(2),
        TWO_PAIR(3),
        THREE_OF_A_KIND(4),
        FULL_HOUSE(5),
        FOUR_OF_A_KIND(6),
        FIVE_OF_A_KIND(7)
        ;

        final int type;
        Type(int type) {
            this.type = type;
        }
    }
    record Hand(Type type, String origHand, char card0, char card1, char card2, char card3, char card4, long bid) implements Comparable<Hand> {

        @Override
        public int compareTo(Hand o) {
            return Objects.compare(this, o,
                    Comparator.comparing(Hand::type)
                    .thenComparing(Hand::card0)
                    .thenComparing(Hand::card1)
                    .thenComparing(Hand::card2)
                    .thenComparing(Hand::card3)
                    .thenComparing(Hand::card4)
                    );
        }
    }
}
