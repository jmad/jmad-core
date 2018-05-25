package cern.accsoft.steering.jmad.modeldefs.io.impl.converter;

import static java.util.stream.Collectors.toCollection;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public abstract class GenericCollectionFilter extends CollectionConverter {

	public GenericCollectionFilter(Mapper mapper) {
		super(mapper);
	}

	protected abstract Optional<Set<?>> filterSet(MarshallingContext context);

	@Override
	public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext ctx) {
		Optional<Set<?>> filterSet = this.filterSet(ctx);
		if (filterSet.isPresent()) {
			Collection<?> collection = (Collection<?>) object;
			Collection<?> filtered = collection.stream().filter(filterSet.get()::contains)
					.collect(toCollection(createInstance(collection.getClass())));
			super.marshal(filtered, writer, ctx);
		} else {
			super.marshal(object, writer, ctx);
		}
	}

	private static <T> Supplier<T> createInstance(Class<T> clazz) {
		return (() -> {
			try {
				return clazz.newInstance();
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
		});
	}
}
