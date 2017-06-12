/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-api/api/src/java/org/etudes/activitymeter/api/MnemeItemDetail.java $
 * $Id: MnemeItemDetail.java 2512 2012-01-14 19:48:50Z ggolden $
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

import org.etudes.mneme.api.AssessmentType;

/**
 * MnemeItemDetail shows Mneme assessment details.
 */
public interface MnemeItemDetail
{
	/**
	 * @return The assessment accept until date.
	 */
	Date getAcceptUntilDate();

	/**
	 * @return The item's status.
	 */
	ItemAccessStatus getAccessStatus();

	/**
	 * @return The assessment due date.
	 */
	Date getDueDate();

	/**
	 * @return The number of "Student" participants who have a submission in progress.
	 */
	Integer getNumInProgress();

	/**
	 * @return The number of "Student" participants who have not yet started a submission.
	 */
	Integer getNumNotSubmitted();

	/**
	 * @return The number of "Student" participants who have submitted at least once. TODO: drop
	 */
	Integer getNumSubmitted();

	/**
	 * @return The assessment open date.
	 */
	Date getOpenDate();

	/**
	 * @return The percent (0..100) of "Student" participants who have submitted at least once.
	 */
	Integer getPctSubmitted();

	/**
	 * @return A display title for the item.
	 */
	String getTitle();

	/**
	 * @return The total number of "Student" participants. TODO: drop
	 */
	Integer getTotalParticipants();

	/**
	 * @return The assessment type.
	 */
	AssessmentType getType();
}
