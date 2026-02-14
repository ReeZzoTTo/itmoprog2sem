package com.andreysankov.itmoprog2sem.commands;

import com.andreysankov.itmoprog2sem.managers.Context;

public abstract class AbstractCommand implements Command {
    private String name;
    private String description;
    private Context context;

    public abstract boolean execute();

    public AbstractCommand(Context context) {
        this.context = context;
    }

    protected void setName(String name) { this.name = name; }
    protected void setDescription(String description) { this.description = description; }


    public Context getContext() {
        return this.context;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return name + " (" + description + ")";
    }

    @Override
    public int hashCode() {
        return name.hashCode() + description.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AbstractCommand other = (AbstractCommand) obj;
        return name.equals(other.name) && description.equals(other.description);
    }
}
