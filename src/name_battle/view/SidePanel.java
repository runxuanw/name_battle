package name_battle.view;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
//unused
public class SidePanel extends JPanel{
	
	int size=0;
	public ViewFrame view;
	JScrollPane Scroll = new JScrollPane(
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	SidePanel(ViewFrame _view,int scrollx,int scrolly,int scrollwidth,int scrollheight){
		view=_view;
		setLayout(null);
		Scroll.setSize(scrollwidth,scrollheight);
		Scroll.setLocation(scrollx,scrolly);
		Scroll.setBorder(BorderFactory.createEtchedBorder());
		Scroll.setViewportView(this);
		view.add(Scroll);
	}
		
}
