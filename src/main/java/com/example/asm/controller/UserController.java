package com.example.asm.controller;

import com.example.asm.model.*;
import com.example.asm.repository.CartDetailRepository;
import com.example.asm.repository.CartRepository;
import com.example.asm.service.KhachHangService;
import com.example.asm.service.LoginService;
import com.example.asm.service.UserService;
import com.example.asm.service.impl.CartImpl;
import com.example.asm.util.LoginSession;
import com.example.asm.util.Timestamp;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    private final KhachHangService khachHangService;
    private final LoginSession loginSession;
    private final LoginService loginService;
    private final CartImpl cartsService;
    private final CartRepository cartRepository;
    private final CartDetailRepository orderDetailRepository;

    public UserController(KhachHangService khachHangService, LoginSession loginSession, LoginService loginService, CartImpl cartsService, CartRepository cartRepository, CartDetailRepository orderDetailRepository) {
        this.khachHangService = khachHangService;
        this.loginSession = loginSession;
        this.loginService = loginService;
        this.cartsService = cartsService;
        this.cartRepository = cartRepository;
        this.orderDetailRepository = orderDetailRepository;
    }
//    @RequestMapping("/")
//    public String home(Model model) {
//        List<SanPhamChiTiet> listProductDetail = userService.getListProducdetail();
//        List<SanPhamChiTiet> listProductNew = userService.getListProductdetailByCreatedAt();
//        List<Category> listCate = userService.getListCategory();
//        List<Category> listCateNew = userService.getListCategoryByCreatedAt();
//        model.addAttribute("listProductDetail", listProductDetail);
//        model.addAttribute("listCate",listCate);
//        model.addAttribute("listProductNew", listProductNew);
//        model.addAttribute("listCateNew", listCateNew);
//        return "/user/index";
//    }

    @RequestMapping("/")
    public String home(Model model) {
        List<SanPhamChiTiet> listProductDetail = userService.getListProducdetail();
        List<SanPhamChiTiet> listProductNew = userService.getListProductdetailByCreatedAt();
        List<Category> listCate = userService.getListCategory();
        List<Category> listCateNew = userService.getListCategoryByCreatedAt();
        model.addAttribute("listProductDetail", listProductDetail);
        model.addAttribute("listCate", listCate); // This adds the listCate to the model
        model.addAttribute("listProductNew", listProductNew);
        model.addAttribute("listCateNew", listCateNew);
        return "/user/index";
    }

    @RequestMapping("/all")
    public String getAllProduct(Model model) {
        List<SanPhamChiTiet> listProductDetail = userService.getListProducdetail();
        List<MauSac> listColor = userService.getListColor();
        List<KichThuoc> listSize = userService.getListSize();
        List<Category> listCate = userService.getListCategory();
        model.addAttribute("listProductDetail", listProductDetail);
        model.addAttribute("listCate", listCate);
        model.addAttribute("listColor", listColor);
        model.addAttribute("listSize", listSize);
        return "/user/store";
    }

//    @RequestMapping("/all")
//    public String getAllProduct(@RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
//                                @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
//                                Model model) {
//        // Nếu không có giá trị cho minPrice và maxPrice, sử dụng giá trị mặc định hoặc thực hiện xử lý khác tùy thuộc vào yêu cầu của bạn
//        if (minPrice == null) {
//            minPrice = BigDecimal.ZERO; // Giá trị mặc định là 0
//        }
//        if (maxPrice == null) {
//            maxPrice = BigDecimal.valueOf(Double.MAX_VALUE); // Giá trị mặc định là giá trị lớn nhất cho kiểu double
//        }
//
//        // Lấy danh sách sản phẩm theo khoảng giá
//        List<Productdetail> productDetails = userService.filterByPriceRange(minPrice, maxPrice);
//
//        // Thêm các danh sách cần thiết vào model
//        model.addAttribute("minPrice", minPrice);
//        model.addAttribute("maxPrice", maxPrice);
//        model.addAttribute("listProductDetail", productDetails);
//        model.addAttribute("listColor", userService.getListColor());
//        model.addAttribute("listSize", userService.getListSize());
//        model.addAttribute("listBrand", userService.getListBrand());
//        model.addAttribute("listCategory", userService.getListCategory());
//
//        return "/user/store";
//    }


    // Hàm này để lấy ra sản phẩm khi chúng ta click danh mục
    @RequestMapping("/cate")
    public String getProductDetailByCategory(@RequestParam("id") Integer id, Model model) {
        List<SanPhamChiTiet> productDetails = userService.getProductDetailByCategory(id);
        model.addAttribute("listProductDetail", productDetails);
        List<MauSac> listColor = userService.getListColor();
        List<KichThuoc> listSize = userService.getListSize();
        List<Category> listCategory = userService.getListCategory();
        model.addAttribute("listColor", listColor);
        model.addAttribute("listSize", listSize);
        model.addAttribute("listCategory", listCategory);
        return "user/store";
    }

    // Hàm này để lấy ra sản phẩm bằng màu sắc
    @RequestMapping("/color")
    public String getProductDetailByColor(@RequestParam("id") Integer id, Model model) {
        List<SanPhamChiTiet> productDetails = userService.getProductDetailByColor(id);
        model.addAttribute("listProductDetail", productDetails);
        List<MauSac> listColor = userService.getListColor();
        List<KichThuoc> listSize = userService.getListSize();
        List<Category> listCategory = userService.getListCategory();
        model.addAttribute("listColor", listColor);
        model.addAttribute("listSize", listSize);
        model.addAttribute("listCategory", listCategory);
        return "user/store";
    }

    // Hàm này để lấy ra sản phẩm bằng size
    @RequestMapping("/size")
    public String getProductDetailBySize(@RequestParam("id") Integer id, Model model) {
        List<SanPhamChiTiet> productDetails = userService.getProductDetailBySize(id);
        model.addAttribute("listProductDetail", productDetails);
        List<MauSac> listColor = userService.getListColor();
        List<KichThuoc> listSize = userService.getListSize();
        List<Category> listCategory = userService.getListCategory();
        model.addAttribute("listColor", listColor);
        model.addAttribute("listSize", listSize);
        model.addAttribute("listCategory", listCategory);
        return "user/store";
    }

    // Hàm này để lấy ra chi tiết sản phẩm bằng id của sản phẩm đó
    @RequestMapping("/product")
    public String updateProduct(@RequestParam("id") Integer id, Model model) {
        List<SanPhamChiTiet> listProductDetail = userService.getListProducdetail();
        List<Category> listCate = userService.getListCategory();
        List<MauSac> listColor = userService.getListColor();
        List<KichThuoc> listSize = userService.getListSize();
        model.addAttribute("listCate", listCate);
        model.addAttribute("listColor", listColor);
        model.addAttribute("listSize", listSize);
        model.addAttribute("listProductDetail", listProductDetail);
        Optional<SanPhamChiTiet> optProductDetail = userService.findProductdetailById(id);
        optProductDetail.ifPresent(productdetail -> model.addAttribute("productDetail", productdetail));
        return "user/product";
    }

    @GetMapping("/ViewDangNhap")
    public String loginForm(Model model) {
        System.out.println("Bắt đầu login");
        model.addAttribute("khachHang", new KhachHang());
        return "/user/layout/login";
    }

    @Autowired
    private HttpSession session;

    @PostMapping("/dangNhap")
    public String loginSubmit(@ModelAttribute KhachHang khachHang, Model model) {
        KhachHang foundNhanVien = khachHangService.findKhachHangByUsernameAndPassword(khachHang.getUsername(), khachHang.getPassword());
        if (foundNhanVien != null) {
            LoginSession.setKhachHang(foundNhanVien);
            session.setAttribute("khachHang", LoginSession.khachHang);
            if (LoginSession.isKhachHangLogin()) {
                return "redirect:/";
            }
        } else {
            model.addAttribute("error", "Đăng nhập thất bại. Vui lòng kiểm tra lại tài khoản và mật khẩu.");
            return "/user/layout/login";
        }
        return "/user/layout/login";
    }

    @ModelAttribute("cart")
    CartImpl getCartService() {
        return cartsService;
    }

    @GetMapping("/gioHang")
    public String cart(Model model) {
//        if (LoginSession.isLogin()) {
//            return "cart";
//        } else {
//            model.addAttribute("khachHang", new KhachHang());
//            return "login";
//        }
        return "user/cart";
    }


    // Hàm này dùng để thêm sản phẩm vào giỏ hàng
    @RequestMapping("/add-to-cart/{id}")
    public String addToCart(@PathVariable("id") Integer id, Model model) {
//        if (LoginSession.isLogin()) {
//            System.out.println("bat dau them san pham chi tiet");
//            cartsService.add(id);
//            return "redirect:/gioHang";
//        } else {
//            model.addAttribute("khachHang", new NhanVien());
//            return "login";
//        }
        cartsService.add(id);
        return "redirect:/gioHang";
    }

    @RequestMapping("/update-cart/{id}")
    public String updateCart(@PathVariable("id") Integer id, @RequestParam("quantity") Integer quantity, Model model) {
//        if (LoginSession.isLogin()) {
//            cartsService.update(id, quantity);
//            return "redirect:/gioHang";
//        } else {
//            model.addAttribute("nhanVien", new Accounts());
//            return "login";
//        }
        cartsService.update(id, quantity);
        return "redirect:/gioHang";
    }

    @RequestMapping("/remove-cart/{id}")
    public String removeCart(@PathVariable("id") Integer id, Model model) {
//        if (LoginSession.isLogin()) {
//            cartsService.remove(id);
//            return "redirect:/gioHang";
//        } else {
//            model.addAttribute("nhanVien", new Accounts());
//            return "login";
//        }
        cartsService.remove(id);
        return "redirect:/gioHang";
    }

    @GetMapping("/checkout")
    public String confirm() {
//        if (LoginSession.isAdmin()) {
//            return "checkout";
//        }
//        return "checkout";
        return "user/checkout";
    }


    @PostMapping("/purchase")
    public String purchase(@RequestParam String address, Model model) {
        if (LoginSession.isLogin()) {
            System.out.println("address=" + address);
            System.out.println("items=" + cartsService.getItems());
            KhachHang acc = LoginSession.getAccount();
            System.out.println(acc);
            System.out.println("Account : " + acc.getUsername() + " Password : " + acc.getPassword());
            if (acc != null) {
                Cart order = new Cart();
                order.setKhachHang(acc);
                order.setDiaChi(address);
                order.setCreatedAt(Timestamp.timeNow()); // Sử dụng phương thức timeNow()
                cartRepository.save(order);

                // Lưu chi tiết đơn hàng
                for (CartDetail item : cartsService.getItems()) {
                    item.setCart(order);
                    orderDetailRepository.save(item);
                }
            }
            cartsService.clear();
            return "redirect:/";
        } else {
            model.addAttribute("khachHang", new KhachHang());
            return "/user/layout/login";
        }
    }

}
