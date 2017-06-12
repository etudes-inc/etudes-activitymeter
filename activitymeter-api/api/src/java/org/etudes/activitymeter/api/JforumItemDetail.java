/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-api/api/src/java/org/etudes/activitymeter/api/JforumItemDetail.java $
 * $Id: JforumItemDetail.java 3554 2012-11-26 22:30:49Z murthyt $
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
 * JforumItemDetail shows JForum item (category, forum or topic) detail information
 */
public interface JforumItemDetail
{
	/**
	 * @return The item's status.
	 */
	ItemAccessStatus getAccessStatus();

	/**
	 * @return The allow until date
	 */
	Date getAllowUntilDate();

	/**
	 * @return The close date.
	 */
	Date getCloseDate();

	/**
	 * @return The open date.
	 */
	Date getOpenDate();
	
	/**
	 * @return The percent (0..100) of "Student" participants who have never posted.
	 */
	Integer getPctNeverPosted();

	/**
	 * @return The percent (0..100) of "Student" participants who have posted at least once.
	 */
	Integer getPctPostedOnce();
	
	/**
	 * @return A display title for the item.
	 */
	String getTitle();
}
