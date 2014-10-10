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
package de.akquinet.engineering.vaadinator.example.crmws.model;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.engineering.vaadinator.example.crmws.dto.HistoryDto;

public class HistoryMapperTest {

	@SuppressWarnings("deprecation")
	Date dateRef = new Date(1970, 0, 22);
	History historyRef = new History();

	HistoryMapper mapper;

	@Before
	public void setUp() {
		mapper = new HistoryMapper();
		historyRef.setId(33);
		historyRef.setText("mors, mors");
		historyRef.setTimestamp(dateRef);
	}

	@Test
	public void testMapContactInclHistory() {
		HistoryDto dto = new HistoryDto();
		mapper.mapContactInclHistory(historyRef, dto);
		assertEquals(33, dto.getId());
		assertEquals("mors, mors", dto.getText());
		assertEquals(dateRef, dto.getTimestamp());
	}

	@Test
	public void testMapHistoryOnly() {
		HistoryDto dto = new HistoryDto();
		mapper.mapContactInclHistory(historyRef, dto);
		assertEquals(33, dto.getId());
		assertEquals("mors, mors", dto.getText());
		assertEquals(dateRef, dto.getTimestamp());
	}

	@Test
	public void testMapHistoryOnlyInbound() {
		// prep work (secured by test above)
		HistoryDto dto = new HistoryDto();
		mapper.mapContactInclHistory(historyRef, dto);
		// actual test
		History history = new History();
		mapper.mapHistoryOnlyInbound(dto, history);
		assertEquals(0 /* !! */, history.getId());
		assertEquals("mors, mors", history.getText());
		assertEquals(dateRef, history.getTimestamp());
	}

}
