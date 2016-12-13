package khoi.vu.service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import khoi.vu.model.Genre;
import khoi.vu.repository.GenreRepository;

@Service
public class GenreServiceImpl implements GenreService {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private GenreRepository genreRepository;

	@Override
	public Genre findById(String id) {
		return genreRepository.findOne(id);

	}

	@Override
	public void saveGenre(Genre genre) {
		genre.setGenreId(UUID.randomUUID().toString());
		genreRepository.save(genre);
	}

	@Override
	public void updateGenre(Genre genre) {
		genreRepository.save(genre);
	}

	@Override
	public void deleteGenreById(String id) {
		genreRepository.delete(id);
	}

	@Override
	public void deleteGenre(Genre genre) {
		genreRepository.delete(genre);
	}

	@Override
	public List<Genre> findAllGenres() {
		List<Genre> lstGenre = (List<Genre>) mongoTemplate.findAll(Genre.class);
		System.out.println("testGen");
		return lstGenre != null ? lstGenre : Collections.<Genre> emptyList();
	}

	@Override
	public Genre findByName(String genreName) {

		if (genreName != null && genreName.trim().length() > 0) {
			Query query = new Query();
			query.addCriteria(Criteria.where("genreName").is(genreName));
			return mongoTemplate.findOne(query, Genre.class);
		}
		return null;
	}

}
