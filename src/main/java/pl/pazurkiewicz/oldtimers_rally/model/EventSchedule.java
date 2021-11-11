package pl.pazurkiewicz.oldtimers_rally.model;

import javax.persistence.*;
import java.time.Instant;

@Table(name = "event_schedule", indexes = {
        @Index(name = "event_schedule_event_id_selected_order_uindex", columnList = "event_id, selected_order", unique = true)
})
@Entity
public class EventSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "selected_order", nullable = false)
    private Integer selectedOrder;

    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "time", nullable = false)
    private Integer time;

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Integer getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(Integer selectedOrder) {
        this.selectedOrder = selectedOrder;
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
}
