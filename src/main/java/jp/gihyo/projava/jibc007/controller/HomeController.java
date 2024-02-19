package jp.gihyo.projava.jibc007.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Controller
public class HomeController {
    private final BookList book;
    private final CommentService commentService;

    @Autowired
    HomeController(BookList book, CommentService commentService) {
        this.book = book;
        this.commentService =  commentService;
    }


    @RequestMapping("/")
    String hello(Model model) {
        model.addAttribute("time", LocalDateTime.now());
        return "index";
    }

    @GetMapping("/list")
    String list(Model model) {
        List<BookList.BookItem> bookItems = book.findAll();
        model.addAttribute("bookList", bookItems);
        return "index";
    }
    @GetMapping("/listcomment")
    String listcomment(Model model) {
        List<CommentService.Comment> comment = commentService.findAll();
        model.addAttribute("comments", comment);
        return "commentlist";
    }
    @GetMapping("/add")
    String addForm() {
        return "add";
    }

    @PostMapping("/add")
    String addItem(@RequestParam("name") String name,
                   @RequestParam("image") MultipartFile image) throws IOException {
        String id = UUID.randomUUID().toString().substring(0, 8);
        // Save the image to a directory (you may want to customize the path)
        try {
            String uploadDir = "C:\\Users\\phucn\\Downloads\\jibc007\\src\\main\\resources\\images\\";

            Path uploadPath = Paths.get(uploadDir);

            // Nếu thư mục không tồn tại, tạo mới
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Lưu tệp vào thư mục ảnh với tên gốc của nó
            Path filePath = uploadPath.resolve(image.getOriginalFilename());
//    Path filePath = uploadDirectory.resolve(image.getOriginalFilename());
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            BookList.BookItem item = new BookList.BookItem(id, name, image.getOriginalFilename());
            book.add(item);

        } catch (IOException e) {
            e.printStackTrace();

        }
        return "redirect:/list";
    }

    @GetMapping("/addComment")
    String addCommentForm() {

        return "addComment";
    }

    @PostMapping("/addComment")
    String addComment(@RequestParam("bookId") String bookId,
                      @RequestParam("commenterName") String commenterName,
                      @RequestParam("commentContent") String commentContent) {
        CommentService.Comment comment = new CommentService.Comment(bookId, commenterName, commentContent);
        commentService.add(comment);
        return "redirect:/listcomment"; // Chuyển hướng đến trang hiển thị danh sách bình luận
    }

    @GetMapping("/commentlist")
    public String commentList(Model model) {
        List<CommentService.Comment> comments = commentService.findAll();
        model.addAttribute("comments", comments);
        return "commentlist";
    }


}




