--*********************************************************************************
-- $URL: https://source.etudes.org/svn/apps/activitymeter/trunk/activitymeter-impl/impl/src/sql/mysql/activitymeter.sql $
-- $Id: activitymeter.sql 1264 2011-03-10 20:13:48Z mallikamt $ 
--**********************************************************************************
--
-- Copyright (c) 2010 Etudes, Inc.
-- 
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
-- 
--      http://www.apache.org/licenses/LICENSE-2.0
-- 
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--
--*********************************************************************************/

CREATE TABLE AM_SITE_VISIT
(
CONTEXT VARCHAR(99) NOT NULL,
USER_ID VARCHAR(99) NOT NULL,
FIRST_VISIT BIGINT,
LAST_VISIT BIGINT,
VISITS INT UNSIGNED
);
CREATE UNIQUE INDEX AM_SITE_VISIT_IDX_CU ON AM_SITE_VISIT
(
	CONTEXT		ASC,
	USER_ID		ASC
);