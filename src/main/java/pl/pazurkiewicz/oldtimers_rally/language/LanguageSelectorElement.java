package pl.pazurkiewicz.oldtimers_rally.language;

public class LanguageSelectorElement {
        private final Language language;
        private Boolean accept;

        LanguageSelectorElement(Language language, Boolean accept) {
            this.language = language;
            this.accept = accept;
        }

        public Language getLanguage() {
            return language;
        }

        public Boolean getAccept() {
            return accept;
        }

        public void setAccept(Boolean accept) {
            this.accept = accept;
        }
    }
