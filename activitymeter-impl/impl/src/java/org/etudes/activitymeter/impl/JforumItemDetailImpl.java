/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/JforumItemDetailImpl.java $
 * $Id: JforumItemDetailImpl.java 3554 2012-11-26 22:30:49Z murthyt $
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
import org.etudes.activitymeter.api.JforumItemDetail;

/**
 * JforumItemDetailImpl implements JforumItemDetail.
 */
public class JforumItemDetailImpl implements JforumItemDetail
{
	/** Our logger. */
	private static Log M_log = LogFactory.getLog(JforumItemDetailImpl.class);

	/** The section close date. */
	protected Date closeDate;

	/** The site id. */
	protected String context = null;

	/** The item id. */
	protected String itemId = null;

	/** The section open date. */
	protected Date openDate;

	/** The % (0.100) qualified participants who have never posted to this item. */
	protected Integer pctNeverPosted = null;

	/** The % (0.100) qualified participants who have posted at least once to this item. */
	protected Integer pctPostedOnce = null;

	/** The item status. */
	protected ItemAccessStatus status = null;

	/** The item title. */
	protected String title = null;
	
	/** The item open date. */
	protected Date allowUntilDate;

	/**
	 * {@inheritDoc}
	 */
	public Date getAllowUntilDate()
	{
		return allowUntilDate;
	}

	/**
	 * Construct
	 */
	public JforumItemDetailImpl(Date allowUntilDate, String context, String itemId, Date closeDate, Date openDate, Integer pctNeverPosted, Integer pctPostedOnce,
			ItemAccessStatus status, String title)
	{
		this.allowUntilDate = allowUntilDate;
		this.context = context;
		this.itemId = itemId;
		this.closeDate = closeDate;
		this.openDate = openDate;
		this.pctNeverPosted = pctNeverPosted;
		this.pctPostedOnce = pctPostedOnce;
		this.status = status;
		this.title = title;
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
	public Date getCloseDate()
	{
		return closeDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getOpenDate()
	{
		return openDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getPctNeverPosted()
	{
		return this.pctNeverPosted;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getPctPostedOnce()
	{
		return this.pctPostedOnce;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getTitle()
	{
		return this.title;
	}
}
