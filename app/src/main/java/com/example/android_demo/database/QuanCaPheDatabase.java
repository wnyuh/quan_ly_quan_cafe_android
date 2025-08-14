package com.example.android_demo.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.android_demo.dao.*;
import com.example.android_demo.entity.*;
import java.util.Date;

@Database(
    entities = {
        NguoiDung.class,
        DanhMucSanPham.class, 
        SanPham.class,
        DonHang.class,
        ChiTietDonHang.class,
        Ban.class,
        PhieuPhucVu.class
    },
    version = 3,
    exportSchema = false
)
@TypeConverters({DateConverter.class})
public abstract class QuanCaPheDatabase extends RoomDatabase {
    
    private static volatile QuanCaPheDatabase INSTANCE;
    
    public abstract NguoiDungDao nguoiDungDao();
    public abstract DanhMucSanPhamDao danhMucSanPhamDao();
    public abstract SanPhamDao sanPhamDao();
    public abstract DonHangDao donHangDao();
    public abstract ChiTietDonHangDao chiTietDonHangDao();
    public abstract BanDao banDao();
    public abstract PhieuPhucVuDao phieuPhucVuDao();
    
    public static QuanCaPheDatabase layDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (QuanCaPheDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        QuanCaPheDatabase.class,
                        "quan_ca_phe_database"
                    )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // Chỉ để development, nên xóa trong production
                    .build();

                    khoiTaoDuLieuMau();
                }
            }
        }
        return INSTANCE;
    }
    
    private static void khoiTaoDuLieuMau() {
        new Thread(() -> {
            try {
                // Kiểm tra xem đã có dữ liệu mẫu chưa
                if (INSTANCE.nguoiDungDao().layTatCaNguoiDung().size() > 0) {
                    return; // Đã có dữ liệu, không tạo lại
                }
                
                // Tạo người dùng mẫu với vai trò đúng
                INSTANCE.nguoiDungDao().themNguoiDung(new NguoiDung("admin", "admin123", "ADMIN", "Quản trị viên", "admin@quancaphe.com"));
                INSTANCE.nguoiDungDao().themNguoiDung(new NguoiDung("nhanvien1", "nv123", "NHAN_VIEN", "Nguyễn Văn An", "nhanvien1@quancaphe.com"));
                INSTANCE.nguoiDungDao().themNguoiDung(new NguoiDung("nhanvien2", "nv456", "NHAN_VIEN", "Trần Thị Bình", "nhanvien2@quancaphe.com"));
                
                // Tạo danh mục sản phẩm
                long danhMucCaPheId = INSTANCE.danhMucSanPhamDao().themDanhMuc(
                    new DanhMucSanPham("Cà Phê", "Các loại cà phê truyền thống và hiện đại"));
                long danhMucTraId = INSTANCE.danhMucSanPhamDao().themDanhMuc(
                    new DanhMucSanPham("Trà", "Các loại trà và đồ uống từ trà"));
                long danhMucBanhId = INSTANCE.danhMucSanPhamDao().themDanhMuc(
                    new DanhMucSanPham("Bánh Kẹo", "Bánh ngọt, bánh mặn và các loại kẹo"));
                long danhMucDoUongId = INSTANCE.danhMucSanPhamDao().themDanhMuc(
                    new DanhMucSanPham("Đồ Uống Khác", "Nước ép, sinh tố, nước giải khát"));
                
                // Tạo sản phẩm cà phê
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Cà Phê Đen", 25000, (int)danhMucCaPheId, "https://gacoffeeandmore.com/wp-content/uploads/2022/12/Ca-Phe-Den-scaled.jpg", "Cà phê đen truyền thống"));
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Cà Phê Sữa", 30000, (int)danhMucCaPheId, "https://product.hstatic.net/200000264775/product/ca_phe_ae27c2fffbc4489884c2efbef3d639bb_master.jpg", "Cà phê với sữa đặc"));
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Cappuccino", 45000, (int)danhMucCaPheId, "https://truongnauan.com/test_disk/photos/shares/nguyen-lieu-pha-che/ca-phe-capuchino-la-gi/ca-phe-cappuccino-%20la-gi.jpg", "Cà phê Ý với bọt sữa"));
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Latte", 50000, (int)danhMucCaPheId, "https://90sstore.vn/wp-content/uploads/2023/01/cach-lam-matcha-latte-nong.jpg", "Cà phê với nhiều sữa"));
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Americano", 35000, (int)danhMucCaPheId, "https://www.cubes-asia.com/storage/blogs/2023/cafe-americano.jpg", "Cà phê pha loãng"));
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Espresso", 30000, (int)danhMucCaPheId, "https://file.hstatic.net/1000075078/article/blog_f80b599183c340bca744c174e7ab2af8.jpg", "Cà phê espresso đậm đà"));
                
                // Tạo sản phẩm trà
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Trà Đá", 15000, (int)danhMucTraId, "https://product.hstatic.net/200000480127/product/tra_da_da927d9754664321a3e18e3a97adbb1a_master.jpg", "Trà đá truyền thống"));
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Trà Sữa Trân Châu", 40000, (int)danhMucTraId, "https://dayphache.edu.vn/wp-content/uploads/2020/02/mon-tra-sua-tran-chau.jpg", "Trà sữa với trân châu"));
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Trà Xanh", 20000, (int)danhMucTraId, "https://media.vneconomy.vn/images/upload/2021/04/21/5-tac-hai-to-lon-khi-uong-tra-xanh-khong-dung-cach-4-15572834476062046433123.jpg", "Trà xanh thơm mát"));
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Trà Ô Long", 25000, (int)danhMucTraId, "https://media.istockphoto.com/id/1294558107/vi/anh/ly-tr%C3%A0-%C4%91%C3%A1-tr%C3%AAn-b%C3%A0n-g%E1%BB%97-cuba-libre-ho%E1%BA%B7c-cocktail-%C4%91%E1%BA%A3o-d%C3%A0i.jpg?s=612x612&w=0&k=20&c=8d32tLAySmTeDhjqCBBZhqdGRaJ0rG4kPHtvWIt8s5k=", "Trà ô long cao cấp"));
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Trà Chanh", 18000, (int)danhMucTraId, "https://www.unileverfoodsolutions.com.vn/dam/global-ufs/mcos/phvn/vietnam/calcmenu/recipes/VN-recipes/other/energizing-lemon-tea/main-header.jpg", "Trà chanh tươi mát"));
                
                // Tạo sản phẩm bánh kẹo
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Bánh Croissant", 35000, (int)danhMucBanhId, "https://static.hawonkoo.vn/hwks1/images/2023/08/cach-lam-banh-croissant-bang-noi-chien-khong-dau-3.jpg", "Bánh sừng bò Pháp"));
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Bánh Tiramisu", 55000, (int)danhMucBanhId, "https://file.hstatic.net/200000164551/file/2_58e61d24cef140c7bdeee2c3e5899ff7_grande.png", "Bánh Tiramisu Ý"));
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Bánh Mì Sandwich", 40000, (int)danhMucBanhId, "https://cdn.nhathuoclongchau.com.vn/unsafe/800x0/filters:quality(95)/https://cms-prod.s3-sgn09.fptcloud.com/mach_ban_6_cach_lam_banh_mi_sandwich_day_du_dinh_duong_1_8756a3e8e2.png", "Bánh mì sandwich thịt nguội"));
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Kẹo Dẻo", 10000, (int)danhMucBanhId, "https://images.unsplash.com/photo-1582058091505-f87a2e55a40f?w=400&h=400&fit=crop", "Kẹo dẻo nhiều vị"));
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Bánh Muffin", 25000, (int)danhMucBanhId, "https://www.lottemart.vn/media/catalog/product/cache/0x0/0/4/0400233910006.jpg.webp", "Bánh muffin chocolate chip"));
                
                // Tạo đồ uống khác
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Nước Cam Tươi", 22000, (int)danhMucDoUongId, "https://images.unsplash.com/photo-1600271886742-f049cd451bba?w=400&h=400&fit=crop", "Nước cam vắt tươi"));
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Sinh Tố Xoài", 28000, (int)danhMucDoUongId, "https://quangon.vn/resources/2020/04/03/cach-lam-sinh-to-xoai--2.jpg", "Sinh tố xoài tươi"));
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Sinh Tố Bơ", 15000, (int)danhMucDoUongId, "https://images.prismic.io/nutriinfo/aBHRavIqRLdaBvLz_hinh-anh-sinh-to-bo.jpg?auto=format,compress", "Sinh tố bơ tươi"));
                
                // Tạo dữ liệu bàn
                INSTANCE.banDao().themBan(new Ban(1, "Khu A", 2, "TRONG", "Bàn đôi cạnh cửa sổ"));
                INSTANCE.banDao().themBan(new Ban(2, "Khu A", 2, "TRONG", "Bàn đôi góc"));
                INSTANCE.banDao().themBan(new Ban(3, "Khu A", 4, "TRONG", "Bàn 4 người trung tâm"));
                INSTANCE.banDao().themBan(new Ban(4, "Khu B", 2, "TRONG", "Bàn đôi yên tĩnh"));
                INSTANCE.banDao().themBan(new Ban(5, "Khu B", 4, "TRONG", "Bàn 4 người"));
                INSTANCE.banDao().themBan(new Ban(6, "Khu B", 6, "TRONG", "Bàn nhóm lớn"));
                INSTANCE.banDao().themBan(new Ban(7, "Khu C", 2, "TRONG", "Bàn bar"));
                INSTANCE.banDao().themBan(new Ban(8, "Khu C", 2, "TRONG", "Bàn bar"));
                INSTANCE.banDao().themBan(new Ban(9, "Sân thượng", 4, "TRONG", "Bàn ngoài trời"));
                INSTANCE.banDao().themBan(new Ban(10, "Sân thượng", 4, "TRONG", "Bàn ngoài trời"));
                INSTANCE.banDao().themBan(new Ban(11, "VIP", 2, "TRONG", "Phòng riêng đôi"));
                INSTANCE.banDao().themBan(new Ban(12, "VIP", 6, "TRONG", "Phòng riêng nhóm"));
                
                // Tạo một số phiếu phục vụ mẫu (bàn đang được sử dụng)
                Date thoiGianHienTai = new Date();
                long phieuPhucVu1 = INSTANCE.phieuPhucVuDao().themPhieuPhucVu(
                    new PhieuPhucVu(1, 2, new Date(thoiGianHienTai.getTime() - 1800000), "DANG_PHUC_VU", 2)); // 30 phút trước
                long phieuPhucVu2 = INSTANCE.phieuPhucVuDao().themPhieuPhucVu(
                    new PhieuPhucVu(3, 2, new Date(thoiGianHienTai.getTime() - 3600000), "DANG_PHUC_VU", 4)); // 1 giờ trước
                
                // Cập nhật trạng thái bàn đang được sử dụng
                INSTANCE.banDao().capNhatTrangThaiBan(1, "DANG_PHUC_VU");
                INSTANCE.banDao().capNhatTrangThaiBan(3, "DANG_PHUC_VU");
                
                // Tạo một số đơn hàng mẫu cho các bàn đang phục vụ
                INSTANCE.donHangDao().themDonHang(new DonHang(2, 1, (int)phieuPhucVu1, 
                    new Date(thoiGianHienTai.getTime() - 1500000), 55000, "DANG_CHO"));
                INSTANCE.donHangDao().themDonHang(new DonHang(2, 3, (int)phieuPhucVu2, 
                    new Date(thoiGianHienTai.getTime() - 3300000), 120000, "DANG_CHO"));
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    public static void dongDatabase() {
        if (INSTANCE != null) {
            INSTANCE.close();
            INSTANCE = null;
        }
    }
}

