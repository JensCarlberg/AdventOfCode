package se.liu.jenca01.adventofcode.y2021;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec08 extends Christmas {

    long sampleAnswer1 = 26;
    long sampleAnswer2 = 61229;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec08();
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

    private List<String> convertDataSolve1(Stream<String> data) {
        var lines = toList(data);
        var out = new ArrayList<String>();
        for (var line: lines)
            for(var part: line.split("\\|")[1].split(" "))
                out.add(part);
        return out;
    }

    private List<Pair<String[], String[]>> convertDataSolve2(Stream<String> data) {
        var lines = toList(data);
        var out = new ArrayList<Pair<String[], String[]>>();
        for (var line: lines) {
            var lineParts = line.split("\\|");
            out.add(makePair(lineParts[0], lineParts[1]));
        }
        return out;
    }

    private Pair<String[], String[]> makePair(String codes, String results) {
        var left = Stream.of(codes.split(" ")).filter(s -> s.length() > 1).collect(Collectors.toList()).toArray(new String[10]);
        var right = Stream.of(results.split(" ")).filter(s -> s.length() > 1).collect(Collectors.toList()).toArray(new String[4]);
        return new ImmutablePair<String[], String[]>(left, right);
    }

    public long solve1(Stream<String> stream) {
        var data = convertDataSolve1(stream);
        var count = 0;
        for (var d: data) {
            int l = d.trim().length();
            if (l == 2 || l == 3 || l == 4 || l == 7)
                count++;
        }
        return count;
    }

    public long solve2(Stream<String> stream) {
        var data = convertDataSolve2(stream);
        var sum = 0L;
        for (var row: data) {
            String[] left = row.getLeft();
            var codes = mapNumbers(row, left);
            int nums = 0;
            for(var num: row.getRight())
                for(int i=0; i<10; i++)
                    if (codes[i].length() == num.length() && common(codes[i], num, num.length())) {
                        nums = nums*10 + i;
                        break;
                    }
            sum += nums;
        }
        return sum;
    }

    private String[] mapNumbers(Pair<String[], String[]> row, String[] left) {
        var codes = new String[10];
        codes[1] = find1(left);
        codes[4] = find4(row.getLeft());
        codes[7] = find7(row.getLeft());
        codes[8] = find8(row.getLeft());
        codes[6] = find6(row.getLeft(), codes[1]);
        codes[9] = find9(row.getLeft(), codes[4]);
        codes[0] = find0(row.getLeft(), codes[6], codes[9]);
        codes[2] = find2(row.getLeft(), codes[4]);
        codes[3] = find3(row.getLeft(), codes[2], codes[7]);
        codes[5] = find5(row.getLeft(), codes[2], codes[3]);
        return codes;
    }

    private String find0(String[] left, String code6, String code9) {
        return Stream.of(left)
                .filter(s -> s.length() == 6)
                .filter(s -> !s.equals(code9) && !s.equals(code6))
                .findFirst().get();
    }

    private String find1(String[] left) {
        return Stream.of(left)
                .filter(s -> s.length() == 2)
                .findFirst().get();
    }

    private String find2(String[] left, String code4) {
        return Stream.of(left)
                .filter(s -> s.length() == 5)
                .filter(s -> common(s, code4, 2))
                .findFirst().get();
    }

    private String find3(String[] left, String code2, String code7) {
        return Stream.of(left)
                .filter(s -> s.length() == 5)
                .filter(s -> !s.equals(code2))
                .filter(s -> common(s, code7, 3))
                .findFirst().get();
    }

    private String find4(String[] left) {
        return Stream.of(left)
                .filter(s -> s.length() == 4)
                .findFirst().get();
    }

    private String find5(String[] left, String code2, String code3) {
        return Stream.of(left)
                .filter(s -> s.length() == 5)
                .filter(s -> !s.equals(code2) && !s.equals(code3))
                .findFirst().get();
    }

    private String find6(String[] left, String code1) {
        return Stream.of(left)
                .filter(s -> s.length() == 6)
                .filter(s -> common(s, code1, 1))
                .findFirst().get();
    }

    private String find7(String[] left) {
        return Stream.of(left)
                .filter(s -> s.length() == 3)
                .findFirst().get();
    }

    private String find8(String[] left) {
        return Stream.of(left)
                .filter(s -> s.length() == 7)
                .findFirst().get();
    }

    private String find9(String[] left, String code4) {
        return Stream.of(left)
                .filter(s -> s.length() == 6)
                .filter(s -> common(s, code4, 4))
                .findFirst().get();
    }

    private boolean common(String code, String code4, int targetCommonCount) {
        var codeChars = code.toCharArray();
        var commonCount = 0;
        for(var c: codeChars)
            if (code4.contains(""+c))
                commonCount++;
        return commonCount == targetCommonCount;
    }
}

/*

--- Day 8: Seven Segment Search ---
You barely reach the safety of the cave when the whale smashes into the cave mouth, collapsing it. Sensors indicate another exit to this cave at a much greater depth, so you have no choice but to press on.

As your submarine slowly makes its way through the cave system, you notice that the four-digit seven-segment displays in your submarine are malfunctioning; they must have been damaged during the escape. You'll be in a lot of trouble without them, so you'd better figure out what's wrong.

Each digit of a seven-segment display is rendered by turning on or off any of seven segments named a through g:

  0:      1:      2:      3:      4:
 aaaa    ....    aaaa    aaaa    ....
b    c  .    c  .    c  .    c  b    c
b    c  .    c  .    c  .    c  b    c
 ....    ....    dddd    dddd    dddd
e    f  .    f  e    .  .    f  .    f
e    f  .    f  e    .  .    f  .    f
 gggg    ....    gggg    gggg    ....

  5:      6:      7:      8:      9:
 aaaa    aaaa    aaaa    aaaa    aaaa
b    .  b    .  .    c  b    c  b    c
b    .  b    .  .    c  b    c  b    c
 dddd    dddd    ....    dddd    dddd
.    f  e    f  .    f  e    f  .    f
.    f  e    f  .    f  e    f  .    f
 gggg    gggg    ....    gggg    gggg

So, to render a 1, only segments c and f would be turned on; the rest would be off. To render a 7, only segments a, c, and f would be turned on.

The problem is that the signals which control the segments have been mixed up on each display. The submarine is still trying to display numbers by producing output on signal wires a through g, but those wires are connected to segments randomly. Worse, the wire/segment connections are mixed up separately for each four-digit display! (All of the digits within a display use the same connections, though.)

So, you might know that only signal wires b and g are turned on, but that doesn't mean segments b and g are turned on: the only digit that uses two segments is 1, so it must mean segments c and f are meant to be on. With just that information, you still can't tell which wire (b/g) goes to which segment (c/f). For that, you'll need to collect more information.

For each display, you watch the changing signals for a while, make a note of all ten unique signal patterns you see, and then write down a single four digit output value (your puzzle input). Using the signal patterns, you should be able to work out which pattern corresponds to which digit.

For example, here is what you might see in a single entry in your notes:

acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab |
cdfeb fcadb cdfeb cdbaf
(The entry is wrapped here to two lines so it fits; in your notes, it will all be on a single line.)

Each entry consists of ten unique signal patterns, a | delimiter, and finally the four digit output value. Within an entry, the same wire/segment connections are used (but you don't know what the connections actually are). The unique signal patterns correspond to the ten different ways the submarine tries to render a digit using the current wire/segment connections. Because 7 is the only digit that uses three segments, dab in the above example means that to render a 7, signal lines d, a, and b are on. Because 4 is the only digit that uses four segments, eafb means that to render a 4, signal lines e, a, f, and b are on.

Using this information, you should be able to work out which combination of signal wires corresponds to each of the ten digits. Then, you can decode the four digit output value. Unfortunately, in the above example, all of the digits in the output value (cdfeb fcadb cdfeb cdbaf) use five segments and are more difficult to deduce.

For now, focus on the easy digits. Consider this larger example:

be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb |
fdgacbe cefdb cefbgd gcbe
edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec |
fcgedb cgb dgebacf gc
fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef |
cg cg fdcagb cbg
fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega |
efabcd cedba gadfec cb
aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga |
gecf egdcabf bgf bfgea
fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf |
gebdcfa ecba ca fadegcb
dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf |
cefg dcbef fcge gbcadfe
bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd |
ed bcgafe cdgba cbgef
egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg |
gbdfcae bgc cg cgb
gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc |
fgae cfgab fg bagce
Because the digits 1, 4, 7, and 8 each use a unique number of segments, you should be able to tell which combinations of signals correspond to those digits. Counting only digits in the output values (the part after | on each line), in the above example, there are 26 instances of digits that use a unique number of segments (highlighted above).

In the output values, how many times do digits 1, 4, 7, or 8 appear?


--- Part Two ---
Through a little deduction, you should now be able to determine the remaining digits. Consider again the first example above:

acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab |
cdfeb fcadb cdfeb cdbaf
After some careful analysis, the mapping between signal wires and segments only make sense in the following configuration:

 dddd
e    a
e    a
 ffff
g    b
g    b
 cccc
So, the unique signal patterns would correspond to the following digits:

acedgfb: 8
cdfbe: 5
gcdfa: 2
fbcad: 3
dab: 7
cefabd: 9
cdfgeb: 6
eafb: 4
cagedb: 0
ab: 1
Then, the four digits of the output value can be decoded:

cdfeb: 5
fcadb: 3
cdfeb: 5
cdbaf: 3
Therefore, the output value for this entry is 5353.

Following this same process for each entry in the second, larger example above, the output value of each entry can be determined:

fdgacbe cefdb cefbgd gcbe: 8394
fcgedb cgb dgebacf gc: 9781
cg cg fdcagb cbg: 1197
efabcd cedba gadfec cb: 9361
gecf egdcabf bgf bfgea: 4873
gebdcfa ecba ca fadegcb: 8418
cefg dcbef fcge gbcadfe: 4548
ed bcgafe cdgba cbgef: 1625
gbdfcae bgc cg cgb: 8717
fgae cfgab fg bagce: 4315
Adding all of the output values in this larger example produces 61229.

For each entry, determine all of the wire/segment connections and decode the four-digit output values. What do you get if you add up all of the output values?



*/