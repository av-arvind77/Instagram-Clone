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

/**
 * POD class describing an avatar part
 */
public class AvatarPart {
    public final String filename;
    public final String type;
    public final int x;
    public final int y;
    public final String id;
    public boolean isSelected;

    public AvatarPart(String filename, int x, int y, String type, String id,boolean isSelected) {
        this.filename = filename;
        this.x = x;
        this.y = y;
        this.type = type;
        this.id = id;
        this.isSelected = isSelected;
    }
}
