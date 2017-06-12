/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/JforumOverviewImpl.java $
 * $Id: JforumOverviewImpl.java 1180 2011-02-17 01:19:58Z ggolden $
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
import org.etudes.activitymeter.api.JforumOverview;

/**
 * JforumOverviewImpl implements JforumOverview.
 */
public class JforumOverviewImpl implements JforumOverview
{
	/** Our logger. */
	private static Log M_log = LogFactory.getLog(JforumOverviewImpl.class);

	protected Integer numPostsTotal = null;

	protected Integer pctNotPosted = null;

	protected Integer pctPosted = null;

	/**
	 * Construct
	 */
	public JforumOverviewImpl(Integer numPostsTotal, Integer pctNotPosted, Integer pctPosted)
	{
		this.numPostsTotal = numPostsTotal;
		this.pctNotPosted = pctNotPosted;
		this.pctPosted = pctPosted;
	}

	public Integer getNumPostsTotal()
	{
		return this.numPostsTotal;
	}

	public Integer getPctNotPosted()
	{
		return this.pctNotPosted;
	}

	public Integer getPctPosted()
	{
		return this.pctPosted;
	}
}
