/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-api/api/src/java/org/etudes/activitymeter/api/StudentOverview.java $
 * $Id: StudentOverview.java 1319 2011-03-18 23:52:53Z ggolden $
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
 * StudentOverview overview information for a student in the site.
 */
public interface StudentOverview
{
	/**
	 * @return The date of the last site visit, or null if never visited.
	 */
	Date getLastSiteVisit();

	/**
	 * @return The number of "missed items" based on the course map.
	 */
	Integer getNumItemsMissed();

	/**
	 * @return The number of JForum posts.
	 */
	Integer getNumJforum();

	/**
	 * @return The number of completed assessments.
	 */
	Integer getNumMneme();

	/**
	 * @return The percent (0..100) of modules this student has completed.
	 */
	Integer getPctMelete();

	/**
	 * @return The student status.
	 */
	ParticipantStatus getStatus();

	/**
	 * @return The date the syllabus was been viewed, or null if not viewed.
	 */
	Date getSyllabusViewed();

	/**
	 * Set the number of missed items based on the course map.
	 * 
	 * @param missed
	 *        The number of items missed.
	 */
	void setNumItemsMissed(Integer missed);
}
