package name_battle.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import name_battle.Fighter;


public class FighterGui extends JPanel{


	Fighter fighter;
	public SidePanel belongpanel;
	public JScrollPane belongscroll;
	//JLabel nametag;
	JLabel name;
	JLabel organize;
	int max_hp;
	int max_gui=6;
	BufferedImage img=null;
	BufferedImage grayimg=null;
	int icon_size=45;
	List<JLabel> attributes=new ArrayList<JLabel>();
	
	public FighterGui(Fighter _fighter,SidePanel _belongpanel,JScrollPane _belongScroll,int _order,String _facefile){
		fighter=_fighter;
		belongpanel=_belongpanel;
		belongscroll=_belongScroll;
		
		belongpanel.setPreferredSize(new Dimension(belongscroll.getWidth(), 5+_order*(belongscroll.getHeight()/max_gui-2)+belongscroll.getHeight()/max_gui-5));
		
		this.setLayout(null);
		this.setBounds(5, 5+_order*(belongscroll.getHeight()/max_gui-2), belongscroll.getWidth()-30,belongscroll.getHeight()/max_gui-5);
		this.setBorder(BorderFactory.createEtchedBorder());
		
		//set img
		try {
			File imgfile=new File("./picture/face/"+_facefile);
		    img = ImageIO.read(imgfile);
		    
		    int width = img.getWidth();  
		    int height = img.getHeight();  
		    grayimg = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		    for(int i= 0 ; i < width ; i++){  
		        for(int j = 0 ; j < height; j++){  
		        int rgb = img.getRGB(i, j);  
		        grayimg.setRGB(i, j, rgb);  
		        }  
		    }  
		} catch (IOException e) {
			System.out.print("no image found");
		}
		
		organize=new JLabel(fighter.team,SwingConstants.CENTER);
		organize.setBounds(icon_size+5, 3, (getWidth()-icon_size)/2-25, 20);
		this.add(organize);
		
		name=new JLabel(fighter.name,SwingConstants.CENTER);
		name.setBounds(icon_size+5, 23, (getWidth()-icon_size)/2-25, 20);
		this.add(name);
		
		for(int i=0;i<fighter.attributes.size();i++){
			
			JLabel namelabel=new JLabel("<html>"+fighter.attributes.get(i).name+"</html>",SwingConstants.CENTER);
			namelabel.setName(fighter.attributes.get(i).name);
			if(fighter.attributes.get(i).name.equals("life")){
				namelabel.setBounds(5, name.getY()+name.getHeight()+5, namelabel.getText().length()*3, 20);
			}
			else{
				namelabel.setBounds(20+(getWidth()/fighter.attributes.size()-5)*i, name.getY()+name.getHeight()+5, namelabel.getText().length()*3, 20);
			}
			//attributes.add(namelabel);
			this.add(namelabel);
			
			JLabel datalabel=new JLabel("",SwingConstants.CENTER);
			if(fighter.attributes.get(i).name.equals("life")){
				max_hp=fighter.attributes.get(i).amount;
				datalabel.setText("<html>"+Integer.toString(fighter.attributes.get(i).amount)+"/"+max_hp+"</html>");
			}
			else{
				datalabel.setText("<html>"+Integer.toString(fighter.attributes.get(i).amount)+"</html>");
				}
			datalabel.setName(fighter.attributes.get(i).name);
			AddNextComponent(datalabel, this, namelabel, datalabel.getText().length()*3, 20, 0,namelabel.getHeight()+5);
			attributes.add(datalabel);
			this.add(datalabel);
		}

		belongpanel.add(this);
		this.validate();
  		belongpanel.revalidate();
  		belongpanel.repaint();
	}
	

	
@Override
protected void paintComponent(Graphics g){
	super.paintComponent(g);
	
	//gradually change life's color
	int hp=fighter.getAttributeAmount("life");
	int red=255;
	if(max_hp/2<=hp){
		red=255*(max_hp-hp)/(max_hp/2);
	}
	int green=255;
	if(max_hp/2>hp){
		green=255*hp/(max_hp/2);
	}
	Color tempcolor=new Color(red,green,0);
	g.setColor(tempcolor);
	g.fillRect(getWidth()/2, name.getY()+name.getHeight()/3, (getWidth()/2-10)*hp/max_hp, 10);
	
	//paint face
	if(fighter.alive){
		g.drawImage(img, 3, 3, icon_size, icon_size, null);
	}
	else{
		g.drawImage(grayimg, 3, 3, icon_size, icon_size, null);
	}
	
	
	
}
	
	public void updateAttribute(String attr_name,int _amount,String color){
		for(int counter=0;counter<attributes.size();counter++){
			if(attributes.get(counter).getName().equals(attr_name)){
				if(attr_name.equals("life")){
					attributes.get(counter).setText("<html><font color="+color+">"+Integer.toString(_amount)+"</font>/"+max_hp+"</html>");
				}
				else{
					attributes.get(counter).setText("<html><font color="+color+">"+Integer.toString(_amount)+"</font></html>");
				}
			}
		}
		repaint();
	}
	

private void AddNextComponent(Component cmp,JPanel container, Component nearcmp,int width,int height,int rightpadding,int downpadding){
	if(nearcmp.getX()+width+rightpadding<container.getX()+container.getWidth()&&
			nearcmp.getY()+height+downpadding<container.getY()+container.getHeight()){
	cmp.setBounds(nearcmp.getX()+rightpadding, nearcmp.getY()+downpadding, width, height);
	}
	else{
		int temp;
		temp=nearcmp.getX()+width+rightpadding-(container.getX()+container.getWidth());
		System.out.println("error component's right - container's right:"+temp);
		temp=nearcmp.getY()+height+downpadding-(container.getY()+container.getHeight());	
		System.out.println("error component's button - container's button:"+temp);
	}
}

	
}
