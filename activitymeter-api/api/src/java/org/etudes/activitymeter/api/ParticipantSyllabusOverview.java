/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-api/api/src/java/org/etudes/activitymeter/api/ParticipantSyllabusOverview.java $
 * $Id: ParticipantSyllabusOverview.java 12544 2016-01-13 21:03:50Z rashmim $
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

package org.etudes.activitymeter.api;

import java.util.Date;

/**
 * ParticipantSyllabusOverview shows syllabus specific overview information for a participant.
 */
public interface ParticipantSyllabusOverview
{
	/**
	 * @return The user id.
	 */
	String getId();

	/**
	 * @return The sort name for this user.
	 */
	String getSortName();
	
	/**
	 * @return The displayId for this user.
	 */
	String getDisplayId();
	
	/**
	 * @return The section name for this user.
	 */
	String getGroupTitle();

	/**
	 * @return The enrollment status.
	 */
	ParticipantStatus getStatus();

	/**
	 * @return The date the syllabus was accepted.
	 */
	Date getSyllabusDate();
	
	/**
	 * @return The date the syllabus was first visited.
	 */
	Date getSyllabusFirstVisitDate();
	
	/**
	 * @return The date the syllabus was last visited
	 */
	Date getSyllabusLastVisitDate();
	
}
