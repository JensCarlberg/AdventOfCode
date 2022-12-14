package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec13 extends Christmas {

    long sampleAnswer1 = 13;
    long sampleAnswer2 = 0;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec13();
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

    private List<Pair> convertData(Stream<String> data) {
        var pairs = new ArrayList<Pair>();
        var lines = data.toList();
        Packet left = null;
        for(var line: lines) {
            if (line.strip().length() == 0) continue;
            if (left == null)
                left = buildPacket(line);
            else {
                pairs.add(new Pair(left, buildPacket(line)));
                left = null;
            }
        }
        return pairs;
    }

    private Packet buildPacket(String packetDef) {
        var packets = new Stack<Packet>();
        Packet packet;
        for (var c: packetDef.toCharArray()) {
            switch (c) {
                case '[': {
                    // börja samla tecken
                    break;
                }
                case ']': {
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unexpected value: " + c);
            }
        }
        // TODO Auto-generated method stub
        return null;
    }

    public long solve1(Stream<String> stream) {
        return 0;
    }

    public long solve2(Stream<String> stream) {
        return 0;
    }


    record Pair(Packet left, Packet right) {}
    record Packet(List<Packet> packets, int leaf) implements Comparable<Packet> {

        boolean isLeaf() { return packets == null || packets.size() == 0; }

        @Override
        public int compareTo(Packet right) {
            var left = this;
            if (left.isLeaf())
                return compareLeaf(left.leaf, right);
            if (right.isLeaf())
                return -compareLeaf(right.leaf, left);
            var rightSize = right.packets.size();
            for(var i = 0; i<left.packets.size(); i++) {
                if (i == rightSize) return 1;
                var compRes = left.packets.get(i).compareTo(right.packets.get(i));
                if (compRes != 0) return compRes;
            }
            return left.packets.size() - right.packets.size(); 
        }
        
        int compareLeaf(int left, Packet right) {
            if (right.isLeaf())
                return left - right.leaf;
            var list = new ArrayList<Packet>();
            list.add(new Packet(null, left));
            var leftAsPacket = Packet.list(list);
            return leftAsPacket.compareTo(right);
        }
        
        @Override
        public String toString() {
            if (isLeaf()) return "" + leaf;
            var tos = new StringBuilder();
            tos.append("[");
            tos.append(String.join(",", packets.stream().map(s -> s.toString()).toList()));
            tos.append("]");
            return tos.toString();
        }
        
        static Packet list(List<Packet> packets) { return new Packet(packets, 0); }
        static Packet leaf(int leaf) { return new Packet(null, leaf); }
        
    }
}

/*



*/