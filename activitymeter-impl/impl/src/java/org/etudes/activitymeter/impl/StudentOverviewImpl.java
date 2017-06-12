/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/StudentOverviewImpl.java $
 * $Id: StudentOverviewImpl.java 1319 2011-03-18 23:52:53Z ggolden $
 ***********************************************************************************
 *
 * Copyright (c) 2011 Etudes, Inc.
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

import org.etudes.activitymeter.api.ParticipantStatus;
import org.etudes.activitymeter.api.StudentOverview;

/**
 * SiteOverviewImpl implements StudentOverview.
 */
public class StudentOverviewImpl implements StudentOverview
{
	/** Our logger. */
	// private static Log M_log = LogFactory.getLog(StudentOverviewImpl.class);

	/** The site id. */
	protected String context = null;

	/** The last site visit. */
	protected Date lastVisit = null;

	/** The number of JForum posts. */
	protected Integer numJforum = null;

	/** The num coursemap items missed. */
	protected Integer numMissed = null;

	/** The number of mneme assessments completed. */
	protected Integer numMneme = null;

	/** The percent (0..100) of melete modules viewed. */
	protected Integer pctMelete = null;

	/** The status. */
	protected ParticipantStatus status = null;

	/** Has syllabus been viewed. */
	protected Date syllabusViewed = null;

	/** The time period in days. */
	protected String userId = null;

	/**
	 * Construct
	 */
	public StudentOverviewImpl(String context, Date lastVisit, Integer numJforum, Integer numMissed, Integer numMneme, Integer pctMelete,
			ParticipantStatus status, Date syllabusViewed, String userId)
	{
		this.context = context;
		this.lastVisit = lastVisit;
		this.numJforum = numJforum;
		this.numMissed = numMissed;
		this.numMneme = numMneme;
		this.pctMelete = pctMelete;
		this.status = status;
		this.syllabusViewed = syllabusViewed;
		this.userId = userId;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getLastSiteVisit()
	{
		return this.lastVisit;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getNumItemsMissed()
	{
		return this.numMissed;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getNumJforum()
	{
		return this.numJforum;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getNumMneme()
	{
		return this.numMneme;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getPctMelete()
	{
		return this.pctMelete;
	}

	/**
	 * {@inheritDoc}
	 */
	public ParticipantStatus getStatus()
	{
		return this.status;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getSyllabusViewed()
	{
		return this.syllabusViewed;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setNumItemsMissed(Integer missed)
	{
		this.numMissed = missed;
	}
}
