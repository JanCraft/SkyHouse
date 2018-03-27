package commands;

public class ICommandTag {

	public final String name;
	public final boolean Abstract;
	public String[] permitted;
	public String typed;

	public ICommandTag(final String name, final boolean Abstract) {
		this.name = name;
		this.Abstract = Abstract;
	}

}
