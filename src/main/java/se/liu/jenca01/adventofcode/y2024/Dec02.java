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
    	var comp = getComp(report.get(0), report.get(1));
    	if (comp == 0) {
			return false;
		}
    	for (int i=1; i<report.size(); i++) {
			if (getComp(report.get(i-1), report.get(i)) != comp || getStep(report.get(i-1), report.get(i)) > 3) {
				return false;
			}
		}
		return true;
	}

	private int getComp(Long a, Long b) {
		var tempComp = a.compareTo(b);
		if (tempComp < 0) {
			return -1;
		}
		if (tempComp > 0) {
			return 1;
		}
		return 0;
	}

	private long getStep(Long a, Long b) {
		return Math.abs(a-b);
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
    	return isSafe2(report, true);
    }

    private boolean isSafe2(List<Long> report, boolean canRetry) {
    	if (report.size() < 2) {
			return true;
		}
    	var comp = getComp(report.get(0), report.get(1));
    	if (comp == 0) {
			return false;
		}

    	for (int i=1; i<report.size(); i++) {
			if (getComp(report.get(i-1), report.get(i)) != comp || getStep(report.get(i-1), report.get(i)) > 3) {
				if (!canRetry) {
					return false;
				}
				var report1 = new ArrayList<Long>(report);
				report1.remove(i);
				var report2 = new ArrayList<Long>(report);
				report2.remove(i-1);
				var report3 = new ArrayList<Long>(report);
				report3.remove(0);
				return isSafe2(report1, false) || isSafe2(report2, false) || isSafe2(report3, false);
			}
		}
		return true;
	}
}
// 360

