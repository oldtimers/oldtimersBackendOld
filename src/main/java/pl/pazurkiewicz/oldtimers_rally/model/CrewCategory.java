package pl.pazurkiewicz.oldtimers_rally.model;

import javax.persistence.*;

@Table(name = "crew_categories", indexes = {
        @Index(name = "crew_categories_ranking_points_index", columnList = "ranking_points")
})
@Entity
public class CrewCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "crew_id", nullable = false)
    private Crew crew;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "ranking_points")
    private Double rankingPoints;

    public Double getRankingPoints() {
        return rankingPoints;
    }

    public void setRankingPoints(Double rankingPoints) {
        this.rankingPoints = rankingPoints;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Crew getCrew() {
        return crew;
    }

    public void setCrew(Crew crew) {
        this.crew = crew;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}