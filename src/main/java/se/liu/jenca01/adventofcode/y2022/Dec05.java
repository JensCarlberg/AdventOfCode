package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec05 extends Christmas {

    String sampleAnswer1 = "CMZ";
    String sampleAnswer2 = "MCD";

    List<Stack<Character>> stacks = new ArrayList<Stack<Character>>();
    List<Move> moves = new ArrayList<Move>();

    public static void main(String[] args) throws Exception {
        var christmas = new Dec05();
        christmas.solveSample();
        christmas.solveMy();
    }

    @Override
    public void solveSample() throws Exception {
        long start = System.currentTimeMillis();
        var solve1 = solve1(sampleData());
        long stop = System.currentTimeMillis();
        System.out.println("Example solve1 timing: " + (stop-start) + "ms");
        assertEquals(sampleAnswer1, solve1, "solve1 is expected to return " + sampleAnswer1);

        start = System.currentTimeMillis();
        var solve2 = solve2(sampleData());
        stop = System.currentTimeMillis();
        System.out.println("Example solve2 timing: " + (stop-start) + "ms");
        assertEquals(sampleAnswer2, solve2, "solve2 is expected to return " + sampleAnswer2);
    }

    @Override
    public void solveMy() throws Exception {
        System.out.println(simpleClassName() + " solve1: " + solve1(myData()));
        System.out.println(simpleClassName() + " solve2: " + solve2(myData()));
    }

    private void convertData(Stream<String> data) {
        var lines = data.toList();

        String lastLine = null;
        for(var line: lines) {
            if (line.contains("["))
                continue;
            if (line.isBlank())
                break;
            lastLine = line;
        }
        var numOfStacks = 0;
        for (int i=1; i<lastLine.length(); i+=4) {
            numOfStacks++;
        }
        stacks.clear();
        for (int i=0; i<numOfStacks; i++)
            stacks.add(new Stack<>());

        for(var line: lines) {
            if (!line.contains("["))
                break;
            var stack = -1;
            for (int i=1; i<line.length(); i+=4) {
                stack++;
                var content = line.charAt(i);
                if (content == ' ') continue;
                stacks.get(stack).add(0, content);
            }
        }

        moves.clear();
        for(var line: lines) {
            if (!line.contains("move"))
                continue;
            var parts = line.split(" ");
            moves.add(new Move(
                    Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[3])-1,
                    Integer.parseInt(parts[5])-1));
        }
    }

    public String solve1(Stream<String> stream) {
        convertData(stream);
        rearrange();
        var ret = "";
        for (var stack: stacks) {
            if (stack.isEmpty()) continue;
            ret += stack.pop();
        }
        return ret;
    }

    private void rearrange() {
        for (var move: moves) {
            for (int i=0; i<move.num; i++) {
                Stack<Character> stack = stacks.get(move.fromStack);
                var crate = stack.pop();
                stacks.get(move.toStack).push(crate);
            }
        }
    }

    public String solve2(Stream<String> stream) {
        convertData(stream);
        rearrangeKeepOrder();
        var ret = "";
        for (var stack: stacks) {
            if (stack.isEmpty()) continue;
            ret += stack.pop();
        }
        return ret;
    }

    private void rearrangeKeepOrder() {
        for (var move: moves) {
            var temp = new Stack<Character>();
            for (int i=0; i<move.num; i++) {
                Stack<Character> stack = stacks.get(move.fromStack);
                var crate = stack.pop();
                temp.push(crate);
            }
            for (int i=0; i<move.num; i++) {
                var crate = temp.pop();
                stacks.get(move.toStack).push(crate);
            }
        }
    }

    record Move(int num, int fromStack, int toStack) {}
}

/*



*/