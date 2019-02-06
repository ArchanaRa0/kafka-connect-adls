 #!/usr/bin/env bash
 export KAFKA_LOG4J_OPTS="-Dlog4j.configuration=file:///../../kafka-connect-adls/config/log4j-adls.properties"

 export JAVA_HOME=/usr/java/jdk1.8.0_151
 export CLASSPATH="$(find /../../kafka-connect-adls/lib/azure-data-lake-store-sdk-2.3.1.jar -type f -name '*.jar' | tr '\n' ':')"

 bin/connect-distributed -daemon etc/kafka/connect-distributed.properties