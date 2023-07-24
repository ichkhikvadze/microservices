package com.example.service;

import com.example.domain.Person;
import com.example.exception.PersonNotFoundException;
import com.example.exception.PersonalNoAlreadyExistsException;
import com.example.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PersonService {

    private final PersonRepository repository;

    @Autowired
    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    public Iterable<Person> findAllPersons(final int page, final int pageSize) {
        log.info(String.format("Find %s persons on %s page", pageSize, page));
        Pageable pageable = PageRequest.of(page, pageSize);
        return repository.findAll(pageable).stream()
                .collect(Collectors.toList());
    }

    public Person createPerson(final Person person) {
        log.info(String.format("Create %s", person));
        if (repository.existsPersonByPersonalNo(person.getPersonalNo())) {
            log.debug(String.format("Person with personal no %s already exists", person.getPersonalNo()));
            throw new PersonalNoAlreadyExistsException(String.format("Person with personal no %s already exists", person.getPersonalNo()));
        }
        return repository.save(person);
    }

    public Person findPersonById(final long id) {
        log.info(String.format("Find person by id = %s", id));
        Optional<Person> personOptional = repository.findById(id);
        if (personOptional.isEmpty()) {
            log.debug(String.format("Person not found by id = %s", id));
            throw new PersonNotFoundException(String.format("Person not found by id = %s", id));
        }
        return personOptional.get();
    }

    public Person updatePerson(final long id, final Person person) {
        log.info(String.format("Update person with id = %s to %s", id, person));
        if (!repository.existsById(id)) {
            log.debug(String.format("Person not found by id = %s", id));
            throw new PersonNotFoundException(String.format("Person not found by id = %s", id));
        }
        Optional<Person> personOptional = repository.findPersonByPersonalNo(person.getPersonalNo());
        if (personOptional.isPresent() && personOptional.get().getId() != id) {
            log.debug(String.format("Person with personal no %s already exists", person.getPersonalNo()));
            throw new PersonalNoAlreadyExistsException(String.format("Person with personal no %s already exists", person.getPersonalNo()));
        }
        person.setId(id);
        return repository.save(person);
    }

    public Person deletePersonById(final long id) {
        log.info(String.format("Delete person with id = %s", id));
        Optional<Person> personOptional = repository.findById(id);
        if (personOptional.isEmpty()) {
            log.debug(String.format("Person not found by id = %s", id));
            throw new PersonNotFoundException(String.format("Person not found by id = %s", id));
        }
        repository.deleteById(id);
        return personOptional.get();
    }

    public Person findPersonByPersonalNo(final String personalNo) {
        return repository.findPersonByPersonalNo(personalNo)
                .orElseThrow(() ->
                        new PersonNotFoundException(String.format("Person not found by personal no = %s", personalNo))
                );
    }
}
