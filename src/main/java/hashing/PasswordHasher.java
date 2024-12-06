package hashing;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class PasswordHasher {
	private static final int SALT_BYTES = 16;
	public static final HashProtocol DEFAULT_PROTOCOL = HashProtocol.DUMMY_NO_HASH;

	// Use Java's most-preferred secure PRNG and let it seed itself.
	private static final SecureRandom random = new SecureRandom();

	private PasswordHasher() {}

	public static String generateSalt() {
		byte[] salt = new byte[SALT_BYTES];
		random.nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt);
	}

	/**
	 * Hash a password with the given salt and the default protocol.
	 */
	public static String hashPassword(char[] password, String salt) {
		return hashPassword(DEFAULT_PROTOCOL, password, salt);
	}

	/**
	 * Hash a password with the given salt.
	 *
	 * <p>Clears the password array.
	 */
	public static String hashPassword(HashProtocol protocol, char[] password, String salt) {
		try {
			return protocol.hash(password, salt);
		} finally {
			Arrays.fill(password, '\0');
		}
	}
}
