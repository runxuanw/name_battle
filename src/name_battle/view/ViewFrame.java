package name_battle.view;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileSystemView;

import name_battle.Estimation;
import name_battle.Fighter;
import name_battle.Process;
import name_battle.Team;
import name_battle.action.ActionBook;
import name_battle.attribute.AttributeBook;


public class ViewFrame extends JFrame{
	
	//init data
			//use for add group
			List<Team> teams=new ArrayList<Team>();
			//use for team panel
			List<TeamPanel> teampanels=new ArrayList<TeamPanel>();
			List<Fighter> fighter_list=new ArrayList<Fighter>();
			public AttributeBook attribute_list;
			ActionBook action_list;
			
			ControlPanel middlepanel;
			
			JPanel battleview=new JPanel();
			SidePanel rightview;
			SidePanel leftview;
			
			JScrollPane battleScroll = new JScrollPane(
					JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			Estimation estimate_name=new Estimation();
			Process CurrentProcess;
			String loadteam;
			String loadname;
			String[] loadattr;
			String[] loadact;
	ViewFrame() {
/*
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	*/	
		
		CurrentProcess=null;
		attribute_list=new AttributeBook();
		action_list=new ActionBook(attribute_list);
		//init frame
		setTitle("MD5 Ãû×Ö´òÈº¼Ü  alpha_v1.0");
		setSize(1100,700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		
		middlepanel=new ControlPanel(this,getWidth()/4-5, 3,getWidth()/2-6,getHeight()/6);
		//init player info panel 1&2
		rightview = new SidePanel(this,middlepanel.getX()+middlepanel.getWidth()+3, 3,(getWidth()-middlepanel.getWidth())/2-16,getHeight()-43);
		leftview= new SidePanel(this,3,3,(getWidth()-middlepanel.getWidth())/2-12,getHeight()-43);

		
		//init the battle panel
		battleScroll.setSize(getWidth()/2,getHeight()-middlepanel.getHeight()-46);
		battleScroll.setLocation(middlepanel.getX(), middlepanel.getY()+middlepanel.getHeight()+3);
		battleScroll.setBorder(BorderFactory.createEtchedBorder());
		battleview.setBackground(Color.WHITE);
		battleview.setLayout(new BoxLayout((Container) battleview, BoxLayout.Y_AXIS));
		battleScroll.setViewportView(battleview);

		//finalize frame

		add(battleScroll);
		setVisible(true);

		//start2.main(null);
		playSound();
		
		
	}
	
	//add background sound
	public void playSound() {
	    try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("./sound/What Are Words.wav").getAbsoluteFile());
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
	}

	void addfighter(String _name,String _teamname,int _addmethod){
		if(fighter_list.size()<24&&_name.length()>0&&(CurrentProcess==null||CurrentProcess.battle_end==true)){
			//select or new a teampanel to put the fighter in
			/*
			TeamPanel temppanel=null;
			for(int i=0;i<teampanels.size();i++){
				if(teampanels.get(i).teamname.equals(_teamname)){
					temppanel=teampanels.get(i);
				}
			}
			if(temppanel==null){
				if(rightview.getComponentCount()<leftview.getComponentCount()){
				temppanel=new TeamPanel(_teamname,rightview,rightScroll);
				}
				else{
				temppanel=new TeamPanel(_teamname,leftview,leftScroll);
				}
				teampanels.add(temppanel);
			}
			*/
			//put in the panel which has less component
			if(leftview.size<=rightview.size){
				leftview.size++;
				Fighter NewFighter=new Fighter(_name,attribute_list,action_list,leftview,leftview.Scroll,_teamname,fighter_list,fighter_list.size(),_addmethod);
				fighter_list.add(NewFighter);
			}
			else{
				rightview.size++;
		  		Fighter NewFighter=new Fighter(_name,attribute_list,action_list,rightview,rightview.Scroll,_teamname,fighter_list,fighter_list.size(),_addmethod);
		  		fighter_list.add(NewFighter);
			}
	  		validate();
		}
	}

	public void setfighterdata(Fighter _fighter){
		int tempamount=0;
		//need to handle unmatch situation
		for(int i=0;i<_fighter.attributes.size();i++){
				tempamount=Integer.parseInt(loadattr[i]);
				_fighter.attributes.get(i).amount=tempamount;
		}
		//read skill
		for(int i=0;i<loadact.length;i++){
			_fighter.actions.add(action_list.get_action(Integer.parseInt(loadact[i])));
		}
		_fighter.max_hp=_fighter.getAttributeAmount("life");
	}
	
public void func_loadbutton(JButton _loadbutton){
	if(CurrentProcess==null){
		final JFileChooser fc = new JFileChooser();
		File workingDirectory = new File(System.getProperty("user.dir"));
		fc.setCurrentDirectory(workingDirectory);
		int returnVal=fc.showOpenDialog(_loadbutton);
		if(returnVal==fc.APPROVE_OPTION){
		//load all fighters
		File loadfile=fc.getSelectedFile();

		Scanner sc;
		try {
			sc = new Scanner(loadfile);
			while(sc.hasNextLine()){

				String line=sc.nextLine();
				loadteam=line;
				line=sc.nextLine();
				loadname=line;
				line=sc.nextLine();
				loadattr=line.split(" ");
				line=sc.nextLine();
				loadact=line.split(" ");
				addfighter(loadname,loadteam,2);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	}
}
	
public void func_savebutton(JButton _savebutton){
	if(CurrentProcess!=null&&CurrentProcess.battle_end==true
			&&CurrentProcess.interrupt==false){
		final JFileChooser fc = new JFileChooser();
		File workingDirectory = new File(System.getProperty("user.dir"));
		fc.setCurrentDirectory(workingDirectory);
		int returnVal=fc.showSaveDialog(_savebutton);
		if(returnVal==fc.APPROVE_OPTION){
		//save all alive fighter
		File savefile=fc.getSelectedFile();
		FileWriter fw;
		String genMD5=null;
		try {
			fw = new FileWriter(savefile);
			for(int i=0;i<fighter_list.size();i++){
				if(fighter_list.get(i).alive==true){
					Fighter fighter=fighter_list.get(i);
					//save one player's data
					fw.write(fighter.team+"\n");
					fw.write(fighter.name+"\n");
					for(int attr=0;attr<fighter.attributes.size();attr++){
					fw.write(fighter.attributes.get(attr).amount+" ");
					}
					fw.write("\n");
					for(int act=0;act<fighter.actions.size();act++){
					fw.write(fighter.actions.get(act).id+" ");
					}
					fw.write("\n");
				}
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	}
}
	
public void func_sliderSpeed(int _speedlevel){
	if(CurrentProcess!=null){
	CurrentProcess.setSpeed(_speedlevel);
	}
}
	
public void func_clearbutton(){
	ClearPanel();
	int tempsize=fighter_list.size();
	for(int i=0;i<tempsize;i++){
		fighter_list.remove(0);
	}
}
	
public void func_createbutton(String _name,String _team,int _goal){
	Random rand=new Random();
	int appendix;
	while(true){
	appendix=rand.nextInt(10000000);
	int power_level=estimate_name.estimate_name(_name+Integer.toString(appendix), attribute_list);
	if(power_level>=_goal*10&&power_level<=_goal*10+20){
		addfighter(estimate_name.name,_team,0);
		break;
	}
	}
}
	
public void func_addgroupbutton(String _teamname){
	for(int i=0;i<teams.size();i++){
		if(teams.get(i).teamname.equals(_teamname)){
			for(int j=0;j<teams.get(i).members.size();j++){
				addfighter(teams.get(i).members.get(j),_teamname,0);
			}
			break;
		}
	}
}
	
public void func_startbutton(){
	try {
			if(CurrentProcess==null){
		CurrentProcess=new Process(fighter_list,action_list,this,battleview,battleScroll);
		CurrentProcess.start();
			}
			else{
				boolean restart=true;
			for(int i=0;i<fighter_list.size();i++){
			if(fighter_list.get(i).addmethod==2){
				restart=false;
				break;
			}
			}
			if(restart){
			ClearPanel();
			for(int i=0;i<fighter_list.size();i++){
	  			Fighter tempfighter=new Fighter(fighter_list.get(0).name,attribute_list,action_list,
	  					fighter_list.get(0).fightergui.belongpanel,fighter_list.get(0).fightergui.belongscroll,
	  					fighter_list.get(0).origin_team,fighter_list,fighter_list.get(0).order_id,fighter_list.get(0).addmethod);
	  			fighter_list.remove(0);
	  			fighter_list.add(tempfighter);
	  			
			}
			
		CurrentProcess=new Process(fighter_list,action_list,this,battleview,battleScroll);
		CurrentProcess.start();
			battleview.updateUI();
			rightview.updateUI();
			leftview.updateUI();
			
			}
			}
		
	} catch (InterruptedException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
}
	

private void ClearPanel(){
	//currently just remove all gui from panel, and let the thread to stop
	if(CurrentProcess!=null){
		CurrentProcess.interrupt_game();
	}
		rightview.size=0;
		leftview.size=0;
		battleview.removeAll();
		rightview.removeAll();
		leftview.removeAll();
		battleview.updateUI();
		rightview.updateUI();
		leftview.updateUI();
}



public static void main(String[]args)
{
 ViewFrame mainview=new ViewFrame();
}



}