package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getItemById(Long ownerId, Long itemId) {
        return get("/" + itemId, ownerId);
    }

    public ResponseEntity<Object> getAllItemsByOwnerId(Long ownerId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", ownerId, parameters);
    }

    public ResponseEntity<Object> createItem(Long ownerId, ItemCreateDto itemCreateDto) {
        return post("", ownerId, itemCreateDto);
    }

    public ResponseEntity<Object> updateItem(Long ownerId, ItemCreateDto itemCreateDto, Long itemId) {
        return patch("/" + itemId, ownerId, itemCreateDto);
    }

    public ResponseEntity<Object> searchItemsByTextInNameAndDescription(Long ownerId, String text, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", ownerId, parameters);
    }

    public ResponseEntity<Object> deleteItemById(Long ownerId, Long itemId) {
        return delete("/" + itemId, ownerId);
    }

    public ResponseEntity<Object> createComment(Long userId, CommentCreateDto commentCreateDto, Long itemId) {
        return post("/" + itemId + "/comment", userId, commentCreateDto);
    }
}
