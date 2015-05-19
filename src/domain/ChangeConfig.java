package domain;

/**
 *
 * @author Frederik
 */
public class ChangeConfig {

    public static final int BOOK_VERSION_ID = 1;
    public static final int CD_VERSION_ID = 2;
    public static final int DVD_VERSION_ID = 4;
    public static final int GAME_VERSION_ID = 8;
    public static final int STORY_BAG_VERSION_ID = 16;
    public static final int ITEM_COPY_VERSION_ID = 32;
    public static final int USER_VERSION_ID = 64;
    public static final int LOAN_VERSION_ID = 128;

    public static Class<? extends Changeable> createEntityClass(int versionId) {
	switch (versionId) {
	    case BOOK_VERSION_ID:
		return Book.class;
	    case CD_VERSION_ID:
		return Cd.class;
	    case DVD_VERSION_ID:
		return Dvd.class;
	    case GAME_VERSION_ID:
		return Game.class;
	    case STORY_BAG_VERSION_ID:
		return StoryBag.class;
	    case ITEM_COPY_VERSION_ID:
		return ItemCopy.class;
	    case USER_VERSION_ID:
		return User.class;
	    case LOAN_VERSION_ID:
		return Loan.class;
	    default:
		return null;
	}
    }
}
