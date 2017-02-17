package name_battle;


import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import name_battle.action.Action;
import name_battle.action.ActionBook;
import name_battle.attribute.Attribute;
import name_battle.attribute.AttributeBook;
import name_battle.view.FighterGui;
import name_battle.view.SidePanel;
import name_battle.view.ViewFrame;

public class Fighter {
	
	public String name;
	String md5;
	private Process belongprocess;
	public FighterGui fightergui;
	int occupation;
	//for start an action
	public int wait_amount;
	//currently team for different panel
	public String team;
	public String origin_team;
	//for pick up target
	public List<Fighter> fighters = new ArrayList<Fighter>();
	//for trace enemy who target this fighter
	List<Fighter> hunterList=new ArrayList<Fighter>();
	//strength level
	
	public List<Attribute> attributes = new ArrayList<Attribute>();
	public int max_hp;
	Fighter target;

	//AI for selecting target
	protected enum characters{
		random,bully,fighter,follower
	};
	protected characters character; 
	
	//currently set alive
	public boolean alive;
	//for insert in jpanel
	public int order_id;
	ActionBook action_list;
	//potential actions
	public List<Action> actions = new ArrayList<Action>();
	//strength level
	int average_power;
	//0 is normal,1 is monster, 2 is load from file
	public int addmethod;
	//may use a tracer to add new id
	//normal generate
	ViewFrame mainview;
	//index for fighter outlook
	int face;
	public Fighter(String _name,AttributeBook attribute_list,ActionBook _action_list,SidePanel infopanel,JScrollPane scrollpanel,String _team,List<Fighter> _fighters,int order,int _addmethod){
		name=_name;
		addmethod=_addmethod;
		mainview=infopanel.view;
		wait_amount=0;
		alive=true;
		team=_team;
		face=0;
		if(team.equals("自己")){
			team=name;
		}
		action_list=_action_list;
		order_id=order;
		origin_team=team;
		final int charactersize = characters.values().length;
		//init attributes
		for(int counter=0;counter<attribute_list.attributes.size();counter++){
			Attribute tempattr=new Attribute(attribute_list.attributes.get(counter).id,attribute_list.attributes.get(counter).name);
			attributes.add(tempattr);
		}
		
		if(addmethod==0){
		//generate md5 according to name
		//32 hex digit
		md5=getMD5(name.getBytes());
		System.out.println(md5);

		int strength=0;
		//generate attributes
		for(int counter=0;counter<attributes.size();counter++){
			//random according to md5
			//max support 16 attributes
			String attribute_seed=md5.substring(counter*2, counter*2+2);
			//System.out.println(attribute_seed);
			int attribute_data=14+(Integer.decode("0x"+attribute_seed))%87;
			strength+=attribute_data;
			//System.out.println(attribute_data);
			attributes.get(counter).amount=attribute_data;
			if(attributes.get(counter).name.equals("life")){
				attributes.get(counter).amount=attributes.get(counter).amount*3;
				attributes.get(counter).amount+=100;
				max_hp=attributes.get(counter).amount;
			}
			
		}
		
		average_power=strength/attributes.size();
		//generate actions
		//currently only normal attack
		int index_seed=attributes.size()*2+3;
		for(int i=0;i<action_list.actions.size();i++){
			if(action_list.actions.get(i).action_name.equals("攻击")){
				actions.add(action_list.actions.get(i));
			}
			else{
				//condition to learn
				if(Integer.decode("0x"+md5.substring(index_seed,index_seed+1))>=action_list.actions.get(i).learning_difficulty){
					actions.add(action_list.actions.get(i));
					System.out.println(this.name+"学会了技能："+action_list.actions.get(i).action_name);
				}
				index_seed++;
			}
		}
		
		//generate character
		character=characters.values()[Integer.decode("0x"+md5.substring(index_seed,index_seed+1))%charactersize];
		index_seed++;
		System.out.println(character.name());
		
		//generate face, from 0-1049
		face=Integer.decode("0x"+md5.substring(index_seed,index_seed+3))%1050;
		index_seed+=2;
		
		}
		else if(addmethod==1){
			ReadFile readmonster=new ReadFile("monster");
			//max_hp also been set in read monster
			readmonster.readmonster(name, this, attribute_list, action_list);
			character=characters.random;
			
		}
		else if(addmethod==2){
			//max_hp set included
			mainview.setfighterdata(this);
			character=characters.random;
		}
		
		String facefile = null;
		if(face>=100){
			facefile=face+"i.png";
		}
		else if(face<100&&face>9){
			facefile="0"+face+"i.png";
		}
		else if(face<=9){
			facefile="00"+face+"i.png";
		}
		else{
			facefile="000i.png";
		}
		System.out.println(facefile);
		fightergui=new FighterGui(this,infopanel,scrollpanel,order/2,facefile);
		
	}
	
	void init_fighters(List<Fighter> _fighters){
		for(int i=0;i<_fighters.size();i++){
			fighters.add(_fighters.get(i));
		}
	}
	
	int get_average_power(){
		return average_power;
	}
	
	public void pick_target(){
		if(target==null||target.alive==false||target.team.equals(team)){
			Fighter temptarget=null;
			
			if(character==characters.random){
				Random rand=new Random();
				int max_dice=-1;
				for(int i=0;i<fighters.size();i++){
					if(fighters.get(i).alive&&!team.equals(fighters.get(i).team)){
					int dice=rand.nextInt(1000);
					//select target according to AI
					if(dice>max_dice){
						temptarget=fighters.get(i);
						max_dice=dice;
						}
					}
				}
			}
			else if(character==characters.bully){
				int kill_count=9999;
			for(int i=0;i<fighters.size();i++){
				if(fighters.get(i).alive&&!team.equals(fighters.get(i).team)){
				int turn_damage=getAttributeAmount("att")-fighters.get(i).getAttributeAmount("def");
				if(turn_damage<=0){
					turn_damage=1;
				}
				int temp_kill_count=fighters.get(i).getAttributeAmount("life")/turn_damage;
				//select target according to AI
				if(temp_kill_count<kill_count){
					temptarget=fighters.get(i);
					kill_count=temp_kill_count;
					}
				}
			}
			}
			else if(character==characters.fighter){
				int max_att_spd=-1;
				for(int i=0;i<fighters.size();i++){
					if(fighters.get(i).alive&&!team.equals(fighters.get(i).team)){
					int temp_att_spd=fighters.get(i).getAttributeAmount("att")+fighters.get(i).getAttributeAmount("spd");
					//select target according to AI
					if(temp_att_spd>max_att_spd){
						temptarget=fighters.get(i);
						max_att_spd=temp_att_spd;
						}
					}
				}
			}
			else if(character==characters.follower){
				int max_hunter=-1;
				for(int i=0;i<fighters.size();i++){
					if(fighters.get(i).alive&&!team.equals(fighters.get(i).team)){
					int temp_hunter=fighters.get(i).hunterList.size();
					//select target according to AI
					if(temp_hunter>max_hunter){
						temptarget=fighters.get(i);
						max_hunter=temp_hunter;
						}
					}
				}
			}
			//select the target or it means all enemys are dead, game should end
			if(temptarget!=null){
				target=temptarget;
				target.hunterList.add(this);
				System.out.println(name+" find new target: "+target.name);
			}
			else{
				//no target to pick, game end
				belongprocess.game_end(team+"的"+name);
			}
			
		}
		else{
			System.out.println(name+" has an alive target already");
		}
	}
	
	
	
	void setprocess(Process _belongprocess){
		belongprocess=_belongprocess;
	}
	
	public int respondToaction(int _action_id){
		if(_action_id==0){
			//future reaction would be added
			return 1;
		}
		return 1;
	}
	
	
	
	public Action pickaction(){
		//first check all possible trigger actions
		List<Action> ready_actions=new ArrayList<Action>();
		int ready_active_rate=0;
		for(int i=0;i<actions.size();i++){
			if(actions.get(i).action_type==0&&action_list.can_trigger_action(actions.get(i), this)){
				ready_active_rate+=actions.get(i).active_rate;
				ready_actions.add(actions.get(i));
			}
		}
		
		//select from ready actions; rate is according to the share of total ready action's trigger rate
		Random rand=new Random();
		int select_rate=ready_active_rate;
		for(int i=0;i<ready_actions.size();i++){
			int randnum=rand.nextInt(select_rate);
			if(randnum<=ready_actions.get(i).active_rate){
				return ready_actions.get(i);
			}
			select_rate-=ready_actions.get(i).active_rate;
		}
		System.out.println("select action error");
		return actions.get(0);
	}
	
	public Action pickReaction(Action _action,Fighter matcher){
		//currenlty just pick one reaction according to all reaction
		//first check all possible passive actions
		List<Action> ready_actions=new ArrayList<Action>();
		int passive_rate=0;
		for(int i=0;i<actions.size();i++){
			if(actions.get(i).action_type==1&&action_list.can_trigger_action(actions.get(i), this)){
				for(int j=0;j<_action.possible_consequence.size();j++){
				if(actions.get(i).id==_action.possible_consequence.get(j).id){
				passive_rate+=action_list.get_action_rate(actions.get(i), this,matcher);
				System.out.println(this.name+" "+actions.get(i).action_name+" "+action_list.get_action_rate(actions.get(i), this,matcher));
				ready_actions.add(actions.get(i));
				}
				}
			}
		}
		//select from ready actions; rate is according to the share of total ready action's trigger rate
		Random rand=new Random();
		int select_rate=passive_rate;
		for(int i=0;i<ready_actions.size();i++){
			int randnum=rand.nextInt(select_rate);
			System.out.println(randnum);
			if(randnum<=action_list.get_action_rate(ready_actions.get(i), this,matcher)){
				return ready_actions.get(i);
			}
			select_rate-=action_list.get_action_rate(ready_actions.get(i), this,matcher);
		}
		System.out.println("select action error");
		return actions.get(0);
	}
	

	
	void checkfighterdead(){
		if(getAttributeAmount("life")==0&&alive==true){
			alive=false;
			belongprocess.fighterdead(this);
			//tell its target, its hunter is dead
			target.hunterList.remove(this);
			//tell fighter's hunter its death
			for(int i=0;i<hunterList.size();i++){
				hunterList.get(i).pick_target();
			}
			hunterList=null;
		}
	}
	
	
	public void changeAttributeAmount(String _name,int change_amount,String color){
		
		for(int counter=0;counter<attributes.size();counter++){
			if(attributes.get(counter).name==_name){
				attributes.get(counter).amount+=change_amount;
				if(attributes.get(counter).amount<0){
					attributes.get(counter).amount=0;
				}
				fightergui.updateAttribute(_name, attributes.get(counter).amount,color);
			}
		}
	}
	
	
	public int getAttributeAmount(String _name){
		for(int counter=0;counter<attributes.size();counter++){
			if(attributes.get(counter).name==_name){
				return attributes.get(counter).amount;
			}
		}
		//error
		System.out.println("attribute not found in fighter");
		return -1;
	}
	
	
	
	public static String getMD5(byte[] source)
	{
	String s = null;
	char hexDigits[] =
	{ // 用来将字节转换成 16 进制表示的字符
	'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b',
	'c', 'd', 'e', 'f'
	};
	try
	{
	java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
	md.update(source);
	// MD5 的计算结果是一个 128 位的长度整数，
	byte tmp[] = md.digest();
	// 用字节表示就是 16 个字节
	char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
	// 所以表示成 16 进制需要 32 个字符
	int k = 0; // 表示转换结果中对应的字符位置
	for (int i = 0; i < 16; i++)
	{ // 从第一个字节开始，对 MD5 的每一个字节
	// 转换成 16 进制字符的转换
	byte byte0 = tmp[i]; // 取第 i 个字节
	str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
	// >>> 为逻辑右移（即无符号右移），将符号位一起右移

	// 取字节中低 4 位的数字转换
	str[k++] = hexDigits[byte0 & 0xf];
	}
	s = new String(str); // 换后的结果转换为字符串

	}
	catch (Exception e)
	{
	e.printStackTrace();
	}
	return s;
	
}
	
}
