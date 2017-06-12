/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/ParticipantSyllabusOverviewImpl.java $
 * $Id: ParticipantSyllabusOverviewImpl.java 12544 2016-01-13 21:03:50Z rashmim $
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

import org.etudes.activitymeter.api.ParticipantStatus;
import org.etudes.activitymeter.api.ParticipantSyllabusOverview;

/**
 * ParticipantSyllabusOverviewImpl implements ParticipantSyllabusOverview.
 */
public class ParticipantSyllabusOverviewImpl implements ParticipantSyllabusOverview
{
	/** Our logger. */
	// private static Log M_log = LogFactory.getLog(ParticipantSyllabusOverviewImpl.class);

	/** The user id. */
	protected String id = null;
	/** The sort name. */
	protected String sortName = null;

	/** The status. */
	protected ParticipantStatus status = null;

	/** The syllabus accept date. */
	protected Date syllabusAcceptDate = null;

	/** The syllabus accept date. */
	protected Date syllabusFirstVisitDate = null;

	/** The syllabus accept date. */
	protected Date syllabusLastVisitDate = null;

	/** iid **/
	protected String displayId = null;
	
	/** section name **/
	protected String groupTitle = null;
	/**
	 * Construct
	 */
	public ParticipantSyllabusOverviewImpl(String id, String sortName, String displayId, String groupTitle, ParticipantStatus status, Date syllabusAcceptDate, Date syllabusFirstVisitDate, Date syllabusLastVisitDate)
	{
		this.id = id;
		this.sortName = sortName;
		this.status = status;
		this.displayId = displayId;
		this.groupTitle = groupTitle;
		this.syllabusAcceptDate = syllabusAcceptDate;
		this.syllabusFirstVisitDate = syllabusFirstVisitDate;
		this.syllabusLastVisitDate = syllabusLastVisitDate;
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
	public String getSortName()
	{
		return this.sortName;
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
	public String getGroupTitle()
	{
		return this.groupTitle;
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
		return this.syllabusAcceptDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getSyllabusFirstVisitDate() {
		return syllabusFirstVisitDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getSyllabusLastVisitDate() {
		return syllabusLastVisitDate;
	}
}
