package br.com.topsys.service.jpa;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public abstract class TSJpaService<T> {

	protected abstract JpaRepository<T, Long> getRepository();

	@GetMapping("/all")
	public List<T> all() {

		return this.getRepository().findAll();
	}

	@GetMapping("/{id}")
	public T get(@PathVariable Long id) {

		return this.getRepository().getReferenceById(id);

	}

	@PostMapping
	public T save(@RequestBody @Valid T model) {

		return this.getRepository().save(model);

	}

	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable Long id) {
		try {
			this.getRepository().deleteById(id);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
		}

		return new ResponseEntity(HttpStatus.OK);

	}

	@PostMapping("/find")
	public List<T> find(@RequestBody T model) {

		return this.getRepository().findAll(Example.of(model,
				ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.STARTING)));

	}

}
