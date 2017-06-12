/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/MeleteItemOverviewImpl.java $
 * $Id: MeleteItemOverviewImpl.java 12131 2015-11-25 15:34:48Z mallikamt $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.activitymeter.api.ItemAccessStatus;
import org.etudes.activitymeter.api.ItemStatus;
import org.etudes.activitymeter.api.MeleteItemOverview;

/**
 * MeleteOverviewItemImpl implements MeleteOverviewItem.
 */
public class MeleteItemOverviewImpl implements MeleteItemOverview
{
	/** Our logger. */
	private static Log M_log = LogFactory.getLog(MeleteItemOverviewImpl.class);

	protected String id = null;

	protected Boolean isModule = null;

	protected Integer numViewers = null;

	/** The item status. */
	protected ItemStatus status = null;

	/** The item access status. */
	protected ItemAccessStatus accessStatus = null;		

	protected String title = null;

	/**
	 * Construct
	 */
	public MeleteItemOverviewImpl(String id, Boolean isModule, Integer numViewers, ItemStatus status, String title, ItemAccessStatus accessStatus)
	{
		this.id = id;
		this.isModule = isModule;
		this.numViewers = numViewers;
		this.status = status;
		this.title = title;
		this.accessStatus = accessStatus;
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
	public Boolean getIsModule()
	{
		return this.isModule;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getNumViewers()
	{
		return this.numViewers;
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
	public ItemAccessStatus getAccessStatus()
	{
		return this.accessStatus;
}
	
}
