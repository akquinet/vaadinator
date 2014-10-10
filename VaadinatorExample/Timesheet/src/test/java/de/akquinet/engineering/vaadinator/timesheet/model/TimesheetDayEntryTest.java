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
package de.akquinet.engineering.vaadinator.timesheet.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TimesheetDayEntryTest {

	TimesheetDayEntry entry = new TimesheetDayEntry();

	@Test
	public void testGetEffectiveDurationHours() {
		entry.setHours("01:30");
		assertEquals(1.5f, entry.getEffectiveDurationHours(), Float.MIN_NORMAL);
	}

	@Test
	public void testGetEffectiveDurationHoursAlt() {
		entry.setHours("1:30");
		assertEquals(1.5f, entry.getEffectiveDurationHours(), Float.MIN_NORMAL);
	}

	@Test
	public void testGetEffectiveDurationMinimum() {
		entry.setHours("00:01");
		assertEquals(1.0f / 60.0f, entry.getEffectiveDurationHours(), Float.MIN_NORMAL);
	}

	@Test
	public void testGetEffectiveDurationMinimumAlt() {
		entry.setHours("0:01");
		assertEquals(1.0f / 60.0f, entry.getEffectiveDurationHours(), Float.MIN_NORMAL);
	}

	@Test
	public void testGetEffectiveDurationHoursZero() {
		entry.setHours("00:00");
		assertEquals(0.0f, entry.getEffectiveDurationHours(), Float.MIN_NORMAL);
	}

	@Test
	public void testGetEffectiveDurationHoursNull() {
		entry.setHours(null);
		assertEquals(0.0f, entry.getEffectiveDurationHours(), Float.MIN_NORMAL);
	}

	@Test
	public void testGetEffectiveDurationHoursNonsense() {
		entry.setHours("bla");
		assertEquals(0.0f, entry.getEffectiveDurationHours(), Float.MIN_NORMAL);
	}

}
