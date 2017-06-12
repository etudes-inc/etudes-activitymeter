/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/MnemeItemOverviewImpl.java $
 * $Id: MnemeItemOverviewImpl.java 8568 2014-08-30 06:26:16Z mallikamt $
 ***********************************************************************************
 *
 * Copyright (c) 2011, 2014 Etudes, Inc.
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
import org.etudes.activitymeter.api.MnemeItemOverview;

/**
 * MnemeItemOverviewImpl implements MnemeItemOverview.
 */
public class MnemeItemOverviewImpl implements MnemeItemOverview
{
	/** Our logger. */
	private static Log M_log = LogFactory.getLog(MnemeItemOverviewImpl.class);

	/** The item access status. */
	protected ItemAccessStatus accessStatus = null;	

	protected String id = null;

	protected Integer numInProgress = null;

	protected Integer numSubmissions = null;

	protected Integer numSubmitters = null;

	/** The item status. */
	protected ItemStatus status = null;

	protected String title = null;

	protected String type = null;

	/**
	 * Construct
	 */
	public MnemeItemOverviewImpl(String id, Integer numInProgress, Integer numSubmissions, Integer numSubmitters, ItemStatus status, String type,
			String title, ItemAccessStatus accessStatus)
	{
		this.id = id;
		this.numInProgress = numInProgress;
		this.numSubmissions = numSubmissions;
		this.numSubmitters = numSubmitters;
		this.status = status;
		this.type = type;
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
	public Integer getNumInProgress()
	{
		return this.numInProgress;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getNumSubmissions()
	{
		return this.numSubmissions;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getNumSubmitters()
	{
		return this.numSubmitters;
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

	/**
	 * {@inheritDoc}
	 */
	public String getType()
	{
		return this.type;
	}
}
