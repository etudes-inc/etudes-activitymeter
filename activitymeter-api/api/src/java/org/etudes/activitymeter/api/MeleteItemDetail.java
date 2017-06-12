/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-api/api/src/java/org/etudes/activitymeter/api/MeleteItemDetail.java $
 * $Id: MeleteItemDetail.java 1466 2011-05-09 23:09:30Z ggolden $
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
 * MeleteItemDetail shows melete section detail information
 */
public interface MeleteItemDetail
{
	/**
	 * @return The item's status.
	 */
	ItemAccessStatus getAccessStatus();

	/**
	 * @return The close date.
	 */
	Date getCloseDate();

	/**
	 * @return A display title for the section's module.
	 */
	String getModuleTitle();

	/**
	 * @return The number of "Student" participants who have viewed this section. TODO: drop
	 */
	Integer getNumViewed();

	/**
	 * @return The open date.
	 */
	Date getOpenDate();

	/**
	 * @return The percent (0..100) of "Student" participants who have viewed this section.
	 */
	Integer getPctViewed();

	/**
	 * @return A display title for the section.
	 */
	String getTitle();

	/**
	 * @return The total number of "Student" participants who have access to this section. TODO: drop
	 */
	Integer getTotal();
}
