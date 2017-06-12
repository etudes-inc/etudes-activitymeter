/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/MeleteConnector.java $
 * $Id: MeleteConnector.java 12131 2015-11-25 15:34:48Z mallikamt $
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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.activitymeter.api.ItemAccessStatus;
import org.etudes.activitymeter.api.ItemStatus;
import org.etudes.activitymeter.api.MeleteItemDetail;
import org.etudes.activitymeter.api.MeleteItemOverview;
import org.etudes.activitymeter.api.MeleteOverview;
import org.etudes.api.app.melete.ModuleObjService;
import org.etudes.api.app.melete.ModuleService;
import org.etudes.api.app.melete.SectionObjService;
import org.etudes.api.app.melete.SectionService;

/**
 * MeleteConnector are methods used by the Overview service to get Melete data.
 */
public class MeleteConnector
{
	/** Our logger. */
	private static Log M_log = LogFactory.getLog(MeleteConnector.class);

	/** Dependency : melete module service */
	private ModuleService moduleService = null;

	/** Dependency : melete section service */
	private SectionService sectionService = null;

	/**
	 * Returns to uninitialized state.
	 */
	public void destroy()
	{
		M_log.info("destroy()");
	}

	/**
	 * Get detail information for a melete item (section) for active users.
	 * 
	 * @param context
	 *        The site id.
	 * @param sectionId
	 *        The section id.
	 * @param activeParticipantIds
	 *        The user ids of all active participants
	 * @return A MeleteSectionOverview.
	 */
	public MeleteItemDetail getMeleteItemDetail(String context, String sectionId, Set<String> activeParticipantIds)
	{
		// get the section
		SectionObjService sec = sectionService.getSection(new Integer(sectionId).intValue());
		if (sec == null) throw new IllegalArgumentException();

		// get section's module
		ModuleObjService mod = sec.getModule();
		Date openDate = mod.getModuleshdate().getStartDate();
		Date closeDate = mod.getModuleshdate().getEndDate();
		Date allowUntilDate = mod.getModuleshdate().getAllowUntilDate();
		Boolean hideUntilStart = mod.getModuleshdate().getHideUntilStart();

		// module status
		ItemAccessStatus status = ItemAccessStatus.invalid;
		String moduleStatus = moduleService.isSectionModuleOpen(openDate, closeDate, allowUntilDate, hideUntilStart);
		if (!"invalid".equals(moduleStatus))
		{
			if ("open".equals(moduleStatus))
				status = ItemAccessStatus.open;
			else if ("later".equals(moduleStatus))
			{
				status = ItemAccessStatus.notOpen;
			}
			else if ("hiddenUntilOpen".equals(moduleStatus))
			{
				status = ItemAccessStatus.hiddenUntilOpen;
			}
			else
				status = ItemAccessStatus.closed;
		}

		// section viewers
		Integer viewCount = getNumberOfActiveViewers(sectionId, activeParticipantIds);

		return new MeleteItemDetailImpl(context, closeDate, allowUntilDate, viewCount, openDate, sectionId, status, sec.getTitle(), mod.getTitle(),
				activeParticipantIds.size());
	}

	/**
	 * Get overview information for Melete items, modules and sections, for this site and the active users, in Melete order (each Module followed by its section).
	 * 
	 * @param context
	 *        The site id.
	 * @param activeParticipantIds
	 *        The user ids of all active participants
	 * @return a List of MeleteOverviewItem.
	 */
	public List<MeleteItemOverview> getMeleteItemOverviews(String context, Set<String> activeParticipantIds)
	{
		ArrayList<MeleteItemOverview> rv = new ArrayList<MeleteItemOverview>();
		try
		{
			List<ModuleObjService> modules = moduleService.getModules(context);
			if (modules == null || modules.size() <= 0) return rv;
			for (ModuleObjService m : modules)
			{
				// module status
				ItemStatus status = ItemStatus.na;
				ItemAccessStatus accessStatus = ItemAccessStatus.open;
				String moduleStatus = moduleService.isSectionModuleOpen(m.getModuleshdate().getStartDate(), m.getModuleshdate().getEndDate(), m.getModuleshdate().getAllowUntilDate(), m.getModuleshdate().getHideUntilStart());
				status = ("invalid".equals(moduleStatus)) ? ItemStatus.invalid : ItemStatus.published;

				// get sections of the module
				List<SectionObjService> sections = sectionService.getSections(m);
				// if module has no section then mark it as invalid
				if (sections == null || sections.size() <= 0) status = ItemStatus.invalid;
							
				if (moduleStatus.equals("invalid")) accessStatus = ItemAccessStatus.invalid;
				if (moduleStatus.equals("open")) accessStatus = ItemAccessStatus.open;
				if (moduleStatus.equals("later")) accessStatus = ItemAccessStatus.notOpen;
				if (moduleStatus.equals("hiddenUntilOpen")) accessStatus = ItemAccessStatus.hiddenUntilOpen;
				if (moduleStatus.equals("closed")) accessStatus = ItemAccessStatus.closed;
					
				// add MeleteItemOverviewImpl
				rv.add(new MeleteItemOverviewImpl(m.getModuleId().toString(), Boolean.TRUE, null, status, m.getTitle(), accessStatus));

				// add sections of the module
				if (sections == null || sections.size() <= 0) continue;
				for (SectionObjService s : sections)
				{
					// section viewers
					Integer viewCount = getNumberOfActiveViewers(s.getSectionId().toString(), activeParticipantIds);

					// add MeleteItemOverviewImpl
					rv.add(new MeleteItemOverviewImpl(s.getSectionId().toString(), Boolean.FALSE, viewCount, ItemStatus.na, s.getTitle(), ItemAccessStatus.unpublished));
				}
			}
		}
		catch (Exception e)
		{
			// nothing
		}
		return rv;
	}

	/**
	 * Get melete overview information for all active users in this site.
	 * 
	 * @param context
	 *        The site id.
	 * @param activeParticipantIds
	 *        The user ids of all active participants
	 * @return A MeleteOverview.
	 */
	public MeleteOverview getMeleteOverview(String context, Set<String> activeParticipantIds)
	{
		// all sections viewed by all users so far
		Map<Integer, List<String>> sectionViewers = sectionService.getNumberOfViewedSections(context);

		// create a new map for just active viewers
		Map<Integer, Set<String>> activeViewers = new HashMap<Integer, Set<String>>();
		Iterator<Integer> i = sectionViewers.keySet().iterator();
		while (i.hasNext())
		{
			Integer secId = i.next();
			Set<String> viewers = new HashSet<String>();

			// get all active users of the section
			List<String> secUsers = sectionViewers.get(secId);
			for (String u : secUsers)
			{
				if (activeParticipantIds.contains(u)) viewers.add(u);
			}

			// add to map only if section is viewed by active participants
			if (viewers.size() > 0) activeViewers.put(secId, viewers);
		}

		return new MeleteOverviewImpl(sectionService.getNumberOfActiveSections(context), activeViewers.size());
	}

	/**
	 * Get the active viewers count for the section
	 * 
	 * @param sectionId
	 *        the section id
	 * @param activeParticipantIds
	 *        The user ids of all active participants
	 * @return Integer
	 */
	public Integer getNumberOfActiveViewers(String sectionId, Set<String> activeParticipantIds)
	{
		Integer viewCount = 0;
		Set<String> activeViewers = new HashSet<String>();
		try
		{
			// all users of the section
			Map<String, Date> sectionUsers = sectionService.getSectionViewDates(sectionId);
			if (sectionUsers != null)
			{
				Iterator<String> allViewers = sectionUsers.keySet().iterator();
				while (allViewers.hasNext())
				{
					String checkId = allViewers.next();

					// if user is part of active participants then add
					if (activeParticipantIds.contains(checkId)) activeViewers.add(checkId);
				}
				viewCount = activeViewers.size();
			}
		}
		catch (Exception e)
		{
			viewCount = 0;
		}
		return viewCount;
	}

	/**
	 * Check with Melete to find the number of completed module views per user, total in the site.
	 * 
	 * @param context
	 *        The site id.
	 * @return The map, keyed by user id, data is the count.
	 */
	public Map<String, Integer> getNumMeleteCompleteModuleViews(String context, List<Participant> participants)
	{
		Map<String, Integer> rv = new HashMap<String, Integer>();
		// get users # of sections viewed
		Map<String, Integer> users = moduleService.getNumberOfModulesCompletedByUserId(context);

		for (Participant p : participants)
		{
			if (users.containsKey(p.userId)) rv.put(p.userId, users.get(p.userId));
		}

		return rv;
	}

	/**
	 * Get the number of active module in the context.
	 * 
	 * @param context
	 *        The site id.
	 * @return The number of modules in the context.
	 */
	public Integer getNumMeleteModules(String context)
	{
		Integer sz = moduleService.getCourseModuleSize(context);

		if (sz == null) return Integer.valueOf(0);
		return sz;
	}

	/**
	 * Check with Melete to find all the section view dates for all users.
	 * 
	 * @param context
	 *        The site id.
	 * @return The map, keyed by user id, data is the view date.
	 */
	public Map<String, Date> getSectionFirstViews(String context, String sectionId, List<Participant> participants)
	{
		Map<String, Date> rv = new HashMap<String, Date>();
		try
		{
			Map<String, Date> sectionUsers = sectionService.getSectionFirstViewDates(sectionId);

			for (Participant p : participants)
			{
				if (sectionUsers.containsKey(p.userId)) rv.put(p.userId, sectionUsers.get(p.userId));
			}
		}
		catch (Exception e)
		{
			// nothing
		}
		return rv;
	}
	
	/**
	 * Check with Melete to find all the section view dates for all users.
	 * 
	 * @param context
	 *        The site id.
	 * @return The map, keyed by user id, data is the view date.
	 */
	public Map<String, Date> getSectionViews(String context, String sectionId, List<Participant> participants)
	{
		Map<String, Date> rv = new HashMap<String, Date>();
		try
		{
			Map<String, Date> sectionUsers = sectionService.getSectionViewDates(sectionId);

			for (Participant p : participants)
			{
				if (sectionUsers.containsKey(p.userId)) rv.put(p.userId, sectionUsers.get(p.userId));
			}
		}
		catch (Exception e)
		{
			// nothing
		}
		return rv;
	}

	/**
	 * Final initialization
	 */
	public void init()
	{
		M_log.info("init()");
	}

	/**
	 * Set Dependency Service
	 * 
	 * @param moduleService
	 */
	public void setModuleService(ModuleService moduleService)
	{
		this.moduleService = moduleService;
	}

	/**
	 * Set Dependency service
	 * 
	 * @param sectionService
	 */
	public void setSectionService(SectionService sectionService)
	{
		this.sectionService = sectionService;
	}
}
