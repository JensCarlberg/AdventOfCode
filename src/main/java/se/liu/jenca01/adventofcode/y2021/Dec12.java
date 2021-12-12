package se.liu.jenca01.adventofcode.y2021;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec12 extends Christmas {

    static class Cave implements Comparable<Cave> {
        final String name;
        final boolean small;
        final Set<Cave> connectsTo;
        
        public Cave(String name, boolean small, Set<Cave> connectsTo) {
            this.name = name;
            this.small = small;
            this.connectsTo = connectsTo;
        }
        
        static Cave build(String name) {
            return new Cave(name, name.equals(name.toLowerCase()), new TreeSet<Cave>());
        }

        @Override public int compareTo(Cave c) { return name.compareTo(c.name); }
        @Override public String toString() { return String.format("{%s -> %s}", name, small ? "small" : "big", String.join(",", connectsTo.stream().map(c -> c.name).collect(Collectors.toList()))); }
    }

    long sampleAnswer1 = 10;
    long sampleAnswer11 = 19;
    long sampleAnswer12 = 226;
    long sampleAnswer2 = 36;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec12();
        christmas.solveSample();
        christmas.solveMy();
    }

    @Override
    public void solveSample() throws Exception {
        // Warmup
        solve1(sampleData());
        
        long start = System.currentTimeMillis();
        long solve1 = solve1(sampleData());
        long stop = System.currentTimeMillis();
        System.out.println("Example solve1 timing: " + (stop-start) + "ms");
        assertEquals(sampleAnswer1, solve1, "solve1 is expected to return " + sampleAnswer1);

        long start11 = System.currentTimeMillis();
        long solve11 = solve1(sampleData(1));
        long stop11 = System.currentTimeMillis();
        System.out.println("Example solve1 timing: " + (stop11-start11) + "ms");
        assertEquals(sampleAnswer11, solve11, "solve1 is expected to return " + sampleAnswer11);

        long start12 = System.currentTimeMillis();
        long solve12 = solve1(sampleData(2));
        long stop12 = System.currentTimeMillis();
        System.out.println("Example solve12 timing: " + (stop12-start12) + "ms");
        assertEquals(sampleAnswer12, solve12, "solve12 is expected to return " + sampleAnswer12);

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

    private Map<String,Cave> convertData(Stream<String> data) {
        var caves = new HashMap<String,Cave>();
        var lines = toList(data);
        for (var line: lines) {
            var parts = line.split("-");
            Cave cave0 = caves.containsKey(parts[0]) ? caves.get(parts[0]) : Cave.build(parts[0]);
            Cave cave1 = caves.containsKey(parts[1]) ? caves.get(parts[1]) : Cave.build(parts[1]);
            cave0.connectsTo.add(cave1);
            cave1.connectsTo.add(cave0);
            caves.put(parts[0], cave0);
            caves.put(parts[1], cave1);
        }
        return caves;
    }

    public long solve1(Stream<String> stream) {
        var caves = convertData(stream);
        var youAreHere = caves.get("start");
        var visited = new ArrayList<String>();
        return findPaths(youAreHere, visited);
    }

    private long findPaths(Cave youAreHere, ArrayList<String> visited) {
        if ("end".equals(youAreHere.name)) return 1;
        var paths = 0L;
        visited.add(youAreHere.name);
        for (var to: youAreHere.connectsTo) {
            if (!to.small || !visited.contains(to.name))
                paths += findPaths(to, (ArrayList<String>) visited.clone());
        }
        return paths;
    }

    public long solve2(Stream<String> stream) {
        var caves = convertData(stream);
        var youAreHere = caves.get("start");
        var visited = new ArrayList<String>();
        return findPaths2(youAreHere, visited);
    }

    private long findPaths2(Cave youAreHere, ArrayList<String> visited) {
        if ("end".equals(youAreHere.name)) return 1;
        var paths = 0L;
        visited.add(youAreHere.name);
        for (var to: youAreHere.connectsTo) {
            boolean big = !to.small;
            if (big || canVisit(visited, to.name))
                paths += findPaths2(to, (ArrayList<String>) visited.clone());
        }
        return paths;
    }

    private boolean canVisit(ArrayList<String> visited, String caveName) {
        if (!visited.contains(caveName))  return true;
        if ("start".equals(caveName) || "end".equals(caveName)) return false;
        var visitedSmall = new ArrayList<String>();
        for(var name: visited)
            if (name.equals(name.toLowerCase()))
                if (visitedSmall.contains(name))
                    return false;
                else
                    visitedSmall.add(name);
        return true;
    }
}

/*

--- Day 12: Passage Pathing ---
With your submarine's subterranean subsystems subsisting suboptimally, the only way you're getting out of this cave anytime soon is by finding a path yourself. Not just a path - the only way to know if you've found the best path is to find all of them.

Fortunately, the sensors are still mostly working, and so you build a rough map of the remaining caves (your puzzle input). For example:

start-A
start-b
A-c
A-b
b-d
A-end
b-end
This is a list of how all of the caves are connected. You start in the cave named start, and your destination is the cave named end. An entry like b-d means that cave b is connected to cave d - that is, you can move between them.

So, the above cave system looks roughly like this:

    start
    /   \
c--A-----b--d
    \   /
     end
Your goal is to find the number of distinct paths that start at start, end at end, and don't visit small caves more than once. There are two types of caves: big caves (written in uppercase, like A) and small caves (written in lowercase, like b). It would be a waste of time to visit any small cave more than once, but big caves are large enough that it might be worth visiting them multiple times. So, all paths you find should visit small caves at most once, and can visit big caves any number of times.

Given these rules, there are 10 paths through this example cave system:

start,A,b,A,c,A,end
start,A,b,A,end
start,A,b,end
start,A,c,A,b,A,end
start,A,c,A,b,end
start,A,c,A,end
start,A,end
start,b,A,c,A,end
start,b,A,end
start,b,end
(Each line in the above list corresponds to a single path; the caves visited by that path are listed in the order they are visited and separated by commas.)

Note that in this cave system, cave d is never visited by any path: to do so, cave b would need to be visited twice (once on the way to cave d and a second time when returning from cave d), and since cave b is small, this is not allowed.

Here is a slightly larger example:

dc-end
HN-start
start-kj
dc-start
dc-HN
LN-dc
HN-end
kj-sa
kj-HN
kj-dc
The 19 paths through it are as follows:

start,HN,dc,HN,end
start,HN,dc,HN,kj,HN,end
start,HN,dc,end
start,HN,dc,kj,HN,end
start,HN,end
start,HN,kj,HN,dc,HN,end
start,HN,kj,HN,dc,end
start,HN,kj,HN,end
start,HN,kj,dc,HN,end
start,HN,kj,dc,end
start,dc,HN,end
start,dc,HN,kj,HN,end
start,dc,end
start,dc,kj,HN,end
start,kj,HN,dc,HN,end
start,kj,HN,dc,end
start,kj,HN,end
start,kj,dc,HN,end
start,kj,dc,end
Finally, this even larger example has 226 paths through it:

fs-end
he-DX
fs-he
start-DX
pj-DX
end-zg
zg-sl
zg-pj
pj-he
RW-he
fs-DX
pj-RW
zg-RW
start-pj
he-WI
zg-he
pj-fs
start-RW
How many paths through this cave system are there that visit small caves at most once?


--- Part Two ---
After reviewing the available paths, you realize you might have time to visit a single small cave twice. Specifically, big caves can be visited any number of times, a single small cave can be visited at most twice, and the remaining small caves can be visited at most once. However, the caves named start and end can only be visited exactly once each: once you leave the start cave, you may not return to it, and once you reach the end cave, the path must end immediately.

Now, the 36 possible paths through the first example above are:

start,A,b,A,b,A,c,A,end
start,A,b,A,b,A,end
start,A,b,A,b,end
start,A,b,A,c,A,b,A,end
start,A,b,A,c,A,b,end
start,A,b,A,c,A,c,A,end
start,A,b,A,c,A,end
start,A,b,A,end
start,A,b,d,b,A,c,A,end
start,A,b,d,b,A,end
start,A,b,d,b,end
start,A,b,end
start,A,c,A,b,A,b,A,end
start,A,c,A,b,A,b,end
start,A,c,A,b,A,c,A,end
start,A,c,A,b,A,end
start,A,c,A,b,d,b,A,end
start,A,c,A,b,d,b,end
start,A,c,A,b,end
start,A,c,A,c,A,b,A,end
start,A,c,A,c,A,b,end
start,A,c,A,c,A,end
start,A,c,A,end
start,A,end
start,b,A,b,A,c,A,end
start,b,A,b,A,end
start,b,A,b,end
start,b,A,c,A,b,A,end
start,b,A,c,A,b,end
start,b,A,c,A,c,A,end
start,b,A,c,A,end
start,b,A,end
start,b,d,b,A,c,A,end
start,b,d,b,A,end
start,b,d,b,end
start,b,end
The slightly larger example above now has 103 paths through it, and the even larger example now has 3509 paths through it.

Given these new rules, how many paths through this cave system are there?

*/