/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/SiteVisitImpl.java $
 * $Id: SiteVisitImpl.java 1284 2011-03-15 21:01:13Z mallikamt $
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
import org.etudes.activitymeter.api.SiteVisit;

/**
 * SiteVisitImpl implements SiteVisit.
 */
public class SiteVisitImpl implements SiteVisit
{
	/** Our logger. */
	private static Log M_log = LogFactory.getLog(SiteOverviewImpl.class);

	/** The site id. */
	protected String context = null;

	/** The date of the first site visit */
	protected Date firstSiteVisit = null;

	/** The date of the last site visit */
	protected Date lastSiteVisit = null;

	/** The user id. */
	protected String userId = null;

	/** Number of visits. */
	protected Integer visits = null;

	/**
	 * Construct
	 */
	public SiteVisitImpl()
	{

	}

	/**
	 * Construct
	 */
	public SiteVisitImpl(String context, String userId, Date firstSiteVisit, Date lastSiteVisit, Integer visits)
	{
		this.context = context;
		this.userId = userId;
		this.firstSiteVisit = firstSiteVisit;
		this.lastSiteVisit = lastSiteVisit;
		this.visits = visits;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getContext()
	{
		return this.context;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getFirstSiteVisit()
	{
		return this.firstSiteVisit;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getLastSiteVisit()
	{
		return this.lastSiteVisit;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getUserId()
	{
		return this.userId;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getVisits()
	{
		return this.visits;
	}

	public void setContext(String context)
	{
		this.context = context;
	}

	public void setFirstSiteVisit(Date firstSiteVisit)
	{
		this.firstSiteVisit = firstSiteVisit;
	}

	public void setLastSiteVisit(Date lastSiteVisit)
	{
		this.lastSiteVisit = lastSiteVisit;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public void setVisits(Integer visits)
	{
		this.visits = visits;
	}
}
