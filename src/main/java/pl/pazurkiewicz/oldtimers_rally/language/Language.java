package pl.pazurkiewicz.oldtimers_rally.language;

import javax.persistence.*;

@Table(name = "languages", indexes = {
        @Index(name = "languages_code_uindex", columnList = "code", unique = true)
})
@Entity
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "code", nullable = false, length = 4)
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
