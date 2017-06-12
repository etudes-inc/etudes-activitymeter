/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/MnemeItemDetailImpl.java $
 * $Id: MnemeItemDetailImpl.java 2517 2012-01-15 17:36:38Z ggolden $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.activitymeter.api.ItemAccessStatus;
import org.etudes.activitymeter.api.MnemeItemDetail;
import org.etudes.mneme.api.AssessmentType;

/**
 * MnemeItemDetailImpl implements MnemeItemDetail.
 */
public class MnemeItemDetailImpl implements MnemeItemDetail
{
	/** Our logger. */
	private static Log M_log = LogFactory.getLog(MnemeItemDetailImpl.class);

	/** The assessment accept until date. */
	protected Date acceptUntilDate;

	/** The site id. */
	protected String context = null;

	/** The assessment due date. */
	protected Date dueDate;

	/** The item id. */
	protected String itemId = null;

	/** The number of "Student" participants who have a submission in progress. */
	protected Integer numInProgress;

	/** The number of "Student" participants who have not yet started a submission. */
	protected Integer numNotSubmitted;

	/** The number of "Student" participants who have submitted at least once. */
	protected Integer numSubmitted;

	/** The assessment open date. */
	protected Date openDate;

	/** The percent (0..100) of "Student" participants who have submitted at least once. */
	protected Integer pctSubmitted;

	/** The item status. */
	protected ItemAccessStatus status = null;

	/** The display title for the item. */
	protected String title;

	/** The total number of "Student" participants. */
	protected Integer totalParticipants;

	/** The assessment type. */
	protected AssessmentType type = null;

	/**
	 * Construct
	 */
	public MnemeItemDetailImpl(Date acceptUntilDate, String context, Date dueDate, String itemId, Integer numInProgress, Integer numNotSubmitted,
			Integer numSubmitted, Date openDate, Integer pctSubmitted, ItemAccessStatus status, String title, Integer totalParticipants,
			AssessmentType type)
	{
		this.acceptUntilDate = acceptUntilDate;
		this.context = context;
		this.dueDate = dueDate;
		this.itemId = itemId;
		this.numInProgress = numInProgress;
		this.numNotSubmitted = numNotSubmitted;
		this.numSubmitted = numSubmitted;
		this.openDate = openDate;
		this.pctSubmitted = pctSubmitted;
		this.status = status;
		this.title = title;
		this.totalParticipants = totalParticipants;
		this.type = type;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getAcceptUntilDate()
	{
		return this.acceptUntilDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public ItemAccessStatus getAccessStatus()
	{
		return this.status;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getDueDate()
	{
		return this.dueDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getNumInProgress()
	{
		return this.numInProgress;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getNumNotSubmitted()
	{
		return this.numNotSubmitted;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getNumSubmitted()
	{
		return this.numSubmitted;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getOpenDate()
	{
		return this.openDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getPctSubmitted()
	{
		return this.pctSubmitted;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getTitle()
	{
		return this.title;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getTotalParticipants()
	{
		return this.totalParticipants;
	}

	/**
	 * {@inheritDoc}
	 */
	public AssessmentType getType()
	{
		return this.type;
	}
}
