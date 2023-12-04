package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec17 extends Christmas {

    private static final char[] EMPTY_ROW = ".......".toCharArray();
    long sampleAnswer1 = 3068;
    long sampleAnswer2 = 0;

    @SuppressWarnings("serial")
    static class NotImplemented extends RuntimeException {}

    static String shape0 = ""
            + "####";
    static String shape1 = ""
            + " # \n"
            + "###\n"
            + " # ";
    static String shape2 = ""
            + "  #\n"
            + "  #\n"
            + "###";
    static String shape3 = ""
            + "#\n"
            + "#\n"
            + "#\n"
            + "#";
    static String shape4 = ""
            + "##\n"
            + "##";

    static String pad(String shape, int pad) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<pad; i++)
            sb.append(" ");
        return pad(shape, sb.toString());

    }

    static String pad(String shape, String pad) {
        var lines = shape.split("\n");
        var paddedShape = new ArrayList<String>();
        for (var line: lines)
            paddedShape.add(pad + line);
        return String.join("\n", paddedShape);
    }

    static int shapeHeight(List<char[][]> list) {
        return list.get(0).length;
    }

    static char[][] shapeArr(String shape) {
        var lines = shape.split("\n");
        var rowCount = lines.length;
        var colCount = lines[0].length();
        var array = new char[rowCount][colCount];
        for (var row=0; row < rowCount; row++)
            for (var col=0; col < colCount; col++)
                array[row][col] = lines[row].charAt(col);
        return array;
    }

    static List<List<char[][]>> shapes = new ArrayList<>();
    static  {
        var shapes0 = new ArrayList<char[][]>();
        shapes0.add(shapeArr(pad(shape0, 0)));
        shapes0.add(shapeArr(pad(shape0, 1)));
        shapes0.add(shapeArr(pad(shape0, 2)));
        shapes0.add(shapeArr(pad(shape0, 3)));
        shapes.add(shapes0);

        var shapes1 = new ArrayList<char[][]>();
        shapes1.add(shapeArr(pad(shape1, 0)));
        shapes1.add(shapeArr(pad(shape1, 1)));
        shapes1.add(shapeArr(pad(shape1, 2)));
        shapes1.add(shapeArr(pad(shape1, 3)));
        shapes1.add(shapeArr(pad(shape1, 4)));
        shapes.add(shapes1);

        var shapes2 = new ArrayList<char[][]>();
        shapes2.add(shapeArr(pad(shape2, 0)));
        shapes2.add(shapeArr(pad(shape2, 1)));
        shapes2.add(shapeArr(pad(shape2, 2)));
        shapes2.add(shapeArr(pad(shape2, 3)));
        shapes2.add(shapeArr(pad(shape2, 4)));
        shapes.add(shapes2);

        var shapes3 = new ArrayList<char[][]>();
        shapes3.add(shapeArr(pad(shape3, 0)));
        shapes3.add(shapeArr(pad(shape3, 1)));
        shapes3.add(shapeArr(pad(shape3, 2)));
        shapes3.add(shapeArr(pad(shape3, 3)));
        shapes3.add(shapeArr(pad(shape3, 4)));
        shapes3.add(shapeArr(pad(shape3, 5)));
        shapes3.add(shapeArr(pad(shape3, 6)));
        shapes.add(shapes3);

        var shapes4 = new ArrayList<char[][]>();
        shapes4.add(shapeArr(pad(shape4, 0)));
        shapes4.add(shapeArr(pad(shape4, 1)));
        shapes4.add(shapeArr(pad(shape4, 2)));
        shapes4.add(shapeArr(pad(shape4, 3)));
        shapes4.add(shapeArr(pad(shape4, 4)));
        shapes4.add(shapeArr(pad(shape4, 5)));
        shapes.add(shapes4);
    }

    public static void main(String[] args) throws Exception {
        var christmas = new Dec17();
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

    private char[] convertData(Stream<String> data) {
        return data.toList().get(0).toCharArray();
    }

    public long solve1(Stream<String> stream) {
        var puzzle = convertData(stream);
        int step = 0;
        var currShape = 0;
        var currShapePos = 2;
        var shapeHeight = 2 + shapeHeight(shapes.get(currShape));
        var tower = new ArrayList<char[]>();
        do {
            var move = puzzle[step % puzzle.length];
            currShapePos = puff(move, currShape, currShapePos, shapeHeight, tower);
            if (canDrop(shapes.get(currShape).get(currShapePos), tower, shapeHeight)) {
                shapeHeight--;
            } else {
                settleShape(shapes.get(currShape).get(currShapePos), tower, shapeHeight);
                display(new char[1][0], tower, shapeHeight);
                currShape = (currShape + 1) % shapes.size();
                shapeHeight = 3 + shapeHeight(shapes.get(currShape)) + tower.size();
            }
        } while (++step < 2022);

        return 0;
    }

    private boolean canDrop(char[][] shape, ArrayList<char[]> tower, int shapeHeight) {
        display(shape, tower, shapeHeight);
        if (shapeHeight - shape.length + 1 == 0) return false; // Bottom
        if (shapeHeight - shape.length >= tower.size()) return true; // Above the tower
        return !collides(shape, tower, shapeHeight);
    }

    private boolean collides(char[][] shape, ArrayList<char[]> tower, int shapeHeight) {
        for (var row=0; row<shape.length; row++) {
            if (tower.size() < shapeHeight - row) continue;
            for (var col=0; col<shape[row].length; col++) {
                if (shape[row][col] == '#' && tower.get(shapeHeight - 1 - row)[col] == '#')
                    return true;
            }
        }
        return false;
    }

    private void display(char[][] shape, ArrayList<char[]> tower, int shapeHeight) {
        var maxHeight = Math.max(tower.size(), shapeHeight);
        for (var rowNo=maxHeight; rowNo >=0; rowNo--) {
            var rowHead = String.format("%5d: ", rowNo);
            var rowAboveTower = rowNo >= tower.size();
            var rowBelowShape = (shapeHeight - rowNo) >= shape.length;
            if (rowAboveTower && rowBelowShape)
                System.out.println(rowHead); //empty space
            else if (rowAboveTower && !rowBelowShape) // only shape
                System.out.println(rowHead + new String(shape[shapeHeight - rowNo]));
            else if (!rowAboveTower && rowBelowShape) // only tower
                System.out.println(rowHead + new String(tower.get(rowNo))); // Tower
            else { // merge shape and tower
                var shapeRow = shape[shapeHeight - rowNo];
                var towerRow = rowNo >= tower.size() ? EMPTY_ROW : tower.get(rowNo);
                for (int i=0; i<shapeRow.length; i++)
                    if (shapeRow[i] == '#')
                        towerRow[i] = '#';
                System.out.println(rowHead + new String(towerRow));
            }
        }
        System.out.println("------+-------+");
    }

    private void settleShape(char[][] shape, ArrayList<char[]> tower, int shapeHeight) {
        var maxHeight = Math.max(tower.size(), shapeHeight);
        for (var rowNo=0; rowNo<maxHeight; rowNo++) {
            var rowAboveTower = rowNo >= tower.size();
            var rowBelowShape = (shapeHeight - rowNo) >= shape.length;
            if (rowAboveTower && rowBelowShape)
                throw new RuntimeException("Trying to settle in space!");
            else if (rowAboveTower && !rowBelowShape) // only shape
                tower.add(shape[shapeHeight-rowNo]);
            else if (!rowAboveTower && rowBelowShape) // only tower
                continue;
            else { // merge shape and tower
                var shapeRow = shape[shapeHeight - rowNo];
                var towerRow = rowNo >= tower.size() ? EMPTY_ROW : tower.get(rowNo);
                for (int i=0; i<shapeRow.length; i++)
                    if (shapeRow[i] == '#')
                        towerRow[i] = '#';
                tower.set(rowNo, towerRow);
            }
        }
    }

    private int puff(char move, int currShape, int currShapePos, int shapeHeight, ArrayList<char[]> tower) {
        if (move == '<') return puffLeft(move, currShape, currShapePos, shapeHeight, tower);
        if (move == '>') return puffRight(move, currShape, currShapePos, shapeHeight, tower);
        throw new RuntimeException("Unkonwn direction: " + move);
    }

    private int puffLeft(char move, int currShape, int currShapePos, int shapeHeight, ArrayList<char[]> tower) {
        if (currShapePos == 0) return 0;
        return currShapePos - 1;
        //		throw new NotImplemented();
    }

    private int puffRight(char move, int currShape, int currShapePos, int shapeHeight, ArrayList<char[]> tower) {
        if (currShapePos == shapes.get(currShape).size() - 1) return currShapePos;
        return currShapePos + 1;
        //		throw new NotImplemented();
    }

    public long solve2(Stream<String> stream) {
        return 0;
    }
}

/*



 */