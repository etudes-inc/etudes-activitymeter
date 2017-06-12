/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-api/api/src/java/org/etudes/activitymeter/api/ParticipantMnemeItemDetail.java $
 * $Id: ParticipantMnemeItemDetail.java 3711 2012-12-06 00:55:31Z ggolden $
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
 * ParticipantMnemeItemDetail shows Mneme item (assessment) specific detail information for a participant.
 */
public interface ParticipantMnemeItemDetail
{
	/**
	 * @return The date of the latest evaluation of the submission, or null if not evaluated.
	 */
	Date getEvaluated();

	/**
	 * @return TRUE if there is an evaluation date, and there was no review, or the review was before the evaluation.
	 */
	Boolean getEvaluationNotReviewed();

	/**
	 * @return TRUE if there is an evaluation date, and a reviewed date, and the review was after the evaluation.
	 */
	Boolean getEvaluationReviewed();

	/**
	 * @return The date the user completed the "best" submission, or null if there is no submission.
	 */
	Date getFinishedDate();

	/**
	 * @return The user id.
	 */
	String getId();

	/**
	 * @return TRUE if the user is in-progress with a submission to this assessment, FALSE if not.
	 */
	Boolean getInProgress();

	/**
	 * @return The date the user has reviewed the evaluated submission, or null if not reviewed.
	 */
	Date getReviewed();

	/**
	 * @return The sort name for this user.
	 */
	String getSortName();

	/**
	 * @return The date the user started the "best" submission, or null if there is no submission.
	 */
	Date getStartedDate();

	/**
	 * @return The enrollment status.
	 */
	ParticipantStatus getStatus();

	/**
	 * @return TRUE to suppress display of the started and finished dates, FALSE otherwise.
	 */
	Boolean getSuppressDates();
}
