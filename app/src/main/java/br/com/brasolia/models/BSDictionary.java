package br.com.brasolia.models;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by cayke on 07/04/17.
 */

public class BSDictionary {
    public static Object getValueWithKeyAndType(Map<String, Object> dictionary, String key, Class type)
    {
        if (type ==  String.class)
            return getStringValueWithKey(dictionary,key);
        else if (type == Boolean.class)
            return getBooleanValueWithKey(dictionary,key);
        else if (type == Double.class)
            return getDoubleValueWithKey(dictionary,key);
        else if (type == Number.class)
            return getNumberValueWithKey(dictionary,key);
        else if (type == Map.class)
            return getMapValueWithKey(dictionary,key);
        else if (type == List.class)
            return getListValueWithKey(dictionary,key);
        else
            return null;
        //throw new Exception("IBDicionary - Classe utilizada como parametro nao e reconhecida");
    }

    private static String getStringValueWithKey(Map<String, Object> dictionary,String key)
    {
        if (dictionary.containsKey(key))
        {
            Object object = dictionary.get(key);
            if (object != null && (object instanceof String))
                return (String) object;
        }
        return "";
    }

    private static Boolean getBooleanValueWithKey(Map<String, Object> dictionary,String key)
    {
        if (dictionary.containsKey(key))
        {
            Object object = dictionary.get(key);
            if (object != null && (object instanceof Boolean))
                return (Boolean) object;
        }
        return false;
    }

    private static Double getDoubleValueWithKey(Map<String, Object> dictionary,String key)
    {
        if (dictionary.containsKey(key))
        {
            Object object = dictionary.get(key);
            if (object != null && object instanceof Double)
                return (Double) object;
            else if (object != null && object instanceof String && NumberUtils.isDigits((String)object))
                return Double.parseDouble((String) object);
        }
        return 0d;
    }

    private static Number getNumberValueWithKey(Map<String, Object> dictionary,String key)
    {
        if (dictionary.containsKey(key))
        {
            Object object = dictionary.get(key);
            if (object != null && (object instanceof Number))
                return (Number) object;
        }
        return 0;
    }

    private static Map getMapValueWithKey(Map<String, Object> dictionary,String key)
    {
        if (dictionary.containsKey(key))
        {
            Object object = dictionary.get(key);
            if (object != null && (object instanceof Map))
                return (Map) object;
        }
        return null;
    }

    private static List getListValueWithKey(Map<String, Object> dictionary,String key)
    {
        if (dictionary.containsKey(key))
        {
            Object object = dictionary.get(key);
            if (object != null && (object instanceof List))
                return (List) object;
        }
        return null;
    }
}
