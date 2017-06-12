/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/ParticipantMeleteItemDetailImpl.java $
 * $Id: ParticipantMeleteItemDetailImpl.java 12565 2016-01-15 21:43:32Z rashmim $
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

import org.etudes.activitymeter.api.ParticipantMeleteItemDetail;
import org.etudes.activitymeter.api.ParticipantStatus;

/**
 * ParticipantMeleteItemDetailImpl implements ParticipantMeleteItemDetail.
 */
public class ParticipantMeleteItemDetailImpl implements ParticipantMeleteItemDetail
{
	/** Our logger. */
	// private static Log M_log = LogFactory.getLog(ParticipantMeleteItemDetailImpl.class);

	/** The user id. */
	protected String id = null;

	/** user's iid **/
	protected String displayId = null;
	
	/** user's section name */
	protected String groupTitle = null;
	
	/** The sort name. */
	protected String sortName = null;

	/** The status. */
	protected ParticipantStatus status = null;

	/** The view date. */
	protected Date firstViewed = null;
	
	/** The view date. */
	protected Date viewed = null;

	/**
	 * Construct
	 */
	public ParticipantMeleteItemDetailImpl(String id, String sortName, String displayId, String groupTitle, ParticipantStatus status, Date firstViewed, Date viewed)
	{
		this.id = id;
		this.sortName = sortName;
		this.displayId = displayId;
		this.groupTitle = groupTitle;
		this.status = status;
		this.firstViewed = firstViewed;
		this.viewed = viewed;
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
	public Date getFirstViewed()
	{
		return this.firstViewed;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getViewed()
	{
		return this.viewed;
	}
}
