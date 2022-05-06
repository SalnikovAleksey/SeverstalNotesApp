package ru.severstal.severstalnotesapp.model.CommandClass;

import ru.severstal.severstalnotesapp.model.CloudMessage;
import ru.severstal.severstalnotesapp.model.CommandType;

public class ConfirmedUser implements CloudMessage {
    @Override
    public CommandType getType() {
        return CommandType.CONFIRM_USER;
    }
}
