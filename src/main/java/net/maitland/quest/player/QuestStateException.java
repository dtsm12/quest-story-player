package net.maitland.quest.player;

/**
 * Created by David on 19/12/2016.
 */
public class QuestStateException extends Exception {

    public QuestStateException(String s) {
        super(s);
    }

    public QuestStateException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
