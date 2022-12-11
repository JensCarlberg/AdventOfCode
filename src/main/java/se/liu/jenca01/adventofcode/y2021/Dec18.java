package se.liu.jenca01.adventofcode.y2021;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import com.google.common.annotations.VisibleForTesting;
import se.liu.jenca01.adventofcode.Christmas;

@SuppressWarnings("unchecked")
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

    private List<Node> convertData(Stream<String> data) {
        var lines = toList(data);
        var nums = new ArrayList<Node>();
        for (var line: lines)
            nums.add(parseNum(line));
        return nums;
    }

    @VisibleForTesting
    Node parseNum(String line) {
        var nodes = new Stack<Node>();
        var tokens = new Stack<Character>();
        for (var c: line.toCharArray()) {
            switch (c) {
                case '[':
                    tokens.push(c);
                    break;
                case ']':
                    var right = nodes.pop();
                    var left = nodes.pop();
                    nodes.push(Node.node(left, right));
                    tokens.pop();
                    break;
                case ',':
                    break;
                default:
                    int numVal = c - '0';
                    nodes.push(Node.leaf(numVal));
            }
        }
        Node retVal = nodes.pop();
        if (nodes.empty() && tokens.empty())
            return retVal;
        throw new RuntimeException("Not all stacks empty! nodes="+nodes.size()+" tokens="+tokens.size());
    }

    private MutablePair<Object, Object> parseNum_2021(String line) {
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
        Node added = data.remove(0);
        for (var num: data) {
            added = new Node(added, num, null);
            while (reduce(added)) {}
        }
        return 0;
    }

    private boolean reduce(Node added) {
        var parents = new Stack<Node>();
        if (explode(added, 0, parents)) return true;
        if (split(added)) return true;
        return false;
    }

    Node explode(Node node) {
        return node;
    }

    private boolean explode(Node added, int depth, Stack<Node> parents) {
        if (!added.left.isLeaf()) {
            parents.add(added.left);
            if (explode(added.left, depth+1, parents))
                return true;
            parents.pop();
        }

        if (!added.right.isLeaf()) {
            parents.add(added.right);
            if (explode(added.right, depth+1, parents))
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

    private boolean split(Node added) {
        if (!added.left.isLeaf())
            if (split(added.left))
                return true;
        var left = (int) added.left.leaf;
        if (left > 9) {
            added.left = Node.node(Node.leaf(left/2), Node.leaf(((left+1)/2)));
            return true;
        }

        if (!added.right.isLeaf())
            if (split((added.right)))
                return true;
        var right = (int) added.right.leaf;
        if (right > 9) {
            added.right = Node.node(Node.leaf(right/2), Node.leaf(((right+1)/2)));
            return true;
        }
        return false;
    }

    public long solve2(Stream<String> stream) {
        return 0;
    }

    static class Node {
        Node left;
        Node right;
        Integer leaf;
        Node(Node left, Node right, Integer leaf) {
            this.left = left;
            this.right = right;
            this.leaf = leaf;

        }
        boolean isLeaf() { return leaf != null; }
        @Override public String toString() {
            if (isLeaf()) return ""+leaf;
            return "["+left.toString()+","+right.toString()+"]";
        }

        static Node leaf(Integer leaf) { return new Node(null, null, leaf); }
        static Node node(Node left, Node right) { return new Node(left, right, null); }
        static Node empty() { return new Node(null, null, null); }
    }
}

/*



 */