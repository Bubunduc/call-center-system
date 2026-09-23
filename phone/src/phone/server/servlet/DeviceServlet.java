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
import phone.shared.exception.TelephonyException;

@WebServlet("/api/rooms/devices")
public class DeviceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final TelephonyService service = ApplicationContext.getInstance().getTelephonyService();

	// Посмотреть статус всех аппаратов в комнате
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		String roomIdParam = req.getParameter("roomId");

		if (roomIdParam == null || roomIdParam.isEmpty()) {
			JsonResponse.errorMessage(resp, HttpServletResponse.SC_BAD_REQUEST, "Не указан параметр roomId");
			return;
		}

		final Long roomId;
		
		try {
			roomId = Long.parseLong(roomIdParam);
		} catch (NumberFormatException e) {
			JsonResponse.errorMessage(resp, HttpServletResponse.SC_BAD_REQUEST, "Указан некорректный roomId");
			return;
		}
		
		try {
			JsonResponse.successMessageFromList(resp, HttpServletResponse.SC_OK,
					service.getDevicesStatusByRoom(roomId));
		} catch (TelephonyException e) {
			JsonResponse.errorMessage(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
		} catch (PersistenceException e) {
			JsonResponse.errorMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка работы базы данных");
		}
	}

}
