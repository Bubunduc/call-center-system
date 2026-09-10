package com.example.ats.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ats.dto.AtsEvent;
import com.example.ats.exception.EventValidationException;
import com.example.ats.storage.ActionStorage;

@Service
public class ActionServiceImpl implements ActionService {
	private final ActionStorage actionStorage;

	private static final String PHONE_REGEX = "^8-\\d{3}-\\d{3}-\\d{2}-\\d{2}$";

	public ActionServiceImpl(ActionStorage actionStorage) {
		this.actionStorage = actionStorage;
	}

	@Override
	public List<AtsEvent> findAllSortedByTimeDesc() {

		return actionStorage.findAllSortedByTimeDesc();
	}

	@Override
	public void save(AtsEvent event) throws EventValidationException {
		if (event.getPhoneNumber() == null || event.getPhoneNumber().isEmpty() || event.getStatus() == null) {
			throw new EventValidationException("Поля телефонного номера и события являются обязательными к заполнению");
		}
		
		if (!event.getPhoneNumber().matches(PHONE_REGEX)) {
			throw new EventValidationException("Номер телефона не соответствует формату вида 8-xxx-xxx-xx-xx");
		}
		
		if (event.getTimeStamp() == null) {
			event.setTimeStamp(new Timestamp(System.currentTimeMillis()));
		}
		actionStorage.save(event);
	}
}
