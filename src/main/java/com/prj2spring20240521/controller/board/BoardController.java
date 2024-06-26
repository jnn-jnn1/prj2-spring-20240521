package com.prj2spring20240521.controller.board;

import com.prj2spring20240521.domain.board.Board;
import com.prj2spring20240521.service.board.BoardService;
import com.prj2spring20240521.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService service;
    private final MemberService memberService;

    @PostMapping("add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity add(
            Authentication authentication,
            Board board, @RequestParam(value = "files[]", required = false) MultipartFile[] files) throws IOException {

        if (service.validate(board)) {
            service.add(board, files, authentication);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("list")
    public Map<String, Object> list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(value = "type", required = false) String searchType,
                                    @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        return service.list(page, searchType, keyword);
    }

    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable Integer id, Authentication authentication) {
        Map<String, Object> result = service.get(id, authentication);
        if (result.get("board") == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity delete(@PathVariable Integer id, Authentication authentication) {
        if (service.hasAccess(id, authentication)) {
            service.remove(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("edit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity edit(Board board, Authentication authentication,
                               @RequestParam(value = "removeFileList[]", required = false) List<String> removeFileList,
                               @RequestParam(value = "addFileList[]", required = false) MultipartFile[] addFileList) throws IOException {

        if (!service.hasAccess(board.getId(), authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (service.validate(board)) {
            service.edit(board, removeFileList, addFileList);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("like")
    @PreAuthorize("isAuthenticated()")
    public Map<String, Object> like(@RequestBody Map<String, Object> req, Authentication authentication) {
        return service.like(req, authentication);
    }
}
