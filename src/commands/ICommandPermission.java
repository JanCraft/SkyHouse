package commands;

public class ICommandPermission {

	private ICommandSender[] senders;
	private ICommandException executionError;

	public ICommandPermission(ICommandSender[] senders, ICommandException error) {
		this.senders = senders;
		this.executionError = error;
	}

}
