package se.liu.jenca01.adventofcode.utils;

import java.math.BigInteger;

public class Mod {
	static boolean mod(BigInteger num, int mod) {
		switch (mod) {
		case 1: return true;
		case 2: return mod2(num);
		case 3: return mod3(num);
		case 4: return mod4(num);
		case 5: return mod5(num);
		case 6: return mod2(num) && mod3(num);
		case 7: return mod7(num);
		case 8: return mod8(num);
		case 9: return mod9(num);
		case 10: return mod2(num) && mod5(num);
		case 11: return mod11(num);
		case 12: return mod3(num) && mod4(num);
		case 13: return mod13(num);
		case 14: return mod2(num) && mod7(num);
		case 15: return mod3(num) && mod5(num);
		case 16: return mod16(num);
		case 17: return mod17(num);
		case 18: return mod2(num) && mod9(num);
		case 19: return mod19(num);
		case 20: return mod4(num) && mod5(num);
		case 21: return mod3(num) && mod7(num);
		case 22: return mod2(num) && mod11(num);
		case 23: return mod23(num);
		default:
			throw new RuntimeException("Not implemented for " + mod);
		}
	}

	private static boolean mod23(BigInteger num) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not implemented");
	}

	private static boolean mod19(BigInteger num) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not implemented");
	}

	private static boolean mod17(BigInteger num) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not implemented");
	}

	private static boolean mod16(BigInteger num) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not implemented");
	}

	private static boolean mod13(BigInteger num) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not implemented");
	}

	private static boolean mod2(BigInteger num) {
		var numS = num.toString();
		var numC = numS.substring(numS.length() - 1);
		return "02468".contains(numC);
	}

	private static boolean mod3(BigInteger num) {
		var sum = 0;
		for (var c: num.toString().toCharArray())
			sum += c - '0';
		return sum % 3 == 0;
	}

	private static boolean mod4(BigInteger num) {
		var numS = num.toString();
		var numC = numS.substring(numS.length() - 2);
		return Integer.parseInt(numC) % 4 == 0;
	}

	private static boolean mod5(BigInteger num) {
		var numS = num.toString();
		var numC = numS.substring(numS.length() - 1);
		return "05".contains(numC);
	}

	private static boolean mod7(BigInteger num) {
		var numS = num.toString();
		var digits = numS.length();
		var sum = 0;
		var sign = 1;
		for (int pos = digits; pos > 2; pos =- 3) {
			var part = xDigits(numS, pos-3, 3);
			sum += Integer.parseInt(part) * sign;
			sign *= -1;
		}
		return sum % 7 == 0;
	}

	private static boolean mod8(BigInteger num) {
		var numS = num.toString();
		var digits = numS.length();
		if (digits < 3)
			return Integer.parseInt(numS) % 8 == 0;
		var last3 = numS.substring(digits - 3);
		if ("02468".contains(last3.substring(0,1)))
			return Integer.parseInt(numS.substring(1)) % 8 == 0;
		return (Integer.parseInt(numS.substring(1)) + 4) % 8 == 0;
	}

	private static boolean mod9(BigInteger num) {
		var sum = 0;
		for (var c: num.toString().toCharArray())
			sum += c - '0';
		return sum % 9 == 0;
	}

	private static boolean mod11(BigInteger num) {
		var numS = num.toString().toCharArray();
		var sum = 0;
		var sign = 1;
		for (var c: numS) {
			sum += (c - '0') * sign;
			sign *= -1;
		}
		return sum % 7 == 0;
	}

	private static String xDigits(String numS, int pos, int x) {
		while (pos < 0)  {
			pos++;
			x--;
		}
		return numS.substring(pos, pos+x);
	}
}
