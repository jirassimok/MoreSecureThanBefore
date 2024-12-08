package hashing;

import java.security.GeneralSecurityException;

/**
 * Hard-coded registry for {@link HashFunction}s.
 */
public enum HashProtocol {
	/**
	 * Fake hash that does nothing at all.
	 */
	SHA256V1(SimpleJCAHash.SHA512_V1),
	SHA512V1(SimpleJCAHash.SHA512_V1),
	;

	private final HashFunction hashFunction;

	HashProtocol(HashFunction hashFunction) {
		this.hashFunction = hashFunction;
	}

	// HashProtocol almost implements HashFunction, but this isn't public.
	String hash(char[] password, byte[] salt) throws GeneralSecurityException {
		return hashFunction.hash(password, salt);
	}
}
