/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-api/api/src/java/org/etudes/activitymeter/api/MeleteItemOverview.java $
 * $Id: MeleteItemOverview.java 12563 2016-01-15 19:59:22Z rashmim $
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
 * MeleteItemOverview shows overview information for Melete items.
 */
public interface MeleteItemOverview
{
	/**
	 * @return The Melete id of the item.
	 */
	String getId();

	/**
	 * @return TRUE if the item is a module, FALSE for section.
	 */
	Boolean getIsModule();

	/**
	 * @return The # viewers for this section, or null of this is for a module.
	 */
	Integer getNumViewers();

	/**
	 * @return The item's status.
	 */
	ItemStatus getStatus();

	/**
	 * @return The module or section title.
	 */
	String getTitle();
	
	/**
	 * @return The item's status.
	 */
	ItemAccessStatus getAccessStatus();
}
