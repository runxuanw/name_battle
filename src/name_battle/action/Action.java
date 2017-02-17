package name_battle.action;

import java.util.*;

public class Action {

	public int id;
	public String action_name;
	String action_color;
	//from 0-15, higher the rate, harder for fighters to learn, =0 must learn
	public int learning_difficulty;
	
	//0 means positive, 1 means passive
	public int action_type;
	
	//set possible following action
	public List<Action> possible_consequence = new ArrayList<Action>();
	
	//-1 is not set, 0 is normal rate, 1 is threadhold, 2 and so on
	//int active_type;
	//from 0-100
	public int active_rate;
	
	//int active_threadhold;

	//effect
	//List<effect> effects=new ArrayList<effect>();
	
	Action(int _id,String _action_name,int _active_rate,int _learning_difficulty,String _action_color,int _type){
		id=_id;
		action_name=_action_name;
		action_color=_action_color;
		active_rate=_active_rate;
		learning_difficulty=_learning_difficulty;
		action_type=_type;
	}
	
	void AddConsequence(Action consequence){
		possible_consequence.add(consequence);
	}
	
}
