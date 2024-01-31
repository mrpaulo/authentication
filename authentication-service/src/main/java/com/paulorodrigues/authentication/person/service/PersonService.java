package com.paulorodrigues.authentication.person.service;

import com.paulorodrigues.authentication.address.service.AddressService;
import com.paulorodrigues.authentication.commons.exception.InvalidRequestException;
import com.paulorodrigues.authentication.commons.exception.NotFoundException;
import com.paulorodrigues.authentication.commons.exception.ValidationException;
import com.paulorodrigues.authentication.commons.model.PersonDTO;

import com.paulorodrigues.authentication.commons.model.PersonQuery;
import com.paulorodrigues.authentication.commons.model.PersonRequest;
import com.paulorodrigues.authentication.commons.model.PersonResponse;
import com.paulorodrigues.authentication.commons.util.MessageUtil;
import com.paulorodrigues.authentication.person.entity.Person;
import com.paulorodrigues.authentication.person.repository.PersonRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.paulorodrigues.authentication.commons.util.FormatUtil.buildPageable;
import static com.paulorodrigues.authentication.commons.util.FormatUtil.removeFormatCPF;


@Service
@Log4j2
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AddressService addressService;
    
    public List<PersonDTO> findAll() {
        return peopleToDTOs(personRepository.findAll());
    }

    public PersonResponse findByQueryPageable(PersonRequest personRequest) {
        PersonQuery personQuery = personRequest.getQuery();
        Pageable pageable = buildPageable(personQuery);

        Page<Person> personPage = personRepository.findByFilterPageable(
                personQuery.getId(),
                personQuery.getName(),
                personQuery.getEmail(),
                removeFormatCPF(personQuery.getCpf()),
                personQuery.getSex(),
                personQuery.getStartDate(),
                personQuery.getEndDate(),
                pageable);
        
        
        return new PersonResponse(peoplePageToDTOs(personPage));
    }

    public Person findById(Long personId) throws NotFoundException {
        log.info("Finding person by personId={}", personId);
        return personRepository.findById(personId)
                .orElseThrow(
                        () -> new NotFoundException(String.format(MessageUtil.getMessage("PERSON_NOT_FOUND"), " ID: %s", personId))
                );
    }

    public PersonDTO findByIdDTO(Long personId) throws NotFoundException {
        return findById(personId).toDTO();
    }

    public PersonResponse findByName (PersonRequest personRequest) throws InvalidRequestException {
        validatePersonRequestFindByName(personRequest);
        PersonQuery personQuery = personRequest.getQuery();
        log.info("Finding person by name={}", personQuery.getName());
        Pageable pageable = buildPageable(personQuery);
        Page<Person> personPage = personRepository.findByName(personQuery.getName(), pageable);
        return new PersonResponse(peoplePageToDTOs(personPage));
    }

    private void validatePersonRequestFindByName(PersonRequest personRequest) throws InvalidRequestException {
        if(Objects.isNull(personRequest) || Objects.isNull(personRequest.getQuery()) || Strings.isBlank(personRequest.getQuery().getName())){
            throw new InvalidRequestException(MessageUtil.getMessage("REQUEST_PERSON_NAME_INVALID"));
        }
    }

    public PersonDTO create(PersonDTO person) throws ValidationException, InvalidRequestException {
        assert Objects.nonNull(person) : MessageUtil.getMessage("SAMPLE_IS_NULL");

        log.info("Creating person name={}", person.getName());
        return save(personFromDTO(person)).toDTO();
    }

    public Person save(Person person) throws ValidationException, InvalidRequestException {
        person.validation();
        log.info("Saving person={}", person);
        return personRepository.saveAndFlush(person);
    }

    public PersonDTO update(Long personId, PersonDTO personEdited) throws ValidationException, NotFoundException, InvalidRequestException {
        Person personToEdit = findById(personId);
        ModelMapper mapper = new ModelMapper();
        personToEdit = mapper.map(personEdited, Person.class);
        log.info("Updating person id={}, name={}", personId, personToEdit.getName());
        return save(personToEdit).toDTO();
    }

    public void delete(Long personId) throws NotFoundException {
        Person person = findById(personId);
        log.info("Deleting person id={}, name={}", personId, person.getName());
        personRepository.delete(person);
    }

    public Person personFromDTO(PersonDTO personDTO) {
        if (Optional.ofNullable(personDTO).isEmpty()) {
            return null;
        }
        return Person.builder()
                .id(personDTO.getId())
                .firstName(personDTO.getFirstName())
                .lastName(personDTO.getLastName())
                .gender(personDTO.getGender())
                .email(personDTO.getEmail())
                .address(addressService.getAddressFromDTO(personDTO.getAddress()))
                .birthdate(personDTO.getBirthdate())
                .birthCity(addressService.getCityFromDTO(personDTO.getBirthCity()))
                .birthCountry(addressService.getCountryFromDTO(personDTO.getBirthCountry()))
                .build();
    }

    public List<PersonDTO> peopleToDTOs(List<Person> people) {
        return people
                .stream()
                .map(Person::toDTO).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }


    public Page<PersonDTO> peoplePageToDTOs(Page<Person> people) {
        List<PersonDTO> personDTOList = people.getContent()
                .stream()
                .map(Person::toDTO)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        return new PageImpl<>(personDTOList, people.getPageable(), people.getTotalElements());
    }

    public List<Person> peopleFromDTOs(List<PersonDTO> peopleDTO) {
        List<Person> result = new ArrayList<>();
        if (Optional.ofNullable(peopleDTO).isPresent()) {
            for (PersonDTO personDTO : peopleDTO) {
                result.add(personFromDTO(personDTO));
            }
        }
        return result;
    }
}
