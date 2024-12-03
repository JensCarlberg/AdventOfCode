package se.liu.jenca01.adventofcode.y2024;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec03 extends Christmas {

    long sampleAnswer1 = 161;
    long sampleAnswer2 = 48;

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
    	long sum = 0;
    	for (var line: lines) {
			sum += sumLine(line);
		}
        return sum;
    }

    private long sumLine(String line) {
    	Pattern pattern = Pattern.compile("(mul\\((\\d{1,3}),(\\d{1,3})\\))");
    	Matcher matcher = pattern.matcher(line);
    	if (!matcher.find()) {
			return 0;
		}
    	String mul = matcher.group(1);
    	String m1 = matcher.group(2);
    	String m2 = matcher.group(3);
    	long mult = Long.parseLong(m1) * Long.parseLong(m2);
    	int resPos = line.indexOf(mul) + mul.length();
    	var res = line.substring(resPos);
    	return mult + sumLine(res);
	}

	public long solve2(Stream<String> stream) {
    	var lines = convertData(stream);
    	var longLineBuilder = new StringBuilder();
    	for (var line: lines) {
			longLineBuilder.append(line).append("#");
		}
		return sumLine(reduceLine(longLineBuilder.toString()));
    }

	private String reduceLine(String line) {
    	Pattern dontDo = Pattern.compile("don't\\(\\)");
    	int dontLen = 7;
    	Pattern doDo = Pattern.compile("do\\(\\)");
    	int doLen = 4;
    	var reduced = new StringBuilder();
    	var enabled = true;

    	looping: while (line.length() > 0) {
    		if (enabled) {
    			Matcher matcher = dontDo.matcher(line);
    			if (!matcher.find()) {
    				reduced.append(line);
    				break looping;
    			}
    			int dontPos = line.indexOf("don't()");
				reduced.append(line.substring(0, dontPos));
    			line = line.substring(dontPos + dontLen);
    			enabled = false;
    		} else {
    			Matcher matcher = doDo.matcher(line);
    			if (!matcher.find()) {
    				break looping;
    			}
    			int doPos = line.indexOf("do()");
    			line = line.substring(doPos + doLen);
    			enabled = true;
    		}
    	}

		return reduced.toString();
	}
}
