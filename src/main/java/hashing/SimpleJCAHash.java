package hashing;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

class SimpleJCAHash implements HashFunction {
	private static final SimpleJCAHash V1 = new SimpleJCAHash(600_000, 512);

	private final int hashSize;
	private final int iterations;

	private SimpleJCAHash(int iterations, int hashSize) {
		this.iterations = iterations;
		this.hashSize = hashSize;
	}

	static SimpleJCAHash getV1() {
		return V1;
	}

	@Override
	public String hash(char[] password, byte[] salt)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterations, hashSize);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSha256");
		return encodeHash(keyFactory.generateSecret(keySpec).getEncoded());
	}

	private static String encodeHash(byte[] hash) {
		return Base64.getEncoder().encodeToString(hash);
	}
}
