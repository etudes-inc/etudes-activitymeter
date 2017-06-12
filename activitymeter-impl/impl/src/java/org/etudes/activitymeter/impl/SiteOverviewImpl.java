/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/SiteOverviewImpl.java $
 * $Id: SiteOverviewImpl.java 1216 2011-03-05 01:27:46Z ggolden $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.activitymeter.api.SiteOverview;

/**
 * SiteOverviewImpl implements SiteOverview.
 */
public class SiteOverviewImpl implements SiteOverview
{
	/** Our logger. */
	private static Log M_log = LogFactory.getLog(SiteOverviewImpl.class);

	/** The site id. */
	protected String context = null;

	/** The count of "Student" participants (enrolled, blocked or dropped) who have never visited the site. */
	protected Integer numNeverVisited = null;

	/** The A count of the "Student" participants (enrolled, blocked or dropped) who have visited at least once. */
	protected Integer numVisited = null;

	/** The count of "Student" participants (enrolled, blocked or dropped) who have visited the site in the period. */
	protected Integer numVisitedInPeriod = null;

	/** The time period in days. */
	protected Integer period = null;

	/**
	 * Construct
	 */
	public SiteOverviewImpl(String context, Integer period, Integer numNeverVisited, Integer numVisited, Integer numVisitedInPeriod)
	{
		this.context = context;
		this.period = period;
		this.numNeverVisited = numNeverVisited;
		this.numVisited = numVisited;
		this.numVisitedInPeriod = numVisitedInPeriod;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getNumNeverVisited()
	{
		return this.numNeverVisited;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getNumVisited()
	{
		return this.numVisited;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getNumVisitedInPeriod()
	{
		return this.numVisitedInPeriod;
	}
}
