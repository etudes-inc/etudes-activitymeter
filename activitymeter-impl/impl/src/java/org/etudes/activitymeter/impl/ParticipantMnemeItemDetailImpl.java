/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/ParticipantMnemeItemDetailImpl.java $
 * $Id: ParticipantMnemeItemDetailImpl.java 3816 2012-12-08 21:16:39Z ggolden $
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

import org.etudes.activitymeter.api.ParticipantMnemeItemDetail;
import org.etudes.activitymeter.api.ParticipantStatus;

/**
 * ParticipantMnemeItemDetailImpl implements ParticipantMnemeItemDetail.
 */
public class ParticipantMnemeItemDetailImpl implements ParticipantMnemeItemDetail
{
	/** Our logger. */
	// private static Log M_log = LogFactory.getLog(ParticipantMnemeItemDetailImpl.class);

	/** The evaluated date. */
	protected Date evaluated = null;

	/** When there is an evaluation later than the last review. */
	protected Boolean evaulationNotReviewed = Boolean.FALSE;

	/** The finished date. */
	protected Date finishedDate = null;

	/** The user id. */
	protected String id = null;

	/** The in-progress indicator. */
	protected Boolean inProgress = Boolean.FALSE;

	/** The reviewed date. */
	protected Date reviewed = null;

	/** The sort name. */
	protected String sortName = null;

	/** The started date. */
	protected Date startedDate = null;

	/** The status. */
	protected ParticipantStatus status = null;

	/** To suppress started and finished dates being displayed. */
	protected Boolean suppressDates = Boolean.FALSE;

	/**
	 * Construct
	 */
	public ParticipantMnemeItemDetailImpl(String id, String sortName, ParticipantStatus status, Date evaluatedDate, Date finishedDate, Date reviewed,
			Date startedDate, Boolean inProgress, Boolean suppressDates, Boolean evaulationNotReviewed)
	{
		this.id = id;
		this.sortName = sortName;
		this.status = status;
		this.evaluated = evaluatedDate;
		this.finishedDate = finishedDate;
		this.reviewed = reviewed;
		this.startedDate = startedDate;
		this.inProgress = inProgress;
		this.suppressDates = suppressDates;
		this.evaulationNotReviewed = evaulationNotReviewed;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getEvaluated()
	{
		return this.evaluated;
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean getEvaluationNotReviewed()
	{
		return this.evaulationNotReviewed;
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean getEvaluationReviewed()
	{
		if ((this.evaluated == null) || (this.reviewed == null)) return Boolean.FALSE;

		return this.reviewed.after(this.evaluated);
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getFinishedDate()
	{
		return this.finishedDate;
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
	public Boolean getInProgress()
	{
		return this.inProgress;
	}

	public Date getReviewed()
	{
		return this.reviewed;
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
	public Date getStartedDate()
	{
		return this.startedDate;
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
	public Boolean getSuppressDates()
	{
		return this.suppressDates;
	}
}
