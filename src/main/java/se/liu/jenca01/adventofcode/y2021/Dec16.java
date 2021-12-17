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

--- Day 16: Packet Decoder ---
As you leave the cave and reach open waters, you receive a transmission from the Elves back on the ship.

The transmission was sent using the Buoyancy Interchange Transmission System (BITS), a method of packing numeric expressions into a binary sequence. Your submarine's computer has saved the transmission in hexadecimal (your puzzle input).

The first step of decoding the message is to convert the hexadecimal representation into binary. Each character of hexadecimal corresponds to four bits of binary data:

0 = 0000
1 = 0001
2 = 0010
3 = 0011
4 = 0100
5 = 0101
6 = 0110
7 = 0111
8 = 1000
9 = 1001
A = 1010
B = 1011
C = 1100
D = 1101
E = 1110
F = 1111
The BITS transmission contains a single packet at its outermost layer which itself contains many other packets. The hexadecimal representation of this packet might encode a few extra 0 bits at the end; these are not part of the transmission and should be ignored.

Every packet begins with a standard header: the first three bits encode the packet version, and the next three bits encode the packet type ID. These two values are numbers; all numbers encoded in any packet are represented as binary with the most significant bit first. For example, a version encoded as the binary sequence 100 represents the number 4.

Packets with type ID 4 represent a literal value. Literal value packets encode a single binary number. To do this, the binary number is padded with leading zeroes until its length is a multiple of four bits, and then it is broken into groups of four bits. Each group is prefixed by a 1 bit except the last group, which is prefixed by a 0 bit. These groups of five bits immediately follow the packet header. For example, the hexadecimal string D2FE28 becomes:

110100101111111000101000
VVVTTTAAAAABBBBBCCCCC
Below each bit is a label indicating its purpose:

The three bits labeled V (110) are the packet version, 6.
The three bits labeled T (100) are the packet type ID, 4, which means the packet is a literal value.
The five bits labeled A (10111) start with a 1 (not the last group, keep reading) and contain the first four bits of the number, 0111.
The five bits labeled B (11110) start with a 1 (not the last group, keep reading) and contain four more bits of the number, 1110.
The five bits labeled C (00101) start with a 0 (last group, end of packet) and contain the last four bits of the number, 0101.
The three unlabeled 0 bits at the end are extra due to the hexadecimal representation and should be ignored.
So, this packet represents a literal value with binary representation 011111100101, which is 2021 in decimal.

Every other type of packet (any packet with a type ID other than 4) represent an operator that performs some calculation on one or more sub-packets contained within. Right now, the specific operations aren't important; focus on parsing the hierarchy of sub-packets.

An operator packet contains one or more packets. To indicate which subsequent binary data represents its sub-packets, an operator packet can use one of two modes indicated by the bit immediately after the packet header; this is called the length type ID:

If the length type ID is 0, then the next 15 bits are a number that represents the total length in bits of the sub-packets contained by this packet.
If the length type ID is 1, then the next 11 bits are a number that represents the number of sub-packets immediately contained by this packet.
Finally, after the length type ID bit and the 15-bit or 11-bit field, the sub-packets appear.

For example, here is an operator packet (hexadecimal string 38006F45291200) with length type ID 0 that contains two sub-packets:

00111000000000000110111101000101001010010001001000000000
VVVTTTILLLLLLLLLLLLLLLAAAAAAAAAAABBBBBBBBBBBBBBBB
The three bits labeled V (001) are the packet version, 1.
The three bits labeled T (110) are the packet type ID, 6, which means the packet is an operator.
The bit labeled I (0) is the length type ID, which indicates that the length is a 15-bit number representing the number of bits in the sub-packets.
The 15 bits labeled L (000000000011011) contain the length of the sub-packets in bits, 27.
The 11 bits labeled A contain the first sub-packet, a literal value representing the number 10.
The 16 bits labeled B contain the second sub-packet, a literal value representing the number 20.
After reading 11 and 16 bits of sub-packet data, the total length indicated in L (27) is reached, and so parsing of this packet stops.

As another example, here is an operator packet (hexadecimal string EE00D40C823060) with length type ID 1 that contains three sub-packets:

11101110000000001101010000001100100000100011000001100000
VVVTTTILLLLLLLLLLLAAAAAAAAAAABBBBBBBBBBBCCCCCCCCCCC
The three bits labeled V (111) are the packet version, 7.
The three bits labeled T (011) are the packet type ID, 3, which means the packet is an operator.
The bit labeled I (1) is the length type ID, which indicates that the length is a 11-bit number representing the number of sub-packets.
The 11 bits labeled L (00000000011) contain the number of sub-packets, 3.
The 11 bits labeled A contain the first sub-packet, a literal value representing the number 1.
The 11 bits labeled B contain the second sub-packet, a literal value representing the number 2.
The 11 bits labeled C contain the third sub-packet, a literal value representing the number 3.
After reading 3 complete sub-packets, the number of sub-packets indicated in L (3) is reached, and so parsing of this packet stops.

For now, parse the hierarchy of the packets throughout the transmission and add up all of the version numbers.

Here are a few more examples of hexadecimal-encoded transmissions:

8A004A801A8002F478 represents an operator packet (version 4) which contains an operator packet (version 1) which contains an operator packet (version 5) which contains a literal value (version 6); this packet has a version sum of 16.
620080001611562C8802118E34 represents an operator packet (version 3) which contains two sub-packets; each sub-packet is an operator packet that contains two literal values. This packet has a version sum of 12.
C0015000016115A2E0802F182340 has the same structure as the previous example, but the outermost packet uses a different length type ID. This packet has a version sum of 23.
A0016C880162017C3686B18A3D4780 is an operator packet that contains an operator packet that contains an operator packet that contains five literal values; it has a version sum of 31.
Decode the structure of your hexadecimal-encoded BITS transmission; what do you get if you add up the version numbers in all packets?


--- Part Two ---
Now that you have the structure of your transmission decoded, you can calculate the value of the expression it represents.

Literal values (type ID 4) represent a single number as described above. The remaining type IDs are more interesting:

Packets with type ID 0 are sum packets - their value is the sum of the values of their sub-packets. If they only have a single sub-packet, their value is the value of the sub-packet.
Packets with type ID 1 are product packets - their value is the result of multiplying together the values of their sub-packets. If they only have a single sub-packet, their value is the value of the sub-packet.
Packets with type ID 2 are minimum packets - their value is the minimum of the values of their sub-packets.
Packets with type ID 3 are maximum packets - their value is the maximum of the values of their sub-packets.
Packets with type ID 5 are greater than packets - their value is 1 if the value of the first sub-packet is greater than the value of the second sub-packet; otherwise, their value is 0. These packets always have exactly two sub-packets.
Packets with type ID 6 are less than packets - their value is 1 if the value of the first sub-packet is less than the value of the second sub-packet; otherwise, their value is 0. These packets always have exactly two sub-packets.
Packets with type ID 7 are equal to packets - their value is 1 if the value of the first sub-packet is equal to the value of the second sub-packet; otherwise, their value is 0. These packets always have exactly two sub-packets.
Using these rules, you can now work out the value of the outermost packet in your BITS transmission.

For example:

C200B40A82 finds the sum of 1 and 2, resulting in the value 3.
04005AC33890 finds the product of 6 and 9, resulting in the value 54.
880086C3E88112 finds the minimum of 7, 8, and 9, resulting in the value 7.
CE00C43D881120 finds the maximum of 7, 8, and 9, resulting in the value 9.
D8005AC2A8F0 produces 1, because 5 is less than 15.
F600BC2D8F produces 0, because 5 is not greater than 15.
9C005AC2F8F0 produces 0, because 5 is not equal to 15.
9C0141080250320F1802104A08 produces 1, because 1 + 3 = 2 * 2.
What do you get if you evaluate the expression represented by your hexadecimal-encoded BITS transmission?

*/