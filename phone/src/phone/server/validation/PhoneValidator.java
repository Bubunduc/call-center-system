package phone.server.validation;

import phone.server.dto.CallRequest;

public class PhoneValidator {

	private static final String PHONE_REGEX = "^8-\\d{3}-\\d{3}-\\d{2}-\\d{2}$";

	public static String verifyIncomingPhone(CallRequest callRequest) {

		if ((callRequest == null) || (callRequest.getPhoneNumber() == null)
				|| (callRequest.getPhoneNumber().isEmpty())) {
			return "Неверный формат данных или отсутствует номер телефона";

		}

		String phone = callRequest.getPhoneNumber();
		if (!phone.matches(PHONE_REGEX)) {
			return "Номер телефона не соответствует формату вида 8-xxx-xxx-xx-xx";
		}
		return null;

	}
}
