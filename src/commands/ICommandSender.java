package commands;

import entities.Camera;

public class ICommandSender {

	public final int permissionLevel;
	public Camera[] players;

	public ICommandSender(int permissionLevel) {
		this.permissionLevel = permissionLevel;
	}

}
