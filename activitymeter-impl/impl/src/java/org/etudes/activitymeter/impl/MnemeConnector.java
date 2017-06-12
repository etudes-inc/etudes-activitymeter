/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/java/org/etudes/activitymeter/impl/MnemeConnector.java $
 * $Id: MnemeConnector.java 9344 2014-11-25 00:12:42Z ggolden $
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
import org.etudes.activitymeter.api.MnemeItemDetail;
import org.etudes.activitymeter.api.MnemeItemOverview;
import org.etudes.activitymeter.api.MnemeOverview;
import org.etudes.activitymeter.api.ParticipantMnemeItemDetail;
import org.etudes.mneme.api.Assessment;
import org.etudes.mneme.api.AssessmentService;
import org.etudes.mneme.api.AssessmentService.AssessmentsSort;
import org.etudes.mneme.api.AssessmentType;
import org.etudes.mneme.api.Submission;
import org.etudes.mneme.api.SubmissionCompletionStatus;
import org.etudes.mneme.api.SubmissionService;
import org.etudes.mneme.api.SubmissionService.FindAssessmentSubmissionsSort;
import org.sakaiproject.authz.api.Member;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;

/**
 * MnemeConnector are methods used by the Overview service to get Mneme data.
 */
public class MnemeConnector
{
	/** Our logger. */
	private static Log M_log = LogFactory.getLog(MnemeConnector.class);

	/** Assessment service. */
	protected AssessmentService assessmentService = null;

	/** Submission service. */
	protected SubmissionService submissionService = null;

	/**
	 * Returns to uninitialized state.
	 */
	public void destroy()
	{
		M_log.info("destroy()");
	}

	/**
	 * Get detail information for a Mneme item (assessment).
	 * 
	 * @param context
	 *        The site id.
	 * @param itemId
	 *        The assessment id.
	 * @param activeParticipantIds
	 *        The user ids for active users.
	 * @return A MnemeItemDetail.
	 */
	public MnemeItemDetail getMnemeItemDetail(String context, String itemId, Set<String> activeParticipantIds)
	{
		// get the assessment
		Assessment assessment = this.assessmentService.getAssessment(itemId);
		if (assessment == null) throw new IllegalArgumentException();

		// get all the submissions - for ALL possible users (no filtering by existence or permissions)
		List<? extends Submission> submissions = this.submissionService.getContextSubmissions(context);
		// TODO: just for this assessment

		Set<String> usersSubmitted = new HashSet<String>();
		Set<String> usersStarted = new HashSet<String>();
		int numInProgress = 0;

		// filter activeParticipantIds to drop non-students and non-provideds for FCEs
		if (assessment.getFormalCourseEval())
		{
			try
			{
				Set<String> filtered = new HashSet<String>();
				Site site = siteService().getSite(context);
				for (String uid : activeParticipantIds)
				{
					Member m = site.getMember(uid);
					if ((m != null) && m.isProvided() && m.getRole().getId().equals("Student"))
					{
						filtered.add(uid);
					}
				}

				activeParticipantIds = filtered;
			}
			catch (IdUnusedException e)
			{
			}

		}
		for (Submission s : submissions)
		{
			// skip any not from our active user list
			if (!activeParticipantIds.contains(s.getUserId())) continue;

			// just for this assessment
			if (!s.getAssessment().getId().equals(itemId)) continue;

			if (!s.getIsComplete())
			{
				numInProgress++;
			}

			else
			{
				usersSubmitted.add(s.getUserId());
			}

			usersStarted.add(s.getUserId());
		}

		int numSubmitted = usersSubmitted.size();
		int numNotSubmitted = activeParticipantIds.size() - usersStarted.size();
		int pctSubmitted = activeParticipantIds.size() > 0 ? ((numSubmitted * 100) / activeParticipantIds.size()) : 0;

		ItemAccessStatus status = ItemAccessStatus.open;
		if (!assessment.getIsValid())
		{
			status = ItemAccessStatus.invalid;
		}
		else if (!assessment.getPublished())
		{
			status = ItemAccessStatus.unpublished;
		}
		else if (assessment.getDates().getIsClosed())
		{
			status = ItemAccessStatus.closed;
		}
		else if (!assessment.getDates().getIsOpen(Boolean.FALSE))
		{
			Date now = new Date();
			if ((assessment.getDates().getOpenDate() != null) && (now.before(assessment.getDates().getOpenDate())))
			{
				if (assessment.getDates().getHideUntilOpen().booleanValue())
				{
					status = ItemAccessStatus.hiddenUntilOpen;
				}
				else
				{
					status = ItemAccessStatus.notOpen;
				}
			}

		}

		return new MnemeItemDetailImpl(assessment.getDates().getAcceptUntilDate(), context, assessment.getDates().getDueDate(), itemId,
				Integer.valueOf(numInProgress), Integer.valueOf(numNotSubmitted), Integer.valueOf(numSubmitted), assessment.getDates().getOpenDate(),
				Integer.valueOf(pctSubmitted), status, assessment.getTitle(), Integer.valueOf(activeParticipantIds.size()), assessment.getType());
	}

	/**
	 * Get overview information for all Mneme items in this site, in Mneme order.
	 * 
	 * @param context
	 *        The site id.
	 * @param activeParticipantIds
	 *        The user ids for active users.
	 * @return a List of MnemeOverviewItem.
	 */
	public List<MnemeItemOverview> getMnemeItemOverviews(String context, Set<String> activeParticipantIds)
	{
		// get the list of assessments, like Mneme's assessments view
		List<Assessment> assessments = this.assessmentService.getContextAssessments(context, AssessmentsSort.ddate_a, Boolean.FALSE);

		// get all the submissions - for ALL possible users (no filtering by existence or permissions)
		List<? extends Submission> submissions = this.submissionService.getContextSubmissions(context);

		List<MnemeItemOverview> rv = new ArrayList<MnemeItemOverview>();
		for (Assessment a : assessments)
		{
			// count submissions and unique users making submissions
			int numSubmissions = 0;
			int numInProgress = 0;
			Set<String> submitters = new HashSet<String>();
			for (Submission s : submissions)
			{
				// for this assessment
				if (!s.getAssessment().getId().equals(a.getId())) continue;

				// skip any not from our active user list
				if (!activeParticipantIds.contains(s.getUserId())) continue;

				// count the submitter
				submitters.add(s.getUserId());

				// for completed ones
				if (s.getIsComplete())
				{
					numSubmissions++;
				}

				// for in progress
				else if (s.getIsStarted())
				{
					numInProgress++;
				}
			}
			// ItemStatus status = a.getIsValid() ? (a.getPublished() ? ItemStatus.published : ItemStatus.na) : ItemStatus.invalid;
			ItemStatus status = ItemStatus.na;
			ItemAccessStatus accessStatus = ItemAccessStatus.open;

			if (!a.getIsValid())
			{
				status = ItemStatus.invalid;
				accessStatus = ItemAccessStatus.invalid;
			}
			else if (!a.getPublished())
			{
				status = ItemStatus.na;
				accessStatus = ItemAccessStatus.unpublished;
			}
			// valid and published, lets see if we are not yet open or if we are closed
			else
			{
				status = ItemStatus.published;
				if (!a.getDates().getIsOpen(Boolean.FALSE))
				{
					if (a.getDates().getIsClosed())
					{
						accessStatus = ItemAccessStatus.closed;
					}
					else
					{
						Date now = new Date();
						if ((a.getDates().getOpenDate() != null) && (now.before(a.getDates().getOpenDate())))
						{
							if (a.getDates().getHideUntilOpen().booleanValue())
							{
								accessStatus = ItemAccessStatus.hiddenUntilOpen;
							}
							else
							{
								accessStatus = ItemAccessStatus.notOpen;
							}
						}
					}
				}
				else
				{
					accessStatus = ItemAccessStatus.open;
				}
			}
			rv.add(new MnemeItemOverviewImpl(a.getId(), Integer.valueOf(numInProgress), Integer.valueOf(numSubmissions), Integer.valueOf(submitters
					.size()), status, (a.getFormalCourseEval() ? "fce" : a.getType().name()), a.getTitle(), accessStatus));
		}

		return rv;
	}

	/**
	 * Get overview information for Mneme for active users in this site.
	 * 
	 * @param context
	 *        The site id.
	 * @param activeParticipantIds
	 *        The user ids for active users.
	 * @return A MnemeOverview.
	 */
	public MnemeOverview getMnemeOverview(String context, Set<String> activeParticipantIds)
	{
		int numAssignmentSubmissions = 0;
		int numAssignmentSubmissionsInProgress = 0;
		int numTestSubmissions = 0;
		int numTestSubmissionsInProgress = 0;
		int numSurveySubmissions = 0;
		int numSurveySubmissionsInProgress = 0;
		int numOfflineSubmissions = 0;

		// get all the submissions - for ALL possible users (no filtering by existence of permissions)
		List<? extends Submission> submissions = this.submissionService.getContextSubmissions(context);

		// count by type / in progress
		for (Submission s : submissions)
		{
			// skip any not from our active user list
			if (!activeParticipantIds.contains(s.getUserId())) continue;

			AssessmentType type = s.getAssessment().getType();
			if (s.getIsComplete())
			{
				switch (type)
				{
					case assignment:
					{
						numAssignmentSubmissions++;
						break;
					}
					case test:
					{
						numTestSubmissions++;
						break;
					}
					case survey:
					{
						numSurveySubmissions++;
						break;
					}
					case offline:
					{
						numOfflineSubmissions++;
					}
				}
			}
			else
			{
				switch (type)
				{
					case assignment:
					{
						numAssignmentSubmissionsInProgress++;
						break;
					}
					case test:
					{
						numTestSubmissionsInProgress++;
						break;
					}
					case survey:
					{
						numSurveySubmissionsInProgress++;
						break;
					}
					default:
						break;
				}
			}
		}

		return new MnemeOverviewImpl(Integer.valueOf(numAssignmentSubmissions), Integer.valueOf(numAssignmentSubmissionsInProgress),
				Integer.valueOf(numSurveySubmissions), Integer.valueOf(numSurveySubmissionsInProgress), Integer.valueOf(numTestSubmissions),
				Integer.valueOf(numTestSubmissionsInProgress), Integer.valueOf(numOfflineSubmissions));
	}

	/**
	 * Check with Mneme to find the number of completed assessments for this user, total in the site.
	 * 
	 * @param context
	 *        The site id.
	 * @param userId
	 *        The user id.
	 * @return The number of completed assessments by this user in the site.
	 */
	public Integer getNumMnemeAssessmentsCompleted(String context, String userId)
	{
		// get all the submissions
		List<? extends Submission> submissions = this.submissionService.getContextSubmissions(context);

		// count assessments that a user has a complete submission to
		Set<String> userAssessments = new HashSet<String>();
		for (Submission s : submissions)
		{
			// only completed ones for this user
			if (!s.getIsComplete()) continue;
			if (!s.getUserId().equals(userId)) continue;

			// skip surveys
			if (s.getAssessment().getType() == AssessmentType.survey) continue;

			userAssessments.add(s.getAssessment().getId());
		}

		return Integer.valueOf(userAssessments.size());
	}

	/**
	 * Check with Mneme to find the number of submissions per user, total in the site. Do not count surveys.
	 * 
	 * @param context
	 *        The site id.
	 * @return The map, keyed by user id, data is the count.
	 */
	public Map<String, Integer> getNumMnemeSubmissions(String context, List<Participant> participants)
	{
		Map<String, Integer> rv = new HashMap<String, Integer>();

		// get all the submissions
		List<? extends Submission> submissions = this.submissionService.getContextSubmissions(context);

		// count submission by user
		for (Submission s : submissions)
		{
			// only completed ones
			if (!s.getIsComplete()) continue;

			// not surveys (survey progress is anonymous)
			if (s.getAssessment().getType() == AssessmentType.survey) continue;

			String userId = s.getUserId();
			Integer count = rv.get(userId);
			if (count == null)
			{
				rv.put(userId, Integer.valueOf(1));
			}
			else
			{
				rv.put(userId, Integer.valueOf(count.intValue() + 1));
			}
		}

		return rv;
	}

	/**
	 * Get detail information for a Mneme item (assessment) for all who submitted.
	 * 
	 * @param context
	 *        The site id.
	 * @param itemId
	 *        The assessment id.
	 * @param participants
	 * 
	 * @return a List of ParticipantMnemeItemOverview.
	 */
	public List<ParticipantMnemeItemDetail> getParticipantMnemeItemDetails(String context, String itemId, List<Participant> participants)
	{
		List<ParticipantMnemeItemDetail> rv = new ArrayList<ParticipantMnemeItemDetail>();

		// get Assessment
		Assessment assessment = this.assessmentService.getAssessment(itemId);
		if (assessment == null)
		{
			throw new IllegalArgumentException();
		}

		// get all submissions
		List<Submission> submissions = this.submissionService.findAssessmentSubmissions(assessment, FindAssessmentSubmissionsSort.userName_a,
				Boolean.FALSE, null, null, null, Boolean.FALSE);

		// create the ParticipantMnemeItemDetail with all needed information
		for (Participant p : participants)
		{
			// find this user's submissions
			boolean found = false;
			for (Submission s : submissions)
			{
				if (s.getIsPhantom()) continue;

				if (s.getUserId().equals(p.userId))
				{
					Date started = null;
					Date finished = null;
					Date reviewed = null;
					Date evaluated = null;
					Boolean inProgress = Boolean.FALSE;
					started = s.getStartDate();
					if (s.getIsComplete())
					{
						finished = s.getSubmittedDate();
						reviewed = s.getReviewedDate();
						evaluated = s.getEvaluatedDate();
					}
					else
					{
						inProgress = Boolean.TRUE;
					}

					ParticipantMnemeItemDetail pmid = new ParticipantMnemeItemDetailImpl(p.userId, p.sortName, p.status, evaluated, finished,
							reviewed, started, inProgress, (s.getCompletionStatus() == SubmissionCompletionStatus.evaluationNonSubmit),
							s.getEvaluationNotReviewed());
					rv.add(pmid);
					found = true;
				}
			}

			// if none found, make one
			if (!found)
			{
				ParticipantMnemeItemDetail pmid = new ParticipantMnemeItemDetailImpl(p.userId, p.sortName, p.status, null, null, null, null,
						Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
				rv.add(pmid);
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
	 * Set the AssessmentService.
	 * 
	 * @param service
	 *        The AssessmentService.
	 */
	public void setAssessmentService(AssessmentService service)
	{
		this.assessmentService = service;
	}

	/**
	 * Set the SubmissionService.
	 * 
	 * @param service
	 *        The SubmissionService.
	 */
	public void setSubmissionService(SubmissionService service)
	{
		this.submissionService = service;
	}

	/**
	 * @return The SiteService, via the component manager.
	 */
	private SiteService siteService()
	{
		return (SiteService) ComponentManager.get(SiteService.class);
	}
}
