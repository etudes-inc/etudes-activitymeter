/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/SiteConnector.java $
 * $Id: SiteConnector.java 12543 2016-01-13 18:30:32Z rashmim $
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

package org.etudes.activitymeter.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.activitymeter.api.ParticipantSiteOverview;
import org.etudes.activitymeter.api.ParticipantStatus;
import org.etudes.activitymeter.api.SiteInfo;
import org.etudes.activitymeter.api.SiteOverview;
import org.etudes.activitymeter.api.SiteVisit;
import org.etudes.activitymeter.api.SiteVisitService;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;

/**
 * SiteConnector are methods used by the Overview service to get Site data.
 */
public class SiteConnector
{
	/** Our logger. */
	private static Log M_log = LogFactory.getLog(SiteConnector.class);

	/** Dependency: SiteVisitService. */
	protected SiteVisitService siteVisitService = null;

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
	 * Check with Site to find the first visits
	 * 
	 * @param context
	 *        The site id.
	 * @return The map, keyed by user id, data is the visit date.
	 */
	public Map<String, Date> getFirstVisits(String context, List<Participant> participants)
	{
		// TODO: Note: this is called along with getLastVisits and getNumVisits, but we want these in separate maps.
		// for now, lets just implement each, but we might want to work out an efficiency for this -ggolden.

		// TODO:
		Map<String, Date> rv = new HashMap<String, Date>();

		// Get all visits for this site
		Map<String, SiteVisit> svMap = this.siteVisitService.getVisits(context);

		// Iterate through each participant and get visit dates for them
		for (Participant p : participants)
		{
			SiteVisit visit = svMap.get(p.userId);
			Date firstVisitDate = visit == null ? null : visit.getFirstSiteVisit();
			rv.put(p.userId, firstVisitDate);
		}

		return rv;
	}

	/**
	 * Check with Site to find the last visits
	 * 
	 * @param context
	 *        The site id.
	 * @return The map, keyed by user id, data is the visit date.
	 */
	public Map<String, Date> getLastVisits(String context, List<Participant> participants)
	{
		// TODO: Note: this is called along with getFirstVisits and getNumVisits, but we want these in separate maps.
		// for now, lets just implement each, but we might want to work out an efficiency for this -ggolden.

		// TODO:
		Map<String, Date> rv = new HashMap<String, Date>();

		// Get all visits for this site
		Map<String, SiteVisit> svMap = this.siteVisitService.getVisits(context);

		// Iterate through each participant and get visit dates for them
		for (Participant p : participants)
		{
			SiteVisit visit = svMap.get(p.userId);
			Date lastVisitDate = visit == null ? null : visit.getLastSiteVisit();
			rv.put(p.userId, lastVisitDate);
		}

		return rv;
	}

	/**
	 * Get the participants who have not visited this site in the last <period> days. Use only enrolled status participants.
	 * 
	 * @param context
	 *        The site id.
	 * @param period
	 *        The # date.
	 * @return A List of Participants who are enrolled and do not have a site visit in the time period of now through <period> days ago.
	 */
	public List<Participant> getNotVisitedInPeriod(String context, Integer period, List<Participant> participants)
	{
		// get the most recent site visits
		Map<String, Date> visits = getLastVisits(context, participants);

		List<Participant> rv = new ArrayList<Participant>();

		// compute the cutoff date for "recent"
		Calendar cutoff = Calendar.getInstance();
		cutoff.add(Calendar.DATE, -1 * period.intValue());
		Date cutoffDate = cutoff.getTime();

		// filter the participants
		for (Participant p : participants)
		{
			// only consider currently enrolled participants
			if (p.status == ParticipantStatus.enrolled || p.status == ParticipantStatus.added)
			{
				// who have never visited or visited before the cutoff
				Date lastVisit = visits.get(p.userId);
				if ((lastVisit == null) || (lastVisit.before(cutoffDate)))
				{
					rv.add(p);
				}
			}
		}

		return rv;
	}

	/**
	 * Get the user ids who have visited this site in the last <period> days. Use only enrolled status participants.
	 * 
	 * @param context
	 *        The site id.
	 * @param period
	 *        The # date.
	 * @param activeParticipantIds
	 *        The set userId strings for the active users in the site.
	 * @return A set of user ids who are enrolled and have a site visit in the time period of now through <period> days ago.
	 */
	public Set<String> getVisitedInPeriod(String context, Integer period, Set<String> activeParticipantIds)
	{
		// Get all visits for this site
		Map<String, SiteVisit> svMap = this.siteVisitService.getVisits(context);

		Set<String> rv = new HashSet<String>();

		// compute the cutoff date for "recent"
		Calendar cutoff = Calendar.getInstance();
		cutoff.add(Calendar.DATE, -1 * period.intValue());
		Date cutoffDate = cutoff.getTime();

		if (activeParticipantIds.isEmpty()) return rv;

		for (String userId : activeParticipantIds)
		{
			SiteVisit visit = svMap.get(userId);
			Date lastVisit = visit == null ? null : visit.getLastSiteVisit();
			if ((lastVisit != null) && (lastVisit.after(cutoffDate)))
			{
				rv.add(userId);
			}
		}

		return rv;
	}

	/**
	 * Check with Site to find the number of visits per user to the site.
	 * 
	 * @param context
	 *        The site id.
	 * @return The map, keyed by user id, data is the count.
	 */
	public Map<String, Integer> getNumVisits(String context, List<Participant> participants)
	{
		// TODO: Note: this is called along with getLastVisits and getFirstVisits, but we want these in separate maps.
		// for now, lets just implement each, but we might want to work out an efficiency for this -ggolden.

		// TODO:
		Map<String, Integer> rv = new HashMap<String, Integer>();

		// Get all visits for this site
		Map<String, SiteVisit> svMap = this.siteVisitService.getVisits(context);

		// Iterate through each participant and get visit dates for them
		for (Participant p : participants)
		{
			SiteVisit visit = svMap.get(p.userId);
			Integer numVisits = visit == null ? null : visit.getVisits();
			rv.put(p.userId, numVisits);
		}

		return rv;
	}

	/**
	 * Get overview information for the site for all users.
	 * 
	 * @param context
	 *        The site id.
	 * @return a Map keyed by userId of ParticipantSiteOverview.
	 */
	public Map<String, ParticipantSiteOverview> getParticipantSiteOverviews(String context, List<Participant> participants)
	{
		Map<String, ParticipantSiteOverview> rv = new HashMap<String, ParticipantSiteOverview>();

		// Note: this is NOT limited to the entries in the participants argument: in fact, probably don't need this argument at all -ggolden
		// Get all visits for this site
		Map<String, SiteVisit> svMap = this.siteVisitService.getVisits(context);

		// create the ParticipantSiteOverview with all needed information
		for (Participant p : participants)
		{
			SiteVisit visit = svMap.get(p.userId);
			Date firstVisitDate = visit == null ? null : visit.getFirstSiteVisit();
			Date lastVisitDate = visit == null ? null : visit.getLastSiteVisit();
			Integer numVisits = visit == null ? null : visit.getVisits();
			ParticipantSiteOverview pso = new ParticipantSiteOverviewImpl(p.userId, p.sortName, p.displayId, p.groupTitle, p.status, firstVisitDate, lastVisitDate,
					numVisits);
			rv.put(p.userId, pso);
		}

		return rv;
	}

	/**
	 * Get some display information for the site.
	 * 
	 * @param context
	 *        The site id.
	 * @return The SiteInfo.
	 */
	public SiteInfo getSiteInfo(String context)
	{
		String title = null;
		String term = null;
		try
		{
			Site site = this.siteService.getSite(context);
			title = site.getTitle();
			term = site.getTermDescription();
		}
		catch (IdUnusedException e)
		{
			M_log.warn("SiteConnector.getSiteInfo - IdUnusedException " + context);
		}

		return new SiteInfoImpl(context, term, title);
	}

	/**
	 * Get a overview information for the active users in this site, using this time period for periodic information.
	 * 
	 * @param context
	 *        The site id.
	 * @param period
	 *        The number of days for the period of the overview (see getNumNotVisitedInPeriod())
	 * @param activeParticipantIds
	 *        The set userId strings for the active users in the site.
	 * @return A SiteOverview.
	 */
	public SiteOverview getSiteOverview(String context, Integer period, Set<String> activeParticipantIds)
	{
		// Get all visits for this site
		Map<String, SiteVisit> svMap = this.siteVisitService.getVisits(context);
		int totalVisits = 0;
		int noVisits = 0;
		for (String userId : activeParticipantIds)
		{
			SiteVisit visit = svMap.get(userId);
			if (visit == null)
				noVisits = noVisits + 1;
			else
				totalVisits = totalVisits + 1;
		}
		Set<String> visitSet = getVisitedInPeriod(context, period, activeParticipantIds);
		return new SiteOverviewImpl(context, period, noVisits, totalVisits, visitSet.size());
	}

	/**
	 * @return the siteVisitService
	 */
	public SiteVisitService getSiteVisitService()
	{
		return siteVisitService;
	}

	/**
	 * @return the siteService
	 */
	public SiteService getSiteService()
	{
		return siteService;
	}

	/**
	 * Final initialization
	 */
	public void init()
	{
		M_log.info("init()");
	}

	/**
	 * @param siteVisitService
	 *        the siteVisitService to set
	 */
	public void setSiteVisitService(SiteVisitService siteVisitService)
	{
		this.siteVisitService = siteVisitService;
	}

	/**
	 * @param siteService
	 *        the siteService to set
	 */
	public void setSiteService(SiteService siteService)
	{
		this.siteService = siteService;
	}
}
