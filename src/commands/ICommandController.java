package commands;

import engineTester.Registry;

public class ICommandController extends ICommandConsole implements Registry {
	
	public ICommandController(ICommand[] commands) {
		super(commands);
	}

	public static ICommand entits;

	@Override
	public Exception getThrownException() {
		return error;
	}
	
}
