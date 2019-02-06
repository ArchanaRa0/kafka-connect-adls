# Overview

This Kafka Connect app is a Sink Connector for reading data from a Kafka topic and writing concurrently to Azure Data Lake Store Gen 1.

# Pre-requisties

This connector works using Azure Service Principal authentication therefore requires you to have an active Azure App Registration done. 
From the registered App, you can get the App ID, Client Secret key and from the Azure Active Directory properties, you can get the Directory ID.
These properties are required for the Sink config.


# Configuration

## ADLSSinkConnector

The connector will read from a topic and write to the specified ADLS Gen 1 Directory. The default file rotation pattern is hourly.
The directory structure is: //<specified dir path>//<topic name>//<current date>//<curr hour>//file 
This writing pattern can be easily extended as needed. 

```properties
name=adls-sink-connector1
tasks.max=3
connector.class=com.github.arao.kafka.connect.adls.ADLSSinkConnector

# Set these required values
adls.dir.path= <ADLS directory to place the files, directory will be created if not pre-existing>
file.prefix= <file name prefix - friendly name to define the data files>
auth.token.endpoint= <the login auth url - https://login.microsoftonline.com/<directory ID/tenant id>/oauth2/token>
account.fqdn= <the ADLS stores' fully qualified name>
client.id= <azure app id>
client.key= <azure app client secret key>
topic= <topic to be used>
```


# Building on you workstation

```
git clone git@github.com:ArchanaRa0/kafka-connect-adls.git
cd kafka-connect-adls
mvn clean package
```

# Starting Connect Distributed with ADLS Sink Connector plugins 

```bash
./bin/start.sh
```
## Starting a ADLS Sink

cd config
curl -X POST -d @adls-sink-config.json  http://localhost:port/connectors --header "content-Type:application/json"