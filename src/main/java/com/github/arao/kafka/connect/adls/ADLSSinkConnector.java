/**
 * Copyright © 2019 Archana Rao (archana.rao4@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.arao.kafka.connect.adls;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ADLSSinkConnector extends SinkConnector {

	private static Logger log = LoggerFactory.getLogger(ADLSSinkConnector.class);
	private ADLSSinkConnectorConfig config;
	Map<String, String> settings;

	@Override
	public List<Map<String, String>> taskConfigs(int maxTasks) {
		 List<Map<String, String>> configs = new ArrayList<>();
		    for (int i = 0; i < maxTasks; i++) {
		      configs.add(this.settings);
		    }
		    return configs;
	}

	@Override
	public void start(Map<String, String> map) {
		config = new ADLSSinkConnectorConfig(map);
		 this.settings = map;

	}

	@Override
	public void stop() {
		// TODO: Do things that are necessary to stop your connector.
	}

	@Override
	public ConfigDef config() {
		return ADLSSinkConnectorConfig.config();
	}

	@Override
	public Class<? extends Task> taskClass() {
		// TODO: Return your task implementation.
		return ADLSSinkTask.class;
	}

	@Override
	public String version() {
		// TODO Auto-generated method stub
		return null;
	}

}
