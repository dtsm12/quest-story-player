package net.maitland.quest.player;

/**
 * Created by David on 20/12/2016.
 */
public class ChoiceNotPossibleException extends Exception {
    public ChoiceNotPossibleException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ChoiceNotPossibleException(String s) {
        super(s);
    }

    public ChoiceNotPossibleException(Throwable throwable) {
        super(throwable);
    }
}
