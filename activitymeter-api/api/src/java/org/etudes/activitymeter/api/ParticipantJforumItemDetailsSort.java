/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-api/api/src/java/org/etudes/activitymeter/api/ParticipantJforumItemDetailsSort.java $
 * $Id: ParticipantJforumItemDetailsSort.java 1660 2011-06-29 19:16:02Z ggolden $
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
 * ParticipantJforumItemDetailsSort are the sort options for the OverviewService's getParticipantJforumItemDetails.
 */
public enum ParticipantJforumItemDetailsSort implements Comparator<ParticipantJforumItemDetail>
{
	name_a("0", "A"), name_d("0", "D"), posts_a("2", "A"), posts_d("2", "D"), status_a("1", "A"), status_d("1", "D"), reviewed_a("3", "A"), reviewed_d(
			"3", "D");

	static public ParticipantJforumItemDetailsSort sortFromCodes(String sortCode, String sortDirection)
	{
		for (ParticipantJforumItemDetailsSort s : ParticipantJforumItemDetailsSort.values())
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

	private ParticipantJforumItemDetailsSort(String sortCode, String sortDirection)
	{
		this.sortCode = sortCode;
		this.sortDirection = sortDirection;
	}

	public int compare(ParticipantJforumItemDetail arg0, ParticipantJforumItemDetail arg1)
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
			case posts_a:
			case posts_d:
			{
				rv = CompareHelper.integerCompare(arg0.getPosts(), arg1.getPosts());
				break;
			}
			case reviewed_a:
			case reviewed_d:
			{
				rv = CompareHelper.dateCompare(arg0.getReviewed(), arg1.getReviewed());
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
