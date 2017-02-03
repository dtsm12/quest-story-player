package net.maitland.quest.model;

import java.util.*;

/**
 * Created by David on 19/12/2016.
 */
public class QuestState {

    public static final String MATH_FLOOR_MATH_RANDOM_6_1_TO_STRING = "(Math.floor(Math.random() * 6) + 1).toString()";
    public static final String MATH_FLOOR_MATH_RANDOM_100_1_TO_STRING = "(Math.floor(Math.random() * 100) + 1).toString()";
    public static final String MATH_FLOOR_MATH_RANDOM_1000_1_TO_STRING = "(Math.floor(Math.random() * 1000) + 1).toString()";
    public static final String MATH_FLOOR_MATH_RANDOM_13_1_TO_STRING = "(Math.floor(Math.random() * 13) + 1).toString()";
    private Map<String, String> attributes = new HashMap<>();

    public QuestState() {
        this(null);
    }

    public QuestState(Map<String, String> attributes) {

        if (attributes != null) {
            this.attributes = new HashMap<>(attributes);
        } else {
            this.attributes = new HashMap<>();
        }

        // add keyword attributes
        this.attributes.put(" greater ", "  >");
        this.attributes.put(" lower ", " < ");
        this.attributes.put(" and ", " && ");
        this.attributes.put(" or ", " || ");
        this.attributes.put("not ", "! ");
        this.attributes.put(" = ", " == ");
        this.attributes.put("{random 1, 6}", MATH_FLOOR_MATH_RANDOM_6_1_TO_STRING);
        this.attributes.put("{random 0, 100}", MATH_FLOOR_MATH_RANDOM_100_1_TO_STRING);
        this.attributes.put("{random 1, 1000}", MATH_FLOOR_MATH_RANDOM_1000_1_TO_STRING);
        this.attributes.put("{random 1, 13}", MATH_FLOOR_MATH_RANDOM_13_1_TO_STRING);
    }

    public Map<String, String> copyAttributes() {
        return new HashMap(this.attributes);
    }

    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(this.attributes);
    }

    public String getAttributeValue(String name) {
        return this.attributes.get(name);
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
