/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-api/api/src/java/org/etudes/activitymeter/api/OverviewService.java $
 * $Id: OverviewService.java 4804 2013-04-24 19:39:19Z ggolden $
 ***********************************************************************************
 *
 * Copyright (c) 2011, 2013 Etudes, Inc.
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

package org.etudes.activitymeter.api;

import java.util.List;

/**
 * OverviewService supports activity meter's data needs.<br />
 * Note: lists of "Student" participants and the counts and percents based on these include all "Student" role users in the site, active or inactive, blocked or not.
 */
public interface OverviewService
{
	/**
	 * Check if the given user is allowed to access activity meter information.
	 * 
	 * @param context
	 *        The site id.
	 * @param userId
	 *        The user id.
	 * @return TRUE if the user has access, FALSE if not.
	 */
	Boolean allowActivityAccess(String context, String userId);

	/**
	 * Get a detail information for a JForum item (category, forum or topic) for active participants.
	 * 
	 * @param context
	 *        The site id.
	 * @param itemId
	 *        The JForum item id.
	 * @return A JforumItemDetail.
	 */
	JforumItemDetail getJforumItemDetail(String context, String itemId);

	/**
	 * Get the overview information for all JForum items for active participants, in JForum order.
	 * 
	 * @param context
	 *        The site id.
	 * @return a List of JforumItemOverview.
	 */
	List<JforumItemOverview> getJforumItemOverviews(String context);

	/**
	 * Get overview information for JForum for active participants.
	 * 
	 * @param context
	 *        The site id.
	 * @return A JforumOverview.
	 */
	JforumOverview getJforumOverview(String context);

	/**
	 * Get detail information for a melete item (section) for active participants.
	 * 
	 * @param context
	 *        The site id.
	 * @param sectionId
	 *        The section id.
	 * @return A MeleteSectionOverview.
	 */
	MeleteItemDetail getMeleteItemDetail(String context, String sectionId);

	/**
	 * Get overview information for Melete items, modules and sections, for active participants, in Melete order (each Module followed by its section).
	 * 
	 * @param context
	 *        The site id.
	 * @return a List of MeleteOverviewItem.
	 */
	List<MeleteItemOverview> getMeleteItemOverviews(String context);

	/**
	 * Get overview information for melete for active participants.
	 * 
	 * @param context
	 *        The site id.
	 * @return A MeleteOverview.
	 */
	MeleteOverview getMeleteOverview(String context);

	/**
	 * Get detail information for a Mneme item (assessment) for active participants.
	 * 
	 * @param context
	 *        The site id.
	 * @param itemId
	 *        The assessment id.
	 * @return A MnemeItemDetail.
	 */
	MnemeItemDetail getMnemeItemDetail(String context, String itemId);

	/**
	 * Get overview information for all Mneme items for active participants, in Mneme order.
	 * 
	 * @param context
	 *        The site id.
	 * @return a List of MnemeOverviewItem.
	 */
	List<MnemeItemOverview> getMnemeItemOverviews(String context);

	/**
	 * Get overview information for Mneme for active participants.
	 * 
	 * @param context
	 *        The site id.
	 * @return A MnemeOverview.
	 */
	MnemeOverview getMnemeOverview(String context);

	/**
	 * Get overview information for this site, using this time period for periodic information.
	 * 
	 * @param context
	 *        The site id.
	 * @param period
	 *        The number of days for the period of the overview (see getNumNotVisitedInPeriod())
	 * @return An Overview.
	 */
	Overview getOverview(String context, Integer period);

	/**
	 * Get detail information for a JForum item (category, topic, or forum) for all "Student" participants in the site.
	 * 
	 * @param context
	 *        The site id.
	 * @param itemId
	 *        The JForum item id.
	 * @param sort
	 *        TODO: sort criteria.
	 * @return a List of ParticipantJforumItemOverview.
	 */
	List<ParticipantJforumItemDetail> getParticipantJforumItemDetails(String context, String itemId, ParticipantJforumItemDetailsSort sort);

	/**
	 * Get detail information for a Melete item (section) for all "Student" participants in the site.
	 * 
	 * @param context
	 *        The site id.
	 * @param sectionId
	 *        The section id.
	 * @param sort
	 *        TODO: sort criteria.
	 * @return a List of ParticipantMeleteSectionOverview.
	 */
	List<ParticipantMeleteItemDetail> getParticipantMeleteItemDetails(String context, String sectionId, ParticipantMeleteItemDetailsSort sort);

	/**
	 * Get detail information for a Mneme item (assessment) for all "Student" participants in the site.
	 * 
	 * @param context
	 *        The site id.
	 * @param itemId
	 *        The assessment id.
	 * @param sort
	 *        TODO: sort criteria.
	 * @return a List of ParticipantMnemeItemOverview.
	 */
	List<ParticipantMnemeItemDetail> getParticipantMnemeItemDetails(String context, String itemId, ParticipantMnemeItemDetailsSort sort);

	/**
	 * Get overview information for all the "Student" participants in the site.
	 * 
	 * @param context
	 *        The site id.
	 * @param sort
	 *        sort criteria.
	 * @param includeAll
	 *        if true, include instructors, TAs and student, else include just students.
	 * @return a List of ParticipantOverview.
	 */
	List<ParticipantOverview> getParticipantOverviews(String context, ParticipantOverviewsSort sort, boolean includeAll);

	/**
	 * Get overview information for the site for all "Student" participants in the site.
	 * 
	 * @param context
	 *        The site id.
	 * @param sort
	 *        TODO: sort criteria.
	 * @return a List of ParticipantSiteOverview.
	 */
	List<ParticipantSiteOverview> getParticipantSiteOverviews(String context, ParticipantSiteOverviewsSort sort);

	/**
	 * Get overview information for all the "Student" participants in the site who have not visited in the period.
	 * 
	 * @param context
	 *        The site id.
	 * @param period
	 *        The number of days for the period of the overview (see getNumNotVisitedInPeriod())
	 * @param sort
	 *        sort criteria.
	 * @return a List of ParticipantOvervies.
	 */
	List<ParticipantOverview> getParticipantsNotVisited(String context, Integer period, ParticipantOverviewsSort sort);

	/**
	 * Get overview information for syllabus for all the "Student" participants in the site.
	 * 
	 * @param context
	 *        The site id.
	 * @param sort
	 *        TODO: sort criteria.
	 * @return a List of ParticipantSyllabusOverview.
	 */
	List<ParticipantSyllabusOverview> getParticipantSyllabusOverviews(String context, ParticipantSyllabusOverviewsSort sort);

	/**
	 * Get some display information for the site.
	 * 
	 * @param context
	 *        The site id.
	 * @return The SiteInfo.
	 */
	SiteInfo getSiteInfo(String context);

	/**
	 * Get an overview information for this site, using this time period for periodic information, for active participants.
	 * 
	 * @param context
	 *        The site id.
	 * @param period
	 *        The number of days for the period of the overview (see getNumNotVisitedInPeriod())
	 * @return A SiteOverview.
	 */
	SiteOverview getSiteOverview(String context, Integer period);

	/**
	 * Get a overview for this user in this site.
	 * 
	 * @param context
	 *        The site id.
	 * @param userId
	 *        The user id.
	 * @return a StudentOverview.
	 */
	StudentOverview getStudentOverview(String context, String userId);

	/**
	 * Get a overview for syllabus for active participants.
	 * 
	 * @param context
	 *        The site id.
	 * @return A SyllabusOverview.
	 */
	SyllabusOverview getSyllabusOverview(String context);
}
