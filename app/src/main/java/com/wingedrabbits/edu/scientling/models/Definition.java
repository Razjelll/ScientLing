package com.wingedrabbits.edu.scientling.models;

/**
 * Created by Razjelll on 09.11.2016.
 */

public class Definition {
    private long id;
    private String definition;
    private String translation;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Definition that = (Definition) o;

        if (id != that.id) return false;
        if (definition != null ? !definition.equals(that.definition) : that.definition != null)
            return false;
        return translation != null ? translation.equals(that.translation) : that.translation == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (definition != null ? definition.hashCode() : 0);
        result = 31 * result + (translation != null ? translation.hashCode() : 0);
        return result;
    }
}
