
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class SanitizeHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private static final Map<String, String> sanitizeMap = new LinkedHashMap<String, String>() {
        {
            put("&", "&amp;");
            put("<", "&lt;");
            put(">", "&gt;");
            put("\"", "&quot;");
            put("'", "&#39;");
        }
    };

    public SanitizeHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public String getParameter(String name) {
        return sanitize(getRequest().getParameter(name));
    }

    public Map getParameterMap() {
        return getSanitizeMap(getRequest().getParameterMap());
    }

    public String[] getParameterValues(String name) {
        String[] values = getRequest().getParameterValues(name);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                values[i] = sanitize(values[i]);
            }
        }
        return values;
    }

    static Map getSanitizeMap(Map originalMap) {
        if (originalMap == null) {
            return (new LinkedHashMap());
        }
        Map dest = new LinkedHashMap();
        Iterator keys = originalMap.keySet().iterator();
        while (keys.hasNext()) {
            String key = sanitize((String) keys.next());
            dest.put(key, originalMap.get(key));
        }
        return dest;
    }

    static String sanitize(String string) {
        String result = string;
        if (result != null && result.length() > 0) {
            for (Map.Entry<String, String> e: sanitizeMap.entrySet()) {
                result = result.replaceAll(e.getKey(), e.getValue());
            }
        }
        return result;
    }
}
