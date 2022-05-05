package ru.severstal.severstalnotesapp.model.CommandClass;

import lombok.Data;
import ru.severstal.severstalnotesapp.model.CloudMessage;
import ru.severstal.severstalnotesapp.model.CommandType;

import java.util.List;

@Data
public class PostNotes implements CloudMessage {

    public PostNotes(String notes) {
        this.notes = notes;
    }

    String notes;

    @Override
    public CommandType getType() {
        return CommandType.NOTES_POST;
    }
}
