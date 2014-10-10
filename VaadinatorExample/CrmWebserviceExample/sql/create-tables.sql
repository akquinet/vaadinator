--
-- Copyright 2014 akquinet engineering GmbH
--  
-- Licensed under the Apache License, Version 2.0 (the "License"); you may not
-- use this file except in compliance with the License. You may obtain a copy of
-- the License at
-- 
-- http://www.apache.org/licenses/LICENSE-2.0
-- 
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
-- WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
-- License for the specific language governing permissions and limitations under
-- the License.
--
-- Table: crmcontact

-- DROP TABLE crmcontact;

CREATE TABLE crmcontact
(
  id serial NOT NULL,
  name character varying(255),
  rev bigint,
  score integer,
  CONSTRAINT crmcontact_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE crmcontact
  OWNER TO postgres;

-- Table: crmhistory

-- DROP TABLE crmhistory;

CREATE TABLE crmhistory
(
  id serial NOT NULL,
  text character varying(255),
  "timestamp" timestamp without time zone,
  parentcontact_id bigint,
  CONSTRAINT crmhistory_pkey PRIMARY KEY (id),
  CONSTRAINT fk_crmhistory_parentcontact_id FOREIGN KEY (parentcontact_id)
      REFERENCES crmcontact (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE crmhistory
  OWNER TO postgres;

-- Table: crmcontact_crmhistory

-- DROP TABLE crmcontact_crmhistory;

CREATE TABLE crmcontact_crmhistory
(
  contact_id bigint NOT NULL,
  history_id bigint NOT NULL,
  CONSTRAINT crmcontact_crmhistory_pkey PRIMARY KEY (contact_id, history_id),
  CONSTRAINT fk_crmcontact_crmhistory_contact_id FOREIGN KEY (contact_id)
      REFERENCES crmcontact (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_crmcontact_crmhistory_history_id FOREIGN KEY (history_id)
      REFERENCES crmhistory (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE crmcontact_crmhistory
  OWNER TO postgres;
