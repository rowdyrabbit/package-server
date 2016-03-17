package protocol;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.InvalidMessageFormatException;
import parser.Message;
import parser.MessageParser;
import repository.Repository;

import static parser.Command.*;
import static protocol.ResponseCode.*;

public class MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);


    public static ResponseCode handle(Repository repo, String message) {
        try {
            boolean success = false;

            Message msg = MessageParser.parseMessage(message);

            if (msg.command == INDEX) {
                logger.debug(String.format("Calling INDEX function with package name: %s and dependencies %s", msg.packageName, msg.dependencies));
                success = repo.addPackage(msg.packageName, msg.dependencies);
            } else if (msg.command == QUERY) {
                logger.debug(String.format("Calling QUERY function with package name: %s", msg.packageName));
                success = repo.packageInstalled(msg.packageName);
            } else if (msg.command == REMOVE) {
                logger.debug(String.format("Calling REMOVE function with package name: %s", msg.packageName));
                success = repo.removePackage(msg.packageName);
            } else {
                throw new InvalidMessageFormatException("Query not recognized");
            }

            if (success) {
                return OK;
            } else {
                return FAIL;
            }

        } catch (InvalidMessageFormatException ife) {
            logger.error(ife.getMessage(), ife);
            return ERROR;
        }
    }




}
