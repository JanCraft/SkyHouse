package commands;

public abstract class ICommand {

	public final ICommandPermission permission;
	public final ICommandTag[] tags;
	public final ICommandArgument[] args;

	public ICommand(ICommandPermission permission, ICommandArgument[] args, ICommandTag[] tags) {
		this.permission = permission;
		this.tags = tags;
		this.args = args;
	}

	public abstract void execute(ICommandSender sender) throws ICommandException;
	
	public abstract void execute(ICommandSender sender, ICommandArgument[] args, ICommandTag[] tags) throws ICommandException;

}
