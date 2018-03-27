package commands;

public abstract class ICommand {

	private ICommandPermission permission;
	private ICommandSender sender;
	private ICommandTag[] tags;
	private ICommandArgument[] args;

	public ICommand(ICommandPermission permission, ICommandArgument[] args, ICommandTag[] tags) {
		this.permission = permission;
		this.tags = tags;
		this.args = args;
	}

	public abstract void execute(ICommandSender sender) throws ICommandException;

}
