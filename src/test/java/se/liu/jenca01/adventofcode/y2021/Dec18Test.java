package se.liu.jenca01.adventofcode.y2021;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Dec18Test {

    @Test
    void testSnailFishNumberParseSimple() {
        var line = "[1,2]";
        var out = new Dec18();
        var res = out.parseNum(line);
        assertEquals(line, res.toString(), "Result toString should return indata");
    }

    @Test
    void testSnailFishNumberParseNodeLeft() {
        var line = "[[9,8],2]";
        var out = new Dec18();
        var res = out.parseNum(line);
        assertEquals(line, res.toString(), "Result toString should return indata");
    }

    @Test
    void testSnailFishNumberParseNodeRight() {
        var line = "[5,[6,4]]";
        var out = new Dec18();
        var res = out.parseNum(line);
        assertEquals(line, res.toString(), "Result toString should return indata");
    }

    @Test
    void testSnailFishNumberParseNodeNodeNode() {
        var line = "[5,[6,[6,[6,4]]]]";
        var out = new Dec18();
        var res = out.parseNum(line);
        assertEquals(line, res.toString(), "Result toString should return indata");
    }

    @Test
    void testParseComplex() {
        var ins = new String [] {
                "[[[[[9,8],1],2],3],4]",
                "[7,[6,[5,[4,[3,2]]]]]",
                "[[6,[5,[4,[3,2]]]],1]",
                "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]"
                };

        var out = new Dec18();
        for (int i=0; i<ins.length; i++) {
            var in = ins[i];
            assertEquals(in, out.parseNum(in).toString(), "Result toString should return indata");
        }
    }

    @Test
    void testExplode() {
        var ins = new String [] {
                "[[[[[9,8],1],2],3],4]",
                "[7,[6,[5,[4,[3,2]]]]]",
                "[[6,[5,[4,[3,2]]]],1]",
                "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]"
                };
        var answers = new String [] {
                "[[[[0,9],2],3],4]",
                "[7,[6,[5,[7,0]]]]",
                "[[6,[5,[7,0]]],3]",
                "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]",

        };
        var out = new Dec18();
        for (int i=0; i<ins.length; i++) {
            var in = ins[i];
            var answer = answers[i];
            assertEquals(answer, out.explode(out.parseNum(in)).toString(), "Explode onm indata should result in given puzzle definition answer");
        }
    }

}
