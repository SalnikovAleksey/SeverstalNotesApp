package ru.severstal.severstalnotesapp.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import ru.severstal.severstalnotesapp.model.CloudMessage;
import ru.severstal.severstalnotesapp.model.CommandClass.*;
import ru.severstal.severstalnotesapp.server.entity.Notes;
import ru.severstal.severstalnotesapp.server.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<CloudMessage> {
    private String login;
    int idUser;
    private List<String> notes;
    private final EntityManager entityManager;

    public ServerHandler(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        notes = new ArrayList<>();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CloudMessage cloudMessage) {
        log.info("Received: " + cloudMessage);

        switch (cloudMessage.getType()) {

            case SIGN_IN:
                processSingIn((SignIn) cloudMessage, ctx);
                break;
            case SIGN_UP:
                processSignUP((SignUp) cloudMessage, ctx);
                break;
            case CONNECTED:
                processConnected((UserConnected) cloudMessage, ctx);
                break;
            case NOTES_POST:
                processPostNote((PostNotes) cloudMessage, ctx);
                break;
            case DELETE_NOTE:
                processDeleteNote((DeleteNote) cloudMessage);
                break;
            case EDIT_NOTE:
                processEditNote((EditNote) cloudMessage);
                break;
        }
    }

    private void processEditNote(EditNote cloudMessage) {
        if (cloudMessage.getOldNote() != null && cloudMessage.getNewNote() != null) {
            int idNote = entityManager.createNamedQuery("Notes.findNotesByNote", Notes.class)
                    .setParameter("note", cloudMessage.getOldNote())
                    .getSingleResult()
                    .getId();
            entityManager.getTransaction().begin();
            entityManager.merge(Notes.builder()
                    .id(idNote)
                    .userid(idUser)
                    .note(cloudMessage.getNewNote())
                    .build());
            entityManager.getTransaction().commit();
        }
    }

    private void processDeleteNote(DeleteNote cloudMessage) {

        if (cloudMessage.getNote() != null) {
            entityManager.getTransaction().begin();
            entityManager.remove(
                    entityManager.createNamedQuery("Notes.findNotesByNote", Notes.class)
                            .setParameter("note", cloudMessage.getNote())
                            .getSingleResult());
            entityManager.getTransaction().commit();
        }
    }

    private void processPostNote(PostNotes cloudMessage, ChannelHandlerContext ctx) {

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(Notes.builder()
                    .note(cloudMessage.getNotes())
                    .userid(idUser)
                    .build());
            entityManager.getTransaction().commit();
            notesResponse(ctx);
        }catch (IllegalStateException ise) {
            entityManager.getTransaction().commit();
        }
    }

    private void notesResponse(ChannelHandlerContext ctx) {
        notes.clear();
        for (Notes note :
                entityManager.createNamedQuery("Notes.findNotesByUserId", Notes.class).setParameter("userId", idUser).getResultList()) {
            if (note != null) {
                notes.add(note.getNote());
            }
        }
        if (notes.isEmpty()) {
            notes.add("Your first note !");
        }
        ctx.writeAndFlush(new NotesResponse(notes));
    }


    private void processConnected(UserConnected cloudMessage, ChannelHandlerContext ctx) {
        login = cloudMessage.getLogin();
        idUser = entityManager.createNamedQuery("User.findUserByUsername", User.class)
                .setParameter("username", login)
                .getSingleResult()
                .getId();
        notesResponse(ctx);
    }

    private void processSignUP(SignUp cloudMessage, ChannelHandlerContext ctx) {

        try {
            entityManager.createNamedQuery("User.findUser", User.class)
                    .setParameter("password", cloudMessage.getPass())
                    .setParameter("username", cloudMessage.getLogin())
                    .getSingleResult().getId();

            ctx.writeAndFlush(new UnknownUser());
        } catch (NoResultException nre) {
            entityManager.getTransaction().begin();
            entityManager.persist(User.builder()
                    .username(cloudMessage.getLogin())
                    .password(cloudMessage.getPass())
                    .build());
            entityManager.getTransaction().commit();
            ctx.writeAndFlush(new ConfirmedUser());
        }

    }

    private void processSingIn(SignIn cloudMessage, ChannelHandlerContext ctx) {

        try {
            entityManager.createNamedQuery("User.findUser", User.class)
                    .setParameter("password", cloudMessage.getPass())
                    .setParameter("username", cloudMessage.getLogin())
                    .getSingleResult().getId();

            ctx.writeAndFlush(new ConfirmedUser());
        } catch (NoResultException nre) {
            ctx.writeAndFlush(new UnknownUser());
        }
    }
}

