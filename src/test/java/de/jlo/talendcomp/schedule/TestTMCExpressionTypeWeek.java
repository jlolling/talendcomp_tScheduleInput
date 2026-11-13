package de.jlo.talendcomp.schedule;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.junit.Test;

import de.jlo.talendcomp.schedule.TMCExpression;

public class TestTMCExpressionTypeWeek {

	@Test
	public void testTMCExpressionInterval() throws Exception {
		String json = "	{\n"
				+ "		\"type\": \"WEEKLY\",\n"
				+ "		\"interval\": 1,\n"
				+ "		\"startDate\": \"2023-07-18\",\n"
				+ "		\"timeZone\": \"Europe/Berlin\",\n"
				+ "		\"atTimes\": {\n"
				+ "			\"type\": \"AT_INTERVALS\",\n"
				+ "			\"startTime\": \"01:03\",\n"
				+ "			\"endTime\": \"06:00\",\n"
				+ "			\"interval\": 10\n"
				+ "		},"
				+ "     \"atDays\": {\n"
				+ "    		\"type\": \"DAY_OF_WEEK\",\n"
				+ "    		\"days\": [\"MONDAY\", \"WEDNESDAY\"]\n"
				+ "     },\n"
				+ "		\"name\": \"daily_every_10min\"\n"
				+ "	}";
		TMCExpression ex = new TMCExpression(json);
		Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-11-13 02:15");
		Date actual = ex.getNextValidTimeAfter(start);
		Date expected = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-11-13 02:23");
		System.out.println(actual);
		assertEquals("Date not match", expected, actual);
	}

	@Test
	public void testTMCExpressionInterval2() throws Exception {
		String json = "	{\n"
				+ "		\"type\": \"WEEKLY\",\n"
				+ "		\"interval\": 1,\n"
				+ "		\"startDate\": \"2023-07-18\",\n"
				+ "		\"timeZone\": \"Europe/Berlin\",\n"
				+ "		\"atTimes\": {\n"
				+ "			\"type\": \"AT_INTERVALS\",\n"
				+ "			\"startTime\": \"01:03\",\n"
				+ "			\"endTime\": \"06:00\",\n"
				+ "			\"interval\": 10\n"
				+ "		},"
				+ "     \"atDays\": {\n"
				+ "    		\"type\": \"DAY_OF_WEEK\",\n"
				+ "    		\"days\": [\"MONDAY\", \"WEDNESDAY\"]\n"
				+ "     },\n"
				+ "		\"name\": \"daily_every_10min\"\n"
				+ "	}";
		TMCExpression ex = new TMCExpression(json);
		Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-11-13 02:13");
		Date actual = ex.getNextValidTimeAfter(start);
		Date expected = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-11-13 02:23");
		System.out.println(actual);
		assertEquals("Date not match", expected, actual);
	}

	@Test
	public void testTMCExpressionIntervalCheckNextDayBeforeList() throws Exception {
		String json = "	{\n"
				+ "		\"type\": \"WEEKLY\",\n"
				+ "		\"interval\": 1,\n"
				+ "		\"startDate\": \"2023-07-18\",\n"
				+ "		\"timeZone\": \"Europe/Berlin\",\n"
				+ "		\"atTimes\": {\n"
				+ "			\"type\": \"AT_INTERVALS\",\n"
				+ "			\"startTime\": \"01:03\",\n"
				+ "			\"endTime\": \"06:00\",\n"
				+ "			\"interval\": 10\n"
				+ "		},"
				+ "     \"atDays\": {\n"
				+ "    		\"type\": \"DAY_OF_WEEK\",\n"
				+ "    		\"days\": [\"MONDAY\", \"WEDNESDAY\"]\n"
				+ "     },\n"
				+ "		\"name\": \"daily_every_10min\"\n"
				+ "	}";
		TMCExpression ex = new TMCExpression(json);
		int next = ex.getNextWeekDay(Calendar.SUNDAY);
		assertEquals(Calendar.MONDAY, next);
	}
	
	@Test
	public void testTMCExpressionIntervalCheckNextDayInList() throws Exception {
		String json = "	{\n"
				+ "		\"type\": \"WEEKLY\",\n"
				+ "		\"interval\": 1,\n"
				+ "		\"startDate\": \"2023-07-18\",\n"
				+ "		\"timeZone\": \"Europe/Berlin\",\n"
				+ "		\"atTimes\": {\n"
				+ "			\"type\": \"AT_INTERVALS\",\n"
				+ "			\"startTime\": \"01:03\",\n"
				+ "			\"endTime\": \"06:00\",\n"
				+ "			\"interval\": 10\n"
				+ "		},"
				+ "     \"atDays\": {\n"
				+ "    		\"type\": \"DAY_OF_WEEK\",\n"
				+ "    		\"days\": [\"MONDAY\", \"WEDNESDAY\"]\n"
				+ "     },\n"
				+ "		\"name\": \"daily_every_10min\"\n"
				+ "	}";
		TMCExpression ex = new TMCExpression(json);
		int next = ex.getNextWeekDay(Calendar.MONDAY);
		assertEquals(Calendar.WEDNESDAY, next);
	}

	@Test
	public void testTMCExpressionIntervalCheckNextDayInList2() throws Exception {
		String json = "	{\n"
				+ "		\"type\": \"WEEKLY\",\n"
				+ "		\"interval\": 1,\n"
				+ "		\"startDate\": \"2023-07-18\",\n"
				+ "		\"timeZone\": \"Europe/Berlin\",\n"
				+ "		\"atTimes\": {\n"
				+ "			\"type\": \"AT_INTERVALS\",\n"
				+ "			\"startTime\": \"01:03\",\n"
				+ "			\"endTime\": \"06:00\",\n"
				+ "			\"interval\": 10\n"
				+ "		},"
				+ "     \"atDays\": {\n"
				+ "    		\"type\": \"DAY_OF_WEEK\",\n"
				+ "    		\"days\": [\"MONDAY\", \"WEDNESDAY\"]\n"
				+ "     },\n"
				+ "		\"name\": \"daily_every_10min\"\n"
				+ "	}";
		TMCExpression ex = new TMCExpression(json);
		int next = ex.getNextWeekDay(Calendar.TUESDAY);
		assertEquals(Calendar.WEDNESDAY, next);
	}

	@Test
	public void testTMCExpressionIntervalCheckNextDayAfterList() throws Exception {
		String json = "	{\n"
				+ "		\"type\": \"WEEKLY\",\n"
				+ "		\"interval\": 1,\n"
				+ "		\"startDate\": \"2023-07-18\",\n"
				+ "		\"timeZone\": \"Europe/Berlin\",\n"
				+ "		\"atTimes\": {\n"
				+ "			\"type\": \"AT_INTERVALS\",\n"
				+ "			\"startTime\": \"01:03\",\n"
				+ "			\"endTime\": \"06:00\",\n"
				+ "			\"interval\": 10\n"
				+ "		},"
				+ "     \"atDays\": {\n"
				+ "    		\"type\": \"DAY_OF_WEEK\",\n"
				+ "    		\"days\": [\"MONDAY\", \"WEDNESDAY\"]\n"
				+ "     },\n"
				+ "		\"name\": \"daily_every_10min\"\n"
				+ "	}";
		TMCExpression ex = new TMCExpression(json);
		int next = ex.getNextWeekDay(Calendar.WEDNESDAY);
		assertEquals(Calendar.MONDAY, next);
	}
	
	@Test
	public void testTMCExpressionIntervalCheckNextDayAfterList2() throws Exception {
		String json = "	{\n"
				+ "		\"type\": \"WEEKLY\",\n"
				+ "		\"interval\": 1,\n"
				+ "		\"startDate\": \"2023-07-18\",\n"
				+ "		\"timeZone\": \"Europe/Berlin\",\n"
				+ "		\"atTimes\": {\n"
				+ "			\"type\": \"AT_INTERVALS\",\n"
				+ "			\"startTime\": \"01:03\",\n"
				+ "			\"endTime\": \"06:00\",\n"
				+ "			\"interval\": 10\n"
				+ "		},"
				+ "     \"atDays\": {\n"
				+ "    		\"type\": \"DAY_OF_WEEK\",\n"
				+ "    		\"days\": [\"MONDAY\", \"WEDNESDAY\"]\n"
				+ "     },\n"
				+ "		\"name\": \"daily_every_10min\"\n"
				+ "	}";
		TMCExpression ex = new TMCExpression(json);
		int next = ex.getNextWeekDay(Calendar.THURSDAY);
		assertEquals(Calendar.MONDAY, next);
	}

	@Test
	public void testTMCExpressionIntervalCheckNextDayUnder() throws Exception {
		String json = "	{\n"
				+ "		\"type\": \"WEEKLY\",\n"
				+ "		\"interval\": 1,\n"
				+ "		\"startDate\": \"2023-07-18\",\n"
				+ "		\"timeZone\": \"Europe/Berlin\",\n"
				+ "		\"atTimes\": {\n"
				+ "			\"type\": \"AT_INTERVALS\",\n"
				+ "			\"startTime\": \"01:03\",\n"
				+ "			\"endTime\": \"06:00\",\n"
				+ "			\"interval\": 10\n"
				+ "		},"
				+ "     \"atDays\": {\n"
				+ "    		\"type\": \"DAY_OF_WEEK\",\n"
				+ "    		\"days\": [\"MONDAY\", \"WEDNESDAY\"]\n"
				+ "     },\n"
				+ "		\"name\": \"daily_every_10min\"\n"
				+ "	}";
		TMCExpression ex = new TMCExpression(json);
		Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-11-12 00:15");
		Date actual = ex.getNextValidTimeAfter(start);
		Date expected = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-11-13 01:03");
		System.out.println(actual);
		assertEquals("Date not match", expected, actual);
	}

	@Test
	public void testTMCExpressionIntervalCheckNextDayUnder2() throws Exception {
		String json = "	{\n"
				+ "		\"type\": \"WEEKLY\",\n"
				+ "		\"interval\": 1,\n"
				+ "		\"startDate\": \"2023-07-18\",\n"
				+ "		\"timeZone\": \"Europe/Berlin\",\n"
				+ "		\"atTimes\": {\n"
				+ "			\"type\": \"AT_INTERVALS\",\n"
				+ "			\"startTime\": \"01:03\",\n"
				+ "			\"endTime\": \"06:00\",\n"
				+ "			\"interval\": 10\n"
				+ "		},"
				+ "     \"atDays\": {\n"
				+ "    		\"type\": \"DAY_OF_WEEK\",\n"
				+ "    		\"days\": [\"MONDAY\", \"WEDNESDAY\"]\n"
				+ "     },\n"
				+ "		\"name\": \"daily_every_10min\"\n"
				+ "	}";
		TMCExpression ex = new TMCExpression(json);
		Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-11-12 01:03");
		Date actual = ex.getNextValidTimeAfter(start);
		Date expected = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-11-13 01:03");
		System.out.println(actual);
		assertEquals("Date not match", expected, actual);
	}
	
	@Test
	public void testTMCExpressionIntervalCheckNextDayMiddle() throws Exception {
		String json = "	{\n"
				+ "		\"type\": \"WEEKLY\",\n"
				+ "		\"interval\": 1,\n"
				+ "		\"startDate\": \"2023-07-18\",\n"
				+ "		\"timeZone\": \"Europe/Berlin\",\n"
				+ "		\"atTimes\": {\n"
				+ "			\"type\": \"AT_INTERVALS\",\n"
				+ "			\"startTime\": \"01:03\",\n"
				+ "			\"endTime\": \"06:00\",\n"
				+ "			\"interval\": 10\n"
				+ "		},"
				+ "     \"atDays\": {\n"
				+ "    		\"type\": \"DAY_OF_WEEK\",\n"
				+ "    		\"days\": [\"MONDAY\", \"WEDNESDAY\"]\n"
				+ "     },\n"
				+ "		\"name\": \"daily_every_10min\"\n"
				+ "	}";
		TMCExpression ex = new TMCExpression(json);
		Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-11-13 07:15");
		Date actual = ex.getNextValidTimeAfter(start);
		Date expected = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-11-15 01:03");
		System.out.println(actual);
		assertEquals("Date not match", expected, actual);
	}

	@Test
	public void testTMCExpressionIntervalCheckNextDayOverrun() throws Exception {
		String json = "	{\n"
				+ "		\"type\": \"WEEKLY\",\n"
				+ "		\"interval\": 1,\n"
				+ "		\"startDate\": \"2023-07-18\",\n"
				+ "		\"timeZone\": \"Europe/Berlin\",\n"
				+ "		\"atTimes\": {\n"
				+ "			\"type\": \"AT_INTERVALS\",\n"
				+ "			\"startTime\": \"01:03\",\n"
				+ "			\"endTime\": \"06:00\",\n"
				+ "			\"interval\": 10\n"
				+ "		},"
				+ "     \"atDays\": {\n"
				+ "    		\"type\": \"DAY_OF_WEEK\",\n"
				+ "    		\"days\": [\"MONDAY\", \"WEDNESDAY\"]\n"
				+ "     },\n"
				+ "		\"name\": \"daily_every_10min\"\n"
				+ "	}";
		TMCExpression ex = new TMCExpression(json);
		Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-11-15 07:15");
		Date actual = ex.getNextValidTimeAfter(start);
		Date expected = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-11-20 01:03");
		System.out.println(actual);
		assertEquals("Date not match", expected, actual);
	}

	@Test
	public void testTMCExpressionIntervalCheckNextDayOverrun2() throws Exception {
		String json = "	{\n"
				+ "		\"type\": \"WEEKLY\",\n"
				+ "		\"interval\": 2,\n"
				+ "		\"startDate\": \"2023-11-01\",\n"
				+ "		\"timeZone\": \"Europe/Berlin\",\n"
				+ "		\"atTimes\": {\n"
				+ "			\"type\": \"AT_INTERVALS\",\n"
				+ "			\"startTime\": \"01:03\",\n"
				+ "			\"endTime\": \"06:00\",\n"
				+ "			\"interval\": 10\n"
				+ "		},"
				+ "     \"atDays\": {\n"
				+ "    		\"type\": \"DAY_OF_WEEK\",\n"
				+ "    		\"days\": [\"MONDAY\", \"WEDNESDAY\"]\n"
				+ "     },\n"
				+ "		\"name\": \"daily_every_10min\"\n"
				+ "	}";
		TMCExpression ex = new TMCExpression(json);
		Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-11-15 07:15");
		Date actual = ex.getNextValidTimeAfter(start);
		Date expected = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-11-27 01:03");
		System.out.println(actual);
		assertEquals("Date not match", expected, actual);
	}

	@Test
	public void testTMCExpressionIntervalCheckNextDayOverrunYear() throws Exception {
		String json = "	{\n"
				+ "		\"type\": \"WEEKLY\",\n"
				+ "		\"interval\": 1,\n"
				+ "		\"startDate\": \"2023-11-01\",\n"
				+ "		\"timeZone\": \"Europe/Berlin\",\n"
				+ "		\"atTimes\": {\n"
				+ "			\"type\": \"AT_INTERVALS\",\n"
				+ "			\"startTime\": \"01:03\",\n"
				+ "			\"endTime\": \"06:00\",\n"
				+ "			\"interval\": 10\n"
				+ "		},"
				+ "     \"atDays\": {\n"
				+ "    		\"type\": \"DAY_OF_WEEK\",\n"
				+ "    		\"days\": [\"MONDAY\", \"WEDNESDAY\"]\n"
				+ "     },\n"
				+ "		\"name\": \"daily_every_10min\"\n"
				+ "	}";
		TMCExpression ex = new TMCExpression(json);
		Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-12-27 07:15");
		Date actual = ex.getNextValidTimeAfter(start);
		Date expected = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2024-01-01 01:03");
		System.out.println(actual);
		assertEquals("Date not match", expected, actual);
	}

	@Test
	public void testTMCExpressionIntervalCheckNextDayOverrun2Year() throws Exception {
		String json = "	{\n"
				+ "		\"type\": \"WEEKLY\",\n"
				+ "		\"interval\": 2,\n"
				+ "		\"startDate\": \"2023-11-01\",\n"
				+ "		\"timeZone\": \"Europe/Berlin\",\n"
				+ "		\"atTimes\": {\n"
				+ "			\"type\": \"AT_INTERVALS\",\n"
				+ "			\"startTime\": \"01:03\",\n"
				+ "			\"endTime\": \"06:00\",\n"
				+ "			\"interval\": 10\n"
				+ "		},"
				+ "     \"atDays\": {\n"
				+ "    		\"type\": \"DAY_OF_WEEK\",\n"
				+ "    		\"days\": [\"MONDAY\", \"WEDNESDAY\"]\n"
				+ "     },\n"
				+ "		\"name\": \"daily_every_10min\"\n"
				+ "	}";
		TMCExpression ex = new TMCExpression(json);
		Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2023-12-27 07:15");
		Date actual = ex.getNextValidTimeAfter(start);
		Date expected = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2024-01-08 01:03");
		System.out.println(actual);
		assertEquals("Date not match", expected, actual);
	}

}
