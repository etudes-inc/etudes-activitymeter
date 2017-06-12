/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/OverviewServiceImpl.java $
 * $Id: OverviewServiceImpl.java 12565 2016-01-15 21:43:32Z rashmim $
 ***********************************************************************************
 *
 * Copyright (c) 2011, 2012, 2013, 2014, 2015 Etudes, Inc.
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
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.activitymeter.api.JforumItemDetail;
import org.etudes.activitymeter.api.JforumItemOverview;
import org.etudes.activitymeter.api.JforumOverview;
import org.etudes.activitymeter.api.MeleteItemDetail;
import org.etudes.activitymeter.api.MeleteItemOverview;
import org.etudes.activitymeter.api.MeleteOverview;
import org.etudes.activitymeter.api.MnemeItemDetail;
import org.etudes.activitymeter.api.MnemeItemOverview;
import org.etudes.activitymeter.api.MnemeOverview;
import org.etudes.activitymeter.api.Overview;
import org.etudes.activitymeter.api.OverviewService;
import org.etudes.activitymeter.api.ParticipantJforumItemDetail;
import org.etudes.activitymeter.api.ParticipantJforumItemDetailsSort;
import org.etudes.activitymeter.api.ParticipantMeleteItemDetail;
import org.etudes.activitymeter.api.ParticipantMeleteItemDetailsSort;
import org.etudes.activitymeter.api.ParticipantMnemeItemDetail;
import org.etudes.activitymeter.api.ParticipantMnemeItemDetailsSort;
import org.etudes.activitymeter.api.ParticipantOverview;
import org.etudes.activitymeter.api.ParticipantOverviewsSort;
import org.etudes.activitymeter.api.ParticipantSiteOverview;
import org.etudes.activitymeter.api.ParticipantSiteOverviewsSort;
import org.etudes.activitymeter.api.ParticipantStatus;
import org.etudes.activitymeter.api.ParticipantSyllabusOverview;
import org.etudes.activitymeter.api.ParticipantSyllabusOverviewsSort;
import org.etudes.activitymeter.api.SiteInfo;
import org.etudes.activitymeter.api.SiteOverview;
import org.etudes.activitymeter.api.StudentOverview;
import org.etudes.activitymeter.api.SyllabusOverview;
import org.etudes.util.api.RosterAdvisor;
import org.sakaiproject.authz.api.Member;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.site.api.Group;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.user.api.UserNotDefinedException;

/**
 * OverviewServiceImpl implements OverviewService.
 */
public class OverviewServiceImpl implements OverviewService
{
	/** Our logger. */
	private static Log M_log = LogFactory.getLog(OverviewServiceImpl.class);

	/** Connector: to JForum. */
	protected JforumConnector jforumConnector = null;

	/** Connector: to Melete. */
	protected MeleteConnector meleteConnector = null;

	/** Connector: to Mneme. */
	protected MnemeConnector mnemeConnector = null;

	/** Dependency: SecurityService */
	protected SecurityService securityService = null;

	/** Connector: to Site. */
	protected SiteConnector siteConnector = null;

	/** Dependency: SiteService. */
	protected SiteService siteService = null;

	/** Connector: to Syllabus. */
	protected SyllabusConnector syllabusConnector = null;

	/** Dependency: UserDirectoryService. */
	protected UserDirectoryService userDirectoryService = null;

	/**
	 * {@inheritDoc}
	 */
	public Boolean allowActivityAccess(String context, String userId)
	{
		if (context == null) throw new IllegalArgumentException();
		if (userId == null) return Boolean.FALSE;

		if (M_log.isDebugEnabled()) M_log.debug("allowActivityAccess: " + context + ": " + userId);

		// check permission - user must have "site.upd" in the context
		boolean ok = checkSecurity(userId, "site.upd", context);

		return ok;
	}

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
	public JforumItemDetail getJforumItemDetail(String context, String itemId)
	{
		// get the active users
		List<Participant> participants = getParticipants(context, false);
		Set<String> actives = getActiveParticipants(participants);

		return this.jforumConnector.getJforumItemDetail(context, itemId, actives);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<JforumItemOverview> getJforumItemOverviews(String context)
	{
		// get the active users
		List<Participant> participants = getParticipants(context, false);
		Set<String> actives = getActiveParticipants(participants);

		return this.jforumConnector.getJforumItemOverviews(context, actives);
	}

	/**
	 * {@inheritDoc}
	 */
	public JforumOverview getJforumOverview(String context)
	{
		// get the active users
		List<Participant> participants = getParticipants(context, false);
		Set<String> actives = getActiveParticipants(participants);

		return this.jforumConnector.getJforumOverview(context, actives);
	}

	/**
	 * {@inheritDoc}
	 */
	public MeleteItemDetail getMeleteItemDetail(String context, String sectionId)
	{
		// get the active users
		List<Participant> participants = getParticipants(context, false);
		Set<String> actives = getActiveParticipants(participants);

		return this.meleteConnector.getMeleteItemDetail(context, sectionId, actives);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<MeleteItemOverview> getMeleteItemOverviews(String context)
	{
		// get the active users
		List<Participant> participants = getParticipants(context, false);
		Set<String> actives = getActiveParticipants(participants);

		return this.meleteConnector.getMeleteItemOverviews(context, actives);
	}

	/**
	 * {@inheritDoc}
	 */
	public MeleteOverview getMeleteOverview(String context)
	{
		// get the active users
		List<Participant> participants = getParticipants(context, false);
		Set<String> actives = getActiveParticipants(participants);

		return this.meleteConnector.getMeleteOverview(context, actives);
	}

	/**
	 * {@inheritDoc}
	 */
	public MnemeItemDetail getMnemeItemDetail(String context, String itemId)
	{
		// get the users
		List<Participant> participants = getParticipants(context, false);
		Set<String> actives = getActiveParticipants(participants);

		return this.mnemeConnector.getMnemeItemDetail(context, itemId, actives);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<MnemeItemOverview> getMnemeItemOverviews(String context)
	{
		// get the users
		List<Participant> participants = getParticipants(context, false);
		Set<String> actives = getActiveParticipants(participants);

		return this.mnemeConnector.getMnemeItemOverviews(context, actives);
	}

	/**
	 * {@inheritDoc}
	 */
	public MnemeOverview getMnemeOverview(String context)
	{
		// get the users
		List<Participant> participants = getParticipants(context, false);
		Set<String> actives = getActiveParticipants(participants);

		return this.mnemeConnector.getMnemeOverview(context, actives);
	}

	/**
	 * {@inheritDoc}
	 */
	public Overview getOverview(String context, Integer period)
	{
		// get all the participants
		List<Participant> participants = getParticipants(context, false);

		// get the no-visit users
		List<Participant> noShows = this.siteConnector.getNotVisitedInPeriod(context, period, participants);

		// count enrollment categories
		int enrolled = 0;
		int added = 0;
		int dropped = 0;
		int blocked = 0;
		for (Participant p : participants)
		{
			if (p.status == ParticipantStatus.enrolled)
			{
				enrolled++;
			}
			if (p.status == ParticipantStatus.added)
			{
				added++;
			}
			else if (p.status == ParticipantStatus.blocked)
			{
				blocked++;
			}
			else if (p.status == ParticipantStatus.dropped)
			{
				dropped++;
			}
		}

		return new OverviewImpl(context, Integer.valueOf(blocked), Integer.valueOf(dropped), Integer.valueOf(enrolled), Integer.valueOf(added), Integer.valueOf(noShows
				.size()), period);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ParticipantJforumItemDetail> getParticipantJforumItemDetails(String context, String itemId, ParticipantJforumItemDetailsSort sort)
	{
		ArrayList<ParticipantJforumItemDetail> rv = new ArrayList<ParticipantJforumItemDetail>();

		// get the users
		List<Participant> participants = getParticipants(context, false);

		rv.addAll(this.jforumConnector.getNumJforumItemPostsEvaluations(context, itemId, participants));

		// sort
		Collections.sort(rv, sort);

		return rv;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ParticipantMeleteItemDetail> getParticipantMeleteItemDetails(String context, String sectionId, ParticipantMeleteItemDetailsSort sort)
	{
		ArrayList<ParticipantMeleteItemDetail> rv = new ArrayList<ParticipantMeleteItemDetail>();

		// get the users
		List<Participant> participants = getParticipants(context, false);

		// get the view dates for each user this item
		Map<String, Date> views = this.meleteConnector.getSectionViews(context, sectionId, participants);

		// get the first view dates for each user this item
		Map<String, Date> firstViews = this.meleteConnector.getSectionFirstViews(context, sectionId, participants);

		// create the ParticipantMeleteItemDetail with all needed information
		for (Participant p : participants)
		{
			ParticipantMeleteItemDetail pmid = new ParticipantMeleteItemDetailImpl(p.userId, p.sortName, p.displayId, p.groupTitle, p.status, firstViews.get(p.userId),
					views.get(p.userId));
			rv.add(pmid);
		}

		// sort
		Collections.sort(rv, sort);

		return rv;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ParticipantMnemeItemDetail> getParticipantMnemeItemDetails(String context, String itemId, ParticipantMnemeItemDetailsSort sort)
	{
		// get the users
		List<Participant> participants = getParticipants(context, false);

		// get the Mneme info for all participants
		List<ParticipantMnemeItemDetail> rv = this.mnemeConnector.getParticipantMnemeItemDetails(context, itemId, participants);

		// sort
		Collections.sort(rv, sort);

		return rv;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ParticipantOverview> getParticipantOverviews(String context, ParticipantOverviewsSort sort, boolean includeAll)
	{
		ArrayList<ParticipantOverview> rv = new ArrayList<ParticipantOverview>();

		// get the users
		List<Participant> participants = getParticipants(context, includeAll);

		// get the first site visits
		Map<String, Date> firstVisits = this.siteConnector.getFirstVisits(context, participants);

		// get the most recent site visits
		Map<String, Date> lastVisits = this.siteConnector.getLastVisits(context, participants);

		// get the number of JForum posts
		Map<String, Integer> jforumPosts = this.jforumConnector.getNumJforumSitePosts(context, participants);

		// get the number of Melete views
		Map<String, Integer> meleteViews = this.meleteConnector.getNumMeleteCompleteModuleViews(context, participants);

		// get the number of Mneme submissions
		Map<String, Integer> mnemeSubmissions = this.mnemeConnector.getNumMnemeSubmissions(context, participants);

		// get the number of site visits
		Map<String, Integer> siteVisits = this.siteConnector.getNumVisits(context, participants);

		// get the syllabus views for the site
		Map<String, Date> syllabusAcceptViews = this.syllabusConnector.getSyllabusAcceptViews(context, participants);

		// create the ParticipantOverviews with all needed information
		for (Participant p : participants)
		{
			ParticipantOverview po = new ParticipantOverviewImpl(firstVisits.get(p.userId), p.userId, lastVisits.get(p.userId),
					jforumPosts.get(p.userId), meleteViews.get(p.userId), mnemeSubmissions.get(p.userId), siteVisits.get(p.userId), p.sortName,
					p.displayId, p.status, syllabusAcceptViews.get(p.userId), p.groupTitle, this.rosterAdvisor.encode(context,
							RosterAdvisor.RosterAccessType.user, p.userId));
			rv.add(po);
		}

		Collections.sort(rv, sort);
		
		//Unless sorting by status, place instructors, observers and ta's at the bottom
		if (sort == ParticipantOverviewsSort.status_a || sort == ParticipantOverviewsSort.status_d) 
		{
			return rv;
		}
		else
		{
			ArrayList<ParticipantOverview> sortedRv = new ArrayList<ParticipantOverview>();
			ArrayList<ParticipantOverview> statusRv = new ArrayList<ParticipantOverview>();
			for (ParticipantOverview po : rv)
			{
				if (po.getStatus() == ParticipantStatus.observer || po.getStatus() == ParticipantStatus.ta || po.getStatus() == ParticipantStatus.instructor)
					statusRv.add(po);
				else sortedRv.add(po);
			}
			
			for (ParticipantOverview po : statusRv)
			{
				sortedRv.add(po);
			}
			return sortedRv;
		}
	}

	/** Dependency: RosterAdvisor. */
	protected RosterAdvisor rosterAdvisor = null;

	/**
	 * Set the RosterAdvisor
	 * 
	 * @param advisor
	 *        The RosterAdvisor
	 */
	public void setRosterAdvisor(RosterAdvisor advisor)
	{
		this.rosterAdvisor = advisor;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ParticipantSiteOverview> getParticipantSiteOverviews(String context, ParticipantSiteOverviewsSort sort)
	{
		ArrayList<ParticipantSiteOverview> rv = new ArrayList<ParticipantSiteOverview>();

		// get the users
		List<Participant> participants = getParticipants(context, true);

		// get the site info
		Map<String, ParticipantSiteOverview> info = this.siteConnector.getParticipantSiteOverviews(context, participants);

		// create the ParticipantSiteOverview with all needed information
		for (Participant p : participants)
		{
			ParticipantSiteOverview pso = info.get(p.userId);

			// if not in the data, make an entry for the participant
			if (pso == null)
			{
				pso = new ParticipantSiteOverviewImpl(p.userId, p.sortName, p.displayId, p.groupTitle, p.status, null, null, Integer.valueOf(0));
			}

			rv.add(pso);
		}

		// sort
		Collections.sort(rv, sort);

		//Unless sorting by status, place instructors, observers and ta's at the bottom
		if (sort == ParticipantSiteOverviewsSort.status_a || sort == ParticipantSiteOverviewsSort.status_d) 
		{
			return rv;
		}
		else
		{
			ArrayList<ParticipantSiteOverview> sortedRv = new ArrayList<ParticipantSiteOverview>();
			ArrayList<ParticipantSiteOverview> statusRv = new ArrayList<ParticipantSiteOverview>();
			for (ParticipantSiteOverview po : rv)
			{
				if (po.getStatus() == ParticipantStatus.observer || po.getStatus() == ParticipantStatus.ta || po.getStatus() == ParticipantStatus.instructor)
					statusRv.add(po);
				else sortedRv.add(po);
			}
			
			for (ParticipantSiteOverview po : statusRv)
			{
				sortedRv.add(po);
			}
			return sortedRv;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ParticipantOverview> getParticipantsNotVisited(String context, Integer period, ParticipantOverviewsSort sort)
	{
		ArrayList<ParticipantOverview> rv = new ArrayList<ParticipantOverview>();

		// get all the participants
		List<Participant> participants = getParticipants(context, false);

		// restrict to those not visited
		List<Participant> noShows = this.siteConnector.getNotVisitedInPeriod(context, period, participants);

		// get the first site visits
		Map<String, Date> firstVisits = this.siteConnector.getFirstVisits(context, noShows);

		// get the most recent site visits
		Map<String, Date> lastVisits = this.siteConnector.getLastVisits(context, noShows);

		// get the number of JForum posts
		Map<String, Integer> jforumPosts = this.jforumConnector.getNumJforumSitePosts(context, noShows);

		// get the number of Melete views
		Map<String, Integer> meleteViews = this.meleteConnector.getNumMeleteCompleteModuleViews(context, noShows);

		// get the number of Mneme submissions
		Map<String, Integer> mnemeSubmissions = this.mnemeConnector.getNumMnemeSubmissions(context, noShows);

		// get the number of site visits
		Map<String, Integer> siteVisits = this.siteConnector.getNumVisits(context, noShows);

		// get the syllabus views for the site
		Map<String, Date> syllabusAcceptViews = this.syllabusConnector.getSyllabusAcceptViews(context, noShows);

		// create the ParticipantOverviews with all needed information
		for (Participant p : noShows)
		{
			ParticipantOverview po = new ParticipantOverviewImpl(firstVisits.get(p.userId), p.userId, lastVisits.get(p.userId),
					jforumPosts.get(p.userId), meleteViews.get(p.userId), mnemeSubmissions.get(p.userId), siteVisits.get(p.userId), p.sortName,
					p.displayId, p.status, syllabusAcceptViews.get(p.userId), p.groupTitle, this.rosterAdvisor.encode(context,
							RosterAdvisor.RosterAccessType.user, p.userId));
			rv.add(po);
		}

		// sort
		Collections.sort(rv, sort);

		return rv;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ParticipantSyllabusOverview> getParticipantSyllabusOverviews(String context, ParticipantSyllabusOverviewsSort sort)
	{
		ArrayList<ParticipantSyllabusOverview> rv = new ArrayList<ParticipantSyllabusOverview>();

		// get the users
		List<Participant> participants = getParticipants(context, false);

		// get the syllabus views for the site
		Map<String, Date> syllabusAcceptViews = this.syllabusConnector.getSyllabusAcceptViews(context, participants);

		// get the syllabus views for the site
		Map<String, Date> syllabusFirstVisitViews = this.syllabusConnector.getSyllabusFirstViews(context, participants);

		// get the syllabus views for the site
		Map<String, Date> syllabusLastVisitViews = this.syllabusConnector.getSyllabusLastViews(context, participants);

		// create the ParticipantSiteOverview with all needed information
		for (Participant p : participants)
		{
			ParticipantSyllabusOverview psyo = new ParticipantSyllabusOverviewImpl(p.userId, p.sortName, p.displayId, p.groupTitle, p.status, syllabusAcceptViews.get(p.userId),
					syllabusFirstVisitViews.get(p.userId), syllabusLastVisitViews.get(p.userId));
			rv.add(psyo);
		}

		// sort
		Collections.sort(rv, sort);

		return rv;
	}

	/**
	 * {@inheritDoc}
	 */
	public SiteInfo getSiteInfo(String context)
	{
		return this.siteConnector.getSiteInfo(context);
	}

	/**
	 * {@inheritDoc}
	 */
	public SiteOverview getSiteOverview(String context, Integer period)
	{
		// get the users
		List<Participant> participants = getParticipants(context, false);
		Set<String> actives = getActiveParticipants(participants);

		return this.siteConnector.getSiteOverview(context, period, actives);
	}

	/**
	 * {@inheritDoc}
	 */
	public StudentOverview getStudentOverview(String context, String userId)
	{
		// get the users
		List<Participant> participants = getParticipants(context, false);
		ParticipantStatus status = ParticipantStatus.dropped;
		for (Participant p : participants)
		{
			if (p.userId.equals(userId))
			{
				status = p.status;
			}
		}

		// get last site visits
		// TODO: need for ONLY one user, not all
		Map<String, Date> lastVisits = this.siteConnector.getLastVisits(context, participants);

		// get JForum posts
		// TODO: need for ONLY one user, not all
		Map<String, Integer> posts = this.jforumConnector.getNumJforumSitePosts(context, participants);

		// get all the syllabus views for the site
		// TODO: need for ONLY one user, not all
		Map<String, Date> syllabusAcceptViews = this.syllabusConnector.getSyllabusAcceptViews(context, participants);

		// missed will not be set here
		Integer missed = null;

		// get assessments
		Integer assessments = this.mnemeConnector.getNumMnemeAssessmentsCompleted(context, userId);

		// get melete count, views, compute percent viewed
		Integer count = this.meleteConnector.getNumMeleteModules(context);
		Map<String, Integer> views = this.meleteConnector.getNumMeleteCompleteModuleViews(context, participants);
		int viewsForUser = views.get(userId) == null ? 0 : views.get(userId).intValue();
		// TODO: need for ONLY one user, not all
		Integer meletePct = Integer.valueOf(count.intValue() == 0 ? 0 : ((viewsForUser * 100) / count.intValue()));

		return new StudentOverviewImpl(context, lastVisits.get(userId), posts.get(userId), missed, assessments, meletePct, status,
				syllabusAcceptViews.get(userId), userId);
	}

	/**
	 * {@inheritDoc}
	 */
	public SyllabusOverview getSyllabusOverview(String context)
	{
		// consider only the active users
		List<Participant> participants = getParticipants(context, false);
		Set<String> actives = getActiveParticipants(participants);

		Integer pctAccepted = null;
		Integer pctViewed = null;

		if (actives.isEmpty())
		{
			pctAccepted = Integer.valueOf(0);
			pctViewed = Integer.valueOf(0);
		}

		else
		{
			// get all the syllabus accepts for the site
			Map<String, Date> syllabusAcceptViews = this.syllabusConnector.getSyllabusAcceptViews(context, participants);

			int accepters = 0;
			for (String userId : actives)
			{
				if (syllabusAcceptViews.get(userId) != null) accepters++;
			}

			// compute the % - considering ONLY active users
			pctAccepted = Integer.valueOf((accepters * 100) / actives.size());

			// get all syllabus views for the site
			Map<String, Date> syllabusFirstViews = this.syllabusConnector.getSyllabusFirstViews(context, participants);

			int viewers = 0;
			for (String userId : actives)
			{
				if (syllabusFirstViews.get(userId) != null) viewers++;
			}

			// compute the % - considering ONLY active users
			pctViewed = Integer.valueOf((viewers * 100) / actives.size());
		}

		return new SyllabusOverviewImpl(context, pctAccepted, pctViewed);
	}

	/**
	 * Final initialization, once all dependencies are set.
	 */
	public void init()
	{
		M_log.info("init()");
	}

	/**
	 * Dependency: JforumConnector
	 * 
	 * @param connector
	 *        The JforumConnector.
	 */
	public void setJforumConnector(JforumConnector jforumConnector)
	{
		this.jforumConnector = jforumConnector;
	}

	/**
	 * Dependency: MeleteConnector
	 * 
	 * @param connector
	 *        The MeleteConnector.
	 */
	public void setMeleteConnector(MeleteConnector meleteConnector)
	{
		this.meleteConnector = meleteConnector;
	}

	/**
	 * Dependency: MnemeConnector
	 * 
	 * @param connector
	 *        The MnemeConnector.
	 */
	public void setMnemeConnector(MnemeConnector mnemeConnector)
	{
		this.mnemeConnector = mnemeConnector;
	}

	/**
	 * Dependency: SecurityService.
	 * 
	 * @param service
	 *        The SecurityService.
	 */
	public void setSecurityService(SecurityService service)
	{
		securityService = service;
	}

	/**
	 * Dependency: SiteConnector
	 * 
	 * @param connector
	 *        The SiteConnector.
	 */
	public void setSiteConnector(SiteConnector siteConnector)
	{
		this.siteConnector = siteConnector;
	}

	/**
	 * Dependency: SiteService
	 * 
	 * @param service
	 *        The SiteService.
	 */
	public void setSiteService(SiteService service)
	{
		this.siteService = service;
	}

	/**
	 * Dependency: SyllabusConnector
	 * 
	 * @param connector
	 *        The SyllabusConnector.
	 */
	public void setSyllabusConnector(SyllabusConnector syllabusConnector)
	{
		this.syllabusConnector = syllabusConnector;
	}

	/**
	 * Dependency: UserDirectoryService
	 * 
	 * @param service
	 *        The UserDirectoryService.
	 */
	public void setUserDirectoryService(UserDirectoryService service)
	{
		this.userDirectoryService = service;
	}

	/**
	 * Check the security for this user doing this function within this context.
	 * 
	 * @param userId
	 *        the user id.
	 * @param function
	 *        the function.
	 * @param context
	 *        The context.
	 * @param ref
	 *        The entity reference.
	 * @return true if the user has permission, false if not.
	 */
	protected boolean checkSecurity(String userId, String function, String context)
	{
		// check for super user
		if (securityService.isSuperUser(userId)) return true;

		// check for the user / function / context-as-site-authz
		// use the site ref for the security service (used to cache the security calls in the security service)
		String siteRef = siteService.siteReference(context);

		// form the azGroups for a context-as-implemented-by-site
		Collection<String> azGroups = new ArrayList<String>(2);
		azGroups.add(siteRef);
		azGroups.add("!site.helper");

		boolean rv = securityService.unlock(userId, function, siteRef, azGroups);
		return rv;
	}

	/**
	 * Build a set containing the user ids of the active participants.
	 * 
	 * @param participants
	 *        The full list of participants.
	 * @return A Set of userId strings for those participants that are active.
	 */
	protected Set<String> getActiveParticipants(List<Participant> participants)
	{
		Set<String> rv = new HashSet<String>();
		for (Participant p : participants)
		{
			if ((p.status == ParticipantStatus.enrolled)||(p.status == ParticipantStatus.added))
			{
				rv.add(p.userId);
			}
		}

		return rv;
	}

	/**
	 * Get a list of basic participant information for the site.
	 * 
	 * @param context
	 *        The site id.
	 * @param includeAll
	 *        if true, include instructors and ta's too, else leave them out.
	 * @return The List of Participants for qualified users in the site.
	 */
	protected List<Participant> getParticipants(String context, boolean includeAll)
	{
		ArrayList<Participant> rv = new ArrayList<Participant>();
		try
		{
			Site site = this.siteService.getSite(context);
			Set<Member> members = site.getMembers();
			for (Member m : members)
			{
				ParticipantStatus status = null;
				String userId = m.getUserId();
				String sortName = null;
				String displayId = null;
				try
				{
					User user = this.userDirectoryService.getUser(userId);
					sortName = user.getSortName();
					displayId = user.getDisplayId().trim();
				}
				catch (UserNotDefinedException e)
				{
					// skip deleted users
					continue;
				}

				// outright skip guests
				if (m.getRole().getId().equalsIgnoreCase("guest"))
				{
					continue;
				}

				if (m.getRole().getId().equals("Blocked"))
				{
					status = ParticipantStatus.blocked;
				}

				else if (m.getRole().getId().equals("Observer"))
				{
					if (includeAll) status = ParticipantStatus.observer;
				}

				else if (site.isAllowed(userId, "section.role.student"))
				{
					if (m.isProvided()) status = ParticipantStatus.enrolled;
					else status = ParticipantStatus.added;
				}

				else if (site.isAllowed(userId, "section.role.instructor"))
				{
					if (includeAll) status = ParticipantStatus.instructor;
				}

				else if (site.isAllowed(userId, "section.role.ta"))
				{
					if (includeAll) status = ParticipantStatus.ta;
				}

				else if (!m.isActive())
				{
					// check for inactive users of the role that has this access
					Set roles = site.getRolesIsAllowed("section.role.student");
					if (roles.contains(m.getRole().getId()))
					{
						// inactive could-be-students that are provided are "dropped", but if they are not provided, they are "blocked"
						if (m.isProvided())
						{
							status = ParticipantStatus.dropped;
						}
						else
						{
							status = ParticipantStatus.blocked;
						}
					}
				}

				// which section is the user in? If in multiple, pick the one that is active, if any. Otherwise, just pick any.
				String groupTitle = null;
				String titleActive = null;
				String titleInactive = null;
				Collection groups = site.getGroups();
				for (Object groupO : groups)
				{
					Group g = (Group) groupO;

					// skip non-section groups
					if (g.getProperties().getProperty("sections_category") == null) continue;

					// we want to find the user even if not active, so we cannot use g.getUsers(), which only returns active users -ggolden
					// if (g.getUsers().contains(userId))
					Set<Member> groupMemebers = g.getMembers();
					for (Member gm : groupMemebers)
					{
						if (gm.getUserId().equals(userId))
						{
							if (gm.isActive())
							{
								if (titleActive == null) titleActive = g.getTitle();
							}
							else
							{
								if (titleInactive == null) titleInactive = g.getTitle();
							}
						}
					}
				}
				if (titleActive != null)
				{
					groupTitle = titleActive;
				}
				else if (titleInactive != null)
				{
					groupTitle = titleInactive;
				}

				// take only those who have a status set (this skips the non-students)
				if (status != null)
				{
					Participant p = new Participant();
					p.userId = userId;
					p.status = status;
					p.sortName = sortName;
					p.displayId = displayId;
					p.groupTitle = groupTitle;
					rv.add(p);
				}
			}
		}
		catch (IdUnusedException e)
		{
			M_log.warn("getParticipants: missing site: " + context);
		}
		return rv;
	}
}
