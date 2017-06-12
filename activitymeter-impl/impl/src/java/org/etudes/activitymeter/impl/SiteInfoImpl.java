/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/SiteInfoImpl.java $
 * $Id: SiteInfoImpl.java 1216 2011-03-05 01:27:46Z ggolden $
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
import org.etudes.activitymeter.api.SiteInfo;

/**
 * SiteInfoImpl implements SiteInfo.
 */
public class SiteInfoImpl implements SiteInfo
{
	/** Our logger. */
	private static Log M_log = LogFactory.getLog(SiteInfoImpl.class);

	/** The site id. */
	protected String context = null;

	/** The site term display string. */
	protected String term = null;

	/** The site title. */
	protected String title = null;

	/**
	 * Construct
	 */
	public SiteInfoImpl(String context, String term, String title)
	{
		this.context = context;
		this.term = term;
		this.title = title;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getTerm()
	{
		return this.term;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getTitle()
	{
		return this.title;
	}
}
