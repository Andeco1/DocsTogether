package ru.docsrogether.application.core.entrypoints.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import ru.docsrogether.application.core.entrypoints.websocket.dto.EditorChange;
import ru.docsrogether.application.core.usecase.ManageDocumentUseCase;

@Controller
@RequiredArgsConstructor
public class DocumentSocketController {

    private final ManageDocumentUseCase manageDocumentUseCase;

    /**
     * Этот метод будет вызван, когда клиент отправит сообщение на:
     * /app/document.edit/{id}
     *
     * @param id     ID документа (из URL)
     * @param change Содержимое сообщения (JSON)
     * @return Объект EditorChange, который будет отправлен всем подписчикам.
     */
    @MessageMapping("/document.edit/{id}") // Куда КЛИЕНТЫ шлют сообщения
    @SendTo("/topic/document/{id}")      // Куда СЕРВЕР шлет ответ
    public EditorChange handleEdit(@DestinationVariable String id, EditorChange change) {

        try {
            manageDocumentUseCase.updateDocumentContent(id, change.getNewContent());
        } catch (RuntimeException e) {
            // Если документ не найден, просто не рассылаем обновление
            // (В идеале, надо бы вернуть ошибку отправителю)
            return null;
        }

        // 2. Возвращаем изменение.
        // Аннотация @SendTo автоматически отправит этот объект
        // всем, кто подписан на /topic/document/{id}

        // Мы не отправляем изменение назад тому, кто его прислал
        // (хотя @SendTo отправит всем, включая его).
        // Клиентская библиотека (типа Stomp.js) обычно сама разбирается,
        // что это его собственное изменение, и не обновляет поле.

        return change;
    }
}