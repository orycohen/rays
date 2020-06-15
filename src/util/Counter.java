package util;

import java.math.BigInteger;

public class Counter {
	private BigInteger _counter = new BigInteger("0");
	private static BigInteger _one = new BigInteger("1");
	public void increase() {
		_counter = _counter.add(_one);
	}
	public BigInteger getNumber() {return _counter;}
}
