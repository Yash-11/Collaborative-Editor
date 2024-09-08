package com.example.editor.delta;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
// import org.apache.commons.lang3.ObjectUtils;
// import org.apache.commons.collections4.MapUtils;

public class AttributeMap {

    // Compose method: combines attributes 'a' and 'b'
    public static Map<String, Object> compose(
            Map<String, Object> a, Map<String, Object> b, boolean keepNull) {
        
        if (a == null) {
            a = new HashMap<>();
        }
        if (b == null) {
            b = new HashMap<>();
        }
        Map<String, Object> attributes = new HashMap<>(b);

        // If keepNull is false, remove null attributes from 'b'
        if (!keepNull) {
            attributes.entrySet().removeIf(entry -> entry.getValue() == null);
        }

        // Copy any keys from 'a' that are not present in 'b'
        for (Map.Entry<String, Object> entry : a.entrySet()) {
            attributes.putIfAbsent(entry.getKey(), entry.getValue());
        }

        return attributes.isEmpty() ? null : attributes;
    }

    // Diff method: finds the difference between attribute sets 'a' and 'b'
    // public static Map<String, Object> diff(Map<String, Object> a, Map<String, Object> b) {
    //     if (a == null) {
    //         a = new HashMap<>();
    //     }
    //     if (b == null) {
    //         b = new HashMap<>();
    //     }

    //     Map<String, Object> attributes = new HashMap<>();
    //     for (String key : ObjectUtils.union(a.keySet(), b.keySet())) {
    //         if (!Objects.equals(a.get(key), b.get(key))) {
    //             attributes.put(key, b.get(key) == null ? null : b.get(key));
    //         }
    //     }

    //     return attributes.isEmpty() ? null : attributes;
    // }

    // Invert method: inverts the attributes relative to the base
    // public static Map<String, Object> invert(Map<String, Object> attr, Map<String, Object> base) {
    //     if (attr == null) {
    //         attr = new HashMap<>();
    //     }
    //     if (base == null) {
    //         base = new HashMap<>();
    //     }

    //     Map<String, Object> inverted = new HashMap<>();
        
    //     // Base attributes that are different from the current attr
    //     for (Map.Entry<String, Object> entry : base.entrySet()) {
    //         if (!Objects.equals(entry.getValue(), attr.get(entry.getKey())) && attr.get(entry.getKey()) != null) {
    //             inverted.put(entry.getKey(), entry.getValue());
    //         }
    //     }

    //     // Current attributes that are missing in the base
    //     for (Map.Entry<String, Object> entry : attr.entrySet()) {
    //         if (!Objects.equals(entry.getValue(), base.get(entry.getKey())) && base.get(entry.getKey()) == null) {
    //             inverted.put(entry.getKey(), null);
    //         }
    //     }

    //     return inverted;
    // }

    // Transform method: applies transformation of 'a' attributes using 'b', considering priority
    // public static Map<String, Object> transform(
    //         Map<String, Object> a, Map<String, Object> b, boolean priority) {
        
    //     if (a == null) {
    //         return b;
    //     }
    //     if (b == null) {
    //         return null;
    //     }
        
    //     if (!priority) {
    //         return b; // If no priority, simply overwrite
    //     }

    //     Map<String, Object> attributes = new HashMap<>();
    //     for (Map.Entry<String, Object> entry : b.entrySet()) {
    //         if (!a.containsKey(entry.getKey())) {
    //             attributes.put(entry.getKey(), entry.getValue()); // null is valid
    //         }
    //     }

    //     return attributes.isEmpty() ? null : attributes;
    // }
}
