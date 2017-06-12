/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-api/api/src/java/org/etudes/activitymeter/api/ParticipantJforumItemDetail.java $
 * $Id: ParticipantJforumItemDetail.java 1660 2011-06-29 19:16:02Z ggolden $
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
 * ParticipantJforumItemDetail shows JForum item (category, forum or topic) specific detail information for a participant.
 */
public interface ParticipantJforumItemDetail
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
	 * @return The user id.
	 */
	String getId();

	/**
	 * @return The number of posts.
	 */
	Integer getPosts();

	/**
	 * @return The date the user has reviewed the evaluated submission, or null if not reviewed.
	 */
	Date getReviewed();

	/**
	 * @return The sort name for this user.
	 */
	String getSortName();

	/**
	 * @return The enrollment status.
	 */
	ParticipantStatus getStatus();
}
