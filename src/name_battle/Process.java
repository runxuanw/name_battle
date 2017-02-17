package name_battle;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import name_battle.action.ActionBook;
import name_battle.view.ViewFrame;


public class Process extends Thread{

	public boolean battle_end=false;
	
	//store all the players
	List<Fighter> fighters = new ArrayList<Fighter>();
	
	ActionBook action_list;
	//wait amount before action
	int wait_time;
	
	ViewFrame mainlview;
	JPanel battleview;
	JPanel dumepanel;
	JScrollPane battlepanel;
	
	int speed;
	public boolean interrupt=false;
	
	public Process(List<Fighter> _fighters,ActionBook _action_list,ViewFrame _mainview,JPanel _battleview,JScrollPane _battlepanel) throws InterruptedException{
		action_list=_action_list;
		wait_time=300;
		speed=600;
		dumepanel=new JPanel();
		mainlview=_mainview;
		battleview=_battleview;
		battlepanel=_battlepanel;
		//init fighters list, set process for each fighters
		for(int i=0;i<_fighters.size();i++){
			fighters.add(_fighters.get(i));
			fighters.get(i).setprocess(this);
			fighters.get(i).init_fighters(_fighters);
			//set each fighter's target
			fighters.get(i).pick_target();
		}
		
	}
	
	
	
	@Override
	public void run(){
		try_sleep();
		while(!battle_end){
			//check wait time for all fighters, and make action for those excess the wait time
			for(int i=0;i<fighters.size();i++){
				if(fighters.get(i).alive){
				if(fighters.get(i).wait_amount>=wait_time&&!battle_end){
					fighters.get(i).wait_amount-=wait_time;
					JLabel log=new JLabel();
					battleview.add(log);
					int[] arg=new int[mainlview.attribute_list.attributes.size()];
					action_list.process_action(this,log,fighters.get(i).pickaction(), fighters.get(i),fighters.get(i).target,arg);
					mainlview.revalidate();
					mainlview.repaint();
					//try_sleep();
				}
				fighters.get(i).wait_amount+=10;
				fighters.get(i).wait_amount+=fighters.get(i).getAttributeAmount("spd");
				}
			}
			
			//dead fighters not removed
			
		}
		if(interrupt==false){
		try_sleep();
		JLabel endlog=new JLabel();
		battleview.add(endlog);
		UpdateAddLog(endlog,"��Ϸ����");
		}
		
		}
	
	public void setSpeed(int multipler){
		//adjust game speed
		if(multipler>0){
			speed=600/multipler;
		}
	}
	
	
	void game_end(String _team){
		JLabel log=new JLabel();
		battleview.add(log);
		UpdateAddLog(log,_team+"���ʤ��");
		battleview.revalidate();
		battleview.repaint();
		battle_end=true;
		try_sleep();
	}
	
	public void interrupt_game(){
		interrupt=true;
		battleview=dumepanel;
		game_end("����");
	}
	
	
	void fighterdead(Fighter _fighter){
		JLabel deadlog=new JLabel(_fighter.name+"����");
		battleview.add(deadlog);
		battleview.revalidate();
	}
	
	

	void try_sleep(){
		try {
			sleep(speed);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void UpdateDamageLog(JLabel _log,Fighter _receiver,int _damage){
		UpdateAddLog(_log,"��"+_receiver.name+"�ܵ���<font color=#FF0000>"+_damage+"</font>���˺�");
		_receiver.checkfighterdead();
	}
	
	public void UpdateDamageLog_NewLine(JLabel _log,Fighter _receiver,int _damage){
		UpdateAddLog(_log,"<br>"+_receiver.name+"�ܵ���<font color=#FF0000>"+_damage+"</font>���˺�</br>");
		_receiver.checkfighterdead();
	}


	
	public void UpdateAddLog(JLabel _log,String _newText){
		String oldText=_log.getText();
		if(oldText.length()>=13){
			oldText=oldText.substring(6, oldText.length()-7);
			_log.setText("<html>"+oldText+_newText+"</html>");
		}
		else{
			_log.setText("<html>"+oldText+_newText+"</html>");
		}
		battlepanel.getVerticalScrollBar().setValue(battlepanel.getVerticalScrollBar().getMaximum()+1);
		battleview.revalidate();
		battleview.repaint();
		try_sleep();
	}
	

	
	
}
