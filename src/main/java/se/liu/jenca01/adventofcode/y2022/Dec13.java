package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec13 extends Christmas {

    long sampleAnswer1 = 13;
    long sampleAnswer2 = 140;

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
    	var lines = data.toList();
        return convertData(lines);
    }

	private List<Pair> convertData(List<String> lines) {
		var pairs = new ArrayList<Pair>();
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
    	var symbols = new ArrayList<Character>();
    	for (var c: packetDef.toCharArray())
    		symbols.add(c);
    	if (symbols.remove(0) != '[') throw new RuntimeException();
    	if (symbols.remove(symbols.size() - 1) != ']') throw new RuntimeException();
    	Packet packet = Packet.list(buildPacket(symbols));
		return packet;
    }

    private List<Packet> buildPacket(List<Character> symbols) {
        var packetList = new ArrayList<Packet>();
        Packet packet = null;
        while (symbols.size() > 0) {
        	var c = symbols.remove(0);
            switch (c) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            	packet = (packet == null) ? Packet.leaf(c - '0') : Packet.leaf(10*packet.leaf + c - '0');
            	break;
            case '[':
            	List<Packet> buildPacket = buildPacket(symbols);
				Packet list = Packet.list(buildPacket);
				packetList.add(list);
            	break;
            case ']':
            	if (packet != null)
            		packetList.add(packet);
            	return packetList;
            case ',':
            	if (packet != null)
            		packetList.add(packet);
            	packet = null;
            	break;
            default:
            	throw new RuntimeException("Unknown symbol: " + c);
            }
        }
        if (packet != null)
        	packetList.add(packet);
        return packetList;
    }

    public long solve1(Stream<String> stream) {
    	var puzzle = convertData(stream);
    	var orderIndexSum = 0;
    	for (int i = 0; i<puzzle.size(); i++) {
    		var pair = puzzle.get(i);
    		Packet left = pair.left;
			Packet right = pair.right;
			if (left.compareTo(right) < 0)
    			orderIndexSum += i + 1;
    	}
        return orderIndexSum;
    }

    public long solve2(Stream<String> stream) {
    	var puzzle = convertData(stream);
    	var packets = new TreeSet<Packet>();
    	for (var p:puzzle) {
    		packets.add(p.left);
    		packets.add(p.right);
    	}
    	var divider1 = buildPacket("[[2]]");
    	var divider2 = buildPacket("[[6]]");
    	packets.add(divider1);
    	packets.add(divider2);
    	var sortedList = packets.stream().toList();
    	var decoderKey = (sortedList.indexOf(divider1) + 1L) * (sortedList.indexOf(divider2) + 1L);
        return decoderKey;
    }


    record Pair(Packet left, Packet right) {}
    static class Packet implements Comparable<Packet> {
    	private List<Packet> packets;
		private Integer leaf;

		Packet(List<Packet> packets, Integer leaf) {
			this.packets = packets;
			this.leaf = leaf;
		}

		boolean isLeaf() { return leaf != null; }

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
        	if (packets.size() == 0) return "[]";
        	var tos = new StringBuilder();
        	tos.append("[");
        	tos.append(String.join(",", packets.stream().map(s -> s.toString()).toList()));
        	tos.append("]");
        	return tos.toString();
        }

		static Packet list(List<Packet> packets) { return new Packet(packets, null); }
		static Packet leaf(int leaf) { return new Packet(null, leaf); }
		static Packet empty() { return new Packet(null, null); }
    }
}

/*



*/