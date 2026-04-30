package com.andreysankov.itmoprog2sem.common.dto;

public enum CommandType {
    HELP("help", false, false),
    INFO("info", false, false),
    SHOW("show", false, false),
    ADD("add", false, true),
    UPDATE_ID("update_id", true, true),
    REMOVE_BY_ID("remove_by_id", true, false),
    CLEAR("clear", false, false),
    // EXECUTE_SCRIPT,
    EXIT("exit", false, false),
    ADD_IF_MIN("add_if_min", false, true),
    REMOVE_LOWER("remove_lower", false, true),
    HISTORY("history", false, false),
    AVERAGE_OF_PERSONAL_QUALITIES_MAXIMUM("average_of_personal_qualities_maximum", false, false),
    GROUP_COUNTING_BY_CREATION_DATE("group_counting_by_creation_date", false, false),
    PRINT_FIELD_DESCENDING_DISCIPLINE("print_field_descending_discipline", false, false),

    REGISTER("register", false, false),
    LOGIN("login", false, false);

    private final String commandName;
    private final boolean requiresArgument;
    private final boolean requiresLabwork;

    CommandType(String name, boolean requiresArgument, boolean requiresLabwork) {
        this.commandName = name;
        this.requiresArgument = requiresArgument;
        this.requiresLabwork = requiresLabwork;
    }

    public boolean isRequiredArgument() { return this.requiresArgument; }
    public boolean isRequiredLabwork() { return this.requiresLabwork; }
    public String getName() { return this.commandName; }
}
