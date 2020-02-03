package ru.korolev.springboot.service;

import java.util.List;
import java.util.stream.Collectors;

public interface DtoService<E, D> {

    D toDto(E entity);

    default List<D> toDto(List<E> entities) {
        return entities == null ? null : entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    E toEntity(D dto);
}