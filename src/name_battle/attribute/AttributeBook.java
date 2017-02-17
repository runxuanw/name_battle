package name_battle.attribute;

import java.util.*;

public class AttributeBook {
	public List<Attribute> attributes=new ArrayList<Attribute>();
	public AttributeBook(){
	Attribute life=new Attribute(0,"life");
	attributes.add(life);
	Attribute att=new Attribute(1,"att");
	attributes.add(att);
	Attribute def=new Attribute(2,"def");
	attributes.add(def);
	Attribute aim=new Attribute(3,"aim");
	attributes.add(aim);
	//for dodge
	Attribute agl=new Attribute(4,"agl");
	attributes.add(agl);
	//for more actions
	Attribute spd=new Attribute(5,"spd");
	attributes.add(spd);
	//for better random
	Attribute luck=new Attribute(6,"luck");
	attributes.add(luck);
	}
	
	public int getId(String attr_name){
		for(int counter=0;counter<attributes.size();counter++){
			if(attributes.get(counter).name==attr_name){
				return attributes.get(counter).id;
			}
		}
		//error
		System.out.println("attribute name not found in book");
		return -1;
	}
	
	
	
}

