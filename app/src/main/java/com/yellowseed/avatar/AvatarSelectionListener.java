package com.yellowseed.avatar;

public interface AvatarSelectionListener {

    void onAccessoriesSelected(int position);
    void onSubAccessoriesSelected(int position, AvatarPart avatarPart);
}
