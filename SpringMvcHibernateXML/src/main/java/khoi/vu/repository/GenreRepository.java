package khoi.vu.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import khoi.vu.model.Genre;

@Repository
public interface GenreRepository extends CrudRepository<Genre, String> {
}
