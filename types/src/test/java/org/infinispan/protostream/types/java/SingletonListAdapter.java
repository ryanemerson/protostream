package org.infinispan.protostream.types.java;

import java.util.Collections;
import java.util.List;

import org.infinispan.protostream.WrappedMessage;
import org.infinispan.protostream.annotations.ProtoAdapter;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;
import org.infinispan.protostream.annotations.ProtoName;

@ProtoAdapter(
      value = List.class,
      implementation = "java.util.Collections$SingletonList"
)
@ProtoName("SingletonList")
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
