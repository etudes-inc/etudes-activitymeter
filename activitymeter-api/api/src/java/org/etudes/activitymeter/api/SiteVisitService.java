/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-api/api/src/java/org/etudes/activitymeter/api/SiteVisitService.java $
 * $Id: SiteVisitService.java 6034 2013-09-25 20:53:05Z ggolden $
 ***********************************************************************************
 *
 * Copyright (c) 2010, 2013 Etudes, Inc.
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

import java.util.Map;

/**
 * SiteVisitService provides API support for the site visit table.
 */
public interface SiteVisitService
{

	/**
	 * Get the Service visit (i.e. login) data for this user
	 * 
	 * @param userId
	 *        The user Id.
	 * @return The SiteVisit object for the user, or null if the user has no data.
	 */
	SiteVisit getServiceVisit(String userId);

	/**
	 * Get all site visit records for this site
	 * 
	 * @param context
	 *        The site id.
	 * @return The map, keyed by user id, data consists of SiteVisit object.
	 */
	Map<String, SiteVisit> getVisits(String context);

	/**
	 * Remove the user service visit entry for this user.
	 * 
	 * @param userId
	 *        The internal user id.
	 */
	void removeUserServiceVisits(String userId);
}
