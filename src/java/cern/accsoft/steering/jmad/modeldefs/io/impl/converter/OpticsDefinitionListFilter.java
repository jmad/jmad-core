package cern.accsoft.steering.jmad.modeldefs.io.impl.converter;

import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.mapper.Mapper;

import cern.accsoft.steering.jmad.modeldefs.io.JMadModelDefinitionExportRequest;
import cern.accsoft.steering.jmad.modeldefs.io.impl.ModelDefinitionMetaData;

public class OpticsDefinitionListFilter extends GenericCollectionFilter {
	public OpticsDefinitionListFilter(Mapper mapper) {
		super(mapper);
	}

	@Override
	protected Optional<Set<?>> filterSet(MarshallingContext context) {
		return Optional.ofNullable(context.get(ModelDefinitionMetaData.EXPORT_REQUEST)) //
				.map(JMadModelDefinitionExportRequest.class::cast) //
				.map(JMadModelDefinitionExportRequest::getOpticsToExport) //
				.map(ImmutableSet::copyOf);
	}

}
