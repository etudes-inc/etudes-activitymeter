/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-api/api/src/java/org/etudes/activitymeter/api/ParticipantStatus.java $
 * $Id: ParticipantStatus.java 12159 2015-11-30 18:25:50Z mallikamt $
 ***********************************************************************************
 *
 * Copyright (c) 2011, 2013, 2014, 2015 Etudes, Inc.
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

package org.etudes.activitymeter.api;

/**
 * ParticipantStatus captures a user's relationship to the site.
 */
public enum ParticipantStatus
{
	enrolled(0), added(1), blocked(2), dropped(3), ta(4), instructor(5), observer(6);

	private final int sortOrder;

	private ParticipantStatus(int sortOrder)
	{
		this.sortOrder = Integer.valueOf(sortOrder);
	}

	public Integer getSortValue()
	{
		return sortOrder;
	}
}
