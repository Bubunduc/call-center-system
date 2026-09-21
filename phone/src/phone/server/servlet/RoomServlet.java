package phone.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.exceptions.PersistenceException;

import phone.server.ApplicationContext;
import phone.server.service.TelephonyService;

@WebServlet("/api/rooms")
public class RoomServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final TelephonyService service = ApplicationContext.getTelephonyService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		try {
			JsonResponse.successMessageFromList(resp, HttpServletResponse.SC_OK, service.getAllRooms());
		} catch (IOException e) {
			JsonResponse.errorMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка формирования json");
		}
		catch (PersistenceException e) {
			JsonResponse.errorMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка работы базы данных");
		}
	
	}
}
