/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/AccessProvider.java $
 * $Id: AccessProvider.java 8568 2014-08-30 06:26:16Z mallikamt $
 ***********************************************************************************
 *
 * Copyright (c) 2011, 2013, 2014 Etudes, Inc.
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

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.activitymeter.api.OverviewService;
import org.etudes.activitymeter.api.ParticipantOverview;
import org.etudes.activitymeter.api.ParticipantOverviewsSort;
import org.etudes.activitymeter.api.ParticipantSiteOverview;
import org.etudes.activitymeter.api.ParticipantSiteOverviewsSort;
import org.etudes.coursemap.api.CourseMapItem;
import org.etudes.coursemap.api.CourseMapItemType;
import org.etudes.coursemap.api.CourseMapMap;
import org.etudes.coursemap.api.CourseMapService;
import org.sakaiproject.authz.api.SecurityAdvisor;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.entity.api.Entity;
import org.sakaiproject.entity.api.EntityAccessOverloadException;
import org.sakaiproject.entity.api.EntityCopyrightException;
import org.sakaiproject.entity.api.EntityManager;
import org.sakaiproject.entity.api.EntityNotDefinedException;
import org.sakaiproject.entity.api.EntityPermissionException;
import org.sakaiproject.entity.api.EntityProducer;
import org.sakaiproject.entity.api.HttpAccess;
import org.sakaiproject.entity.api.Reference;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.entity.api.ResourcePropertiesEdit;
import org.sakaiproject.event.api.EventTrackingService;
import org.sakaiproject.exception.CopyrightException;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.exception.ServerOverloadException;
import org.sakaiproject.i18n.InternationalizedMessages;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.util.BaseResourcePropertiesEdit;
import org.sakaiproject.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * AttachmentServiceImpl implements AttachmentService.
 */
public class AccessProvider implements EntityProducer
{
	/** Our logger. */
	private static Log M_log = LogFactory.getLog(AccessProvider.class);

	/** The chunk size used when streaming (100k). */
	protected static final int STREAM_BUFFER_SIZE = 102400;

	/** For download (such as all submissions) request references. */
	static final String DOWNLOAD = "download";

	/** The reference type. */
	static final String REF_TYPE = "etudes:am";

	/** This string starts the references to download requests. */
	static final String REFERENCE_ROOT = "/am";

	/** Messages bundle name. */
	protected String bundle = null;

	/** Dependency: RosterService. */
	protected CourseMapService courseMapService = null;

	/** Dependency: EntityManager */
	protected EntityManager entityManager = null;

	/** Dependency: EventTrackingService */
	protected EventTrackingService eventTrackingService = null;

	/** Messages. */
	protected transient InternationalizedMessages messages = null;

	/** Dependency: OverviewService. */
	protected OverviewService overviewService = null;

	/** Dependency: SecurityService */
	protected SecurityService securityService = null;

	/** Dependency: ServerConfigurationService */
	protected ServerConfigurationService serverConfigurationService = null;

	/** Dependency: SessionManager */
	protected SessionManager sessionManager = null;

	/**
	 * {@inheritDoc}
	 */
	public String archive(String siteId, Document doc, Stack stack, String archivePath, List attachments)
	{
		return null;
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
	public Entity getEntity(Reference ref)
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection getEntityAuthzGroups(Reference ref, String userId)
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getEntityDescription(Reference ref)
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public ResourceProperties getEntityResourceProperties(Reference ref)
	{
		// decide on security
		if (!checkSecurity(ref)) return null;

		ResourcePropertiesEdit props = new BaseResourcePropertiesEdit();

		props.addProperty(ResourceProperties.PROP_CONTENT_TYPE, "text/csv");
		props.addProperty(ResourceProperties.PROP_IS_COLLECTION, "FALSE");

		// TODO: localize
		props.addProperty("DAV:displayname", "Export to CSV");

		return props;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getEntityUrl(Reference ref)
	{
		// TODO:
		return serverConfigurationService.getAccessUrl() + ref.getReference();
	}

	/**
	 * {@inheritDoc}
	 */
	public HttpAccess getHttpAccess()
	{
		// TODO:
		final AccessProvider service = this;

		return new HttpAccess()
		{
			public void handleAccess(HttpServletRequest req, HttpServletResponse res, Reference ref, Collection copyrightAcceptedRefs)
					throws EntityPermissionException, EntityNotDefinedException, EntityAccessOverloadException, EntityCopyrightException
			{
				// decide on security
				if (!checkSecurity(ref))
				{
					throw new EntityPermissionException(sessionManager.getCurrentSessionUserId(), "access", ref.getReference());
				}

				service.handleAccessDownload(req, res, ref, copyrightAcceptedRefs);
			}
		};
	}

	/**
	 * {@inheritDoc}
	 */
	public String getLabel()
	{
		return null;
	}

	/**
	 * Final initialization, once all dependencies are set.
	 */
	public void init()
	{
		try
		{
			// register as an entity producer
			entityManager.registerEntityProducer(this, REFERENCE_ROOT);

			// messages
			// if (this.bundle != null) this.messages = new ResourceLoader(this.bundle);
		}
		catch (Throwable t)
		{
			M_log.warn("init(): ", t);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String merge(String siteId, Element root, String archivePath, String fromSiteId, Map attachmentNames, Map userIdTrans,
			Set userListAllowImport)
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean parseEntityReference(String reference, Reference ref)
	{
		// TODO:
		if (reference.startsWith(REFERENCE_ROOT))
		{
			// /am/overview/siteid/site-title activity.csv
			// /am/student/siteid/studentid/site-title student-name activity.csv
			// /am/site/siteid/site-title visits.csv
			String id = null;
			String context = null;
			String subType = null;
			String[] parts = StringUtil.split(reference, Entity.SEPARATOR);
			String container = null;

			// overview export
			if ((parts.length == 5) && ("overview".equals(parts[2])))
			{
				// site id
				context = parts[3];

				// the download name
				id = parts[4];

				// set the sub-type to the type of download
				subType = parts[2];
			}

			// student activity export
			else if ((parts.length == 6) && ("student".equals(parts[2])))
			{
				// site id
				context = parts[3];

				// the download name
				id = parts[5];

				// set the sub-type to the type of download
				subType = parts[2];

				// put the user id in container
				container = parts[4];
			}

			// site visits export
			else if ((parts.length == 5) && ("site".equals(parts[2])))
			{
				// site id
				context = parts[3];

				// the download name
				id = parts[4];

				// set the sub-type to the type of download
				subType = parts[2];
			}

			else
			{
				return false;
			}

			ref.set(REF_TYPE, subType, id, container, context);

			return true;
		}

		return false;
	}

	/**
	 * Set the message bundle.
	 * 
	 * @param bundle
	 *        The message bundle.
	 */
	public void setBundle(String name)
	{
		this.bundle = name;
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
	 * Dependency: EntityManager.
	 * 
	 * @param service
	 *        The EntityManager.
	 */
	public void setEntityManager(EntityManager service)
	{
		entityManager = service;
	}

	/**
	 * Dependency: EventTrackingService.
	 * 
	 * @param service
	 *        The EventTrackingService.
	 */
	public void setEventTrackingService(EventTrackingService service)
	{
		eventTrackingService = service;
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
	 * Dependency: SecurityService.
	 * 
	 * @param service
	 *        The SecurityService.
	 */
	public void setSecurityService(SecurityService service)
	{
		this.securityService = service;
	}

	/**
	 * Dependency: ServerConfigurationService.
	 * 
	 * @param service
	 *        The ServerConfigurationService.
	 */
	public void setServerConfigurationService(ServerConfigurationService service)
	{
		this.serverConfigurationService = service;
	}

	/**
	 * Dependency: SessionManager.
	 * 
	 * @param service
	 *        The SessionManager.
	 */
	public void setSessionManager(SessionManager service)
	{
		this.sessionManager = service;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean willArchiveMerge()
	{
		return false;
	}

	/**
	 * Check security for this entity.
	 * 
	 * @param ref
	 *        The Reference to the entity.
	 * @return true if allowed, false if not.
	 */
	protected boolean checkSecurity(Reference ref)
	{
		String userId = this.sessionManager.getCurrentSessionUserId();
		String context = ref.getContext();

		return this.overviewService.allowActivityAccess(context, userId);
	}

	/**
	 * If the value contains the separator, escape it with quotes.
	 * 
	 * @param value
	 *        The value to escape.
	 * @param separator
	 *        The separator.
	 * @return The escaped value.
	 */
	protected String escape(String value, String separator)
	{
		if (value.indexOf(separator) != -1)
		{
			// any quotes in here need to be doubled
			value = value.replaceAll("\"", "\"\"");

			return "\"" + value + "\"";
		}

		return value;
	}

	/**
	 * Format a date for inclusion in the csv file
	 * 
	 * @param date
	 *        The date.
	 * @return The formatted date.
	 */
	protected String formatDate(Date date)
	{
		if (date == null) return "-";
		DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.US);
		return removeSeconds(format.format(date));
	}

	/**
	 * Format an Float for inclusion in the csv.
	 * 
	 * @param value
	 *        The value.
	 * @return The value formatted.
	 */
	protected String formatFloat(Float value)
	{
		if (value == null) return "-";
		return value.toString();
	}

	/**
	 * Format an Integer for inclusion in the csv.
	 * 
	 * @param value
	 *        The value.
	 * @return The value formatted.
	 */
	protected String formatInteger(Integer value)
	{
		if (value == null) return "-";
		return value.toString();
	}

	/**
	 * Format a String for inclusion in the csv.
	 * 
	 * @param value
	 *        The value.
	 * @return The value formatted.
	 */
	protected String formatString(String value)
	{
		if (value == null) return "-";
		return value;
	}

	/**
	 * Process the access request for a download (not CHS private docs).
	 * 
	 * @param req
	 * @param res
	 * @param ref
	 * @param copyrightAcceptedRefs
	 * @throws PermissionException
	 * @throws IdUnusedException
	 * @throws ServerOverloadException
	 * @throws CopyrightException
	 */
	protected void handleAccessDownload(HttpServletRequest req, HttpServletResponse res, Reference ref, Collection copyrightAcceptedRefs)
			throws EntityPermissionException, EntityNotDefinedException, EntityAccessOverloadException, EntityCopyrightException
	{
		// ref.getSubType will be student, site, overview...

		String contentType = "text/csv";
		String disposition = "attachment; filename=\"" + ref.getId() + "\"";

		OutputStream out = null;
		PrintStream printer = null;
		try
		{
			res.setContentType(contentType);
			res.addHeader("Content-Disposition", disposition);

			out = res.getOutputStream();
			printer = new PrintStream(out, true, "UTF-8");

			if ("overview".equals(ref.getSubType()))
			{
				printOverviewCsv(printer, ref.getContext());
			}
			else if ("site".equals(ref.getSubType()))
			{
				printSiteCsv(printer, ref.getContext());
			}
			else if ("student".equals(ref.getSubType()))
			{
				printStudentCsv(printer, ref.getContext(), ref.getContainer());
			}

			printer.flush();
		}
		catch (Throwable e)
		{
			M_log.warn("handleAccessDownload: ", e);
		}
		finally
		{
			if (printer != null)
			{
				try
				{
					printer.close();
				}
				catch (Throwable e)
				{
					M_log.warn("closing printer: " + e.toString());
				}
			}
		}

		// track event
		// this.eventTrackingService.post(this.eventTrackingService.newEvent(MnemeService.DOWNLOAD_SQ, ref.getReference(), false));

		// else if not a valid subtype
		// {
		// M_log.warn("handleAccessDownload: unknown request: " + ref.getReference());
		// }
	}

	/**
	 * Remove our security advisor.
	 */
	protected void popAdvisor()
	{
		this.securityService.popAdvisor();
	}

	/**
	 * Format the overview csv for this site.
	 * 
	 * @param out
	 *        The print stream.
	 * @param context
	 *        The site id.
	 */
	protected void printOverviewCsv(PrintStream out, String context)
	{
		String separator = ",";

		// TODO: localize headers
		out.println("Name" + separator + "Status" + separator + "Section" + separator + "First Visit" + separator + "LastVisit" + separator
				+ "SiteVisits" + separator + "Syllabus" + separator + "Modules Completed" + separator + "Posts" + separator + "Assessments");

		// get the data
		List<ParticipantOverview> participantOverviews = this.overviewService.getParticipantOverviews(context, ParticipantOverviewsSort.name_a, true);

		// process each row
		for (ParticipantOverview overview : participantOverviews)
		{
			// name
			out.print(escape(formatString(overview.getSortName() + " (" + overview.getDisplayId() + ")"), separator) + separator);

			// status
			out.print(escape(formatString(overview.getStatus().toString()), separator) + separator);

			// section
			out.print(escape(formatString(overview.getGroupTitle()), separator) + separator);

			// first visit
			out.print(escape(formatDate(overview.getFirstVisitDate()), separator) + separator);

			// last visit
			out.print(escape(formatDate(overview.getLastVisitDate()), separator) + separator);

			// site visits
			out.print(escape(formatInteger(overview.getNumVisits()), separator) + separator);

			// syllabus
			out.print(escape(formatDate(overview.getSyllabusDate()), separator) + separator);

			// modules completed
			out.print(escape(formatInteger(overview.getNumMeleteViews()), separator) + separator);

			// posts
			out.print(escape(formatInteger(overview.getNumJforumPosts()), separator) + separator);

			// assessments
			out.println(escape(formatInteger(overview.getNumMnemeSubmissions()), separator));
		}
	}

	/**
	 * Format the site visits csv for this site.
	 * 
	 * @param out
	 *        The print stream.
	 * @param context
	 *        The site id.
	 */
	protected void printSiteCsv(PrintStream out, String context)
	{
		String separator = ",";

		// TODO: localize headers
		out.println("Name" + separator + "Status" + separator + "First Visit" + separator + "LastVisit" + separator + "SiteVisits");

		// get the data
		List<ParticipantSiteOverview> participantOverviews = this.overviewService.getParticipantSiteOverviews(context,
				ParticipantSiteOverviewsSort.name_a);

		// process each row
		for (ParticipantSiteOverview overview : participantOverviews)
		{
			// name
			out.print(escape(formatString(overview.getSortName() + " (" + overview.getDisplayId() + ")"), separator) + separator);

			// status
			out.print(escape(formatString(overview.getStatus().toString()), separator) + separator);

			// first visit
			out.print(escape(formatDate(overview.getFirstVisitDate()), separator) + separator);

			// last visit
			out.print(escape(formatDate(overview.getLastVisitDate()), separator) + separator);

			// site visits
			out.println(escape(formatInteger(overview.getNumVisits()), separator));
		}
	}

	/**
	 * Format the site visits csv for this site.
	 * 
	 * @param out
	 *        The print stream.
	 * @param context
	 *        The site id.
	 */
	protected void printStudentCsv(PrintStream out, String context, String userId)
	{
		String separator = ",";

		// TODO: localize headers
		out.println("Type" + separator + "Title" + separator + "Open" + separator + "Due" + separator + "Activity" + separator + "Count" + separator
				+ "Score");

		// get the data
		CourseMapMap map = this.courseMapService.getUnfilteredMap(context, userId);

		for (CourseMapItem item : map.getItems())
		{
			if (item.getType() == CourseMapItemType.header)
			{
				if (item.getTitle() != null && item.getTitle().trim().length() > 0)
				{
					out.println(separator + escape(formatString(item.getTitle().toString()), separator) + separator + separator + separator
							+ separator + separator);
			}
			}

			else
			{
				// type
				out.print(escape(formatString(item.getType().toString()), separator) + separator);

				// title
				out.print(escape(formatString(item.getTitle()), separator) + separator);

				// open
				out.print(escape(formatDate(item.getOpen()), separator) + separator);

				// due
				out.print(escape(formatDate(item.getDue()), separator) + separator);

				// finished
				if (!item.getSuppressFinished()) out.print(escape(formatDate(item.getFinished()), separator) + separator);
				else out.print(escape(formatDate(null), separator) + separator);

				// count
				out.print(escape(formatInteger(item.getCount()), separator) + separator);

				// score
				// TODO: score status
				out.println(escape(formatFloat(item.getScore()), separator));
			}
		}
	}

	/**
	 * Setup a security advisor.
	 */
	protected void pushAdvisor()
	{
		// setup a security advisor
		this.securityService.pushAdvisor(new SecurityAdvisor()
		{
			public SecurityAdvice isAllowed(String userId, String function, String reference)
			{
				return SecurityAdvice.ALLOWED;
			}
		});
	}

	/**
	 * Remove the ":xx" seconds part of a MEDIUM date format display.
	 * 
	 * @param display
	 *        The MEDIUM formatted date.
	 * @return The MEDIUM formatted date with the seconds removed.
	 */
	protected String removeSeconds(String display)
	{
		int i = display.lastIndexOf(":");
		if ((i == -1) || ((i + 3) >= display.length())) return display;

		String rv = display.substring(0, i) + display.substring(i + 3);
		return rv;
	}
}
