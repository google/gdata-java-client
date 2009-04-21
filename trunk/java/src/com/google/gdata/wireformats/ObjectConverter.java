/* Copyright (c) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.google.gdata.wireformats;

import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.data.DateTime;
import com.google.gdata.util.ParseException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Converts values from strings to typed values (objects).
 * 
 * @param <T> data type handled by this converter
 * 
 */
public abstract class ObjectConverter<T> {

  /**
   * Map from data type to value convert. This is used for
   * types that don't have a string-arg constructor.
   */
  protected static final Map<Class<?>, ObjectConverter<?>> CONVERTERS =
      new HashMap<Class<?>, ObjectConverter<?>>();

  static {
    CONVERTERS.put(DateTime.class, new DateTimeConverter());
    CONVERTERS.put(Enum.class, new EnumConverter());
  }

  /**
   * Add converter for a data type.
   * 
   * @param type data type
   * @param converter converter
   */
  public static <V> void addConverter(
      Class<V> type, ObjectConverter<V> converter) {
    CONVERTERS.put(type, converter);
  }

  /**
   * Converts an object to the given datatype by casting if possible, and if not
   * by narrowing from {@link String} using the registered object converters.
   * 
   * @param <V> the type of value to return.
   * @param value the value to convert.
   * @param datatype the datatype to convert to.
   * @return the original value if it could be cast to the required type, or
   *     a narrowed version of the value if narrowing was possible.
   * @throws ParseException if narrowing was not possible.
   */
  public static <V> V getValue(Object value, Class<V> datatype)
      throws ParseException {
    if (value instanceof String) {
      return getValue((String) value, datatype);
    }
    if (value == null || datatype.isInstance(value)) {
      return datatype.cast(value);
    }
    throw new ParseException("Cannot convert value " + value
        + " of type " + value.getClass() + " to " + datatype);
  }
  
  /**
   * Translate an untyped (string) value to a typed value.
   * 
   * @param <V> data type
   * @param value value
   * @param datatype class of value type
   * @return typed value
   * @throws ParseException if value cannot be parsed according to type
   */
  public static <V> V getValue(String value, Class<V> datatype)
      throws ParseException {
    if (value == null || datatype.isInstance(value)) {
      return datatype.cast(value);
    }
    
    try {
      ObjectConverter<V> handler = getHandler(datatype);
      if (handler != null) {
        return handler.convertValue(value, datatype);
      }
      
      Constructor<V> cons = datatype.getConstructor(String.class);
      return cons.newInstance(value);
    } catch (NoSuchMethodException e) {
      ParseException pe = new ParseException(
          CoreErrorDomain.ERR.missingConverter);
      pe.setInternalReason("No converter for type " + datatype);
      throw pe;
    } catch (IllegalArgumentException e) {
      throw new ParseException(e);
    } catch (InstantiationException e) {
      throw new ParseException(e);
    } catch (IllegalAccessException e) {
      throw new ParseException(e);
    } catch (InvocationTargetException e) {
      throw new ParseException(e.getTargetException());
    }
  }

  /**
   * Translate an untyped (string) value to a typed value.
   * 
   * @param value value to convert.
   * @return value converted to type {@code T}.
   * @throws ParseException if value cannot be parsed according to type
   */
  public abstract T convertValue(String value, Class<? extends T> datatype)
      throws ParseException;

  /**
   * Get handler associated with a data type.  Unchecked because we know that
   * the class type and the handler type match, but we can't tell the compiler.
   * 
   * @param type data type to retrieve a handler for.
   * @return an object converter that can convert to type {@code V}.
   */
  @SuppressWarnings("unchecked")
  private static <V> ObjectConverter<V> getHandler(Class<? extends V> type) {
    ObjectConverter<V> handler = (ObjectConverter<V>) CONVERTERS.get(type);
    if (handler == null && type.isEnum()) {
      handler = (ObjectConverter<V>) CONVERTERS.get(Enum.class);
    }
    return handler;
  }

  /**
   * Object converter for {@link DateTime}.
   */
  private static class DateTimeConverter extends ObjectConverter<DateTime> {
    @Override
    public DateTime convertValue(String value,
        Class<? extends DateTime> datatype) throws ParseException {
      try {
        return DateTime.parseDateTimeChoice(value);
      } catch (NumberFormatException e) {
        throw new ParseException(
            CoreErrorDomain.ERR.invalidDatetime, e);
      }
    }
  }
  
  /**
   * Object converter for {@link Enum} instances.  Unchecked because
   * we don't actually care about the return type from
   * {@link Enum#valueOf(Class, String)}, but it requires stronger typing than
   * we can give it.
   */
  @SuppressWarnings("unchecked")
  private static class EnumConverter extends ObjectConverter<Enum> {
    @Override
    public Enum<?> convertValue(String value, Class<? extends Enum> datatype)
        throws ParseException {
      if (value == null) {
        return null;
      }
      Enum<?> result = Enum.valueOf(datatype, value.toUpperCase());
      if (result == null) {
        ParseException pe = new ParseException(
            CoreErrorDomain.ERR.invalidEnumValue);
        pe.setInternalReason("No such enum of type " + datatype
            + " named " + value.toUpperCase());
        throw pe;
      }
      return result;
    }
  }
}
