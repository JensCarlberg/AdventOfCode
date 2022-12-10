package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Stream;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec10 extends Christmas {

    long sampleAnswer1 = 13140;
    long sampleAnswer2 = 0;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec10();
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

    private List<Instruction> convertData(Stream<String> data) {
        var lines = data.toList();
        var allinstr = new ArrayList<Instruction>(lines.size()); 
        for (var line: lines) {
            if (line.startsWith("noop"))
                allinstr.add(new Instruction(1, "noop", 0));
            else
                allinstr.add(new Instruction(2, line.substring(3, 4), Integer.parseInt(line.substring(5))));
        }
        return allinstr;
    }

    public long solve1(Stream<String> stream) {
        var cyclesOfInterest = new TreeSet<Integer>();
        cyclesOfInterest.add(20);
        cyclesOfInterest.add(60);
        cyclesOfInterest.add(100);
        cyclesOfInterest.add(140);
        cyclesOfInterest.add(180);
        cyclesOfInterest.add(220);
        var signalStrength = 0;
        var allInstr = convertData(stream);
        var x = 1;
        var cycle = 0;
        for (var instr: allInstr) {
            for (int i=0; i<instr.timing; i++) {
                cycle++;
                if (cyclesOfInterest.contains(cycle))
                    signalStrength += cycle * x;
            }
            x += instr.change;
        }
        return signalStrength;
    }

    public long solve2(Stream<String> stream) {
        var allInstr = convertData(stream);
        var display = new StringBuilder[6];
        var row = "........................................";
        for (int i=0; i<display.length; i++)
            display[i] = new StringBuilder(row);
        var x = 1;
        var programCounter = 0;
        for (int pixel=0; pixel<240;) {
            int programStep = programCounter++ % allInstr.size();
            var instr = allInstr.get(programStep);
            for (int i=0; i<instr.timing; i++) {
                if (Math.abs((pixel % 40) - x) < 2) {
                    var line = pixel / 40;
                    display[line].setCharAt(pixel % 40, '#');
                }
                pixel++;
            }
            x += instr.change;
        }
        for (var line: display)
            System.out.println(line);
        return 0;
    }

    record Instruction(int timing, String reg, int change) {}
}

/*



*/