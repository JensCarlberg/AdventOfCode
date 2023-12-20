package se.liu.jenca01.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec20 extends Christmas {

    long sampleAnswer1 = 11687500; // 4250 low pulses * 2750 high pulses
    long sampleAnswer2 = 0;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec20();
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

    private List<Module> convertData(Stream<String> data) {
        var lines = data.toList();
        var modules = new ArrayList<Module>();
        var queue = new LinkedList<Signal>();

        // Create modules
        for (var line: lines)
            modules.add(parseModule(line, queue));

        // Set outputs, create output modules not yet defined (missing output of their own)
        var moduleMap = modules.stream().collect(Collectors.toMap(Module::name, m -> m));
        for (var line: lines)
            addOutputModules(line, modules, moduleMap);

        // For Conjunction nodes, set inputs
        for (var module: modules)
            if (module instanceof Conjunction)
                ((Conjunction) module).setMemory(getModulesWithThisOutput(module, modules));


        return modules;
    }

    private Map<Module, Pulse> getModulesWithThisOutput(Module conjunction, ArrayList<Module> modules) {
        var map = new TreeMap<Module, Pulse>();
        for (var module: modules)
            if (module.getOutputs().contains(conjunction))
                map.put(module, Pulse.LOW);
        return map;
    }

    private void addOutputModules(String moduleDesc, List<Module> modules, Map<String, Module> moduleMap) {
        var parts = moduleDesc.replace("%", "").replace("&", "").split("->");
        var outputs = Arrays.stream(parts[1].strip().split(", *")).map(m -> {
            if (moduleMap.containsKey(m))
                return moduleMap.get(m);
            else
                return new Output(m);
            }).toList();
        moduleMap.get(parts[0].strip()).setOutputs(outputs);
    }

    private Module parseModule(String moduleDesc, Queue<Signal> queue) {
        var parts = moduleDesc.split("->");
        if ("broadcaster".equalsIgnoreCase(parts[0].strip()))
            return new Broadcaster("broadcaster", queue);
        if (parts[0].charAt(0) == '%')
            return new FlipFlop(parts[0].strip().substring(1), queue);
        if (parts[0].charAt(0) == '&')
            return new Conjunction(parts[0].strip().substring(1), queue);
        throw new RuntimeException("Could not identify module type. moduleDesc=" + moduleDesc);
    }

    public long solve1(Stream<String> stream) {
        var puzzle = convertData(stream);
        return 0;
    }

    public long solve2(Stream<String> stream) {
        return 0;
    }

    enum Pulse { LOW, HIGH; }
    static class Counter {
        static int low = 0;
        static int high = 0;
        static void add(Pulse pulse) { if (pulse == Pulse.LOW) low++; else high++; }
    }

    record Signal(Module from, Module to, Pulse pulse) {
        static Signal of(Module from, Module to, Pulse pulse) { return new Signal(from, to, pulse); }
    }
    interface Module extends Comparable<Module> {
        String name();
        void input(Pulse pulse, Module from);
        void setOutputs(List<Module> outputs);
        List<Module> getOutputs();
        @Override default int compareTo(Module o) { return name().compareTo(o.name()); }
    }
    static class Output implements Module {
        private String name;
        public Output(String name) { this.name = name; }
        @Override public String name() { return name; }
        @Override public void input(Pulse pulse, Module ignore) { }
        @Override public void setOutputs(List<Module> outputs) { }
        @Override public List<Module> getOutputs() { return List.of(); }
    }
    static class Broadcaster implements Module {
        private String name;
        private List<Module> outputs = List.of();
        private Queue<Signal> pulseQueue;

        public Broadcaster(String name, Queue<Signal> pulseQueue) {
            this.name = name;
            this.pulseQueue = pulseQueue;
        }

        @Override public void setOutputs(List<Module> outputs) { this.outputs = outputs; }
        @Override public List<Module> getOutputs() { return outputs; }

        @Override
        public String name() { return name; }

        @Override
        public void input(Pulse pulse, Module ignore) {
            for (var module: outputs)
                pulseQueue.add(Signal.of(this, module, pulse));
        }
    }
    static class FlipFlop implements Module {
        boolean isOn = false;
        private String name;
        private List<Module> outputs = List.of();
        private Queue<Signal> pulseQueue;

        public FlipFlop(String name, Queue<Signal> pulseQueue) {
            this.name = name;
            this.pulseQueue = pulseQueue;
        }

        @Override
        public void setOutputs(List<Module> outputs) { this.outputs = outputs; }
        @Override public List<Module> getOutputs() { return outputs; }

        @Override
        public String name() { return name; }

        @Override
        public void input(Pulse pulse, Module ignore) {
            if (pulse == Pulse.LOW) {
                for (var module: outputs)
                    pulseQueue.add(Signal.of(this, module, isOn ? Pulse.LOW : Pulse.HIGH));
                isOn = !isOn;
            }
        }
    }
    static class Conjunction implements Module {
        private String name;
        private List<Module> outputs = List.of();
        private Queue<Signal> pulseQueue;
        private Map<Module, Pulse> pulseMemory;

        public Conjunction(String name, Queue<Signal> pulseQueue) {
            this.name = name;
            this.pulseQueue = pulseQueue;
        }

        void setMemory(Map<Module, Pulse> pulseMemory) { this.pulseMemory = pulseMemory; }

        @Override
        public void setOutputs(List<Module> outputs) { this.outputs = outputs; }
        @Override public List<Module> getOutputs() { return outputs; }

        @Override
        public String name() { return name; }

        @Override
        public void input(Pulse pulse, Module from) {
            pulseMemory.put(from, pulse);
            if (pulseMemory.values().stream().anyMatch(p -> p == Pulse.LOW))
                send(Pulse.HIGH);
            else
                send(Pulse.LOW);
        }

        private void send(Pulse pulse) {
            for (var module: outputs)
                pulseQueue.add(Signal.of(this, module, pulse));
        }
    }
}
