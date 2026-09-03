const phoneInput = document.getElementById("phoneNumber");
const callButton = document.getElementById("callButton");
const cancelButton = document.getElementById("cancelButton");
const eventHistory = document.getElementById("eventHistory");


const PHONE_TEMPLATE = "+7-___-___-__-__";

const PHONE_REGEX =
    /^\+7-\d{3}-\d{3}-\d{2}-\d{2}$/;


/*
 * Микросервис Телефонии.
 */
const QUEUE_URL =
    "http://127.0.0.1:8888/api/queue";


/*
 * Микросервис АТС.
 *
 * Страница работает на localhost:8080,
 * поэтому адрес относительно текущего сервера.
 */
const ACTION_URL =
    "/api/action";


let phoneDigits = "";

let historyLoading = false;



/*
 * =========================
 * ВВОД НОМЕРА ТЕЛЕФОНА
 * =========================
 */


/*
 * Формируем номер:
 *
 * +7-___-___-__-__
 *
 * +7-960-___-__-__
 *
 * +7-960-119-98-57
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


    /*
     * Курсор всегда ставим в конец.
     */
    setTimeout(function () {

        phoneInput.setSelectionRange(
            phoneInput.value.length,
            phoneInput.value.length
        );

    }, 0);
}



/*
 * Пользователь вводит только цифры.
 */
phoneInput.addEventListener(
    "keydown",
    function (event) {

        /*
         * Ctrl-комбинации не блокируем.
         */
        if (event.ctrlKey || event.metaKey) {
            return;
        }


        /*
         * Цифры от 0 до 9.
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
         * Backspace удаляет
         * последнюю введённую цифру.
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
         * Delete очищает весь номер.
         */
        if (event.key === "Delete") {

            event.preventDefault();

            phoneDigits = "";

            updatePhoneInput();

            return;
        }


        /*
         * Tab разрешаем.
         */
        if (event.key === "Tab") {
            return;
        }


        /*
         * Все остальные символы запрещаем.
         */
        event.preventDefault();
    }
);



/*
 * При клике курсор ставим в конец,
 * чтобы пользователь не мог писать
 * внутри шаблона.
 */
phoneInput.addEventListener(
    "click",
    function () {

        this.setSelectionRange(
            this.value.length,
            this.value.length
        );
    }
);



/*
 * Аналогично при получении фокуса.
 */
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
 * Поддержка вставки номера.
 *
 * Можно вставить:
 *
 * 9601199857
 *
 * +79601199857
 *
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


        /*
         * Если вставили номер:
         *
         * 79601199857
         * или
         * 89601199857
         *
         * убираем первую цифру.
         */
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
 * Получаем готовый номер.
 *
 * Если введены не все 10 цифр,
 * запрос не отправляем.
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
 * =========================
 * POST / DELETE В ТЕЛЕФОНИЮ
 * =========================
 */


function sendQueueRequest(method) {

    const phoneNumber =
        getPhoneNumber();


    if (phoneNumber === null) {
        return;
    }


    /*
     * Формируем URL:
     *
     * /api/queue?phoneNumber=...
     *
     * URLSearchParams автоматически
     * корректно закодирует "+".
     */
    const url =
        new URL(QUEUE_URL);


    url.searchParams.set(
        "phoneNumber",
        phoneNumber
    );


    setButtonsDisabled(true);


    fetch(
        url.toString(),
        {
            method: method
        }
    )
        .then(function (response) {

            if (response.ok) {
                return;
            }


            /*
             * Ошибки согласно ТЗ.
             */
            if (response.status === 400) {

                if (method === "POST") {

                    throw new Error(
                        "Этот номер уже находится в очереди"
                    );
                }


                if (method === "DELETE") {

                    throw new Error(
                        "Такого номера нет в очереди"
                    );
                }
            }


            throw new Error(
                "Ошибка сервера. Код: " +
                response.status
            );
        })

        .then(function () {

            /*
             * После успешного действия
             * сразу обновляем историю.
             */
            loadEventHistory();
        })

        .catch(function (error) {

            alert(
                error.message
            );
        })

        .finally(function () {

            setButtonsDisabled(false);
        });
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


/*
 * GET /api/action
 */
function loadEventHistory() {

    /*
     * Не отправляем новый GET,
     * пока предыдущий ещё выполняется.
     */
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

            /*
             * alert здесь не используем,
             * иначе при проблеме он будет
             * появляться каждые 500 мс.
             */
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
 * ОТРИСОВКА ТАБЛИЦЫ
 * =========================
 */


/*
 * AtsEvent:
 *
 * {
 *     phoneNumber: String,
 *     deviceNumber: String,
 *     operatorName: String,
 *     timeStamp: String,
 *     status: String
 * }
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


            /*
             * В колонке "Событие"
             * отображаем Status.
             */
            if (
                event.status !== null &&
                event.status !== undefined
            ) {

                eventCell.textContent =
                    event.status;
            }


            /*
             * В колонке "Данные"
             * отображаем все данные,
             * кроме status.
             *
             * null автоматически
             * пропускается.
             */

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



/*
 * Добавляет поле в колонку "Данные".
 *
 * null и undefined вообще
 * не отображаются.
 */
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



/*
 * Если история пустая,
 * оставляем пустое пространство
 * как на макете.
 */
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


/*
 * Начальное состояние поля.
 */
phoneInput.value =
    PHONE_TEMPLATE;


/*
 * Сразу получаем историю
 * после загрузки страницы.
 */
loadEventHistory();


/*
 * По ТЗ обновляем историю
 * каждые 500 миллисекунд.
 */
setInterval(
    loadEventHistory,
    500
);