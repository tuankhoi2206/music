package khoi.vu.service;

import java.util.List;

import khoi.vu.model.Genre;

public interface GenreService {

	Genre findById(String id);

	Genre findByName(String genreName);

	void saveGenre(Genre genre);

	void updateGenre(Genre genre);

	void deleteGenreById(String id);

	void deleteGenre(Genre genre);

	List<Genre> findAllGenres();

}
