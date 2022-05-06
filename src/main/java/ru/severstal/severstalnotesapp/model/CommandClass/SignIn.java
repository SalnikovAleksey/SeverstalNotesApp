package ru.severstal.severstalnotesapp.model.CommandClass;

import lombok.Data;
import ru.severstal.severstalnotesapp.model.CloudMessage;
import ru.severstal.severstalnotesapp.model.CommandType;

@Data
public class SignIn implements CloudMessage {

    private final String login;
    private final String pass;

    public SignIn(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

    @Override
    public CommandType getType() {
        return CommandType.SIGN_IN;
    }
}
