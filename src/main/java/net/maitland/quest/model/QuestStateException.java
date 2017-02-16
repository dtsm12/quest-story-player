package net.maitland.quest.model;

/**
 * Created by David on 19/12/2016.
 */
public class QuestStateException extends RuntimeException {

    public QuestStateException(String s) {
        super(s);
    }

    public QuestStateException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
