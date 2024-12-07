package hashing;

import java.security.GeneralSecurityException;

/**
 * The types of password hash understood by {@link PasswordHasher}.
 */
public enum HashProtocol {
	/**
	 * Fake hash that does nothing at all.
	 */
	// TODO: Remove dummy has after adding a real hash
	DUMMY_NO_HASH((password, salt) -> String.valueOf(password)),
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
