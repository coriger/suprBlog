package com.supr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.supr.mapper.AdminMapper;
import com.supr.model.Manager;
import com.supr.util.MD5;

@Service
@Transactional(isolation=Isolation.DEFAULT,rollbackFor=Exception.class)
public class AdminService{
	
	@Autowired
	private AdminMapper adminMapper;
	
	public boolean validateManager(Manager manager) {
		manager.setPassword(MD5.digest(manager.getPassword()));
		return adminMapper.validateManager(manager) > 0 ? true : false;
	}
	
	public Manager getManagerInfo(Manager manager) {
		return adminMapper.getManagerInfo(manager);
	}
	
}
