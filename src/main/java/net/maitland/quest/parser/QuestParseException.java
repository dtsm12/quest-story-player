package net.maitland.quest.parser;

/**
 * Created by David on 07/02/2017.
 */
public class QuestParseException extends RuntimeException {

    public QuestParseException(String s) {
        super(s);
    }

    public QuestParseException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
