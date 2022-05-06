package ru.severstal.severstalnotesapp.model.CommandClass;

import lombok.*;
import ru.severstal.severstalnotesapp.model.CloudMessage;
import ru.severstal.severstalnotesapp.model.CommandType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeleteNote implements CloudMessage {

    private String note;

    @Override
    public CommandType getType() {
        return CommandType.DELETE_NOTE;
    }
}
