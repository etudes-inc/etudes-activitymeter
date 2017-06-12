/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-api/api/src/java/org/etudes/activitymeter/api/Overview.java $
 * $Id: Overview.java 12131 2015-11-25 15:34:48Z mallikamt $
 ***********************************************************************************
 *
 * Copyright (c) 2011, 2015 Etudes, Inc.
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
 * Overview holds overview information for the site
 */
public interface Overview
{
	/**
	 * @return The number of active participants with the "Blocked" role.
	 */
	Integer getNumBlocked();

	/**
	 * @return The number of inactive participants with the "Student" role.
	 */
	Integer getNumDropped();

	/**
	 * @return The number of active participants with the "Student" role.
	 */
	Integer getNumEnrolled();

	/**
	 * @return The number of added participants with the "Student" role.
	 */
	Integer getNumAdded();


	/**
	 * @return A count of qualified participants who have "site.visit" (active), are in the "Student" role, and have not had a site visit in the time period established for this overview or null if there were none.
	 */
	Integer getNumNotVisitedInPeriod();
}
