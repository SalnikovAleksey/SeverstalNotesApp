package ru.severstal.severstalnotesapp.model.CommandClass;

import lombok.Data;
import ru.severstal.severstalnotesapp.model.CloudMessage;
import ru.severstal.severstalnotesapp.model.CommandType;

@Data
public class UserConnected implements CloudMessage {

    private String login;

    public UserConnected(String login) {
        this.login = login;
    }

    @Override
    public CommandType getType() {
        return CommandType.CONNECTED;
    }
}
