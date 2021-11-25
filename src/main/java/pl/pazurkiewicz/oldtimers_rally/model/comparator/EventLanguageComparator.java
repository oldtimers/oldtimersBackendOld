package pl.pazurkiewicz.oldtimers_rally.model.comparator;

import org.jboss.logging.Logger;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguage;

import java.util.Comparator;

public class EventLanguageComparator implements Comparator<EventLanguage> {
    Logger log = Logger.getLogger(EventLanguageComparator.class.getSimpleName());

    @Override
    public int compare(EventLanguage o1, EventLanguage o2) {
        log.info("comparing");
        return o1.getPriority().compareTo(o2.getPriority());
    }
}
