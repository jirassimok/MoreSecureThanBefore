package hashing;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * Simple hash functions based on the Java Cryptography Architecture's
 * password-based encryption tools.
 */
class SimpleJCAHash implements HashFunction {
	static final SimpleJCAHash SHA256_V1 = new SimpleJCAHash("PBKDF2WithHmacSha256", 600_000, 512);
	static final SimpleJCAHash SHA512_V1 = new SimpleJCAHash("PBKDF2WithHmacSha512", 210_000, 512);

	private final String algorithm;
	private final int hashSize;
	private final int iterations;

	private SimpleJCAHash(String algorithm, int iterations, int hashSize) {
		this.algorithm = algorithm;
		this.iterations = iterations;
		this.hashSize = hashSize;
	}

	@Override
	public String hash(char[] password, byte[] salt)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterations, hashSize);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
		return encodeHash(keyFactory.generateSecret(keySpec).getEncoded());
	}

	private static String encodeHash(byte[] hash) {
		return Base64.getEncoder().encodeToString(hash);
	}
}
