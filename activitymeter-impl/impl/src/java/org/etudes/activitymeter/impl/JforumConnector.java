/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/JforumConnector.java $
 * $Id: JforumConnector.java 8568 2014-08-30 06:26:16Z mallikamt $
 ***********************************************************************************
 *
 * Copyright (c) 2011, 2012, 2014 Etudes, Inc.
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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.activitymeter.api.ItemAccessStatus;
import org.etudes.activitymeter.api.ItemStatus;
import org.etudes.activitymeter.api.JforumItemDetail;
import org.etudes.activitymeter.api.JforumItemOverview;
import org.etudes.activitymeter.api.JforumOverview;
import org.etudes.activitymeter.api.ParticipantJforumItemDetail;
import org.etudes.api.app.jforum.Category;
import org.etudes.api.app.jforum.Evaluation;
import org.etudes.api.app.jforum.Forum;
import org.etudes.api.app.jforum.Grade;
import org.etudes.api.app.jforum.JForumService;
import org.etudes.api.app.jforum.Topic;

/**
 * JforumConnector are methods used by the Overview service to get JForum data.
 */
public class JforumConnector
{
	public static final String CATEGORY = "CAT";
	public static final String FORUM = "FORUM";
	public static final String TOPIC = "TOPIC";

	/** Our logger. */
	private static Log M_log = LogFactory.getLog(JforumConnector.class);

	/** Dependency: JForumService. */
	protected JForumService jforumService = null;

	/**
	 * Returns to uninitialized state.
	 */
	public void destroy()
	{
		M_log.info("destroy()");
	}

	/**
	 * Get a detail information for a JForum item (category, forum or topic) for active users in the site.
	 * 
	 * @param context
	 *        The site id.
	 * @param itemId
	 *        The JForum item id.
	 * @param activeParticipantIds
	 *        The set of active user ids.
	 * @return A JforumItemDetail.
	 */
	public JforumItemDetail getJforumItemDetail(String context, String itemId, Set<String> activeParticipantIds)
	{
		boolean categoryItem = itemId.startsWith(CATEGORY + "-");
		boolean forumItem = itemId.startsWith(FORUM + "-");
		boolean topicItem = itemId.startsWith(TOPIC + "-");

		int id;
		try
		{
			id = Integer.parseInt(itemId.substring(itemId.indexOf("-") + 1));

		}
		catch (NumberFormatException e)
		{
			M_log.warn("error in parsing of item id.", e);
			return null;
		}

		if (categoryItem)
		{
			Category category = this.jforumService.getUsersPostCountByCategory(context, id);

			if (category != null)
			{
				ItemAccessStatus status = ItemAccessStatus.open;
				Boolean isHideUntilOpen = Boolean.FALSE;

				Date openDate = category.getAccessDates().getOpenDate();
				if (isHideUntilOpen != null)
				{
					isHideUntilOpen = category.getAccessDates().isHideUntilOpen();
				}
				Date dueDate = category.getAccessDates().getDueDate();
				Date allowUntilDate = category.getAccessDates().getAllowUntilDate();

				Set<String> posters = new HashSet<String>();
				int numPosts = 0;
				// gradable categories
				if (category.isGradable() && (category.getForums() != null) && (category.getForums().isEmpty()))
				{
					for (Map.Entry<String, Integer> entry : category.getUserPostCount().entrySet())
					{
						String userId = entry.getKey();
						Integer userPostsCount = entry.getValue();
						if (!activeParticipantIds.contains(userId))
						{
							continue;
						}
						posters.add(userId);
						numPosts += userPostsCount.intValue();
					}

				}

				int pctPosted = 0;

				if (activeParticipantIds.size() > posters.size())
				{
					pctPosted = Math.round((new Float(posters.size()) / new Float(activeParticipantIds.size())) * 100);
				}
				else
				{
					pctPosted = 100;
				}
				int pctNotPosted = 100 - pctPosted;

				Date now = new Date();

				//if ((openDate != null) && (dueDate != null) && openDate.after(dueDate))
				if (((openDate != null) || (dueDate != null) || (allowUntilDate != null)) && (!category.getAccessDates().isDatesValid()))
				{
					status = ItemAccessStatus.invalid;
				}
				else if (openDate != null)
				{
					if (now.before(openDate))
					{
						if (isHideUntilOpen.booleanValue())
						{
							status = ItemAccessStatus.hiddenUntilOpen;
						}
						else
						{
							status = ItemAccessStatus.notOpen;
						}
					}
				}

				//if ((status == ItemAccessStatus.open) && ((dueDate != null) && (now.after(dueDate))) && (category.getAccessDates().isLocked()))
				if (status == ItemAccessStatus.open) 
				{
					if (allowUntilDate != null)
					{
						 if (now.after(allowUntilDate))
						 {
							 status = ItemAccessStatus.closed;
						 }
					}
					else if (dueDate != null)
					{
						 if (now.after(dueDate))
						 {
							 status = ItemAccessStatus.closed;
						 }
					}					
				}

				return new JforumItemDetailImpl(allowUntilDate, context, itemId, dueDate, openDate, Integer.valueOf(pctNotPosted), Integer.valueOf(pctPosted),
						status, category.getTitle());
			}

			return null;
		}
		else if (forumItem)
		{
			Category category = this.jforumService.getUsersPostCountByForum(context, id);

			if ((category != null) && (category.getForums().size() == 1))
			{
				ItemAccessStatus status = ItemAccessStatus.open;

				Date openDate = null;
				Date dueDate = null;
				//boolean isLocked = false;
				Date allowUntilDate = null;
				
				Boolean isHideUntilOpen = Boolean.FALSE;
				
				Forum forum = category.getForums().get(0);
				
				boolean validDates = true;

				Date now = new Date();

				if ((forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getDueDate() != null) || (forum.getAccessDates().getAllowUntilDate() != null))
				{
					openDate = forum.getAccessDates().getOpenDate();
					
					if (openDate != null)
					{
						isHideUntilOpen = forum.getAccessDates().isHideUntilOpen();
					}
					dueDate = forum.getAccessDates().getDueDate();
					allowUntilDate = forum.getAccessDates().getAllowUntilDate();

					validDates = forum.getAccessDates().isDatesValid();
					/*if ((dueDate != null) && (now.after(dueDate)) && (forum.getAccessDates().isLocked()))
					{
						isLocked = true;
					}*/
				}
				else
				{
					openDate = category.getAccessDates().getOpenDate();
					if (openDate != null)
					{
						isHideUntilOpen = category.getAccessDates().isHideUntilOpen();
					}
					dueDate = category.getAccessDates().getDueDate();
					allowUntilDate = category.getAccessDates().getAllowUntilDate();

					validDates = category.getAccessDates().isDatesValid();
					/*if ((dueDate != null) && (now.after(dueDate)) && (category.getAccessDates().isLocked()))
					{
						isLocked = true;
					}*/
				}

				Set<String> posters = new HashSet<String>();
				int numPosts = 0;

				for (Map.Entry<String, Integer> entry : forum.getUserPostCount().entrySet())
				{
					String userId = entry.getKey();
					Integer userPostsCount = entry.getValue();
					if (!activeParticipantIds.contains(userId))
					{
						continue;
					}
					posters.add(userId);
					numPosts += userPostsCount.intValue();
				}

				int pctPosted = 0;

				if (activeParticipantIds.size() > posters.size())
				{
					pctPosted = Math.round((new Float(posters.size()) / new Float(activeParticipantIds.size())) * 100);
				}
				else
				{
					pctPosted = 100;
				}
				int pctNotPosted = 100 - pctPosted;

				//if ((openDate != null) && (dueDate != null) && openDate.after(dueDate))
				if (((openDate != null) || (dueDate != null) || (allowUntilDate != null)) && (!validDates))
				{
					status = ItemAccessStatus.invalid;
				}
				// check for deny access
				else if (forum.getAccessType() == Forum.ACCESS_DENY)
				{
					status = ItemAccessStatus.denyAccess;
				}
				else if (openDate != null)
				{
					if (now.before(openDate))
					{
						if (isHideUntilOpen.booleanValue())
						{
							status = ItemAccessStatus.hiddenUntilOpen;
						}
						else
						{
							status = ItemAccessStatus.notOpen;
						}
					}
				}

				//if ((status == ItemAccessStatus.open) && (isLocked))
				if (status == ItemAccessStatus.open) 
				{
					if (allowUntilDate != null)
					{
						 if (now.after(allowUntilDate))
						 {
							 status = ItemAccessStatus.closed;
						 }
					}
					else if (dueDate != null)
					{
						 if (now.after(dueDate))
						 {
							 status = ItemAccessStatus.closed;
						 }
					}
				}

				return new JforumItemDetailImpl(allowUntilDate, context, itemId, dueDate, openDate, Integer.valueOf(pctNotPosted), Integer.valueOf(pctPosted),
						status, forum.getName());
			}

			return null;
		}
		else if (topicItem)
		{
			Category category = this.jforumService.getUsersPostCountByTopic(context, id);

			if ((category.getForums().size() == 1) && (category.getForums().get(0).getTopics().size() == 1))
			{
				Topic topic = category.getForums().get(0).getTopics().get(0);

				ItemAccessStatus status = ItemAccessStatus.open;

				Date openDate = null;
				Date dueDate = null;
				Date allowUntilDate = null;
				
				Boolean isHideUntilOpen = Boolean.FALSE;

				Forum forum = category.getForums().get(0);
				
				boolean validDates = true;

				Date now = new Date();

				//boolean isLocked = false;
				if ((topic.getAccessDates().getOpenDate() != null) || (topic.getAccessDates().getDueDate() != null) || (topic.getAccessDates().getAllowUntilDate() != null))
				{
					openDate = topic.getAccessDates().getOpenDate();
					if (openDate != null)
					{
						isHideUntilOpen = topic.getAccessDates().isHideUntilOpen();
					}
					
					dueDate = topic.getAccessDates().getDueDate();
					allowUntilDate = topic.getAccessDates().getAllowUntilDate();

					validDates = topic.getAccessDates().isDatesValid();
					/*if ((dueDate != null) && (now.after(dueDate)) && (topic.getAccessDates().isLocked()))
					{
						isLocked = true;
					}*/
				}
				else if ((forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getDueDate() != null) || (forum.getAccessDates().getAllowUntilDate() != null))
				{
					openDate = forum.getAccessDates().getOpenDate();
					if (openDate != null)
					{
						isHideUntilOpen = forum.getAccessDates().isHideUntilOpen();
					}
					dueDate = forum.getAccessDates().getDueDate();
					allowUntilDate = forum.getAccessDates().getAllowUntilDate();

					validDates = forum.getAccessDates().isDatesValid();
					/*if ((dueDate != null) && (now.after(dueDate)) && (forum.getAccessDates().isLocked()))
					{
						isLocked = true;
					}*/
				}
				else
				{
					openDate = category.getAccessDates().getOpenDate();
					if (openDate != null)
					{
						isHideUntilOpen = category.getAccessDates().isHideUntilOpen();
					}
					dueDate = category.getAccessDates().getDueDate();
					allowUntilDate = category.getAccessDates().getAllowUntilDate();

					validDates = category.getAccessDates().isDatesValid();
					/*if ((dueDate != null) && (now.after(dueDate)) && (category.getAccessDates().isLocked()))
					{
						isLocked = true;
					}*/
				}

				/*if (!isLocked)
				{
					if (topic.getStatus() == Topic.STATUS_LOCKED)
					{
						isLocked = true;
					}
				}*/
				
				Set<String> posters = new HashSet<String>();
				int numPosts = 0;

				for (Map.Entry<String, Integer> entry : topic.getUserPostCount().entrySet())
				{
					String userId = entry.getKey();
					Integer userPostsCount = entry.getValue();
					if (!activeParticipantIds.contains(userId))
					{
						continue;
					}
					posters.add(userId);
					numPosts += userPostsCount.intValue();
				}

				int pctPosted = 0;

				if (activeParticipantIds.size() > posters.size())
				{
					pctPosted = Math.round((new Float(posters.size()) / new Float(activeParticipantIds.size())) * 100);
				}
				else
				{
					pctPosted = 100;
				}
				int pctNotPosted = 100 - pctPosted;

				//if ((openDate != null) && (dueDate != null) && openDate.after(dueDate))
				if (((openDate != null) || (dueDate != null) || (allowUntilDate != null)) && (!validDates))
				{
					status = ItemAccessStatus.invalid;
				}
				// check for deny access
				else if (forum.getAccessType() == Forum.ACCESS_DENY)
				{
					status = ItemAccessStatus.denyAccess;
				}
				else if (openDate != null)
				{
					if (now.before(openDate))
					{
						if (isHideUntilOpen.booleanValue())
						{
							status = ItemAccessStatus.hiddenUntilOpen;
						}
						else
						{
							status = ItemAccessStatus.notOpen;
						}
					}
				}

				//if ((status == ItemAccessStatus.open) && (isLocked))
				if (status == ItemAccessStatus.open)
				{
					if (allowUntilDate != null)
					{
						 if (now.after(allowUntilDate))
						 {
							 status = ItemAccessStatus.closed;
						 }
					}
					else if (dueDate != null)
					{
						 if (now.after(dueDate))
						 {
							 status = ItemAccessStatus.closed;
						 }
					}
					
					if (status != ItemAccessStatus.closed)
					{
						if (topic.getStatus() == Topic.STATUS_LOCKED)
						{
							status = ItemAccessStatus.closed;
						}
					}
				}

				return new JforumItemDetailImpl(allowUntilDate, context, itemId, dueDate, openDate, Integer.valueOf(pctNotPosted), Integer.valueOf(pctPosted),
						status, topic.getTitle());

			}
			return null;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Get the overview information for all JForum items for active users in the site, in JForum order.
	 * 
	 * @param context
	 *        The site id.
	 * @param activeParticipantIds
	 *        The set of active user ids.
	 * @return a List of JforumItemOverview.
	 */
	public List<JforumItemOverview> getJforumItemOverviews(String context, Set<String> activeParticipantIds)
	{
		List<Category> categories = this.jforumService.getGradableItemsUserPostsCountByContext(context);

		List<JforumItemOverview> rv = new ArrayList<JforumItemOverview>();

		ItemAccessStatus accessStatus = ItemAccessStatus.open;

		for (Category category : categories)
		{
			// gradable categories
			if (category.isGradable())
			{
				String id = null;
				ItemStatus status = ItemStatus.published;
				Set<String> posters = new HashSet<String>();
				int numPosts = 0;
				Date open = null;
				Date due = null;
				Date allowUntilDate = null;
				Boolean isHideUntilOpen = Boolean.FALSE;
				
				accessStatus = ItemAccessStatus.open;

				Date now = new Date();

				for (Map.Entry<String, Integer> entry : category.getUserPostCount().entrySet())
				{
					String userId = entry.getKey();
					Integer userPostsCount = entry.getValue();
					if (!activeParticipantIds.contains(userId))
					{
						continue;
					}
					posters.add(userId);
					numPosts += userPostsCount.intValue();
				}

				id = JforumConnector.CATEGORY + "-" + String.valueOf(category.getId());

				if ((category.getAccessDates().getDueDate() != null) || (category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getAllowUntilDate() != null))
				{
					open = category.getAccessDates().getOpenDate();
					
					if (open != null)
					{
						isHideUntilOpen = category.getAccessDates().isHideUntilOpen();
					}
					
					due = category.getAccessDates().getDueDate();
					allowUntilDate = category.getAccessDates().getAllowUntilDate();

					/*
					 * if (due != null) { if (!category.getAccessDates().isLocked()) { status = ItemStatus.published; } }
					 */
				}

				// check for invalid (Note: categories cannot have "deny access"
				// check for invalid
				//if ((open != null) && (due != null) && open.after(due))
				if (((open != null) || (due != null) || (allowUntilDate != null)) && (!category.getAccessDates().isDatesValid()))
				{
					status = ItemStatus.invalid;
					accessStatus = ItemAccessStatus.invalid;
				}
				else if (open != null)
				{
					//if (now.before(open))
					if (now.before(open))
					{
						if (isHideUntilOpen.booleanValue())
						{
							status = ItemStatus.na;
							accessStatus = ItemAccessStatus.hiddenUntilOpen;
						}
						else
						{
							status = ItemStatus.na;
							accessStatus = ItemAccessStatus.notOpen;
						}						
					}
				}
				else if (due == null)
				{
					boolean forumsOpen = false;

					// check all forum dates for access status
					for (Forum forum : category.getForums())
					{
						if (forum.getAccessDates() != null)
						{
							if ((forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getDueDate() != null) || (forum.getAccessDates().getAllowUntilDate() != null))
							{
								if (forum.getAccessDates().getOpenDate() == null)
								{
									forumsOpen = true;
								}
								else if (now.after(forum.getAccessDates().getOpenDate()))
								{
									forumsOpen = true;
								}
							}
							else
							{
								forumsOpen = true;
							}
						}
						else
						{
							forumsOpen = true;
						}

						if (forumsOpen)
						{
							break;
						}
					}

					if (!forumsOpen)
					{
						status = ItemStatus.na;
						accessStatus = ItemAccessStatus.notOpen;
					}
				}
				else if (category.getForums().size() == 0)
				{
					status = ItemStatus.na;
					accessStatus = ItemAccessStatus.notOpen;
				}

				rv.add(new JforumItemOverviewImpl(id, Integer.valueOf(posters.size()), Integer.valueOf(numPosts), status, category.getTitle(),
						accessStatus));
			}
			else
			{
				List<Forum> forums = category.getForums();

				for (Forum forum : forums)
				{
					if (forum.getGradeType() == Grade.GRADE_BY_FORUM)
					{

						String id = null;
						ItemStatus status = ItemStatus.published;
						Set<String> posters = new HashSet<String>();
						int numPosts = 0;
						Date open = null;
						Date due = null;
						Date allowUntilDate = null;
						Boolean isHideUntilOpen = Boolean.FALSE;
						
						boolean validDates = true;
						
						accessStatus = ItemAccessStatus.open;

						for (Map.Entry<String, Integer> entry : forum.getUserPostCount().entrySet())
						{
							String userId = entry.getKey();
							Integer userPostsCount = entry.getValue();
							if (!activeParticipantIds.contains(userId))
							{
								continue;
							}
							posters.add(userId);
							numPosts += userPostsCount.intValue();
						}

						id = JforumConnector.FORUM + "-" + String.valueOf(forum.getId());

						if ((forum.getAccessDates().getDueDate() != null) || (forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getAllowUntilDate() != null))
						{
							open = forum.getAccessDates().getOpenDate();
							if (open != null)
							{
								isHideUntilOpen = forum.getAccessDates().isHideUntilOpen();
							}
							
							due = forum.getAccessDates().getDueDate();
							allowUntilDate = forum.getAccessDates().getAllowUntilDate();

							/*
							 * if (due != null) { if (!forum.getAccessDates().isLocked()) { status = ItemStatus.published; } }
							 */
							validDates = forum.getAccessDates().isDatesValid();
						}
						else if ((category.getAccessDates().getDueDate() != null) || (category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getAllowUntilDate() != null))
						{
							open = category.getAccessDates().getOpenDate();
							if (open != null)
							{
								isHideUntilOpen = category.getAccessDates().isHideUntilOpen();
							}
							
							due = category.getAccessDates().getDueDate();
							allowUntilDate = category.getAccessDates().getAllowUntilDate();

							/*
							 * if (due != null) { if (!category.getAccessDates().isLocked()) { status = ItemStatus.published; } }
							 */
							validDates = category.getAccessDates().isDatesValid();
						}

						// check for invalid
						//if ((open != null) && (due != null) && open.after(due))
						if (((open != null) || (due != null) || (allowUntilDate != null)) && (!validDates))
						{
							status = ItemStatus.invalid;
							accessStatus = ItemAccessStatus.invalid;
						}
						// otherwise check for deny access
						else if (forum.getAccessType() == Forum.ACCESS_DENY)
						{
							status = ItemStatus.denyAccess;
							accessStatus = ItemAccessStatus.denyAccess;
						}
						else if (open != null)
						{
							Date now = new Date();
							if (now.before(open))
							{
								if (isHideUntilOpen.booleanValue())
								{
									status = ItemStatus.na;
									accessStatus = ItemAccessStatus.hiddenUntilOpen;
								}
								else
								{
									status = ItemStatus.na;
									accessStatus = ItemAccessStatus.notOpen;
								}
							}
						}

						rv.add(new JforumItemOverviewImpl(id, Integer.valueOf(posters.size()), Integer.valueOf(numPosts), status, forum.getName(),
								accessStatus));
					}
					else
					{
						List<Topic> topics = forum.getTopics();

						for (Topic topic : topics)
						{
							if (forum.getGradeType() == Grade.GRADE_BY_TOPIC)
							{
								String id = null;
								ItemStatus status = ItemStatus.published;
								Set<String> posters = new HashSet<String>();
								int numPosts = 0;
								Date open = null;
								Date due = null;
								Date allowUntilDate = null;
								Boolean isHideUntilOpen = Boolean.FALSE;
								
								boolean validDates = true;
								
								accessStatus = ItemAccessStatus.open;

								for (Map.Entry<String, Integer> entry : topic.getUserPostCount().entrySet())
								{
									String userId = entry.getKey();
									Integer userPostsCount = entry.getValue();
									if (!activeParticipantIds.contains(userId))
									{
										continue;
									}
									posters.add(userId);
									numPosts += userPostsCount.intValue();
								}

								id = JforumConnector.TOPIC + "-" + String.valueOf(topic.getId());

								if ((topic.getAccessDates().getDueDate() != null) || (topic.getAccessDates().getOpenDate() != null) || (topic.getAccessDates().getAllowUntilDate() != null))
								{
									open = topic.getAccessDates().getOpenDate();
									if (open != null)
									{
										isHideUntilOpen = topic.getAccessDates().isHideUntilOpen();
									}
									
									due = topic.getAccessDates().getDueDate();
									allowUntilDate = topic.getAccessDates().getAllowUntilDate();

									validDates = topic.getAccessDates().isDatesValid();
									/*
									 * if (due != null) { if (!topic.getAccessDates().isLocked()) { accessAfterClose = Boolean.TRUE; } }
									 */
								}
								else if ((forum.getAccessDates().getDueDate() != null) || (forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getAllowUntilDate() != null))
								{
									open = forum.getAccessDates().getOpenDate();
									if (open != null)
									{
										isHideUntilOpen = forum.getAccessDates().isHideUntilOpen();
									}
									
									due = forum.getAccessDates().getDueDate();
									allowUntilDate = forum.getAccessDates().getAllowUntilDate();

									validDates = forum.getAccessDates().isDatesValid();
									/*
									 * if (due != null) { if (!forum.getAccessDates().isLocked()) { status = ItemStatus.published; } }
									 */
								}
								else if ((category.getAccessDates().getDueDate() != null) || (category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getAllowUntilDate() != null))
								{
									open = category.getAccessDates().getOpenDate();
									if (open != null)
									{
										isHideUntilOpen = category.getAccessDates().isHideUntilOpen();
									}
									
									due = category.getAccessDates().getDueDate();
									allowUntilDate = category.getAccessDates().getAllowUntilDate();
									
									validDates = category.getAccessDates().isDatesValid();
									/*
									 * if (due != null) { if (!category.getAccessDates().isLocked()) { status = ItemStatus.published; } }
									 */
								}

								// check for invalid
								//if ((open != null) && (due != null) && open.after(due))
								if (((open != null) || (due != null) || (allowUntilDate != null)) && (!validDates))
								{
									status = ItemStatus.invalid;
									accessStatus = ItemAccessStatus.invalid;
								}
								// otherwise check for deny access
								else if (forum.getAccessType() == Forum.ACCESS_DENY)
								{
									status = ItemStatus.denyAccess;
									accessStatus = ItemAccessStatus.denyAccess;
								}
								else if (open != null)
								{
									Date now = new Date();
									if (now.before(open))
									{
										if (isHideUntilOpen.booleanValue())
										{
											status = ItemStatus.na;
											accessStatus = ItemAccessStatus.hiddenUntilOpen;
										}
										else
										{
											status = ItemStatus.na;
											accessStatus = ItemAccessStatus.notOpen;
										}
									}
								}

								rv.add(new JforumItemOverviewImpl(id, Integer.valueOf(posters.size()), Integer.valueOf(numPosts), status, topic
										.getTitle(), accessStatus));
							}
						}
					}
				}
			}
		}

		return rv;
	}

	/**
	 * Get overview information for JForum for active users in this site.
	 * 
	 * @param context
	 *        The site id.
	 * @param activeParticipantIds
	 *        The set of active user ids.
	 * @return A JforumOverview.
	 */
	public JforumOverview getJforumOverview(String context, Set<String> activeParticipantIds)
	{
		Map<String, Integer> postsCount = this.jforumService.getContextUserPostsCount(context);

		Set<String> posters = new HashSet<String>();
		int numPosts = 0;

		for (String activeParticipantId : activeParticipantIds)
		{
			if (postsCount.containsKey(activeParticipantId))
			{
				posters.add(activeParticipantId);

				Integer userPostsCount = postsCount.get(activeParticipantId);
				numPosts += userPostsCount.intValue();
			}
		}

		int pctPosted = 0;

		if (activeParticipantIds.size() > posters.size())
		{
			pctPosted = Math.round((new Float(posters.size()) / new Float(activeParticipantIds.size())) * 100);
		}
		else
		{
			pctPosted = 100;
		}
		int pctNotPosted = 100 - pctPosted;

		return new JforumOverviewImpl(Integer.valueOf(numPosts), Integer.valueOf(pctNotPosted), Integer.valueOf(pctPosted));
	}

	/**
	 * Check with JForum to find the number of posts per user for the JForum item.
	 * 
	 * @param context
	 *        The site id.
	 * @param itemId
	 *        The JForum item id.
	 * @return The map, keyed by user id, data is the count.
	 */
	public Map<String, Integer> getNumJforumItemPosts(String context, String itemId, List<Participant> participants)
	{
		boolean categoryItem = itemId.startsWith(CATEGORY + "-");
		boolean forumItem = itemId.startsWith(FORUM + "-");
		boolean topicItem = itemId.startsWith(TOPIC + "-");

		int id;
		try
		{
			id = Integer.parseInt(itemId.substring(itemId.indexOf("-") + 1));

		}
		catch (NumberFormatException e)
		{
			M_log.warn("error in parsing of item id.", e);
			return null;
		}

		Map<String, Integer> rv = new HashMap<String, Integer>();

		if (categoryItem)
		{
			Category category = this.jforumService.getUsersPostCountByCategory(context, id);

			if (category != null)
			{
				if (category.isGradable() && (category.getForums() != null) && (category.getForums().isEmpty()))
				{
					Map<String, Integer> posters = category.getUserPostCount();

					for (Participant p : participants)
					{
						if (posters.containsKey(p.userId))
						{
							rv.put(p.userId, posters.get(p.userId));
						}
						else
						{
							rv.put(p.userId, Integer.valueOf(0));
						}
					}
				}
			}
		}
		else if (forumItem)
		{
			Category category = this.jforumService.getUsersPostCountByForum(context, id);

			if ((category != null) && (category.getForums().size() == 1))
			{
				Forum forum = category.getForums().get(0);

				Map<String, Integer> posters = forum.getUserPostCount();

				for (Participant p : participants)
				{
					if (posters.containsKey(p.userId))
					{
						rv.put(p.userId, posters.get(p.userId));
					}
					else
					{
						rv.put(p.userId, Integer.valueOf(0));
					}
				}
			}
		}
		else if (topicItem)
		{
			Category category = this.jforumService.getUsersPostCountByTopic(context, id);

			if ((category.getForums().size() == 1) && (category.getForums().get(0).getTopics().size() == 1))
			{
				Topic topic = category.getForums().get(0).getTopics().get(0);

				Map<String, Integer> posters = topic.getUserPostCount();

				for (Participant p : participants)
				{
					if (posters.containsKey(p.userId))
					{
						rv.put(p.userId, posters.get(p.userId));
					}
					else
					{
						rv.put(p.userId, Integer.valueOf(0));
					}
				}
			}
		}

		return rv;
	}

	/**
	 * Check with JForum to find the number of posts and evaluations for the JForum item.
	 * 
	 * @param context
	 *        Context The site id.
	 * @param itemId
	 *        Item id The JForum item id.
	 * @param participants
	 *        The set of active user ids.
	 * @return List of ParticipantJforumItemDetail
	 */
	public List<ParticipantJforumItemDetail> getNumJforumItemPostsEvaluations(String context, String itemId, List<Participant> participants)
	{
		boolean categoryItem = itemId.startsWith(CATEGORY + "-");
		boolean forumItem = itemId.startsWith(FORUM + "-");
		boolean topicItem = itemId.startsWith(TOPIC + "-");

		int id;
		try
		{
			id = Integer.parseInt(itemId.substring(itemId.indexOf("-") + 1));

		}
		catch (NumberFormatException e)
		{
			M_log.warn("error in parsing of item id.", e);
			return null;
		}

		ArrayList<ParticipantJforumItemDetail> rv = new ArrayList<ParticipantJforumItemDetail>();

		if (categoryItem)
		{
			Category category = this.jforumService.getUsersPostCountByCategory(context, id);

			if (category != null)
			{
				if (category.isGradable() && (category.getForums() != null) && (category.getForums().isEmpty()))
				{
					Map<String, Integer> posters = category.getUserPostCount();

					Category catEval = this.jforumService.getEvaluationsByCategory(context, id);

					List<Evaluation> catEvaluations = null;
					if (catEval != null)
					{
						catEvaluations = catEval.getEvaluations();
					}

					Map<String, Evaluation> userEvaluations = new HashMap<String, Evaluation>();

					if (catEvaluations != null)
					{
						for (Evaluation eval : catEvaluations)
						{
							userEvaluations.put(eval.getSakaiUserId(), eval);
						}
					}

					for (Participant p : participants)
					{
						/*
						 * if (posters.containsKey(p.userId)) { rv.put(p.userId, posters.get(p.userId)); } else { rv.put(p.userId, Integer.valueOf(0)); }
						 */
						Date evaluated = null;
						Date reviewed = null;
						Integer posts = Integer.valueOf(0);

						if (userEvaluations.containsKey(p.userId))
						{
							Evaluation userEvaluation = userEvaluations.get(p.userId);

							evaluated = userEvaluation.getEvaluatedDate();
							reviewed = userEvaluation.getReviewedDate();
						}

						if (posters.containsKey(p.userId))
						{
							posts = posters.get(p.userId);

							ParticipantJforumItemDetail pjid = new ParticipantJforumItemDetailImpl(p.userId, p.sortName, p.status, posts, evaluated,
									reviewed);
							rv.add(pjid);
						}
						else
						{
							ParticipantJforumItemDetail pjid = new ParticipantJforumItemDetailImpl(p.userId, p.sortName, p.status, posts, evaluated,
									reviewed);
							rv.add(pjid);
						}
					}
				}
			}
		}
		else if (forumItem)
		{
			Category category = this.jforumService.getUsersPostCountByForum(context, id);

			if ((category != null) && (category.getForums().size() == 1))
			{
				Forum forum = category.getForums().get(0);

				Map<String, Integer> posters = forum.getUserPostCount();

				Category catEval = this.jforumService.getEvaluationsByForum(context, id);

				List<Evaluation> forumEvaluations = null;
				if (catEval != null)
				{
					if (catEval.getForums().size() == 1)
					{
						Forum forumEval = catEval.getForums().get(0);

						forumEvaluations = forumEval.getEvaluations();
					}
				}

				Map<String, Evaluation> userEvaluations = new HashMap<String, Evaluation>();

				if (forumEvaluations != null)
				{
					for (Evaluation eval : forumEvaluations)
					{
						userEvaluations.put(eval.getSakaiUserId(), eval);
					}
				}

				for (Participant p : participants)
				{
					Date evaluated = null;
					Date reviewed = null;
					Integer posts = Integer.valueOf(0);

					if (userEvaluations.containsKey(p.userId))
					{
						Evaluation userEvaluation = userEvaluations.get(p.userId);

						evaluated = userEvaluation.getEvaluatedDate();
						reviewed = userEvaluation.getReviewedDate();
					}

					if (posters.containsKey(p.userId))
					{
						posts = posters.get(p.userId);

						ParticipantJforumItemDetail pjid = new ParticipantJforumItemDetailImpl(p.userId, p.sortName, p.status, posts, evaluated,
								reviewed);
						rv.add(pjid);
					}
					else
					{
						ParticipantJforumItemDetail pjid = new ParticipantJforumItemDetailImpl(p.userId, p.sortName, p.status, posts, evaluated,
								reviewed);
						rv.add(pjid);
					}
				}
			}
		}
		else if (topicItem)
		{
			Category category = this.jforumService.getUsersPostCountByTopic(context, id);

			if ((category.getForums().size() == 1) && (category.getForums().get(0).getTopics().size() == 1))
			{
				Topic topic = category.getForums().get(0).getTopics().get(0);

				Map<String, Integer> posters = topic.getUserPostCount();

				Category catEval = this.jforumService.getEvaluationsByTopic(context, id);

				List<Evaluation> topicEvaluations = null;
				if (catEval != null)
				{
					if ((catEval.getForums().size() == 1) && (catEval.getForums().get(0).getTopics().size() == 1))
					{
						Topic topicEval = catEval.getForums().get(0).getTopics().get(0);

						topicEvaluations = topicEval.getEvaluations();
					}
				}

				Map<String, Evaluation> userEvaluations = new HashMap<String, Evaluation>();

				if (topicEvaluations != null)
				{
					for (Evaluation eval : topicEvaluations)
					{
						userEvaluations.put(eval.getSakaiUserId(), eval);
					}
				}

				for (Participant p : participants)
				{
					Date evaluated = null;
					Date reviewed = null;
					Integer posts = Integer.valueOf(0);

					if (userEvaluations.containsKey(p.userId))
					{
						Evaluation userEvaluation = userEvaluations.get(p.userId);

						evaluated = userEvaluation.getEvaluatedDate();
						reviewed = userEvaluation.getReviewedDate();
					}

					if (posters.containsKey(p.userId))
					{
						posts = posters.get(p.userId);

						ParticipantJforumItemDetail pjid = new ParticipantJforumItemDetailImpl(p.userId, p.sortName, p.status, posts, evaluated,
								reviewed);
						rv.add(pjid);
					}
					else
					{
						ParticipantJforumItemDetail pjid = new ParticipantJforumItemDetailImpl(p.userId, p.sortName, p.status, posts, evaluated,
								reviewed);
						rv.add(pjid);
					}
				}
			}
		}

		return rv;
	}

	/**
	 * Check with JForum to find the number of posts per user, total in the site.
	 * 
	 * @param context
	 *        The site id.
	 * @return The map, keyed by user id, data is the count.
	 */
	public Map<String, Integer> getNumJforumSitePosts(String context, List<Participant> participants)
	{
		Map<String, Integer> postsCount = this.jforumService.getContextUserPostsCount(context);

		Map<String, Integer> rv = new HashMap<String, Integer>();

		for (Participant p : participants)
		{
			if (postsCount.containsKey(p.userId))
			{
				rv.put(p.userId, postsCount.get(p.userId));
			}
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
	 * @param jforumService
	 *        the jforumService to set
	 */
	public void setJforumService(JForumService jforumService)
	{
		this.jforumService = jforumService;
	}
}
