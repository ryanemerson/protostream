package org.infinispan.protostream.impl;

import static java.lang.invoke.MethodType.methodType;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class StringUtil {

   private static final Log log = Log.LogFactory.getLog(ProtoStreamReaderImpl.class);
   private static final Byte LATIN1 = (byte) 0;

   static ToIntFunction<String> STRING_CODER;
   static Function<String, byte[]> STRING_VALUE;

   static {
      try {
         MethodHandles.Lookup lookup = JDKUtils.trustedLookup(String.class);
         MethodHandle coder = lookup.findSpecial(
               String.class,
               "coder",
               methodType(byte.class),
               String.class
         );
         CallSite applyAsInt = LambdaMetafactory.metafactory(
               lookup,
               "applyAsInt",
               methodType(ToIntFunction.class),
               methodType(int.class, Object.class),
               coder,
               MethodType.methodType(byte.class, String.class)
         );
         STRING_CODER = (ToIntFunction<String>) applyAsInt.getTarget().invokeExact();

         MethodHandle value = lookup.findSpecial(
               String.class,
               "value",
               methodType(byte[].class),
               String.class
         );
         CallSite apply = LambdaMetafactory.metafactory(
               lookup,
               "apply",
               methodType(Function.class),
               methodType(Object.class, Object.class),
               value,
               methodType(byte[].class, String.class)
         );
         STRING_VALUE = (Function<String, byte[]>) apply.getTarget().invokeExact();

      } catch (Throwable t) {
         log.warn("Could not retrieve MethodHandles for String.class, ignoring coder optimisations:", t);
         STRING_CODER = null;
         STRING_VALUE = null;
      }
   }

   private StringUtil() {
   }

   public static boolean isUTF8CoderOptimisationSupported() {
      return STRING_CODER != null;
   }

   private static boolean isLatin1(String s) {
      return STRING_CODER != null && STRING_CODER.applyAsInt(s) == LATIN1;
   }

   public static byte[] getBytes(String s) {
      if (!isLatin1(s))
         return s.getBytes(StandardCharsets.UTF_8);
      return STRING_VALUE.apply(s);
   }
}
