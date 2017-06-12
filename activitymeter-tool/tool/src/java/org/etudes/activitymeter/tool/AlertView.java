/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-tool/tool/src/java/org/etudes/activitymeter/tool/AlertView.java $
 * $Id: AlertView.java 3726 2012-12-06 18:11:29Z ggolden $
 ***********************************************************************************
 *
 * Copyright (c) 2011, 2012 Etudes, Inc.
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

package org.etudes.activitymeter.tool;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.activitymeter.api.Overview;
import org.etudes.activitymeter.api.OverviewService;
import org.etudes.activitymeter.api.ParticipantOverview;
import org.etudes.activitymeter.api.ParticipantOverviewsSort;
import org.etudes.activitymeter.api.SiteInfo;
import org.etudes.ambrosia.api.Context;
import org.etudes.ambrosia.util.ControllerImpl;
import org.etudes.util.api.RosterAdvisor;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.site.api.ToolConfiguration;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.util.StringUtil;
import org.sakaiproject.util.Web;

public class AlertView extends ControllerImpl
{
	/** Our log. */
	private static Log M_log = LogFactory.getLog(AlertView.class);

	/** Dependency: OverviewService. */
	protected OverviewService overviewService = null;

	/** Dependency: SessionManager. */
	protected SessionManager sessionManager = null;

	/** Dependency: SiteService */
	protected SiteService siteService;

	/** Dependency: ToolManager. */
	protected ToolManager toolManager = null;

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
	 * Shutdown.
	 */
	public void destroy()
	{
		M_log.info("destroy()");
	}

	/**
	 * {@inheritDoc}
	 */
	public void get(HttpServletRequest req, HttpServletResponse res, Context context, String[] params) throws IOException
	{
		// sort, and the parameters are a return destination
		if (params.length < 3)
		{
			throw new IllegalArgumentException();
		}

		// default sort is status, ascending
		String sortCodes = params[2];
		if (sortCodes.length() != 2)
		{
			throw new IllegalArgumentException();
		}
		String sCol = sortCodes.substring(0, 1);
		String sDir = sortCodes.substring(1, 2);
		ParticipantOverviewsSort sort = ParticipantOverviewsSort.sortFromCodes(sCol, sDir);
		if (sort == null)
		{
			throw new IllegalArgumentException();
		}

		context.put("sort_column", sCol);
		context.put("sort_direction", sDir);

		String destination = null;
		if (params.length > 3)
		{
			destination = "/" + StringUtil.unsplit(params, 3, params.length - 3, "/");
		}

		// if not specified, go to the overview
		else
		{
			destination = "/overview";
		}
		context.put("return", destination);

		// security
		if (!this.overviewService.allowActivityAccess(this.toolManager.getCurrentPlacement().getContext(),
				this.sessionManager.getCurrentSessionUserId()))
		{
			// redirect to error
			res.sendRedirect(res.encodeRedirectURL(Web.returnUrl(req, "/error/unauthorized")));
			return;
		}

		// get the current site's id
		String siteId = this.toolManager.getCurrentPlacement().getContext();

		// get site info
		SiteInfo siteInfo = this.overviewService.getSiteInfo(siteId);
		context.put("siteInfo", siteInfo);

		// get an overview for the site - for a 1 week period.
		Overview overview = this.overviewService.getOverview(siteId, Integer.valueOf(7));
		context.put("overview", overview);

		// get the list of students who have not visited in period
		// get the participant overviews
		List<ParticipantOverview> participantOverviews = this.overviewService.getParticipantsNotVisited(siteId, Integer.valueOf(7), sort);
		context.put("participantOverviews", participantOverviews);

		// destination info for private message link
		String toolId = null;
		try
		{
			Site site = this.siteService.getSite(siteId);
			ToolConfiguration config = site.getToolForCommonId("sakai.jforum.tool");
			if (config != null) toolId = config.getId();
		}
		catch (IdUnusedException e)
		{
			M_log.warn("get: missing site: " + context);
		}
		context.put("pmTool", toolId);

		// the roster access code to send a PM to the alert (not visited in past 7 days) users
		String pmCodeA = this.rosterAdvisor.encode(siteId, RosterAdvisor.RosterAccessType.alert, null);
		context.put("pmCodeA", pmCodeA);

		// do we have any users who have never visited?
		boolean found = false;
		for (ParticipantOverview p : participantOverviews)
		{
			// filter out those who have a first visit
			if (p.getFirstVisitDate() != null) continue;

			found = true;
			break;
		}

		if (found)
		{
			// the roster access code to send a PM to the never visited users
			String pmCodeN = this.rosterAdvisor.encode(siteId, RosterAdvisor.RosterAccessType.neverVisit, null);
			context.put("pmCodeN", pmCodeN);
		}

		// render
		uiService.render(ui, context);
	}

	/**
	 * Final initialization, once all dependencies are set.
	 */
	public void init()
	{
		super.init();

		M_log.info("init()");
	}

	/**
	 * {@inheritDoc}
	 */
	public void post(HttpServletRequest req, HttpServletResponse res, Context context, String[] params) throws IOException
	{
		String destination = uiService.decode(req, context);
		res.sendRedirect(res.encodeRedirectURL(Web.returnUrl(req, destination)));
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
	 * Set the SessionManager.
	 * 
	 * @param manager
	 *        The SessionManager.
	 */
	public void setSessionManager(SessionManager manager)
	{
		this.sessionManager = manager;
	}

	/**
	 * Set the SiteService.
	 * 
	 * @param service
	 *        The service.
	 */
	public void setSiteService(SiteService service)
	{
		this.siteService = service;
	}

	/**
	 * Set the tool manager.
	 * 
	 * @param manager
	 *        The tool manager.
	 */
	public void setToolManager(ToolManager manager)
	{
		this.toolManager = manager;
	}
}
