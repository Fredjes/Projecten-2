package persistence;

import domain.User;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Brent
 */
public class UserRepositoryTest {

    public UserRepositoryTest() {
    }

    @Test
    public void testPasswordHash() {
	String password = "Hello";
	String hash = UserRepository.getInstance().generatePasswordHash(password);
	Assert.assertEquals("f7ff9e8b7bb2e09b70935a5d785e0cc5d9d0abf0", hash);
    }

    @Test
    public void testSaveChangesPersistsUser() {
	User u = new User();
	u.setFirstName("test");
	u.setPasswordHash(UserRepository.getInstance().generatePasswordHash("password"));
	u.setUserType(User.UserType.TEACHER);
	u.setEmail("bla@bla.com");
	UserRepository.getInstance().add(u);
	UserRepository.getInstance().saveChanges();
	UserRepository.getInstance().sync();
	Assert.assertNotNull(UserRepository.getInstance().getUser(u.getName()));
    }

    @Test
    public void testDeleteUserIsSavedIntoDatabase() {
	User u = UserRepository.getInstance().getUser("test");
	UserRepository.getInstance().remove(u);
	UserRepository.getInstance().saveChanges();
	UserRepository.getInstance().sync();
	Assert.assertNull(UserRepository.getInstance().getUser("test"));
    }

    @Test
    public void testAuthenticate() {
	UserRepository.getInstance().authenticate("test", "password");
	User au = UserRepository.getInstance().getAuthenticatedUser();
	Assert.assertNotNull(au);
	Assert.assertEquals("test", au.getName());
    }

}
