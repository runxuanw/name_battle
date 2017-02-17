package name_battle.view;

import java.awt.Dimension;
import java.util.*;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import name_battle.Fighter;

public class TeamPanel extends JPanel{

	String teamname;
	List<Fighter> members=new ArrayList<Fighter>();
	JPanel belongpanel;
	JScrollPane belongscroll;
	
	TeamPanel(String _teamname,JPanel _belongpanel,JScrollPane _belongscroll){
		teamname=_teamname;
		belongpanel=_belongpanel;
		belongscroll=_belongscroll;
		
		setLayout(null);
		setSize(_belongpanel.getWidth(),10);
	//	setLocation(3, 3);
		setBorder(BorderFactory.createEtchedBorder());
		
	//	belongpanel.add(this);
	//	validate();
	//	belongpanel.repaint();
	}
	
	void addmember(Fighter _fighter){
		members.add(_fighter);
		setPreferredSize(new Dimension(belongscroll.getWidth(), 5+members.size()*(5+_fighter.fightergui.getHeight())));
		
	}
	
	void removemember(Fighter _fighter){
		members.remove(_fighter);
		setPreferredSize(new Dimension(belongscroll.getWidth(), 5+members.size()*(5+_fighter.fightergui.getHeight())));
	}
	
	
	
}
