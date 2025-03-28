package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UploadService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

@Controller
public class ProductController {

    private final UploadService uploadService;
    private final ProductService productService;

    public ProductController(UploadService uploadService, ProductService productService) {
        this.uploadService = uploadService;
        this.productService = productService;
    }

    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    @PostMapping("/admin/product/create")
    public String createNewProduct(Model model,
            @ModelAttribute("newProduct") @Valid Product product,
            BindingResult newProductBindingResult,
            @RequestParam("hoidanitFile") MultipartFile file) {

        List<FieldError> errors = newProductBindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(">>> " + error.getField() + " - " + error.getDefaultMessage());
        }

        if (newProductBindingResult.hasErrors()) {
            return "admin/product/create";
        }

        String avatar = this.uploadService.handleSaveFile(file, "product");
        product.setImage(avatar);

        // Save
        this.productService.createProduct(product);

        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product")
    public String getProductTable(Model model, @RequestParam("page") Optional<String> optionalPage) {
        int page = 1;
        try {
            if (optionalPage.isPresent()) {
                page = Integer.parseInt(optionalPage.get());
            } else {
                // page = 1
            }
        } catch (Exception e) {
            // page = 1
        }
        Pageable pageable = PageRequest.of(page - 1, 2);
        Page<Product> arrProduct = this.productService.getAllProducts(pageable);
        List<Product> listProducts = arrProduct.getContent();

        model.addAttribute("products", listProducts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", arrProduct.getTotalPages());

        return "admin/product/show";
    }

    @RequestMapping("/admin/product/{id}")
    public String getDetailUser(Model model, @PathVariable Long id) {
        Product product = this.productService.getProductById(id).get();
        model.addAttribute("product", product);
        model.addAttribute("id", id);
        model.addAttribute("name", product.getName());
        model.addAttribute("price", product.getPrice());
        model.addAttribute("detailDesc", product.getDetailDesc());

        return "admin/product/detail";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String getDeleteProductPage(Model model, @PathVariable Long id) {
        model.addAttribute("id", id);
        model.addAttribute("deleteProduct", new Product());
        return "admin/product/delete";
    }

    @PostMapping("/admin/product/delete")
    public String postDeleteProduct(Model model, @ModelAttribute("deleteProduct") Product product) {
        this.productService.deleteAProduct(product.getId());
        return "redirect:/admin/product";
    }

    @RequestMapping("/admin/product/update/{id}")
    public String getUpdateUserPage(Model model, @PathVariable Long id) {
        Product currentProduct = this.productService.getProductById(id).get();
        model.addAttribute("productUpdate", currentProduct);
        return "admin/product/update";
    }

    @PostMapping("/admin/product/update")
    public String updateUser(Model model,
            @ModelAttribute("productUpdate") @Valid Product productU,
            BindingResult productBindingResult,
            @RequestParam("hoidanitFile") MultipartFile file) {

        // if (productBindingResult.hasErrors()) {
        // // Nếu có lỗi, lấy thông tin sản phẩm hiện tại từ cơ sở dữ liệu
        // Product currentProduct =
        // this.productService.getProductById(productU.getId()).get();

        // // Truyền thông tin về sản phẩm hiện tại vào model
        // model.addAttribute("productUpdate", currentProduct);

        // // Truyền đường dẫn ảnh cũ vào model nếu không có file mới
        // if (currentProduct.getImage() != null &&
        // !currentProduct.getImage().isEmpty()) {
        // model.addAttribute("imagePath", currentProduct.getImage());
        // model.addAttribute("imageName", new
        // File(currentProduct.getImage()).getName());
        // }

        // return "admin/product/update";
        // }
        if (productBindingResult.hasErrors()) {
            return "admin/product/update";
        }

        Product product = this.productService.getProductById(productU.getId()).get();

        if (product != null) {
            if (!file.isEmpty()) {
                String img = this.uploadService.handleSaveFile(file, "product");
                product.setImage(img);
            }
            product.setName(productU.getName());
            product.setPrice(productU.getPrice());
            product.setDetailDesc(productU.getDetailDesc());
            product.setShortDesc(productU.getShortDesc());
            product.setQuantity(productU.getQuantity());
            product.setFactory(productU.getFactory());
            product.setTarget(productU.getTarget());

            this.productService.createProduct(product);
        }

        return "redirect:/admin/product";
    }
}
