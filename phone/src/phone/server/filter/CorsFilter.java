package phone.server.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CorsFilter implements Filter {

	private static final String LOCALHOST_ORIGIN = "http://localhost:8080";
	private static final String LOOPBACK_ORIGIN = "http://127.0.0.1:8080";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(
			ServletRequest request,
			ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest =
				(HttpServletRequest) request;

		HttpServletResponse httpResponse =
				(HttpServletResponse) response;

		String origin = httpRequest.getHeader("Origin");

		if (LOCALHOST_ORIGIN.equals(origin)
				|| LOOPBACK_ORIGIN.equals(origin)) {

			httpResponse.setHeader(
					"Access-Control-Allow-Origin",
					origin
			);
		}

		httpResponse.setHeader(
				"Access-Control-Allow-Methods",
				"GET, POST, PUT, DELETE, OPTIONS"
		);

		httpResponse.setHeader(
				"Access-Control-Allow-Headers",
				"Content-Type, Accept"
		);

		httpResponse.setHeader(
				"Access-Control-Max-Age",
				"3600"
		);

		/*
		 * Origin может отличаться,
		 * поэтому сообщаем кэшу, что ответ
		 * зависит от заголовка Origin.
		 */
		httpResponse.setHeader(
				"Vary",
				"Origin"
		);

		/*
		 * Для CORS preflight.
		 *
		 * Браузер может сначала отправить OPTIONS,
		 * например перед DELETE-запросом.
		 */
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