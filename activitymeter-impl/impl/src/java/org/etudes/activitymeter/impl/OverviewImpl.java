/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/OverviewImpl.java $
 * $Id: OverviewImpl.java 12131 2015-11-25 15:34:48Z mallikamt $
 ***********************************************************************************
 *
 * Copyright (c) 2011, 2015 Etudes, Inc.
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

import org.etudes.activitymeter.api.Overview;

/**
 * OverviewImpl implements OverviewImpl.
 */
public class OverviewImpl implements Overview
{
	/** Our logger. */
	// private static Log M_log = LogFactory.getLog(OverviewImpl.class);

	/** The site id. */
	protected String context = null;

	/** The number of blocked participants. */
	protected Integer numBlocked = null;

	/** The number of dropped participants. */
	protected Integer numDropped = null;

	/** The number of enrolled participants. */
	protected Integer numEnrolled = null;

	/** The number of added participants. */
	protected Integer numAdded = null;

	/** The number of qualified participants who have not visited. */
	protected Integer numNotVisited = null;

	/** The time period in days. */
	protected Integer period = null;

	/**
	 * Construct
	 */
	public OverviewImpl(String context, Integer numBlocked, Integer numDropped, Integer numEnrolled, Integer numAdded, Integer numNotVisited, Integer period)
	{
		this.context = context;
		this.numBlocked = numBlocked;
		this.numDropped = numDropped;
		this.numEnrolled = numEnrolled;
		this.numAdded = numAdded;
		this.numNotVisited = numNotVisited;
		if ((this.numNotVisited != null) && (this.numNotVisited.intValue() == 0)) this.numNotVisited = null;
		this.period = period;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getNumBlocked()
	{
		return this.numBlocked;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getNumDropped()
	{
		return this.numDropped;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getNumEnrolled()
	{
		return this.numEnrolled;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getNumAdded()
	{
		return this.numAdded;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getNumNotVisitedInPeriod()
	{
		return this.numNotVisited;
	}
}
