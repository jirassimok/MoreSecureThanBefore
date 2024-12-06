package hashing;

@FunctionalInterface
interface HashFunction {
	String hash(char[] password, String salt);
}
