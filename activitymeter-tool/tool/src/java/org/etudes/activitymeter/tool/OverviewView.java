/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-tool/tool/src/java/org/etudes/activitymeter/tool/OverviewView.java $
 * $Id: OverviewView.java 12259 2015-12-10 21:35:14Z mallikamt $
 ***********************************************************************************
 *
 * Copyright (c) 2010, 2011, 2013 Etudes, Inc.
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
import org.sakaiproject.entity.api.EntityManager;
import org.sakaiproject.entity.api.Reference;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.util.Validator;
import org.sakaiproject.util.Web;

public class OverviewView extends ControllerImpl
{
	/** Our log. */
	private static Log M_log = LogFactory.getLog(OverviewView.class);

	/** Dependency: EntityManager */
	protected EntityManager entityManager = null;

	/** Dependency: OverviewService. */
	protected OverviewService overviewService = null;

	/** Dependency: SessionManager. */
	protected SessionManager sessionManager = null;

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
		// optional sort parameter
		if ((params.length != 2) && (params.length != 3))
		{
			throw new IllegalArgumentException();
		}

		// security
		if (!this.overviewService.allowActivityAccess(this.toolManager.getCurrentPlacement().getContext(),
				this.sessionManager.getCurrentSessionUserId()))
		{
			// redirect to error
			res.sendRedirect(res.encodeRedirectURL(Web.returnUrl(req, "/error/unauthorized")));
			return;
		}

		// default sort is status, ascending
		String sortCodes = (params.length > 2) ? params[2] : "1A";
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

		// disable the tool navigation to this view
		context.put("disableOverview", Boolean.TRUE);

		// get the current site's id
		String siteId = this.toolManager.getCurrentPlacement().getContext();

		// get site info
		SiteInfo siteInfo = this.overviewService.getSiteInfo(siteId);
		context.put("siteInfo", siteInfo);

		// for the export link
		String suffix = context.getMessages().getString("export-overview-suffix");

		String exportPath = "/am/overview/" + siteId + "/" + Validator.escapeResourceName(siteInfo.getTitle()) + " " + suffix + ".csv";
		Reference ref = this.entityManager.newReference(exportPath);
		context.put("exportRef", ref);

		// get an overview for the site - for a 1 week period.
		Overview overview = this.overviewService.getOverview(siteId, Integer.valueOf(7));
		context.put("overview", overview);

		// get the participant overviews
		List<ParticipantOverview> participantOverviews = this.overviewService.getParticipantOverviews(siteId, sort, true);
		context.put("participantOverviews", participantOverviews);

		context.put("sort_column", sCol);
		context.put("sort_direction", sDir);

		// return here
		context.put("return", "/overview/" + sortCodes);

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
	 * Set the EntityManager.
	 * 
	 * @param service
	 *        the EntityManager.
	 */
	public void setEntityManager(EntityManager service)
	{
		this.entityManager = service;
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
