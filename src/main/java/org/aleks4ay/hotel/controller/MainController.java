package org.aleks4ay.hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@Controller
public class MainController {

/*
    @GetMapping("/")
    public String index() {
        return "index";
    }
*/

    @RequestMapping("/shopcar")
    public String hello(Map<String, Object> model) {
        model.put("name", "Spring Boot with Thymeleaf .html");
        return "hello";
    }

/*    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String hello(Model model, @RequestParam(defaultValue = "") String searchName) {
        List<List<String>> persons = userRepo.getRepository();
        List<List<String>> filterList = persons.stream()
                .filter(p -> p.get(0).contains(searchName))
                .collect(Collectors.toList());
        model.addAttribute("persons", filterList);
        model.addAttribute("lastSearch", searchName);
        return "persons";
    }*/


/*    @RequestMapping(value = "/newClient", method = RequestMethod.POST)
    public String saveClient(Model model, @ModelAttribute("client") User client) {
        clientRepo.save(client);
        return "redirect:user_list";
    }*/


/*
        @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/myapp/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name,
                           Map<String, Object> model) {
        model.put("name", name);
        return "greeting";
    }
    @GetMapping("/myapp/shop")
    public String main(Map<String, Object> model) {
        Iterable<Product> products = productRepository.findAll();
        model.put("products", products);
        return "main_page";
    }

    @PostMapping("/myapp/add")
    public String main(@RequestParam String name,@RequestParam Integer price,
                       @RequestParam Integer amount,@RequestParam String description,
                       Map<String, Object> model) {
        Product product = new Product(name, price,amount,description);
        productRepository.save(product);
        return "redirect:shop";
    }

    @PostMapping("/myapp/filter")
    public String filter (@RequestParam String filter, Map<String, Object> model) {
        Iterable<Product> products;
        if (filter != null && !filter.isEmpty()) {
            products = productRepository.findByName(filter);
            model.put("products", products);
            return "main_page";
        } else {
            return "redirect:shop";
        }
    }
    @PostMapping("/myapp/delete")
    public String deleteById (@RequestParam String id, Map<String, Object> model) {
        productRepository.deleteById(Integer.valueOf(id));
        return "redirect:shop";
    }

    @RequestMapping("/")
    public String start() {
        return "redirect:myapp/shop";
    }

    @GetMapping("/customer_add")
    public String registration(Map<String, Object> model) {
        return "customer_new";
    }
    @PostMapping("/customer_add")
    public String registration(@RequestParam String name,@RequestParam String address,
                       @RequestParam String email, Map<String, Object> model) {
        Customer customer = new Customer(name, email, address);
        customerRepository.save(customer);
        return "redirect:customer";
    }

    @GetMapping("/delete_customer")
    public String deleteCustomerById (@RequestParam String id, Map<String, Object> model) {
        customerRepository.deleteById(Long.valueOf(id));
        return "redirect:customer";
    }*/
}
