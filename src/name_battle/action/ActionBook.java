package name_battle.action;

import java.util.*;

import javax.swing.JLabel;

import name_battle.Fighter;
import name_battle.Process;
import name_battle.attribute.AttributeBook;

public class ActionBook {
	//here is a book of action for init each fighter, how to process them are in Process.java:process_action
	public List<Action> actions=new ArrayList<Action>();
	
	AttributeBook attribute_list;
	//passive actions,active when attacked
	
	//only for test
	int action1_count=0;
	int action2_count=0;
	int action3_count=0;
	
	public ActionBook(AttributeBook _attribute_list){
		attribute_list=_attribute_list;
		//id,name,active_rate,learning_difficulty,action_color
		
		//positive
		Action normal_att=new Action(0,"攻击", 100,0,"#FF0000",0);
		actions.add(normal_att);
		Action bite=new Action(1,"咬", 40,11,"#FF0000",0);
		actions.add(bite);
		Action dying=new Action(2,"垂死抗争",120,12,"#0000FF",0);
		actions.add(dying);
		Action poison=new Action(3,"投毒",40,11,"#008000",0);
		actions.add(poison);
		Action curse=new Action(4,"诅咒",40,13,"#FF00FF",0);
		actions.add(curse);
		Action anger=new Action(5,"暴打",30,12,"#FF0000",0);
		actions.add(anger);
		Action heal=new Action(6,"治疗",50,14,"#00DF00",0);
		actions.add(heal);
		
		//passive
		Action none=new Action(100,"无",200,0,"#000000",1);
		actions.add(none);
		Action dodge=new Action(101,"闪开了",100,0,"#00CED1",1);
		actions.add(dodge);
		Action defence=new Action(102,"防御",200,12,"#DAA520",1);
		actions.add(defence);
		Action trip=new Action(103,"绊倒了",100,11,"#FF00FF",1);
		actions.add(trip);
		
		//actions consequence map
		normal_att.AddConsequence(none);
		normal_att.AddConsequence(dodge);
		normal_att.AddConsequence(defence);
		normal_att.AddConsequence(trip);
		
		bite.AddConsequence(none);
		bite.AddConsequence(dodge);
		bite.AddConsequence(defence);
		bite.AddConsequence(trip);
		
		curse.AddConsequence(none);
		curse.AddConsequence(dodge);

		poison.AddConsequence(none);
		poison.AddConsequence(dodge);

		dying.AddConsequence(none);
		
		anger.AddConsequence(none);
		
		heal.AddConsequence(none);
		
	}
	
	public int get_action_rate(Action action,Fighter sender,Fighter matcher){
		int temp_rate=action.active_rate;
		//luck associate with passive action rate
		if(action.action_name.equals("无")){
			temp_rate-=sender.getAttributeAmount("luck")/2;
		}
		
		if(action.action_name.equals("闪开了")){
			temp_rate+=(sender.getAttributeAmount("agl")-(matcher.getAttributeAmount("aim")));
			if(temp_rate<=0){
				return 1;
			}
			return temp_rate;
		}
		else if(action.action_name.equals("防御")){
			temp_rate+=sender.getAttributeAmount("def")/2;
			return temp_rate;
		}
		else{
			if(temp_rate>0){
			return temp_rate;
			}
			else{
				return 1;
			}
		}
		
	}
	
	public Action get_action(int _id){
		for(int i=0;i<actions.size();i++){
			if(actions.get(i).id==_id){
				return actions.get(i);
			}
		}
		System.out.println("action id not found");
		return null;
		
	}
	
	public boolean can_trigger_action(Action action,Fighter sender){
		if(action.action_name.equals("垂死抗争")){
			if(sender.getAttributeAmount("life")<70){
				return true;
			}
			else{
				return false;
			}
		}
		return true;
	}
	
	String action_display(Action action){
		return "<font color="+action.action_color+">"+action.action_name+"</font>";
	}
	

	//arg to store changed amount
	public void process_action(Process _process,JLabel _log,Action _action,Fighter _sender,Fighter _target,int[] _arg){
		if(_action.action_type==0){
		//get action name and then process each effect
		if(_action.action_name.equals("攻击")){

			//calc effect
			int damage=_sender.getAttributeAmount("att")-_target.getAttributeAmount("def");
			if(damage<=0){
				damage=1;
			}
			
			//update log
			_process.UpdateAddLog(_log,_sender.name+"对"+_target.name+"发动了攻击");
			//execute effect
			Action reaction=_target.pickReaction(_action,_sender);
			if(reaction.action_name.equals("无")){
			_target.changeAttributeAmount("life", -damage,_action.action_color);
			_process.UpdateDamageLog(_log, _target, damage);
			}
			else{
				_arg[attribute_list.getId("life")]=-damage;
				process_action(_process,_log,reaction,_target,_sender,_arg);
			}
		}
		if(_action.action_name.equals("咬")){
			action1_count++;
			//calc effect
			int damage=_sender.getAttributeAmount("att")*3/2-_target.getAttributeAmount("def");
			if(damage<=0){
				damage=1;
			}
			damage+=_sender.getAttributeAmount("att")/3;
			//update log
			_process.UpdateAddLog(_log,_sender.name+"发狂了");
			_process.UpdateAddLog(_log,"，冲上去"+action_display(_action)+"了"+_target.name+"一口");
			//execute effect
			Action reaction=_target.pickReaction(_action,_sender);
			if(reaction.action_name.equals("无")){
			_target.changeAttributeAmount("life", -damage,_action.action_color);
			_process.UpdateDamageLog(_log, _target, damage);
			}
			else{
				_arg[attribute_list.getId("life")]=-damage;
				process_action(_process,_log,reaction,_target,_sender,_arg);
			}
		}
		if(_action.action_name.equals("诅咒")){
			action2_count++;
			//calc effect
			//no need to calc
			
			//update log
			_process.UpdateAddLog(_log,_sender.name+action_display(_action)+_target.name);
			//execute effect
			Action reaction=_target.pickReaction(_action,_sender);
			if(reaction.action_name.equals("无")){
				for(int i=0;i<_target.attributes.size();i++){
					if(!_target.attributes.get(i).name.equals("life")){
						_target.changeAttributeAmount(_target.attributes.get(i).name, 
								-(_target.attributes.get(i).amount)/5,_action.action_color);
					}
				}
			_process.UpdateAddLog(_log,"，"+_target.name+"变弱了");
			}
			else{
				for(int i=0;i<attribute_list.attributes.size();i++){
					if(!_target.attributes.get(i).name.equals("life")){
					_arg[i]=-(_target.attributes.get(i).amount)/5;
					}
				}
				process_action(_process,_log,reaction,_target,_sender,_arg);
			}
		}
		if(_action.action_name.equals("投毒")){
			action3_count++;
			//calc effect
			int damage=_target.getAttributeAmount("life")/3+5;
			//update log
			_process.UpdateAddLog(_log,_sender.name+"对"+_target.name+action_display(_action));
			//execute effect
			Action reaction=_target.pickReaction(_action,_sender);
			if(reaction.action_name.equals("无")){
				_target.changeAttributeAmount("life", -damage,_action.action_color);
				_process.UpdateDamageLog(_log, _target, damage);
			}
			else{
				_arg[attribute_list.getId("life")]=-damage;
				process_action(_process,_log,reaction,_target,_sender,_arg);
			}
		}
		if(_action.action_name.equals("垂死抗争")){
			//update log
			_process.UpdateAddLog(_log,_sender.name+action_display(_action));
			//execute effect
			if(_target.respondToaction(0)==1){
				for(int i=0;i<_sender.attributes.size();i++){
					if(!_sender.attributes.get(i).name.equals("life")){
						_sender.changeAttributeAmount(_sender.attributes.get(i).name, 
								(_sender.attributes.get(i).amount)/4,_action.action_color);
					}
				}
				_sender.wait_amount+=100;
			}
			_process.UpdateAddLog(_log,"，各项能力值提升了!");
			//only trigger once
			_sender.actions.remove(_action);
		}
		if(_action.action_name.equals("治疗")){
			//heal the one who receive most damage
			
			//pick lowest health teammate to cure
			Fighter healtarget=null;
			int mostdamage=0;
			for(int i=0;i<_sender.fighters.size();i++){
				if(_sender.fighters.get(i).team.equals(_sender.team)&&_sender.fighters.get(i).alive){
				if(_sender.fighters.get(i).max_hp-_sender.fighters.get(i).getAttributeAmount("life")>mostdamage){
					mostdamage=_sender.fighters.get(i).max_hp-_sender.fighters.get(i).getAttributeAmount("life");
					healtarget=_sender.fighters.get(i);
				}
				}
			}
			if(healtarget==null){
				healtarget=_sender;
			}
			//update log
			_process.UpdateAddLog(_log,_sender.name+"对"+healtarget.name+"发动了"+action_display(_action));
			//execute effect
			if(_target.respondToaction(0)==1){
				int healamount=_sender.getAttributeAmount("luck");
				if(healamount+healtarget.getAttributeAmount("life")>healtarget.max_hp){
					healamount=healtarget.max_hp-healtarget.getAttributeAmount("life");
				}
				if(healamount<0){
					healamount=0;
				}
				healtarget.changeAttributeAmount("life", healamount,_action.action_color);
				_process.UpdateAddLog(_log,"，"+healtarget.name+"回复了<font color="+_action.action_color+">"+healamount+"</font>点生命");
			}
			
		}
		if(_action.action_name.equals("暴打")){
			//update log
			_process.UpdateAddLog(_log,_sender.name+"发怒了");
			_process.UpdateAddLog(_log,"，把"+_target.name+"按在地上一顿"+action_display(_action));
			Random rand=new Random();
			int range=10+_sender.getAttributeAmount("aim")-_target.getAttributeAmount("agl")/2;
			if(range<=0){
				range=1;
			}
			int counter=rand.nextInt(range)/5;
			//execute effect
			while(counter>0&&_target.alive){
				//damage would be vary later
				int damage=(_sender.getAttributeAmount("att")+30-_target.getAttributeAmount("def"))/2;
				if(damage<=0){
					damage=1;
				}
				_target.changeAttributeAmount("life",-damage,_action.action_color);
				_process.UpdateDamageLog_NewLine(_log, _target, damage);
				counter--;
			}
			if(_target.alive){
			_process.UpdateAddLog(_log,"<br>"+_target.name+"挣脱了</br>");
			}
		}
		
	}
		
		//passive action
		if(_action.action_type==1){
			if(_action.action_name.equals("闪开了")){
				//update log
				_process.UpdateAddLog(_log,"，但是被"+_sender.name+action_display(_action));
				//execute effect
				//none effect
			}
			if(_action.action_name.equals("防御")){
				//update log
				_process.UpdateAddLog(_log,"，"+_sender.name+action_display(_action));
				//execute effect
				int damage=_arg[attribute_list.getId("life")]/2;
				if(damage==0){
					damage=-1;
				}
				_sender.changeAttributeAmount("life", damage,_action.action_color);
				_process.UpdateDamageLog(_log, _sender, -damage);
			}
			if(_action.action_name.equals("绊倒了")){
				//update log
				_process.UpdateAddLog(_log,"，但是被"+_sender.name+action_display(_action));
				//execute effect
				int damage=_arg[attribute_list.getId("life")]/3-(90-_target.getAttributeAmount("luck"))/2;
				if(damage>=0){
					damage=-1;
				}
				_target.changeAttributeAmount("life", damage,_action.action_color);
				_process.UpdateDamageLog(_log, _target, -damage);
			}
			
		}
		
		
	}
	
	
	
	
}
