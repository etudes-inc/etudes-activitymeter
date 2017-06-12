/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/MeleteItemDetailImpl.java $
 * $Id: MeleteItemDetailImpl.java 2978 2012-06-13 15:34:19Z ggolden $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.activitymeter.api.ItemAccessStatus;
import org.etudes.activitymeter.api.MeleteItemDetail;

/**
 * MeleteItemDetailImpl implements MeleteItemDetail.
 */
public class MeleteItemDetailImpl implements MeleteItemDetail
{
	/** Our logger. */
	private static Log M_log = LogFactory.getLog(MeleteItemDetailImpl.class);

	/** The section close date. */
	protected Date closeDate;
	
	/** The section allow until date. */
	protected Date allowUntilDate;

	/** The site id. */
	protected String context = null;

	/** The display title for the section's module. */
	protected String moduleTitle;

	/** The number of "Student" participants who have viewed this section */
	protected Integer numViewed;

	/** The section open date. */
	protected Date openDate;

	/** The section id. */
	protected String sectionId = null;

	/** The item status. */
	protected ItemAccessStatus status = null;

	/** The display title for the section. */
	protected String title;

	/** The total number of "Student" participants who have access to this section. */
	protected Integer total;

	/**
	 * Construct
	 */
	public MeleteItemDetailImpl(String context, Date closeDate, Date allowUntilDate, Integer numViewed, Date openDate, String sectionId, ItemAccessStatus status,
			String title, String moduleTitle, Integer total)
	{
		this.context = context;
		this.closeDate = closeDate;
		this.allowUntilDate = allowUntilDate;
		this.numViewed = numViewed;
		this.openDate = openDate;
		this.sectionId = sectionId;
		this.status = status;
		this.title = title;
		this.moduleTitle = moduleTitle;
		this.total = total;
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
	public Date getAllowUntilDate()
	{
		return allowUntilDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getModuleTitle()
	{
		return this.moduleTitle;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getNumViewed()
	{
		return this.numViewed;
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
	public Integer getPctViewed()
	{
		if (this.total <= 0) return Integer.valueOf(0);
		return (this.numViewed * 100) / this.total;
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
	public Integer getTotal()
	{
		return this.total;
	}
}
