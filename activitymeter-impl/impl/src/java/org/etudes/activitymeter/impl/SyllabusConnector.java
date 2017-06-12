/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/SyllabusConnector.java $
 * $Id: SyllabusConnector.java 2978 2012-06-13 15:34:19Z ggolden $
 ***********************************************************************************
 *
 * Copyright (c) 2011, 2012 Etudes, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/

package org.etudes.activitymeter.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.api.app.syllabus.SyllabusService;

/**
 * SyllabusConnector are methods used by the Overview service to get Syllabus data.
 */
public class SyllabusConnector
{
	/** Our logger. */
	private static Log M_log = LogFactory.getLog(SyllabusConnector.class);

	/** Dependency: */
	protected SyllabusService syllabusService = null;

	/**
	 * Returns to uninitialized state.
	 */
	public void destroy()
	{
		M_log.info("destroy()");
	}

	/**
	 * Check with Syllabus to find all the acceptance views.
	 * 
	 * @param context
	 *        The site id.
	 * @return The map, keyed by user id, data is the accepted date.
	 */
	public Map<String, Date> getSyllabusAcceptViews(String context, List<Participant> participants)
	{
		Map<String, Date> rv = new HashMap<String, Date>();
		//TODO: change to accept 
		Map<String, Date> syllabusUsers = syllabusService.getAllSyllabusUsersViewedData(context);

		for (Participant p : participants)
		{
			if (syllabusUsers.containsKey(p.userId)) rv.put(p.userId, syllabusUsers.get(p.userId));
		}

		return rv;
	}

	/**
	 * Check with Syllabus to find all the first views.
	 * 
	 * @param context
	 *        The site id.
	 * @return The map, keyed by user id, data is the first visit date.
	 */
	public Map<String, Date> getSyllabusFirstViews(String context, List<Participant> participants)
	{
		Map<String, Date> rv = new HashMap<String, Date>();
		Map<String, Date> syllabusUsers = syllabusService.getAllSyllabusUsersFirstViewedData(context);

		for (Participant p : participants)
		{
			if (syllabusUsers.containsKey(p.userId)) rv.put(p.userId, syllabusUsers.get(p.userId));
		}

		return rv;
	}
	
	/**
	 * Check with Syllabus to find all the last views.
	 * 
	 * @param context
	 *        The site id.
	 * @return The map, keyed by user id, data is the last visit date.
	 */
	public Map<String, Date> getSyllabusLastViews(String context, List<Participant> participants)
	{
		Map<String, Date> rv = new HashMap<String, Date>();
		Map<String, Date> syllabusUsers = syllabusService.getAllSyllabusUsersLastViewedData(context);

		for (Participant p : participants)
		{
			if (syllabusUsers.containsKey(p.userId)) rv.put(p.userId, syllabusUsers.get(p.userId));
		}

		return rv;
	}
	
	/**
	 * Final initialization
	 */
	public void init()
	{
		M_log.info("init()");
	}

	/*
	 * Set SyllabusService Dependency
	 */
	public void setSyllabusService(SyllabusService syllabusService)
	{
		this.syllabusService = syllabusService;
	}
}
