package ru.severstal.severstalnotesapp.client;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import lombok.extern.slf4j.Slf4j;
import ru.severstal.severstalnotesapp.model.CloudMessage;
import ru.severstal.severstalnotesapp.model.CommandClass.*;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.*;

@Slf4j
public class Client implements Initializable {
    public ListView<String> listView;
    private ObjectDecoderInputStream is;
    private ObjectEncoderOutputStream os;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            Socket socket = new Socket("localhost", 8189);

            log.info("Network created...");

            os = new ObjectEncoderOutputStream(socket.getOutputStream());
            is = new ObjectDecoderInputStream(socket.getInputStream());

            os.writeObject(new UserConnected(Authorization.getLogin()));

            Thread readThread = new Thread(this::readLoop);
            readThread.setDaemon(true);
            readThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readLoop() {
        try {
            while (true) {
                CloudMessage message = (CloudMessage) is.readObject();
                log.info("received: {}", message);

                switch (message.getType()) {
                    case NOTES_RESPONSE:
                        processNotesResponse((NotesResponse) message);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processNotesResponse(NotesResponse message) {
        Platform.runLater(() -> {
            listView.getItems().clear();

            for (String note : message.getNotes()) {
                listView.getItems().add(note);
            }
        });
////        listView.getItems().clear();
//        for (String note : message.getNotes()) {
//            listView.getItems().add(note);
//        }
    }

    private void postNotes(String note) throws IOException {
        os.writeObject(new PostNotes(note));
    }

    private String getSelectNote() {
        return listView.getSelectionModel().getSelectedItem();
    }

    private void warningWin() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("The operation cannot be performed.");
        alert.setContentText("You have not selected a note.");
        alert.showAndWait();
    }

    public void addBut(ActionEvent actionEvent) {

        TextInputDialog dialog = new TextInputDialog("Enter the text");

        dialog.setTitle("Creating a new note.");
        dialog.setHeaderText("Creating a new note.");
        dialog.setContentText("Please enter new text note:");

//        Platform.runLater(() -> {

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String note = result.get() + " " + new Date();
                listView.getItems().add(note);

                try {
                    postNotes(note);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
//        });
    }

    public void deleteBut(ActionEvent actionEvent) throws IOException {
        if (getSelectNote() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setTitle("Confirmation delete");
            alert.setHeaderText("Are you sure you want to delete the note");
            alert.setContentText("Are you ok with this?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                os.writeObject(new DeleteNote(getSelectNote()));
                listView.getItems().remove(getSelectNote());
            }
        } else {
            warningWin();
        }
    }

    public void editBut(ActionEvent actionEvent) {

        TextInputDialog dialog = new TextInputDialog(getSelectNote());

        dialog.setTitle("Edit note");
        dialog.setHeaderText("Changing the note text");
        dialog.setContentText("Please enter new text:");

        Platform.runLater(() -> {
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                try {
                    os.writeObject(new EditNote(getSelectNote(), result.get()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < listView.getItems().toArray().length; i++) {
                    if (listView.getItems().get(i).equals(getSelectNote())) {
                        listView.getItems().remove(i);
                        listView.getItems().add(result.get());
                    }
                }
            }
        });
    }
}
