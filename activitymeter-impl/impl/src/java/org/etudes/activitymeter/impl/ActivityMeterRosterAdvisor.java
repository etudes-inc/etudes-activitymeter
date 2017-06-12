/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/ActivityMeterRosterAdvisor.java $
 * $Id: ActivityMeterRosterAdvisor.java 12131 2015-11-25 15:34:48Z mallikamt $
 ***********************************************************************************
 *
 * Copyright (c) 2011, 2012, 2015 Etudes, Inc.
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

package org.etudes.activitymeter.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.activitymeter.api.OverviewService;
import org.etudes.activitymeter.api.ParticipantJforumItemDetail;
import org.etudes.activitymeter.api.ParticipantJforumItemDetailsSort;
import org.etudes.activitymeter.api.ParticipantMeleteItemDetail;
import org.etudes.activitymeter.api.ParticipantMeleteItemDetailsSort;
import org.etudes.activitymeter.api.ParticipantMnemeItemDetail;
import org.etudes.activitymeter.api.ParticipantMnemeItemDetailsSort;
import org.etudes.activitymeter.api.ParticipantOverview;
import org.etudes.activitymeter.api.ParticipantOverviewsSort;
import org.etudes.activitymeter.api.ParticipantStatus;
import org.etudes.activitymeter.api.ParticipantSyllabusOverview;
import org.etudes.activitymeter.api.ParticipantSyllabusOverviewsSort;
import org.etudes.util.api.RosterAdvisor;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.util.StringUtil;

/**
 * CourseMapAccessAdvisor implements AccessAdvisor
 */
public class ActivityMeterRosterAdvisor implements RosterAdvisor
{
	/** Our logger. */
	private static Log M_log = LogFactory.getLog(ActivityMeterRosterAdvisor.class);

	/** Dependency: OverviewService. */
	protected OverviewService overviewService = null;

	/** Dependency: SiteService. */
	protected SiteService siteService = null;

	/**
	 * Returns to uninitialized state.
	 */
	public void destroy()
	{
		M_log.info("destroy()");
	}

	/**
	 * {@inheritDoc}
	 */
	public String encode(String context, RosterAccessType type, String id)
	{
		String rv = null;
		switch (type)
		{
			case user:
			{
				rv = "S:" + context + ":U:" + id;
				break;
			}
			case alert:
			{
				rv = "S:" + context + ":A";
				break;
			}
			case syllabus:
			{
				rv = "S:" + context + ":Y";
				break;
			}
			case melete:
			{
				rv = "S:" + context + ":M:" + id;
				break;
			}
			case jforum:
			{
				rv = "S:" + context + ":J:" + id;
				break;
			}
			case mneme:
			{
				rv = "S:" + context + ":N:" + id;
				break;
			}
			case neverVisit:
			{
				rv = "S:" + context + ":X";
				break;
			}
		}

		return rv;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getUsers(String accessCode)
	{
		List<String> rv = new ArrayList<String>();

		// parse by :
		String[] parts = StringUtil.split(accessCode, ":");
		if (parts.length >= 3)
		{
			// [0] is "S" for site
			// [1] is the site id
			String context = parts[1];
			// [2] is the type
			char type = parts[2].charAt(0);
			switch (type)
			{
				case 'U':
				{
					// [3] is the single user id
					if (parts.length == 4)
					{
						rv.add(parts[3]);
					}
					break;
				}
				case 'A':
				{
					getAlertUsers(context, rv);
					break;
				}
				case 'X':
				{
					getNeverVisitedUsers(context, rv);
					break;
				}
				case 'Y':
				{
					getSyllabusUsers(context, rv);
					break;
				}
				case 'M':
				{
					getMeleteUsers(context, parts[3], rv);
					break;
				}
				case 'J':
				{
					getJforumUsers(context, parts[3], rv);
					break;
				}
				case 'N':
				{
					getMnemeUsers(context, parts[3], rv);
					break;
				}
			}
		}

		return rv;
	}

	/**
	 * Final initialization, once all dependencies are set.
	 */
	public void init()
	{
		M_log.info("init()");
	}

	/**
	 * Set the OverviewService.
	 * 
	 * @param service
	 *        the OverviewService.
	 */
	public void setOverviewService(OverviewService service)
	{
		this.overviewService = service;
	}

	/**
	 * Set the SiteService.
	 * 
	 * @param service
	 *        The SiteService.
	 */
	public void setSiteService(SiteService service)
	{
		this.siteService = service;
	}

	/**
	 * Find the alert users.
	 * 
	 * @param context
	 *        The site id.
	 * @param rv
	 *        The list to fill.
	 */
	protected void getAlertUsers(String context, List<String> rv)
	{
		// get the list of students who have not visited in period (7 days)
		List<ParticipantOverview> participantOverviews = this.overviewService.getParticipantsNotVisited(context, Integer.valueOf(7),
				ParticipantOverviewsSort.name_a);
		for (ParticipantOverview p : participantOverviews)
		{
			rv.add(p.getId());
		}
	}

	/**
	 * Find the JForum users.
	 * 
	 * @param context
	 *        The site id.
	 * @param id
	 *        The item id.
	 * @param rv
	 *        The list to fill.
	 */
	protected void getJforumUsers(String context, String id, List<String> rv)
	{
		List<ParticipantJforumItemDetail> participantDetails = this.overviewService.getParticipantJforumItemDetails(context, id,
				ParticipantJforumItemDetailsSort.name_a);
		for (ParticipantJforumItemDetail p : participantDetails)
		{
			if ((p.getStatus() == ParticipantStatus.enrolled || p.getStatus() == ParticipantStatus.added) && (p.getPosts() == null) || (p.getPosts().intValue() == 0))
			{
				rv.add(p.getId());
			}
		}
	}

	/**
	 * Find the melete users.
	 * 
	 * @param context
	 *        The site id.
	 * @param id
	 *        The item id.
	 * @param rv
	 *        The list to fill.
	 */
	protected void getMeleteUsers(String context, String id, List<String> rv)
	{
		List<ParticipantMeleteItemDetail> participantDetails = this.overviewService.getParticipantMeleteItemDetails(context, id,
				ParticipantMeleteItemDetailsSort.name_a);
		for (ParticipantMeleteItemDetail p : participantDetails)
		{
			if ((p.getStatus() == ParticipantStatus.enrolled || p.getStatus() == ParticipantStatus.added) && (p.getViewed() == null))
			{
				rv.add(p.getId());
			}
		}
	}

	/**
	 * Find the mneme users.
	 * 
	 * @param context
	 *        The site id.
	 * @param id
	 *        The item id.
	 * @param rv
	 *        The list to fill.
	 */
	protected void getMnemeUsers(String context, String id, List<String> rv)
	{
		List<ParticipantMnemeItemDetail> participantDetails = this.overviewService.getParticipantMnemeItemDetails(context, id,
				ParticipantMnemeItemDetailsSort.name_a);
		for (ParticipantMnemeItemDetail p : participantDetails)
		{
			if ((p.getStatus() == ParticipantStatus.enrolled || p.getStatus() == ParticipantStatus.added) && (p.getStartedDate() == null))
			{
				rv.add(p.getId());
			}
		}
	}

	/**
	 * Find the users who have never visited the site.
	 * 
	 * @param context
	 *        The site id.
	 * @param rv
	 *        The list to fill.
	 */
	protected void getNeverVisitedUsers(String context, List<String> rv)
	{
		// get the list of students who have not visited in period (7 days)
		List<ParticipantOverview> participantOverviews = this.overviewService.getParticipantsNotVisited(context, Integer.valueOf(7),
				ParticipantOverviewsSort.name_a);
		for (ParticipantOverview p : participantOverviews)
		{
			// filter out those who have a first visit
			if (p.getFirstVisitDate() != null) continue;

			rv.add(p.getId());
		}
	}

	/**
	 * Find the syllabus users.
	 * 
	 * @param context
	 *        The site id.
	 * @param rv
	 *        The list to fill.
	 */
	protected void getSyllabusUsers(String context, List<String> rv)
	{
		List<ParticipantSyllabusOverview> participantOverviews = this.overviewService.getParticipantSyllabusOverviews(context,
				ParticipantSyllabusOverviewsSort.name_a);
		for (ParticipantSyllabusOverview p : participantOverviews)
		{
			if ((p.getStatus() == ParticipantStatus.enrolled || p.getStatus() == ParticipantStatus.added) && (p.getSyllabusDate() == null))
			{
				rv.add(p.getId());
			}
		}
	}
}
