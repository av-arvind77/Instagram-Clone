package com.yellowseed.model.resModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PollResultData implements Serializable{

	@SerializedName("name")
	private String name;

	@SerializedName("per")
	private int per;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setPer(int per){
		this.per = per;
	}

	public int getPer(){
		return per;
	}

	@Override
 	public String toString(){
		return 
			"PollResultData{" + 
			"name = '" + name + '\'' + 
			",per = '" + per + '\'' + 
			"}";
		}
}