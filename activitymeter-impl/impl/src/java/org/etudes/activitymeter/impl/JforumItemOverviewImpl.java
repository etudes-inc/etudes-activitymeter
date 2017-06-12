/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/JforumItemOverviewImpl.java $
 * $Id: JforumItemOverviewImpl.java 1974 2011-09-06 16:46:25Z ggolden $
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
import org.etudes.activitymeter.api.ItemAccessStatus;
import org.etudes.activitymeter.api.ItemStatus;
import org.etudes.activitymeter.api.JforumItemOverview;

/**
 * JforumItemOverviewImpl implements JforumItemOverview.
 */
public class JforumItemOverviewImpl implements JforumItemOverview
{
	/** Our logger. */
	private static Log M_log = LogFactory.getLog(JforumItemOverviewImpl.class);

	/** The item access status. */
	protected ItemAccessStatus accessStatus = null;

	protected String id = null;

	protected Integer numPosters = null;

	protected Integer numPosts = null;

	/** The item status. */
	protected ItemStatus status = null;

	protected String title = null;

	/**
	 * Construct
	 */
	public JforumItemOverviewImpl(String id, Integer numPosters, Integer numPosts, ItemStatus status, String title, ItemAccessStatus accessStatus)
	{
		this.id = id;
		this.numPosters = numPosters;
		this.numPosts = numPosts;
		this.status = status;
		this.title = title;
		this.accessStatus = accessStatus;
	}

	/**
	 * {@inheritDoc}
	 */
	public ItemAccessStatus getAccessStatus()
	{
		return this.accessStatus;
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
	public Integer getNumPosters()
	{
		return this.numPosters;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getNumPosts()
	{
		return this.numPosts;
	}

	/**
	 * {@inheritDoc}
	 */
	public ItemStatus getStatus()
	{
		return this.status;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getTitle()
	{
		return this.title;
	}
}
