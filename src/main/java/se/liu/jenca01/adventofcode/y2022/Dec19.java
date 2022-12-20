package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec19 extends Christmas {

    long sampleAnswer1 = 33;
    long sampleAnswer2 = 0;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec19();
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

    private List<Blueprint> convertData(Stream<String> data) {
    	var lines = data.toList();
    	var blueprints = new ArrayList<Blueprint>();
    	for (var line: lines)
    		blueprints.add(buildBlueprint(line));
        return blueprints;
    }

    private Blueprint buildBlueprint(String line) {
        Pattern r = Pattern.compile("Blueprint \\d+: Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.");
        Matcher m = r.matcher(line);

        if (m.find( )) {
        	var oreOre = parse(m.group(1));
        	var clayOre = parse(m.group(2));
        	var obsidianOre = parse(m.group(3));
        	var obsidianClay = parse(m.group(4));
        	var geodeOre = parse(m.group(5));
        	var geodeObsidian = parse(m.group(6));
        	return new Blueprint(
        			Cost.ore(oreOre),
        			Cost.clay(clayOre),
        			Cost.obsidian(obsidianOre, obsidianClay),
        			Cost.geode(geodeOre, geodeObsidian));
        }
        throw new RuntimeException("Line doews not match.");
	}

	private int parse(String num) {
		return Integer.parseInt(num);
	}

	public long solve1(Stream<String> stream) {
    	var noOfMinutes = 24;
    	var blueprints = convertData(stream);
    	var geodes = new ArrayList<Integer>();
    	for (var blueprint: blueprints)
    		geodes.add(simulate(blueprint, noOfMinutes));
    	var sum = 0;
    	for (var geode=0; geode<geodes.size(); geode++)
    		sum += (geode + 1)*geodes.get(geode);
        return sum;
    }

    private Integer simulate(Blueprint blueprint, int noOfMinutes) {
    	var ore = 0;
    	var clay = 0;
    	var obsidian = 0;
    	var geode = 0;
    	var oreRobots = 1;
    	var clayRobots = 0;
    	var obsidianRobots = 0;
    	var geodeRobots = 0;
    	var factoryQueue = new ArrayList<RobotType>();

    	for (var i=0; i<noOfMinutes; i++) {
    		if (blueprint.geode.enoughFor(ore, clay, obsidian)) {
    			factoryQueue.add(RobotType.GEODE);
    			ore -= blueprint.geode.ore;
    			clay -= blueprint.geode.clay;
    			obsidian -= blueprint.geode.obsidian;
    		}
    		if (blueprint.obsidian.enoughFor(ore, clay, obsidian)) {
    			factoryQueue.add(RobotType.OBSIDIAN);
    			ore -= blueprint.obsidian.ore;
    			clay -= blueprint.obsidian.clay;
    			obsidian -= blueprint.obsidian.obsidian;
    		}
    		if (blueprint.clay.enoughFor(ore, clay, obsidian)) {
    			factoryQueue.add(RobotType.CLAY);
    			ore -= blueprint.clay.ore;
    			clay -= blueprint.clay.clay;
    			obsidian -= blueprint.clay.obsidian;
    		}
    		if (blueprint.ore.enoughFor(ore, clay, obsidian)) {
    			factoryQueue.add(RobotType.ORE);
    			ore -= blueprint.ore.ore;
    			clay -= blueprint.ore.clay;
    			obsidian -= blueprint.ore.obsidian;
    		}
    		ore += oreRobots;
    		clay += clayRobots;
    		obsidian += obsidianRobots;
    		geode += geodeRobots;
    		for (var type: factoryQueue) {
    			switch (type) {
				case CLAY: clayRobots++; break;
				case GEODE: geodeRobots++; break;
				case OBSIDIAN: obsidianRobots++; break;
				case ORE: oreRobots++; break;
    			}
    		}
    		System.out.println("== Minute " + (i+1) + " ==");
    		System.out.println("ore:      " + oreRobots + " robots, " + ore + " ore");
    		System.out.println("clay:     " + clayRobots + " robots, " + clay + " clay");
    		System.out.println("obsidian: " + obsidianRobots + " robots, " + obsidian + " obsidian");
    		System.out.println("geode:    " + geodeRobots + " robots, " + geode + " geode");
    		factoryQueue.clear();
    	}
		return geode;
	}

	public long solve2(Stream<String> stream) {
        return 0;
    }

	enum RobotType {
		ORE, CLAY, OBSIDIAN, GEODE;
	}
    record Cost(int ore, int clay, int obsidian) {
    	boolean enoughFor(int ore, int clay, int onbsidian) { return ore >= this.ore && clay >= this.clay && onbsidian >= obsidian; }
    	static Cost ore(int ore) { return new Cost(ore, 0, 0); }
    	static Cost clay(int ore) { return ore(ore); }
    	static Cost obsidian(int ore, int clay) { return new Cost(ore, clay, 0); }
    	static Cost geode(int ore, int obsidian) { return new Cost(ore, 0, obsidian); }
    }
    record Blueprint(Cost ore, Cost clay, Cost obsidian, Cost geode) {}
}

/*



*/