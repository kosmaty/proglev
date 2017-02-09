package com.proglev.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

//@RestController
@RequestMapping(
        value = "/api/pregnancies")
public class PregnancyController {

    @Resource
    private PregnancyRepository pregnancyRepository;

    @GetMapping("/")
    public Iterable<Pregnancy> getAll() {
        return pregnancyRepository.getAll();
    }

    @GetMapping("/{id}")
    public Pregnancy get(@PathVariable Long id) {
        return pregnancyRepository.get(id);
    }

    @PostMapping(value = "/")
    @ResponseStatus(HttpStatus.CREATED)
    public Pregnancy create(@RequestBody Pregnancy pregnancy) throws IOException {
        System.out.println("about to create new Pregnancy: " + pregnancy);
        return pregnancyRepository.create(pregnancy);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void update(@RequestBody Pregnancy pregnancy) throws IOException {
        pregnancyRepository.update(pregnancy);
    }

    @DeleteMapping("/{id}")
    public Pregnancy delete(@PathVariable Long id) throws IOException {
        return pregnancyRepository.delete(id);
    }
}
