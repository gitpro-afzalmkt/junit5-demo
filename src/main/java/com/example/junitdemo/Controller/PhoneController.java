package com.example.junitdemo.Controller;

import com.example.junitdemo.Entity.Phone;
import com.example.junitdemo.Repository.PhoneRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/phones")
public class PhoneController {

    @Autowired
    private PhoneRepository phoneRepository;


    @GetMapping
    public List<Phone> getAllPhones() { return phoneRepository.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPhoneById(@PathVariable("id") Long id) {
        Optional<Phone> phone = phoneRepository.findById(id);
        if(phone.isEmpty()) return new ResponseEntity<>("Phone Not Found!!", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(phone.get(), HttpStatus.OK);
    }

    @PostMapping("/newPhone")
    public Phone addNewPhone(@RequestBody Phone phone) {
        return phoneRepository.save(phone);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePhone(@RequestBody Phone phone) {
        if(phone == null || phone.getId() == null || phone.getCompany() == null || phone.getModel() == null
        || phone.getRam() == null || phone.getStorage() == null) {
            return new ResponseEntity<>("Values can't be null", HttpStatus.BAD_REQUEST);
        }

        if(phoneRepository.findById(phone.getId()).isEmpty()) {
            return new ResponseEntity<>("Phone Not Found!!", HttpStatus.BAD_REQUEST);
        }

        Phone existingPhone = phoneRepository.findById(phone.getId()).get();
        existingPhone.setCompany(phone.getCompany());
        existingPhone.setModel(phone.getModel());
        existingPhone.setRam(phone.getRam());
        existingPhone.setStorage(phone.getStorage());
        Phone updatedPhone = phoneRepository.save(existingPhone);
        return new ResponseEntity<>(updatedPhone, HttpStatus.OK);
    }

    @DeleteMapping("/deletePhone/{id}")
    public void deletePhone(@PathVariable("id") Long id) throws NotFoundException {
        if(phoneRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Phone Not Found");
        }
        phoneRepository.deleteById(id);
    }

}
