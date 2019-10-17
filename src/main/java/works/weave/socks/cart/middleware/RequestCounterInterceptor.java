package works.weave.socks.cart.middleware;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import io.prometheus.client.Counter;

@Component
public class RequestCounterInterceptor implements HandlerInterceptor {

	private static final Counter requestTotal = Counter.build()
			.name("http_requests_total")
			.labelNames("method", "handler", "status")
			.help("Http Request Total").register();

	@Override
	public boolean preHandle(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final Object o) throws Exception {
		return false;
	}

	@Override
	public void postHandle(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final Object o, final ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception {
		String handlerLabel = handler.toString();
    System.out.println("request interceptor");
		if (handler instanceof HandlerMethod) {
			Method method = ((HandlerMethod) handler).getMethod();
			handlerLabel = method.getDeclaringClass().getSimpleName() + "." + method.getName();
		}

		requestTotal.labels(request.getMethod(), handlerLabel, Integer.toString(response.getStatus())).inc();
	}
}
