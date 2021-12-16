package se.liu.jenca01.adventofcode.y2021;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec16 extends Christmas {

    enum PacketType {
        Sum,
        Product,
        Min,
        Max,
        Literal,
        GreaterThan,
        LessThan,
        Equals;

        static PacketType typeOf(byte type) {
            switch(type) {
                case 0: return Sum;
                case 1: return Product;
                case 2: return Min;
                case 3: return Max;
                case 4: return Literal;
                case 5: return GreaterThan;
                case 6: return LessThan;
                case 7: return Equals;
            }
            throw new RuntimeException();
        }
    }

    static class Packet {
        private final int version;
        private final PacketType type;
        private final List<Packet> subPackets;

        public Packet(int version, PacketType type, List<Packet> subPackets) {
            this.version = version;
            this.type = type;
            this.subPackets = subPackets;
        }
        
        public long value() {
            switch (type) {
                case Sum: return sum();
                case Product: return product();
                case Min: return min();
                case Max: return max();
                case Literal: return ((LiteralPacket) this).value;
                case GreaterThan: return greaterThan();
                case LessThan: return lessThan();
                case Equals: return eq();
            }
            throw new RuntimeException();
        }

        private long sum() {
            var start = 0L;
            for(var p : subPackets)
                start += p.value();
            return start;
        }

        private long product() {
            var start = 1L;
            for(var p : subPackets)
                start *= p.value();
            return start;
        }

        private long min() {
            return subPackets.stream().map(p -> p.value()).sorted().findFirst().get();
        }

        private long max() {
            return subPackets.stream().map(p -> -1 * p.value()).sorted().findFirst().get() * -1 ;
        }

        private long greaterThan() {
            if (subPackets.get(0).value() > subPackets.get(1).value())
                return 1;
            return 0;
        }

        private long lessThan() {
            if (subPackets.get(0).value() < subPackets.get(1).value())
                return 1;
            return 0;
        }

        private long eq() {
            if (subPackets.get(0).value() == subPackets.get(1).value())
                return 1;
            return 0;
        }
    }

    static class LiteralPacket extends Packet {

        private long value;

        public LiteralPacket(int version, long value) {
            super(version, PacketType.Literal, List.of());
            this.value = value;
        }
    }

    long sampleAnswer1 = 12;
    long sampleAnswer2 = 46;

    int pos = 0;
    int dep = 0;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec16();
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

    private String convertData(Stream<String> data) {
        StringBuilder sb = new StringBuilder();
        for(var c: toList(data).get(0).toCharArray())
            switch(c) {
                case '0': sb.append("0000"); break;
                case '1': sb.append("0001"); break;
                case '2': sb.append("0010"); break;
                case '3': sb.append("0011"); break;
                case '4': sb.append("0100"); break;
                case '5': sb.append("0101"); break;
                case '6': sb.append("0110"); break;
                case '7': sb.append("0111"); break;
                case '8': sb.append("1000"); break;
                case '9': sb.append("1001"); break;
                case 'A': sb.append("1010"); break;
                case 'B': sb.append("1011"); break;
                case 'C': sb.append("1100"); break;
                case 'D': sb.append("1101"); break;
                case 'E': sb.append("1110"); break;
                case 'F': sb.append("1111"); break;
                default: throw new RuntimeException("wtf? " + c);
            }
        return sb.toString();
    }

    public long solve1(Stream<String> stream) {
        var data = convertData(stream);
        pos = 0;
        Packet packet = parsePacket(data);
        return sumVersions(packet);
    }

    private long sumVersions(Packet packet) {
        var versionSum = packet.version;
        for(var p: packet.subPackets)
            versionSum += sumVersions(p);
        return versionSum;
    }

    private Packet parsePacket(String data) {
        var version = getVersionOrType(data);
        byte type = getVersionOrType(data);

        switch (type) {
            case 4:
                return parseLiteral(version, data);
            default:
                return parseOperator(version, PacketType.typeOf(type), data);
        }
    }

    private Packet parseOperator(int version, PacketType type, String data) {
        var lengthTypeId = getX(data, 1);
        switch(lengthTypeId) {
            case "0": return parseLenOperator(version, type, data);
            case "1": return parseCountOperator(version, type, data);
            default: throw new RuntimeException("wtf? " + lengthTypeId);
        }
    }

    private Packet parseCountOperator(int version, PacketType type, String data) {
        var count = Integer.parseInt(getX(data, 11), 2);
        var subPackets = new ArrayList<Packet>();;
        while (count-- > 0)
            subPackets.add(parsePacket(data));
        return new Packet(version, type, subPackets);
    }

    private Packet parseLenOperator(int version, PacketType type, String data) {
        var len = Integer.parseInt(getX(data, 15), 2);
        var startPos = pos;
        var subPackets = new ArrayList<Packet>();;
        while (pos - startPos < len)
            subPackets.add(parsePacket(data));
        return new Packet(version, type, subPackets);
    }

    private Packet parseLiteral(int version, String data) {
        var startPos = pos;
        StringBuilder bits = new StringBuilder();
        while (fillBits(data, bits)) {}
        return new LiteralPacket(version, Long.parseLong(bits.toString(), 2));
    }

    private boolean fillBits(String data, StringBuilder bits) {
        var packet = getX(data, 5);
        bits.append(packet.substring(1));
        return packet.charAt(0) == '1';
    }

    private byte getVersionOrType(String data) {
        return Byte.parseByte(getX(data, 3), 2);
    }
    
    private String getX(String data, int x) {
        pos +=x;
        String dataPart = data.substring(pos-x, pos);
        indent();
        System.out.println(dataPart);
        return dataPart;
    }

    private void indent() {
        for(int i=0; i<dep; i++)
            System.out.print("  ");
    }

    public long solve2(Stream<String> stream) {
        var data = convertData(stream);
        pos = 0;
        Packet packet = parsePacket(data);
        return packet.value();
    }
}

/*



*/