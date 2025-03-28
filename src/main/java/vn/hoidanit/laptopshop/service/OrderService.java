package vn.hoidanit.laptopshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.OrderDetail;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.OrderDetailRepository;
import vn.hoidanit.laptopshop.repository.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public OrderService(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }
    public Page<Order> getAllOrders(Pageable page) {
        return this.orderRepository.findAll(page);
    }
    public Optional<Order> getOrderById(long id){
        return this.orderRepository.findById(id);
    }
    public void handleUpdateOrder(Order order){
        Order upOrder = this.orderRepository.findById(order.getId()).get();
        upOrder.setStatus(order.getStatus());
        this.orderRepository.save(upOrder);
    }
    public void handleDeleteOrder(Order order){
        Order delOrder = this.orderRepository.findById(order.getId()).get();
        // delete order_detail 
        List<OrderDetail> orderDetails = delOrder.getOrderDetails();
        for (OrderDetail cd : orderDetails) {
            this.orderDetailRepository.deleteById(cd.getId());;
        }
        this.orderRepository.deleteById(order.getId());

    }
    public List<Order> findOrdersByUser(User user) {
        return this.orderRepository.findByUser(user);
    }
    
}
