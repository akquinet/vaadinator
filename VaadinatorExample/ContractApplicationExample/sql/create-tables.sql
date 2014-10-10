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
-- Table: contractapplication

-- DROP TABLE contractapplication;

CREATE TABLE contractapplication
(
  id serial NOT NULL,
  customercity character varying(255),
  customerfirstname character varying(255),
  customerlastname character varying(255),
  customerpostalcode character varying(255),
  customerstreet character varying(255),
  lazinessprotection boolean,
  monthlyfee integer,
  retirementprotection boolean,
  CONSTRAINT contractapplication_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE contractapplication
  OWNER TO postgres;
