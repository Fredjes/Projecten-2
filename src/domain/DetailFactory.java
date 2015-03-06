package domain;

import gui.DetailViewBook;
import gui.DetailViewCd;
import gui.DetailViewDvd;
import gui.DetailViewGame;
import gui.DetailViewStoryBag;
import gui.DetailViewUser;
import javafx.scene.control.TabPane;

public class DetailFactory {

    public static TabPane getDetailPane(FilterOption o) {

        if (o == FilterOption.BOOK) {
            return new DetailViewBook();
        } else if (o == FilterOption.CD) {
            return new DetailViewCd();
        } else if (o == FilterOption.DVD) {
            return new DetailViewDvd();
        } else if (o == FilterOption.GAME) {
            return new DetailViewGame();
        } else if (o == FilterOption.STORYBAG) {
            return new DetailViewStoryBag();
        } else if (o == FilterOption.USER) {
            return new DetailViewUser();
        }
        return null;
    }
}
