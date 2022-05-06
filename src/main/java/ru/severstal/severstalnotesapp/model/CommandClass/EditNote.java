package ru.severstal.severstalnotesapp.model.CommandClass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.severstal.severstalnotesapp.model.CloudMessage;
import ru.severstal.severstalnotesapp.model.CommandType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditNote implements CloudMessage {

    private String oldNote;
    private String newNote;

    @Override
    public CommandType getType() {
        return CommandType.EDIT_NOTE;
    }
}
