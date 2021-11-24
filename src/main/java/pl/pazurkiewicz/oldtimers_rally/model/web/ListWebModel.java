package pl.pazurkiewicz.oldtimers_rally.model.web;

import pl.pazurkiewicz.oldtimers_rally.model.DatabaseModel;

import java.util.List;
import java.util.Set;

public interface ListWebModel<T extends DatabaseModel> {
    default void removeFromList(Integer removeId, List<T> categories, Set<Integer> deletedList) {
        if (removeId != null && categories.size() > removeId && removeId >= 0) {
            T removedObject = categories.get(removeId);
            categories.remove(removeId.intValue());
            if (removedObject.getId() != null) {
                deletedList.add(removedObject.getId());
            }
        }
    }
}
