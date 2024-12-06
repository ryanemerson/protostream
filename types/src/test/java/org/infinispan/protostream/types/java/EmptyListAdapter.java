package org.infinispan.protostream.types.java;

import java.util.Collections;
import java.util.List;

import org.infinispan.protostream.annotations.ProtoAdapter;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoName;

@ProtoAdapter(
      value = List.class,
      implementation = "java.util.Collections$EmptyList"
)
@ProtoName("EmptyList")
public class EmptyListAdapter {
   @ProtoFactory
   List<?> create() {
      return Collections.emptyList();
   }
}
