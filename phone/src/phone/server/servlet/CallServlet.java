package phone.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import phone.server.ApplicationContext;
import phone.server.dto.AnswerCallRequest;
import phone.server.dto.EndCallRequest;
import phone.server.service.TelephonyService;
import phone.shared.exception.AtsCommunicationException;
import phone.shared.exception.InvalidDeviceStateException;
import phone.shared.exception.InvalidRequestException;
import phone.shared.exception.TelephonyException;

@WebServlet("/api/calls")
public class CallServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final TelephonyService service = ApplicationContext.getInstance().getTelephonyService();

	// Сигнал "кто-то взял трубку аппарата" - ответили на звонок.
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		String deviceNumber = req.getParameter("deviceNumber");
		String phoneNumber = req.getParameter("phoneNumber");
		try {
			AnswerCallRequest callRequest = new AnswerCallRequest(deviceNumber, phoneNumber);

			service.answerCall(callRequest);
			JsonResponse.successMessage(resp, HttpServletResponse.SC_OK, "Звонок принят на обработку");

		} catch (TelephonyException e) {
			JsonResponse.errorMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());

		} catch (InvalidDeviceStateException e) {
			JsonResponse.errorMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		} catch (InvalidRequestException e) {
			JsonResponse.errorMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		} catch (AtsCommunicationException e) {
			JsonResponse.errorMessage(resp, HttpServletResponse.SC_SERVICE_UNAVAILABLE, e.getMessage());
		} catch (Exception e) {
			JsonResponse.errorMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера");
		}
	}

	// Сигнал "Звонок окончен" - положили трубку. (Удалить из активных)
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		String deviceNumber = req.getParameter("deviceNumber");

		try {
			EndCallRequest callRequest = new EndCallRequest(deviceNumber);

			service.endCall(callRequest);
			JsonResponse.successMessage(resp, HttpServletResponse.SC_OK, "Звонок окончен");// "Звонок окончен"

		} catch (TelephonyException e) {
			JsonResponse.errorMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		} catch (InvalidDeviceStateException e) {
			JsonResponse.errorMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		} catch (InvalidRequestException e) {
			JsonResponse.errorMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		} catch (AtsCommunicationException e) {
			JsonResponse.errorMessage(resp, HttpServletResponse.SC_SERVICE_UNAVAILABLE, e.getMessage());
		} catch (Exception e) {
			JsonResponse.errorMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера");
		}
	}

	// Посмотреть все активные звонки - кто с кем разговаривает.
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		try {
			JsonResponse.successMessageFromList(resp, HttpServletResponse.SC_OK, service.getActiveCallsList());
		} catch (IOException e) {
			JsonResponse.errorMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера");
		}
	}
}
