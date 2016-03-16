package parser;


import java.util.List;

public class Message {

    public Command command;
    public String packageName;
    public List<String> dependencies;

    public Message(Command command, String packageName, List<String> dependencies) {
        this.command = command;
        this.packageName = packageName;
        this.dependencies = dependencies;
    }


}
