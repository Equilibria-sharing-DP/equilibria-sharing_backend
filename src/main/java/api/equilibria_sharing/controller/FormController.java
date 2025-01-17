package api.equilibria_sharing.controller;
import api.equilibria_sharing.model.Form;
import api.equilibria_sharing.repositories.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class FormController {

    @Autowired
    private FormRepository formRepository;

    @PostMapping("/forms")
    public ResponseEntity<Form> createForm(@RequestBody Form form) {
        System.out.println("New form received: " + form.toString());
        // calculate guests count and tourist tax
        form.calculateGuests();
        form.calculateTouristTax();

        // save form object into db
        Form savedForm = formRepository.save(form);

        return ResponseEntity.ok(savedForm);
    }

}

