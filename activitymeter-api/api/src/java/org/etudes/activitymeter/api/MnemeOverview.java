/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-api/api/src/java/org/etudes/activitymeter/api/MnemeOverview.java $
 * $Id: MnemeOverview.java 9344 2014-11-25 00:12:42Z ggolden $
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

package org.etudes.activitymeter.api;

/**
 * MnemeOverview shows overview information for mneme.
 */
public interface MnemeOverview
{
	/**
	 * @return The total number submissions to assessments of type "assignment"
	 */
	Integer getNumAssignmentSubmissions();

	/**
	 * @return The total number submissions in-progress to assessments of type "assignment"
	 */
	Integer getNumAssignmentSubmissionsInProgress();

	/**
	 * @return The total number of offline "submissions" (i.e. those created in grading for evaluations).
	 */
	Integer getNumOfflineSubmissions();

	/**
	 * @return The total number submissions to assessments of type "survey"
	 */
	Integer getNumSurveySubmissions();

	/**
	 * @return The total number submissions in-progress to assessments of type "survey"
	 */
	Integer getNumSurveySubmissionsInProgress();

	/**
	 * @return The total number submissions to assessments of type "test"
	 */
	Integer getNumTestSubmissions();

	/**
	 * @return The total number submissions in-progress to assessments of type "test"
	 */
	Integer getNumTestSubmissionsInProgress();
}
