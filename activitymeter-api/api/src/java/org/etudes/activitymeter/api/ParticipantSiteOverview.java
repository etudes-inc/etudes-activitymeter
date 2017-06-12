/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-api/api/src/java/org/etudes/activitymeter/api/ParticipantSiteOverview.java $
 * $Id: ParticipantSiteOverview.java 12543 2016-01-13 18:30:32Z rashmim $
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

package org.etudes.activitymeter.api;

import java.util.Date;

/**
 * ParticipantSiteOverview shows site specific overview information for a participant
 */
public interface ParticipantSiteOverview
{
	/**
	 * @return The displayId for this user.
	 */
	String getDisplayId();

	/**
	 * @return The date of the first site visit, or null if never visited.
	 */
	Date getFirstVisitDate();
	
	/**
	 * @return The roster section name for this user.
	 */
	String getGroupTitle();

	/**
	 * @return The user id.
	 */
	String getId();

	/**
	 * @return The date of the last site visit, or null if never visited.
	 */
	Date getLastVisitDate();

	/**
	 * @return The number of times the site was visited by this user.
	 */
	Integer getNumVisits();

	/**
	 * @return The sort name for this user.
	 */
	String getSortName();

	/**
	 * @return The enrollment status.
	 */
	ParticipantStatus getStatus();
}
