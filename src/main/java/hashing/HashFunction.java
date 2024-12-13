package hashing;

import java.security.GeneralSecurityException;

@FunctionalInterface
interface HashFunction {
	String hash(char[] password, byte[] salt) throws GeneralSecurityException;
}
