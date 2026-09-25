package com.example.ats.storage;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;

import com.example.ats.dto.AtsEvent;
import com.example.ats.exception.StorageOverflowException;

@Component
public class ActionStorage {

	private final List<AtsEvent> events = new CopyOnWriteArrayList<>();
	private static final int EVENT_LIMIT = 10000;

	public synchronized void save(AtsEvent event) throws StorageOverflowException {
		if (events.size() >= EVENT_LIMIT) {
			throw new StorageOverflowException("Хранилище переполнено");
		}
		events.add(event);

	}

	public List<AtsEvent> findAllSortedByTimeDesc() {

		events.sort(Comparator.comparing(AtsEvent::getTimeStamp).reversed());

		return events;
	}
}
