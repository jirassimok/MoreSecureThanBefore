package hashing;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

@FunctionalInterface
interface HashFunction {
	String hash(char[] password, byte[] salt) throws GeneralSecurityException;
}
