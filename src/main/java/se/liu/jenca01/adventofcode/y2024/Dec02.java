package se.liu.jenca01.adventofcode.y2024;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec02 extends Christmas {

    long sampleAnswer1 = 2;
    long sampleAnswer2 = 4;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec02();
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

    private List<List<Long>> convertData(Stream<String> data) {
        return data.map(s -> parseReport(s)).collect(Collectors.toList());
    }

    private List<Long> parseReport(String s) {
    	return
    			Arrays.stream(s.split(" "))
    			.map(p -> Long.parseLong(p))
    			.collect(Collectors.toList());
	}

	public long solve1(Stream<String> stream) {
    	var reports = convertData(stream);
    	var safeReports = 0L;
    	for (var report: reports) {
			if (isSafe(report)) {
				safeReports++;
			}
		}
        return safeReports;
    }

    private boolean isSafe(List<Long> report) {
    	if (report.size() < 2) {
			return true;
		}
    	return checkReport(report, 1L, 3L, false) || checkReport(report, -3L, -1L, false);
	}

	private boolean checkReport(List<Long> report, long lower, long upper, boolean mergeOnce) {
		for(var pos=0; pos<report.size()-1; pos++) {
			if (!between(report.get(pos)-report.get(pos+1), lower, upper)) {
				if (mergeOnce) {
					return checkReport(dampenReport(report, pos), lower, upper, false) || checkReport(dampenReport(report, pos+1), lower, upper, false);
				}
				return false;
			}
		}
		return true;
	}

	private List<Long> dampenReport(List<Long> report, int pos) {
		var dampenedReport = new ArrayList<Long>(report);
		dampenedReport.remove(pos);
		return dampenedReport;
	}

	private boolean between(Long diff, long lower, long upper) {
		return diff >= lower && diff <= upper;
	}

	public long solve2(Stream<String> stream) {
    	var reports = convertData(stream);
    	var safeReports = 0L;
    	for (var report: reports) {
			if (isSafe2(report)) {
				safeReports++;
			}
		}
        return safeReports;
    }

    private boolean isSafe2(List<Long> report) {
    	if (report.size() < 2) {
			return true;
		}
    	return checkReport(report, 1L, 3L, true) || checkReport(report, -3L, -1L, true);
    }

}
// 366 is the right answer, why do I get more?

