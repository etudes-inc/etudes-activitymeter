/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/SiteVisitServiceImpl.java $
 * $Id: SiteVisitServiceImpl.java 6034 2013-09-25 20:53:05Z ggolden $
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

package org.etudes.activitymeter.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.activitymeter.api.SiteVisit;
import org.etudes.activitymeter.api.SiteVisitService;
import org.etudes.util.SqlHelper;
import org.sakaiproject.db.api.SqlReader;
import org.sakaiproject.db.api.SqlService;
import org.sakaiproject.entity.api.ContextObserver;
import org.sakaiproject.entity.api.Entity;
import org.sakaiproject.entity.api.EntityManager;
import org.sakaiproject.entity.api.EntityProducer;
import org.sakaiproject.entity.api.HttpAccess;
import org.sakaiproject.entity.api.Reference;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.event.api.Event;
import org.sakaiproject.event.api.EventTrackingService;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.tool.api.ContextSession;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.user.api.UserDirectoryService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * SiteVisitServiceImpl implements SiteVisitService.
 */
public class SiteVisitServiceImpl implements SiteVisitService, Observer, EntityProducer, ContextObserver
{
	protected class MutableBoolean
	{
		boolean value = false;
	}

	/** The "siteId" for a service visit (i.e. login). */
	protected static String SERVICE_ID = "SERVICE";

	/** Our logger. */
	private static Log M_log = LogFactory.getLog(SiteVisitServiceImpl.class);

	/** Configuration: to run the ddl on init or not. */
	protected boolean autoDdl = false;

	/** Dependency: EventTrackingService */
	protected EventTrackingService eventTrackingService = null;

	/** Dependency: EntityManager. */
	protected EntityManager entityManager = null;

	/** Dependency: SessionManager */
	protected SessionManager sessionManager = null;

	/** Dependency: SiteService. */
	protected SiteService siteService = null;

	/** Dependency: SqlService. */
	protected SqlService sqlService = null;

	/** Dependency: UserDirectoryService. */
	protected UserDirectoryService userDirectoryService = null;

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
	public Map<String, SiteVisit> getVisits(String context)
	{
		final Map<String, SiteVisit> rv = new HashMap<String, SiteVisit>();

		// let us NOT try this for the service level!
		if (context.equals(SERVICE_ID))
		{
			M_log.warn("getVisits cannot be called for context " + SERVICE_ID);
			return rv;
		}

		// Get all columns of the site visit table
		String sql = "SELECT CONTEXT,USER_ID,FIRST_VISIT,LAST_VISIT,VISITS FROM AM_SITE_VISIT WHERE CONTEXT=?";

		Object[] fields = new Object[1];
		fields[0] = context;

		this.sqlService.dbRead(sql, fields, new SqlReader()
		{

			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					int i = 1;
					// Create site visit objects out of the results
					SiteVisitImpl svImpl = new SiteVisitImpl();
					svImpl.setContext(SqlHelper.readString(result, i++));
					String userId = SqlHelper.readString(result, i++);
					svImpl.setUserId(userId);
					svImpl.setFirstSiteVisit(SqlHelper.readDate(result, i++));
					svImpl.setLastSiteVisit(SqlHelper.readDate(result, i++));
					svImpl.setVisits(SqlHelper.readInteger(result, i++));

					// Add them to a map keyed by user id
					rv.put(userId, (SiteVisit) svImpl);
					return null;
				}
				catch (SQLException e)
				{
					e.printStackTrace();
					M_log.warn("getLastVisits(context,num_of_days): " + e);
					return null;
				}
			}
		});

		return rv;
	}

	/**
	 * {@inheritDoc}
	 */
	public SiteVisit getServiceVisit(String userId)
	{
		final Map<String, SiteVisit> rv = new HashMap<String, SiteVisit>();

		// Get all columns of the site visit table
		String sql = "SELECT CONTEXT,USER_ID,FIRST_VISIT,LAST_VISIT,VISITS FROM AM_SITE_VISIT WHERE CONTEXT=\"" + SERVICE_ID + "\" AND USER_ID=?";

		Object[] fields = new Object[1];
		fields[0] = userId;

		this.sqlService.dbRead(sql, fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					int i = 1;
					// Create site visit objects out of the results
					SiteVisitImpl svImpl = new SiteVisitImpl();
					svImpl.setContext(SqlHelper.readString(result, i++));
					String userId = SqlHelper.readString(result, i++);
					svImpl.setUserId(userId);
					svImpl.setFirstSiteVisit(SqlHelper.readDate(result, i++));
					svImpl.setLastSiteVisit(SqlHelper.readDate(result, i++));
					svImpl.setVisits(SqlHelper.readInteger(result, i++));

					// Add them to a map keyed by user id
					rv.put(userId, (SiteVisit) svImpl);
					return null;
				}
				catch (SQLException e)
				{
					e.printStackTrace();
					M_log.warn("getLastVisits(context,num_of_days): " + e);
					return null;
				}
			}
		});

		return rv.get(userId);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeUserServiceVisits(String userId)
	{
		deleteServiceVisitRecord(userId);
	}

	/**********************************************************************************************************************************************************************************************************************************************************
	 * Observer implementation
	 *********************************************************************************************************************************************************************************************************************************************************/

	/**
	 * Final initialization, once all dependencies are set.
	 */
	public void init()
	{
		M_log.info("init()");
		if (this.autoDdl)
		{
			this.sqlService.ddl(this.getClass().getClassLoader(), "activitymeter");
		}
		// start watching the events - only those generated on this server, not
		// those from elsewhere
		this.eventTrackingService.addLocalObserver(this);
		// entity producer registration
		try
		{
			this.entityManager.registerEntityProducer(this, null);

			M_log.info("init()");
		}
		catch (Throwable t)
		{
			M_log.warn("init(): ", t);
		}
	}

	/**
	 * Configuration: to run the ddl on init or not.
	 * 
	 * @param value
	 *        the auto ddl value.
	 */
	public void setAutoDdl(String value)
	{
		autoDdl = new Boolean(value).booleanValue();
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

	public void setEventTrackingService(EventTrackingService service)
	{
		this.eventTrackingService = service;
	}

	/**
	 * Dependency: SessionManager.
	 * 
	 * @param service
	 *        The SessionManager.
	 */
	public void setSessionManager(SessionManager service)
	{
		sessionManager = service;
	}

	/**
	 * @param siteService
	 *        the siteService to set
	 */
	public void setSiteService(SiteService siteService)
	{
		this.siteService = siteService;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSqlService(SqlService service)
	{
		this.sqlService = service;
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
	 * This method is called whenever the observed object is changed. An application calls an <tt>Observable</tt> object's <code>notifyObservers</code> method to have all the object's observers notified of the change. default implementation is to cause
	 * the courier service to deliver to the interface controlled by my controller. Extensions can override.
	 * 
	 * @param o
	 *        the observable object.
	 * @param arg
	 *        an argument passed to the <code>notifyObservers</code> method.
	 */
	public void update(Observable o, Object arg)
	{
		// arg is Event
		if (!(arg instanceof Event)) return;
		Event event = (Event) arg;

		String function = event.getEvent();
		if (function == null) return;

		// for site visits
		if (function.equals("pres.begin"))
		{
			updateSiteVisit(event);
		}

		// for Service visits (i.e. logins)
		else if (function.equals("user.login"))
		{
			updateServiceVisit(event);
		}
	}

	/**
	 * Record an Service visit (login)
	 * 
	 * @param event
	 *        The event.
	 */
	protected void updateServiceVisit(Event event)
	{
		// Get the user id
		String userId = userDirectoryService.getCurrentUser().getId();
		if (userId == null) return;

		SiteVisitImpl sv = new SiteVisitImpl();
		sv.setContext(SERVICE_ID);
		sv.setUserId(userId);

		boolean recordInserted = insertVisitRecord(sv);

		// Entry exists already for this user and site
		// So update last visit time as this is a new session
		if (!recordInserted)
		{
			updateVisitRecord(sv);
		}
	}

	/**
	 * Record a site visit
	 * 
	 * @param event
	 *        The event.
	 */
	protected void updateSiteVisit(Event event)
	{
		// Get the site id from the event resource id
		String siteId = parseSiteId(event.getResource());
		if (siteId == null) return;

		// Get the user id
		String userId = userDirectoryService.getCurrentUser().getId();

		// If all values are not null, for pres.begin events
		if (userId != null && siteId != null)
		{
			// For non-my workspace sites and !admin site
			if (!checkWorkspaceSite(siteId))
			{
				ContextSession cs = this.sessionManager.getCurrentSession().getContextSession(siteId);
				// User may be in the same session, if so exit
				if (cs.getAttribute("AM-VISITED") != null) return;
				// Else set attribute so it may be checked again
				cs.setAttribute("AM-VISITED", Boolean.TRUE);

				SiteVisitImpl sv = new SiteVisitImpl();
				sv.setContext(siteId);
				sv.setUserId(userId);

				boolean recordInserted = insertVisitRecord(sv);

				// Entry exists already for this user and site
				// So update last visit time as this is a new session
				if (!recordInserted)
				{
					updateVisitRecord(sv);
				}
			}
		}
	}

	/**
	 * Check if the site is a myworkspace site
	 * 
	 * @param siteId
	 * @return true if the site is a myworkspace site, also returns true for admin workspace
	 */
	protected boolean checkWorkspaceSite(String siteId)
	{
		return this.siteService.isUserSite(siteId);
	}

	/**
	 * Insert a new visit record.
	 * 
	 * @param siteVisit
	 *        The site visit record.
	 * @return true if insert is successful, false if not
	 */
	protected boolean insertVisitRecord(final SiteVisitImpl siteVisit)
	{
		final MutableBoolean success = new MutableBoolean();
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				success.value = insertVisitRecordTx(siteVisit);
			}
		}, "insertVisitRecord: ");

		return success.value;
	}

	/**
	 * Insert a new visit record (transaction code).
	 * 
	 * @param siteVisit
	 *        The site visit record.
	 * @return true if insert is successful, false if not
	 */
	protected boolean insertVisitRecordTx(SiteVisitImpl siteVisit)
	{
		Date visitDate = new java.util.Date();
		Long visitTime = visitDate == null ? null : visitDate.getTime();
		String sql = "INSERT INTO AM_SITE_VISIT(CONTEXT, USER_ID, FIRST_VISIT, LAST_VISIT, VISITS) VALUES(?,?,?,?,?)";

		Object[] fields = new Object[5];
		int i = 0;
		fields[i++] = siteVisit.getContext();
		fields[i++] = siteVisit.getUserId();
		fields[i++] = visitTime;
		fields[i++] = visitTime;
		fields[i++] = 1;

		return this.sqlService.dbWrite(sql.toString(), fields);
	}

	/**
	 * Parses an event resource id and returns the site id
	 * 
	 * @param eventResourceId
	 * @return siteId or null
	 */
	protected String parseSiteId(String eventResourceId)
	{
		String siteId;
		if ((eventResourceId == null) || (!eventResourceId.endsWith("-presence"))) return null;
		siteId = eventResourceId.substring(0, eventResourceId.indexOf("-presence"));
		siteId = siteId.substring(10);
		return siteId;
	}

	/**
	 * Update an existing visit record.
	 * 
	 * @param siteVisit
	 *        The site visit record.
	 */
	protected void updateVisitRecord(final SiteVisitImpl siteVisit)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				updateVisitRecordTx(siteVisit);
			}
		}, "updateVisitRecord: ");
	}

	/**
	 * Update an existing site visit record.
	 * 
	 * @param siteVisit
	 *        The site visit record.
	 */
	protected void updateVisitRecordTx(SiteVisitImpl siteVisit)
	{
		Date visitDate = new java.util.Date();

		// Update session id,last visit time and number of visits
		String sql = "UPDATE AM_SITE_VISIT SET LAST_VISIT=?, VISITS=VISITS+1 WHERE CONTEXT=? AND USER_ID=?";

		Object[] fields = new Object[3];
		int i = 0;
		fields[i++] = visitDate.getTime();
		fields[i++] = siteVisit.getContext();
		fields[i++] = siteVisit.getUserId();

		if (!this.sqlService.dbWrite(sql.toString(), fields))
		{
			throw new RuntimeException("update Am Site Visit: db write failed");
		}
	}

	/**
	 * Delete site visit records.
	 * 
	 * @param context
	 *        The site visit id.
	 */
	protected void deleteVisitRecord(final String context)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				deleteVisitRecordTx(context);
			}
		}, "deleteVisitRecord: ");
	}

	/**
	 * Delete site visit records(transaction method).
	 * 
	 * @param context
	 *        The site id.
	 */
	protected void deleteVisitRecordTx(String context)
	{
		// Delete site visit records for this context
		String sql = "DELETE FROM AM_SITE_VISIT WHERE CONTEXT=?";

		Object[] fields = new Object[1];
		fields[0] = context;

		if (!this.sqlService.dbWrite(sql.toString(), fields))
		{
			throw new RuntimeException("delete Am Site Visit: db write failed");
		}
	}

	/**
	 * Delete site visit records.
	 * 
	 * @param context
	 *        The site visit id.
	 */
	protected void deleteServiceVisitRecord(final String userId)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				deleteServiceVisitRecordTx(userId);
			}
		}, "deleteVisitRecord: ");
	}

	/**
	 * Delete site visit records(transaction method).
	 * 
	 * @param context
	 *        The site id.
	 */
	protected void deleteServiceVisitRecordTx(String userId)
	{
		// Delete site visit records for this context
		String sql = "DELETE FROM AM_SITE_VISIT WHERE CONTEXT=\"" + SERVICE_ID + "\" AND USER_ID=?";

		Object[] fields = new Object[1];
		fields[0] = userId;

		if (!this.sqlService.dbWrite(sql.toString(), fields))
		{
			throw new RuntimeException("delete Am Service Visit: db write failed");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void contextDeleted(String context, boolean toolPlacement)
	{
		System.out.println("Context deleted");
		deleteVisitRecord(context);
	}

	/**
	 * @inheritDoc
	 */
	public String[] myToolIds()
	{
		return new String[0];
	}

	/**
	 * {@inheritDoc}
	 */
	public void contextCreated(String context, boolean toolPlacement)
	{

	}

	/**
	 * {@inheritDoc}
	 */
	public void contextUpdated(String context, boolean toolPlacement)
	{

	}

	/**
	 * {@inheritDoc}
	 */
	public HttpAccess getHttpAccess()
	{
		return null;
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
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getEntityUrl(Reference ref)
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getLabel()
	{
		return "SiteVisit";
	}

	/**
	 * {@inheritDoc}
	 */
	public String archive(String siteId, Document doc, Stack stack, String archivePath, List attachments)
	{
		return null;
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
	public boolean willArchiveMerge()
	{
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean parseEntityReference(String reference, Reference ref)
	{
		return false;
	}
}
