package com.yellowseed.model;


import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by mobiloitte on 22/5/18.
 */

public class SocialPostPhotoModel {
    private File file;
    private String url;
    private String file_type;
    private String type;
    private String _destroy;
    private String id;
    private String thumbServer;
    private Bitmap thumbLocal;

    public String getThumbServer() {
        return thumbServer;
    }

    public void setThumbServer(String thumbServer) {
        this.thumbServer = thumbServer;
    }

    public Bitmap getThumbLocal() {
        return thumbLocal;
    }

    public void setThumbLocal(Bitmap thumbLocal) {
        this.thumbLocal = thumbLocal;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String get_destroy() {
        return _destroy;
    }

    public void set_destroy(String _destroy) {
        this._destroy = _destroy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
