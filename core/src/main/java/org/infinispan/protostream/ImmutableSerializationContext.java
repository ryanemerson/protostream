package org.infinispan.protostream;

import java.util.Map;

import org.infinispan.protostream.descriptors.Descriptor;
import org.infinispan.protostream.descriptors.EnumDescriptor;
import org.infinispan.protostream.descriptors.FileDescriptor;
import org.infinispan.protostream.descriptors.GenericDescriptor;

/**
 * A repository of Protobuf type definitions and their marshallers. All marshalling operations happen in the context of
 * a {@link SerializationContext}. The {@link ImmutableSerializationContext} is a super-interface that contains strictly
 * read-only methods while its descendant {@link SerializationContext} provides methods to mutate the context.
 *
 * @author anistor@redhat.com
 * @since 4.0
 */
public interface ImmutableSerializationContext {

   /**
    * Obtain the currently registered file descriptors.
    *
    * @return an immutable copy of the internal map of descriptors
    */
   Map<String, FileDescriptor> getFileDescriptors();

   /**
    * @throws IllegalArgumentException if the descriptor is not found or is not a message
    */
   Descriptor getMessageDescriptor(String fullTypeName);

   /**
    * @throws IllegalArgumentException if the descriptor is not found or is not an enum
    */
   EnumDescriptor getEnumDescriptor(String fullTypeName);

   /**
    * Checks if the given type (message or enum) can be marshalled. This checks that a marshaller was registered for
    * it.
    *
    * @param javaClass the object or enum class to check
    * @return {@code true} if a marshaller exists, {@code false} otherwise
    */
   boolean canMarshall(Class<?> javaClass);

   /**
    * Checks if the given type (message or enum) can be marshalled. This checks that the Protobuf type was defined and a
    * marshaller was registered for it.
    *
    * @param fullTypeName the fully qualified name of the protobuf definition to check
    * @return {@code true} if a marshaller exists, {@code false} otherwise
    */
   boolean canMarshall(String fullTypeName);

   /**
    * Obtains the marshaller associated with a Protobuf type name.
    *
    * @param fullTypeName the type name
    * @return the marshaller
    * @throws IllegalArgumentException if the given type name is unknown
    */
   <T> BaseMarshaller<T> getMarshaller(String fullTypeName);

   /**
    * Obtains the marshaller associated with a Java type.
    *
    * @param clazz the class
    * @return the marshaller
    * @throws IllegalArgumentException if the given Java type is unknown
    */
   <T> BaseMarshaller<T> getMarshaller(Class<T> clazz);

   /**
    * Obtains the Protobuf type name associated with a numeric type id.
    *
    * @param typeId the numeric type id
    * @return the fully qualified type name
    * @throws IllegalArgumentException if the given type id is unknown
    * @deprecated replaced by {@code getDescriptorByTypeId(typeId).getFullName()}
    */
   @Deprecated
   String getTypeNameById(Integer typeId);

   /**
    * Obtains the associated numeric type id for a Protobuf type name, if a numeric id was defined.
    *
    * @param fullTypeName the fully qualified type name
    * @return the type id or {@code null} if no type id is associated with the type
    * @throws IllegalArgumentException if the given type name is unknown
    * @deprecated replaced by {@code getDescriptorByName(fullTypeName).getTypeId()}
    */
   @Deprecated
   Integer getTypeIdByName(String fullTypeName);

   /**
    * Obtains the descriptor associated with a numeric type id.
    *
    * @param typeId the numeric type id
    * @return the descriptor
    * @throws IllegalArgumentException if the given type id is unknown
    */
   GenericDescriptor getDescriptorByTypeId(Integer typeId);

   /**
    * Obtains the descriptor associated with a type name.
    *
    * @param fullTypeName the fully qualified type name
    * @return the descriptor
    * @throws IllegalArgumentException if the given type name is unknown
    */
   GenericDescriptor getDescriptorByName(String fullTypeName);
}
