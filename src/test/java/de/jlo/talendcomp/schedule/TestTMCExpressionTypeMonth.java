package de.jlo.talendcomp.schedule;

import static org.junit.Assert.*;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;

import de.jlo.talendcomp.schedule.TMCExpression;
import de.jlo.talendcomp.schedule.TMCExpression.Time;

public class TestTMCExpressionTypeMonth {

	@Test
	public void testTMCExpressionInterval() throws Exception {
		String json = "	{\r\n"
				+ "		\"type\": \"MONTHLY\",\r\n"
				+ "		\"interval\": 1,\r\n"
				+ "		\"startDate\": \"2023-07-18\",\r\n"
				+ "		\"timeZone\": \"Europe/Berlin\",\r\n"
				+ "		\"atTimes\": {\r\n"
				+ "			\"type\": \"AT_INTERVALS\",\r\n"
				+ "			\"startTime\": \"01:03\",\r\n"
				+ "			\"endTime\": \"06:00\",\r\n"
				+ "			\"interval\": 10\r\n"
				+ "		},\r\n"
				+ "     \"atDays\": {\n"
				+ "    		\"type\": \"DAY_OF_MONTH\",\n"
				+ "    		\"day\": 3\n"
				+ "     },\n"
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
	public void testTMCExpressionIntervalCheckNextDayBefore() throws Exception {
		String json = "	{\r\n"
				+ "		\"type\": \"MONTHLY\",\r\n"
				+ "		\"interval\": 1,\r\n"
				+ "		\"startDate\": \"2023-07-18\",\r\n"
				+ "		\"timeZone\": \"Europe/Berlin\",\r\n"
				+ "		\"atTimes\": {\r\n"
				+ "			\"type\": \"AT_INTERVALS\",\r\n"
				+ "			\"startTime\": \"01:03\",\r\n"
				+ "			\"endTime\": \"06:00\",\r\n"
				+ "			\"interval\": 10\r\n"
				+ "		},\r\n"
				+ "     \"atDays\": {\n"
				+ "    		\"type\": \"DAY_OF_MONTH\",\n"
				+ "    		\"day\": 3\n"
				+ "     },\n"
				+ "		\"name\": \"daily_every_10min\"\r\n"
				+ "	}";
		TMCExpression ex = new TMCExpression(json);
		Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-08-01 01:03");
		Date actual = ex.getNextValidTimeAfter(start);
		Date expected = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-08-03 01:03");
		System.out.println(actual);
		assertEquals("Date not match", expected, actual);
	}

	@Test
	public void testTMCExpressionIntervalCheckNextDayBefore2() throws Exception {
		String json = "	{\r\n"
				+ "		\"type\": \"MONTHLY\",\r\n"
				+ "		\"interval\": 1,\r\n"
				+ "		\"startDate\": \"2023-07-18\",\r\n"
				+ "		\"timeZone\": \"Europe/Berlin\",\r\n"
				+ "		\"atTimes\": {\r\n"
				+ "			\"type\": \"AT_INTERVALS\",\r\n"
				+ "			\"startTime\": \"01:03\",\r\n"
				+ "			\"endTime\": \"06:00\",\r\n"
				+ "			\"interval\": 10\r\n"
				+ "		},\r\n"
				+ "     \"atDays\": {\n"
				+ "    		\"type\": \"DAY_OF_MONTH\",\n"
				+ "    		\"day\": 3\n"
				+ "     },\n"
				+ "		\"name\": \"daily_every_10min\"\r\n"
				+ "	}";
		TMCExpression ex = new TMCExpression(json);
		Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-08-03 01:03");
		Date actual = ex.getNextValidTimeAfter(start);
		Date expected = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-08-03 01:13");
		System.out.println(actual);
		assertEquals("Date not match", expected, actual);
	}

	@Test
	public void testTMCExpressionIntervalCheckNextDatetimeOverrun() throws Exception {
		String json = "	{\r\n"
				+ "		\"type\": \"MONTHLY\",\r\n"
				+ "		\"interval\": 1,\r\n"
				+ "		\"startDate\": \"2023-07-18\",\r\n"
				+ "		\"timeZone\": \"Europe/Berlin\",\r\n"
				+ "		\"atTimes\": {\r\n"
				+ "			\"type\": \"AT_INTERVALS\",\r\n"
				+ "			\"startTime\": \"01:03\",\r\n"
				+ "			\"endTime\": \"06:00\",\r\n"
				+ "			\"interval\": 10\r\n"
				+ "		},\r\n"
				+ "     \"atDays\": {\n"
				+ "    		\"type\": \"DAY_OF_MONTH\",\n"
				+ "    		\"day\": 3\n"
				+ "     },\n"
				+ "		\"name\": \"daily_every_10min\"\r\n"
				+ "	}";
		TMCExpression ex = new TMCExpression(json);
		Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-08-03 07:00");
		Date actual = ex.getNextValidTimeAfter(start);
		Date expected = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-09-03 01:03");
		System.out.println(actual);
		assertEquals("Date not match", expected, actual);
	}

}