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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.azure.datalake.store.ADLStoreClient;
import com.microsoft.azure.datalake.store.Core;
import com.microsoft.azure.datalake.store.OperationResponse;
import com.microsoft.azure.datalake.store.RequestOptions;
import com.microsoft.azure.datalake.store.oauth2.AccessTokenProvider;
import com.microsoft.azure.datalake.store.oauth2.ClientCredsTokenProvider;
import com.microsoft.azure.datalake.store.retrypolicies.NonIdempotentRetryPolicy;

public class ADLSSinkTask extends SinkTask {
	
	private static Logger log = LoggerFactory.getLogger(ADLSSinkTask.class);

	ADLSSinkConnectorConfig config;
	AccessTokenProvider provider;
	static ADLStoreClient client;

	static String filePrefix;

	static String dirPath;

	@Override
	public void start(Map<String, String> settings) {
		this.config = new ADLSSinkConnectorConfig(settings);

		log.info("Starting...");
		this.provider = new ClientCredsTokenProvider(this.config.authTokenEndpoint, this.config.clientId,
				this.config.clientKey);
		ADLSSinkTask.client = ADLStoreClient.createClient(this.config.accountFQDN, provider);

		ADLSSinkTask.filePrefix = this.config.filePrefix + "_";

		ADLSSinkTask.dirPath = this.config.dirPath;
		if (!dirPath.endsWith("/"))
			ADLSSinkTask.dirPath += "/";

	}

	@Override
	public void put(Collection<SinkRecord> collection) {

		if (collection.isEmpty()) {
			log.trace("No records in collection.");
			return;
		}

		String key = collection.iterator().next().topic();

		String curr_date = LocalDate.now().toString();
		final int curr_hour = LocalDateTime.now().getHour();

		final String filename = ADLSSinkTask.dirPath + key + "/" + curr_date + "/" + curr_hour + "/" + filePrefix
				+ curr_hour + ".txt";

		try {
			writeToADLS(key, collection, filename);
		} catch (ConnectException ex) {
			log.warn(" Exception: {} Message: {}", ex.getClass().getName(), ex.getMessage());
		} catch (WakeupException e) {
			log.info("Kafka Consumer wakeup exception");
			log.debug(" Exception: {} Message: {}", e.getClass().getName(), e.getMessage());
		}

	}

	static void writeToADLS(String topic, Collection<SinkRecord> records, String filename) {

		log.debug("Calling action to ADLS...");

		if (records.isEmpty()) {
			log.trace("No records in collection.");
			return;
		}

		try {
			log.trace("Posting {} message(s) to ADLS Gen1", records.size());

			byte[] buffer = null;

			try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
				buffer = writeTo(outputStream, records);
				log.trace("Posting\n{}", new String(buffer, "UTF-8"));
			} catch (IOException ex) {
				log.error(" Exception: {} Message: {}", ex.getClass().getName(), ex.getMessage());
			} catch (Exception ex) {
				log.error(" Exception: {} Message: {}", ex.getClass().getName(), ex.getMessage());
			}

			log.info("Writing to filename {}", filename);

			if (client == null)
				log.error("ADLS Client is Not Instantiated");

			if (buffer != null) {
				try {
					RequestOptions opts = new RequestOptions();
					opts.retryPolicy = new NonIdempotentRetryPolicy();
					OperationResponse resp = new OperationResponse();

					log.trace("Writing buffer of length {} to ADLS", buffer.length);

					Core.concurrentAppend(filename, buffer, 0, buffer.length, true, client, opts, resp);

					if (!resp.successful) {
						log.error(resp.remoteExceptionMessage);
						log.error(resp.exceptionHistory);
						log.error(resp.ex.getMessage());
						log.error("HTTP Response Code {}", resp.httpResponseCode);
					} else {
						log.trace("Successfully wrote to ADLS file!");
					}
				} catch (ConnectException ex) {
					log.warn(" Exception: {} Message: {}", ex.getClass().getName(), ex.getMessage());
				} catch (WakeupException e) {
					log.info("Kafka Consumer wakeup exception");
					log.debug(" Exception: {} Message: {}", e.getClass().getName(), e.getMessage());
				} catch (Exception ex) {
					log.error(" Exception: {} Message: {}", ex.getClass().getName(), ex.getMessage());
				}
			} else
				log.info("Empty Buffer - something went wrong with creating buffer for the sinkrecords");

		} finally {

			filename = "";
		}
	}

	static byte[] writeTo(ByteArrayOutputStream outputStream, Collection<SinkRecord> sinkRecords) throws IOException {

		for (SinkRecord sinkRecord : sinkRecords) {

			outputStream.write(sinkRecord.value().toString().getBytes());
			outputStream.write(System.lineSeparator().getBytes());
		}

		outputStream.flush();
		return outputStream.toByteArray();
	}

	@Override
	public void flush(Map<TopicPartition, OffsetAndMetadata> map) {

	}

	public String version() {
		return null;
	}

	@Override
	public void close(Collection<TopicPartition> partitions) {

	}

	@Override
	public void stop() throws ConnectException {
		log.info("Stopping Task");

		synchronized (this) {
			this.notifyAll();
		}

	}
}
