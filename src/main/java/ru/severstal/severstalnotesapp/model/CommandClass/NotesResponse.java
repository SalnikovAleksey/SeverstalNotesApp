package ru.severstal.severstalnotesapp.model.CommandClass;

import lombok.Data;
import ru.severstal.severstalnotesapp.model.CloudMessage;
import ru.severstal.severstalnotesapp.model.CommandType;

import java.util.List;

@Data
public class NotesResponse implements CloudMessage {
    List<String> notes;

    public NotesResponse(List<String> notes) {
        this.notes = notes;
    }

    @Override
    public CommandType getType() {
        return CommandType.NOTES_RESPONSE;
    }
}
