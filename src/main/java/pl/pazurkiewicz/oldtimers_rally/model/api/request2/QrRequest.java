package pl.pazurkiewicz.oldtimers_rally.model.api.request2;

import javax.validation.constraints.NotNull;

public class QrRequest {
    @NotNull
    private String qr;

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }
}
