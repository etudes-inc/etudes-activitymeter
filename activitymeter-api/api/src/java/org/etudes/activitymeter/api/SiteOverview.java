/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-api/api/src/java/org/etudes/activitymeter/api/SiteOverview.java $
 * $Id: SiteOverview.java 1183 2011-02-22 00:01:52Z ggolden $
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
 * SiteOverview shows overview information for the site
 */
public interface SiteOverview
{
	/**
	 * @return A count of "Student" participants (enrolled, blocked or dropped) who have never visited the site.
	 */
	Integer getNumNeverVisited();

	/**
	 * @return A count of the "Student" participants (enrolled, blocked or dropped) who have visited at least once.
	 */
	Integer getNumVisited();

	/**
	 * @return A count of "Student" participants (enrolled, blocked or dropped) who have visited the site in the period.
	 */
	Integer getNumVisitedInPeriod();
}
