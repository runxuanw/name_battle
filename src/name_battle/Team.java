package name_battle;

import java.util.ArrayList;
import java.util.List;

//store team members from txt file

public class Team {
	public String teamname;
	public List<String> members=new ArrayList<String>();

	Team(String _teamname){
		teamname=_teamname;
	}
	void addmember(String _member){
		members.add(_member);
	}
}
