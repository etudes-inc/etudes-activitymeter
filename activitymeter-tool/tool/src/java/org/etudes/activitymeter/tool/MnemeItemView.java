/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-tool/tool/src/java/org/etudes/activitymeter/tool/MnemeItemView.java $
 * $Id: MnemeItemView.java 12131 2015-11-25 15:34:48Z mallikamt $
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

package org.etudes.activitymeter.tool;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.activitymeter.api.MnemeItemDetail;
import org.etudes.activitymeter.api.MnemeItemOverview;
import org.etudes.activitymeter.api.OverviewService;
import org.etudes.activitymeter.api.ParticipantMnemeItemDetail;
import org.etudes.activitymeter.api.ParticipantMnemeItemDetailsSort;
import org.etudes.activitymeter.api.ParticipantStatus;
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
import org.sakaiproject.util.Web;

public class MnemeItemView extends ControllerImpl
{
	/** Our log. */
	private static Log M_log = LogFactory.getLog(MnemeItemView.class);

	/** Dependency: OverviewService. */
	protected OverviewService overviewService = null;

	/** Dependency: RosterAdvisor. */
	protected RosterAdvisor rosterAdvisor = null;

	/** Dependency: SessionManager. */
	protected SessionManager sessionManager = null;

	/** Dependency: SiteService */
	protected SiteService siteService;

	/** Dependency: ToolManager. */
	protected ToolManager toolManager = null;

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
		// id, then optional sort parameter
		if ((params.length != 3) && (params.length != 4))
		{
			throw new IllegalArgumentException();
		}

		// item id
		String itemId = params[2];
		context.put("itemId", itemId);

		// security
		if (!this.overviewService.allowActivityAccess(this.toolManager.getCurrentPlacement().getContext(),
				this.sessionManager.getCurrentSessionUserId()))
		{
			// redirect to error
			res.sendRedirect(res.encodeRedirectURL(Web.returnUrl(req, "/error/unauthorized")));
			return;
		}

		// default sort is status, ascending
		String sortCodes = (params.length == 4) ? params[3] : "1A";
		if (sortCodes.length() != 2)
		{
			throw new IllegalArgumentException();
		}
		String sCol = sortCodes.substring(0, 1);
		String sDir = sortCodes.substring(1, 2);
		ParticipantMnemeItemDetailsSort sort = ParticipantMnemeItemDetailsSort.sortFromCodes(sCol, sDir);
		if (sort == null)
		{
			throw new IllegalArgumentException();
		}

		// get the current site's id
		String siteId = toolManager.getCurrentPlacement().getContext();

		// get site info
		SiteInfo siteInfo = this.overviewService.getSiteInfo(siteId);
		context.put("siteInfo", siteInfo);

		// get an overview for the assessment
		MnemeItemDetail mnemeItemDetail = this.overviewService.getMnemeItemDetail(siteId, itemId);
		context.put("mnemeItemDetail", mnemeItemDetail);

		// get the mneme item overviews
		List<ParticipantMnemeItemDetail> participantDetails = this.overviewService.getParticipantMnemeItemDetails(siteId, itemId, sort);
		context.put("participantDetails", participantDetails);

		context.put("sort_column", sCol);
		context.put("sort_direction", sDir);

		// find our position, next and prev in the list
		List<MnemeItemOverview> mnemeItemOverviews = this.overviewService.getMnemeItemOverviews(siteId);
		int index = 0;
		for (MnemeItemOverview mio : mnemeItemOverviews)
		{
			if (mio.getId().equals(itemId)) break;
			index++;
		}

		// wrap next and prev if needed
		int prev = index - 1;
		int next = index + 1;
		if (next > mnemeItemOverviews.size() - 1) next = 0;
		if (prev < 0) prev = mnemeItemOverviews.size() - 1;

		context.put("prev", mnemeItemOverviews.get(prev).getId());
		context.put("next", mnemeItemOverviews.get(next).getId());
		context.put("position", index + 1);
		context.put("size", mnemeItemOverviews.size());

		// TODO: prior view fixed? /mneme ?? have sort parameters
		context.put("return", "/mneme");

		// check for PM to users who have not yet started
		boolean pmEnabled = false;
		for (ParticipantMnemeItemDetail p : participantDetails)
		{
			if ((p.getStatus() == ParticipantStatus.enrolled || p.getStatus() == ParticipantStatus.added) && (p.getStartedDate() == null))
			{
				pmEnabled = true;
				break;
			}
		}
		if (pmEnabled)
		{
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

			// the roster access code to send PMs to these users
			String pmCode = this.rosterAdvisor.encode(siteId, RosterAdvisor.RosterAccessType.mneme, itemId);
			context.put("pmCode", pmCode);
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
