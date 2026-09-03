package com.example.ats.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ats.dto.AtsEvent;
import com.example.ats.storage.ActionStorage;

@Service
public class ActionServiceImpl implements ActionService {
	private final ActionStorage actionStorage;

	public ActionServiceImpl(ActionStorage actionStorage) {
		this.actionStorage = actionStorage;
	}
	
	@Override
	public List<AtsEvent> findAllSortedByTimeDesc() {

		return actionStorage.findAllSortedByTimeDesc();
	}

	@Override
	public void save(AtsEvent event) {
		if (event.getTimeStamp() == null) {
			event.setTimeStamp(new Timestamp(System.currentTimeMillis()));
		}
		actionStorage.save(event);
	}
}
