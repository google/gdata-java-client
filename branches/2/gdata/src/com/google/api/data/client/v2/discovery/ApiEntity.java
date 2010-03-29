package com.google.api.data.client.v2.discovery;

import com.google.api.data.client.entity.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Entity is the base class for untyped object graphs.
 *
 * This will likely be replaced with
 * com.google.api.server.core.data.Entity
 *
 * Untyped objects will root with Entity, and can include:
 * Entity
 * ArrayList<UntypedObject>
 * Primitives: String, Boolean, Integer, Long
 *
 * @deprecated use {@link Entity}
 * @author uidude@google.com (Evan Gilbert)
 */
@Deprecated
public class ApiEntity extends HashMap<String, Object> {

  /**
   * Checks whether a class is an untyped primitive we support.
   * @param cls The class
   * @return True if untyped primitive
   */
  public static boolean isPrimitive(Class<?> cls) {
    return cls.equals(Boolean.class) || cls.equals(Integer.class)
        || cls.equals(String.class) || cls.equals(Long.class);
  }

  /**
   * Fancy to string function
   */
  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    output(this, 0, sb);
    return sb.toString();
  }

  public ApiEntity merge(ApiEntity other) {
    for (Map.Entry<String, Object> entry : other.entrySet()) {
      put(entry.getKey(), entry.getValue());
    }
    
    return this;
  }


  static String spaces(int count) {
    String s = "";
    for (int i = 0; i < count; i++) {
      s = s + " ";
    }
    return s;
  }

  static void output(Object o, int indent, StringBuffer sb) {
    if (o == null) {
      sb.append("null");
    } else if (o instanceof String) {
      sb.append("\"" + o + "\"");
    } else if (isPrimitive(o.getClass())) {
      sb.append(o);
    } else if (o instanceof List<?>) {
      sb.append("[\n");
      for (Object item : (List<?>)o) {
        sb.append(spaces(indent + 2));
        output(item, indent + 2, sb);
        sb.append(",\n");
      }
      sb.append(spaces(indent) + "]");
    } else if (o instanceof ApiEntity) {
      ApiEntity e = (ApiEntity)o;
      sb.append("{\n");
      for (String key : e.keySet()) {
        sb.append(spaces(indent + 2) + key + ": ");
        output(e.get(key), indent + 2, sb);
        sb.append("\n");
      }
      sb.append(spaces(indent) + "}");
    }
  }

}
