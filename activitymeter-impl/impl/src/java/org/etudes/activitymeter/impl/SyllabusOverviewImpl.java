/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/SyllabusOverviewImpl.java $
 * $Id: SyllabusOverviewImpl.java 2978 2012-06-13 15:34:19Z ggolden $
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

import org.etudes.activitymeter.api.SyllabusOverview;

/**
 * SyllabusOverviewImpl implements SyllabusOverview.
 */
public class SyllabusOverviewImpl implements SyllabusOverview
{
	/** Our logger. */
	// private static Log M_log = LogFactory.getLog(SyllabusOverviewImpl.class);

	/** The site id. */
	protected String context = null;

	/** The percent (0..100) of users who have accepted the syllabus. */
	protected Integer pctAccepted = null;

	/** The percent (0..100) of users who have viewed the syllabus. */
	protected Integer pctViewed = null;
	/**
	 * Construct
	 */
	public SyllabusOverviewImpl(String context, Integer pctAccepted, Integer pctViewed)
	{
		this.context = context;
		this.pctAccepted = pctAccepted;
		this.pctViewed = pctViewed;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getPctAccepted()
	{
		return this.pctAccepted;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Integer getPctViewed()
	{
		return this.pctViewed;
	}
}
