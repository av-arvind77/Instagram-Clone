package com.yellowseed.model.reqModel;

import java.io.Serializable;

/**
 * Created by ankit_mobiloitte on 7/6/18.
 */

public class SchoolModel  implements Serializable{
    private String id;

    private String school_name;
    private String college_name;
    private String worked_at;
    private String _destroy;

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getCollege_name() {
        return college_name;
    }

    public void setCollege_name(String college_name) {
        this.college_name = college_name;
    }

    public String getWork_at() {
        return worked_at;
    }

    public void setWork_at(String worked_at) {
        this.worked_at = worked_at;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    private String designation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String get_destroy() {
        return _destroy;
    }

    public void set_destroy(String _destroy) {
        this._destroy = _destroy;
    }
}
