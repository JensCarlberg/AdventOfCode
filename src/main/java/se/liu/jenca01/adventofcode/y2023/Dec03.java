package se.liu.jenca01.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec03 extends Christmas {

    long sampleAnswer1 = 4361;
    long sampleAnswer2 = 467835;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec03();
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

    private List<String> convertData(Stream<String> data) {
        return data.collect(Collectors.toList());
    }

    public long solve1(Stream<String> stream) {
        var lines = convertData(stream);
        lines = padArea(lines);
        var schema = buildSchema(lines);
        var posNums = getPosNums(schema);

        return posNums.stream()
                .filter(pn -> hasSymbolAdjacent(pn, schema))
                .mapToInt(pn -> pn.num)
                .sum();
    }

    private char[][] buildSchema(List<String> lines) {
        var schema = new char[lines.get(0).length()][lines.size()];
        for (int y=0; y<lines.size(); y++) {
            for (int x=0; x<lines.get(y).length(); x++) {
                schema[y][x] = lines.get(y).charAt(x);
            }
        }
        return schema;
    }

    private boolean hasSymbolAdjacent(PosNum pn, char[][] schema) {
        return symbolAdjacentPos(pn, schema).size() > 0;
    }

    private List<Pos> symbolAdjacentPos(PosNum pn, char[][] schema) {
        var symbolPos = new ArrayList<Pos>();

        int xBoundaryLeft = pn.pos.x-1;
        int xBoundaryRight = pn.pos.x+pn.numLen+1;
        for(int x=xBoundaryLeft; x<xBoundaryRight; x++)
            if (isSymbol(schema[pn.pos.y-1][x]))
                symbolPos.add(new Pos(x, pn.pos.y-1));
        for(int x=xBoundaryLeft; x<xBoundaryRight; x++)
            if (isSymbol(schema[pn.pos.y+1][x]))
                symbolPos.add(new Pos(x, pn.pos.y+1));
        if (isSymbol(schema[pn.pos.y][xBoundaryLeft]))
            symbolPos.add(new Pos(xBoundaryLeft, pn.pos.y));
        if (isSymbol(schema[pn.pos.y][xBoundaryRight-1]))
            symbolPos.add(new Pos(xBoundaryRight-1, pn.pos.y));

        return symbolPos;
    }

    private List<String> padArea(List<String> lines) {
        var padded = new ArrayList<String>();
        var w = lines.get(0).length();
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<w+2; i++)
            sb.append('.');
        padded.add(sb.toString());
        for (var line: lines)
            padded.add("."+line+".");
        padded.add(sb.toString());
        return padded;
    }

    private List<PosNum> getPosNums(char[][] schema) {
        var posNums = new ArrayList<PosNum>();

        for (int y=0; y<schema.length; y++) {
            boolean inNum = false;
            int num = 0; int posX = -1, posY = -1;
            for (int x=0; x<schema[y].length; x++) {
                var c = schema[y][x];
                if (isNum(c)) {
                    num = num * 10 + (c - '0');
                    if (!inNum) {
                        posX = x;
                        posY = y;
                        inNum = true;
                    }
                } else {
                    if (inNum) {
                        posNums.add(new PosNum(num, new Pos(posX, posY), x - posX));
                        num = 0; posX = -1; posY = -1;
                        inNum = false;
                    }
                }
            }
        }

       return posNums;
    }

    private boolean isNum(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isSymbol(char c) {
        return !isNum(c) && c != '.';
    }

    private boolean isGear(char c) {
        return c == '*';
    }

    public long solve2(Stream<String> stream) {
        var lines = convertData(stream);
        lines = padArea(lines);
        var schema = buildSchema(lines);
        var posNums = getPosNums(schema);
        var posNumSymbols = getSymbolsForPosNums(posNums, schema);
        var posGears = getPosGears(schema);

        long ratioSum = 0;
        for(Pos gear: posGears) {
            var adjacent = getAdjacent(gear, posNumSymbols);
            if (adjacent.size() > 1)
                ratioSum += 1L * adjacent.get(0).num * adjacent.get(1).num;
        }

        return ratioSum;
    }

    private List<PosNum> getAdjacent(Pos gear, Map<PosNum, List<Pos>> posNumSymbols) {
        var list = new ArrayList<PosNum>();
        for(var posNum: posNumSymbols.keySet())
            if (posNumSymbols.get(posNum).contains(gear))
                list.add(posNum);
        return list;
    }

    private Map<PosNum, List<Pos>> getSymbolsForPosNums(List<PosNum> posNums, char[][] schema) {
        var map = new TreeMap<PosNum, List<Pos>>();
        for (var posNum: posNums)
            map.put(posNum, symbolAdjacentPos(posNum, schema));
        return map;
    }

    private List<Pos> getPosGears(char[][] schema) {
        var posGears = new ArrayList<Pos>();

        for (int y=0; y<schema.length; y++) {
            for (int x=0; x<schema[y].length; x++) {
                var c = schema[y][x];
                if (isGear(c))
                    posGears.add(new Pos(x, y));
            }
        }

       return posGears;
    }

    record PosNum(int num, Pos pos, int numLen) implements Comparable<PosNum> {

        @Override public int compareTo(PosNum o) {
            return Objects.compare(this, o, Comparator
                    .comparing(PosNum::num)
                    .thenComparing(PosNum::pos)
                    .thenComparing(PosNum::numLen));
        }

    }
    record Pos(int x, int y) implements Comparable<Pos> {
        @Override public int compareTo(Pos o) {
        return Objects.compare(this, o, Comparator
                .comparing(Pos::x)
                .thenComparing(Pos::y));
        }
    }
    record GearBox(PosNum a, PosNum b) {}
}
