/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/ParticipantOverviewImpl.java $
 * $Id: ParticipantOverviewImpl.java 2010 2011-09-28 03:45:12Z ggolden $
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

import org.etudes.activitymeter.api.ParticipantOverview;
import org.etudes.activitymeter.api.ParticipantStatus;

/**
 * ParticipantOverviewImpl implements ParticipantOverview.
 */
public class ParticipantOverviewImpl implements ParticipantOverview
{
	/** Our logger. */
	// private static Log M_log = LogFactory.getLog(ParticipantOverviewImpl.class);

	/** The user display id. */
	protected String displayId = null;

	/** First site visit. */
	protected Date firstVisit = null;

	/** The name of the group the user is in in the site. */
	protected String groupTitle = null;

	/** The user id. */
	protected String id = null;

	/** Most recent site visit. */
	protected Date lastVisit = null;

	/** Number of JForum posts. */
	protected Integer numJforumPosts = null;

	/** Number of melete views. */
	protected Integer numMeleteViews = null;

	/** Number of mneme submissions. */
	protected Integer numMnemeSubmissions = null;

	/** Number of site visits. */
	protected Integer numVisits = null;

	/** The roster access code for this user. */
	protected String rosterAccessCode = null;

	/** The sort name. */
	protected String sortName = null;

	/** The status. */
	protected ParticipantStatus status = null;

	/** The syllabus view date. */
	protected Date syllabusDate = null;

	/**
	 * Construct
	 */
	public ParticipantOverviewImpl(Date firstVisit, String id, Date lastVisit, Integer numJforumPosts, Integer numMeleteViews,
			Integer numMnemeSubmissions, Integer numVisits, String sortName, String displayId, ParticipantStatus status, Date syllabusDate,
			String groupTitle, String rosterAccessCode)
	{
		this.firstVisit = firstVisit;
		this.id = id;
		this.lastVisit = lastVisit;
		this.numJforumPosts = numJforumPosts;
		this.numMeleteViews = numMeleteViews;
		this.numMnemeSubmissions = numMnemeSubmissions;
		this.numVisits = numVisits;
		this.sortName = sortName;
		this.displayId = displayId;
		this.status = status;
		this.syllabusDate = syllabusDate;
		this.groupTitle = groupTitle;
		this.rosterAccessCode = rosterAccessCode;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getDisplayId()
	{
		return this.displayId;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getFirstVisitDate()
	{
		return this.firstVisit;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getGroupTitle()
	{
		return this.groupTitle;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getId()
	{
		return this.id;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getLastVisitDate()
	{
		return this.lastVisit;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getNumJforumPosts()
	{
		return this.numJforumPosts;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getNumMeleteViews()
	{
		return this.numMeleteViews;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getNumMnemeSubmissions()
	{
		return this.numMnemeSubmissions;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getNumVisits()
	{
		return this.numVisits;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getRosterAccessCode()
	{
		return this.rosterAccessCode;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getSortName()
	{
		return this.sortName;
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
	public Date getSyllabusDate()
	{
		return this.syllabusDate;
	}
}
