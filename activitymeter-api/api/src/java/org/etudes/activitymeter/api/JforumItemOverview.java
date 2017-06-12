/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-api/api/src/java/org/etudes/activitymeter/api/JforumItemOverview.java $
 * $Id: JforumItemOverview.java 1974 2011-09-06 16:46:25Z ggolden $
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

/**
 * JforumItemOverview shows overview information for JForum items.
 */
public interface JforumItemOverview
{
	/**
	 * @return The item's access status.
	 */
	ItemAccessStatus getAccessStatus();

	/**
	 * @return The JForum id of the item.
	 */
	String getId();

	/**
	 * @return The number of "Student" participants who have posted to this item.
	 */
	Integer getNumPosters();

	/**
	 * @return The number of posts to this item.
	 */
	Integer getNumPosts();

	/**
	 * @return The item's status.
	 */
	ItemStatus getStatus();

	/**
	 * @return The title of the item.
	 */
	String getTitle();
}
