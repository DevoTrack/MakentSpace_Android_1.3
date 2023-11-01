package com.makent.trioangle.model.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  model
 * @category    Mapmodel
 * @author      Trioangle Product Team
 * @version     1.1
 */

import java.io.Serializable;

public class Property_Type_model implements Serializable {
	private String name,desc,id,listtype;
	public String type;
	public String image;

	public Property_Type_model(String type) {
		this.type = type;
	}

	public Property_Type_model(String type, String listtype, String name, String id, String desc) {
		this.type=type;
		this.name=name;
		this.desc=desc;
		this.id=id;
		this.listtype=listtype;
	}

	public Property_Type_model() {

	}
	//Detailed list space

	public String getType() {	return listtype; }

	public void setType(String listtype) {this.listtype = listtype; }

	public String getId() {	return id;	}

	public void setId(String id) {this.id = id; }

	public String getName() {return name;}

	public void setName(String name) {this.name = name; }

	public String getDesc() {return desc;}

	public void setDesc(String desc) {this.desc = desc; }

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
