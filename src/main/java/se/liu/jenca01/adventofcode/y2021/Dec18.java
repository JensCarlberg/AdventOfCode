package se.liu.jenca01.adventofcode.y2021;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec18 extends Christmas {

    long sampleAnswer1 = 4140; // [[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]
    long sampleAnswer2 = 0;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec18();
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

    private List<MutablePair<Object, Object>> convertData(Stream<String> data) {
        var lines = toList(data);
        var nums = new ArrayList<MutablePair<Object, Object>>();
        for (var line: lines)
            nums.add(parseNum(line));
        return nums;
    }

    private MutablePair<Object, Object> parseNum(String line) {
        line = line.replace('[', '(').replace(']',')'); // To get num.equals(parseNum(num.toString)) == true
        var stack = new Stack<MutablePair<Object, Object>>();
        var currPair = new MutablePair<Object, Object>();
        for (var c: line.toCharArray()) {
            switch (c) {
                case '(':
                    stack.push(currPair);
                    currPair = new MutablePair<>();
                    break;
                case ')':
                    var prevPair = stack.pop();
                    if (prevPair.getLeft() == null)
                        prevPair.left = currPair;
                    else
                        prevPair.right = currPair;
                    currPair = prevPair;
                    break;
                case ',':
                    break;
                default:
                    int numVal = c - '0';
                    if (currPair.getLeft() == null) currPair.left = numVal;
                    else currPair.right = numVal;
            }
        }
        MutablePair<Object, Object> returnVal = (MutablePair<Object, Object>) currPair.getLeft();
        return returnVal;
    }

    public long solve1(Stream<String> stream) {
        var data = convertData(stream);
        MutablePair<Object, Object> added = data.remove(0);
        for (var num: data) {
            added = new MutablePair<>(added, num);
            while (reduce(added)) {}
        }
        return 0;
    }

    private boolean reduce(MutablePair<Object, Object> added) {
        var parents = new Stack<MutablePair<Object, Object>>();
        if (explode(added, 0, parents)) return true;
        if (split(added)) return true;
        return false;
    }

    private boolean explode(MutablePair<Object, Object> added, int depth, Stack<MutablePair<Object,Object>> parents) {
        if (added.left instanceof Pair) {
            parents.add((MutablePair<Object, Object>) added.left);
            if (explode((MutablePair<Object, Object>) added.left, depth+1, parents))
                return true;
            parents.pop();
        }

        if (added.right instanceof Pair) {
            parents.add((MutablePair<Object, Object>) added.right);
            if (explode((MutablePair<Object, Object>) added.right, depth+1, parents))
                return true;
            parents.pop();
        }

        if (depth < 4) return false;

        return false;
    }

    private List<Object> flatten(MutablePair<Object, Object> added, List<Object> list) {
        if (added.left instanceof Pair)
            flatten((MutablePair<Object, Object>) added.left, list);
        else
            list.add(added.left);

        if (added.right instanceof Pair)
            flatten((MutablePair<Object, Object>) added.right, list);
        else
            list.add(added.right);

        return list;
    }

    private boolean split(MutablePair<Object, Object> added) {
        if (added.getLeft() instanceof Pair)
            if (split((MutablePair<Object, Object>) added.getLeft()))
                return true;
        var left = (int) added.getLeft();
        if (left > 9) {
            added.left = new MutablePair<Object, Object>(left/2, (left+1)/2);
            return true;
        }

        if (added.getRight() instanceof Pair)
            if (split((MutablePair<Object, Object>) added.getRight()))
                return true;
        var right = (int) added.getRight();
        if (right > 9) {
            added.right = new MutablePair<Object, Object>(right/2, (right+1)/2);
            return true;
        }
        return false;
    }

    public long solve2(Stream<String> stream) {
        return 0;
    }
}

/*



 */