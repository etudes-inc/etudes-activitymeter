/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-api/api/src/java/org/etudes/activitymeter/api/ParticipantOverviewsSort.java $
 * $Id: ParticipantOverviewsSort.java 1467 2011-05-09 23:45:06Z ggolden $
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
 * ParticipantOverviewsSort are the sort options for the OverviewService's getParticipantOverviews.
 */
public enum ParticipantOverviewsSort implements Comparator<ParticipantOverview>
{
	firstVisit_a("2", "A"), firstVisit_d("2", "D"), jforum_a("7", "A"), jforum_d("7", "D"), lastVisit_a("3", "A"), lastVisit_d("3", "D"), melete_a(
			"6", "A"), melete_d("6", "D"), mneme_a("8", "A"), mneme_d("8", "D"), name_a("0", "A"), name_d("0", "D"), siteVisits_a("4", "A"), siteVisits_d(
			"4", "D"), status_a("1", "A"), status_d("1", "D"), syllabus_a("5", "A"), syllabus_d("5", "D"), section_a("9", "A"), section_d("9", "D");

	static public ParticipantOverviewsSort sortFromCodes(String sortCode, String sortDirection)
	{
		for (ParticipantOverviewsSort s : ParticipantOverviewsSort.values())
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

	private ParticipantOverviewsSort(String sortCode, String sortDirection)
	{
		this.sortCode = sortCode;
		this.sortDirection = sortDirection;
	}

	public int compare(ParticipantOverview arg0, ParticipantOverview arg1)
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
			case syllabus_a:
			case syllabus_d:
			{
				rv = CompareHelper.dateCompare(arg0.getSyllabusDate(), arg1.getSyllabusDate());
				break;
			}
			case melete_a:
			case melete_d:
			{
				rv = CompareHelper.integerCompare(arg0.getNumMeleteViews(), arg1.getNumMeleteViews());
				break;
			}
			case jforum_a:
			case jforum_d:
			{
				rv = CompareHelper.integerCompare(arg0.getNumJforumPosts(), arg1.getNumJforumPosts());
				break;
			}
			case mneme_a:
			case mneme_d:
			{
				rv = CompareHelper.integerCompare(arg0.getNumMnemeSubmissions(), arg1.getNumMnemeSubmissions());
				break;
			}
			case section_a:
			case section_d:
			{
				rv = CompareHelper.stringCompare(arg0.getGroupTitle(), arg1.getGroupTitle());
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
