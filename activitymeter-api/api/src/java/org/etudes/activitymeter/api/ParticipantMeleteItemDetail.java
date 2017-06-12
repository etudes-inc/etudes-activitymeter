/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-api/api/src/java/org/etudes/activitymeter/api/ParticipantMeleteItemDetail.java $
 * $Id: ParticipantMeleteItemDetail.java 12565 2016-01-15 21:43:32Z rashmim $
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
 * ParticipantMeleteItemDetail shows melete item (section) specific detail information for a participant.
 */
public interface ParticipantMeleteItemDetail
{
	/**
	 * @return The user id.
	 */
	String getId();

	/**
	 * @return The user's display id.
	 */
	String getDisplayId();
	
	/**
	 * @return The user's group title
	 */
	String getGroupTitle();
	
	/**
	 * @return The sort name for this user.
	 */
	String getSortName();

	/**
	 * @return The enrollment status.
	 */
	ParticipantStatus getStatus();

	/**
	 * @return The date the section was first read.
	 */
	Date getFirstViewed();
	
	/**
	 * @return The date the section was last read.
	 */
	Date getViewed();
}
