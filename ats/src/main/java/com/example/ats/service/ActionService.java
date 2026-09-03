package com.example.ats.service;

import java.util.List;

import com.example.ats.dto.AtsEvent;

public interface ActionService {
	
	List<AtsEvent> findAllSortedByTimeDesc();
	void save(AtsEvent event);
	
}
