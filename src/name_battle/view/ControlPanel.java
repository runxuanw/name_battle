package name_battle.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import name_battle.ReadFile;

public class ControlPanel extends JTabbedPane implements ActionListener,ChangeListener{

	ViewFrame mainview;
	JPanel addpanel = new JPanel();
	JPanel monsterpanel=new JPanel();
	JPanel filepanel=new JPanel();
	
	JLabel name_tag=new JLabel("Name:");
	JLabel team_tag=new JLabel("Team:");
	TextField fightername=new TextField();
	TextField fighterteam=new TextField();
	//TextField teamname=new TextField();
	JButton loadbutton = new JButton("Load");
	JButton addbutton=new JButton("Add");
	JButton startbutton=new JButton("Fight");	
	JButton addgroupbutton=new JButton("Add Group");
	JButton clearbutton=new JButton("Clear All");
	JButton createbutton=new JButton("Generate");
	JButton savebutton=new JButton("Save");
	JButton addmonster=new JButton("Add Monster");;
	JSlider slider;
	JComboBox teamname;
	JComboBox monstername;
	JComboBox monsterteamname;
	
	
	
	ControlPanel(ViewFrame _mainview,int _x,int _y,int _width,int _height){

		
		mainview=_mainview;
		setBounds(_x,_y,_width,_height);
		
		ReadFile readteam=new ReadFile("team");
		teamname = new JComboBox();
		teamname.setBackground(Color.WHITE);
		readteam.addteam(teamname,mainview.teams);
		monsterteamname=new JComboBox();
		monsterteamname.setBackground(Color.WHITE);
		readteam.addteam(monsterteamname);
		
		ReadFile readmonster=new ReadFile("monster");
		monstername = new JComboBox();
		monstername.setBackground(Color.WHITE);
		readmonster.addmonster(monstername);
		
		//init control panel
		addpanel.setLayout(null);
		//-22 is hard code for tab height
		addpanel.setBounds(3, 3,getWidth()-5,getHeight()-22);
		
		filepanel.setLayout(null);
		filepanel.setBounds(3, 3,getWidth()-5,getHeight()-22);
		monsterpanel.setLayout(null);
		monsterpanel.setBounds(3,3,getWidth()-5,getHeight()-22);
		
        

        slider = new JSlider(JSlider.HORIZONTAL,1,9,1);//direction , min , max , current
        slider.setFont(new Font("Tahoma",Font.BOLD,12));
        slider.setMajorTickSpacing(1);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setPaintTrack(true);
        slider.setAutoscrolls(false);
        
      monstername.setBounds(3, 3, 100, 26);
      monsterpanel.add(monstername);
      AddNextRightComponent(monsterteamname,monsterpanel,monstername,100,26,3);
      AddNextRightComponent(addmonster,monsterpanel,monsterteamname,120,26,3);
      
      savebutton.setBounds(3, 3, 100, 26);
      filepanel.add(savebutton);
      AddNextDownComponent(loadbutton,filepanel,savebutton,savebutton.getWidth(),savebutton.getHeight(),3);

		//set components in control panel
		//init first component in container
        //left
		name_tag.setBounds(5,addpanel.getY()+3, name_tag.getText().length()*10, addpanel.getHeight()/4);
		addpanel.add(name_tag);
		AddNextRightComponent(fightername,addpanel,name_tag,addpanel.getWidth()/6,name_tag.getHeight(),3);
		AddNextDownComponent(team_tag,addpanel,name_tag,team_tag.getText().length()*10,name_tag.getHeight(),3);
		AddNextRightComponent(teamname,addpanel,team_tag,addpanel.getWidth()/6,team_tag.getHeight(),3);
		//middle
		AddNextRightComponent(fighterteam,addpanel,fightername,addpanel.getWidth()/6,fightername.getHeight(),3);
		AddNextRightComponent(slider,addpanel,teamname,addpanel.getWidth()/3,teamname.getHeight()*2,5);
		//right
		AddNextRightComponent(addbutton,addpanel,fightername,(fightername.getWidth()+name_tag.getWidth())*2/3,name_tag.getHeight(),addpanel.getWidth()/3+20);
		AddNextDownComponent(addgroupbutton,addpanel,addbutton,addbutton.getWidth()*2+3,addbutton.getHeight(),3);
		AddNextDownComponent(startbutton,addpanel,addgroupbutton,(addgroupbutton.getWidth()-3)/2,addgroupbutton.getHeight(),3);
		AddNextRightComponent(createbutton,addpanel,addbutton,addbutton.getWidth(),addbutton.getHeight(),3);
		AddNextRightComponent(clearbutton,addpanel,startbutton,startbutton.getWidth(),startbutton.getHeight(),3);
		
		
		
		
		loadbutton.addActionListener(this);
		startbutton.addActionListener(this);
		addbutton.addActionListener(this);
		addgroupbutton.addActionListener(this);
		clearbutton.addActionListener(this);
		createbutton.addActionListener(this);
		savebutton.addActionListener(this);
		slider.addChangeListener(this);
		addmonster.addActionListener(this);
		
		
		addTab("Add", addpanel);
		setMnemonicAt(0, KeyEvent.VK_1);
		addTab("File", filepanel);
		setMnemonicAt(1, KeyEvent.VK_2);
		addTab("Monster", monsterpanel);
		setMnemonicAt(2, KeyEvent.VK_3);
		
		mainview.add(this);
	}
	
	
	
	public void stateChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub
		mainview.func_sliderSpeed(slider.getValue());
	}


public void actionPerformed(ActionEvent e){
	if(e.getSource()==loadbutton){
		mainview.func_loadbutton(loadbutton);
	}
	if(e.getSource()==addbutton){
		if(fighterteam.getText().length()>0){
		mainview.addfighter(fightername.getText(),fighterteam.getText(),0);
		}
	}
	if(e.getSource()==addgroupbutton){
		mainview.func_addgroupbutton((String)teamname.getSelectedItem());
	}
	if(e.getSource()==createbutton){
		mainview.func_createbutton(fightername.getText(), (String)teamname.getSelectedItem(), slider.getValue());
	}
	if(e.getSource()==startbutton){
  		mainview.func_startbutton();
	}
	if(e.getSource()==savebutton){
		mainview.func_savebutton(savebutton);
	}
	if(e.getSource()==clearbutton){
		mainview.func_clearbutton();
	}
	if(e.getSource()==addmonster){
		mainview.addfighter((String)monstername.getSelectedItem(),(String)monsterteamname.getSelectedItem(),1);
	}
}


private void AddNextRightComponent(Component cmp,JPanel container, Component nearcmp,int width,int height,int padding){
	if(nearcmp.getX()+nearcmp.getWidth()+width+padding<container.getX()+container.getWidth()&&
			nearcmp.getY()+height<container.getY()+container.getHeight()){
	cmp.setBounds(nearcmp.getX()+nearcmp.getWidth()+padding, nearcmp.getY(), width, height);
	container.add(cmp);
	}
	else{
		int temp;
		temp=nearcmp.getX()+nearcmp.getWidth()+width+padding-(container.getX()+container.getWidth());
		System.out.println("error component's right - container's right:"+temp);
		temp=nearcmp.getY()+height-(container.getY()+container.getHeight());	
		System.out.println("error component's button - container's button:"+temp);
	}
}

private void AddNextDownComponent(Component cmp,JPanel container, Component nearcmp,int width,int height,int padding){
	if(nearcmp.getX()+width<container.getX()+container.getWidth()&&
			nearcmp.getY()+nearcmp.getHeight()+height+padding<container.getY()+container.getHeight()){
	cmp.setBounds(nearcmp.getX(), nearcmp.getY()+nearcmp.getHeight()+padding, width, height);
	container.add(cmp);
	}
	else{
		int temp;
		temp=nearcmp.getX()+width-(container.getX()+container.getWidth());
		System.out.println("error component's right - container's right:"+temp);
		temp=nearcmp.getY()+nearcmp.getHeight()+height+padding-(container.getY()+container.getHeight());	
		System.out.println("error component's button - container's button:"+temp);
	}
}

}
