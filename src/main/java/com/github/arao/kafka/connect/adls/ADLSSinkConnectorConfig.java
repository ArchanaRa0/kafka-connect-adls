package com.github.arao.kafka.connect.adls;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigDef.Importance;
//import com.github.jcustenborder.kafka.connect.utils.config.ConfigKeyBuilder;

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
