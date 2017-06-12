/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/ParticipantJforumItemDetailImpl.java $
 * $Id: ParticipantJforumItemDetailImpl.java 1660 2011-06-29 19:16:02Z ggolden $
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

import org.etudes.activitymeter.api.ParticipantJforumItemDetail;
import org.etudes.activitymeter.api.ParticipantStatus;

/**
 * ParticipantJforumItemDetailImpl implements ParticipantJforumItemDetail.
 */
public class ParticipantJforumItemDetailImpl implements ParticipantJforumItemDetail
{
	/** Our logger. */
	// private static Log M_log = LogFactory.getLog(ParticipantJforumItemDetailImpl.class);

	/** The evaluated date. */
	protected Date evaluated = null;

	/** The user id. */
	protected String id = null;

	/** Number of posts. */
	protected Integer posts = null;

	/** The reviewed date. */
	protected Date reviewed = null;

	/** The sort name. */
	protected String sortName = null;

	/** The status. */
	protected ParticipantStatus status = null;

	/**
	 * Construct
	 */
	public ParticipantJforumItemDetailImpl(String id, String sortName, ParticipantStatus status, Integer posts, Date evaluated, Date reviewed)
	{
		this.id = id;
		this.sortName = sortName;
		this.status = status;
		this.posts = posts;
		if (this.posts == null) this.posts = Integer.valueOf(0);
		this.evaluated = evaluated;
		this.reviewed = reviewed;
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
		if (this.evaluated == null) return Boolean.FALSE;
		if (this.reviewed == null) return Boolean.TRUE;

		return this.reviewed.before(this.evaluated);
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
	public String getId()
	{
		return this.id;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getPosts()
	{
		return this.posts;
	}

	/**
	 * {@inheritDoc}
	 */
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
	public ParticipantStatus getStatus()
	{
		return this.status;
	}
}
