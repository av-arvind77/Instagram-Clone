package com.yellowseed.model;

import java.io.Serializable;

public class ExploreModel implements Serializable {
    private boolean isSeeMoreClicked;

    public boolean isSeeMoreClicked() {
        return isSeeMoreClicked;
    }

    public void setSeeMoreClicked(
            boolean seeMoreClicked) {
        isSeeMoreClicked = seeMoreClicked;
    }
}
