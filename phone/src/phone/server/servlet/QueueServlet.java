package phone.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import phone.server.ApplicationContext;
import phone.server.dto.CallRequest;
import phone.server.service.TelephonyService;
import phone.shared.exception.AtsCommunicationException;
import phone.shared.exception.InvalidPhoneFormatException;
import phone.shared.exception.TelephonyException;

@WebServlet("/api/queue")
public class QueueServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final TelephonyService service = ApplicationContext.getTelephonyService();

	// Добавить входящий звонок в очередь (вызов начался, но никто не взял трубку)
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		String phoneNumber = req.getParameter("phoneNumber");

		try {
			CallRequest callRequest = new CallRequest(phoneNumber);
			service.addToQueue(callRequest);
			JsonResponse.successMessage(resp, HttpServletResponse.SC_OK, "Звонок добавлен в очередь");

		} catch (TelephonyException e) {
			JsonResponse.errorMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());

		} catch (InvalidPhoneFormatException e) {
			JsonResponse.errorMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		} catch (AtsCommunicationException e) {
			JsonResponse.errorMessage(resp, HttpServletResponse.SC_SERVICE_UNAVAILABLE, e.getMessage());
		} catch (Exception e) {// 500
			JsonResponse.errorMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера");
		}
	}

	// Удалить звонок из очереди (отменить входящий звонок)
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		String phoneNumber = req.getParameter("phoneNumber");

		try {
			CallRequest callRequest = new CallRequest(phoneNumber);

			service.removeFromQueue(callRequest);
			JsonResponse.successMessage(resp, HttpServletResponse.SC_OK, "Звонок удален из очереди");

		} catch (TelephonyException e) {
			JsonResponse.errorMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());

		} catch (AtsCommunicationException e) {
			JsonResponse.errorMessage(resp, HttpServletResponse.SC_SERVICE_UNAVAILABLE, e.getMessage());
		} catch (Exception e) {// 500
			JsonResponse.errorMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера");
		}
	}

	// Посмотреть всю очередь (только входящие, для которых никто не взял трубку)
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		try {
			JsonResponse.successMessageFromList(resp, HttpServletResponse.SC_OK, service.getNumsList());
		} catch (IOException e) {
			JsonResponse.errorMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка формирования json");
		}
	}
}
