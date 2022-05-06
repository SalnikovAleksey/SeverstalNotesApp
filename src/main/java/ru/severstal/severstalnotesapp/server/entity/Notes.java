package ru.severstal.severstalnotesapp.server.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = ("user_notes"))
@NamedQueries ({
        @NamedQuery(name = "Notes.findNotesByUserId", query = "SELECT n FROM Notes n WHERE n.userid = :userId"),
        @NamedQuery(name = "Notes.findNotesByNote", query = "SELECT n FROM Notes n WHERE n.note = :note")
})
public class Notes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "user_id")
    private Integer userid;
    @Column(name = "note")
    private String note;
}
