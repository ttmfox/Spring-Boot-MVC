package vn.hoidanit.laptopshop.controller.client;

import java.util.ArrayList;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import vn.hoidanit.laptopshop.domain.Cart;
import vn.hoidanit.laptopshop.domain.CartDetail;

import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.Product_;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.domain.dto.ProductCriteriaDTO;
import vn.hoidanit.laptopshop.service.ProductService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;




@Controller
public class ItemController {

private final ProductService productService;

    public ItemController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product/{id}")    
    public String getItemDetailPage(Model model, @PathVariable long id){
        Product product = this.productService.getProductById(id).get();
        model.addAttribute("product", product);
        model.addAttribute("id", id);
        model.addAttribute("name", product.getName());
        model.addAttribute("price", product.getPrice());
        model.addAttribute("shortDesc", product.getShortDesc());
        model.addAttribute("factory", product.getFactory());
        model.addAttribute("detailDesc", product.getDetailDesc());
        return "client/product/detail";
    }

    @PostMapping("/add-product-to-cart/{id}")   
    public String addProductToCart(@PathVariable long id, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        String email =(String) session.getAttribute("email");
        long productId = id;
        
        this.productService.handleAddProductToCart(email, productId, session, 1);
        return "redirect:/";
    }
    
    @GetMapping("/cart")
    public String getCartTable(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        User user = new User(); 

        long id =(long) session.getAttribute("id");
        user.setId(id);
        Cart cart = this.productService.getCartByUser(user);

        List<CartDetail> cartDetails = cart == null ? new ArrayList<>() :cart.getCartDetails();
        double totalPrice = 0;
        for (CartDetail cd : cartDetails) {
            totalPrice += cd.getPrice()*cd.getQuantity();
        }
        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cart", cart);
        return "client/cart/show";
    }

    @PostMapping("/delete-cart-product/{id}")    
    public String deleteCartDetail(@PathVariable Long id, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        long cartDetailId = id;
        this.productService.deleteACartDetail(cartDetailId, session);
        return "redirect:/cart";
    }


    @GetMapping("/checkout")
    public String getCheckOutPage(Model model, HttpServletRequest request) {
        User currentUser = new User();// null
        HttpSession session = request.getSession(false);
        long id = (long) session.getAttribute("id");
        currentUser.setId(id);

        Cart cart = this.productService.getCartByUser(currentUser);

        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();

        double totalPrice = 0;
        for (CartDetail cd : cartDetails) {
            totalPrice += cd.getPrice() * cd.getQuantity();
        }

        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", totalPrice);

        return "client/cart/checkout";
    }


    @PostMapping("/confirm-checkout")
    public String getCheckoutPage(@ModelAttribute("cart") Cart cart) {
        List<CartDetail> cartDetails = cart == null ? new ArrayList<>(null) : cart.getCartDetails();
        this.productService.handleUpdateCartBeforeCheckout(cartDetails); 
        
        return "redirect:/checkout";
    }
    @PostMapping("/place-order")
    public String handlePlaceOrder(
            HttpServletRequest request,
            @RequestParam("receiverName") String receiverName,
            @RequestParam("receiverAddress") String receiverAddress,
            @RequestParam("receiverPhone") String receiverPhone) {
        HttpSession session = request.getSession(false);
        User currentUser = new User();// null
        long id = (long) session.getAttribute("id");
        currentUser.setId(id);
        
        this.productService.handlePlaceOrder(currentUser, session, receiverName, receiverAddress, receiverPhone);
        return "redirect:/thanks";
    }
    @PostMapping("/add-product-from-view-detail")
    public String handleAddProductFromViewDetail(
            HttpServletRequest request,
            @RequestParam("id") long id,
            @RequestParam("quantity") long quantity) {
        HttpSession session = request.getSession(false);
        String email = (String) session.getAttribute("email");
        this.productService.handleAddProductToCart(email, id, session, quantity);
        return "redirect:/product/" + id;
    }
    @GetMapping("/thanks")
    public String getThanksPage(Model model) {
        return "client/cart/thanks";
    }
    
    @GetMapping("/products")
    public String getProductsPage(Model model, ProductCriteriaDTO productCriteriaDTO, HttpServletRequest request) {
        int page = 1;
        try {
            if (productCriteriaDTO.getPage().isPresent()) {
                page = Integer.parseInt(productCriteriaDTO.getPage().get());
            } else {
                // page = 1
            }
        } catch (Exception e) {
            // page = 1
        }
        Pageable pageable = PageRequest.of(page - 1, 3);
        if (productCriteriaDTO.getSort() != null && productCriteriaDTO.getSort().isPresent()) {
            String sort = productCriteriaDTO.getSort().get();
            if (sort.equals("gia-tang-dan")) {
                pageable = PageRequest.of(page - 1, 3, Sort.by(Product_.PRICE).ascending());
            }
            else if (sort.equals("gia-giam-dan")) {
                pageable = PageRequest.of(page - 1, 3, Sort.by(Product_.PRICE).descending());
                
            }
        }
        
        Page<Product> products = this.productService.getAllProductsWithSpec(pageable, productCriteriaDTO);
        
        List<Product> listProduct = products.getContent().size() > 0 ? products.getContent() : new ArrayList<Product>();
        String queryString = request.getQueryString();
        if (queryString != null && !queryString.isBlank()) {
            // remove page
            queryString = queryString.replace("page=" + page, "");
        }

        model.addAttribute("products", listProduct);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("queryString", queryString);
        return "client/product/show";
    }
    
}
