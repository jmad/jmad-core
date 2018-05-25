package cern.accsoft.steering.jmad.modeldefs.io;

import static java.util.stream.Collectors.toSet;

import java.util.Collection;

import com.google.common.collect.ImmutableSet;

import cern.accsoft.steering.jmad.domain.machine.RangeDefinition;
import cern.accsoft.steering.jmad.domain.machine.SequenceDefinition;
import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinition;
import cern.accsoft.steering.jmad.modeldefs.domain.OpticsDefinition;

public class JMadModelDefinitionExportRequest {
	private final JMadModelDefinition modelDefinition;
	private final Collection<OpticsDefinition> opticsToExport;
	private final Collection<RangeDefinition> rangesToExport;

	private JMadModelDefinitionExportRequest(JMadModelDefinition modelDefinition,
			Collection<OpticsDefinition> opticsToExport, Collection<RangeDefinition> rangesToExport) {
		this.modelDefinition = modelDefinition;
		this.opticsToExport = opticsToExport;
		this.rangesToExport = rangesToExport;
	}

	public static JMadModelDefinitionExportRequest allFrom(JMadModelDefinition modelDefinition) {
		return from(modelDefinition).exportAllOptics().exportAllRanges().build();
	}

	public static JMadModelDefinitionExportRequest.Builder from(JMadModelDefinition modelDefinition) {
		return new Builder(modelDefinition);
	}

	public static class Builder {
		private final JMadModelDefinition modelDefinition;
		private final ImmutableSet.Builder<OpticsDefinition> opticsToExport = ImmutableSet.builder();
		private final ImmutableSet.Builder<RangeDefinition> rangesToExport = ImmutableSet.builder();

		Builder(JMadModelDefinition modelDefinition) {
			this.modelDefinition = modelDefinition;
		}

		public Builder exportAllOptics() {
			opticsToExport.addAll(modelDefinition.getOpticsDefinitions());
			return this;
		}

		public Builder exportAllRanges() {
			rangesToExport.addAll(modelDefinition.getRangeDefinitions());
			return this;
		}

		public Builder export(OpticsDefinition optics) {
			opticsToExport.add(optics);
			return this;
		}

		public Builder exportAllRangesFrom(SequenceDefinition sequence) {
			sequence.getRangeDefinitions().forEach(this::export);
			return this;
		}

		public Builder export(RangeDefinition range) {
			rangesToExport.add(range);
			return this;
		}

		public JMadModelDefinitionExportRequest build() {
			return new JMadModelDefinitionExportRequest(//
					modelDefinition, //
					opticsToExport.build(), //
					rangesToExport.build());
		}
	}

	public Collection<OpticsDefinition> getOpticsToExport() {
		return opticsToExport;
	}

	public Collection<SequenceDefinition> getSequencesToExport() {
		return rangesToExport.stream().map(RangeDefinition::getSequenceDefinition).collect(toSet());
	}

	public Collection<RangeDefinition> getRangesToExport() {
		return rangesToExport;
	}

	public JMadModelDefinition getModelDefinition() {
		return modelDefinition;
	}
}
