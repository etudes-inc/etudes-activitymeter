/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/MeleteOverviewImpl.java $
 * $Id: MeleteOverviewImpl.java 1271 2011-03-11 20:37:37Z ggolden $
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

import org.etudes.activitymeter.api.MeleteOverview;

/**
 * MeleteOverviewImpl implements MeleteOverview.
 */
public class MeleteOverviewImpl implements MeleteOverview
{
	/** Our logger. */
	// private static Log M_log = LogFactory.getLog(MeleteOverviewImpl.class);

	protected Integer numSections = null;

	protected Integer numSectionsViewed = null;

	/**
	 * Construct
	 */
	public MeleteOverviewImpl(Integer numSections, Integer numSectionsViewed)
	{
		this.numSections = numSections;
		this.numSectionsViewed = numSectionsViewed;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getNumSections()
	{
		return this.numSections;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getNumSectionsViewed()
	{
		return this.numSectionsViewed;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getPctSectionsViewed()
	{
		if (this.numSections <= 0) return Integer.valueOf(0);
		return (this.numSectionsViewed * 100) / this.numSections;
	}
}
