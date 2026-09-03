package com.example.ats.storage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;

import com.example.ats.dto.AtsEvent;

@Component
public class ActionStorage {

	private final List<AtsEvent> events = new CopyOnWriteArrayList<>();

	public void save(AtsEvent event) {
		events.add(event);
	}

	public List<AtsEvent> findAllSortedByTimeDesc() {
		List<AtsEvent> sortedList = new ArrayList<>(events);

		sortedList.sort(Comparator.comparing(AtsEvent::getTimeStamp).reversed());

		return sortedList;
	}
}
