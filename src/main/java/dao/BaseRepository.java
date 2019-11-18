package dao;

import java.util.List;

public interface BaseRepository<T> {
    T get( Integer id);
    List<T> getAll();
}
