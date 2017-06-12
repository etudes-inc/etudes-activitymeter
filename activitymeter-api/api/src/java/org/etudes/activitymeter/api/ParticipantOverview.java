/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-api/api/src/java/org/etudes/activitymeter/api/ParticipantOverview.java $
 * $Id: ParticipantOverview.java 2010 2011-09-28 03:45:12Z ggolden $
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
 * ParticipantOverview shows overview information for a participant.
 */
public interface ParticipantOverview
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
	 * @return The group title of the group the user is in in the site.
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
	 * @return The number of JForum posts made by this user.
	 */
	Integer getNumJforumPosts();

	/**
	 * @return The number of Melete modules that this user has viewed completely (all sections viewed).
	 */
	Integer getNumMeleteViews();

	/**
	 * @return The number of assessments this user has made at least one completed submission to.
	 */
	Integer getNumMnemeSubmissions();

	/**
	 * @return The number of times the site was visited by this user.
	 */
	Integer getNumVisits();

	/**
	 * @return The roster access code to select this particular user for a PM.
	 */
	String getRosterAccessCode();

	/**
	 * @return The sort name for this user.
	 */
	String getSortName();

	/**
	 * @return The enrollment status.
	 */
	ParticipantStatus getStatus();

	/**
	 * @return The date the syllabus was read.
	 */
	Date getSyllabusDate();
}
