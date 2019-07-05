/*
Copyright 2016 Aurélien Gâteau

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.yellowseed.avatar;

import android.graphics.Point;
import android.text.TextUtils;
import android.util.JsonReader;

import com.yellowseed.webservices.response.avatar.AvatarCategory;
import com.yellowseed.webservices.response.avatar.AvatarImage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.Vector;

/**
 * Holds all avatar parts
 *
 * The json file looks like this:
 *
 * [
 *     ["$category", "$filename", $x, $y],
 *     ...
 * ]
 *
 * all files of a same category are grouped together
 *
 */
public class AvatarPartDb {
    private Map<String, Vector<AvatarPart>> mParts = new HashMap<>();

/*    public static final String[] PART_NAMES_MALE = {"dress","face", "hair","beard","cap" };
    public static final String[] PART_NAMES_FEMALE = {"dress","face", "hair" };*/

    public int getPartCount(String partName) {
        return mParts.get(partName).size();
    }

    public AvatarPart getPart(String partName, int idx) {
        return mParts.get(partName).get(idx);
    }


    public void addParts(List<AvatarCategory> avatarCategories , boolean isMale) throws IOException {
        Vector<AvatarPart> parts = null;

        for (AvatarCategory avatarCategory:avatarCategories) {
            parts = new Vector<>();
            String category = avatarCategory.getName();
            mParts.put(avatarCategory.getName(), parts);
            for (AvatarImage avatarImage:avatarCategory.getAvatarImage()) {
                String filename = avatarImage.getFile().getUrl();
                Point point = getPoint(category,isMale);
                AvatarPart part = null;
                if (!TextUtils.isEmpty(filename)) {
                    part = new AvatarPart(filename, point.x, point.y, category, avatarImage.getId(),true);
                }
                assert parts != null;
                parts.add(part);
            }
        }

    }


    public Point getPoint(String category, boolean isMale){
        Point point = new Point();
        switch (category){
            case "Skin Color":
                if(isMale) {
                    point.y = 140;
                }else{
                    point.y = 150;
                }
                break;
            case "Cloths":
                if(isMale) {
                    point.y = 445;
                }else{
                    point.y = 300;
                }
                break;
            case "Hair Style":
                if(isMale) {
                    point.y = 110;
                }else{
                    point.y = 100;
                }
                break;
            case "Hair Color":
                if(isMale) {
                    point.y = 90;
                }else{
                    point.y = 100;
                }
                break;
            case "Beared Shape":
                point.y = 330;
                break;
            case "Beared Color":
                point.y = 330;
                break;
            case "Spects":
                if(isMale) {
                    point.y = 275;
                }else{
                    point.y = 198;
                }
                break;
            case "Hat":
                if(isMale) {
                    point.y = 0;
                }else{
                    point.y = 0;
                }
                break;
            case "Lips Shape":
                point.y = 262;
                break;

            case "Lips color":
                point.y = 262;
                break;

        }

        return point;
    }

}