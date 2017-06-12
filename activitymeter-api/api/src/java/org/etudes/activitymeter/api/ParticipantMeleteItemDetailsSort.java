/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-api/api/src/java/org/etudes/activitymeter/api/ParticipantMeleteItemDetailsSort.java $
 * $Id: ParticipantMeleteItemDetailsSort.java 2978 2012-06-13 15:34:19Z ggolden $
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

import java.util.Comparator;

/**
 * ParticipantMeleteItemDetailsSort are the sort options for the OverviewService's getParticipantMeleteItemDetails.
 */
public enum ParticipantMeleteItemDetailsSort implements Comparator<ParticipantMeleteItemDetail>
{
	name_a("0", "A"), name_d("0", "D"), status_a("1", "A"), status_d("1", "D"), firstViewed_a("2", "A"), firstViewed_d("2", "D"), viewed_a("3", "A"), viewed_d("3", "D");

	static public ParticipantMeleteItemDetailsSort sortFromCodes(String sortCode, String sortDirection)
	{
		for (ParticipantMeleteItemDetailsSort s : ParticipantMeleteItemDetailsSort.values())
		{
			if (s.getSortCode().equals(sortCode) && s.getSortDirection().equals(sortDirection))
			{
				return s;
			}
		}
		return null;
	}

	private String sortCode = null;

	private String sortDirection = null;

	private ParticipantMeleteItemDetailsSort(String sortCode, String sortDirection)
	{
		this.sortCode = sortCode;
		this.sortDirection = sortDirection;
	}

	public int compare(ParticipantMeleteItemDetail arg0, ParticipantMeleteItemDetail arg1)
	{
		int rv = 0;

		// primary sort
		switch (this)
		{
			case name_a:
			case name_d:
			{
				rv = CompareHelper.stringCompare(arg0.getSortName(), arg1.getSortName());
				break;
			}
			case status_a:
			case status_d:
			{
				rv = CompareHelper.participantStatusCompare(arg0.getStatus(), arg1.getStatus());
				break;
			}
			case firstViewed_a:
			case firstViewed_d:
			{
				rv = CompareHelper.dateCompare(arg0.getFirstViewed(), arg1.getFirstViewed());
				break;
			}
			case viewed_a:
			case viewed_d:
			{
				rv = CompareHelper.dateCompare(arg0.getViewed(), arg1.getViewed());
				break;
			}
		}

		// secondary sort (on name)
		if (rv == 0)
		{
			rv = CompareHelper.stringCompare(arg0.getSortName(), arg1.getSortName());
		}

		// descending, reverse
		if ("D".equals(getSortDirection()))
		{
			rv = -1 * rv;
		}

		return rv;
	}

	public String getSortCode()
	{
		return this.sortCode;
	}

	public String getSortDirection()
	{
		return this.sortDirection;
	}
}
