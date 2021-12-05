package pl.pazurkiewicz.oldtimers_rally.model.web;

import pl.pazurkiewicz.oldtimers_rally.model.Crew;

import java.util.LinkedList;
import java.util.List;

public class QrCodeListWrapper {
    private final List<QrCodePiece> pieces;

    public QrCodeListWrapper(List<QrCodePiece> pieces) {
        this.pieces = pieces;
    }

    public List<QrCodePiece> getPieces() {
        return pieces;
    }

    public List<QrCodePiece[]> getSpecial() {
        List<QrCodePiece[]> result = new LinkedList<>();
        QrCodePiece[] actual = new QrCodePiece[2];
        for (int i = 0; i < pieces.size(); i++) {
            actual[i % 2] = pieces.get(i);
            if (i % 2 == 1) {
                result.add(actual);
                actual = new QrCodePiece[2];
            }
        }
        if (actual[0] != null) {
            result.add(actual);
        }
        return result;
    }

    public static class QrCodePiece {
        private final Crew crew;
        private final String base64;

        public QrCodePiece(Crew crew, String base64) {
            this.crew = crew;
            this.base64 = base64;
        }

        public Crew getCrew() {
            return crew;
        }

        public String getBase64() {
            return base64;
        }
    }
}
