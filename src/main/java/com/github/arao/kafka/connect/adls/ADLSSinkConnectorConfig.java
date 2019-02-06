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

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigDef.Importance;

import java.util.Map;

public class ADLSSinkConnectorConfig extends AbstractConfig {

	public static final String ACCOUNT_FQDN_CONF = "account.fqdn";
	public static final String CLIENT_ID_CONF = "client.id";
	public static final String ACCOUNT_TOKEN_ENDPOINT_CONF = "auth.token.endpoint";
	public static final String CLIENT_SECRET_CONF = "client.key";
	public static final String ADLS_DIR_PATH_CONF = "adls.dir.path";
	public static final String FILE_PREFIX = "file.prefix";
	
	public final String accountFQDN;
	public final String clientId;
	public final String authTokenEndpoint;
	public final String clientKey;
	public final String dirPath;
	public final String filePrefix;
	
	public ADLSSinkConnectorConfig(Map<String, String> originals) {
		super(config(), originals);
		this.accountFQDN = this.getString(ACCOUNT_FQDN_CONF);
		this.clientId = this.getString(CLIENT_ID_CONF);
		this.authTokenEndpoint = this.getString(ACCOUNT_TOKEN_ENDPOINT_CONF);
		this.clientKey = this.getString(CLIENT_SECRET_CONF); // this.getPassword(CLIENT_SECRET_CONF);
		this.dirPath = this.getString(ADLS_DIR_PATH_CONF);
		this.filePrefix = this.getString(FILE_PREFIX);

	}

	public static ConfigDef config() {
		return new ConfigDef().define(ACCOUNT_FQDN_CONF, Type.STRING, Importance.HIGH, "")
				.define(CLIENT_ID_CONF, Type.STRING, Importance.HIGH, "")
				.define(ACCOUNT_TOKEN_ENDPOINT_CONF, Type.STRING, Importance.HIGH, "")
				.define(CLIENT_SECRET_CONF, Type.STRING, Importance.HIGH, "")
				.define(ADLS_DIR_PATH_CONF, Type.STRING, Importance.HIGH, "")
				.define(FILE_PREFIX, Type.STRING, "", Importance.HIGH, "");
	}
}
