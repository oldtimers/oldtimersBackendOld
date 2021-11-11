package pl.pazurkiewicz.oldtimers_rally.model;

import javax.persistence.*;

@Table(name = "user_groups", indexes = {
        @Index(name = "user_groups_event_id_user_id_uindex", columnList = "event_id, user_id", unique = true)
})
@Entity
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "selected_group", nullable = false, columnDefinition = "enum")
    private UserGroupEnum selectedGroup;

    public UserGroup() {
    }

    public UserGroupEnum getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(UserGroupEnum selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserGroup(Event event, User user, UserGroupEnum selectedGroup) {
        this.event = event;
        this.user = user;
        this.selectedGroup = selectedGroup;
    }
}
