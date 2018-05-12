package commands;

import entities.Camera;

public abstract class IChat {
	
	protected char commandLineChar;
	
	private int openKey;
	
	public boolean isOpen;
	
	protected String error = "Unknown";

	public boolean pressed;
	
	public IChat(int openKey) {
		this.openKey = openKey;
	}
	
	protected abstract boolean isCommandAndExecute(String command, Camera player);
	
	public boolean executeCommand(String command, Camera player) {
		boolean ret = false;
		
		if(command.charAt(0) == commandLineChar) {
			ret = isCommandAndExecute(command, player);
		}
		
		return ret;
	}
	
	public int getOpeningKey() {
		return openKey;
	}

	public String getErrorMessage() {
		return error;
	}
	
}
