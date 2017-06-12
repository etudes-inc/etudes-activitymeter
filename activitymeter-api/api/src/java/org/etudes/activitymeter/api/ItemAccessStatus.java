/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-api/api/src/java/org/etudes/activitymeter/api/ItemAccessStatus.java $
 * $Id: ItemAccessStatus.java 8568 2014-08-30 06:26:16Z mallikamt $
 ***********************************************************************************
 *
 * Copyright (c) 2010, 2011, 2014 Etudes, Inc.
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
 * ItemStatus describes a tool item's status.
 */
public enum ItemAccessStatus
{
	closed(), denyAccess(), invalid(), notOpen(), hiddenUntilOpen(), open(), unpublished();
}
