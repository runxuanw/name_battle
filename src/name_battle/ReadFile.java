package name_battle;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import javax.swing.JComboBox;

import name_battle.action.ActionBook;
import name_battle.attribute.Attribute;
import name_battle.attribute.AttributeBook;

public class ReadFile {

	File file;
	//file must be the dir of 3d_line(not src)
	public ReadFile(String filename){
		file = new File(filename);
	}
	
	int getsize(){
		int i=0;
		try {
			Scanner sc = new Scanner(file);
			String line=sc.nextLine();
			while(sc.hasNextLine()){
				i++;
				line=sc.nextLine();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}
	
	public void addmonster(JComboBox dropmenu){
		int i=0;
		try {
			Scanner sc = new Scanner(file);
			String line=sc.nextLine();
			
			while(sc.hasNextLine()){
				if(line.equals("monster")){
					line=sc.nextLine();
					dropmenu.addItem(line);
					line=sc.nextLine();
					while(!line.equals("monster")&&sc.hasNextLine()){
						line=sc.nextLine();
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	void readmonster(String _name,Fighter _fighter,AttributeBook _attribute_list,ActionBook _action_list){
		
		try {
			Scanner sc = new Scanner(file);
			String line=sc.nextLine();
			
			while(sc.hasNextLine()&&!line.equals(_name)){
					line=sc.nextLine();
			}
			if(sc.hasNextLine()){
				//read all data and pass them to fighter
				//read attribute
				line=sc.nextLine();
				String[] attrs=line.split(" ");
				int tempamount=0;
				//need to handle unmatch situation
				for(int i=0;i<_fighter.attributes.size();i++){
						tempamount=Integer.parseInt(attrs[i]);
						_fighter.attributes.get(i).amount=tempamount;
				}
				//read skill
				line=sc.nextLine();
				String[] actions=line.split(" ");
				for(int i=0;i<actions.length;i++){
					_fighter.actions.add(_action_list.get_action(Integer.parseInt(actions[i])));
				}
				
			}
				
			_fighter.max_hp=_fighter.getAttributeAmount("life");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	
	public void addteam(JComboBox dropmenu,List<Team> _teams){
		int i=0;
		try {
			Scanner sc = new Scanner(file);
			String line=sc.nextLine();
			
			while(sc.hasNextLine()){
				if(line.equals("team")){
					line=sc.nextLine();
					Team tempteam=new Team(line);
					_teams.add(tempteam);
					dropmenu.addItem(line);
					line=sc.nextLine();
					while(!line.equals("team")&&sc.hasNextLine()){
						tempteam.addmember(line);
						line=sc.nextLine();
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void addteam(JComboBox dropmenu){
		int i=0;
		try {
			Scanner sc = new Scanner(file);
			String line=sc.nextLine();
			while(sc.hasNextLine()){
				if(line.equals("team")){
					line=sc.nextLine();
					dropmenu.addItem(line);
					line=sc.nextLine();
					while(!line.equals("team")&&sc.hasNextLine()){
						line=sc.nextLine();
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
