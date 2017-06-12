/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-api/api/src/java/org/etudes/activitymeter/api/ParticipantSiteOverviewsSort.java $
 * $Id: ParticipantSiteOverviewsSort.java 1245 2011-03-09 20:57:03Z ggolden $
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
 * ParticipantSiteOverviewsSort are the sort options for the OverviewService's getParticipantSiteOverviews.
 */
public enum ParticipantSiteOverviewsSort implements Comparator<ParticipantSiteOverview>
{
	firstVisit_a("2", "A"), firstVisit_d("2", "D"), lastVisit_a("3", "A"), lastVisit_d("3", "D"), name_a("0", "A"), name_d("0", "D"), siteVisits_a(
			"4", "A"), siteVisits_d("4", "D"), status_a("1", "A"), status_d("1", "D");

	static public ParticipantSiteOverviewsSort sortFromCodes(String sortCode, String sortDirection)
	{
		for (ParticipantSiteOverviewsSort s : ParticipantSiteOverviewsSort.values())
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

	private ParticipantSiteOverviewsSort(String sortCode, String sortDirection)
	{
		this.sortCode = sortCode;
		this.sortDirection = sortDirection;
	}

	public int compare(ParticipantSiteOverview arg0, ParticipantSiteOverview arg1)
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
			case firstVisit_a:
			case firstVisit_d:
			{
				rv = CompareHelper.dateCompare(arg0.getFirstVisitDate(), arg1.getFirstVisitDate());
				break;
			}
			case lastVisit_a:
			case lastVisit_d:
			{
				rv = CompareHelper.dateCompare(arg0.getLastVisitDate(), arg1.getLastVisitDate());
				break;
			}
			case siteVisits_a:
			case siteVisits_d:
			{
				rv = CompareHelper.integerCompare(arg0.getNumVisits(), arg1.getNumVisits());
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
