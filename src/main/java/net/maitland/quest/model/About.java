package net.maitland.quest.model;

/**
 * Created by David on 18/12/2016.
 */
public class About {

    private String title;
    private String author;
    private String intro;

    public About() {
    }

    public About(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof About)) return false;

        About about = (About) o;

        if (!title.equals(about.title)) return false;
        return author.equals(about.author);

    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + author.hashCode();
        return result;
    }
}
