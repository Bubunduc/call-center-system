package com.example.ats.service;

import java.util.List;

import com.example.ats.dto.AtsEvent;
import com.example.ats.exception.EventValidationException;

public interface ActionService {
	
	List<AtsEvent> findAllSortedByTimeDesc();
	void save(AtsEvent event) throws EventValidationException;
	
}
