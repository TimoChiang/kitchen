package com.timochiang.kitchen.services;

import com.timochiang.kitchen.entities.*;
import com.timochiang.kitchen.repositories.DishRepository;
import com.timochiang.kitchen.repositories.UserIngredientRepository;
import com.timochiang.kitchen.utils.json.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserIngredientRepository userIngredientRepository;
    @MockBean
    private DishRepository dishRepository;
    @MockBean
    private RestTemplate restTemplate;

    private final UserIngredient ui1 = new UserIngredient();
    private List<UserIngredient> userIngredients;
    private final Dish dish1 = new Dish();
    private List<Dish> dishes;
    private MockMultipartFile file;

    @BeforeEach
    public void setUp() throws IOException {
        Category c1 = new Category();
        c1.setId(1);
        c1.setName("cat1");
        Category c2 = new Category();
        c1.setId(2);
        c1.setName("cat2");
        ui1.setId(1);
        ui1.setName("first user ingredient");
        ui1.setQuantity(10.0);
        ui1.setOriginalQuantity(10.0);
        ui1.setCategory(c1);
        ui1.setUnit(Unit.GRAM);
        UserIngredient ui2 = new UserIngredient();
        ui2.setId(2);
        ui2.setName("second user ingredient");
        ui2.setQuantity(3.0);
        ui2.setOriginalQuantity(3.0);
        ui2.setCategory(c2);
        ui2.setUnit(Unit.MILLILITER);
        UserIngredient ui3 = new UserIngredient();
        ui3.setId(3);
        ui3.setName("third user ingredient");
        ui3.setQuantity(1.0);
        ui3.setOriginalQuantity(1.0);
        ui3.setCategory(c1);
        ui3.setUnit(Unit.PIECE);

        c1.setUserIngredients(new ArrayList<>(Arrays.asList(ui1,ui3)));
        c2.setUserIngredients(new ArrayList<>(Collections.singletonList(ui2)));
        userIngredients = new ArrayList<>(Arrays.asList(ui1, ui2, ui3));

        dish1.setId(1);
        dish1.setName("first dish");
        dish1.setScheduleDate("2020-01-08");
        dish1.setScheduleTime("10");
        Dish dish2 = new Dish();
        dish2.setId(2);
        dish2.setName("second dish");
        Dish dish3 = new Dish();
        dish3.setId(3);
        dish3.setName("third recipe");
        dishes = new ArrayList<>(Arrays.asList(dish1 , dish2, dish3));

        DishIngredient di1 = new DishIngredient(); // pass
        di1.setId(1);
        di1.setQuantity(2.0);
        di1.setUserIngredientId(ui1.getId());
        DishIngredient di2 = new DishIngredient(); // pass
        di2.setId(2);
        di2.setQuantity(3.0);
        di2.setUserIngredientId(ui2.getId());
        DishIngredient di3 = new DishIngredient();
        di2.setId(3);
        di2.setName("dish ingredient without user ingredient id");
        di2.setQuantity(5.0);
        DishIngredient di4 = new DishIngredient();
        di4.setId(4);
        di4.setName("dish ingredient with zero quantity");
        di4.setQuantity(0.0);
        di4.setUserIngredientId(ui3.getId());
        DishIngredient di5 = new DishIngredient(); // pass, quantity would be modified
        di5.setId(5);
        di5.setName("dish ingredient with not enough user ingredient");
        di5.setQuantity(100.0);
        di5.setUserIngredientId(ui3.getId());
        dish1.setIngredients(new ArrayList<>(Arrays.asList(di1, di2, di3, di4, di5)));

        FileInputStream inputStream = new FileInputStream("src/test/resources/files/test.jpg");
        file = new MockMultipartFile("receipt_image", "test.jpg", "image/jpg", inputStream);

        // mock prepare
        Mockito.when(userIngredientRepository.findAll()).thenReturn(userIngredients);
        Mockito.when(userIngredientRepository.findById(ui1.getId())).thenReturn(Optional.of(ui1));
        Mockito.when(userIngredientRepository.findById(ui2.getId())).thenReturn(Optional.of(ui2));
        Mockito.when(userIngredientRepository.findById(ui3.getId())).thenReturn(Optional.of(ui3));
        Mockito.when(userIngredientRepository.save(ui1)).thenReturn(ui1);
        Mockito.when(dishRepository.findAll()).thenReturn(dishes);
        Mockito.when(dishRepository.save(dish1)).thenReturn(dish1);
    }

    @Test
    public void findAllIngredient() {
        Iterable<UserIngredient> uis = userService.findAllIngredient();
        assertThat(((Collection<?>) uis).size()).isEqualTo(userIngredients.size());
        int i = 0;
        for (UserIngredient ui : uis) {
            UserIngredient expect = userIngredients.get(i);
            assertThat(ui).isEqualTo(expect);
            i++;
        }
    }

    @Test
    public void findIngredientById() {
        UserIngredient ui = userService.findIngredientById(ui1.getId());
        assertThat(ui.getId()).isEqualTo(ui1.getId());
        assertThat(ui.getName()).isEqualTo(ui1.getName());
    }

    @Test
    public void saveIngredient() {
        UserIngredient ui = userService.saveIngredient(ui1);
        assertThat(ui).isEqualTo(ui1);
    }

    @Test
    public void findAllDish() {
        Iterable<Dish> ds = userService.findAllDish();
        assertThat(((Collection<?>) ds).size()).isEqualTo(dishes.size());
        int i = 0;
        for (Dish d : ds) {
            Dish expect = dishes.get(i);
            assertThat(d).isEqualTo(expect);
            i++;
        }
    }

    @Test
    public void saveDish() {
        Dish dish = userService.saveDish(dish1);
        assertThat(dish).isEqualTo(dish1);
    }

    @Test
    public void createDish() {
        userService.createDish(dish1);
        assertThat(dish1.getIngredients().size()).isEqualTo(3); // di1, di2, di5
        for(DishIngredient di : dish1.getIngredients()) {
            assertTrue("DishIngredient's quantity should greater than 0", di.getQuantity() > 0);
            assertThat(di.getDish()).isEqualTo(dish1);
            for (UserIngredient ui : userIngredients) {
                if (ui.getId().equals(di.getUserIngredientId())) {
                    // check that the settings of dish ingredients
                    assertThat(di.getQuantity()).isEqualTo(ui.getOriginalQuantity() - ui.getQuantity()); // check using quantity
                    assertThat(di.getName()).isEqualTo(ui.getName());
                    assertThat(di.getCategory()).isEqualTo(ui.getCategory());
                    assertThat(di.getUnit()).isEqualTo(ui.getUnit());
                    break;
                }
            }
        }
    }

    @Test
    public void uploadReceipt() throws IOException {
        String mockResponseString = "[{\"name\":\"キッコーマンエンブンヒカエメショウユ\",\"quantity\":1,\"price\":255,\"discount\":0},{\"name\":\"バナメイムキエヒ\",\"quantity\":1,\"price\":279,\"discount\":8},{\"name\":\"ヤマザキ ショウジュン8マイ\",\"quantity\":1,\"price\":78,\"discount\":3},{\"name\":\"コンドウキュウニュウ1000ml\",\"quantity\":2,\"price\":318,\"discount\":0},{\"name\":\"コクサンワカトリムネニク\",\"quantity\":1,\"price\":149,\"discount\":0},{\"name\":\"ニチレイブロッコリー250g\",\"quantity\":1,\"price\":152,\"discount\":0},{\"name\":\"メキシコサンブタモモキリオトシ\",\"quantity\":1,\"price\":401,\"discount\":120}]";
        Mockito.when(restTemplate.postForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.<Class<String>>any()))
          .thenReturn(new ResponseEntity(mockResponseString, HttpStatus.OK));
        List<Product> pds = userService.uploadReceipt(file);
        assertThat(pds.size()).isEqualTo(7);
    }
}
