package vn.hoidanit.laptopshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import vn.hoidanit.laptopshop.domain.Cart;
import vn.hoidanit.laptopshop.domain.CartDetail;
import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.OrderDetail;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.Product_;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.domain.dto.ProductCriteriaDTO;
import vn.hoidanit.laptopshop.repository.CartDetailRepository;
import vn.hoidanit.laptopshop.repository.CartRepository;
import vn.hoidanit.laptopshop.repository.OrderDetailRepository;
import vn.hoidanit.laptopshop.repository.OrderRepository;
import vn.hoidanit.laptopshop.repository.ProductRepository;
import vn.hoidanit.laptopshop.service.specification.ProductSpec;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public ProductService(ProductRepository productRepository,
            CartRepository cartRepository,
            CartDetailRepository cartDetailRepository,
            UserService userService,
            OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.userService = userService;
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
    }

    public Product createProduct(Product product) {
        Product ntd = this.productRepository.save(product);
        return ntd;
    }

    public Page<Product> getAllProducts(Pageable page) {
        return this.productRepository.findAll(page);
    }

    public Page<Product> getAllProductsWithSpec(Pageable page, ProductCriteriaDTO productCriteriaDTO) {
        if (productCriteriaDTO.getTarget() == null
                && productCriteriaDTO.getFactory() == null
                && productCriteriaDTO.getPrice() == null) {
            return this.productRepository.findAll(page);
        }
        Specification<Product> combinedSpecs = Specification.where(null);
        if (productCriteriaDTO.getTarget() != null && productCriteriaDTO.getTarget().isPresent()) {
            Specification<Product> currentSpecs = ProductSpec.matchListTarget(productCriteriaDTO.getTarget().get());
            combinedSpecs = combinedSpecs.and(currentSpecs);
        }
        if (productCriteriaDTO.getFactory() != null && productCriteriaDTO.getFactory().isPresent()) {
            Specification<Product> currentSpecs = ProductSpec.matchListFactory(productCriteriaDTO.getFactory().get());
            combinedSpecs = combinedSpecs.and(currentSpecs);

        }
        if (productCriteriaDTO.getPrice() != null && productCriteriaDTO.getPrice().isPresent()) {
            Specification<Product> currentSpecs = this.getPricesSpecification(productCriteriaDTO.getPrice().get());
            combinedSpecs = combinedSpecs.and(currentSpecs);

        }
        return this.productRepository.findAll(combinedSpecs, page);
    }
    // public Page<Product> getAllProductsWithSpec(Pageable page, double minPrice) {
    // return this.productRepository.findAll(ProductSpec.minPrice(minPrice),page);
    // }
    // public Page<Product> getAllProductsWithSpec(Pageable page, double maxPrice) {
    // return this.productRepository.findAll(ProductSpec.maxPrice(maxPrice),page);
    // }

    // public Page<Product> getAllProductsWithSpec(Pageable page, List<String>
    // factory) {
    // return this.productRepository.findAll(ProductSpec.matchListFactory(factory),
    // page);
    // }

    // public Page<Product> getAllProductsWithSpec(Pageable page, String price) {
    // if (price.equals("10-toi-15-trieu")) {
    // double min = 10000000;
    // double max = 15000000;
    // return this.productRepository.findAll(ProductSpec.matchPrice(min, max),
    // page);
    // }
    // else if (price.equals("15-toi-30-trieu")) {

    // double min = 15000000;
    // double max = 30000000;
    // return this.productRepository.findAll(ProductSpec.matchPrice(min, max),
    // page);

    // }
    // else return this.productRepository.findAll(page);
    // }

    public Specification<Product> getPricesSpecification(List<String> prices) {
        Specification<Product> combinedSpecification = Specification.where(null);

        for (String pr : prices) {
            double min = 0;
            double max = 0;
            switch (pr) {
                case "duoi-10-trieu":
                    min = 1;
                    max = 10000000;

                    break;

                case "10-15-trieu":
                    min = 10000000;
                    max = 15000000;

                    break;

                case "15-20-trieu":
                    min = 15000000;
                    max = 20000000;

                    break;
                case "tren-20-trieu":
                    min = 20000000;
                    max = 300000000;
                    break;
            }
            if (min != 0 && max != 0) {
                Specification<Product> rangeSpec = ProductSpec.matchMultiPrice(min, max);
                combinedSpecification = combinedSpecification.or(rangeSpec);
            }
        }
        return combinedSpecification;

    }

    public Optional<Product> getProductById(Long id) {
        return this.productRepository.findById(id);
    }

    public void deleteAProduct(Long id) {
        this.productRepository.deleteById(id);
    }

    public void handleAddProductToCart(String email, long productId, HttpSession session, long quantity) {

        User user = this.userService.getUSerByEmail(email);
        // Check has user had cart? Not yet -> create new one
        if (user != null) {
            Cart cart = this.cartRepository.findByUser(user);
            if (cart == null) {
                Cart newCart = new Cart();
                newCart.setUser(user);
                newCart.setSum(0);

                cart = this.cartRepository.save(newCart);
            }

            // find product
            Optional<Product> productOptional = this.productRepository.findById(productId);
            if (productOptional.isPresent()) {
                Product realProduct = productOptional.get();
                // Check has product existed in cart?
                CartDetail oldDetail = this.cartDetailRepository.findByCartAndProduct(cart, realProduct);

                if (oldDetail == null) {
                    // Save cart detail
                    CartDetail cartDetail = new CartDetail();
                    cartDetail.setCart(cart);
                    cartDetail.setProduct(realProduct);
                    cartDetail.setPrice(realProduct.getPrice());
                    cartDetail.setQuantity(quantity);
                    this.cartDetailRepository.save(cartDetail);

                    int sumCart = cart.getSum() + 1;
                    cart.setSum(sumCart);

                    this.cartRepository.save(cart);
                    session.setAttribute("sum", sumCart);
                } else {
                    oldDetail.setQuantity(oldDetail.getQuantity() + quantity);
                    this.cartDetailRepository.save(oldDetail);
                }

            }

        }

    }

    public Cart getCartByUser(User user) {
        return this.cartRepository.findByUser(user);
    }

    public void deleteACartDetail(long id, HttpSession session) {
        Optional<CartDetail> cartDetailOptional = this.cartDetailRepository.findById(id);
        if (cartDetailOptional.isPresent()) {
            CartDetail cartDetail = cartDetailOptional.get();

            Cart cart = cartDetail.getCart();
            // delete cartDetail
            this.cartDetailRepository.deleteById(id);

            int cartSum = cart.getSum();
            if (cartSum > 1) {
                cart.setSum(cartSum - 1);
                session.setAttribute("sum", cart.getSum());
                this.cartRepository.save(cart);
            } else {
                this.cartRepository.deleteById(cart.getId());
                session.setAttribute("sum", 0);

            }

        }
    }

    // Hàm xử lý cập nhật lại trường Quantity của product trong cartDetail
    // Vì số lượng sản phẩm trong cartDetail có thể đã thay đổi từ view của người
    // dùng
    // Cụ thể là thay đổi Quantity khi nhấn nút Tăng hoặc Giảm từ file
    // client/cart/show.jsp
    public void handleUpdateCartBeforeCheckout(List<CartDetail> cartDetails) {
        for (CartDetail cartDetail : cartDetails) {
            Optional<CartDetail> cdOptional = this.cartDetailRepository.findById(cartDetail.getId());
            if (cdOptional.isPresent()) {
                CartDetail currentDetail = cdOptional.get();
                currentDetail.setQuantity(cartDetail.getQuantity());
                this.cartDetailRepository.save(currentDetail);
            }
        }
    }

    public void handlePlaceOrder(User user, HttpSession session,
            String receiverName,
            String receiverAddress,
            String receiverPhone) {

        // Create Order detail
        // Step 1
        Cart cart = this.cartRepository.findByUser(user);
        if (cart != null) {
            List<CartDetail> cartDetails = cart.getCartDetails();
            if (cartDetails != null) {

                // Create Order
                Order order = new Order();
                order.setUser(user);
                order.setReceiverName(receiverName);
                order.setReceiverAddress(receiverAddress);
                order.setReceiverPhone(receiverPhone);
                order.setStatus("PENDING");

                double sum = 0;
                for (CartDetail cd : cartDetails) {
                    sum += cd.getPrice();
                }
                order.setTotalPrice(sum);
                order = this.orderRepository.save(order);

                for (CartDetail cd : cartDetails) {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrder(order);
                    orderDetail.setProduct(cd.getProduct());
                    orderDetail.setQuantity(cd.getQuantity());
                    orderDetail.setPrice(cd.getPrice());
                    this.orderDetailRepository.save(orderDetail);
                }
            }
            // Step 2 delete cartDetail and cart
            for (CartDetail cd : cartDetails) {
                this.cartDetailRepository.deleteById(cd.getId());
            }
            this.cartRepository.deleteById(cart.getId());

            // Step 3 Update session
            // Thanh toan het cart nen set cart.sum = 0
            session.setAttribute("sum", 0);
        }
    }
}
