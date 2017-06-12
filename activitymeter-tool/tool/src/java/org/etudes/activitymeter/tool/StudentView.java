/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-tool/tool/src/java/org/etudes/activitymeter/tool/StudentView.java $
 * $Id: StudentView.java 12259 2015-12-10 21:35:14Z mallikamt $
 ***********************************************************************************
 *
 * Copyright (c) 2011, 2013, 2015 Etudes, Inc.
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
import org.etudes.activitymeter.api.OverviewService;
import org.etudes.activitymeter.api.ParticipantOverview;
import org.etudes.activitymeter.api.ParticipantOverviewsSort;
import org.etudes.activitymeter.api.SiteInfo;
import org.etudes.activitymeter.api.StudentOverview;
import org.etudes.ambrosia.api.Context;
import org.etudes.ambrosia.api.Value;
import org.etudes.ambrosia.util.ControllerImpl;
import org.etudes.coursemap.api.CourseMapMap;
import org.etudes.coursemap.api.CourseMapService;
import org.etudes.util.api.RosterAdvisor;
import org.sakaiproject.entity.api.EntityManager;
import org.sakaiproject.entity.api.Reference;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.site.api.ToolConfiguration;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.user.api.UserNotDefinedException;
import org.sakaiproject.util.StringUtil;
import org.sakaiproject.util.Validator;
import org.sakaiproject.util.Web;

public class StudentView extends ControllerImpl
{
	/** Our log. */
	private static Log M_log = LogFactory.getLog(StudentView.class);

	/** Dependency: CourseMapService. */
	protected CourseMapService courseMapService = null;

	/** Dependency: EntityManager */
	protected EntityManager entityManager = null;

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

	/** Dependency: UserDirectoryService */
	protected UserDirectoryService userDirectoryService;

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
		// need id, and "student view" flag, and the rest a return destination
		if (params.length < 4)
		{
			throw new IllegalArgumentException();
		}

		// user id
		String userId = params[2];
		context.put("userId", userId);

		// student view "S" or all "A"
		boolean studentViewChoice = params[3].equals("S");

		// we come from either the alert or overview views...
		String sortCodes = "1A";
		String returnView = null;
		String destination = null;
		if (params.length > 4)
		{
			destination = "/" + StringUtil.unsplit(params, 4, params.length - 4, "/");

			// we come from either the alert or overview views...
			if (params.length >= 4) returnView = params[4];

			// sort parameter may be the sort code
			if (params.length >= 5) sortCodes = params[5];
		}

		// if not specified, go to the overview
		else
		{
			destination = "/overview";
		}
		context.put("return", destination);

		if (sortCodes.length() != 2)
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

		// get the map - put the items from the map (not the actual map) into the context
		CourseMapMap map = null;
		if (studentViewChoice)
		{
			map = this.courseMapService.getMap(toolManager.getCurrentPlacement().getContext(), userId);
		}
		else
		{
			map = this.courseMapService.getUnfilteredMap(toolManager.getCurrentPlacement().getContext(), userId);
		}
		context.put("mapItems", map.getItems());

		// get the current site's id
		String siteId = toolManager.getCurrentPlacement().getContext();

		String sCol = sortCodes.substring(0, 1);
		String sDir = sortCodes.substring(1, 2);

		ParticipantOverviewsSort sort = ParticipantOverviewsSort.sortFromCodes(sCol, sDir);
		if (sort == null)
		{
			throw new IllegalArgumentException();
		}

		// get the return view's participant list, so we can offer next / prev
		List<ParticipantOverview> participantOverviews = null;
		if ("overview".equals(returnView))
		{
			participantOverviews = this.overviewService.getParticipantOverviews(siteId, sort, false);
		}
		else if ("alert".equals(returnView))
		{
			participantOverviews = this.overviewService.getParticipantsNotVisited(siteId, Integer.valueOf(7), sort);
		}

		if (participantOverviews != null)
		{
			// find our position
			int index = 0;
			for (ParticipantOverview p : participantOverviews)
			{
				if (p.getId().equals(userId)) break;
				index++;
			}

			// wrap next and prev if needed
			int prev = index - 1;
			int next = index + 1;
			if (next > participantOverviews.size() - 1) next = 0;
			if (prev < 0) prev = participantOverviews.size() - 1;

			context.put("prev", participantOverviews.get(prev).getId());
			context.put("next", participantOverviews.get(next).getId());
			context.put("position", index + 1);
			context.put("size", participantOverviews.size());
		}

		// get site info
		SiteInfo siteInfo = this.overviewService.getSiteInfo(siteId);
		context.put("siteInfo", siteInfo);

		// get an overview for the student
		StudentOverview studentOverview = this.overviewService.getStudentOverview(siteId, userId);

		// set the missed items from the map
		studentOverview.setNumItemsMissed(map.getNumItemsMissed());

		context.put("studentOverview", studentOverview);

		// for the export link
		String userName = "student";
		try
		{
			User user = this.userDirectoryService.getUser(userId);
			userName = user.getSortName() + " (" + user.getDisplayId().trim() + ")";
		}
		catch (UserNotDefinedException e)
		{
			M_log.warn("get: userId not found: " + userId);
		}

		String suffix = context.getMessages().getString("export-student-suffix");

		String exportPath = "/am/student/" + siteId + "/" + userId + "/" + Validator.escapeResourceName(siteInfo.getTitle()) + " " + userName + " " + suffix + ".csv";
		Reference ref = this.entityManager.newReference(exportPath);
		context.put("exportRef", ref);

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

		// the roster access code to send a PM to this user
		String pmCode = this.rosterAdvisor.encode(siteId, RosterAdvisor.RosterAccessType.user, userId);
		context.put("pmCode", pmCode);

		// student view decision (boolean string)
		Value studentView = this.uiService.newValue();
		studentView.setValue(Boolean.toString(studentViewChoice));
		context.put("studentView", studentView);

		// where should the student view click go?
		String toggle = (studentViewChoice ? "A" : "S");
		String studentViewDestination = "/student/" + userId + "/" + toggle + destination;
		context.put("studentViewDestination", studentViewDestination);

		context.put("studentViewToggle", (studentViewChoice ? "S" : "A"));

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
	 * Set the CourseMap.
	 * 
	 * @param service
	 *        The CourseMap.
	 */
	public void setCourseMapService(CourseMapService service)
	{
		this.courseMapService = service;
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

	/**
	 * Set the UserDirectoryService.
	 * 
	 * @param service
	 *        The service.
	 */
	public void setUserDirectoryService(UserDirectoryService service)
	{
		this.userDirectoryService = service;
	}
}
