package com.example.ats.service;

import java.util.List;

import com.example.ats.dto.AtsEvent;
import com.example.ats.exception.EventValidationException;
import com.example.ats.exception.StorageOverflowException;

public interface ActionService {

	List<AtsEvent> findAllSortedByTimeDesc();

	void save(AtsEvent event) throws EventValidationException, StorageOverflowException;

}
