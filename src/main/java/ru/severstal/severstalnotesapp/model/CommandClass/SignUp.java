package ru.severstal.severstalnotesapp.model.CommandClass;

import lombok.Data;
import ru.severstal.severstalnotesapp.model.CloudMessage;
import ru.severstal.severstalnotesapp.model.CommandType;

@Data
public class SignUp implements CloudMessage {

    private final String login;
    private final String pass;

    public SignUp(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

    @Override
    public CommandType getType() {
        return CommandType.SIGN_UP;
    }
}
