package nz.ac.aut.hss.encrypt;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author Martin Schrimpf
 * @created 13.08.2014
 */
public class MillerRabin {

	/**
	 * @param args the command line arguments
	 */
	int k = 0;
	BigInteger a, q, current, next;
	BigInteger two = new BigInteger("2");
	BigInteger one = new BigInteger("1");
	BigInteger zero = new BigInteger("0");

	public MillerRabin() {
		k = 1;
		two = new BigInteger("2");
		one = new BigInteger("1");
		zero = new BigInteger("0");
	}

	public static BigInteger randomBig(BigInteger n, Random rand) {
		BigInteger r;
		do {
			r = new BigInteger(n.bitLength(), rand);
		} while (r.compareTo(n) >= 2);
		return r;
	}

	public boolean probablyPrime(BigInteger n) {

		//find q and k
		BigInteger nMinusOne = n.subtract(one);
		BigInteger q = nMinusOne;
		while (q.mod(two).equals(zero)) {
			q = q.divide(two);
			k++;
		}
		System.out.println("q is " + q);
		System.out.println("k is " + k);

		//generates new random number between 2 and n-1

		a = new BigInteger("0");
		if (a.compareTo(nMinusOne) != -1 || a.compareTo(two) != 1) {
			a = randomBig(n, new Random());
//            a=a.mod(nMinusOne);
			System.out.println("entered if");
		}
		System.out.println("a is " + a);

		//current=(a^q)%n
		current = a.modPow(q, n);

		//checks current=1
		if (current.compareTo(one) == 0) {
			System.out.println("1a");
			return true;
		}

		for (int i = 1; i < k; i++) {
			next = current.modPow(two, n);
			if (next.compareTo(one) == 0) {
				if (current.compareTo(n.subtract(one)) != 0) {
					System.out.println("1b");
					return false;
				} else {
					System.out.println("1c");
					return true;
				}
			}
			current = next;
		}
		System.out.println("1d");
		return false;
	}

	public boolean millerRabin(BigInteger n, int s) {
		if (n.compareTo(two) == 0) {
			System.out.println("2");
			return true;
		} else if (n.mod(two).compareTo(zero) == 0) {
			System.out.println("2");
			return false;
		}
		for (int j = 1; j < s; j++) {
			if (!probablyPrime(n)) {
				System.out.println("2");
				return false;
			}
		}
		System.out.println("2");
		return true;
	}
}
