package domain;

/**
 * An enumerator to specify on what should be filtered, to allow full support
 * each enum holds the class of what it represents.
 *
 * @author Frederik De Smedt
 */
public enum FilterOption {

    BOOK(Book.class), CD(Cd.class), DVD(Dvd.class), GAME(Game.class), STORYBAG(StoryBag.class), USER(User.class), ITEMCOPY(ItemCopy.class);

    private final Class<?> clazz;

    private FilterOption(Class<?> clazz) {
	this.clazz = clazz;
    }

    public Class<?> getFilterClass() {
	return this.clazz;
    }
}
