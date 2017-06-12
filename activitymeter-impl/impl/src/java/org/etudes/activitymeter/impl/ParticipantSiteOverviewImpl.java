/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/ParticipantSiteOverviewImpl.java $
 * $Id: ParticipantSiteOverviewImpl.java 12543 2016-01-13 18:30:32Z rashmim $
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

import org.etudes.activitymeter.api.ParticipantSiteOverview;
import org.etudes.activitymeter.api.ParticipantStatus;

/**
 * ParticipantSiteOverviewImpl implements ParticipantSiteOverview.
 */
public class ParticipantSiteOverviewImpl implements ParticipantSiteOverview
{
	/** Our logger. */
	// private static Log M_log = LogFactory.getLog(ParticipantSiteOverviewImpl.class);

	/** The user display id. */
	protected String displayId = null;

	/** The date of the first visit. */
	protected Date firstVisit = null;

	/** The user id. */
	protected String id = null;

	/** The date of the most recent visit. */
	protected Date lastVisit = null;

	/** The number of sessions in which the user visited the site. */
	protected Integer numVisits = null;

	/** The sort name. */
	protected String sortName = null;

	/** The status. */
	protected ParticipantStatus status = null;
	
	/** section information **/
	protected String groupTitle = null;

	/**
	 * Construct
	 */
	public ParticipantSiteOverviewImpl(String id, String sortName, String displayId, String groupTitle, ParticipantStatus status, Date firstVisit, Date lastVisit,
			Integer numVisits)
	{
		this.id = id;
		this.sortName = sortName;
		this.displayId = displayId;
		this.groupTitle = groupTitle;
		this.status = status;
		this.firstVisit = firstVisit;
		this.lastVisit = lastVisit;
		this.numVisits = numVisits;
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
	public Integer getNumVisits()
	{
		return this.numVisits;
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
}
