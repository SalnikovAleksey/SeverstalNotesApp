package ru.severstal.severstalnotesapp.model.CommandClass;

import ru.severstal.severstalnotesapp.model.CloudMessage;
import ru.severstal.severstalnotesapp.model.CommandType;

public class UnknownUser implements CloudMessage {

    @Override
    public CommandType getType() {
        return CommandType.UNKNOWN_USER;
    }
}
