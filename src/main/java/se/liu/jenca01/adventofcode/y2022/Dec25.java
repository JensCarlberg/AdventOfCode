package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec25 extends Christmas {

    String sampleAnswer1 = "2=-1=0";
    long sampleAnswer2 = 0;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec25();
        christmas.solveSample();
        christmas.solveMy();
    }

    @Override
    public void solveSample() throws Exception {
        long start = System.currentTimeMillis();
        String solve1 = solve1(sampleData());
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
    	return data.toList();
    }

    public String solve1(Stream<String> stream) {
    	var puzzle = convertData(stream);
    	for (var line: puzzle)
    		assertEquals(line, convertToSnafu(convertToDec(line)));
    	var sum = 0L;
    	for (var line: puzzle)
    		sum += convertToDec(line);
        return convertToSnafu(sum);
    }

    private long convertToDec(String snafu) {
    	var num = 0L;
    	var pos=0;
    	for(int i=snafu.length()-1; i>=0; i--) {
    		var digit = snafu.charAt(i);
    		var posWeight = Math.pow(5, pos++);
    		switch (digit) {
    		case '=': num += (-2 * posWeight); break;
    		case '-': num += (-1 * posWeight); break;
    		case '0': break;
    		case '1': num += (1 * posWeight); break;
    		case '2': num += (2 * posWeight); break;
    		default: throw new RuntimeException("Unknown SNAFU digit: " + digit);
    		}
    	}
		return num;
	}

	private String convertToSnafu(long num) {
		var base5 = Long.toString(num, 5);
		var numbers = new StringBuilder(base5);
		var pos = numbers.length() - 1;
		while (pos >= 0) {
			var testDigit = numbers.charAt(pos);
			if (testDigit > '2') {
				numbers.replace(pos, pos+1, minus5(testDigit));
				if (pos > 0)
					numbers.replace(pos-1, pos, plus1(numbers.charAt(pos-1)));
				else {
					// The number just got longer
					numbers.insert(0, "1");
					pos++;
				}
			}
			pos--;
		}
		return numbers.toString();
	}

	private String minus5(char testDigit) {
		switch (testDigit) {
		case '7': return "2";
		case '6': return "1";
		case '5': return "0";
		case '4': return "-";
		case '3': return "=";
		default:
			throw new IllegalArgumentException("Unexpected value: " + testDigit);
		}
	}

	private String plus1(char testDigit) {
		switch (testDigit) {
		case '6': return "7";
		case '5': return "6";
		case '4': return "5";
		case '3': return "4";
		case '2': return "3";
		case '1': return "2";
		case '0': return "1";
		case '-': return "0";
		case '=': return "-";
		default:
			throw new IllegalArgumentException("Unexpected value: " + testDigit);
		}
	}

	public long solve2(Stream<String> stream) {
        return 0;
    }
}

/*



*/