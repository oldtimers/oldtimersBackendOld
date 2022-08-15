package pl.pazurkiewicz.oldtimers_rally.model.api.response2;

import pl.pazurkiewicz.oldtimers_rally.model.Score;

import java.util.List;

public class ShowScores {
    private final List<Score> scores;

    public ShowScores(List<Score> scores) {
        this.scores = scores;
    }

    public List<Score> getScores() {
        return scores;
    }
}
