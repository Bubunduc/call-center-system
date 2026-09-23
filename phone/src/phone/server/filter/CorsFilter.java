package phone.server.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CorsFilter implements Filter {

	private final Set<String> allowedOrigins = new HashSet<String>();

	@Override
	public void init(FilterConfig filterConfig) {

		addAllowedOrigin(System.getenv("SWAGGER_ALLOWED_ORIGIN"));

		addAllowedOrigin(System.getenv("ATS_ALLOWED_ORIGIN"));
	}

	private void addAllowedOrigin(String origin) {
		if (origin != null && !origin.trim().isEmpty()) {
			allowedOrigins.add(origin);
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;

		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String origin = httpRequest.getHeader("Origin");

		if (origin != null && allowedOrigins.contains(origin)) {

			httpResponse.setHeader("Access-Control-Allow-Origin", origin);

			httpResponse.setHeader("Vary", "Origin");
		}

		httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");

		httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept");

		httpResponse.setHeader("Access-Control-Max-Age", "3600");

		if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {

			httpResponse.setStatus(HttpServletResponse.SC_OK);

			return;
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}
}