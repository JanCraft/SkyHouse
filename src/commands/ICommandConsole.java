package commands;

public class ICommandConsole {
	
	public final ICommand[] commands;
	public ICommandSender[] users;
	
	public ICommandConsole(final ICommand[] commands) {
		this.commands = commands;
	}
	
	public void execute(final int command, final int user) throws ICommandException {
		commands[command].execute(users[user]);
	}
	
}
