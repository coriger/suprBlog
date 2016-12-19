package com.supr.mapper;

import com.supr.model.Manager;

public interface AdminMapper {

	int validateManager(Manager manager);

	Manager getManagerInfo(Manager manager);
	
}
