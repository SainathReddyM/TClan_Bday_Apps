package com.thoughtclan.bday.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.thoughtclan.bday.entity.AppInstall;

public interface AppInstallsRepository extends CrudRepository<AppInstall,Integer>{

	public List<AppInstall> findByUserId(String userId);
	public List<AppInstall> findByIsProcessed(boolean isProcessed);
}
