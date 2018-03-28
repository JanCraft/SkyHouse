package commands;

public class ICommandException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public ICommandException(final String error) {
		super(error);
	}
	
}
