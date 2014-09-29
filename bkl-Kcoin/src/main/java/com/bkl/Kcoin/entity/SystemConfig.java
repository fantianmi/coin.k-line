package com.bkl.Kcoin.entity;

import com.km.common.dao.TableAonn;

@TableAonn(tableName = "systemconfig")
public class SystemConfig {
	
	private long id;
	private String configKey;
	private String configValue;

	public SystemConfig() {
		
	}
	
	public SystemConfig(String configKey, String configValue) {
		this.configKey = configKey;
		this.configValue = configValue;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}
	
}
