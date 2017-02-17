package world;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import name_battle.view.ViewFrame;
public class StartPanel extends JFrame implements ActionListener{

	JPanel gamemenu=new JPanel();
	JButton newgame=new JButton("New Game");
	JButton loadgame=new JButton("Load Game");
	JButton arena=new JButton("Arena");
	JButton exit=new JButton("Exit");
	int buttonwide=150;
	int buttonheight=40;
	
	StartPanel(){
		//init frame
		setTitle("Ãû×ÖÊÀ½ç");
		setSize(500,400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		
		gamemenu.setSize(getWidth(),getHeight());
		gamemenu.setLocation(0,0);
		gamemenu.setBorder(BorderFactory.createEtchedBorder());
		gamemenu.setBackground(Color.WHITE);
		gamemenu.setLayout(null);
		
		newgame.setBounds((getWidth()-buttonwide)/2, buttonheight, buttonwide, buttonheight);
		AddNextDownComponent(loadgame, gamemenu, newgame, buttonwide, buttonheight, 5);
		AddNextDownComponent(arena, gamemenu, loadgame, buttonwide, buttonheight, 5);
		AddNextDownComponent(exit, gamemenu, arena, buttonwide, buttonheight, 5);
		
		setSize(500,exit.getY()+buttonheight*2+43);
		
		gamemenu.add(newgame);
		gamemenu.add(loadgame);
		gamemenu.add(arena);
		gamemenu.add(exit);
		
		add(gamemenu);
		setVisible(true);
		
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 StartPanel startgame=new StartPanel();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
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
