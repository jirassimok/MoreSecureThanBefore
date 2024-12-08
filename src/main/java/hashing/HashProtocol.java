package hashing;

import java.security.GeneralSecurityException;

/**
 * The types of password hash understood by {@link PasswordHasher}.
 */
public enum HashProtocol {
	/**
	 * Fake hash that does nothing at all.
	 */
	SHA256V1(Sha256Hash.getV1()),
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
