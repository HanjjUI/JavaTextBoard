package com.project.board.controller;

import com.project.board.common.SessionNames;
import com.project.board.dto.BoardDto;
import com.project.board.service.BoardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/board")
public class BoardController {

    private static final int DEFAULT_PAGE_SIZE = 100;
    private static final String LOGIN_REQUIRED = "LOGIN_REQUIRED";
    private static final Path UPLOAD_DIR = Path.of("uploads");
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("pdf", "jpg", "jpeg", "png", "gif", "webp", "exe");

    private final BoardService service;

    public BoardController(BoardService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public List<BoardDto> list(
            @RequestParam(defaultValue = "titleAuthor") String searchType,
            @RequestParam(defaultValue = "") String keyword
    ) {
        PageRequest pageRequest = PageRequest.of(
                0,
                DEFAULT_PAGE_SIZE,
                Sort.by(Sort.Direction.DESC, "id")
        );

        return service.findAll(pageRequest, searchType, keyword).getContent();
    }

    @GetMapping("/{id}")
    public BoardDto detail(
            @PathVariable Long id,
            @RequestParam(defaultValue = "true") boolean increaseViewCount
    ) {
        return service.findById(id, increaseViewCount);
    }

    @PostMapping("/write")
    public String write(@RequestBody BoardDto dto, HttpSession session) {
        String user = getLoginUser(session);

        if (user == null) {
            return LOGIN_REQUIRED;
        }

        service.save(dto, user);
        return "OK";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file, HttpSession session) throws IOException {
        if (getLoginUser(session) == null) {
            return LOGIN_REQUIRED;
        }

        String extension = getAllowedExtension(file);
        if (file.isEmpty() || extension == null) {
            return "INVALID_FILE";
        }

        Files.createDirectories(UPLOAD_DIR);

        String filename = UUID.randomUUID() + "." + extension;
        Path target = UPLOAD_DIR.resolve(filename);
        file.transferTo(target);

        return "/uploads/" + filename;
    }

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> download(@PathVariable String filename) throws IOException {
        Path file = UPLOAD_DIR.resolve(filename).normalize();
        Path uploadDir = UPLOAD_DIR.toAbsolutePath().normalize();
        Path absoluteFile = file.toAbsolutePath().normalize();

        if (!absoluteFile.startsWith(uploadDir) || !Files.exists(absoluteFile)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(absoluteFile.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(filename)
                        .build()
                        .toString())
                .body(resource);
    }

    @PutMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @RequestBody BoardDto dto,
            HttpSession session
    ) {
        String user = getLoginUser(session);

        if (user == null) {
            return LOGIN_REQUIRED;
        }

        service.update(id, dto, user);
        return "OK";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        String user = getLoginUser(session);

        if (user == null) {
            return LOGIN_REQUIRED;
        }

        service.delete(id, user);
        return "OK";
    }

    private String getLoginUser(HttpSession session) {
        return (String) session.getAttribute(SessionNames.LOGIN_USER);
    }

    private String getAllowedExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            return null;
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1)
                .toLowerCase(Locale.ROOT);

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            return null;
        }

        return extension;
    }
}
