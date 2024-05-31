package ru.mars.commands;

import ru.mars.enums.State;

public interface ChangeStateCommand {

    void execute(ChangeStateContext changeStateContext);

    boolean availableFor(State state);
}
