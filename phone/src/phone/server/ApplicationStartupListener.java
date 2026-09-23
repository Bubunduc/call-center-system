package phone.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ApplicationStartupListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ApplicationContext.getInstance();
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}
}