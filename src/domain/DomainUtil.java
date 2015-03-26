package domain;

/**
 * Util class for domain specific rules.
 *
 * @author Brent
 */
public class DomainUtil {

    /**
     * Check whether a password complies with the domain rules.
     *
     * @param password The password that should be checked
     * @return true if the password passes the constraints, false otherwise
     */
    public static boolean isValidPassword(String password) {
	return password.matches("(.*[A-Z].*[0-9].*|.*[0-9].*[A-Z].*)") && password.length() >= 8;
    }
}
