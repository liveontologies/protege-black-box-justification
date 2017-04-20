package org.liveontologies.protege.explanation.justification.blackbox;

/*-
 * #%L
 * Protege Blackbox Justification Explanation
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2016 - 2017 Live Ontologies Project
 * %%
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
 * #L%
 */


/**
 * Author: Matthew Horridge
 * Stanford University
 * Bio-Medical Informatics Research Group
 * Date: 20/03/2012
 */
public class WorkbenchSettings {

	private JustificationType justificationType = JustificationType.REGULAR;

	private int limit = 2;

	private boolean findAll = true;

	public JustificationType getJustificationType() {
		return justificationType;
	}

	public void setJustificationType(JustificationType justificationType) {
		this.justificationType = justificationType;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public boolean isFindAllExplanations() {
		return findAll;
	}

	public void setFindAllExplanations(boolean findAllExplanations) {
		findAll = findAllExplanations;
	}
}