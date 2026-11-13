package de.jlo.talendcomp.schedule;

import static org.junit.Assert.*;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;

import de.jlo.talendcomp.schedule.TMCExpression;
import de.jlo.talendcomp.schedule.TMCExpression.Time;

public class TestTMCExpressionTypeDay {

	@Test
	public void testTMCExpressionInterval() throws Exception {
		String json = "	{\r\n"
				+ "		\"type\": \"DAILY\",\r\n"
				+ "		\"interval\": 1,\r\n"
				+ "		\"startDate\": \"2023-07-18\",\r\n"
				+ "		\"timeZone\": \"Europe/Berlin\",\r\n"
				+ "		\"atTimes\": {\r\n"
				+ "			\"type\": \"AT_INTERVALS\",\r\n"
				+ "			\"startTime\": \"01:03\",\r\n"
				+ "			\"endTime\": \"06:00\",\r\n"
				+ "			\"interval\": 10\r\n"
				+ "		},\r\n"
				+ "		\"name\": \"daily_every_10min\"\r\n"
				+ "	}";
		TMCExpression ex = new TMCExpression(json);
		int expectedCount = 30;
		List<Time> result = ex.getTimes();
		boolean first = true;
		Time ft = null;
		for (Time t : result) {
			if (first) {
				first = false;
				ft = t;
			}
			System.out.println(t);
		}
		assertEquals(expectedCount, result.size());
		assertTrue(ft.first);
	}

	@Test
	public void testTMCExpressionIntervalCheckNextTimeMiddle() throws Exception {
		String json = "	{\r\n"
				+ "		\"type\": \"DAILY\",\r\n"
				+ "		\"interval\": 1,\r\n"
				+ "		\"startDate\": \"2023-07-18\",\r\n"
				+ "		\"timeZone\": \"Europe/Berlin\",\r\n"
				+ "		\"atTimes\": {\r\n"
				+ "			\"type\": \"AT_INTERVALS\",\r\n"
				+ "			\"startTime\": \"01:03\",\r\n"
				+ "			\"endTime\": \"06:00\",\r\n"
				+ "			\"interval\": 10\r\n"
				+ "		},\r\n"
				+ "		\"name\": \"daily_every_10min\"\r\n"
				+ "	}";
		TMCExpression ex = new TMCExpression(json);
		int hour = 4;
		int minute = 5;
		Time expected = new Time();
		expected.hour = 4;
		expected.minute = 13;
		Time actual = ex.getNextTime(hour, minute);
		System.out.println(actual);
		assertEquals("Time not match", expected, actual);
	}

	@Test
	public void testTMCExpressionIntervalCheckNextTimeOverrun() throws Exception {
		String json = "	{\r\n"
				+ "		\"type\": \"DAILY\",\r\n"
				+ "		\"interval\": 1,\r\n"
				+ "		\"startDate\": \"2023-07-18\",\r\n"
				+ "		\"timeZone\": \"Europe/Berlin\",\r\n"
				+ "		\"atTimes\": {\r\n"
				+ "			\"type\": \"AT_INTERVALS\",\r\n"
				+ "			\"startTime\": \"01:03\",\r\n"
				+ "			\"endTime\": \"06:00\",\r\n"
				+ "			\"interval\": 10\r\n"
				+ "		},\r\n"
				+ "		\"name\": \"daily_every_10min\"\r\n"
				+ "	}";
		TMCExpression ex = new TMCExpression(json);
		int hour = 7;
		int minute = 0;
		Time expected = new Time();
		expected.hour = 1;
		expected.minute = 3;
		Time actual = ex.getNextTime(hour, minute);
		System.out.println(actual);
		assertEquals("Time not match", expected, actual);
	}

	@Test
	public void testTMCExpressionIntervalCheckNextTimeBefore() throws Exception {
		String json = "	{\r\n"
				+ "		\"type\": \"DAILY\",\r\n"
				+ "		\"interval\": 1,\r\n"
				+ "		\"startDate\": \"2023-07-18\",\r\n"
				+ "		\"timeZone\": \"Europe/Berlin\",\r\n"
				+ "		\"atTimes\": {\r\n"
				+ "			\"type\": \"AT_INTERVALS\",\r\n"
				+ "			\"startTime\": \"01:03\",\r\n"
				+ "			\"endTime\": \"06:00\",\r\n"
				+ "			\"interval\": 10\r\n"
				+ "		},\r\n"
				+ "		\"name\": \"daily_every_10min\"\r\n"
				+ "	}";
		TMCExpression ex = new TMCExpression(json);
		int hour = 0;
		int minute = 45;
		Time expected = new Time();
		expected.hour = 1;
		expected.minute = 3;
		Time actual = ex.getNextTime(hour, minute);
		System.out.println(actual);
		assertEquals("Time not match", expected, actual);
	}

	@Test
	public void testTMCExpressionIntervalCheckNextDatetime() throws Exception {
		String json = "	{\r\n"
				+ "		\"type\": \"DAILY\",\r\n"
				+ "		\"interval\": 1,\r\n"
				+ "		\"startDate\": \"2023-08-01\",\r\n"
				+ "		\"timeZone\": \"Europe/Berlin\",\r\n"
				+ "		\"atTimes\": {\r\n"
				+ "			\"type\": \"AT_INTERVALS\",\r\n"
				+ "			\"startTime\": \"01:03\",\r\n"
				+ "			\"endTime\": \"06:00\",\r\n"
				+ "			\"interval\": 10\r\n"
				+ "		},\r\n"
				+ "		\"name\": \"daily_every_10min\"\r\n"
				+ "	}";
		TMCExpression ex = new TMCExpression(json);
		Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-08-10 02:04");
		Date actual = ex.getNextValidTimeAfter(start);
		Date expected = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-08-10 02:13");
		System.out.println(actual);
		assertEquals("Date not match", expected, actual);
	}

	@Test
	public void testTMCExpressionIntervalCheckNextDatetime2() throws Exception {
		String json = "	{\r\n"
				+ "		\"type\": \"DAILY\",\r\n"
				+ "		\"interval\": 1,\r\n"
				+ "		\"startDate\": \"2023-08-01\",\r\n"
				+ "		\"timeZone\": \"Europe/Berlin\",\r\n"
				+ "		\"atTimes\": {\r\n"
				+ "			\"type\": \"AT_INTERVALS\",\r\n"
				+ "			\"startTime\": \"01:03\",\r\n"
				+ "			\"endTime\": \"06:00\",\r\n"
				+ "			\"interval\": 10\r\n"
				+ "		},\r\n"
				+ "		\"name\": \"daily_every_10min\"\r\n"
				+ "	}";
		TMCExpression ex = new TMCExpression(json);
		Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-08-10 01:03");
		Date actual = ex.getNextValidTimeAfter(start);
		Date expected = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-08-10 01:13");
		System.out.println(actual);
		assertEquals("Date not match", expected, actual);
	}

	@Test
	public void testTMCExpressionIntervalCheckNextDatetimeOverrun() throws Exception {
		String json = "	{\r\n"
				+ "		\"type\": \"DAILY\",\r\n"
				+ "		\"interval\": 1,\r\n"
				+ "		\"startDate\": \"2023-08-01\",\r\n"
				+ "		\"timeZone\": \"Europe/Berlin\",\r\n"
				+ "		\"atTimes\": {\r\n"
				+ "			\"type\": \"AT_INTERVALS\",\r\n"
				+ "			\"startTime\": \"01:03\",\r\n"
				+ "			\"endTime\": \"06:00\",\r\n"
				+ "			\"interval\": 10\r\n"
				+ "		},\r\n"
				+ "		\"name\": \"daily_every_10min\"\r\n"
				+ "	}";
		TMCExpression ex = new TMCExpression(json);
		Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-08-10 07:00");
		Date actual = ex.getNextValidTimeAfter(start);
		Date expected = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-08-11 01:03");
		System.out.println(actual);
		assertEquals("Date not match", expected, actual);
	}

	@Test
	public void testTMCExpressionIntervalCheckNextDatetimeOverrun2() throws Exception {
		String json = "	{\r\n"
				+ "		\"type\": \"DAILY\",\r\n"
				+ "		\"interval\": 2,\r\n"
				+ "		\"startDate\": \"2023-08-01\",\r\n"
				+ "		\"timeZone\": \"Europe/Berlin\",\r\n"
				+ "		\"atTimes\": {\r\n"
				+ "			\"type\": \"AT_INTERVALS\",\r\n"
				+ "			\"startTime\": \"01:03\",\r\n"
				+ "			\"endTime\": \"06:00\",\r\n"
				+ "			\"interval\": 10\r\n"
				+ "		},\r\n"
				+ "		\"name\": \"daily_every_10min\"\r\n"
				+ "	}";
		TMCExpression ex = new TMCExpression(json);
		Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-08-10 07:00");
		Date actual = ex.getNextValidTimeAfter(start);
		Date expected = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-08-12 01:03");
		System.out.println(actual);
		assertEquals("Date not match", expected, actual);
	}

}
