package org.duracloud.duradmin.view;


public class MainMenu extends Menu{
	private static final long serialVersionUID = 1L;
	public static final String HOME = "home";
	public static final String SPACES = "spaces";
	public static final String SERVICES = "services";
	private static Menu instance;
	private MainMenu (){
		addMenuItem(HOME,"/", "general.home");
		addMenuItem(SPACES,"/spaces.htm", "general.spaces");
		addMenuItem(SERVICES,"/services.htm", "general.services");
	}
	
	public static Menu instance(){
		if(instance == null){
			instance = new MainMenu();
		}
		return instance;
	}


}