package ru.practicum.server.item.comment.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.server.item.comment.model.Comment;
import ru.practicum.server.item.model.Item;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByItem(Item item);

    @Query("SELECT c FROM Comment c WHERE c.item IN :items")
    List<Comment> findAllByItems(@Param("items") List<Item> items);

    List<Comment> findByItemIn(List<Item> items, Sort sort);
}
