package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageParser {


    private static final String SEPARATOR_CHAR = "\\|";

    public static Message parseMessage(String msg) throws InvalidMessageFormatException {

        String[] components = msg.split(SEPARATOR_CHAR, -1);
        if (components.length != 3) {
            throw new InvalidMessageFormatException(String.format("The message '%s' is not a valid format, " +
                    "found %d components, expected 3.", msg, components.length));
        }

        try {
            Command command = validateCommand(components[0]);
            String packageName = validatePackageName(components[1]);
            List<String> dependencies = validateDependecies(components[2]);

            return new Message(command, packageName, dependencies);
        } catch (IllegalArgumentException ex) {
            throw new InvalidMessageFormatException(String.format("Command in message '%s' was not recognized", msg), ex);
        }

    }

    private static List<String> validateDependecies(String component) throws InvalidMessageFormatException {
        List<String> dependencies = new ArrayList<>();
        if (component.isEmpty()) {
            return dependencies;
        }

        String[] deps = component.split(",");
        for (String dep: deps) {
            throwExceptionIfContainsInvalidChars(dep, "Dependency name");
        }
        dependencies.addAll(Arrays.asList(deps));
        return dependencies;
    }

    private static String validatePackageName(String component) throws InvalidMessageFormatException {
        throwExceptionIfContainsInvalidChars(component, "Package name");
        return component;
    }

    private static Command validateCommand(String component) throws IllegalArgumentException {
        return Command.valueOf(component.toUpperCase());
    }

    private static void throwExceptionIfContainsInvalidChars(String str, String label) throws InvalidMessageFormatException {
        // Legal chars = 0-9, A-B, a-b, -, +, _
        String legalRegex = "^[a-zA-Z0-9-_+]*$";
        Pattern pattern = Pattern.compile(legalRegex);
        Matcher matcher = pattern.matcher(str);
        boolean legalChars =  matcher.find();
        if (!legalChars) {
            throw new InvalidMessageFormatException(String.format("%s: %s is invalid.", label, str));
        }
    }


}
