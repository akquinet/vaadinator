/*
 * Copyright 2014 akquinet engineering GmbH
 *  
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.akquinet.engineering.vaadinator.timesheet.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

public class AbstractCouchDbService {

	public AbstractCouchDbService() {
		super();
		initProperties();
	}

	private void initProperties() {
		Properties properties = new Properties();
		try {
			properties.load(getClass().getResourceAsStream("/couchdb.properties"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		couchUrl = properties.getProperty("couchUrl");
	}

	private String couchUrl;

	protected JSONObject getCouch(String urlPart) throws IOException, MalformedURLException, JSONException {
		HttpURLConnection couchConn = null;
		BufferedReader couchRead = null;
		try {
			couchConn = (HttpURLConnection) (new URL(couchUrl + (couchUrl.endsWith("/") ? "" : "/") + urlPart)).openConnection();
			couchRead = new BufferedReader(new InputStreamReader(couchConn.getInputStream()));
			StringBuffer jsonBuf = new StringBuffer();
			String line = couchRead.readLine();
			while (line != null) {
				jsonBuf.append(line);
				line = couchRead.readLine();
			}
			JSONObject couchObj = new JSONObject(jsonBuf.toString());
			return couchObj;
		} finally {
			if (couchRead != null) {
				couchRead.close();
			}
			if (couchConn != null) {
				couchConn.disconnect();
			}
		}
	}

	protected JSONObject putCouch(String urlPart, JSONObject content) throws IOException, MalformedURLException, JSONException {
		HttpURLConnection couchConn = null;
		BufferedReader couchRead = null;
		OutputStreamWriter couchWrite = null;
		try {
			couchConn = (HttpURLConnection) (new URL(couchUrl + (couchUrl.endsWith("/") ? "" : "/") + urlPart)).openConnection();
			couchConn.setRequestMethod("PUT");
			couchConn.setDoInput(true);
			couchConn.setDoOutput(true);
			couchWrite = new OutputStreamWriter(couchConn.getOutputStream());
			couchWrite.write(content.toString());
			couchWrite.flush();
			couchRead = new BufferedReader(new InputStreamReader(couchConn.getInputStream()));
			StringBuffer jsonBuf = new StringBuffer();
			String line = couchRead.readLine();
			while (line != null) {
				jsonBuf.append(line);
				line = couchRead.readLine();
			}
			JSONObject couchObj = new JSONObject(jsonBuf.toString());
			return couchObj;
		} finally {
			if (couchRead != null) {
				couchRead.close();
			}
			if (couchWrite != null) {
				couchWrite.close();
			}
			if (couchConn != null) {
				couchConn.disconnect();
			}
		}
	}
}
