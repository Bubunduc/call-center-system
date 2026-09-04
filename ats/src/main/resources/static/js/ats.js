const phoneInput = document.getElementById("phoneNumber");
const callButton = document.getElementById("callButton");
const cancelButton = document.getElementById("cancelButton");
const eventHistory = document.getElementById("eventHistory");

const PHONE_TEMPLATE = "+7-___-___-__-__";

/*
 * Формат, который видит пользователь:
 * +7-960-119-98-57
 */
const PHONE_REGEX =
    /^\+7-\d{3}-\d{3}-\d{2}-\d{2}$/;

const QUEUE_URL =
    "http://127.0.0.1:8888/api/queue";

const ACTION_URL =
    "/api/action";

let phoneDigits = "";
let historyLoading = false;


/*
 * =========================
 * МАСКА НОМЕРА
 * =========================
 */

function updatePhoneInput() {

    const digits =
        phoneDigits.padEnd(10, "_");

    phoneInput.value =
        "+7-" +
        digits.substring(0, 3) +
        "-" +
        digits.substring(3, 6) +
        "-" +
        digits.substring(6, 8) +
        "-" +
        digits.substring(8, 10);

    setTimeout(function () {
        phoneInput.setSelectionRange(
            phoneInput.value.length,
            phoneInput.value.length
        );
    }, 0);
}


phoneInput.addEventListener(
    "keydown",
    function (event) {

        if (event.ctrlKey || event.metaKey) {
            return;
        }

        /*
         * Ввод цифр
         */
        if (/^\d$/.test(event.key)) {

            event.preventDefault();

            if (phoneDigits.length >= 10) {
                return;
            }

            phoneDigits += event.key;

            updatePhoneInput();

            return;
        }

        /*
         * Удаление последней цифры
         */
        if (event.key === "Backspace") {

            event.preventDefault();

            if (phoneDigits.length > 0) {

                phoneDigits =
                    phoneDigits.substring(
                        0,
                        phoneDigits.length - 1
                    );

                updatePhoneInput();
            }

            return;
        }

        /*
         * Очистка номера
         */
        if (event.key === "Delete") {

            event.preventDefault();

            phoneDigits = "";

            updatePhoneInput();

            return;
        }

        if (event.key === "Tab") {
            return;
        }

        event.preventDefault();
    }
);


phoneInput.addEventListener(
    "click",
    function () {

        this.setSelectionRange(
            this.value.length,
            this.value.length
        );
    }
);


phoneInput.addEventListener(
    "focus",
    function () {

        const input = this;

        setTimeout(function () {

            input.setSelectionRange(
                input.value.length,
                input.value.length
            );

        }, 0);
    }
);


/*
 * Поддержка вставки:
 *
 * 9601199857
 * +79601199857
 * 89601199857
 */
phoneInput.addEventListener(
    "paste",
    function (event) {

        event.preventDefault();

        let text =
            event.clipboardData.getData("text");

        let digits =
            text.replace(/\D/g, "");

        if (
            digits.length === 11 &&
            (digits.startsWith("7") ||
             digits.startsWith("8"))
        ) {

            digits = digits.substring(1);
        }

        phoneDigits =
            digits.substring(0, 10);

        updatePhoneInput();
    }
);


/*
 * =========================
 * КНОПКИ
 * =========================
 */

callButton.addEventListener(
    "click",
    function () {
        sendQueueRequest("POST");
    }
);


cancelButton.addEventListener(
    "click",
    function () {
        sendQueueRequest("DELETE");
    }
);


/*
 * Получаем номер в клиентском формате:
 *
 * +7-960-119-98-57
 */
function getPhoneNumber() {

    if (phoneDigits.length !== 10) {

        alert(
            "Введите номер телефона полностью"
        );

        phoneInput.focus();

        return null;
    }

    const phoneNumber =
        phoneInput.value;

    if (!PHONE_REGEX.test(phoneNumber)) {

        alert(
            "Номер должен быть в формате " +
            "+7-909-589-45-82"
        );

        phoneInput.focus();

        return null;
    }

    return phoneNumber;
}


/*
 * Преобразуем клиентский формат:
 *
 * +7-960-119-98-57
 *
 * в серверный:
 *
 * 8-960-119-98-57
 */
function toServerPhoneNumber(phoneNumber) {

    return phoneNumber.replace(
        /^\+7/,
        "8"
    );
}


/*
 * =========================
 * POST / DELETE
 * =========================
 */

function sendQueueRequest(method) {

    const phoneNumber =
        getPhoneNumber();

    if (phoneNumber === null) {
        return;
    }

    /*
     * В интерфейсе остаётся +7,
     * а на сервер отправляется 8.
     */
    const serverPhoneNumber =
        toServerPhoneNumber(phoneNumber);

    const url =
        new URL(QUEUE_URL);

    url.searchParams.set(
        "phoneNumber",
        serverPhoneNumber
    );

    setButtonsDisabled(true);

    fetch(
        url.toString(),
        {
            method: method
        }
    )
        .then(function (response) {

            /*
             * Читаем тело ответа,
             * чтобы при ошибке показать
             * именно сообщение сервера.
             */
            return response.text()
                .then(function (body) {

                    if (!response.ok) {

                        const message =
                            getErrorMessage(body);

                        if (message !== null) {

                            throw new Error(
                                message
                            );
                        }

                        throw new Error(
                            "Ошибка сервера. Код: " +
                            response.status
                        );
                    }

                    return body;
                });
        })

        .then(function () {

            loadEventHistory();
        })

        .catch(function (error) {

            alert(error.message);
        })

        .finally(function () {

            setButtonsDisabled(false);
        });
}


/*
 * Если сервер вернул JSON:
 *
 * {
 *   "message": "..."
 * }
 *
 * показываем только message.
 *
 * Если пришла обычная строка -
 * показываем строку.
 */
function getErrorMessage(body) {

    if (
        !body ||
        body.trim() === ""
    ) {
        return null;
    }

    try {

        const error =
            JSON.parse(body);

        if (error.message) {
            return error.message;
        }

        return body;

    } catch (e) {

        return body;
    }
}


function setButtonsDisabled(disabled) {

    callButton.disabled =
        disabled;

    cancelButton.disabled =
        disabled;
}


/*
 * =========================
 * ИСТОРИЯ СОБЫТИЙ
 * =========================
 */

function loadEventHistory() {

    if (historyLoading) {
        return;
    }

    historyLoading = true;

    fetch(
        ACTION_URL,
        {
            method: "GET"
        }
    )
        .then(function (response) {

            if (!response.ok) {

                throw new Error(
                    "Ошибка получения истории. Код: " +
                    response.status
                );
            }

            return response.json();
        })

        .then(function (events) {

            renderEventHistory(events);
        })

        .catch(function (error) {

            console.error(
                "Не удалось получить историю событий:",
                error
            );
        })

        .finally(function () {

            historyLoading = false;
        });
}


/*
 * =========================
 * ТАБЛИЦА
 * =========================
 */

function renderEventHistory(events) {

    eventHistory.innerHTML = "";

    if (
        !events ||
        events.length === 0
    ) {

        appendEmptyRow();

        return;
    }

    events.forEach(
        function (event) {

            const row =
                document.createElement("tr");

            const eventCell =
                document.createElement("td");

            const dataCell =
                document.createElement("td");

            if (
                event.status !== null &&
                event.status !== undefined
            ) {

                eventCell.textContent =
                    event.status;
            }

            appendData(
                dataCell,
                "Номер телефона",
                event.phoneNumber
            );

            appendData(
                dataCell,
                "Номер аппарата",
                event.deviceNumber
            );

            appendData(
                dataCell,
                "Оператор",
                event.operatorName
            );

            appendData(
                dataCell,
                "Время",
                event.timeStamp
            );

            row.appendChild(
                eventCell
            );

            row.appendChild(
                dataCell
            );

            eventHistory.appendChild(
                row
            );
        }
    );
}


function appendData(
    cell,
    label,
    value
) {

    if (
        value === null ||
        value === undefined
    ) {
        return;
    }

    const line =
        document.createElement("div");

    line.className =
        "event-data-line";

    line.textContent =
        label + ": " + value;

    cell.appendChild(line);
}


function appendEmptyRow() {

    const row =
        document.createElement("tr");

    row.className =
        "empty-space";

    const eventCell =
        document.createElement("td");

    const dataCell =
        document.createElement("td");

    row.appendChild(
        eventCell
    );

    row.appendChild(
        dataCell
    );

    eventHistory.appendChild(
        row
    );
}


/*
 * =========================
 * ЗАПУСК
 * =========================
 */

phoneInput.value =
    PHONE_TEMPLATE;

loadEventHistory();

setInterval(
    loadEventHistory,
    500
);