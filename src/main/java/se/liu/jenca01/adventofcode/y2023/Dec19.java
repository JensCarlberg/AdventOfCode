package se.liu.jenca01.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.json.JSONObject;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec19 extends Christmas {

    long sampleAnswer1 = 19114;
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

    private Puzzle convertData(Stream<String> data) {
        var lines = data.toList();
        var workflows = new TreeMap<String, Workflow>();
        var parts = new ArrayList<JSONObject>();
        var parseWf = true;
        for (var line: lines) {
            if (line.strip().length() == 0) {
                parseWf = false;
                continue;
            }
            if (parseWf) {
                Workflow wf = Workflow.parse(line);
                workflows.put(wf.name, wf);
            } else
                parts.add(new JSONObject(json(line)));
        }
        workflows.put(Workflow.ACCEPTED.name, Workflow.ACCEPTED);
        workflows.put(Workflow.REJECTED.name, Workflow.REJECTED);
        return new Puzzle(workflows, parts);
    }

    private String json(String line) {
        return line
        .replace("x=", "\"x\":")
        .replace("m=", "\"m\":")
        .replace("a=", "\"a\":")
        .replace("s=", "\"s\":");
    }

    public long solve1(Stream<String> stream) {
        var puzzle = convertData(stream);
        return 0;
    }

    public long solve2(Stream<String> stream) {
        return 0;
    }

    record Rule(String attribute, char operator, int testThreshold, String targetWorkflow) {
        static Rule parse(String ruleDescr) {
            if (!ruleDescr.contains(":"))
                return new Rule("x", '>', -1, ruleDescr);
            var parts = ruleDescr.split("[<>:]");
            return new Rule(
                    parts[0].substring(0, 1),
                    (ruleDescr.contains("<") ? '<' : '>'),
                    Integer.parseInt(parts[1]),
                    parts[2]
                    );
        }
    }
    record Workflow(String name, List<Rule> rules) {
        static Workflow ACCEPTED = new Workflow("A", List.of());
        static Workflow REJECTED = new Workflow("R", List.of());
        static Workflow parse(String wfDescr) {
            var parts = wfDescr.split("[\\{\\}]");
            String[] rulesDescr = parts[1].split(",");
            var rules = Arrays.stream(rulesDescr).map(r -> Rule.parse(r)).toList();
            return new Workflow(parts[0], rules);
        }
    }
    record Puzzle(Map<String, Workflow> workflows, List<JSONObject> parts) {}
}
