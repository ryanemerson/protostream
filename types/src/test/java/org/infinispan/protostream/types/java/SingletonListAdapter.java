package org.infinispan.protostream.types.java;

import java.util.Collections;
import java.util.List;

import org.infinispan.protostream.WrappedMessage;
import org.infinispan.protostream.annotations.ProtoAdapter;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

@ProtoAdapter(
      value = List.class,
      implementation = "java.util.Collections$SingletonList"
)
// TODO
// Verify implementation field set if value is interface
// Ensure subClassNames and implementation cannot be set at the same time?
// OR make it so that subClassNames must be a singleton if value is a list
// OR can we make it so that we register a marshaller for each subClassName?
public class SingletonListAdapter {

   @ProtoFactory
   List<?> create(WrappedMessage element) {
      return Collections.singletonList(element == null ? null : element.getValue());
   }

   @ProtoField(1)
   WrappedMessage getElement(List<?> list) {
      var val = list.get(0);
      return val == null ? null : new WrappedMessage(val);
   }
}
