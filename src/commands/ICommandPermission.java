package commands;

public class ICommandPermission {

	public final int level;
	private ICommandException executionError;

	public ICommandPermission(int level, ICommandException error) {
		this.level = level;
		this.executionError = error;
	}
	
	public ICommandException getError() {
		return executionError;
	}

}
