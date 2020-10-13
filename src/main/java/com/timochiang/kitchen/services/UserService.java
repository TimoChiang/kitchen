package com.timochiang.kitchen.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timochiang.kitchen.entities.Dish;
import com.timochiang.kitchen.entities.DishIngredient;
import com.timochiang.kitchen.entities.UserIngredient;
import com.timochiang.kitchen.utils.json.Product;
import com.timochiang.kitchen.repositories.DishRepository;
import com.timochiang.kitchen.repositories.UserIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserIngredientRepository userIngredientRepository;
    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${OCR_URL:http://localhost}")
    private String ocrUrl;

    public Iterable<UserIngredient> findAllIngredient() {
        return userIngredientRepository.findAll();
    }

    public UserIngredient findIngredientById(Integer id) {
        return userIngredientRepository.findById(id).orElseThrow();
    }

    public UserIngredient saveIngredient(UserIngredient ingredient) {
        return userIngredientRepository.save(ingredient);
    }

    public Iterable<Dish> findAllDish() {
        return dishRepository.findAll();
    }

    public Dish saveDish(Dish dish) {
        dish.setScheduleDateTime();
        return dishRepository.save(dish);
    }

    @Transactional
    public Dish createDish(Dish dish) {
        // for safely remove null ingredients
        Iterator<DishIngredient> i = dish.getIngredients().iterator();
        while (i.hasNext()) {
            DishIngredient ingredient = i.next(); // must be called before you can call i.remove()
            if (ingredient.getUserIngredientId() != null && ingredient.getQuantity() != null && ingredient.getQuantity() > 0) {
                UserIngredient userIngredient = this.findIngredientById(ingredient.getUserIngredientId());
                if (ingredient.getQuantity() <= userIngredient.getQuantity()) {
                    userIngredient.setQuantity(userIngredient.getQuantity() - ingredient.getQuantity());
                } else {
                    // maximum quantity follows user ingredient's maximum quantity
                    ingredient.setQuantity(userIngredient.getQuantity());
                    userIngredient.setQuantity(0.0);
                }
                this.saveIngredient(userIngredient);
                this.setDataFromIngredient(ingredient, userIngredient);
                ingredient.setDish(dish);
            } else {
                i.remove();
            }
        }
        return this.saveDish(dish);
    }

    private void setDataFromIngredient(DishIngredient i, UserIngredient a) {
        i.setName(a.getName());
        i.setUnit(a.getUnit());
        i.setCategory(a.getCategory());
    }

    public List<Product> uploadReceipt(MultipartFile file) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(new URL(ocrUrl).toString(), requestEntity, String.class);
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(response.getBody(), new TypeReference<>(){});
    }

    static class MultipartInputStreamFileResource extends InputStreamResource {

        private final String filename;

        MultipartInputStreamFileResource(InputStream inputStream, String filename) {
            super(inputStream);
            this.filename = filename;
        }

        @Override
        public String getFilename() {
            return this.filename;
        }

        @Override
        public long contentLength() throws IOException {
            return -1; // we do not want to generally read the whole stream into memory ...
        }
    }

}
