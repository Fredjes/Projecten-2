package domain;

/**
 *
 * @author Brent
 */
public class DomainUtil {

    public static boolean isValidPassword(String password) {
	return password.matches("(.*[A-Z].*[0-9].*|.*[0-9].*[A-Z].*)") && password.length() >= 8;
    }
}
