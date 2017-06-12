/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/MnemeOverviewImpl.java $
 * $Id: MnemeOverviewImpl.java 9344 2014-11-25 00:12:42Z ggolden $
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
import org.etudes.activitymeter.api.MnemeOverview;

/**
 * MnemeOverviewImpl implements MnemeOverview.
 */
public class MnemeOverviewImpl implements MnemeOverview
{
	/** Our logger. */
	private static Log M_log = LogFactory.getLog(MnemeOverviewImpl.class);

	protected Integer numAsignmentSubmissions = null;

	protected Integer numAssignmentSubmissionsInProgress = null;

	protected Integer numOfflineSubmssions = null;

	protected Integer numSurveySubmissions = null;

	protected Integer numSurveySubmissionsInProgress = null;

	protected Integer numTestSubmissions = null;

	protected Integer numTestSubmissionsInProgress = null;

	/**
	 * Construct
	 */
	public MnemeOverviewImpl(Integer numAsignmentSubmissions, Integer numAssignmentSubmissionsInProgress, Integer numSurveySubmissions,
			Integer numSurveySubmissionsInProgress, Integer numTestSubmissions, Integer numTestSubmissionsInProgress, Integer numOfflineSubmissions)
	{
		this.numAsignmentSubmissions = numAsignmentSubmissions;
		this.numAssignmentSubmissionsInProgress = numAssignmentSubmissionsInProgress;
		this.numSurveySubmissions = numSurveySubmissions;
		this.numSurveySubmissionsInProgress = numSurveySubmissionsInProgress;
		this.numTestSubmissions = numTestSubmissions;
		this.numTestSubmissionsInProgress = numTestSubmissionsInProgress;
		this.numOfflineSubmssions = numOfflineSubmissions;
	}

	public Integer getNumAssignmentSubmissions()
	{
		return this.numAsignmentSubmissions;
	}

	public Integer getNumAssignmentSubmissionsInProgress()
	{
		return this.numAssignmentSubmissionsInProgress;
	}

	public Integer getNumOfflineSubmissions()
	{
		return this.numOfflineSubmssions;
	}

	public Integer getNumSurveySubmissions()
	{
		return this.numSurveySubmissions;
	}

	public Integer getNumSurveySubmissionsInProgress()
	{
		return this.numSurveySubmissionsInProgress;
	}

	public Integer getNumTestSubmissions()
	{
		return this.numTestSubmissions;
	}

	public Integer getNumTestSubmissionsInProgress()
	{
		return this.numTestSubmissionsInProgress;
	}
}
