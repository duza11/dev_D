
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class SanitizeFilter implements Filter {

    private static final boolean debug = true;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;

    public SanitizeFilter() {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        try {
            ServletRequest sanitizedRequest = request;
            if (request instanceof HttpServletRequest
                    && request.getParameterMap().size() > 0) {
                sanitizedRequest = new SanitizeHttpServletRequestWrapper((HttpServletRequest) request);
            }
            chain.doFilter(sanitizedRequest, response);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    public void destroy() {
        filterConfig = null;
    }

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }
}
