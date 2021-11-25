package pl.pazurkiewicz.oldtimers_rally.model.comparator;

import pl.pazurkiewicz.oldtimers_rally.model.Dictionary;

import java.util.Comparator;

public class DictionaryComparator implements Comparator<Dictionary> {
    @Override
    public int compare(Dictionary o1, Dictionary o2) {
        return o1.getEventLanguage().getPriority().compareTo(o2.getEventLanguage().getPriority());
    }
}
