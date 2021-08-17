package org.aleks4ay.hotel.service;

import java.util.ArrayList;
import java.util.List;

public final class UtilService<T> {

    public List<T> doPagination(int positionOnPage, int page, List<T> entities) {
        int startPosition = positionOnPage * (page - 1);
        List<T> roomsAfterFilter = new ArrayList<>();

        if (entities.size() > startPosition) {
            for (int i = startPosition; i < startPosition + positionOnPage; i++) {
                if (i >= entities.size()) {
                    break;
                }
                roomsAfterFilter.add(entities.get(i));
            }
            return roomsAfterFilter;
        }
        return new ArrayList<>();
    }
}
