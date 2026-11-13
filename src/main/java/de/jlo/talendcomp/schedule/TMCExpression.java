package de.jlo.talendcomp.schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TMCExpression {

	private Date startDate = null;
	private ObjectNode oneTriggerNode = null;
	private final static ObjectMapper objectMapper = new ObjectMapper();
	private String type = null;
	private ObjectNode atTimesNode = null;
	private ObjectNode atDaysNode = null;
	private TimeZone timeZone = null;
	private List<Time> listTimes = null;
	private List<Integer> listWeekDayIndex = null;
	private int dayOfMonth = 0;
	
	public TMCExpression(String triggerStr) throws Exception {
		if (triggerStr == null || triggerStr.trim().isEmpty()) {
			throw new IllegalArgumentException("triggerStr cannot be null or empty");
		}
		try {
			oneTriggerNode = (ObjectNode) objectMapper.readTree(triggerStr);
		} catch (Exception e) {
			throw new Exception("Error parsing trigger json. Error: " + e.getMessage() + "\njson: " + triggerStr, e);
		}
		this.type = getMandatoryValueString(oneTriggerNode, "type");
		this.atTimesNode = (ObjectNode) oneTriggerNode.get("atTimes");
		this.atDaysNode = (ObjectNode) oneTriggerNode.get("atDays");
		this.startDate = getMandatoryValueDate(oneTriggerNode, "startDate");
		JsonNode tzNode = oneTriggerNode.get("timeZone");
		if (tzNode != null && tzNode.isNull() == false) {
			String tz = tzNode.asText();
			timeZone = TimeZone.getTimeZone(tz);
		}
		if (type.equals("WEEKLY")) {
			configureDayIndexForWeek();
		} else if (type.equals("MONTHLY")) {
			configureDayIndexForMonth();
		}
		configureTimeList();
	}
	
	private void configureDayIndexForWeek() throws Exception {
		if (atDaysNode == null || atDaysNode.isNull()) {
			throw new Exception("Object atDays is mandatory for the type WEEKLY");
		}
		ArrayNode node = (ArrayNode) atDaysNode.get("days");
		listWeekDayIndex = new ArrayList<>();
		for (JsonNode vn : node) {
			String weekDayName = vn.textValue();
			if ("MONDAY".equals(weekDayName)) {
				listWeekDayIndex.add(Calendar.MONDAY);
			} else if ("TUESDAY".equals(weekDayName)) {
				listWeekDayIndex.add(Calendar.TUESDAY);
			} else if ("WEDNESDAY".equals(weekDayName)) {
				listWeekDayIndex.add(Calendar.WEDNESDAY);
			} else if ("THURSDAY".equals(weekDayName)) {
				listWeekDayIndex.add(Calendar.THURSDAY);
			} else if ("FRIDAY".equals(weekDayName)) {
				listWeekDayIndex.add(Calendar.FRIDAY);
			} else if ("SATURDAY".equals(weekDayName)) {
				listWeekDayIndex.add(Calendar.SATURDAY);
			} else if ("SUNDAY".equals(weekDayName)) {
				listWeekDayIndex.add(Calendar.SUNDAY);
			} else {
				throw new Exception("Unknown or missing day of week: " + weekDayName);
			}
		}
		if (listWeekDayIndex.size() == 0) {
			throw new Exception("days attribute is empty. ASt least one week day is needed");
		}
		// now sort the list according the week days
		Collections.sort(listWeekDayIndex);
	}
	
	private void configureDayIndexForMonth() throws Exception {
		if (atDaysNode == null || atDaysNode.isNull()) {
			throw new Exception("Object atDays is mandatory for the type MONTHLY");
		}
		dayOfMonth = getMandatoryValueInt(atDaysNode, "day");
	}

	private void configureTimeList() throws Exception {
		listTimes = new ArrayList<>();
		if (atTimesNode != null && atTimesNode.isNull() == false) {
			String atTimeType = getMandatoryValueString(atTimesNode, "type");
			if (atTimeType.equals("AT_TIME")) {
				String timeStr = getMandatoryValueString(atTimesNode, "time");
				listTimes.add(getTime(timeStr));
			} else if (atTimeType.equals("AT_SPECIFIC_TIMES")) {
				JsonNode timesNode = atTimesNode.get("times");
				if (timesNode != null && timesNode.isNull() == false) {
					if (timesNode.isArray()) {
						ArrayNode timeArray = (ArrayNode) timesNode;
						for (JsonNode oneTime : timeArray) {
							String timeStr = oneTime.asText();
							Time t = getTime(timeStr);
							if (listTimes.size() == 0) {
								t.first = true;
							}
							listTimes.add(t);
						}
					} else if (timesNode.isTextual()) {
						String timeStr = timesNode.asText();
						listTimes.add(getTime(timeStr));
					} else {
						throw new Exception("Invalid times value in atTimes node: " + atTimesNode);
					}
				} else {
					throw new Exception("times attribute is missing in atTimes node: " + atTimesNode);
				}
			} else if (atTimeType.equals("AT_INTERVALS")) {
				// create times entries for intervals
				Date startTime = getMandatoryValueTime(atTimesNode, "startTime");
				Calendar c = Calendar.getInstance(timeZone);
				c.setTime(startTime);
				Date endTime = getMandatoryValueTime(atTimesNode, "endTime");
				int interval = getMandatoryValueInt(atTimesNode, "interval");
				boolean done = false;
				while (done == false) {
					Date d = c.getTime();
					if (d.after(endTime)) {
						done = true;
					} else {
						Time t = getTime(d);
						if (listTimes.size() == 0) {
							t.first = true;
						}
						listTimes.add(t);
					}
					c.add(Calendar.MINUTE, interval);
				}
			}
			Collections.sort(listTimes);
		}
		
	}
	
	private Time getTime(String time) throws Exception {
		if (time == null || time.trim().isEmpty()) {
			throw new Exception("time value cannot be empty or null");
		}
		int pos = time.indexOf(':');
		if (pos == -1) {
			throw new Exception("time value: " + time + " does not contains a valid time");
		}
		String hours = time.substring(0, pos);
		String minutes = time.substring(pos + 1);
		Time t = new Time();
		t.hour = Integer.valueOf(hours);
		t.minute = Integer.valueOf(minutes);
		return t;
	}
	
	private Time getTime(Date time) throws Exception {
		if (time == null) {
			throw new Exception("time value cannot be empty or null");
		}
		Calendar c = Calendar.getInstance(timeZone);
		c.setTime(time);
		Time t = new Time();
		t.hour = c.get(Calendar.HOUR_OF_DAY);
		t.minute = c.get(Calendar.MINUTE);
		return t;
	}
	
	public Time getNextTime(int hour, int minute) {
		if (listTimes.size() == 0) {
			throw new IllegalStateException("List of times is empty");
		}
		Time nextTime = null;
		for (Time t : listTimes) {
			if (t.hour == hour) {
				if (t.minute > minute) {
					nextTime = t;
					break;
				}
			} else if (t.hour > hour) {
				nextTime = t;
				break;
			}
		}
		if (nextTime == null) {
			// we did not found one, means we have overrun the list. 
			// take the first one
			nextTime = listTimes.get(0);
		}
		return nextTime;
	}
	
	public int getNextWeekDay(int currentWeekDay) {
		int result = 0;
		for (int d : listWeekDayIndex) {
			if (d > currentWeekDay) {
				result = d;
				break;
			}
		}
		if (result == 0) {
			// we were at the end of the list of days
			// take the first one
			result = listWeekDayIndex.get(0);
		}
		return result;
	}

	public static class Time implements Comparable<Time> {
		int hour = 0;
		int minute = 0;
		boolean first = false;
		@Override
		public String toString() {
			return (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + (first ? " *" : "");
		}
		@Override
		public boolean equals(Object o) {
			if (o instanceof Time) {
				Time other = (Time) o;
				return other.hour == this.hour && other.minute == this.minute;
			} else {
				return false;
			}
		}
		@Override
		public int compareTo(Time o) {
			if (o.hour > hour) {
				return -1;
			} else if (o.hour < hour) {
				return 1;
			} else if (o.minute > minute) {
				return -1;
			} else if (o.minute < minute) {
				return 1;
			} else {
				return 0;
			}
		}
	}
	
	public List<Time> getTimes() {
		return listTimes;
	}
	
	public static class Day {
		int year = 0;
		int month = 1;
		int dayOfMonth = 1;
		int dayOfWeek = 1;
		int week = 1;
	}
	
	private String getMandatoryValueString(JsonNode node, String attribute) throws Exception {
		if (node == null || node.isNull()) {
			throw new Exception("node cannot be null");
		}
		JsonNode valueNode = node.get(attribute);
		if (valueNode == null || valueNode.isNull()) {
			throw new Exception("attribute: " + attribute + " is missing or null");
		}
		String value = valueNode.asText();
		if (value == null || value.trim().isEmpty()) {
			throw new Exception("attribute: " + attribute + " value is empty or null");
		}
		return value;
	}
	
	private int getMandatoryValueInt(JsonNode node, String attribute) {
		try {
			String strValue = getMandatoryValueString(node, attribute);
			return Integer.parseInt(strValue);
		} catch (Exception e) {
			throw new IllegalStateException("Cannot get int value from attribute: " + attribute + ", error: " + e.getMessage(), e);
		}
	}
	
	private int getInterval() {
		return getMandatoryValueInt(oneTriggerNode, "interval");
	}
	
	private Date getMandatoryValueDate(JsonNode node, String attribute) {
		try {
			String strValue = getMandatoryValueString(node, attribute);
			return new SimpleDateFormat("yyyy-MM-dd").parse(strValue);
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot get Date value from attribute: " + attribute + ", error: " + e.getMessage(), e);
		}
	}
	
	private Date getMandatoryValueTime(JsonNode node, String attribute) {
		try {
			String strValue = getMandatoryValueString(node, attribute);
			return new SimpleDateFormat("HH:mm").parse(strValue);
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot get Time value from attribute: " + attribute + ", error: " + e.getMessage(), e);
		}
	}

	/**
     * Returns the time zone for which this <code>CronExpression</code> 
     * will be resolved.
     */
    public TimeZone getTimeZone() {
        if (timeZone == null) {
            timeZone = TimeZone.getDefault();
        }
        return timeZone;
    }

    ////////////////////////////////////////////////////////////////////////////
    //
    // Computation Functions
    //
    ////////////////////////////////////////////////////////////////////////////
    public Date getNextValidTimeAfter(Date afterTime) {
    	if (afterTime == null) {
    		afterTime = this.startDate;
    	}
        // Computation is based on Gregorian year only.
        Calendar cl = new java.util.GregorianCalendar(getTimeZone()); 
        cl.setTime(afterTime);
        cl.setFirstDayOfWeek(Calendar.MONDAY);
        // TMC does not deal with seconds and milliseconds
        cl.set(Calendar.SECOND, 0);
        cl.set(Calendar.MILLISECOND, 0);
        // move ahead one minute forward to prevent find the same one again
        cl.add(Calendar.MINUTE, 1);
        int minute = 0;
        int hour = 0;
        if (cl.get(Calendar.YEAR) > 2999) { // prevent endless loop...
            return null;
        }
        // check the date and time matches and continue if not matched
        hour = cl.get(Calendar.HOUR_OF_DAY);
        minute = cl.get(Calendar.MINUTE);
        if (type.equals("DAILY")) {
        	// check time list
        	Time now = new Time();
        	now.hour = hour;
        	now.minute = minute;
        	Time next = getNextTime(hour, minute);
        	if (next.compareTo(now) < 0) {
        		// if there is an overrun add interval days
            	cl.add(Calendar.DAY_OF_YEAR, getInterval());
        	}
        	cl.set(Calendar.HOUR_OF_DAY, next.hour);
        	cl.set(Calendar.MINUTE, next.minute);
        } else if (type.equals("WEEKLY")) {
        	// check if we are at the right week day
        	// check time list
        	Time now = new Time();
        	now.hour = hour;
        	now.minute = minute;
        	Time next = null;
            int currentWeekDay = cl.get(Calendar.DAY_OF_WEEK);
    		int nextWeekDay = getNextWeekDay(currentWeekDay);
    		if (nextWeekDay == listWeekDayIndex.get(0)) {
        		// check if we found the first entry of the week day list
            	cl.set(Calendar.DAY_OF_WEEK, nextWeekDay);
            	cl.add(Calendar.WEEK_OF_YEAR, getInterval());
            	next = listTimes.get(0); // we have to set the first time for the first day
    		} else {
    			next = getNextTime(hour, minute);
        		if (next.compareTo(now) < 0) {
            		// if there is an overrun the time switch to next week day
                	cl.set(Calendar.DAY_OF_WEEK, nextWeekDay);
                	if (nextWeekDay < currentWeekDay) {
                		// overrun of week day, switch to next week
                		cl.add(Calendar.WEEK_OF_YEAR, getInterval());
                	}
            	}
    		}
        	cl.set(Calendar.HOUR_OF_DAY, next.hour);
        	cl.set(Calendar.MINUTE, next.minute);
        } else if (type.equals("MONTHLY")) {
        	// check time list
        	Time now = new Time();
        	now.hour = hour;
        	now.minute = minute;
        	Time next = null;
        	int currentDayOfMonth = cl.get(Calendar.DAY_OF_MONTH);
        	if (currentDayOfMonth < dayOfMonth) {
            	cl.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            	next = listTimes.get(0);
        	} else if (currentDayOfMonth > dayOfMonth) {
            	cl.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            	cl.add(Calendar.MONTH, getInterval());
            	next = listTimes.get(0);
        	} else {
        		// we are at the right day but over the time
        		next = getNextTime(hour, minute); // get the next time
            	if (next.compareTo(now) < 0) {
            		// if there is an overrun add interval days
                	cl.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                	cl.add(Calendar.MONTH, getInterval());
            	}
        	}
        	cl.set(Calendar.HOUR_OF_DAY, next.hour);
        	cl.set(Calendar.MINUTE, next.minute);
        } else {
        	throw new IllegalStateException("Unknown schedule type: " + type);
        }
        return cl.getTime();
    }
    
}
