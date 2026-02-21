package com.andreysankov.itmoprog2sem.commands;

import com.andreysankov.itmoprog2sem.managers.Context;

public abstract class Command {
    private String name;
    private String description;
    private Context context;

    public abstract boolean execute(String[] arguments);

    public Command(Context context) {
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

    public boolean execute() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }
}
