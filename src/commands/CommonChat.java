package commands;

import entities.Camera;
import toolbox.Maths;

public class CommonChat extends IChat {
	
	public CommonChat(char ichar, int key) {
		super(key);
		
		this.commandLineChar = ichar;
	}

	@Override
	protected boolean isCommandAndExecute(String command, Camera player) {
		if(command.startsWith("gamemode", 1)) {
			int param = Integer.parseInt(command.substring(10));
			if(param < 0 || param > 1) {
				error = "Parameters not found or lesser/bigger than needed!";
				return false;
			}
			player.capabilities.changeCapability(0, 0, param);
			player.capabilities.changeCapability(0, 1, Maths.IntToBoolean(param));
			return true;
		}
		
		error = "Not reconogized command!";
		return false;
	}

}
