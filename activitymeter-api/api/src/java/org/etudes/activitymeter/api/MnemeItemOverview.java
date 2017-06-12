/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-api/api/src/java/org/etudes/activitymeter/api/MnemeItemOverview.java $
 * $Id: MnemeItemOverview.java 8568 2014-08-30 06:26:16Z mallikamt $
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
 * MnemeItemOverview shows overview information for Mneme items.
 */
public interface MnemeItemOverview
{
	/**
	 * @return The item's access status.
	 */
	ItemAccessStatus getAccessStatus();
	
	/**
	 * @return The Mneme id of the item.
	 */
	String getId();

	/**
	 * @return The number of submissions in-progress to this item.
	 */
	Integer getNumInProgress();

	/**
	 * @return The number of submissions (complete) to this item.
	 */
	Integer getNumSubmissions();

	/**
	 * @return The number of "Student" participants who have submitted to this item.
	 */
	Integer getNumSubmitters();

	/**
	 * @return The item's status.
	 */
	ItemStatus getStatus();

	/**
	 * @return The title of the item.
	 */
	String getTitle();

	/**
	 * @return The Mneme type string for the item.
	 */
	String getType();
}
