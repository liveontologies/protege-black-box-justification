package org.liveontologies.protege.justification.blackbox;

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

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.*;

/**
 * Author: Matthew Horridge Stanford University Bio-Medical Informatics Research
 * Group Date: 20/03/2012
 */
public class JustificationCache {

	private Map<OWLAxiom, Set<Explanation<OWLAxiom>>> justifications_ = new HashMap<>();

	public boolean contains(OWLAxiom entailment) {
		return justifications_.containsKey(entailment);
	}

	public Set<Explanation<OWLAxiom>> get(OWLAxiom entailment) {
		return justifications_.get(entailment);
	}

	public void initialise(OWLAxiom entailment) {
		if (!contains(entailment)) {
			justifications_.put(entailment,
					new HashSet<Explanation<OWLAxiom>>());
		}
	}

	public void put(Explanation<OWLAxiom> justification) {
		Set<Explanation<OWLAxiom>> justifications = justifications_
				.get(justification.getEntailment());
		if (justifications == null) {
			justifications = new HashSet<>();
			justifications_.put(justification.getEntailment(), justifications);
		}
		justifications.add(justification);
	}

	public void putAll(Set<Explanation<OWLAxiom>> justifications) {
		for (Explanation<OWLAxiom> justification : justifications) {
			put(justification);
		}
	}

	public void clear() {
		justifications_.clear();
	}

	public void clear(OWLAxiom entailment) {
		justifications_.remove(entailment);
	}
}