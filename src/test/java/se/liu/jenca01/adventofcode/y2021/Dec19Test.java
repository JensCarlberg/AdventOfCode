package se.liu.jenca01.adventofcode.y2021;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

class Dec19Test {

	Dec19 out = new Dec19();

	@Test
	void scanSimgleScannerData() {
		var scannerInput = "--- scanner 0 ---\n"
				+ "404,-588,-901\n"
				+ "528,-643,409\n"
				+ "-838,591,734\n"
				+ "390,-675,-793\n"
				+ "-537,-823,-458\n"
				+ "-485,-357,347\n"
				+ "-345,-311,381\n"
				+ "-661,-816,-575\n"
				+ "-876,649,763\n"
				+ "-618,-824,-621\n"
				+ "553,345,-567\n"
				+ "474,580,667\n"
				+ "-447,-329,318\n"
				+ "-584,868,-557\n"
				+ "544,-627,-890\n"
				+ "564,392,-477\n"
				+ "455,729,728\n"
				+ "-892,524,684\n"
				+ "-689,845,-530\n"
				+ "423,-701,434\n"
				+ "7,-33,-71\n"
				+ "630,319,-379\n"
				+ "443,580,662\n"
				+ "-789,900,-551\n"
				+ "459,-707,401\n"
				+ "";
		var res = out.convertData(Arrays.asList(scannerInput.split("\n")).stream());
		assertTrue(res != null, "convertData should return non-null result");
		assertEquals(1,  res.size(), "conbvertData should result in simgle scanner set");
		assertEquals(25, res.get(0), "Beacon set should contain 25 beacons");
	}

}
